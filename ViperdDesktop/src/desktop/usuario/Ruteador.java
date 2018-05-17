package desktop.usuario;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import desktop.usuario.Interfaces.IRuteador;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

//SUPERCLASE ABSTRACTA Ruteador
public abstract class Ruteador implements IRuteador {
    protected final static Logger TRAZADOR = Logger.getLogger(Ruteador.class.getName());
    protected static String SOLICITUD_PREDETERMINADA;
    protected static String SOLICITUD_AUTENTICARSE;
    protected static String SOLICITUD_INICIARSESION;
    protected static String SOLICITUD_CERRARSESION;

    public Ruteador() {}
    
    //IMPLEMENTACION DE INTERFACE "IRuteador"
    public synchronized static Map<String, String> procesarSolicitud(String[] args) {
        Map<String, String> var = new LinkedHashMap<>();
        String solicitud = "";
        String direccion = "";
        if (null != args) {
            if (args.length >0) {solicitud = args[0];}
            if (args.length >1 && null != args[1]) {direccion = args[1];}
        }
        if (solicitud.isEmpty()) {solicitud = SOLICITUD_PREDETERMINADA;}
        if (direccion.isEmpty()) {direccion = SOLICITUD_PREDETERMINADA;}
        var.put("solicitud", solicitud);
        var.put("direccion", direccion);
        TRAZADOR.info(var.get("solicitud"));
        Sesion.Abrir();
        if (Sesion.getEntorno("usuario").isEmpty()) {
            var.put("direccion", var.get("solicitud"));
            var.put("solicitud", SOLICITUD_AUTENTICARSE);
        }
        return var;
    }
}