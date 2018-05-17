package web.usuario;
/* MAESTRO VIPERD (App.Web)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import interno.nucleo.Interfaces.IVista;
import web.usuario.Interfaces.ISesion;

import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

//SUPERCLASE ABSTRACTA Vista
public abstract class Vista implements IVista {
    protected final Logger TRAZADOR = Logger.getLogger(Vista.class.getName());

    private String salida;
    private String plantilla;
    private Document xml;
    private String json;
    private HttpServletRequest peticion;
    private HttpServletResponse respuesta;
    private Map<String, String> variables;
    private Map<String, String> valores;
    private URIResolver localizador;
    private ISesion sesion;

    public Vista(HttpServletRequest peticion, HttpServletResponse respuesta, Map<String, String> variables, URIResolver localizador) {
        TRAZADOR.info(this.getClass().getSimpleName());
        this.valores = new LinkedHashMap<>();
        this.peticion = peticion;
        this.respuesta = respuesta;
        this.variables = variables;
        this.localizador = localizador;
        this.sesion = new Sesion(peticion.getSession(true));
        this.json = "[]";
        leerParametro("uid");
        leerParametro("info");
        leerParametro("fecha");
        leerParametro("pag");
        leerParametro("antes");
        if (!leerParametro("idioma").isEmpty()) {setEntorno("idioma", variables.get("idioma"));}
        if (!leerParametro("periodo").isEmpty()) {setEntorno("periodo", variables.get("periodo"));}
        for (Map.Entry<String, String[]> entry : peticion.getParameterMap().entrySet()) {
            this.valores.put(entry.getKey(), entry.getValue()[0]);
        }
    }
    public void Configurar(String salida, String modelo, String plantilla) {
        TRAZADOR.info("salida: " + salida + " - modelo: " + modelo + " - plantilla: " + plantilla);
        this.salida = salida;
        this.plantilla = plantilla;
        try {
            this.xml = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()            
                    .parse(getClass().getResourceAsStream(modelo));
        } catch (Exception e) {}
    }
    public void Salida(String salida) {
        TRAZADOR.info(salida);
        this.salida = salida;
    }

    //IMPLEMENTACION DE INTERFACE "IVista"
    @Override public void setEntorno(String nombre, String valor) {
        TRAZADOR.info(nombre + " = " + valor);
        this.sesion.setEntorno(nombre, valor);
    }
    @Override public void Redirigir(String destino) {
        TRAZADOR.info(destino);
        try {
            this.respuesta.sendRedirect(destino);
            this.respuesta.flushBuffer();
        } catch (Exception e) {}
    }
    @Override public void mostrarVista(Boolean opcion) {
        TRAZADOR.info(opcion.toString());
        StringBuilder str = new StringBuilder();
        str.append("<doc><valores>");
        for (Map.Entry<String, String> valor: this.valores.entrySet()) {
            str.append("<").append(valor.getKey()).append("><![CDATA[").append(valor.getValue()).append("]]></").append(valor.getKey()).append(">");
        }
        str.append("</valores><var>");
        for (Map.Entry<String, String> variable: this.variables.entrySet()) {
            str.append("<").append(variable.getKey()).append("><![CDATA[").append(variable.getValue()).append("]]></").append(variable.getKey()).append(">");
        }
        str.append("</var><entorno>");
        for (Map.Entry<String, String> valor: this.sesion.Entorno().entrySet()) {
            str.append("<").append(valor.getKey()).append("><![CDATA[").append(valor.getValue()).append("]]></").append(valor.getKey()).append(">");
        }
        str.append("</entorno></doc>");
        Node importar = this.xml.importNode(crearXML(str.toString()), true);
        this.xml.getDocumentElement().appendChild(importar);
        TransformerFactory gestor = TransformerFactory.newInstance();
        gestor.setURIResolver(this.localizador);
        if (opcion) {
            this.respuesta.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");
            this.respuesta.setHeader("Pragma", "no-cache");
            this.respuesta.setDateHeader("Expires", 0);
        }
        switch (this.salida) {
            case "html":
                try {
                    this.respuesta.setContentType("text/html;charset=UTF-8");
                    StreamSource xslt = new StreamSource(getClass().getResourceAsStream(this.plantilla));
                    Transformer transformador = gestor.newTemplates(xslt).newTransformer();
                    transformador.transform(new DOMSource(this.xml), new StreamResult(this.respuesta.getWriter()));
                } catch (Exception e) {}
                break;
            case "xml":
                try {
                    this.respuesta.setContentType("text/xml;charset=UTF-8");
                    gestor.newTransformer().transform(new DOMSource(this.xml), new StreamResult(this.respuesta.getWriter()));
                } catch (Exception e) {}
                break;
            case "json":
                try {
                    this.respuesta.setContentType("application/json;charset=UTF-8");
                    this.respuesta.getWriter().write(this.json);
                } catch (Exception e) {}
                break;
        }
    }
    @Override public void actualizarContenedor(Map<String, String> elementos) {
        TRAZADOR.info("");
        if (elementos != null) {
            StringBuilder str = new StringBuilder();
            switch (this.salida) {
                case "json":
                    str.append("{\"").append("contenedor").append("\":{");
                    for (Map.Entry<String, String> atributo: elementos.entrySet()) {
                        str.append("\"").append(atributo.getKey()).append("\":\"").append(atributo.getValue()).append("\", ");
                        TRAZADOR.info(atributo.getKey() + " = " + atributo.getValue());
                    }
                    String cont = str.toString();
                    cont = cont.substring(0, cont.length()-2) + "}}\n";
                    this.json = cont;
                    break;
                default:
                    str.append("<contenedor>");
                    for (Map.Entry<String, String> atributo: elementos.entrySet()) {
                        str.append("<").append(atributo.getKey()).append("><![CDATA[").append(atributo.getValue()).append("]]></").append(atributo.getKey()).append(">");
                        TRAZADOR.info(atributo.getKey() + " = " + atributo.getValue());
                    }
                    str.append("</contenedor>");
                    Node importar = this.xml.importNode(crearXML(str.toString()), true);
                    this.xml.getDocumentElement().appendChild(importar);
                    break;
            }
        }
    }
    @Override public void actualizarConjunto(List<Map<String, String>> conjunto) {
        TRAZADOR.info("");
        if (conjunto != null) {
            StringBuilder str = new StringBuilder();
            switch (this.salida) {
                case "json":
                    str.append("{\"").append("conjunto").append("\":[");
                    for (Map<String, String> caso: conjunto) {
                        str.append("{\"").append("caso").append("\":{");
                        for (Map.Entry<String, String> atributo: caso.entrySet()) {
                            str.append("\"").append(atributo.getKey()).append("\":\"").append(atributo.getValue()).append("\", ");
                        }
                        str.append("}},\n");
                    }
                    str.append("]}");
                    this.json = str.toString().replace(", }", "}").replace("},\n]}", "}]}");
                    break;
                default:
                    str.append("<conjunto>");
                    for (Map<String, String> caso: conjunto) {
                        str.append("<caso>");
                        for (Map.Entry<String, String> campo: caso.entrySet()) {
                            str.append("<").append(campo.getKey()).append("><![CDATA[").append(campo.getValue()).append("]]></").append(campo.getKey()).append(">");
                        }
                        str.append("</caso>");
                    }
                    str.append("</conjunto>");
                    Node importar = this.xml.importNode(crearXML(str.toString()), true);
                    this.xml.getDocumentElement().appendChild(importar);
                    break;
            }
        }
    }
    @Override public void setValor(String nombre, String valor) {
        TRAZADOR.info(nombre + " = " + valor);
        try {
            Node node = (Node) XPathFactory.newInstance().newXPath().evaluate("//contenedor/*[name()='" + nombre + "']", this.xml, XPathConstants.NODE);
            node.setTextContent(valor);
        } catch (Exception e) {}
    }
    @Override public void setEnfocado(String nombre) {
        TRAZADOR.info(nombre);
        setAtributo("//contenedor", nombre, "enfocado", "1");
    }
    @Override public void setActivo(String nombre, Boolean opcion) {
        TRAZADOR.info(nombre + " = " + opcion.toString());
        String valor = "0";
        if (opcion) {valor = "1";}
        setAtributo("//contenedor", nombre, "activo", valor);
    }
    @Override public void setVisible(String nombre, Boolean opcion) {
        TRAZADOR.info(nombre + " = " + opcion.toString());
        String valor = "0";
        if (opcion) {valor = "1";}
        setAtributo("//contenedor", nombre, "visible", valor);
    }
    @Override public void mostrarMensajes(Map<String, String> mensajes) {
        TRAZADOR.info(String.valueOf(mensajes.size()));
        int i = 0;
        String texto;
        String codigo = "";
        String opcion = "";
        StringBuilder str = new StringBuilder("");
        for (Map.Entry<String, String> mensaje: mensajes.entrySet()) {
            codigo = mensaje.getKey();
            texto = mensaje.getValue();
            if (texto.isEmpty()) {texto = codigo;}
            if (i > 0) {str.append("<br/>");}
            str.append(texto);
            i++;
        }
        if (codigo.startsWith("ERROR")) {opcion = "ERROR";}
        if (codigo.startsWith("EXITO")) {opcion = "EXITO";}
        if (codigo.startsWith("ALERTA")) {opcion = "ALERTA";}
        if (codigo.startsWith("INFO")) {opcion = "INFO";}
        if (str.toString().length()>0) {
            mostrarMensaje(str.toString(), opcion);
        }
    }
    @Override public void mostrarMensaje(String texto, String opcion) {
        TRAZADOR.info("opcion: " + opcion + ", mensaje: " + texto);
        StringBuilder str = new StringBuilder();
        str.append("<mensaje opcion=\"").append(opcion).append("\"><![CDATA[").append(texto).append("]]></mensaje>");
        Node importar = this.xml.importNode(crearXML(str.toString()), true);
        this.xml.getDocumentElement().appendChild(importar);
    }
    @Override public void Cerrar() {
        TRAZADOR.info("");
        sesion.Cerrar();
    }
    @Override public String getValor(String nombre) {
        String valor = "";
        if (null != this.valores.get(nombre)) {valor = this.valores.get(nombre);}
        TRAZADOR.info(nombre + " = " + valor);
        return valor;
    }
    @Override public Map<String, String> getValores() {
        return this.valores;
    }
    @Override public Map<String, String> Entorno() {
        return this.sesion.Entorno();
    }

    //FUNCIONES INTERNAS
    private String leerParametro(String nombre) {
        String valor = this.peticion.getParameter(nombre);
        if (valor == null) {valor = "";}
        else if (valor.equalsIgnoreCase("null")) {valor = "";}
        this.variables.put(nombre, valor);
        return valor;
    }
    private Node crearXML(String texto) {
        Node nodo = null;
        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()            
                    .parse(new InputSource(new StringReader(texto)));
            nodo = doc.getDocumentElement();
        } catch (Exception e) {}
        return nodo;
    }
    private void setAtributo(String ruta, String nombre, String atributo, String valor) {
        try {
            Element node = (Element) XPathFactory.newInstance().newXPath().evaluate(ruta + "/*[name()='" + nombre + "']", this.xml, XPathConstants.NODE);
            node.setAttribute(atributo, valor);
        } catch (Exception e) {}
    }
}