<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:jaxws="https://jakarta.ee/xml/ns/jaxws">
<!-- JAX xsl:transform version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:jaxws="http://java.sun.com/xml/ns/jaxws" -->
<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
<xsl:template match="/">
<xsl:param name="file" select="$p1" />
<!-- xsl:variable name="wsdlLoc" select="document-uri()" / -->
<!-- xsl:variable name="wsdlLoc" select="concat(&quot;, $file, &quot;)" / -->
<xsl:variable name="wsdlLoc" select="&quot;/META-INF/wsdl/www.onvif.org/ver10/device/wsdl/devicemgmt.wsdl&quot;" />
<jaxws:bindings xmlns:jaxws="https://jakarta.ee/xml/ns/jaxws" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:jaxb="https://jakarta.ee/xml/ns/jaxb" >
<!-- JAX jaxws:bindings xmlns:jaxws="http://java.sun.com/xml/ns/jaxws" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" -->
 <xsl:attribute name="wsdlLocation">
  <xsl:value-of select="$p1" />
 </xsl:attribute> 
<jaxb:globalBindings enableJavaNamingConventions = "false" underscoreBinding = "asCharInWord"/>
  <xsl:for-each select="wsdl:definitions/wsdl:portType[@*]">
   <xsl:variable name="portType" select="@name" />
   <xsl:for-each select="wsdl:operation[@*]">
    <xsl:variable name="operation" select="@name" />
    <xsl:element name="jaxws:bindings">
     <xsl:attribute name="node">
      <xsl:variable name="selector" select="concat(&quot;wsdl:definitions/wsdl:portType[@name='&quot;, $portType, &quot;']/wsdl:operation[@name='&quot;, $operation, &quot;']&quot;)" />
      <xsl:value-of select="$selector" />
      <!-- xsl:value-of select="concat(&quot;wsdl:definitions/wsdl:portType[@name='Device']/wsdl:operation[@name='&quot;,$operation, &quot;']&quot;)" / -->
     </xsl:attribute>
     <xsl:element name="jaxws:method">
       <xsl:attribute name="name">
        <xsl:value-of select="$operation" />
       </xsl:attribute>
     </xsl:element>
    </xsl:element>
   </xsl:for-each>
  </xsl:for-each>
</jaxws:bindings>
</xsl:template>
</xsl:transform>
