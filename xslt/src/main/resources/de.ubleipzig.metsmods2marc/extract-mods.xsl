<xsl:stylesheet xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mets="http://www.loc.gov/METS/" xmlns:dv="http://dfg-viewer.de/"
                xmlns:mods="http://www.loc.gov/mods/v3"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
                exclude-result-prefixes="mods xlink">
    <xsl:output omit-xml-declaration="yes" indent="yes"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="/">
        <out>
            <xsl:apply-templates/>
        </out>
    </xsl:template>
    <xsl:template match="mods:mods">
        <xsl:copy>
            <xsl:attribute name="name">
                <xsl:value-of select="../../../@ID"></xsl:value-of>
            </xsl:attribute>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="mets:metsHdr"/>
    <xsl:template match="mets:*">
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="dv:*/node()">
        <xsl:apply-templates/>
    </xsl:template>
</xsl:stylesheet>
