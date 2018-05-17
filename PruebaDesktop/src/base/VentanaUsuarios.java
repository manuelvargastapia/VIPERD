package base;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import desktop.usuario.Ventana;
import desktop.usuario.Conjunto;
import desktop.usuario.Contenedor;
import desktop.usuario.Contenedor.COMPONENTE;
import base.PresentadorUsuarios.ACCION;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class VentanaUsuarios extends Ventana {
    protected final static Logger TRAZADOR = Logger.getLogger(VentanaUsuarios.class.getName());

    JTable tabla;
    JScrollPane area;

    public VentanaUsuarios(Map<String, String> variables) {
        super(variables);
        this.contenedor = new Contenedor(this);
        this.contenedor.agregarComponente("direccion", null, COMPONENTE.Variable);
        this.contenedor.getComponentes().get("direccion").setValor(variables.get("direccion"));
        this.conjunto = new Conjunto(this);
        this.presentador = new PresentadorUsuarios(this);

        //Configurar el JFrame:
            //Título de la ventana
            setTitle("Usuarios");
            //Color de fondo de la ventana
            getContentPane().setBackground(Color.decode("#FFFFFF"));
            //Dimensiones de la ventana
            setPreferredSize(new Dimension(640, 480));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Crear Layout:
            BorderLayout layout = new BorderLayout();
            layout.setHgap(5);
            layout.setVgap(5);
            setLayout(layout);
        //Crear Paneles:
            JPanel panel_izq, panel_der, panel_cen, panel_enc, panel_pie;
            panel_izq = new JPanel(new FlowLayout());
            panel_der = new JPanel(new FlowLayout());
            panel_cen = new JPanel(new FlowLayout());
            panel_enc = new JPanel(new FlowLayout());
            panel_pie = new JPanel(new FlowLayout());
            panel_izq.setBackground(Color.decode("#FFFFFF"));
            panel_der.setBackground(Color.decode("#FFFFFF"));
            panel_cen.setBackground(Color.decode("#FFFFCC"));
            panel_enc.setBackground(Color.decode("#FFFFFF"));
            panel_pie.setBackground(Color.decode("#FFFFFF"));
        //Agregar Paneles a Layout:
            add(panel_izq, BorderLayout.WEST);
            add(panel_der, BorderLayout.EAST);
            add(panel_cen, BorderLayout.CENTER);
            add(panel_enc, BorderLayout.NORTH);
            add(panel_pie, BorderLayout.SOUTH);
        //Crear Tabla y Area desplazable (quitar si no se usarán):
            tabla = new JTable(this.conjunto);
            tabla.setPreferredScrollableViewportSize(new Dimension(540, 380));
            area = new JScrollPane();
            area.setViewportView(tabla);
            area.setColumnHeaderView (tabla.getTableHeader());
        //Agregar Componentes a Paneles:
            panel_cen.add(area); //Quitar si no se usará
        //Empacar, centrar y hacer visible el JFrame:
            pack();
            setLocationRelativeTo(null);
        
        SwingUtilities.invokeLater(new GestorComponentes());
    }
    @Override public void Redirigir(String destino) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            TRAZADOR.info("Redirigir() " + destino);
            dispose();
            String[] args = new String[1];
            args[0] = destino;
            args[1] = variables.get("direccion");
            Inicio.main(args);
        }});
    }
    @Override protected void cargarComponentes() {
        TRAZADOR.info("");
        //Agregar Componentes a Contenedor:
            this.contenedor.agregarComponente("conjunto", area, COMPONENTE.AreaDesplazable);
        //Pedir al Presentador la acción inicial:
            this.presentador.pedirAccion(ACCION.ListaUsuarios, "");
    }
    
    //LISTENERS PARA EVENTOS DE COMPONENTES
    //ActionListener
    @Override public void actionPerformed(ActionEvent ae) {
        //TRAZADOR.info(ae.getActionCommand());
        switch (ae.getActionCommand()) {
        }
    }    
}