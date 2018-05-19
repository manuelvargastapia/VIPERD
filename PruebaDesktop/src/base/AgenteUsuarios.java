package base;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import interno.nucleo.Interfaces.IOperacion;
import interno.nucleo.Interfaces.IDTO;
import interno.datos.Agente;
import desktop.datos.*;

import java.util.logging.Logger;

public class AgenteUsuarios extends Agente {
    protected final static Logger TRAZADOR = Logger.getLogger(AgenteUsuarios.class.getName());
    
    public AgenteUsuarios() {
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
                this.dao = DaoHTTPJson.Activar("http://administralo.cl/cursoptealto/", "", "");
                break;
            case "MySql":
                this.dao = DaoMySql.Activar("localhost:3307/bd_app", "usuario", "12345");
                break;
            case "SQLite":
                this.dao = DaoSQLite.Activar("bd//bd.sqlite", "", "");
                break;
        }
        Configurar(dto);
    }

    //DEFINICION DE OPERACIONES
    protected enum OPERACION implements IOperacion {
        //ObtenerSesion("HTTPJson", "app.php?op=autenticar&us=[login]&pw=[password]", "caso"),
        ObtenerSesion("SQLite", "SELECT id, login AS usuario, alias, roles FROM app_usuarios WHERE login='[login]' AND clave='[password]' AND estado='1'", ""),
        SeleccionarUsuario("SQLite", "SELECT", "app_usuarios"),
        AgregarUsuario("SQLite", "INSERT", "app_usuarios"),
        EditarUsuario("SQLite", "UPDATE", "app_usuarios"),
        BorrarUsuario("SQLite", "DELETE", "app_usuarios"),
        ListaUsuarios("SQLite", "LISTADO", "app_usuarios"),
        ;
        private final String comando, origen, fuente;
        OPERACION(String origen, String comando, String fuente) {this.comando = comando; this.origen = origen; this.fuente = fuente;}
        @Override public String Origen() {return this.origen;}
        @Override public String Comando() {return this.comando;}
        @Override public String Fuente() {return this.fuente;}
    }
}