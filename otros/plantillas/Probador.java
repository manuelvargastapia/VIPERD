<#if package?? && package != "">package ${package};</#if>
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import interno.nucleo.Interfaces.IInteractor;
import interno.nucleo.Interfaces.IDTO;
import desktop.usuario.Sesion;
//import ${package}.InteractorX.CASODEUSO;

import java.util.logging.Logger;

//CLASE ${name} - Autor: ${user}
public class ${name} {
    protected final static Logger TRAZADOR = Logger.getLogger(${name}.class.getName());

    public static void main(String[] args) {
        Sesion.Abrir();
        
        /* 
        interactor = new InteractorX(null, Sesion.Entorno());
        IDTO dto = interactor.crearDTO(null);
        dto.setAgente(new AgenteX());
        dto.setAsincrono(true);
        dto.setCasoDeUso(CASODEUSO.NINGUNO);
        interactor.solicitarCasoDeUso(dto);
        */

    }

    static IInteractor interactor;
}