package web.usuario;
/* MAESTRO VIPERD (App.Web)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Interfaces {
    interface IRuteador {
        void procesarSolicitud(HttpServletRequest peticion, HttpServletResponse respuesta, String metodo) throws ServletException, IOException;
        void Despachar(HttpServletRequest peticion, HttpServletResponse respuesta, Map<String, String> variables);
    }
    interface ISesion {
        ISesion Abrir();
        void setEntorno(String nombre, String valor);
        String getEntorno(String nombre);
        Map<String, String> Entorno();
        void setFechaHora(Date fecha);
        void Cerrar();
    }
}