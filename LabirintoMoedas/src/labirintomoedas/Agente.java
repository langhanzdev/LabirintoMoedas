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
    private int pontos;
    
    private final int sleep = 250;

    public Agente(int x, int y) {
        this.x = x;
        this.y = y;
        this.listaElementos = new ArrayList<>();
        this.listaSacos = new ArrayList<>();
        this.direcao = 0;
        this.pontos = 0;
    }

    public Agente() throws InterruptedException {
        this.listaElementos = new ArrayList<>();
        this.listaSacos = new ArrayList<>();
        this.direcao = 0;
        this.pontos = 0;
        System.out.println("Iniciando labirinto");
        labirinto = new LabirintoMoedas(this);
        labirinto.geraAgente();
        
        setX(labirinto.getXAgente());
        setY(labirinto.getYAgente());

        desenhaAmbiente();
        anda();
        Thread.sleep(500);

    }
    
    public static void main(String[] args) throws InterruptedException {
        Agente agente = new Agente();
        
    }
    
    // Metodos principais ------------------------------------------------------
    
    /**
     * Funcão que realiza os movimentos do agente no labirinto
     * @throws InterruptedException 
     */
    public void anda() throws InterruptedException{
        for(int i=0;i<200;i++){
            
            detector();
            desenhaAmbiente();
            
            
            Elemento e = temElemento(getX(), getY());
            if(e != null && e.getTipo() == TipoElemento.Buraco){
                mostraMensagemFinal(2);
                return;
            }
            
            if(listaSacos.size() == 16){
                genetico();
                depositaMoedas();
                
                for(Elemento p:listaElementos){
                    if(p.getTipo() == TipoElemento.Porta){
                        caminhaAstar(p);
                    }
                }
                
            }
            
            if(abriuPorta()){
                mostraMensagemFinal(1);
                return;
            }
            
            Elemento saco = temSacoNaoVisitado();
            if(saco != null){
                caminhaAstar(saco);
                
            }else{
                caminhaAleatorio();
            }
            
            Thread.sleep(sleep);
        }
    }
    
    /**
     * Deposita moedas nos baus
     * @throws InterruptedException 
     */
    public void depositaMoedas() throws InterruptedException{
        Elemento el;
        int contBau = 0;
        
        for(int j=0;j<listaElementos.size();j++){
            el = listaElementos.get(j);
            int soma = 0;
            if(el.getTipo() == TipoElemento.Bau){
                caminhaAstar(el);
                
                for(int x=0;x<4;x++){
                    for(int i=0;i<16;i++){
                        if(melhor[i] == x){
                            soma += listaSacos.get(j).getMoedas();
                        }

                    }
                    listaElementos.get(j).setMoedas(soma);
                    soma = 0;
                }
                
                
                //listaElementos.get(j).setMoedas(melhor[contBau]+melhor[contBau+1]+melhor[contBau+2]+melhor[contBau+3]);
                //System.out.println("Bau "+contBau+" M: "+listaElementos.get(j).getMoedas());
                contBau +=4;
            }

        }
    }
    
    /**
     * Faz o caminho encontrado pelo A*
     * @param destino
     * @throws InterruptedException 
     */
    public void caminhaAstar(Elemento destino) throws InterruptedException{

                Nodo caminho, melhorCaminho;
                double valorCaminho = Double.MAX_VALUE;
                Ponto p = new Ponto(destino.getX(),destino.getY());
                caminho = this.aStar(new Ponto(this.x, this.y), p);

                if (caminho.parent.finalCost < valorCaminho) {
                    valorCaminho = caminho.parent.finalCost;
                    melhorCaminho = caminho;
                }
                andaCaminho(caminho);
                while (caminho.parent != null) {
                    caminho = caminho.parent;
                }


    }
    
    /**
     * Faz um caminho aleatorio, quando não há nada detectado
     */
    public void caminhaAleatorio(){
        detector();
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
    
    /**
     * Anda o caminho determinado pelo A*, recursivamente
     * @param caminho
     * @return
     * @throws InterruptedException 
     */
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
        desenhaAmbiente();
        Thread.sleep(sleep);
        return 0;
    }
    
    /**
     * Detecta elementos ao redor
     */
    public void detector(){
        Elemento e;
        e = temElemento(getX(),getY());
        if(e != null && e.getTipo() == TipoElemento.Saco){
            labirinto.listaElementos.remove(e);
            listaElementos.remove(e);
            pontos += e.getMoedas()*10;
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
        
        e = temElemento(getX()-1, getY()+1);
        addLista(e);
        e = temElemento(getX()+1, getY()+1);
        addLista(e);
        e = temElemento(getX()+1, getY()-1);
        addLista(e);
        e = temElemento(getX()-1, getY()-1);
        addLista(e);
        
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
                                pontos += 30;
                                return true;
                            }
                            
                            break;
                        case 1:
                            if(temElemento(x+1, y) == null || temElemento(x+1, y).getTipo() == TipoElemento.Bau || temElemento(x+1, y).getTipo() == TipoElemento.Saco && x+2 < labirinto.n-1){
                                setX(getX()+1);
                                pontos += 30;
                                return true;
                            }
                            break;
                        case 2:
                            if(temElemento(x, y-1) == null || temElemento(x, y-1).getTipo() == TipoElemento.Bau || temElemento(x, y-1).getTipo() == TipoElemento.Saco && y-2 >= 0){
                                setY(getY()-1);
                                pontos += 30;
                                return true;
                            }
                            break;
                        case 3:
                            if(temElemento(x-1, y) == null || temElemento(x-1, y).getTipo() == TipoElemento.Bau || temElemento(x-1, y).getTipo() == TipoElemento.Saco && x-2 >= 0){
                                setX(getX()-1);
                                pontos += 30;
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
    
    /**
     * Classe interna Nodo, para realizar operaçoes no A*
     */
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

    /**
     * Algoritmo A*
     * @param inicio
     * @param destino
     * @return 
     */
    public Nodo aStar(Ponto inicio, Ponto destino) {
        Nodo atual = null;
        Nodo comp = null;
        ArrayList<Nodo> aberto = new ArrayList<>();
        ArrayList<Nodo> fechado = new ArrayList<>();
        Nodo nodo = new Nodo(inicio.x, inicio.y);
        nodo.finalCost = 0;
        nodo.heuristicCost = 0;
        aberto.add(nodo);
        while (!aberto.isEmpty()) {
            double max_final_cost = Double.MAX_VALUE;
            for (Nodo n : aberto) {
                if (n.finalCost < max_final_cost) {
                    atual = n;
                    max_final_cost = n.finalCost;
                }
            }
            aberto.remove(atual);
            fechado.add(atual);
            ArrayList<Nodo> sucessor = new ArrayList<>();
            Elemento elemento;
            if (atual.x != 0) {
                elemento = labirinto.temElemento(atual.x - 1, atual.y);
                if (elemento == null || (elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x - 1, atual.y))) {
                    sucessor.add(new Nodo(atual.x - 1, atual.y));
                }

            }
            if (atual.x < labirinto.getSize() - 1) {
                elemento = labirinto.temElemento(atual.x + 1, atual.y);
                if (elemento == null || (elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x + 1, atual.y))) {
                    sucessor.add(new Nodo(atual.x + 1, atual.y));
                }
            }

            if (atual.y != 0) {
                elemento = labirinto.temElemento(atual.x, atual.y - 1);
                if (elemento == null || (elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x, atual.y - 1))) {
                    sucessor.add(new Nodo(atual.x, atual.y - 1));
                }

            }

            if (atual.y < labirinto.getSize() - 1) {
                elemento = labirinto.temElemento(atual.x, atual.y + 1);
                if (elemento == null || (elemento.getTipo() != TipoElemento.Parede && elemento.getTipo() != TipoElemento.Porta) || destino.equals(new Ponto(atual.x, atual.y + 1))) {
                    sucessor.add(new Nodo(atual.x, atual.y + 1));
                }
            }
            for (Nodo vizinho : sucessor) {
                vizinho.parent = atual;
                boolean flag = false;
                if (destino.equals(new Ponto(vizinho.x, vizinho.y))) {
                    return vizinho;
                }
                vizinho.stepsCost = atual.stepsCost + 1;
                vizinho.heuristicCost = this.diagonalDistace(new Ponto(vizinho.x, vizinho.y), destino);
                vizinho.finalCost = vizinho.stepsCost + vizinho.heuristicCost;
                for (Nodo n : aberto) {
                    if (n.x == vizinho.x && n.y == vizinho.y) {
                        if (n.finalCost > vizinho.finalCost) {
                            n.parent = vizinho.parent;
                            n.finalCost = vizinho.finalCost;
                            flag = true;
                            break;
                        }
                    }
                }
                for (Nodo n : fechado) {
                    if (n.x == vizinho.x && n.y == vizinho.y) {
                        if (n.finalCost > vizinho.finalCost) {
                            aberto.add(vizinho);
                            flag = true;
                        }
                    }
                }
                if (flag == false) {
                    aberto.add(vizinho);
                }

            }
        }
        return null;
    }

    

    // genetico ---------------------------------------------------------------
    
    private int[] populacao = new int[17];
    private int[] atual = new int[17];
    private int[] melhor = new int[17];

    public boolean genetico(){
        System.out.println("############# GENETICO #######################");
        popular();
        for(int i=0;i < 5000;i++){
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
        int diferenca = Math.abs(soma0+soma1)-Math.abs(soma2+soma3);
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
    
    // ---- Auxiliares ----------------------------------------------------------
    
    public void desenhaAmbiente(){
        labirinto.desenhaAmbiente();
        mostraPontuacao();
    }
    
    public void mostraPontuacao(){
        int moedas = 0;
        for(Elemento e:listaSacos){
            moedas += e.getMoedas();
        }
        System.out.println("Moedas: "+moedas);
        System.out.println("Pontos: "+pontos);
    }
    
    public boolean abriuPorta(){
        for(Elemento e:listaElementos){
            if(e.getTipo() == TipoElemento.Porta){
                if(getX() == e.getX() && getY() == e.getY()){
                    pontos += 330;
                    return true;
                }
            }
        }
        return false;
    }
    
    public void mostraMensagemFinal(int tipo){
        System.out.println("--------------------------------------------------------");
        if(tipo == 1){
            System.out.println("SUCESSO: Agente conseguiu sair do labirinto!");
            mostraPontuacao();
//            for(Elemento e:listaElementos){
//                if(e.getTipo() == TipoElemento.Bau){
//                    System.out.println("Bau: "+e.getMoedas());
//                }
//            }
        }
        if(tipo == 2){
            System.out.println("GAME OVER");
            System.out.println("INSUCESSO: Agente caiu em um buraco.");
            mostraPontuacao();
//            for(Elemento e:listaElementos){
//                if(e.getTipo() == TipoElemento.Bau){
//                    System.out.println("Bau: "+e.getMoedas());
//                }
//            }
        }
        
        System.out.println("--------------------------------------------------------");
        
    }
    
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
    
    public double diagonalDistace(Ponto start, Ponto end){
        double dx = Math.abs(start.x - end.x);
        double dy = Math.abs(start.y - end.y);
        return (1 *(dx + dy) + (Math.sqrt(2) - 2 * 1) * Math.min(dx, dy));
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
    
    // GETs e SETs -------------------------------------------------------------

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
