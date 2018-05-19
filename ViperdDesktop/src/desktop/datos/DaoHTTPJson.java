package desktop.datos;
/* MAESTRO VIPERD (multi-plataforma)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import interno.datos.Agente;
import interno.nucleo.Interfaces.IDAO;
import interno.nucleo.Interfaces.IDTO;
import interno.nucleo.Interfaces.IPeticion;
import interno.nucleo.Interfaces.IRespuesta;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//CLASE SINGLETON DaoHTTPJson
public final class DaoHTTPJson implements IDAO {
    protected final static Logger TRAZADOR = Logger.getLogger(DaoHTTPJson.class.getName());
    
    private static DaoHTTPJson instancia;
    private static String conexion;

    //CONSTRUCTOR PRIVADO + ACTIVADOR PUBLICO (PATRON SINGLETON)
    private DaoHTTPJson() {
    }
    public static synchronized DaoHTTPJson Activar(String url, String usuario, String password) {
        if (instancia == null) {instancia = new DaoHTTPJson();}
        conexion = url;
        return instancia;
    }
    
    //IMPLEMENTACION DE INTERFACE "IDAO"
    @Override public void Conectar(IDTO dto) {
        //No implementada
    }
    @Override public void Seleccionar(IDTO dto) {
        TRAZADOR.info("");
        try {ejecutarPeticion(dto.getPeticion(), dto.getRespuesta());} 
        catch (Exception e) {TRAZADOR.info(e.getMessage());}
    }
    @Override public void Editar(IDTO dto) {
        TRAZADOR.info("");
        try {ejecutarPeticion(dto.getPeticion(), dto.getRespuesta());} 
        catch (Exception e) {TRAZADOR.info(e.getMessage());}
    }
    @Override public void Agregar(IDTO dto) {
        TRAZADOR.info("");
        try {ejecutarPeticion(dto.getPeticion(), dto.getRespuesta());} 
        catch (Exception e) {TRAZADOR.info(e.getMessage());}
    }
    @Override public void Borrar(IDTO dto) {
        TRAZADOR.info("");
        try {ejecutarPeticion(dto.getPeticion(), dto.getRespuesta());} 
        catch (Exception e) {TRAZADOR.info(e.getMessage());}
    }
    
    //FUNCIONES PRIVADAS
    private String generarArgumentos(String expresion, Map<String, String> parametros) throws UnsupportedEncodingException {
        String cadena;
        if (expresion.indexOf("?")>0) {cadena = expresion.substring(expresion.indexOf("?") + 1);}
        else {cadena = expresion.toString();}
        if (parametros != null && parametros.size()>0 && cadena.contains("[")) {
            for (Map.Entry<String, String> parametro : parametros.entrySet()) {
                cadena = Agente.reemplazarTexto(cadena, "[" + parametro.getKey() + "]", URLEncoder.encode(parametro.getValue(), "UTF-8"));
            }
        }
        return cadena;
    }
    private String convertirParametrosEnJSON(Map<String, String> datos, Boolean codificar) {
        String cadena = "";
        if (datos != null && datos.size()>0) {
            JSONObject json = new JSONObject();
            for (Map.Entry<String, String> entry : datos.entrySet()) {
                try {
                    json.put(entry.getKey(), entry.getValue());
                } catch (Exception e) {TRAZADOR.info(e.getMessage());}
            }
            if (codificar) {
                try {cadena = URLEncoder.encode(json.toString(), "UTF-8");}
                catch (Exception e) {TRAZADOR.info(e.getMessage());}
            } else {cadena = json.toString();}
        }
        return cadena;
    }
    private String resolverDireccion(String expresion) {
        String cadena = "";
        if (expresion.contains("?")) {cadena = expresion.substring(0, expresion.indexOf("?"));}
        return conexion + cadena;
    }
    private synchronized void ejecutarPeticion(IPeticion peticion, IRespuesta respuesta) throws UnsupportedEncodingException {
        URL url;
        String expresion = peticion.getComando();
        Map<String, String> parametros = peticion.getParametros();
        HttpURLConnection con = null;
        BufferedReader reader = null;
        StringBuilder resultado = new StringBuilder();
        String argumentos = Agente.reemplazarTexto(generarArgumentos(expresion, parametros), "[json]", convertirParametrosEnJSON(parametros, true));
        String dir = resolverDireccion(expresion);
        TRAZADOR.info("URL: " + dir);
        TRAZADOR.info("Argumentos: " + argumentos);
        try {
            url = new URL(dir + "?_=" + System.currentTimeMillis());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(15000);
            con.setReadTimeout(45000);
            con.setDefaultUseCaches(false);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setFixedLengthStreamingMode(argumentos.getBytes().length);
            PrintWriter writer = new PrintWriter(con.getOutputStream());
            writer.print(argumentos);
            writer.close();
            con.connect();
            try {
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {resultado.append(line);}
            } catch (Exception e) {
                TRAZADOR.info(e.getMessage());
            }
            finally {
                if (reader != null) {
                    try {reader.close();}
                    catch (Exception e) {TRAZADOR.info(e.getMessage());}
                }
            }
        } catch (Exception e) {
            TRAZADOR.info(e.getMessage());
        }
        finally {if(con != null) {con.disconnect();}}
        mapearRespuesta(resultado.toString(), respuesta, peticion.getOperacion(), peticion.getFuente());
    }
    private synchronized void mapearRespuesta(String datos, IRespuesta respuesta, String operacion, String fuente) {
        if (respuesta == null) {return;}
        TRAZADOR.info("operacion=" + operacion + ", fuente=" + fuente);
        List<Map<String, String>> lista = new ArrayList<>();
        Map<String, String> caso;
        if(!datos.isEmpty() && !datos.equals("[]")) {
            try {
                JSONObject json = new JSONObject(datos);
                if (json.optString("estado").length()>0) {respuesta.setEstado(json.getString("estado"));}
                if (json.optString("mensaje").length()>0) {respuesta.setMensaje(json.getString("mensaje"));}
                if (json.optJSONArray(fuente)!=null) {
                    JSONArray jlista = json.optJSONArray(fuente);
                    for (int i = 0; i < jlista.length(); i++) {
                        JSONObject jcaso = jlista.optJSONObject(i);
                        if (jcaso != null) {
                            caso = new LinkedHashMap<>();
                            for(Iterator iterator = jcaso.keys(); iterator.hasNext();) {
                                String key = (String) iterator.next();
                                caso.put(key, jcaso.get(key).toString());
                            }
                            lista.add(caso);
                        }
                    }
                    respuesta.setListado(lista, operacion);
                } else if (json.optJSONObject(fuente)!=null) {
                    JSONObject jcaso = json.optJSONObject(fuente);
                    caso = new LinkedHashMap<>();
                    for(Iterator iterator = jcaso.keys(); iterator.hasNext();) {
                        String key = (String) iterator.next();
                        caso.put(key, jcaso.get(key).toString());
                    }
                    lista.add(caso);
                    respuesta.setListado(lista, operacion);
                }
            } catch (Exception e) {
                TRAZADOR.info(e.getMessage());
            }
        } else {
            respuesta.setEstado("0");
            respuesta.setMensaje("NO se pudo conectar con el servicio");
            respuesta.setListado(lista, operacion);
        }
        TRAZADOR.info(respuesta.respuestaToString());
    }
}