//
// File: WsdlCatalogFetcher.java
//
// Generated: google ai
//
// Prompts:
//
// Date: Nov 2026
//

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.*;
import javax.net.ssl.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class WsdlCatalogFetcher {

    private record Fetch(URI uri, byte[] body) {}

    public static void main(String[] args) throws Exception {
        List<String> pos = new ArrayList<>();
        boolean insecure = false;
        for (String a : args) {
            if ("--insecure".equals(a)) insecure = true;
            else if (!a.isBlank()) pos.add(a.trim());
        }
        if (pos.size() < 2) {
            System.err.println("Usage: java WsdlCatalogFetcher.java <BASE_DIR> <WSDL_URL> [--insecure]");
            System.exit(1);
        }

        List<String> wsdlUrls = new ArrayList<>();
        for (String arg : pos.subList(1, pos.size())) {
            for (String url : arg.split(",")) {
                String trimmed = url.trim();
                if (!trimmed.isEmpty()) {
                    wsdlUrls.add(trimmed);
                }
            }
        }

        Path meta = Path.of(pos.get(0)).resolve("src/main/resources/META-INF");
        HttpClient client = client(insecure);
        Map<String, String> map = crawl(client, wsdlUrls, meta.resolve("wsdl"));
        writeCatalog(map, meta.resolve("jax-ws-catalog.xml"));
    }

    private static HttpClient client(boolean insecure) throws Exception {
        var b = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(15));
        if (insecure) {
            TrustManager[] tm = { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                public void checkClientTrusted(X509Certificate[] c, String a) {}
                public void checkServerTrusted(X509Certificate[] c, String a) {}
            }};
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, tm, new SecureRandom());
            b.sslContext(ctx);
        }
        return b.build();
    }

    private static Fetch fetch(HttpClient c, String url) throws Exception {
        URI u = URI.create(url);
        for (int i = 0; i <= 10; i++) {
            var req = HttpRequest.newBuilder(u)
                .header("User-Agent", "WsdlCatalogFetcher/4.0")
                .timeout(Duration.ofSeconds(20)).GET().build();
            var r = c.send(req, HttpResponse.BodyHandlers.ofByteArray());
            int s = r.statusCode();
            if (s == 301 || s == 302 || s == 303 || s == 307 || s == 308) {
                u = u.resolve(r.headers().firstValue("Location").orElseThrow());
                System.out.println("  -> Redirect (" + s + "): " + u);
                continue;
            }
            if (s >= 400) throw new RuntimeException("HTTP " + s);
            return new Fetch(u, r.body());
        }
        throw new RuntimeException("Too many redirects: " + url);
    }

    private static Map<String, String> crawl(HttpClient c, List<String> seeds, Path wsdlDir) throws Exception {
        Files.createDirectories(wsdlDir);
        Queue<String> q = new ArrayDeque<>(new LinkedHashSet<>(seeds));
        Map<String, String> map = new LinkedHashMap<>();

        while (!q.isEmpty()) {
            String url = q.poll();
            if (map.containsKey(url)) continue;
            System.out.println("Downloading: " + url);
            Fetch res;
            try { res = fetch(c, url); }
            catch (Exception e) { System.err.println("  [WARN] " + url + ": " + e.getMessage()); continue; }

            String fin = res.uri().toString();
            String rel = map.get(fin);
            if (rel == null) {
                Path p = mirrorPath(res.uri(), res.body());
                Path full = wsdlDir.resolve(p);
                mkParentDirs(full.getParent());
                Files.write(full, res.body());
                rel = "wsdl/" + p.toString().replace('\\', '/');
                System.out.println("  -> Saved: " + full);
            }
            map.put(url, rel);
            map.put(fin, rel);

            for (String d : deps(res.body(), res.uri(), url)) {
                if (!map.containsKey(d)) {
                    System.out.println("     + Queued: " + d);
                    q.add(d);
                }
            }
        }
        return map;
    }

    private static void mkParentDirs(Path dir) throws Exception {
        if (dir == null) return;
        Path cur = dir.isAbsolute() ? dir.getRoot() : Path.of("");
        for (Path part : dir) {
            cur = cur.resolve(part);
            if (Files.isRegularFile(cur)) Files.delete(cur);
        }
        Files.createDirectories(dir);
    }

    private static Path mirrorPath(URI uri, byte[] data) {
        String host = (uri.getHost() != null ? uri.getHost() : "local").replaceAll("[^a-zA-Z0-9.-]", "_");
        if (uri.getPort() != -1) host += "_" + uri.getPort();

        String raw = uri.getPath() == null ? "" : uri.getPath();
        List<String> segs = new ArrayList<>(List.of(host));
        for (String s : raw.split("/")) {
            if (!s.isEmpty() && !s.equals(".") && !s.equals("..")) segs.add(s.replaceAll("[^a-zA-Z0-9._-]", "_"));
        }

        String head = new String(data, 0, Math.min(data.length, 512), StandardCharsets.UTF_8);
        String ext = (head.contains("<wsdl:definitions") || head.contains("<definitions")) ? ".wsdl" : ".xsd";
        if (raw.isEmpty() || raw.endsWith("/")) segs.add("index" + ext);

        int last = segs.size() - 1;
        String f = segs.get(last);
        if (uri.getQuery() != null && !uri.getQuery().isEmpty()) {
            String q = "_" + uri.getQuery().replaceAll("[^a-zA-Z0-9._-]", "_");
            int dot = f.lastIndexOf('.');
            f = (dot > 0) ? f.substring(0, dot) + q + f.substring(dot) : f + q;
        }
        if (!f.matches(".*\\.(wsdl|xsd|xml|dtd)$")) f += ext;
        segs.set(last, f);

        return Path.of(segs.get(0), segs.subList(1, segs.size()).toArray(new String[0]));
    }

    private static List<String> deps(byte[] raw, URI base, String src) {
        List<String> out = new ArrayList<>();
        if (src.endsWith(".dtd")) return out;
        byte[] xml = (raw.length >= 3 && raw[0] == (byte) 0xEF && raw[1] == (byte) 0xBB && raw[2] == (byte) 0xBF)
            ? Arrays.copyOfRange(raw, 3, raw.length) : raw;
        try {
            var dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setEntityResolver((p, s) -> new InputSource(new StringReader("")));
            NodeList nodes = db.parse(new ByteArrayInputStream(xml)).getElementsByTagName("*");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element el = (Element) nodes.item(i);
                for (String a : new String[]{"schemaLocation", "location"}) {
                    String v = el.getAttribute(a).trim();
                    if (!v.isEmpty()) out.add(base.resolve(v).normalize().toString());
                }
            }
        } catch (Exception e) {
            System.err.println("  [WARN] Parse failed for " + src + ": " + e.getMessage());
        }
        return out;
    }

    private static void writeCatalog(Map<String, String> map, Path cat) throws Exception {
        Files.createDirectories(cat.getParent());
        String lt = "\u003C";
        var sb = new StringBuilder();
        sb.append(lt).append("?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append(lt).append("catalog xmlns=\"urn:oasis:names:tc:entity:xmlns:xml:catalog\" prefer=\"system\">\n");

        Set<String> seen = new HashSet<>();
        Set<String> hosts = new LinkedHashSet<>();

        for (var e : map.entrySet()) {
            String u = e.getKey(), t = e.getValue();
            add(sb, seen, u, t);
            if (u.startsWith("http://")) {
                add(sb, seen, "https://" + u.substring(7), t);
            } else if (u.startsWith("https://")) {
                add(sb, seen, "http://" + u.substring(8), t);
            }
            String host = URI.create(u).getHost();
            if (host != null) hosts.add(host);
        }

        for (String h : hosts) {
            sb.append("    ").append(lt).append("rewriteSystem systemIdStartString=\"http://").append(h)
              .append("/\" rewritePrefix=\"wsdl/").append(h).append("/\"/>\n");
            sb.append("    ").append(lt).append("rewriteSystem systemIdStartString=\"https://").append(h)
              .append("/\" rewritePrefix=\"wsdl/").append(h).append("/\"/>\n");
            sb.append("    ").append(lt).append("rewriteURI uriStartString=\"http://").append(h)
              .append("/\" rewritePrefix=\"wsdl/").append(h).append("/\"/>\n");
            sb.append("    ").append(lt).append("rewriteURI uriStartString=\"https://").append(h)
              .append("/\" rewritePrefix=\"wsdl/").append(h).append("/\"/>\n");
        }

        sb.append(lt).append("/catalog>\n");
        Files.writeString(cat, sb.toString(), StandardCharsets.UTF_8);
        System.out.println("\nGenerated: " + cat + " (" + seen.size() + " entries)");
    }

    private static void add(StringBuilder sb, Set<String> seen, String sys, String uri) {
        if (seen.add(sys)) {
            String esc = sys.replace("&", "&amp;").replace("\"", "&quot;");
            sb.append("    \u003Csystem systemId=\"").append(esc).append("\" uri=\"").append(uri).append("\"/>\n");
            sb.append("    \u003Curi name=\"").append(esc).append("\" uri=\"").append(uri).append("\"/>\n");
        }
    }
}

