## XSLT_API

This repository provides two methods for XML transformation:

* An HTTP API powered by Apache Camel.
* A CLI tool 

### HTTP API Usage
The API depends on HTTP accessible resources.  For example, XML binaries stored in a [Trellis Repository](https://github.com/trellis-ldp/trellis-deployment).

The API can be accessed like this:

    $ curl "http://localhost:9095/xml?format=marc21&context=http://localhost:8080/repository/node/collection/DE15/buchhandschriften/res/MS_1033.xml" 

* `context` is an HTTP accessible resource.  Currently, only METS/MODS XML formatted documents are supported.
* `format` can be one of three values (mods, marc21, bibframe).  
    
### CLI Usage

    $ gradle installDist
    
    $ cd ./xslt/build/install/xslt/bin
    
    $ ./xslt -r {$filesytem path to XML source file} -d {$output dir for transformed file} 
    
example:
     
    $ ./xslt -r ~/IdeaProjects/metsmods2marc/xslt/src/test/resources/BntItin_021340072.xml -d /tmp/output-data 

### MARC21 Output

See [example](https://github.com/ub-leipzig/xslt-api/blob/62fa0fc40b1a6b9d562a3d101bdd66c1fa3115cd/xslt/src/test/resources/marc21xml/marc-output_17:48:51.468.xml)