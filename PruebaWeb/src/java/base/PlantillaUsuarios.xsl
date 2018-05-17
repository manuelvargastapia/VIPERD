<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:decimal-format grouping-separator="." decimal-separator="," NaN="---" zero-digit="0"/>
    <xsl:output method="html" encoding="utf-8" indent="yes"
        doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
        doctype-system="http://www.w3.org/TR/html4/loose.dtd" />
    <xsl:import href="/WEB-INF/PlantillaBase.xsl"/>
    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="//var/solicitud='ListaUsuarios'">
                <xsl:call-template name="ListaUsuarios"/>
            </xsl:when>
            <xsl:when test="//var/solicitud='VerUsuario'">
                <xsl:call-template name="VerUsuario"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="solicitudes_globales"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="ListaUsuarios">
        <html>
            <head>
                <title><xsl:value-of select="//var/solicitud"/></title>
                <xsl:call-template name="base_head"/>
            </head>
            <body>
                <div><img src="{//var/aplicacion}/base/img/logo.png" /></div>
                <h2>Esta es la PLANTILLA: PlantillaUsuarios.xsl</h2>
                <p><b><xsl:value-of select="//modelo/descripcion"/></b></p>
                <p>Abajo se muestran casos de la base de datos con la solicitud "<xsl:value-of select="//var/solicitud"/>":</p>
                <xsl:for-each select="//conjunto/caso">
                    <div><xsl:value-of select="uid"/> - <a href="VerUsuario?uid={uid}"><xsl:value-of select="alias"/></a> - <xsl:value-of select="email"/></div>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
    <xsl:template name="VerUsuario">
        <html>
            <head>
                <title><xsl:value-of select="//var/solicitud"/></title>
                <xsl:call-template name="base_head"/>
            </head>
            <body>
                <div><img src="{//var/aplicacion}/base/img/logo.png" /></div>
                <h2>Esta es la PLANTILLA: PlantillaUsuarios.xsl</h2>
                <p><b><xsl:value-of select="//modelo/descripcion"/></b></p>
                <p>Abajo se muestran los datos del usuario elegido con la solicitud "<xsl:value-of select="//var/solicitud"/>":</p>
                <div>
                UID: <xsl:value-of select="//contenedor/uid"/><br/>
                Nombre: <xsl:value-of select="//contenedor/alias"/><br/>
                E-mail: <xsl:value-of select="//contenedor/email"/><br/>
                </div>
                <p><a href="CerrarSesion">Cerrar sesión</a></p>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>