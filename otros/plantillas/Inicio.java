<#if package?? && package != "">package ${package};</#if>
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import desktop.usuario.Ruteador;

import java.util.Map;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

//CLASE ${name} - Autor: ${user}
public class ${name} extends Ruteador {
    protected final static Logger TRAZADOR = Logger.getLogger(${name}.class.getName());

    //IMPLEMENTACION DEL LANZADOR
    public synchronized static void main(String[] args) {
        SOLICITUD_PREDETERMINADA = "Inicial";
        SOLICITUD_AUTENTICARSE = "Autenticarse";
        SOLICITUD_INICIARSESION = "IniciarSesion";
        SOLICITUD_CERRARSESION = "CerrarSesion";
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %2$s %5$s%6$s%n");
        Map<String, String> var = procesarSolicitud(args);
        var.put("aplicacion", ${name}.class.getPackage().getName());
        Despachar(var);
    }

    //IMPLEMENTACION DE INTERFACE "IRuteador"
    public synchronized static void Despachar(Map<String, String> var) {
        TRAZADOR.info(var.get("solicitud"));
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                switch (var.get("solicitud")) {
                    case "Inicial":
	                /* Indicar la Ventana que se abrirá al lanzar la aplicación */
                        //new VentanaX(var);
                        break;
                }
            }
        });
    }
}