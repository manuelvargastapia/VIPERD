package base;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import desktop.usuario.Ruteador;

import java.util.Map;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class Inicio extends Ruteador {
    protected final static Logger TRAZADOR = Logger.getLogger(Inicio.class.getName());

    //IMPLEMENTACION DEL LANZADOR
    public synchronized static void main(String[] args) {
        SOLICITUD_PREDETERMINADA = "ListaUsuarios";
        SOLICITUD_AUTENTICARSE = "Autenticarse";
        SOLICITUD_INICIARSESION = "IniciarSesion";
        SOLICITUD_CERRARSESION = "CerrarSesion";
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %2$s %5$s%6$s%n");
        Map<String, String> var = procesarSolicitud(args);
        var.put("aplicacion", Inicio.class.getPackage().getName());
        Despachar(var);
    }

    //IMPLEMENTACION DE INTERFACE "IRuteador"
    public synchronized static void Despachar(Map<String, String> var) {
        TRAZADOR.info(var.get("solicitud"));
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                switch (var.get("solicitud")) {
                    case "Autenticarse":
                        new VentanaLogin(var);
                        break;
                    case "ListaUsuarios":
                        new VentanaUsuarios(var);
                        break;
                }
            }
        });
    }
}