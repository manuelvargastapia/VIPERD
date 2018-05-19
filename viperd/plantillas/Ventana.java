<#if package?? && package != "">package ${package};</#if>
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import desktop.usuario.Ventana;
import desktop.usuario.Contenedor;
import desktop.usuario.Contenedor.COMPONENTE;

/* Indique la clase del Presentador que contiene el enum de acciones que usará
   esta Ventana. Reemplace "PresentadorX" por el nombre real de la clase. */
//import ${package}.PresentadorX.ACCION;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.Map;
import java.util.logging.Logger;

//CLASE ${name} - Autor: ${user}
public class ${name} extends Ventana {
    protected final static Logger TRAZADOR = Logger.getLogger(${name}.class.getName());
    
    public ${name}(Map<String, String> variables) {
        super(variables);
        this.contenedor = new Contenedor(this);
        this.contenedor.agregarComponente("direccion", null, COMPONENTE.Variable);
        this.setValor("direccion", variables.get("direccion"));

        //Crear Presentador:
            /* Debe crear una instancia de la clase de Presentador que usará. */
            //this.presentador = new PresentadorX(this);

        //Configurar el JFrame:
            setTitle("");
            setPreferredSize(new Dimension(250, 180));
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pack();
            setLocationRelativeTo(null);
        
        SwingUtilities.invokeLater(new GestorComponentes());
    }
    @Override public void Redirigir(String destino) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            TRAZADOR.info("Redirigir() " + destino);
            dispose();
            String[] args = new String[2];
            args[0] = destino;
            args[1] = variables.get("direccion");
            Inicio.main(args);
        }});
    }
    @Override protected void cargarComponentes() {
        TRAZADOR.info("");
        //Agregar Componentes a Contenedor:
            //this.contenedor.agregarComponente("boton", miboton, COMPONENTE.Boton);

            /* Quitar el "listado" si no se usará en esta ventana */
            //this.contenedor.agregarComponente("listado", milistado, COMPONENTE.AreaDesplazable);

            /* Todos los nuevos componentes que hayan sido creados y asignados a 
               un panel, deben también ser registrados en el contenedor de esta
               ventana. Agregue aquí todos los nuevos componentes al contenedor, 
               indicando el "nombre" que los identifica y el tipo de COMPONENTE
               al que corresponden, al igual que los anteriores. */

        /* Pedir al Presentador la acción inicial: */
            //this.presentador.pedirAccion(ACCION.Nada, "");

    }
    
    //LISTENERS PARA EVENTOS DE COMPONENTES
    //ActionListener
    @Override public void actionPerformed(ActionEvent ae) {
        //TRAZADOR.info(ae.getActionCommand());
        switch (ae.getActionCommand()) {
            case "boton": 
                //this.presentador.pedirAccion(ACCION.Nada, "");
                break;

            /* Agregue aquí otros "case" para asignar Acciones a los componentes
               cuando ejecutan un comando (cuando se hace clic en un botón o se
               presiona la tecla "enter" en un campo de texto). Es necesario 
               identificar los componentes por su "nombre" y asociarlos a una
               petición al presentador, igual como se muestra con el "boton". */
        }
    }
    //FocusListener
    @Override public void focusGained(FocusEvent fe) {
        //TRAZADOR.info(fe.getComponent().getName());
        switch (fe.getComponent().getName()) {
            case "boton":
                //this.presentador.pedirAccion(ACCION.Nada, "");
                break;

            /* Agregue aquí otros "case" para asignar Acciones a los componentes
               que se gatillarán cuando éstos RECIBAN el foco. */
        }
    }
    @Override public void focusLost(FocusEvent fe) {
        //TRAZADOR.info(fe.getComponent().getName());
        switch (fe.getComponent().getName()) {
            case "boton":
                //this.presentador.pedirAccion(ACCION.Nada, "");
                break;

            /* Agregue aquí otros "case" para asignar Acciones a los componentes
               que se gatillarán cuando éstos PIERDAN el foco. */
        }
    }
}