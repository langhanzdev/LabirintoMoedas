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
    private final int n = 10;
    private Random rand = new Random();
    
    public static void main(String[] args) {
        
        
        LabirintoMoedas lab = new LabirintoMoedas();
        lab.geraParedao();
        lab.desenhaAmbiente();
        
    }
    
    /**
     * Desenha o cenario de jogo
    */
    public void desenhaAmbiente(){
        Elemento e;
        for(int y=0;y<this.n;y++){
            for(int x=0;x<this.n;x++){
                e = temElemento(x,y);
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
                                    System.out.print("[S]");
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
    
    public void geraParedao(){
        int parede = rand.nextInt(4);
        System.out.println("Parede "+parede);
        switch(parede){
            case 0:
                for(int i=0;i<10;i++)
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, 0, i));
                break;
            case 1:
                for(int i=0;i<10;i++)
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, i, 0));
                break;
            case 2:
                for(int i=0;i<10;i++)
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, 9, i));
                break;
            case 3:
                for(int i=0;i<10;i++)
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, i, 9));
                break;
        }
        int porta = rand.nextInt(10);
        listaElementos.get(porta).setTipo(TipoElemento.Porta);
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
    
    public boolean isLivre(int x, int y){
        Elemento e;
        for(int i=0;i<listaElementos.size();i++){
            e  = listaElementos.get(i);
            if(e.getX() == x && e.getY() == y){
                if(e.getTipo() == TipoElemento.Bau || e.getTipo() == TipoElemento.Saco){
                    return true;
                }else{
                    return false;
                }
            }
        }
        if(x < 0 || y < 0 || x >= n || y >= n)
            return false;
        return true;
    }
    
}
