<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template name="solicitudes_globales">
        <xsl:choose>
            <xsl:when test="//var/solicitud='Autenticarse'">
                <xsl:call-template name="Autenticarse"/>
            </xsl:when>
            <xsl:when test="//var/solicitud='NoAutorizado'">
                <xsl:call-template name="NoAutorizado"/>
            </xsl:when>
            <xsl:when test="//var/solicitud='CerrarSesion'">
                <xsl:call-template name="CerrarSesion"/>
            </xsl:when>
            <xsl:when test="//var/solicitud='IniciarSesion'">
                <xsl:call-template name="Autenticarse"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="NoEncontrado"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template name="base_head">
        <link rel="icon" href="{//var/aplicacion}/base/img/favicon.ico" type="image/x-icon" />
        <link rel="shortcut icon" href="{//var/aplicacion}/base/img/favicon.ico" type="image/x-icon" />
        <link href="{//var/aplicacion}/base/estilos.css" rel="stylesheet" type="text/css"/>
        <script src="{//var/aplicacion}/base/jquery321.js" type="text/javascript"></script>
    </xsl:template>
    <xsl:template name="NoEncontrado">
        <html>
            <head>
                <title>No encontrado</title>
                <xsl:call-template name="base_head"/>
            </head>
            <body>
                <div><img src="{//var/aplicacion}/base/img/logo.png" /></div>
                <h2>El recurso "<xsl:value-of select="//var/solicitud"/>" no existe.</h2>
            </body>
        </html>
    </xsl:template>
    <xsl:template name="NoAutorizado">
        <html>
            <head>
                <title>No autorizado</title>
                <xsl:call-template name="base_head"/>
            </head>
            <body>
                <div><img src="{//var/aplicacion}/base/img/logo.png" /></div>
                <h2>Acceso no autorizado</h2>
            </body>
        </html>
    </xsl:template>
    <xsl:template name="Autenticarse">
        <html>
            <head>
                <title>Autenticarse</title>
                <xsl:call-template name="base_head"/>
                <script type="text/javascript">
                    jQuery(document).ready(function() {
                        jQuery('#login').focus();
                    });
                </script>
            </head>
            <body>
                <div><img src="{//var/aplicacion}/base/img/logo.png" /></div>
                <h2>Autenticarse</h2>
                <xsl:if test="count(//mensaje[@opcion])!=0">
                    <h3><div id="mensaje" class="{//mensaje/@opcion}"><xsl:value-of select="//mensaje[@opcion]" disable-output-escaping="yes" /></div></h3>
                </xsl:if>
                <form action="IniciarSesion" method="post">
                    <input type="hidden" name="direccion" value="{//var/direccion}" />
                    <table cellpadding="5">
                        <tr>
                            <td><label for="login">Usuario</label></td>
                            <td><input type="text" name="login" id="login" size="12"/></td>
                        </tr>
                        <tr>
                            <td><label for="password">Contraseña</label></td>
                            <td><input type="password" name="password" size="12"/></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="submit" name="enviar" value="INICIAR SESION" />
                            </td>
                        </tr>
                    </table>
                </form>
            </body>
        </html>
    </xsl:template>
    <xsl:template name="CerrarSesion">
        <html>
            <head>
                <title>Sesión cerrada</title>
                <xsl:call-template name="base_head"/>
            </head>
            <body>
                <div><img src="{//var/aplicacion}/base/img/logo.png" /></div>
                <h2>Sesión cerrada correctamente</h2>
                <p><a href="Autenticarse">Volver a autenticarse</a></p>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>