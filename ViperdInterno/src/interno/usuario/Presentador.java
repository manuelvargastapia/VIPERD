package interno.usuario;
/* MAESTRO VIPERD (multi-plataforma)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import interno.nucleo.Interfaces.IAccion;
import interno.nucleo.Interfaces.IPresentador;
import interno.nucleo.Interfaces.IVista;

import java.util.Map;
import java.util.logging.Logger;

//SUPERCLASE ABSTRACTA Presentador
public abstract class Presentador implements IPresentador {
    protected final static Logger TRAZADOR = Logger.getLogger(Presentador.class.getName());

    protected IVista vista;
    protected Map<String, String> entorno;
    
    public Presentador(IVista vista) {
        this.vista = vista;
        this.entorno = this.vista.Entorno();
        TRAZADOR.info(this.getClass().getSimpleName());
    }
    public Presentador(IVista vista, Map<String, String> entorno) {
        this.vista = vista;
        this.entorno = entorno;
        TRAZADOR.info(this.getClass().getSimpleName());
    }
    
    //IMPLEMENTACION DE INTERFACE "IPresentador"
    @Override public Map<String, String> Entorno() {
        return this.entorno;
    }
    @Override public Boolean autorizarAcceso(Enum accion, String roles) {
        String requisitos = ((IAccion)accion).Requisitos();
        if (null == roles) {roles = "";}
        Boolean resultado = this.evaluarPermisos(requisitos, roles);
        TRAZADOR.info("accion: " + accion.name() + " - roles: " + roles + " - resultado: " + resultado.toString());
        return resultado;
    }
    
    //FUNCIONES INTERNAS
    protected Boolean evaluarPermisos(String requisitos, String roles) {
        Boolean resultado = false;
        if (requisitos.isEmpty()) {resultado = true;}
        else if (requisitos.equalsIgnoreCase("*") && !roles.isEmpty()) {resultado = true;}
        else if (!roles.isEmpty()) {
            String evaluar = "," + requisitos + ",";
            for (String elemento : ((String[])roles.split(","))) {
                if (evaluar.contains("," + elemento + ",") && !elemento.isEmpty()) {
                    resultado = true;
                    break;
                }
            }
        }
        return resultado;
    }
    protected Boolean comprobarSolicitud(Enum accion, Enum casodeuso, Boolean estricta) {
        Boolean comprobar = false;
        if (estricta) {
            if (accion != null && casodeuso != null) {comprobar = true;}
        } else {
            if (accion != null) {comprobar = true;}
        }
        return comprobar;
    }
}