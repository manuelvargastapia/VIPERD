package interno.datos;
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
import interno.nucleo.Interfaces.IValueObject;

import java.util.Map;
import java.util.logging.Logger;

//SUPERCLASE ABSTRACTA Agente
public abstract class Agente implements IDAO {
    protected final static Logger TRAZADOR = Logger.getLogger(Agente.class.getName());

    protected IDAO dao;
    protected Map<String, IValueObject> estructura;
    protected String origen, operacion, comando, fuente;
    
    public Agente() {
        TRAZADOR.info(this.getClass().getSimpleName());
    }
    protected void Configurar(IDTO dto) {
        dto.getPeticion().setFuente(this.fuente);
        dto.getPeticion().setComando(generarSQL(this.comando, this.fuente, dto));
        this.dao.Conectar(dto);
        TRAZADOR.info("fuente: " + this.fuente);
    }
    
    //IMPLEMENTACION DE LA INTERFACE "IDAO"
    @Override public void Conectar(IDTO dto) {
        this.estructura = dto.getPeticion().getEstructura();
        this.operacion = dto.getPeticion().getOperacion();
        TRAZADOR.info("operacion: " + this.operacion);
    }
    @Override public void Seleccionar(IDTO dto) {
        this.dao.Seleccionar(dto);
    }
    @Override public void Editar(IDTO dto) {
        this.dao.Editar(dto);
    }
    @Override public void Agregar(IDTO dto) {
        this.dao.Agregar(dto);
    }
    @Override public void Borrar(IDTO dto) {
        this.dao.Borrar(dto);
    }

    //FUNCIONES PUBLICAS
    public static String reemplazarTexto(String cadena, String buscar, String reemplazar) {
        StringBuilder sb = null;
        int start = 0;
        for (int i; (i = cadena.indexOf(buscar, start)) != -1;) {
            if (sb == null) {
                sb = new StringBuilder();
            }
            sb.append(cadena, start, i);
            sb.append(reemplazar);
            start = i + buscar.length();
        }
        if (sb == null) {
            return cadena;
        }
        sb.append(cadena, start, cadena.length());
        return sb.toString();
    }
    public static String reemplazarComillas(String cadena) {
        if (cadena.contains("'")) {
            StringBuilder sb = null;
            int start = 0;
            for (int i; (i = cadena.indexOf("'", start)) != -1;) {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(cadena, start, i);
                sb.append("''");
                start = i + 1;
            }
            if (sb == null) {
                return cadena;
            } else {
                sb.append(cadena, start, cadena.length());
                return sb.toString();
            }
        } else {
            return cadena;
        }
    }
    
    //FUNCIONES INTERNAS
    private String unirTextos(String[] lista, String separador) {
        StringBuilder str = new StringBuilder();
        for (int i = 0, largo = lista.length; i < largo; i++) {
            if (lista[i] != null) {
                if (i > 0) {
                    str.append(separador);
                }
                str.append(lista[i]);
            }
        }
        return str.toString();
    }
    private String generarSQL(String expresion, String tabla, IDTO dto) {
        String sql = "", clave = "", valor = "";
        StringBuilder str1, str2;
        String[] columnas;
        int i = 0;
        switch (expresion) {
            case "SELECT": 
                columnas = new String[this.estructura.size()];
                for (IValueObject item: this.estructura.values()) {
                    String campo = item.get("d");
                    String nombre = item.get("nombre");
                    if (item.get("filtro").contains("S") && !campo.isEmpty()) {
                        columnas[i] = new StringBuilder(campo).toString(); i++;
                    }
                    if (item.get("filtro").contains("C") && !campo.isEmpty()) {
                        clave = campo;
                        valor = dto.getPeticion().getParametro(nombre);
                    }
                }
                sql = new StringBuilder("SELECT ").append(unirTextos(columnas, ", ")).append(" FROM ").append(tabla).append(" WHERE ").append(clave).append("='").append(valor).append("'").toString();
                break;
            case "LISTADO": 
                columnas = new String[this.estructura.size()];
                for (IValueObject item: this.estructura.values()) {
                    String campo = item.get("d");
                    String nombre = item.get("nombre");
                    if (item.get("filtro").contains("L") && !campo.isEmpty()) {
                        columnas[i] = new StringBuilder(campo).toString(); i++;
                    }
                }
                sql = new StringBuilder("SELECT ").append(unirTextos(columnas, ", ")).append(" FROM ").append(tabla).append(" ").toString();
                break;
            case "INSERT": 
                str1 = new StringBuilder("INSERT INTO ");
                str1.append(tabla).append(" (");
                str2 = new StringBuilder(") VALUES (");
                for (IValueObject item: this.estructura.values()) {
                    String campo = item.get("d");
                    String nombre = item.get("nombre");
                    if (item.get("filtro").contains("I") && !campo.isEmpty()) {
                        str1.append(campo).append(", ");
                        str2.append("'").append(reemplazarComillas(dto.getPeticion().getParametro(nombre))).append("', ");
                    }                
                }
                sql = reemplazarTexto((str1.toString() + str2.toString() + ")"), ", )", ")");
                break;
            case "UPDATE": 
                str1 = new StringBuilder("UPDATE ");
                str1.append(tabla).append(" SET  ");
                for (IValueObject item: this.estructura.values()) {
                    String campo = item.get("d");
                    String nombre = item.get("nombre");
                    if (item.get("filtro").contains("U") && !campo.isEmpty()) {
                        str1.append(campo).append("='").append(reemplazarComillas(dto.getPeticion().getParametro(nombre))).append("', ");
                    }
                    if (item.get("filtro").contains("C") && !campo.isEmpty()) {
                        clave = campo;
                        valor = dto.getPeticion().getParametro(nombre);
                    }
                }
                sql = str1.toString();
                sql = sql.substring(0, sql.length()-2) + " WHERE " + clave + "='" + valor + "'";
                break;
            case "DELETE":
                for (IValueObject item: this.estructura.values()) {
                    String campo = item.get("d");
                    String nombre = item.get("nombre");
                    if (item.get("filtro").contains("C") && !campo.isEmpty()) {
                        clave = campo;
                        valor = dto.getPeticion().getParametro(nombre);
                    }
                }
                sql = "DELETE FROM " + tabla + " WHERE " + clave + "='" + valor + "'";
                break;
            default:
                sql = expresion;
                break;
        }
        return sql;
    }
}