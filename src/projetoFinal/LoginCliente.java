/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoFinal;


import java.io.Serializable;

/**
 *
 * @author gjafa
 */
public class LoginCliente implements Serializable{
   
    public InterfaceCliente interfaceCliente;
  
    private String Nome;
    private int fichas;
    private String estadoJogador;
    private boolean minhaVez;
    
    public LoginCliente(String aNome, InterfaceCliente interfaceCliente, String estadoJogador){
        
        this.interfaceCliente = interfaceCliente;
 
        this.Nome=aNome;
        this.fichas=1000;
        this.estadoJogador = estadoJogador;
        this.minhaVez = false;
        
    }

    public String getNome() {
        return Nome;
    }

    public int getFichas() {
        return fichas;
    }

    public void setFichas(int fichas) {
        this.fichas = fichas;
    }

    public InterfaceCliente getInterfaceCliente() {
        return interfaceCliente;
    }

    public boolean isMinhaVez() {
        return minhaVez;
    }

    public void setMinhaVez(boolean minhaVez) {
        this.minhaVez = minhaVez;
    }
    
    
 @Override
    public String toString() {
        return "LoginCliente{" + "Nome=" + Nome + ", fichas=" + fichas + ", estadoJogador=" + estadoJogador + ", minhaVez=" + minhaVez + '}';
    }
  
    
    
    
}

    
    
    
    

