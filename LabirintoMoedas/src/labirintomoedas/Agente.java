package labirintomoedas;

import java.util.ArrayList;
import java.util.Random;


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
    private ArrayList<Elemento> listaSacos;
    private LabirintoMoedas labirinto;

    public Agente(int x, int y) {
        this.x = x;
        this.y = y;
        this.listaElementos = new ArrayList<>();
        this.listaSacos = new ArrayList<>();
        this.direcao = 0;
    }

    public Agente() throws InterruptedException {
        this.listaElementos = new ArrayList<>();
        this.listaSacos = new ArrayList<>();
        this.direcao = 0;
        System.out.println("Iniciando labirinto");
        labirinto = new LabirintoMoedas(this);
        labirinto.geraAgente();
        
        setX(labirinto.getXAgente());
        setY(labirinto.getYAgente());
//        for(int i=0;i<10 ;i++){
            labirinto.desenhaAmbiente();
            anda();
            Thread.sleep(500);
//        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Agente agente = new Agente();
        
    }
    
    public void anda() throws InterruptedException{
        for(int i=0;i<200;i++){
            
            detector();
            labirinto.desenhaAmbiente();
            
            Elemento e = temElemento(getX(), getY());
            if(e != null && e.getTipo() == TipoElemento.Buraco){
                System.out.println("Game Over");
                return;
            }
            
            if(listaSacos.size() == 16){
                genetico();
                caminhaAstar(labirinto.getPorta());
            }
            
            Elemento saco = temSacoNaoVisitado();
            if(saco != null){
                caminhaAstar(saco);
                
            }else{
                caminhaAleatorio();
            }
            
            Thread.sleep(250);
        }
    }
    
    public void caminhaAstar(Elemento destino) throws InterruptedException{
//        System.out.println("Tamanho lista: "+this.listaElementos.size());
                Nodo caminho, melhorCaminho;
                double valorCaminho = Double.MAX_VALUE;
                Ponto p = new Ponto(destino.getX(),destino.getY());
               // System.out.println("inicio caminho");
                caminho = this.aStar(new Ponto(this.x, this.y), p);
               // System.out.println("valor caminho: " + caminho.parent.finalCost);
                if (caminho.parent.finalCost < valorCaminho) {
                    valorCaminho = caminho.parent.finalCost;
                    melhorCaminho = caminho;
                }
                andaCaminho(caminho);
                while (caminho.parent != null) {
                    //System.out.println(caminho.toString());
                    caminho = caminho.parent;

                }

                //System.out.println("final caminho");
    }
    
    public void caminhaAleatorio(){
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
    
    public int andaCaminho(Nodo caminho) throws InterruptedException{
        if(caminho == null) return 0;
        int retorno = andaCaminho(caminho.parent);
        Elemento e = temElemento(caminho.x,caminho.y);
        if(retorno == 1 && e != null && e.getTipo() == TipoElemento.Buraco){
            return -1;
        }
        if(retorno == 1 && e != null && e.getTipo() != TipoElemento.Buraco){
            setX(caminho.x);
            setY(caminho.y);
            detector();
            return 0;
        }
        if(retorno == 0 && e != null && e.getTipo() == TipoElemento.Buraco){
            setX(caminho.x);
            setY(caminho.y);
            detector();
            return 1;
        }
        setX(caminho.x);
        setY(caminho.y);
        detector();
        labirinto.desenhaAmbiente();
        Thread.sleep(250);
        return 0;
    }
    
    public void detector(){
        Elemento e;
        e = temElemento(getX(),getY());
        if(e != null && e.getTipo() == TipoElemento.Saco){
            recolhoMoedas();
            labirinto.listaElementos.remove(e);
            listaElementos.remove(e);
            listaSacos.add(e);
        }
        
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
    
    public void recolhoMoedas(){
        
    }
    
    public int qtdSacosRecolhidos(){
        int soma = 0;
        for(Elemento e:listaElementos){
            if(e.getTipo() == TipoElemento.Saco){
                soma++;
            }
        }
        return soma;
    }
    
    public Elemento temSacoNaoVisitado(){
        for(Elemento e:listaElementos){
            if(!e.isIsVisitado() && e.getTipo() == TipoElemento.Saco)
                return e;
        }
        return null;
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
        for(int i=0;i<labirinto.listaElementos.size();i++){
            aux  = labirinto.listaElementos.get(i);
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
        int d;
        int dif;
        if(getDirecao() == 0);
            dif = 2;
        if(getDirecao() == 1){
            dif = 3;
        }
        if(getDirecao() == 2){
            dif = 0;
        }
        if(getDirecao() == 3){
            dif = 1;
        }
        do{
            d = ran.nextInt(4);
        }while(d == dif);
        return d;
    }
    
    /**
     * Retorna se a posicao passada está livre para o agente
     * seguindo as regras do agente. Ele pode andar sobre baus e sacos de moedas
     * @param x
     * @param y
     * @return 
     */
    public  boolean isLivre(int x, int y){
        Elemento e;
        for(int i=0;i < labirinto.listaElementos.size();i++){
            e  = labirinto.listaElementos.get(i);
            if(e.getX() == x && e.getY() == y){
                if(e.getTipo() == TipoElemento.Bau || e.getTipo() == TipoElemento.Saco){
                    return true;
                }else{
                    if(e.getTipo() == TipoElemento.Buraco){ //Pula buraco
                        switch(getDirecao()){
                            case 0:
                            if(temElemento(x, y+1) == null || temElemento(x, y+1).getTipo() == TipoElemento.Bau || temElemento(x, y+1).getTipo() == TipoElemento.Saco && y+2 < labirinto.n-1){
                                setY(getY()+1);
                                return true;
                            }
                            
                            break;
                        case 1:
                            if(temElemento(x+1, y) == null || temElemento(x+1, y).getTipo() == TipoElemento.Bau || temElemento(x+1, y).getTipo() == TipoElemento.Saco && x+2 < labirinto.n-1){
                                setX(getX()+1);
                                return true;
                            }
                            break;
                        case 2:
                            if(temElemento(x, y-1) == null || temElemento(x, y-1).getTipo() == TipoElemento.Bau || temElemento(x, y-1).getTipo() == TipoElemento.Saco && y-2 < labirinto.n-1){
                                setY(getY()-1);
                                return true;
                            }
                            break;
                        case 3:
                            if(temElemento(x-1, y) == null || temElemento(x-1, y).getTipo() == TipoElemento.Bau || temElemento(x-1, y).getTipo() == TipoElemento.Saco && x-2 < labirinto.n-1){
                                setX(getX()-1);
                                return true;
                            }
                            break;
                                    
                        }
                    }
                    return false;
                }
            }
        }
        if(x < 0 || y < 0 || x >= labirinto.n || y >= labirinto.n)
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
//                if(atual.y > 0){
//                    elemento = labirinto.temElemento(atual.x-1, atual.y-1);
//                    if(elemento == null || (elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x-1, atual.y-1)))
//                        sucessor.add(new Nodo(atual.x-1, atual.y-1));
//                }
                elemento = labirinto.temElemento(atual.x - 1, atual.y);
                if(elemento == null || (elemento.getTipo() != TipoElemento.Parede  && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x-1, atual.y)))
                    sucessor.add(new Nodo(atual.x-1, atual.y));
//                if(atual.y < labirinto.getSize() -1){
//                    elemento = labirinto.temElemento(atual.x - 1, atual.y+1);
//                    if(elemento == null || (elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x-1, atual.y+1)))
//                        sucessor.add(new Nodo(atual.x-1, atual.y+1));
//                }
            }
            if(atual.x < labirinto.getSize() -1){
//               if(atual.y > 0){   
//                   elemento = labirinto.temElemento(atual.x + 1, atual.y - 1);
//                   if(elemento == null || (elemento.getTipo() != TipoElemento.Parede  && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x+1, atual.y-1))) 
//                        sucessor.add(new Nodo(atual.x+1, atual.y-1));
//                }
                elemento = labirinto.temElemento(atual.x + 1, atual.y);
                if(elemento == null || (elemento.getTipo() != TipoElemento.Parede  && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x+1, atual.y)))
                    sucessor.add(new Nodo(atual.x+1, atual.y));
//                if(atual.y < labirinto.getSize() -1){
//                    elemento = labirinto.temElemento(atual.x + 1, atual.y + 1);
//                    if(elemento == null || (elemento.getTipo() != TipoElemento.Parede  && elemento.getTipo() != TipoElemento.Porta)||destino.equals(new Ponto(atual.x+1, atual.y+1)))
//                        sucessor.add(new Nodo(atual.x+1, atual.y+1));
//                } 
            }
            
            if(atual.y != 0){
                elemento = labirinto.temElemento(atual.x, atual.y - 1);
                if(elemento == null || (elemento.getTipo() != TipoElemento.Parede  && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x, atual.y-1)))
                    sucessor.add(new Nodo(atual.x, atual.y-1));
                
            }
            
            if(atual.y < labirinto.getSize() -1){
                elemento = labirinto.temElemento(atual.x, atual.y+1);
                if(elemento == null || (elemento.getTipo() != TipoElemento.Parede  && elemento.getTipo() != TipoElemento.Porta)|| destino.equals(new Ponto(atual.x, atual.y+1)))
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

    // genetico ----------------------
    private int[] populacao = new int[17];
    private int[] atual = new int[17];
    private int[] melhor = new int[17];
    
    public void desenhaMelhor(){
        for(int x=0;x<4;x++)
        for(int i=0;i<16;i++){
            if(melhor[i] == x){
                System.out.print("["+listaSacos.get(i).getMoedas()+"]");
            }
            
        }
        System.out.println();
    }
    
    public void desenhaPop(){
        for(int x=0;x<4;x++)
        for(int i=0;i<16;i++){
            if(populacao[i] == x){
                System.out.print("["+listaSacos.get(i).getMoedas()+"]");
            }
            
        }
        System.out.println();
    }
    
    public boolean genetico(){
        System.out.println("############# GENETICO #######################");
        popular();
        for(int i=0;i < 1000;i++){
            mutar();
            if(aptdar()){
                
                return true;
            }
        }
        desenhaPop();
        desenhaMelhor();
        return false;
    }
    
    public void popular(){
        int cont0=0,cont1=0, cont2=0,cont3=0;
        Random rand = new Random();
        int r;
        for(int i=0;i<16;i++){
                     
            do{
                r = rand.nextInt(4);
            }while((r == 0 && cont0 >= 4) || (r == 1 && cont1 >= 4) || (r == 2 && cont2 >= 4) || (r == 3 && cont3 >= 4));
            populacao[i] = r;
            if(r == 0 && cont0 < 4) cont0++;
            if(r == 1 && cont1 < 4) cont1++;
            if(r == 2 && cont2 < 4) cont2++;
            if(r == 3 && cont3 < 4) cont3++;           
        }
        atual = populacao.clone();
    }
    public void mutar(){
        Random rand = new Random();
        int t1 = rand.nextInt(16);
        int t2 = rand.nextInt(16);
        atual = melhor.clone();
        int a = atual[t1];
        int b = atual[t2];
        atual[t1] = b;
        atual[t2] = a;
    }
    
    public boolean aptdar(){
        int soma0=0,soma1=0,soma2=0,soma3=0;
        for(int i=0;i<16;i++){
            if(atual[i] == 0) soma0 += listaSacos.get(i).getMoedas();
            if(atual[i] == 1) soma1 += listaSacos.get(i).getMoedas();
            if(atual[i] == 2) soma2 += listaSacos.get(i).getMoedas();
            if(atual[i] == 3) soma3 += listaSacos.get(i).getMoedas();           
        }
        int diferenca = Math.abs(soma0-soma1)-Math.abs(soma2-soma3);
        atual[16] = diferenca;
        if(diferenca == 0){
            melhor = atual.clone();
            return true;
        }
        if(diferenca < melhor[16]){
            melhor = atual.clone();
        }
        return false;
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
