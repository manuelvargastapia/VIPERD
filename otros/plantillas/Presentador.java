<#if package?? && package != "">package ${package};</#if>
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import interno.nucleo.Interfaces.IVista;
import interno.nucleo.Interfaces.IDTO;
import interno.nucleo.Interfaces.IInteractor;
import interno.nucleo.Interfaces.IAccion;
import interno.usuario.Presentador;

/* Agregue la importación del enum de casos de uso del Interactor que usará
   este Presentador (Reemplace "InteractorX" por el nombre de la clase). */
//import ${package}.InteractorX.CASODEUSO;

import java.util.Map;
import java.util.logging.Logger;

//CLASE ${name} - Autor: ${user}
public class ${name} extends Presentador {
    protected final static Logger TRAZADOR = Logger.getLogger(${name}.class.getName());

    public ${name}(IVista vista) {
        super(vista);
    }
    public ${name}(IVista vista, Map<String, String> entorno) {
        super(vista, entorno);
    }
    
    //ENUMERADOR DE LAS ACCIONES DE ESTE PRESENTADOR
    public enum ACCION implements IAccion {
        Nada(""),
        ;
        ACCION(String req) {this.requisitos = req;}
        private final String requisitos;
        public String Requisitos() {return this.requisitos;}
    }

    //IMPLEMENTACION DE INTERFACE "IPresentador"
    @Override public void pedirAccion(Enum accion, String caso) {
        TRAZADOR.info(accion.toString());
        if (!autorizarAcceso(accion, this.entorno.get("usuario"))) {
            vista.mostrarMensaje("Acceso No autorizado", "ERROR");
            vista.Cerrar();
        } else {

            /* Indique cuál Interactor usará este Presentador, según la
               clase que contiene el enum de casos de uso. */

            //IInteractor interactor = new InteractorX(this);
            IDTO dto = interactor.crearDTO(accion);

            //SELECTOR DE ACCIONES PARA SOLICITAR CASOS DE USO
            switch ((ACCION) accion) {
                case Nada:
                    //dto.setCasoDeUso(CASODEUSO.NINGUNO);
                    break;

                /* Agregue aquí otros "case" con las demás acciones definidas en 
                   el Enum "ACCION", y asóciele a cada una el caso de uso que le
                   corresponda en el Interactor que se esté usando. */

            }
            if (comprobarSolicitud(accion, dto.getCasoDeUso(), true)) {

                /* Indique cuál Agente se usará para enlazar con los DAO. */
                //dto.setAgente(new AgenteX());
                interactor.solicitarCasoDeUso(dto);
            } else {
                mostrarResultado(dto);
            }
        }
    }
    @Override public void mostrarResultado(IDTO dto) {
        TRAZADOR.info("");
        if (comprobarSolicitud(dto.getAccion(), dto.getCasoDeUso(), false)) {
            switch ((ACCION) dto.getAccion()) {
                case Nada:
                    vista.mostrarVista(true);
                    break;

                /* Agregue aquí los "case" con las demás acciones definidas en 
                   el Enum "ACCION", y programe para cada una las reacciones en 
                   la vista, usando los métodos de la interface "IVista". */
            }
        }
    }
}