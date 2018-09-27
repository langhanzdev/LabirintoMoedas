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
    private int posicao;
    private int x;
    private int y;
    private boolean isAdd;
    private boolean isVisitado;
    private int moedas;

    public Elemento(TipoElemento tipo, int posicao, int x, int y) {
        this.tipo = tipo;
//        this.id = id;
        this.x = x;
        this.y = y;
        this.isAdd = false;
        this.isVisitado = false;
        this.moedas = 0;
        this.posicao = posicao;
    }

    public boolean isIsVisitado() {
        return isVisitado;
    }

    public void setIsVisitado(boolean isVisitado) {
        this.isVisitado = isVisitado;
    }

    public int getMoedas() {
        return moedas;
    }

    public void setMoedas(int moedas) {
        this.moedas = moedas;
    }
    
    

    public boolean isIsAdd() {
        return isAdd;
    }

    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public TipoElemento getTipo() {
        return tipo;
    }

    public void setTipo(TipoElemento tipo) {
        this.tipo = tipo;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
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
