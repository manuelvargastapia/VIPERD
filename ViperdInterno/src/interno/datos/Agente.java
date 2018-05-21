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
import interno.nucleo.Funciones;

import java.util.logging.Logger;

//SUPERCLASE ABSTRACTA Agente
public abstract class Agente implements IDAO {
    protected final static Logger TRAZADOR = Logger.getLogger(Agente.class.getName());

    protected IDAO dao;
    protected String origen, operacion, comando, fuente;
    
    public Agente() {
        TRAZADOR.info(this.getClass().getSimpleName());
    }
    
    //IMPLEMENTACION DE LA INTERFACE "IDAO"
    @Override public void Conectar(String origen) {
        this.origen = origen;
        Activar(this.origen);
        TRAZADOR.info("origen: " + this.origen);
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

    //FUNCIONES INTERNAS
    protected void Configurar(IDTO dto) {
        dto.getPeticion().setFuente(this.fuente);
        dto.getPeticion().setComando(generarSQL(this.comando, this.fuente, dto));
        TRAZADOR.info("origen: " + this.origen + " - fuente: " + this.fuente);
        this.dao.Conectar(dto);
    }
    protected void Activar(String origen) {}
    protected String generarSQL(String expresion, String tabla, IDTO dto) {
        String sql = "", clave = "", valor = "";
        StringBuilder str1, str2;
        String[] columnas;
        int i = 0;
        switch (expresion) {
            case "SELECT": 
                columnas = new String[dto.getPeticion().getEstructura().size()];
                for (IValueObject item: dto.getPeticion().getEstructura().values()) {
                    String campo = item.get("d");
                    if (item.get("filtro").contains("S") && !campo.isEmpty()) {
                        columnas[i] = new StringBuilder(campo).toString(); i++;
                    }
                    if (item.get("filtro").contains("C") && !campo.isEmpty()) {
                        clave = campo;
                        valor = dto.getPeticion().getParametro(campo);
                    }
                }
                sql = new StringBuilder("SELECT ").append(Funciones.unirTextos(columnas, ", ")).append(" FROM ").append(tabla).append(" WHERE ").append(clave).append("='").append(valor).append("'").toString();
                break;
            case "LISTADO": 
                columnas = new String[dto.getPeticion().getEstructura().size()];
                for (IValueObject item: dto.getPeticion().getEstructura().values()) {
                    String campo = item.get("d");
                    if (item.get("filtro").contains("L") && !campo.isEmpty()) {
                        columnas[i] = new StringBuilder(campo).toString(); i++;
                    }
                }
                sql = new StringBuilder("SELECT ").append(Funciones.unirTextos(columnas, ", ")).append(" FROM ").append(tabla).append(" ").toString();
                break;
            case "UPDATE": 
                str1 = new StringBuilder("UPDATE ");
                str1.append(tabla).append(" SET  ");
                for (IValueObject item: dto.getPeticion().getEstructura().values()) {
                    String campo = item.get("d");
                    if (item.get("filtro").contains("U") && !campo.isEmpty()) {
                        str1.append(campo).append("='").append(Funciones.reemplazarComillas(dto.getPeticion().getParametro(campo))).append("', ");
                    }
                    if (item.get("filtro").contains("C") && !campo.isEmpty()) {
                        clave = campo;
                        valor = dto.getPeticion().getParametro(campo);
                    }
                }
                sql = str1.toString();
                sql = sql.substring(0, sql.length()-2) + " WHERE " + clave + "='" + valor + "'";
                break;
            case "DELETE":
                for (IValueObject item: dto.getPeticion().getEstructura().values()) {
                    String campo = item.get("d");
                    if (item.get("filtro").contains("C") && !campo.isEmpty()) {
                        clave = campo;
                        valor = dto.getPeticion().getParametro(campo);
                    }
                }
                sql = "DELETE FROM " + tabla + " WHERE " + clave + "='" + valor + "'";
                break;
            case "INSERT": 
                str1 = new StringBuilder("INSERT INTO ");
                str1.append(tabla).append(" (");
                str2 = new StringBuilder(") VALUES (");
                for (IValueObject item: dto.getPeticion().getEstructura().values()) {
                    String campo = item.get("d");
                    if (item.get("filtro").contains("I") && !campo.isEmpty()) {
                        str1.append(campo).append(", ");
                        str2.append("'").append(Funciones.reemplazarComillas(dto.getPeticion().getParametro(campo))).append("', ");
                    }                
                }
                sql = Funciones.reemplazarTexto((str1.toString() + str2.toString() + ")"), ", )", ")");
                break;
            default:
                sql = expresion;
                break;
        }
        return sql;
    }
}