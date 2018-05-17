package base;
/* MAESTRO VIPERD (App.Web)
 * Licensed under the Apache License, Version 2.0 */

import web.usuario.Ruteador;
import web.usuario.Vista;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

@WebServlet(name = "Inicio", urlPatterns = {"/inicio/*"})
public class Inicio extends Ruteador {
    protected final static Logger TRAZADOR = Logger.getLogger(Inicio.class.getName());

    @Override public void init() {
        SOLICITUD_PREDETERMINADA = "ListaUsuarios";
        SOLICITUD_AUTENTICARSE = "Autenticarse";
        SOLICITUD_INICIARSESION = "IniciarSesion";
        SOLICITUD_CERRARSESION = "CerrarSesion";
    }

    //IMPLEMENTACION DE INTERFACE "IRuteador"
    @Override public synchronized void Despachar(HttpServletRequest peticion, HttpServletResponse respuesta, Map<String, String> variables) {
        TRAZADOR.info(variables.get("solicitud"));
        VistaWeb vista = new VistaWeb(peticion, respuesta, variables, new Localizador());
        vista.Configurar("html", "ModeloUsuarios.xml", "PlantillaUsuarios.xsl");
        switch (variables.get("solicitud")) {
            case "Autenticarse":
                new PresentadorUsuarios(vista).pedirAccion(PresentadorUsuarios.ACCION.Autenticarse, "");
                break;
            case "IniciarSesion":
                new PresentadorUsuarios(vista).pedirAccion(PresentadorUsuarios.ACCION.IniciarSesion, "");
                break;
            case "CerrarSesion":
                new PresentadorUsuarios(vista).pedirAccion(PresentadorUsuarios.ACCION.CerrarSesion, "");
                break;
            case "ListaUsuarios":
                new PresentadorUsuarios(vista).pedirAccion(PresentadorUsuarios.ACCION.ListaUsuarios, "");
                break;
            case "VerUsuario":
                new PresentadorUsuarios(vista).pedirAccion(PresentadorUsuarios.ACCION.VerUsuario, variables.get("uid"));
                break;
            default:
                new PresentadorUsuarios(vista).pedirAccion(PresentadorUsuarios.ACCION.Nada, "");
                break;
        }
    }

    //CLASES AUXILIARES
    private class VistaWeb extends Vista {
        public VistaWeb(HttpServletRequest peticion, HttpServletResponse respuesta, Map<String, String> variables, URIResolver localizador) {
            super(peticion, respuesta, variables, localizador);
        }
    }
    private class Localizador implements URIResolver {
        @Override public Source resolve(String href, String base) throws TransformerException {
            if (href.contains("/")) {
                return new StreamSource(Inicio.super.getServletContext().getResourceAsStream(href));
            } else {
                return new StreamSource(this.getClass().getResourceAsStream(href));
            }
        }
    }
}