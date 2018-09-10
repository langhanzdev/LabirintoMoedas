package labirintomoedas;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author langhanz
 */
public class Agente {
    
    private int x;
    private int y;
    private ArrayList<Elemento> listaElementos;

    public Agente(int x, int y) {
        this.x = x;
        this.y = y;
        this.listaElementos = listaElementos;
    }

    public Agente() {
    }
    
    

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ArrayList<Elemento> getListaElementos() {
        return listaElementos;
    }

    public void setListaElementos(ArrayList<Elemento> listaElementos) {
        this.listaElementos = listaElementos;
    }

    @Override
    public String toString() {
        return "Agente{" + "x=" + x + ", y=" + y + ", listaElementos=" + listaElementos + '}';
    }
    
    
}
