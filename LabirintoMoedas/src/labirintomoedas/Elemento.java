/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labirintomoedas;

/**
 *
 * @author langhanz
 */
public class Elemento {
    
    private TipoElemento tipo;
    private int id;
    private int x;
    private int y;

    public Elemento(TipoElemento tipo, int id, int x, int y) {
        this.tipo = tipo;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public TipoElemento getTipo() {
        return tipo;
    }

    public void setTipo(TipoElemento tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Elemento{" + "tipo=" + tipo + ", id=" + id + ", x=" + x + ", y=" + y + '}';
    }
    
    
    
}
