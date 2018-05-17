package base;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 */

import desktop.usuario.Ventana;
import desktop.usuario.Contenedor;
import desktop.usuario.Contenedor.COMPONENTE;
import base.PresentadorUsuarios.ACCION;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class VentanaLogin extends Ventana {
    protected final static Logger TRAZADOR = Logger.getLogger(VentanaLogin.class.getName());
 
    JButton comp_enviar;
    JLabel comp_titulo;
    JTextField comp_login;
    JPasswordField comp_password;
    
    public VentanaLogin(Map<String, String> variables) {
        super(variables);
        this.contenedor = new Contenedor(this);
        this.contenedor.agregarComponente("direccion", null, COMPONENTE.Variable);
        this.contenedor.getComponentes().get("direccion").setValor(variables.get("direccion"));
        this.presentador = new PresentadorUsuarios(this);

        //Configurar el JFrame:
            //Título de la ventana
            setTitle("Autenticarse");
            //Color de fondo de la ventana
            getContentPane().setBackground(Color.decode("#FFFFCC"));
            //Dimensiones de la ventana
            setPreferredSize(new Dimension(250, 180));
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
            panel_izq.setBackground(Color.decode("#FFFFCC"));
            panel_der.setBackground(Color.decode("#FFFFCC"));
            panel_cen.setBackground(Color.decode("#FFFFCC"));
            panel_enc.setBackground(Color.decode("#FFFFCC"));
            panel_pie.setBackground(Color.decode("#FFFFCC"));
        //Agregar Paneles a Layout:
            add(panel_izq, BorderLayout.WEST);
            add(panel_der, BorderLayout.EAST);
            add(panel_cen, BorderLayout.CENTER);
            add(panel_enc, BorderLayout.NORTH);
            add(panel_pie, BorderLayout.SOUTH);
        //Crear Componentes:
            comp_titulo = new JLabel("Ingrese sus datos");
            comp_login = new JTextField();
            comp_password = new JPasswordField();
            comp_enviar = new JButton("Iniciar sesión");
            comp_login.setColumns(10);
            comp_password.setColumns(10);
        //Agregar Componentes a Paneles:
            panel_cen.add(comp_titulo);
            panel_cen.add(comp_login);
            panel_cen.add(comp_password);
            panel_cen.add(comp_enviar);
        //Empacar y centrar el JFrame:
            pack();
            setLocationRelativeTo(null);
            setBotonPredeterminado(comp_enviar);
        
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
            this.contenedor.agregarComponente("titulo", comp_titulo, COMPONENTE.Etiqueta);
            this.contenedor.agregarComponente("login", comp_login, COMPONENTE.TextoEditable);
            this.contenedor.agregarComponente("password", comp_password, COMPONENTE.TextoPassword);
            this.contenedor.agregarComponente("enviar", comp_enviar, COMPONENTE.Boton);
        //Pedir al Presentador la acción inicial:
            this.presentador.pedirAccion(ACCION.Autenticarse, "");
    }
    
    //LISTENERS PARA EVENTOS DE COMPONENTES
    //ActionListener
    @Override public void actionPerformed(ActionEvent ae) {
        //TRAZADOR.info(ae.getActionCommand());
        switch (ae.getActionCommand()) {
            case "enviar": 
            case "login": 
            case "password": 
                this.presentador.pedirAccion(ACCION.IniciarSesion, "");
                break;
        }
    }
    //FocusListener
    @Override public void focusGained(FocusEvent fe) {
        //TRAZADOR.info(fe.getComponent().getName());
        switch (fe.getComponent().getName()) {
            case "login":
                this.presentador.pedirAccion(ACCION.EnfocarLogin, "");
                break;
            case "password":
                this.presentador.pedirAccion(ACCION.LimpiarPassword, "");
                break;
        }
    }
    @Override public void focusLost(FocusEvent fe) {
        //TRAZADOR.info(fe.getComponent().getName());
        switch (fe.getComponent().getName()) {
            case "login":
                this.presentador.pedirAccion(ACCION.DesenfocarLogin, "");
                break;
        }
    }
}