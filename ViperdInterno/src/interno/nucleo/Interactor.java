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
import interno.nucleo.Interfaces.IPresentador;
import interno.nucleo.Interfaces.IInteractor;
import interno.nucleo.Interfaces.IEntidad;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

//SUPERCLASE ABSTRACTA Interactor
public abstract class Interactor implements IInteractor {
    protected final static Logger TRAZADOR = Logger.getLogger(Interactor.class.getName());

    protected IPresentador presentador;
    protected Map<String, String> entorno;

    public Interactor(IPresentador presentador) {
        this.presentador = presentador;
        this.entorno = this.presentador.Entorno();
        TRAZADOR.info(this.getClass().getSimpleName());
    }
    public Interactor(IPresentador presentador, Map<String, String> entorno) {
        this.presentador = presentador;
        this.entorno = entorno;
        TRAZADOR.info(this.getClass().getSimpleName());
    }

    //IMPLEMENTACION DE INTERFACE "IInteractor"
    @Override public IDTO crearDTO(Enum accion) {
        TRAZADOR.info("");
        return new DTO(accion);
    }
    @Override public void solicitarCasoDeUso(IDTO dto) {
        TRAZADOR.info("");
        if (dto.getAsincrono()) {new Thread(new GestorSolicitudes(dto)).start();}
        else {ejecutarCasoDeUso(dto);}
    }
    
    //FUNCIONES INTERNAS
    protected void ejecutarCasoDeUso(IDTO dto) {}
    protected Boolean validarParametros(IDTO dto, IEntidad entidad, String filtro) {
        TRAZADOR.info("entidad=" + entidad.getClass().getSimpleName() + ", filtro=" + filtro);
        Map<String, String> parametros = entidad.TransformarDatos(dto.getElementos(), "D", filtro, true);
        if (entidad.getEstado()) {
            dto.getPeticion().setParametros(parametros);
        } else {
            dto.setEstado(false);
            dto.agregarMensajes(entidad.getMensajes());
        }
        TRAZADOR.info("Resultado: " + entidad.getEstado().toString());
        return entidad.getEstado();
    }
    protected void prepararOperacion(IDTO dto, IEntidad entidad, String operacion) {
        dto.getPeticion().setOperacion(operacion);
        dto.getPeticion().setEstructura(entidad.getEstructura());
        dto.getAgente().Conectar(dto);
    }
    protected Boolean procesarRespuesta(IDTO dto, IEntidad entidad, String filtro) {
        TRAZADOR.info("entidad=" + entidad.getClass().getSimpleName() + ", filtro=" + filtro);
        Boolean resultado = false;
        String codigo = "EXITO-EXTERNO";
        String mensaje = dto.getRespuesta().getMensaje();
        if (dto.getRespuesta().getEstado().equalsIgnoreCase("1")) {resultado = dto.setEstado(true);}
        else {resultado = dto.setEstado(false);}
        if (!mensaje.isEmpty()) {
            if (!resultado) {codigo = "ERROR-EXTERNO";}
            dto.agregarMensaje(codigo, mensaje);
        }
        if (resultado) {
            Map<String, List<Map<String, String>>> conjunto = new LinkedHashMap<>();
            int total_listas = dto.getRespuesta().getOperaciones().length;
            int lista_actual = 0;
            for (Map.Entry<String, List<Map<String,String>>> lista: dto.getRespuesta().getDatos().entrySet()) {
                List<Map<String, String>> elemento = new ArrayList<>();
                lista_actual++;
                String operacion = lista.getKey();
                int total_casos = dto.getRespuesta().getFilasListado(operacion);
                if (total_casos > 0) {
                    for (Map<String, String> caso: lista.getValue()) {
                        Map<String, String> temporal = entidad.TransformarDatos(caso, "V", filtro, false);
                        if (entidad.getEstado()) {
                            elemento.add(temporal);
                            if (lista_actual == 1 && total_casos == 1 && temporal.size()>1) {
                                for (Map.Entry<String, String> atributo: temporal.entrySet()) {
                                    dto.setElemento(atributo.getKey(), atributo.getValue());
                                }
                            }
                        }
                    }
                    conjunto.put(operacion, elemento);
                } else {
                    //TODO: Revisar
                    if (lista_actual == 1) {entidad.setAtributos(null);}
                }
            }
            dto.setConjuntos(conjunto);
            if (filtro.equalsIgnoreCase("X") && dto.getRespuesta().getCuentaCasos()==1) {
                //TODO: Revisar
                entidad.Vaciar();
            }
        }
        dto.Limpiar();
        return resultado;
    }
    protected void entregarResultado(IDTO dto) {
        if (null == this.presentador) {
            TRAZADOR.info(dto.elementosToString());
            TRAZADOR.info(dto.conjuntosToString());
        } else {
            this.presentador.mostrarResultado(dto);
        }
    }

    //CLASE PARA EJECUCION EN HILO SECUNDARIO
    protected class GestorSolicitudes implements Runnable {
        IDTO dto;
        GestorSolicitudes(IDTO dto) {this.dto = dto;}
        @Override public void run() {
            TRAZADOR.info("(asincrono)");
            ejecutarCasoDeUso(this.dto);
        }
    }
}