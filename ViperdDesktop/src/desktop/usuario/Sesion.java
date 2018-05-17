package desktop.usuario;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import desktop.usuario.Interfaces.ISesion;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

//CLASE SINGLETON Sesion
public final class Sesion implements ISesion {
    protected final static Logger TRAZADOR = Logger.getLogger(Sesion.class.getName());
    
    private static Sesion instancia;
    private static Map<String, String> entorno;

    //CONSTRUCTOR PRIVADO + ACTIVADOR PUBLICO (PATRON SINGLETON)
    private Sesion() {
        entorno = new LinkedHashMap<>();
    }
    
    //IMPLEMENTACION DE INTERFACE "ISesion"
    public static ISesion Abrir() {
        if (instancia == null) {instancia = new Sesion();}
        setFechaHora(new Date());
        entorno.put("idioma", Locale.getDefault().getLanguage().toLowerCase());
        return instancia;
    }
    public static void Cerrar() {
        entorno.clear();
    }
    public static String getEntorno(String nombre) {
        String valor = "";
        if (null != entorno.get(nombre)) {valor = entorno.get(nombre);}
        return valor;
    }
    public static void setEntorno(String nombre, String valor) {
        entorno.put(nombre, valor);
    }
    public static Map<String, String> Entorno() {
        return entorno;
    }
    public static void setFechaHora(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        try {
            String mes = "0" + Integer.toString(cal.get(Calendar.MONTH) + 1);
            String dia = "0" + Integer.toString(cal.get(Calendar.DATE));
            entorno.put("dia", dia.substring(dia.length()-2, dia.length()));
            entorno.put("mes", mes.substring(mes.length()-2, mes.length()));
            entorno.put("año", Integer.toString(cal.get(Calendar.YEAR)));
            entorno.put("fecha", (entorno.get("año") + "-" + entorno.get("mes") + "-" + entorno.get("dia")));
        } catch (Exception e) {TRAZADOR.info(e.getMessage());}
    }
}