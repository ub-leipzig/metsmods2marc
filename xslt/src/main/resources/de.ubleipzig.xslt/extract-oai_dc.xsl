<xsl:stylesheet xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
    xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0">
    <xsl:output omit-xml-declaration="yes" indent="yes"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="/">
        <records>
            <xsl:for-each select="//oai_dc:dc">
                <oai_dc:dc>
                    <xsl:apply-templates select="node() | @*"/>
                </oai_dc:dc>
            </xsl:for-each>
        </records>
    </xsl:template>
    <xsl:template match="node() | @*">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
