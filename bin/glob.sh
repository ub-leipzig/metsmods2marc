#!/bin/bash
for file in /tmp/output/*.xml; do
../xslt/build/install/xslt/bin/xslt -r "$file" -d /tmp/rdf-out -p http://localhost:8080/repository -b; done
