package desktop.usuario;
/* MAESTRO VIPERD (App.Desktop)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. */

import desktop.usuario.Interfaces.IContenedor;
import desktop.usuario.Interfaces.IComponente;

import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

//CLASE Contenedor
public class Contenedor implements IContenedor {
    protected final static Logger TRAZADOR = Logger.getLogger(Contenedor.class.getName());
    
    protected Map<String, IComponente> componentes;
    protected Object oyente;

    public Contenedor(Object oyente) {
        TRAZADOR.info("");
        this.componentes = new LinkedHashMap<>();
        this.oyente = oyente;
    }
    
    //IMPLEMENTACION DE INTERFACE "IContenedor"
    @Override public IComponente agregarComponente(String nombre, Object comp, Enum tipo) {
        TRAZADOR.info(nombre);
        IComponente componente = null;
        switch ((COMPONENTE)tipo) {
            case TextoEditable: componente = new CompTextoEditable(nombre, comp); break;
            case TextoPassword: componente = new CompTextoPassword(nombre, comp); break;
            case Etiqueta: componente = new CompEtiqueta(nombre, comp); break;
            case Boton: componente = new CompBoton(nombre, comp); break;
            case AreaDesplazable: componente = new CompAreaDesplazable(nombre, comp); break;
            case Variable: componente = new CompVariable(nombre); break;
        }
        if (componente != null) {
            this.componentes.put(nombre, componente);
        }
        return componente;
    }
    @Override public IComponente getComponente(String nombre) {
        return this.componentes.get(nombre);
    }
    @Override public Map<String, IComponente> getComponentes() {
        return this.componentes;
    }

    //ENUMERADOR DE COMPONENTES PUBLICADOS PARA USAR EN LA VISTA
    public enum COMPONENTE {Variable, TextoEditable, TextoPassword, Etiqueta, Boton, AreaDesplazable}

    //IMPLEMENTACION DE LAS CLASES DE LOS COMPONENTES
    private class CompTextoEditable implements IComponente {
        JTextField comp;
        CompTextoEditable(String nombre, Object componente) {
            comp = (JTextField)componente;
            comp.addActionListener((ActionListener)oyente);
            comp.addFocusListener((FocusListener)oyente);
            comp.addKeyListener((KeyListener)oyente);
            comp.setActionCommand(nombre);
            comp.setName(nombre);
        }
        @Override public String getNombre() {return comp.getName();}
        @Override public void setValor(String valor) {comp.setText(valor);}
        @Override public String getValor() {return comp.getText();}
        @Override public void setVisible(Boolean valor) {comp.setVisible(valor);}
        @Override public void setActivo(Boolean valor) {comp.setEnabled(valor);}
        @Override public void setEnfocado() {comp.requestFocus();}
    }
    private class CompTextoPassword implements IComponente {
        JPasswordField comp;
        CompTextoPassword(String nombre, Object componente) {
            comp = (JPasswordField)componente;
            comp.addActionListener((ActionListener)oyente);
            comp.addFocusListener((FocusListener)oyente);
            comp.addKeyListener((KeyListener)oyente);
            comp.setActionCommand(nombre);
            comp.setName(nombre);
        }
        @Override public String getNombre() {return comp.getName();}
        @Override public void setValor(String valor) {comp.setText(valor);}
        @Override public String getValor() {return String.valueOf(comp.getPassword());}
        @Override public void setVisible(Boolean valor) {comp.setVisible(valor);}
        @Override public void setActivo(Boolean valor) {comp.setEnabled(valor);}
        @Override public void setEnfocado() {comp.requestFocus();}
    }
    private class CompEtiqueta implements IComponente {
        JLabel comp;
        CompEtiqueta(String nombre, Object componente) {
            comp = (JLabel)componente;
            comp.setName(nombre);
        }
        @Override public String getNombre() {return comp.getName();}
        @Override public void setValor(String valor) {}
        @Override public String getValor() {return comp.getText();}
        @Override public void setVisible(Boolean valor) {}
        @Override public void setActivo(Boolean valor) {comp.setEnabled(valor);}
        @Override public void setEnfocado() {}
    }
    private class CompBoton implements IComponente {
        JButton comp;
        CompBoton(String nombre, Object componente) {
            comp = (JButton)componente;
            comp.addActionListener((ActionListener)oyente);
            comp.addFocusListener((FocusListener)oyente);
            comp.addKeyListener((KeyListener)oyente);
            comp.setActionCommand(nombre);
            comp.setName(nombre);
        }
        @Override public String getNombre() {return comp.getName();}
        @Override public void setValor(String valor) {comp.setText(valor);}
        @Override public String getValor() {return comp.getText();}
        @Override public void setVisible(Boolean valor) {comp.setVisible(valor);}
        @Override public void setActivo(Boolean valor) {comp.setEnabled(valor);}
        @Override public void setEnfocado() {comp.requestFocus();}
    }
    private class CompAreaDesplazable implements IComponente {
        JScrollPane comp;
        CompAreaDesplazable(String nombre, Object componente) {
            comp = (JScrollPane)componente;
            comp.setName(nombre);
        }
        @Override public String getNombre() {return comp.getName();}
        @Override public void setValor(String valor) {}
        @Override public String getValor() {return "";}
        @Override public void setVisible(Boolean valor) {comp.setVisible(valor);}
        @Override public void setActivo(Boolean valor) {comp.setEnabled(valor);}
        @Override public void setEnfocado() {comp.requestFocus();}
    }
    private class CompVariable implements IComponente {
        String nombre;
        String valor;
        CompVariable(String nombre) {
            this.nombre = nombre;
        }
        @Override public String getNombre() {return this.nombre;}
        @Override public void setValor(String valor) {this.valor = valor;}
        @Override public String getValor() {return this.valor;}
        @Override public void setVisible(Boolean valor) {}
        @Override public void setActivo(Boolean valor) {}
        @Override public void setEnfocado() {}
    }
    
    /* TODO: Crear aqui las clases para los nuevos Componentes.

    Procedimiento para crear un nuevo componente:
    1. Agregar el nombre del nuevo componente al Enum COMPONENTE.
    2. Crear una clase privada para declarar el nuevo componente, que implemente
       la interfaz "IComponente" y "envuelva" al JComponente real.
    3. Agregar el nuevo componente en el switch del método "agregarComponente", 
       vinculando el nombre que tiene en el Enum con la creación de la instancia
       de la clase interna que lo implementa.
    */
    
}