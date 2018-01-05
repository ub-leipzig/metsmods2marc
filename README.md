## MetsMods2Marc

A CLI tool that creates Marc21 XML from a MetsMods XML file.

### Transformation Process
The transformation works in two steps. 
- The first XSLT (`extract-mods.xsl`) separates the mets elements from the mods elements. 
- The second XSLT (`MODS3-4_MARC21slim_XSLT1-0.xsl`) processes known mods elements into Marc21 encoded XML.

### Usage

    $ gradle installDist
    
    $ cd ./xslt/build/install/xslt/bin
    
    $ ./xslt -r {$filesytem path to XML source file} -x {$filesytem path to transformation xslt} -d {$output dir for transformed file} 
    
example:
     
    $ ./xslt -r ~/IdeaProjects/metsmods2marc/xslt/src/test/resources/BntItin_021340072.xml -x ~/IdeaProjects/metsmods2marc/xslt/src/main/resources/de.ubleipzig.metsmods2marc/extract-mods.xsl -d /tmp/output-data 

### Output

See [example](https://github.com/ub-leipzig/metsmods2marc/blob/master/xslt/src/test/resources/marc-out.xml)