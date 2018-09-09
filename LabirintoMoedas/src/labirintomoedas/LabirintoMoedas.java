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
        lab.geraMuros();
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
    
    /**
     * Gera o paredão onde se encontra a porta
     */
    public void geraParedao(){
        int parede = rand.nextInt(4);
        System.out.println("Parede "+parede);
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
        int porta = rand.nextInt(n);
        listaElementos.get(porta).setTipo(TipoElemento.Porta);
    }
    
    
    public void geraMuros(){
        for(int p=0;p<4;p++){
            int posicao = rand.nextInt(2); // 0 - vertical, 1 - horizontal
            if(posicao == 0){
                int y = rand.nextInt(n-5);
                int x = rand.nextInt(n-1);
                for(int i=y;(i-y < 5);i++){
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, x, i));
                }
            }else{
                int y = rand.nextInt(n-1);
                int x = rand.nextInt(n-5);
                for(int i=x;i-x<5;i++){
                    listaElementos.add(new Elemento(TipoElemento.Parede, i, i, y));
                }
            }
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
