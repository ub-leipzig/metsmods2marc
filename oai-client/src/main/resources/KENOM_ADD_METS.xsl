<xsl:stylesheet xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mets="http://www.loc.gov/METS/"
                xmlns:mods="http://www.loc.gov/mods/v3"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                xmlns:saxon="http://saxon.sf.net/" extension-element-prefixes="saxon"
                exclude-result-prefixes="mods xlink">
    <xsl:output omit-xml-declaration="yes" indent="yes"/>
    <xsl:variable name="count" select="0" saxon:assignable="yes"/> 
    <xsl:strip-space elements="*"/>
    <xsl:template match="/">
            <xsl:for-each select="//mods:mods">
                <xsl:variable name="identifier" select="mods:recordInfo/mods:recordIdentifier"/>
                <xsl:variable name="creationDate" select="mods:recordInfo/mods:recordCreationDate"/> 
                <saxon:assign name="count" select="$count+1"/>
                <xsl:variable name="filename" select="concat('meta_',concat(format-number($count, '00000'),'.xml'))"/>
                <xsl:value-of select="$filename"/>
                <xsl:result-document href="{$identifier}/{$filename}">
                    <mets:mets xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/version17/mets.xsd">
                    <mets:metsHdr CREATEDATE="{$creationDate}">
                        <mets:agent OTHERTYPE="SOFTWARE" ROLE="CREATOR" TYPE="OTHER">
                            <mets:name>CHJ</mets:name>
                            <mets:note>XSLT</mets:note>
                        </mets:agent>
                    </mets:metsHdr>
                    <mets:dmdSec ID="DMDLOG_0000">
                        <mets:mdWrap MDTYPE="MODS">
                            <mets:xmlData>
                                <mods:mods>
                <xsl:apply-templates select="node()|@*"/>
                                </mods:mods>
                            </mets:xmlData>
                        </mets:mdWrap>
                    </mets:dmdSec>
                        <mets:dmdSec ID="DMDPHYS_0000">
                            <mets:mdWrap MDTYPE="MODS">
                                <mets:xmlData>
                                    <mods:mods xmlns:mods="http://www.loc.gov/mods/v3">
                                        <mods:location>
                                            <mods:physicalLocation authority="marcorg" displayLabel="Leipzig University Library, Leipzig, Germany">DE-15</mods:physicalLocation>
                                        </mods:location>
                                    </mods:mods>
                                </mets:xmlData>
                            </mets:mdWrap>
                        </mets:dmdSec>
                    <mets:fileSec>
                        <mets:fileGrp USE="ORIGINAL">
                            <mets:file ID="FILE_0001_ORIGINAL" MIMETYPE="image/tiff">
                                <mets:FLocat xmlns:xlink="http://www.w3.org/1999/xlink" LOCTYPE="URL" xlink:href="{$identifier}_media/{$identifier}_vs.tif"/>
                            </mets:file>
                            <mets:file ID="FILE_0002_ORIGINAL" MIMETYPE="image/tiff">
                                <mets:FLocat xmlns:xlink="http://www.w3.org/1999/xlink" LOCTYPE="URL" xlink:href="{$identifier}_media/{$identifier}_rs.tif"/>
                            </mets:file>
                        </mets:fileGrp>
                    </mets:fileSec>
                    <mets:structMap TYPE="PHYSICAL">
                        <mets:div DMDID="DMDPHYS_0000" ID="PHYS_0000" TYPE="physSequence">
                            <mets:div ID="PHYS_0001" ORDER="1" ORDERLABEL=" - " TYPE="page">
                                <mets:fptr FILEID="FILE_0001_ORIGINAL"/>
                            </mets:div>
                            <mets:div ID="PHYS_0002" ORDER="2" ORDERLABEL=" - " TYPE="page">
                                <mets:fptr FILEID="FILE_0002_ORIGINAL"/>
                            </mets:div>
                        </mets:div>
                    </mets:structMap>
                    <mets:structLink>
                        <mets:smLink xmlns:xlink="http://www.w3.org/1999/xlink" xlink:to="PHYS_0001" xlink:from="LOG_0000"/>
                        <mets:smLink xmlns:xlink="http://www.w3.org/1999/xlink" xlink:to="PHYS_0002" xlink:from="LOG_0000"/>
                    </mets:structLink>
                </mets:mets>    
                </xsl:result-document>
            </xsl:for-each>        
    </xsl:template>
    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>