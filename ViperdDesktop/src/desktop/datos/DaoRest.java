package desktop.datos;
/* MAESTRO VIPERD (multi-plataforma)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import interno.nucleo.Interfaces.IDAO;
import interno.nucleo.Interfaces.IDTO;
import interno.nucleo.Interfaces.IPeticion;
import interno.nucleo.Interfaces.IRespuesta;
import interno.nucleo.Funciones;

import java.util.logging.Logger;

//CLASE SINGLETON DaoRest
public final class DaoRest implements IDAO {
    protected final static Logger TRAZADOR = Logger.getLogger(DaoRest.class.getName());

    private static DaoRest instancia;
    private static String conexion;

    //CONSTRUCTOR PRIVADO + ACTIVADOR PUBLICO (PATRON SINGLETON)
    private DaoRest() {
    }
    public static synchronized DaoRest Activar(String url, String usuario, String password) {
        if (instancia == null) {instancia = new DaoRest();}
        conexion = url;
        return instancia;
    }
    //IMPLEMENTACION DE INTERFACE "IDAO"
    @Override public void Conectar(IDTO dto) {
        //No implementada
    }
    @Override public void Seleccionar(IDTO dto) {
        TRAZADOR.info("");
    }
    @Override public void Editar(IDTO dto) {
        TRAZADOR.info("");
    }
    @Override public void Agregar(IDTO dto) {
        TRAZADOR.info("");
    }
    @Override public void Borrar(IDTO dto) {
        TRAZADOR.info("");
    }
}