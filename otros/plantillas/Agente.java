<#if package?? && package != "">package ${package};</#if>
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import interno.nucleo.Interfaces.IOperacion;
import interno.nucleo.Interfaces.IDTO;
import interno.datos.Agente;
import desktop.datos.*;

import java.util.logging.Logger;

//CLASE ${name} - Autor: ${user}
public class ${name} extends Agente {
    protected final static Logger TRAZADOR = Logger.getLogger(${name}.class.getName());
    
    public ${name}() {
        super();
    }

    //CONFIGURACION DEL AGENTE
    @Override public void Conectar(IDTO dto) {
        super.Conectar(dto);
        try {
            this.origen = OPERACION.valueOf(this.operacion).Origen();
            this.comando = OPERACION.valueOf(this.operacion).Comando();
            this.fuente = OPERACION.valueOf(this.operacion).Fuente();
        } catch (Exception e) {TRAZADOR.info(e.getMessage());}
        switch (this.origen) {
            case "HTTPJson":
                this.dao = DaoHTTPJson.Activar("http://localhost/aplicacion/", "", "");
                break;
            case "MySql":
                this.dao = DaoMySql.Activar("jdbc:mysql://localhost/bd_app", "usuario", "12345");
                break;
        }
        Configurar(dto);
    }

    //DEFINICION DE OPERACIONES
    protected enum OPERACION implements IOperacion {
        Seleccionar${name}("MySql", "SELECT", "tabla_bd"),
        Editar${name}("MySql", "UPDATE", "tabla_bd"),
        Borrar${name}("MySql", "DELETE", "tabla_bd"),
        Agregar${name}("MySql", "INSERT", "tabla_bd"),
        Lista${name}("MySql", "LISTADO", "tabla_bd"),
        ;
        private final String comando, origen, fuente;
        OPERACION(String origen, String comando, String fuente) {this.comando = comando; this.origen = origen; this.fuente = fuente;}
        @Override public String Origen() {return this.origen;}
        @Override public String Comando() {return this.comando;}
        @Override public String Fuente() {return this.fuente;}
    }
}