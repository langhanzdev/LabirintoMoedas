package labirintomoedas;

import java.util.ArrayList;
import java.util.Random;
import static labirintomoedas.LabirintoMoedas.listaElementos;

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
    private int direcao;
    private ArrayList<Elemento> listaElementos;

    public Agente(int x, int y) {
        this.x = x;
        this.y = y;
        this.listaElementos = new ArrayList<>();
        this.direcao = 0;
    }

    public Agente() {
        this.listaElementos = new ArrayList<>();
        this.direcao = 0;
    }
    
    public void anda(){
        detetor();
        System.out.println("Tamanho lista: "+this.listaElementos.size());
        
        switch(getDirecao()){
                case 0:
                    if(isLivre(getX(), getY()+1)){
                        
                        
                        setY(getY()+1);
                    }else{
                        setDirecao(novaDirecao());
                    }
                    break;
                case 1:
                    if(isLivre(getX()+1, getY())){
                        
                       
                        setX(getX()+1);
                    }else{
                        setDirecao(novaDirecao());
                    }
                    break;
                case 2:
                    if(isLivre(getX(), getY()-1)){
                        
                        
                        setY(getY()-1);
                    }else{
                        setDirecao(novaDirecao());
                    }
                    break;
                case 3:
                    if(isLivre(getX()-1, getY())){
                        
                        
                        setX(getX()-1);
                    }else{
                        setDirecao(novaDirecao());
                    }
                    break;
            }
    }
    
    public void detetor(){
        Elemento e;
        e = temElemento(getX(), getY()+1);
        addLista(e);
        e = temElemento(getX(), getY()+2);
        addLista(e);
        e = temElemento(getX(), getY()-1);
        addLista(e);
        e = temElemento(getX(), getY()-1);
        addLista(e);
        e = temElemento(getX()+1, getY());
        addLista(e);
        e = temElemento(getX()+2, getY());
        addLista(e);
        e = temElemento(getX()-1, getY());
        addLista(e);
        e = temElemento(getX()-2, getY());
        addLista(e);
        
    }
    
    public void addLista(Elemento e){
        if(e != null && e.getTipo() != TipoElemento.Parede && !e.isIsAdd()){
            e.setIsAdd(true);
            listaElementos.add(e);
        }
    }
    
    
    /**
     * Retorna o elemento que está na posicao passada por parametro
     * @param x
     * @param y
     * @return Elemento da posicao x,y
     */
    public Elemento temElemento(int x, int y){
        Elemento aux;
        for(int i=0;i<LabirintoMoedas.listaElementos.size();i++){
            aux  = LabirintoMoedas.listaElementos.get(i);
            if(aux.getX() == x && aux.getY() == y){
                return aux;
            }
        }
        return null;
    }
    
    /**
     * Sorteia uma nova direção aleatoria para o agente
     * @return 
     */
    public int novaDirecao(){
        Random ran = new Random();
        return ran.nextInt(4);
    }
    
    /**
     * Retorna se a posicao passada está livre para o agente
     * seguindo as regras do agente. Ele pode andar sobre baus e sacos de moedas
     * @param x
     * @param y
     * @return 
     */
    public static boolean isLivre(int x, int y){
        Elemento e;
        for(int i=0;i < LabirintoMoedas.listaElementos.size();i++){
            e  = LabirintoMoedas.listaElementos.get(i);
            if(e.getX() == x && e.getY() == y){
                if(e.getTipo() == TipoElemento.Bau || e.getTipo() == TipoElemento.Saco){
                    return true;
                }else{
                    return false;
                }
            }
        }
        if(x < 0 || y < 0 || x >= LabirintoMoedas.n || y >= LabirintoMoedas.n)
            return false;
        return true;
    }
    
    
    //--------------------------------------------------------

    public int getDirecao() {
        return direcao;
    }

    public void setDirecao(int direcao) {
        this.direcao = direcao;
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
