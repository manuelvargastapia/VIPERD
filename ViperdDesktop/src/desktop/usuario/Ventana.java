package desktop.usuario;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import interno.nucleo.Interfaces.IVista;
import interno.nucleo.Interfaces.IPresentador;
import desktop.usuario.Interfaces.IComponente;
import desktop.usuario.Interfaces.IContenedor;
import desktop.usuario.Interfaces.IConjunto;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

//SUPERCLASE ABSTRACTA Ventana
public abstract class Ventana extends JFrame implements IVista, 
        ActionListener, FocusListener, KeyListener, MouseListener, ItemListener {
    protected final static Logger TRAZADOR = Logger.getLogger(Ventana.class.getName());
    
    protected IContenedor contenedor;
    protected IPresentador presentador;
    protected AbstractTableModel conjunto;
    protected Map<String, String> entorno;
    protected Map<String, String> variables;
    
    public Ventana(Map<String, String> variables) {
        TRAZADOR.info(this.getClass().getSimpleName());
        this.entorno = Sesion.Entorno();
        this.variables = variables;
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0),"clickButton");
    }

    //IMPLEMENTACION DE INTERFACE "IVista"
    @Override public Map<String, String> getValores() {
        Map<String, String> lista = new LinkedHashMap<>();
        if (null != this.contenedor) {
            for (Map.Entry<String, IComponente> contenido : this.contenedor.getComponentes().entrySet()) {
                IComponente componente = contenido.getValue();
                lista.put(contenido.getKey(), componente.getValor().trim());
            }
        }
        return lista;
    }
    @Override public String getValor(String nombre) {
        String valor = "";
        if (null != this.contenedor) {
            if (!this.contenedor.getComponentes().isEmpty()) {
                valor = this.contenedor.getComponente(nombre).getValor();
            }
        }
        TRAZADOR.info(nombre + " = " + valor);
        return valor;
    }
    @Override public void setValor(String nombre, String valor) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            if (null != contenedor) {
                if (contenedor.getComponentes().containsKey(nombre)) {
                    contenedor.getComponentes().get(nombre).setValor(valor);
                    TRAZADOR.info("setValor() " + nombre + " = " + valor);
                }
            }
        }});
    }
    
    @Override public void setEnfocado(String nombre) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            if (null != contenedor) {
                IComponente componente = contenedor.getComponentes().get(nombre);
                if (componente != null) {
                    TRAZADOR.info("setEnfocado() " + nombre);
                    componente.setEnfocado();
                }
            }
        }});
    }
    @Override public void setActivo(String nombre, Boolean opcion) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            if (null != contenedor) {
                IComponente componente = contenedor.getComponentes().get(nombre);
                if (componente != null) {
                    componente.setActivo(opcion);
                    TRAZADOR.info("setActivo() " + nombre + " = " + opcion.toString());
                }
            }
        }});
    }
    @Override public void setVisible(String nombre, Boolean opcion) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            if (null != contenedor) {
                IComponente componente = contenedor.getComponentes().get(nombre);
                if (componente != null) {
                    componente.setVisible(opcion);
                    TRAZADOR.info("setVisible() " + nombre + " = " + opcion.toString());
                }
            }
        }});
    }
    @Override public void actualizarContenedor(Map<String, String> elementos) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            TRAZADOR.info("actualizarContenedor()");
            if (null != contenedor) {
                for (Map.Entry<String, IComponente> contenido : contenedor.getComponentes().entrySet()) {
                    IComponente componente = contenido.getValue();
                    String elemento = elementos.get(contenido.getKey());
                    componente.setValor(elemento);
                    TRAZADOR.info(componente.getNombre() + " = " + elemento);
                }
            }
        }});
    }
    @Override public void actualizarConjunto(List<Map<String, String>> datos) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            TRAZADOR.info("actualizarConjunto()");
            if (null != datos && null != conjunto) {
                ((IConjunto)conjunto).setConjunto(datos);
            }
        }});
    }
    @Override public void mostrarMensajes(Map<String, String> mensajes) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            TRAZADOR.info("mostrarMensajes() " + String.valueOf(mensajes.size()));
            int i = 0;
            String texto;
            String codigo = "";
            String opcion = "";
            StringBuilder str = new StringBuilder("");
            for (Map.Entry<String, String> mensaje: mensajes.entrySet()) {
                codigo = mensaje.getKey();
                texto = mensaje.getValue();
                if (texto.isEmpty()) {texto = codigo;}
                if (i > 0) {str.append("\n");}
                str.append(texto);
                i++;
            }
            if (codigo.startsWith("ERROR")) {opcion = "ERROR";}
            if (codigo.startsWith("EXITO")) {opcion = "EXITO";}
            if (codigo.startsWith("ALERTA")) {opcion = "ALERTA";}
            if (codigo.startsWith("INFO")) {opcion = "INFO";}
            if (str.toString().length()>0) {
                mostrarMensaje(str.toString(), opcion);
            }
        }});
    }
    @Override public void mostrarMensaje(String texto, String opcion) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            TRAZADOR.info("mostrarMensaje() opcion: " + opcion + ", mensaje: " + texto);
            switch (opcion) {
                case "ERROR": 
                    JOptionPane.showMessageDialog(null, texto, opcion, JOptionPane.ERROR_MESSAGE);
                    break;
                case "ALERTA": 
                    JOptionPane.showMessageDialog(null, texto, opcion, JOptionPane.WARNING_MESSAGE);
                    break;
                case "INFO":
                    JOptionPane.showMessageDialog(null, texto, opcion, JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }});
    }
    @Override public void mostrarVista(Boolean opcion) {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            TRAZADOR.info("mostrarVista() " + opcion.toString());
            if (opcion) {setVisible(true);} else {setVisible(false);}
        }});
    }
    @Override public void Cerrar() {
        SwingUtilities.invokeLater(new Runnable() {public void run() {
            TRAZADOR.info("Cerrar()");
            dispose();
        }});
    }
    @Override public Map<String, String> Entorno() {
        return this.entorno;
    }
    @Override public void setEntorno(String nombre, String valor) {
        TRAZADOR.info(nombre + " = " + valor);
        Sesion.setEntorno(nombre, valor);
    }
    
    //FUNCIONES QUE DEBEN SOBREESCRIBIRSE EN LA CLASE HIJA CUANDO SE USEN
    @Override public void Redirigir(String destino) {}
    protected void cargarComponentes() {}
    
    //EJECUTABLE PARA LA CARGA DE COMPONENTES
    public class GestorComponentes implements Runnable {
        @Override public void run() {
            TRAZADOR.info("(asincrono)");
            cargarComponentes();
        }
    }

    //FUNCIONES INTERNAS
    protected void setBotonPredeterminado(JButton boton) {
        getRootPane().getActionMap().put("clickButton", new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                boton.doClick();
            }
        });
    }
    
    //LISTENERS QUE DEBEN SOBREESCRIBIRSE EN LA CLASE HIJA CUANDO SE USEN
    //ActionListener
    @Override public void actionPerformed(ActionEvent ae) {}
    //FocusListener
    @Override public void focusGained(FocusEvent fe) {}
    @Override public void focusLost(FocusEvent fe) {}
    //KeyListener
    @Override public void keyTyped(KeyEvent ke) {}
    @Override public void keyPressed(KeyEvent ke) {}
    @Override public void keyReleased(KeyEvent ke) {}
    //MouseListener
    @Override public void mouseClicked(MouseEvent me) {}
    @Override public void mousePressed(MouseEvent me) {}
    @Override public void mouseReleased(MouseEvent me) {}
    @Override public void mouseEntered(MouseEvent me) {}
    @Override public void mouseExited(MouseEvent me) {}
    //itemStateChanged
    @Override public void itemStateChanged(ItemEvent ie) {}
}