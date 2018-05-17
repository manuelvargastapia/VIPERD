package interno.nucleo;
/* MAESTRO VIPERD (multi-plataforma)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import java.util.List;
import java.util.Map;

public interface Interfaces {
    
    interface IVista {
        void setValor(String nombre, String valor);
        void setEnfocado(String nombre);
        void setActivo(String nombre, Boolean opcion);
        void setVisible(String nombre, Boolean opcion);
        void mostrarMensajes(Map<String, String> mensajes);
        void mostrarMensaje(String texto, String opcion);
        void mostrarVista(Boolean opcion);
        void actualizarContenedor(Map<String, String> elementos);
        void actualizarConjunto(List<Map<String, String>> conjunto);
        void Cerrar();
        void Redirigir(String destino);
        void setEntorno(String nombre, String valor);
        String getValor(String nombre);
        Map<String, String> getValores();
        Map<String, String> Entorno();
    }
    interface IInteractor {
        IDTO crearDTO(Enum accion);
        void solicitarCasoDeUso(IDTO dto);
    }
    interface IPresentador {
        void pedirAccion(Enum accion, String caso);
        void mostrarResultado(IDTO dto);
        Boolean autorizarAcceso(Enum accion, String roles);
        Map<String, String> Entorno();
    }
    interface IEntidad extends IValueObject {
        Boolean setAtributos(Map<String, String> atributos);
        Map<String, String> getAtributos();
        String getEntidad();
        Map<String, IValueObject> getEstructura();
        Boolean getEstado();
        Map<String, String> getMensajes();
        Float Calcular(ICalculo calculo, float[] valor);
        Map<String, String> TransformarDatos(Map<String, String> datos, String destino, String filtro, Boolean evaluar);
        String mensajesToString();
        String entidadToString();
        String entidadToJson();
        void Vaciar();
    }
    interface IDAO {
        void Conectar(IDTO dto);
        void Seleccionar(IDTO dto);
        void Editar(IDTO dto);
        void Borrar(IDTO dto);
        void Agregar(IDTO dto);
    }

    interface IDTO {
        Enum getAccion();
        Enum getCasoDeUso();
        void setCasoDeUso(Enum casodeuso);
        Boolean getEstado();
        Boolean setEstado(Boolean estado);
        Boolean getAsincrono();
        void setAsincrono(Boolean opcion);
        void agregarMensaje(String codigo, String mensaje);
        void agregarMensajes(Map<String, String> mensajes);
        Map<String, String> getMensajes();
        String getElemento(String nombre);
        String setElemento(String nombre, String valor);
        Map<String, String> getElementos();
        void setElementos(Map<String, String> elementos);
        List<Map<String, String>> getConjunto(String operacion);
        void setConjuntos(Map<String, List<Map<String, String>>> conjuntos);
        void setAgente(IDAO agente);
        IDAO getAgente();
        void Limpiar();
        IPeticion getPeticion();
        IRespuesta getRespuesta();
        String elementosToString();
        String conjuntosToString();
    }
    interface IPeticion {
        String getParametro(String nombre);
        void setParametro(String nombre, String valor);
        Map<String, String> getParametros();
        void setParametros(Map<String, String> parametros);
        void vaciarParametros();
        String getComando();
        void setComando(String expresion);
        String getOperacion();
        void setOperacion(String operacion);
        String getFuente();
        void setFuente(String fuente);
        void setEstructura(Map<String, IValueObject> estructura);
        Map<String, IValueObject> getEstructura();
        void Limpiar();
        String parametrosToString();
    }
    interface IRespuesta {
        Map<String, List<Map<String, String>>> getDatos();
        String[] getOperaciones();
        List<Map<String, String>> getListado(String operacion);
        void setListado(List<Map<String, String>> lista, String operacion);
        Integer getFilasListado(String operacion);
        Integer getCuentaCasos();
        void setCuentaCasos(Integer cuenta);
        String getUID();
        void setUID(String uid);
        String getEstado();
        void setEstado(String estado);
        String getMensaje();
        void setMensaje(String mensaje);
        void Limpiar();
        String respuestaToString();
    }
    interface IValueObject {
        String get(String nombre);
        Boolean set(String nombre, String valor);
    }
    interface IEstructura {
        String V();
        String D();
        String Etiqueta();
        String Filtro();
        String Regla();
        String Min();
        String Max();
        String E1();
        String E2();
        String E3();
        String Inicial();
        String Tipo();
        String Nombre();
    }
    interface IOperacion {
        String Origen();
        String Comando();
        String Fuente();
    }
    interface ICalculo {
        float Calcular(float[] valor);
    }
    interface IRegla {
        String Inclusion();
        String ExpRegular();
        String FormatoVista();
        String FormatoDatos();
    }
    interface IAccion {
        String Requisitos();
    }
}