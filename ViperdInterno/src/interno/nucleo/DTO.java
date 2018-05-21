package interno.nucleo;
/* MAESTRO VIPERD (multi-plataforma)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import interno.nucleo.Interfaces.IDTO;
import interno.nucleo.Interfaces.IPeticion;
import interno.nucleo.Interfaces.IRespuesta;
import interno.nucleo.Interfaces.IDAO;
import interno.nucleo.Interfaces.IValueObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//CLASE DTO
public final class DTO implements IDTO {
    protected final static Logger TRAZADOR = Logger.getLogger(DTO.class.getName());
    
    private IPeticion peticion;
    private IRespuesta respuesta;
    private IDAO agente;
    private Enum accion, casodeuso;
    private Boolean estado, asincrono;
    private Map<String, String> elementos, mensajes;
    private Map<String, List<Map<String, String>>> conjuntos;

    public DTO(Enum accion) {
        this.accion = accion;
        this.casodeuso = null;
        this.estado = false;
        this.asincrono = false;
        this.agente = null;
        this.elementos = new LinkedHashMap<>();
        this.mensajes = new LinkedHashMap<>();
        this.conjuntos = new LinkedHashMap<>();
        this.peticion = new Peticion();
        this.respuesta = new Respuesta();
    }

    @Override public Enum getAccion() {return this.accion;}
    @Override public Enum getCasoDeUso() {return this.casodeuso;}
    @Override public void setCasoDeUso(Enum casodeuso) {this.casodeuso = casodeuso;}
    @Override public Boolean getEstado() {return this.estado;}
    @Override public Boolean setEstado(Boolean estado) {
        this.estado = estado;
        return this.estado;
    }
    @Override public Boolean getAsincrono() {return this.asincrono;}
    @Override public void setAsincrono(Boolean opcion) {this.asincrono = opcion;}
    @Override public void agregarMensaje(String codigo, String mensaje) {this.mensajes.put(codigo, mensaje);}
    @Override public void agregarMensajes(Map<String, String> mensajes) {
        for (Map.Entry<String, String> mensaje: mensajes.entrySet()) {
            this.mensajes.put(mensaje.getKey(), mensaje.getValue());
        }
    }
    @Override public Map<String, String> getMensajes() {return this.mensajes;}
    @Override public String getElemento(String nombre) {
        String valor = "";
        if (!nombre.isEmpty()) {valor = this.elementos.get(nombre);}
        return valor;
    }
    @Override public String setElemento(String nombre, String valor) {
        String resultado = "";
        if (!nombre.isEmpty()) {
            this.elementos.put(nombre, valor);
            resultado = valor;
        }
        return resultado;
    }
    @Override public Map<String, String> getElementos() {return this.elementos;}
    @Override public void setElementos(Map<String, String> elementos) {this.elementos = elementos;}
    @Override public IDAO getAgente() {
        try {
            return (this.agente);
        } catch (Exception e) {}
        return null;
    }
    @Override public void setAgente(IDAO agente) {
        this.agente = agente;
    }
    @Override public IPeticion getPeticion() {return this.peticion;}
    @Override public IRespuesta getRespuesta() {return this.respuesta;}
    @Override public void setConjuntos(Map<String, List<Map<String, String>>> conjuntos) {this.conjuntos = conjuntos;}
    @Override public List<Map<String, String>> getConjunto(String operacion) {
        if (!this.conjuntos.isEmpty() && !operacion.isEmpty()) {
            if (this.conjuntos.containsKey(operacion)) {return this.conjuntos.get(operacion);}
        }
        return null;
    }
    @Override public void Limpiar() {
        this.respuesta.Limpiar();
        this.peticion.Limpiar();
    }
    @Override public String elementosToString() {
        StringBuilder str = new StringBuilder();
        if (!this.elementos.entrySet().isEmpty()) {
            str.append("----------------------------------------------------\nELEMENTOS:\n| ");
            for (Map.Entry<String, String> elemento: this.elementos.entrySet()) {
                str.append(elemento.getKey()).append("=").append(elemento.getValue()).append(" | ");
            }
            str.append("\n");
        }
        return str.toString();
    }
    @Override public String conjuntosToString() {
        StringBuilder str = new StringBuilder();
        if (!this.conjuntos.entrySet().isEmpty()) {
            str.append("----------------------------------------------------\nCONJUNTOS:\n");
            for (Map.Entry<String, List<Map<String,String>>> lista: this.conjuntos.entrySet()) {
                String nombre_lista = lista.getKey();
                str.append("Conjunto: ").append(nombre_lista).append(" (total=").append(lista.getValue().size()).append(")\n");
                for (Map<String, String> caso: lista.getValue()) {
                    str.append("  Caso: | ");
                    for (Map.Entry<String, String> campo: caso.entrySet()) {
                        str.append(campo.getKey()).append("=").append(campo.getValue()).append(" | ");
                    }
                    str.append("\n");
                }
            }
        }
        return str.toString();
    }
    
    //SUBCLASES INTERNAS PARA DTO
    private class Peticion implements IPeticion {
        
        private String comando, operacion, fuente;
        private Map<String, String> parametros;
        private Map<String, IValueObject> estructura;
        
        public Peticion() {
            this.parametros = new LinkedHashMap<>();
            this.comando = "";
            this.operacion = "";
            this.estructura = null;
        }
        
        @Override public String getParametro(String nombre) {
            String valor = "";
            if (!nombre.isEmpty()) {valor = this.parametros.get(nombre);}
            return valor;
        }
        @Override public void setParametro(String nombre, String valor) {
            if (valor == null) {valor = "";}
            if (!nombre.isEmpty() && !valor.isEmpty()) {
                this.parametros.put(nombre, valor);
            }
        }
        @Override public Map<String, String> getParametros() {return this.parametros;}
        @Override public void setParametros(Map<String, String> parametros) {this.parametros = parametros;}
        @Override public void vaciarParametros() {this.parametros.clear();}
        @Override public String getComando() {return this.comando;}
        @Override public void setComando(String comando) {this.comando = comando;}
        @Override public String getOperacion() {return this.operacion;}
        @Override public void setOperacion(String operacion) {this.operacion = operacion;}
        @Override public String getFuente() {return this.fuente;}
        @Override public void setFuente(String fuente) {this.fuente = fuente;}
        @Override public void Limpiar() {
            this.parametros.clear();
            this.comando = "";
            this.operacion = "";
        }
        @Override public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("PETICION:\n| ");
            for (Map.Entry<String, String> parametro: this.parametros.entrySet()) {
                str.append(parametro.getKey()).append("=").append(parametro.getValue()).append(" | ");
            }
            str.append("\n");
            return str.toString();
        }
        @Override public void setEstructura(Map<String, Interfaces.IValueObject> estructura) {this.estructura = estructura;}
        @Override public Map<String, IValueObject> getEstructura() {return this.estructura;}
    }
    private class Respuesta implements IRespuesta {
        
        private Map<String, List<Map<String, String>>> datos;
        private Integer cuenta;
        private String uid, estado, mensaje;
        
        public Respuesta() {
            this.datos = new LinkedHashMap<>();
            this.estado = "";
            this.mensaje = "";
            this.uid = "";
            this.cuenta = 0;
            Limpiar();
        }
        
        @Override public Map<String, List<Map<String, String>>> getDatos() {return this.datos;}
        @Override public String[] getOperaciones() {
            int i = 0;
            String[] listas = new String[this.datos.size()];
            for (Map.Entry<String, List<Map<String,String>>> lista: this.datos.entrySet()) {
                listas[i] = lista.getKey();
                i++;
            }
            return listas;
        }
        @Override public List<Map<String, String>> getListado(String operacion) {
            if (operacion.isEmpty()) {operacion = "caso";}
            try {
                return this.datos.get(operacion);
            } catch(Exception e) {
                return new ArrayList<>();
            }
        }
        @Override public void setListado(List<Map<String, String>> lista, String operacion) {
            if (this.datos == null) {this.datos = new LinkedHashMap<>();}
            if (operacion.isEmpty()) {operacion = "caso";}
            this.datos.remove(operacion);
            if (null != lista) {this.datos.put(operacion, lista);}
        }
        @Override public Integer getFilasListado(String operacion) {
            int total = 0;
            if (this.datos.containsKey(operacion)) {
                total = this.datos.get(operacion).size();
            }
            return total;
        }
        @Override public Integer getCuentaCasos() {return this.cuenta;}
        @Override public void setCuentaCasos(Integer cuenta) {this.cuenta = cuenta;}
        @Override public String getUID() {return this.uid;}
        @Override public void setUID(String uid) {this.uid = uid;}
        @Override public String getEstado() {return this.estado;}
        @Override public void setEstado(String estado) {this.estado = estado;}
        @Override public String getMensaje() {return this.mensaje;}
        @Override public void setMensaje(String mensaje) {this.mensaje = mensaje;}
        @Override public void Limpiar() {
            this.mensaje = "";
            this.estado = "";
            this.uid = "";
            this.cuenta = 0;
            this.datos.clear();
        }
        @Override public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("estado=").append(this.estado).append(", mensaje=").append(this.mensaje).append("\n");
            for (Map.Entry<String, List<Map<String,String>>> lista: this.datos.entrySet()) {
                String nombre_lista = lista.getKey();
                Integer columnas = 0;
                if (!getListado(nombre_lista).isEmpty()) {columnas = getListado(nombre_lista).get(0).size();}
                str.append("Lista: ").append(nombre_lista).append(" (filas=").append(getFilasListado(nombre_lista)).append(", columnas=").append(columnas.toString()).append(")\n");
                for (Map<String, String> caso: lista.getValue()) {
                    str.append("  Caso: | ");
                    for (Map.Entry<String, String> campo: caso.entrySet()) {
                        str.append(campo.getKey()).append("=").append(campo.getValue()).append(" | ");
                    }
                    str.append("\n");
                }
            }
            return str.toString();
        }
    }
}