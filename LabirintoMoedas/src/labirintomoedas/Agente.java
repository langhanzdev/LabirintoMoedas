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
    
     private class Nodo implements Comparable<Nodo>{ 
        double stepsCost = 0;
        double heuristicCost = 0; //Heuristic cost
        double finalCost = 0; //G+H
        int x, y;
        Nodo parent; 
        
        Nodo(int x, int y){
            this.x = x;
            this.y = y; 
        }
        
        public int compareTo(Nodo n){
            if(this.finalCost < n.finalCost)
                return -1;
            else if(this.finalCost > n.finalCost)
                return 1;
            return 0;
        }
        @Override
        public String toString(){
            return "["+this.x+", "+this.y+"]";
        }
}

    
    public Nodo aStar(Ponto inicio, Ponto destino){
        Nodo atual = null;
        Nodo comp = null;
        ArrayList<Nodo> aberto =  new ArrayList<>();
        ArrayList<Nodo> fechado = new ArrayList<>();
        Nodo nodo = new Nodo(inicio.x, inicio.y);
        nodo.finalCost = 0;
        nodo.heuristicCost = 0;
        aberto.add(nodo);
        while(!aberto.isEmpty()){
            double max_final_cost = Double.MAX_VALUE;
            for(Nodo n:aberto){
                if(n.finalCost < max_final_cost){
                    atual = n;
                    max_final_cost = n.finalCost;
                }
            }
            aberto.remove(atual);                                       
            fechado.add(atual);
            ArrayList<Nodo> sucessor = new ArrayList<>();
            Elemento elemento;
            if(atual.x != 0){
                if(atual.y > 0){
                    elemento = LabirintoMoedas.temElemento(atual.x-1, atual.y-1);
                    if((elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Buraco && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x-1, atual.y-1)))
                        sucessor.add(new Nodo(atual.x-1, atual.y-1));
                }
                elemento = LabirintoMoedas.temElemento(atual.x - 1, atual.y);
                if((elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Buraco && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x-1, atual.y)))
                    sucessor.add(new Nodo(atual.x-1, atual.y));
                if(atual.y < LabirintoMoedas.getSize() -1){
                    elemento = LabirintoMoedas.temElemento(atual.x - 1, atual.y+1);
                    if((elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Buraco && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x-1, atual.y+1)))
                        sucessor.add(new Nodo(atual.x-1, atual.y+1));
                }
            }
            if(atual.x < LabirintoMoedas.getSize() -1){
               if(atual.y > 0){   
                   elemento = LabirintoMoedas.temElemento(atual.x + 1, atual.y - 1);
                   if((elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Buraco && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x+1, atual.y-1))) 
                        sucessor.add(new Nodo(atual.x+1, atual.y-1));
                }
                elemento = LabirintoMoedas.temElemento(atual.x + 1, atual.y);
                if((elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Buraco && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x+1, atual.y)))
                    sucessor.add(new Nodo(atual.x+1, atual.y));
                if(atual.y < LabirintoMoedas.getSize() -1){
                    elemento = LabirintoMoedas.temElemento(atual.x + 1, atual.y + 1);
                    if((elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Buraco && elemento.getTipo() != TipoElemento.Porta)||destino.equals(new Ponto(atual.x+1, atual.y+1)))
                        sucessor.add(new Nodo(atual.x+1, atual.y+1));
                } 
            }
            
            if(atual.y != 0){
                elemento = LabirintoMoedas.temElemento(atual.x, atual.y - 1);
                if((elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Buraco && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x, atual.y-1)))
                    sucessor.add(new Nodo(atual.x, atual.y-1));
                
            }
            
            if(atual.y < LabirintoMoedas.getSize() -1){
                elemento = LabirintoMoedas.temElemento(atual.x, atual.y+1);
                if((elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Buraco && elemento.getTipo() != TipoElemento.Porta)|| destino.equals(new Ponto(atual.x, atual.y+1)))
                    sucessor.add(new Nodo(atual.x, atual.y+1));
            }
            for(Nodo vizinho: sucessor){
                vizinho.parent = atual;
                boolean flag = false;
                if(destino.equals(new Ponto(vizinho.x, vizinho.y))){
                    return vizinho;
                }
                vizinho.stepsCost = atual.stepsCost + 1;
                vizinho.heuristicCost = this.diagonalDistace(new Ponto(vizinho.x,vizinho.y), destino);
                vizinho.finalCost = vizinho.stepsCost + vizinho.heuristicCost;
                for(Nodo n : aberto){
                    if(n.x == vizinho.x && n.y == vizinho.y){
                        if(n.finalCost > vizinho.finalCost){
                            n.parent = vizinho.parent;
                            n.finalCost = vizinho.finalCost;
                            flag = true;
                            break;
                        }
                    }
                }
                for(Nodo n : fechado){
                    if(n.x == vizinho.x && n.y == vizinho.y){
                        if(n.finalCost > vizinho.finalCost){
                            aberto.add(vizinho);
                            flag = true;
                        }
                    }
                }
                if(flag == false){
                    aberto.add(vizinho);
}
                
            } 
        }
        return null;
}
    
    public double diagonalDistace(Ponto start, Ponto end){
        double dx = Math.abs(start.x - end.x);
        double dy = Math.abs(start.y - end.y);
        return (1 *(dx + dy) + (Math.sqrt(2) - 2 * 1) * Math.min(dx, dy));
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
