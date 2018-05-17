package web.usuario;
/* MAESTRO VIPERD (App.Web)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import web.usuario.Interfaces.IRuteador;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

//SUPERCLASE ABSTRACTA Ruteador
public abstract class Ruteador extends HttpServlet implements IRuteador {
    protected final static Logger TRAZADOR = Logger.getLogger(Ruteador.class.getName());
    protected String SOLICITUD_PREDETERMINADA;
    protected String SOLICITUD_AUTENTICARSE;
    protected String SOLICITUD_INICIARSESION;
    protected String SOLICITUD_CERRARSESION;

    //IMPLEMENTACION GENERICA DEL SERVLET
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        procesarSolicitud(request, response, "GET");
    }
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        procesarSolicitud(request, response, "POST");
    }
    @Override protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        procesarSolicitud(request, response, "PUT");
    }
    @Override protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        procesarSolicitud(request, response, "DELETE");
    }

    //IMPLEMENTACION DE INTERFACE "IRuteador"
    @Override public synchronized void procesarSolicitud(HttpServletRequest peticion, HttpServletResponse respuesta, String metodo) throws ServletException, IOException {
        Map<String, String> var = new LinkedHashMap<>();
        var.put("aplicacion", peticion.getContextPath());
        var.put("solicitud", peticion.getPathInfo().replace("/", ""));
        if (var.get("solicitud").isEmpty()) {var.put("solicitud", this.SOLICITUD_PREDETERMINADA);}
        String direccion = "";
        if (!var.get("solicitud").equalsIgnoreCase(this.SOLICITUD_AUTENTICARSE) && !var.get("solicitud").equalsIgnoreCase(this.SOLICITUD_INICIARSESION)) {
            direccion = peticion.getRequestURI().toString();
            if (null != peticion.getQueryString()) {direccion = direccion + "?" + peticion.getQueryString();}
        }
        var.put("direccion", direccion);
        TRAZADOR.info(var.get("solicitud") + " (" + metodo + ")");
        String usuario = "";
        HttpSession sesion = peticion.getSession(false);
        if (null != sesion) {
            if (null != sesion.getAttribute("usuario")) {
                usuario = sesion.getAttribute("usuario").toString();
            }
        }
        if (usuario.isEmpty() && !var.get("solicitud").equalsIgnoreCase(this.SOLICITUD_INICIARSESION) && !var.get("solicitud").equalsIgnoreCase(this.SOLICITUD_CERRARSESION)) {
            var.put("solicitud", this.SOLICITUD_AUTENTICARSE);
        }
        Despachar(peticion, respuesta, var);
    }
}