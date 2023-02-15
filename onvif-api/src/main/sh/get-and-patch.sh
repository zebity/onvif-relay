#!/bin/bash

files="$1"
srcdir="$2"
destdir="$3"
patchdir="$4"
startdir=`pwd`

# echo $1 $2 $3 $4 $startdir
# input file:
# get[0] protocol[1] url/dir[2] file[3] [n|patchfile][4]

while read -r line
do
	# echo "Got: $line"
	sbits=($line)
        mkdir -p $srcdir${sbits[2]}
        mkdir -p $destdir${sbits[2]}
	( cd $srcdir${sbits[2]} && ${sbits[0]} ${sbits[1]}${sbits[2]}${sbits[3]} )
	if [ x"${sbits[4]}" = x"n" ]; then
		cp $srcdir${sbits[2]}${sbits[3]} $destdir${sbits[2]} 
		echo "downloading ${sbits[2]}${sbits[3]} to $srcdir & copying to $destdir"
	else
		patch -o - $srcdir${sbits[2]}${sbits[3]} $patchdir${sbits[4]} > $destdir${sbits[2]}${sbits[3]}
		echo "downloading ${sbits[2]}${sbits[3]} to $srcdir & patching to $destdir"
	fi 
done < $1 
	
