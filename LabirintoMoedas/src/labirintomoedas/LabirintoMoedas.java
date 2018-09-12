/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labirintomoedas;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author langhanz
 */
public class LabirintoMoedas {

    public static ArrayList<Elemento> listaElementos = new ArrayList<>();
    public Elemento porta;
    private int ladoPorta;
    private Agente agente;
    public static final int n = 10;
    private Random rand = new Random();
    
    public static void main(String[] args) throws InterruptedException {
        
        
        LabirintoMoedas lab = new LabirintoMoedas();
        lab.agente = new Agente();
        System.out.println("Criado paredes...");
        lab.geraParedao();
        lab.desenhaAmbiente();
        System.out.println("Criando baus...");
        lab.geraBaus();
        System.out.println("Criando muros...");
        lab.geraMuros();
        System.out.println("Criando buracos...");
        lab.geraBuracos();
        
        System.out.println("Criando sacos...");
        lab.geraSacos();
        System.out.println("Liberando agente...");
        lab.geraAgente();
        for(int i=0;i<2;i++){
            lab.agente.anda();
            lab.desenhaAmbiente();
            Thread.sleep(500);
        }
        
    }
    
    /**
     * Desenha o cenario de jogo
    */
    public void desenhaAmbiente(){
        Elemento e;
        for(int y=0;y<this.n;y++){
            for(int x=0;x<this.n;x++){
                e = temElemento(x,y);
                if(agente.getX() == x && agente.getY() == y){
                    System.out.print("[A]");
                }else
                if(e != null){
                    if(e.getTipo() == TipoElemento.Parede){
                        System.out.print("[#]");
                    }else{
                        if(e.getTipo() == TipoElemento.Bau){
                            System.out.print("[B]");
                        }else{
                            if(e.getTipo() == TipoElemento.Buraco){
                                System.out.print("[X]");
                            }else{
                                if(e.getTipo() == TipoElemento.Saco){
                                    System.out.print("[$]");
                                }else{
                                    if(e.getTipo() == TipoElemento.Porta){
                                        System.out.print("[P]");
                                    }
                                }
                            }
                        }
                    }
                }else
                System.out.print("[ ]");
            }
            System.out.println();
        }
        System.out.println("----------------------------------------------------------------");
    }
    
    /**
     * Retorna o elemento que está na posicao passada por parametro
     * @param x
     * @param y
     * @return Elemento da posicao x,y
     */
    public Elemento temElemento(int x, int y){
        Elemento aux;
        for(int i=0;i<listaElementos.size();i++){
            aux  = listaElementos.get(i);
            if(aux.getX() == x && aux.getY() == y){
                return aux;
            }
        }
        return null;
    }
    
    /**
     * Gera o paredão onde se encontra a porta
     */
    public void geraParedao(){
        int parede = rand.nextInt(4); //escolhe em qual lado vai o paredão
        ladoPorta = parede;
        switch(parede){
            case 0:
                for(int i=0;i<n;i++)
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, 0, i));
                break;
            case 1:
                for(int i=0;i<n;i++)
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, i, 0));
                break;
            case 2:
                for(int i=0;i<n;i++)
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, n-1, i));
                break;
            case 3:
                for(int i=0;i<n;i++)
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, i, n-1));
                break;
        }
        int porta = rand.nextInt(n-2)+1;
        listaElementos.get(porta).setTipo(TipoElemento.Porta);
        this.porta = listaElementos.get(porta);
    }
    
    /**
     * Gera os muros internos do mapa
     */
    public void geraMuros(){
        int x,y;
        boolean ok = true;
        for(int p=0;p<4;p++){
            int posicao = rand.nextInt(2); // 0 - vertical, 1 - horizontal
            if(posicao == 0){ //Vertical
                do{
                    ok=true;
                    y = rand.nextInt(n-5);
                    x = rand.nextInt(n-1);
                    for(int i=y;(i-y < 5);i++){
                        if(!isLivre(x,i) || isColadoParedao(x, i, 0) || isFrentePorta(x,i,0)){
                            ok = false;
                            break;
                        }
                    }
                    
                }while(!ok);
                for(int i=y;(i-y < 5);i++){
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, x, i));
                }
            }else{ // Horizontal
                do{
                    ok=true;
                    y = rand.nextInt(n-1);
                    x = rand.nextInt(n-5);
                    for(int i=x;i-x<5;i++){
                        if(!isLivre(i,y) || isColadoParedao(i, y, 1) || isFrentePorta(i,y,1)){
                            ok = false;
                            break;
                        }
                    }
                    
                }while(!ok);
                for(int i=x;i-x<5;i++){
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, i, y));
                }
            }
        }
    }
    
    /**
     * Gera os buracos do mapa
     */
    public void geraBuracos(){
        int x,y;
        for(int i=0;i<4;i++){
            do{
                y = rand.nextInt(n);
                x = rand.nextInt(n);
            }while(!isLivre(x, y));
            listaElementos.add(new Elemento(TipoElemento.Buraco, i, x, y));
        }
    }
    
    /**
     * Gera a posicao inicial do agente
     */
    public void geraAgente(){
        int x,y;      
        do{
            y = rand.nextInt(n);
            x = rand.nextInt(n);
        }while(!isLivre(x, y) || !temSaida(x, y));
        agente.setX(x);
        agente.setY(y);
        
    }
    
    /**
     * Verifica se tem saida na posicao atual
     * @param x
     * @param y
     * @return true se tiver saida
     */
    public boolean temSaida(int x, int y){
        if(!isLivre(x-1,y) && !isLivre(x+1,y) && !isLivre(x,y-1) && !isLivre(x,y+1))
            return false;
        return true;
    }
    
    /**
     * Gera os baus do mapa
     */
    public void geraBaus(){
        
        
        for(int i=0;i<4;i++){
            
            int y,x;
            
            if (this.porta.getX() == 0) {
                do{
                    y = rand.nextInt(n);
                }while(!isLivre(1, y));
                listaElementos.add(new Elemento(TipoElemento.Bau, i, 1, y));

            } else if (this.porta.getX() == 9) {
                do{
                    y = rand.nextInt(n);
                }while(!isLivre(8,y));
                listaElementos.add(new Elemento(TipoElemento.Bau, i, 8, y));

            } else if (this.porta.getY() == 0) {
                do{
                    x = rand.nextInt(n);
                }while(!isLivre(x,1));
                listaElementos.add(new Elemento(TipoElemento.Bau, i, x, 1));

            } else if (this.porta.getY() == 9) {
                do{
                    x = rand.nextInt(n);
                }while(!isLivre(x,8));
                listaElementos.add(new Elemento(TipoElemento.Bau, i, x, 8));

            }
        }
    }
    
    /**
     * Gera sacos de moedas no mapa
     */
    public void geraSacos(){
        int x,y;
        for(int i=0;i<7;i++){
            do{
            y = rand.nextInt(n);
            x = rand.nextInt(n);
            }while(!isLivre(x, y));
            listaElementos.add(new Elemento(TipoElemento.Saco, i, x, y));
        }
    }
    
    /**
     * Returna true se na posicao passada tiver uma parede
     * @param x
     * @param y
     * @return true se x,y for parede
     */
    public boolean isParede(int x, int y){
        Elemento e;
        for(int i=0;i<listaElementos.size();i++){
            e  = listaElementos.get(i);
            if(e.getX() == x && e.getY() == y && e.getTipo() == TipoElemento.Parede){
                return true;
            }
        }
        return false;
    }
    
    /**
     * retorna se a posicao está livre
     * @param x
     * @param y
     * @return true se está livre
     */
    public static boolean isLivre(int x, int y){
        Elemento e;
        for(int i=0;i<listaElementos.size();i++){
            e  = listaElementos.get(i);
            if(e.getX() == x && e.getY() == y){
                
                return false;
                
            }
        }
        if(x < 0 || y < 0 || x >= n || y >= n)
            return false;
        return true;
    }
    
    /**
     * returna se a posicao está colada com o paredao
     * @param x
     * @param y
     * @param sentido
     * @return true se está colada com o paredao
     */
    public boolean isColadoParedao(int x, int y, int sentido){
        
        switch(ladoPorta){
            case 0:
                if(sentido == 0 && x == 1){
                    return true;
                }
                break;
            case 1:
                if(sentido == 1 && y == 1){
                    return true;
                }
                break;
            case 2:
                if(sentido == 0 && x == 8){
                    return true;
                }
                break;
            case 3:
                if(sentido == 1 && y == 8){
                    return true;
                }
                break;
        }
        
        return false;
    }
    
    /**
     * retorna se a posicao está na frente da porta
     * @param x
     * @param y
     * @param sentido
     * @return true se está na frente da porta
     */
    public boolean isFrentePorta(int x, int y, int sentido){
        
        switch(ladoPorta){
            case 0:
                if(sentido == 1 && x == 1){
                    return true;
                }
                break;
            case 1:
                if(sentido == 0 && y == 1){
                    return true;
                }
                break;
            case 2:
                if(sentido == 1 && x == 8){
                    return true;
                }
                break;
            case 3:
                if(sentido == 0 && y == 8){
                    return true;
                }
                break;
        }
        
        return false;
    }
    
}
