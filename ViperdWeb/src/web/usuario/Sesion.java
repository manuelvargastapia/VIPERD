package web.usuario;
/* MAESTRO VIPERD (App.Web)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import web.usuario.Interfaces.ISesion;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

//CLASE Sesion
public final class Sesion implements ISesion {

    private Map<String, String> entorno;
    private HttpSession sesion;

    public Sesion(HttpSession s) {
        sesion = s;
        entorno = new LinkedHashMap<>();
        setFechaHora(new Date());
        if (sesion.getAttribute("idioma") == null) {
            setEntorno("idioma", Locale.getDefault().getLanguage().toLowerCase());
        }
        Enumeration keys = sesion.getAttributeNames();
        while (keys.hasMoreElements()) {
            String clave = (String)keys.nextElement();
            entorno.put(clave, sesion.getAttribute(clave).toString());
        }
    }

    //IMPLEMENTACION DE INTERFACE "ISesion"
    @Override public ISesion Abrir() {return this;}
    @Override public void Cerrar() {
        sesion.invalidate();
        entorno.clear();
    }
    @Override public String getEntorno(String nombre) {
        String valor = "";
        if (null != entorno.get(nombre)) {valor = entorno.get(nombre);}
        return valor;
    }
    @Override public void setEntorno(String nombre, String valor) {
        entorno.put(nombre, valor);
        sesion.setAttribute(nombre, valor);
    }
    @Override public Map<String, String> Entorno() {
        return entorno;
    }
    @Override public void setFechaHora(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        try {
            String mes = "0" + Integer.toString(cal.get(Calendar.MONTH) + 1);
            String dia = "0" + Integer.toString(cal.get(Calendar.DATE));
            setEntorno("dia", dia.substring(dia.length()-2, dia.length()));
            setEntorno("mes", mes.substring(mes.length()-2, mes.length()));
            setEntorno("año", Integer.toString(cal.get(Calendar.YEAR)));
            setEntorno("fecha", (entorno.get("año") + "-" + entorno.get("mes") + "-" + entorno.get("dia")));
        } catch (Exception e) {}
    }
}