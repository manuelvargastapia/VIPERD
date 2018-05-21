package desktop.datos;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import interno.nucleo.Interfaces.IDAO;
import interno.nucleo.Interfaces.IDTO;
import interno.nucleo.Interfaces.IRespuesta;
import interno.nucleo.Funciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

//CLASE SINGLETON DaoMySql
public final class DaoMySql implements IDAO {
    protected final static Logger TRAZADOR = Logger.getLogger(DaoMySql.class.getName());
    
    private static DaoMySql instancia;
    private static BasicDataSource db = null;

    //CONSTRUCTOR PRIVADO + ACTIVADOR PUBLICO
    private DaoMySql() {
        db = new BasicDataSource();
        db.setDriverClassName("com.mysql.jdbc.Driver");
    }
    public static synchronized DaoMySql Activar(String url, String usuario, String password) {
        if (instancia == null) {instancia = new DaoMySql();}
        db.setUsername(usuario);
        db.setPassword(password);
        db.setUrl("jdbc:mysql://" + url);
        return instancia;
    }

    //IMPLEMENTACION DE INTERFACE "IDAO"
    @Override public void Conectar(IDTO dto) {
        //No implementada
    }
    @Override public void Seleccionar(IDTO dto) {
        Connection bd = null;
        String expresion = "";
        try {
            expresion = generarExpresion(dto.getPeticion().getComando(), dto.getPeticion().getParametros());
            if (!expresion.isEmpty()) {
                TRAZADOR.info(expresion);
                bd = db.getConnection();
                Statement instruccion = bd.createStatement();
                ResultSet datos = instruccion.executeQuery(expresion);
                mapearRespuesta(datos, dto.getRespuesta(), dto.getPeticion().getOperacion());
                datos.close();
                instruccion.close();
            } else {
                dto.getRespuesta().setCuentaCasos(0);
                dto.getRespuesta().setEstado("0");
                dto.getRespuesta().setMensaje("Petición NO válida");
            }
        } catch (SQLException e) {
            dto.getRespuesta().setCuentaCasos(0);
            dto.getRespuesta().setEstado("0");
            dto.getRespuesta().setMensaje(e.getMessage());
            TRAZADOR.info(e.getMessage());
        }
        finally {
            try {if (null != bd) {bd.close();}}
            catch (SQLException e) {TRAZADOR.info(e.getMessage());}
        }
    }
    @Override public void Agregar(IDTO dto) {
        Connection bd = null;
        String expresion = "";
        String uid = "";
        try {
            expresion = generarExpresion(dto.getPeticion().getComando(), dto.getPeticion().getParametros());
            if (!expresion.isEmpty()) {
                TRAZADOR.info(expresion);
                bd = db.getConnection();
                Statement instruccion = bd.createStatement();
                int total = instruccion.executeUpdate(expresion, Statement.RETURN_GENERATED_KEYS);
                dto.getRespuesta().setCuentaCasos(total);
                if (total >0) {
                    dto.getRespuesta().setEstado("1");
                    ResultSet resultados = instruccion.getGeneratedKeys();
                    if (resultados.next()) {
                        uid = resultados.getObject(1).toString();
                    }
                    resultados.close();
                    if (!uid.isEmpty()) {
                        List<Map<String, String>> resultado = new ArrayList<>();
                        Map<String, String> fila = new LinkedHashMap<>();
                        dto.getRespuesta().setUID(uid);
                        fila.put("id", uid);
                        resultado.add(fila);
                        dto.getRespuesta().setListado(resultado, dto.getPeticion().getOperacion());
                    }
                } else {
                    dto.getRespuesta().setEstado("0");
                    dto.getRespuesta().setMensaje("El caso NO se agregó");
                }
                instruccion.close();
            } else {
                dto.getRespuesta().setEstado("0");
                dto.getRespuesta().setMensaje("Petición NO válida");
            }
        } catch (SQLException e) {
            dto.getRespuesta().setEstado("0");
            dto.getRespuesta().setMensaje(e.getMessage());
            TRAZADOR.info(e.getMessage());
        }
        finally {
            try {if (null != bd) {bd.close();}}
            catch (SQLException e) {TRAZADOR.info(e.getMessage());}
        }
        TRAZADOR.info("UID=" + uid + " - " + dto.getRespuesta().toString());
    }
    @Override public void Editar(IDTO dto) {
        Connection bd = null;
        String expresion = "";
        Integer total = 0;
        try {
            expresion = generarExpresion(dto.getPeticion().getComando(), dto.getPeticion().getParametros());
            if (!expresion.isEmpty()) {
                TRAZADOR.info(expresion);
                bd = db.getConnection();
                Statement instruccion = bd.createStatement();
                total = instruccion.executeUpdate(expresion);
                dto.getRespuesta().setCuentaCasos(total);
                if (total >0) {
                    dto.getRespuesta().setEstado("1");
                } else {
                    dto.getRespuesta().setEstado("0");
                    dto.getRespuesta().setMensaje("El caso NO se modificó");
                }
                instruccion.close();
            } else {
                dto.getRespuesta().setEstado("0");
                dto.getRespuesta().setMensaje("Petición NO válida");
            }
        } catch (SQLException e) {
            dto.getRespuesta().setCuentaCasos(0);
            dto.getRespuesta().setEstado("0");
            dto.getRespuesta().setMensaje(e.getMessage());
            TRAZADOR.info(e.getMessage());
        }
        finally {
            try {if (null != bd) {bd.close();}}
            catch (SQLException e) {TRAZADOR.info(e.getMessage());}
        }
        TRAZADOR.info("filas=" + total.toString());
    }
    @Override public void Borrar(IDTO dto) {
        Connection bd = null;
        String expresion = "";
        Integer total= 0;
        try {
            expresion = generarExpresion(dto.getPeticion().getComando(), dto.getPeticion().getParametros());
            if (!expresion.isEmpty()) {
                TRAZADOR.info(expresion);
                bd = db.getConnection();
                Statement instruccion = bd.createStatement();
                total = instruccion.executeUpdate(expresion);
                dto.getRespuesta().setCuentaCasos(total);
                if (total >0) {
                    dto.getRespuesta().setEstado("1");
                } else {
                    dto.getRespuesta().setEstado("0");
                    dto.getRespuesta().setMensaje("El caso NO se eliminó");
                }
                instruccion.close();
            } else {
                dto.getRespuesta().setEstado("0");
                dto.getRespuesta().setMensaje("Petición NO válida");
            }
        } catch (SQLException e) {
            dto.getRespuesta().setCuentaCasos(0);
            dto.getRespuesta().setEstado("0");
            dto.getRespuesta().setMensaje(e.getMessage());
            TRAZADOR.info(e.getMessage());
        }
        finally {
            try {if (null != bd) {bd.close();}}
            catch (SQLException e) {TRAZADOR.info(e.getMessage());}
        }
        TRAZADOR.info("filas=" + total.toString());
    }
    
    //FUNCIONES PRIVADAS
    private String generarExpresion(String expresion, Map<String, String> parametros) {
        String cadena = expresion;
        if (cadena.contains("[")) {
            for (Map.Entry<String, String> parametro: parametros.entrySet()) {
                try {
                    cadena = Funciones.reemplazarTexto(cadena, "[" + parametro.getKey() + "]", Funciones.reemplazarComillas(parametro.getValue()));
                } catch (Exception e) {TRAZADOR.info(e.getMessage());}
            }
        }
        return cadena;
    }
    private void mapearRespuesta(ResultSet datos, IRespuesta respuesta, String operacion) {
        if (null == respuesta || null == datos) {return;}
        List<Map<String, String>> resultado = new ArrayList<>();
        respuesta.setEstado("1");
        int cuenta = 0;
        try {
            if (datos.first()) {
                Map<String, String> fila;
                String valor;
                do {
                    fila = new LinkedHashMap<>();
                    for (int i = 1; i <= datos.getMetaData().getColumnCount(); i++) {
                        valor = "";
                        if (null != datos.getObject(i)) {valor = datos.getObject(i).toString();}
                        fila.put(datos.getMetaData().getColumnName(i), valor);
                    }
                    cuenta++;
                    resultado.add(fila);
                } while (datos.next());
            }
        } catch (Exception e) {
            respuesta.setEstado("0");
            respuesta.setMensaje("Respuesta NO válida");
            TRAZADOR.info(e.getMessage());
        }
        respuesta.setCuentaCasos(cuenta);
        respuesta.setListado(resultado, operacion);
        TRAZADOR.info(respuesta.toString());
    }
}