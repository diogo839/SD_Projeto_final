/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoFinal;

import cliente.Card;
import cliente.CardLabel;
import cliente.Deck;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Timer;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gjafa
 */
public class Servidor extends UnicastRemoteObject implements InterfaceServidor {

    public Vector<LoginCliente> listaLogin;

    private static String estado;
    public Card[][] arrayCartas;
    public Servidor() throws RemoteException {
        super();
        listaLogin = new Vector<LoginCliente>();

        estado = "pausa";
    }

    @Override
    public synchronized LoginCliente login(String nome, InterfaceCliente cliente) throws RemoteException {

        LoginCliente loginCliente;

       

        if (listaLogin.size() < 3) {
            loginCliente = new LoginCliente(nome, cliente, "jogo");

        } else {
            loginCliente = new LoginCliente(nome, cliente, "espera");

        }

        
        this.listaLogin.add(loginCliente);
        System.out.println("nome do utilizador: " + nome);
        System.out.println("numeroo de utilizadofre: " + listaLogin.size());
        newClientListSend();
        System.out.println(estado+" "+listaLogin.size());
         if(listaLogin.size()==1 && estado.equalsIgnoreCase("pausa")) {
           threadIniciarJogo();
        }else if(estado.equalsIgnoreCase("jogar")){
             iniciarJogo();
         }
        return loginCliente;
    }

    public synchronized LoginCliente[] getJogadores() throws RemoteException {
        if (listaLogin != null) {
            LoginCliente[] array = listaLogin.toArray(new LoginCliente[listaLogin.size()]);

            return array;
        }
        return null;

    }

    public synchronized void newClientListSend() throws RemoteException {
        Iterator<LoginCliente> ite = listaLogin.iterator();

        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();
            try {

                obj2.getInterfaceCliente().NewListaUsers(listaLogin.toArray(new LoginCliente[listaLogin.size()]));

            } catch (Exception ex) {
                System.out.println("projetoFinal.Servidor.newClientListSend(): " + ex);
                logout(obj2.getNome(), obj2.getInterfaceCliente());
            }
        }
    }

    public synchronized void logout(String nome, InterfaceCliente iCliente) throws RemoteException {
        Iterator<LoginCliente> ite = listaLogin.iterator();
        int i=0;
        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();
            if (obj2.getNome().equalsIgnoreCase(nome) && obj2.getInterfaceCliente().equals(iCliente)) {
                ite.remove();
                
                if(i==0){
                    removeTheElement(1);
                }else  if(i==1){
                    removeTheElement(2);
                }
                else  if(i==2){
                    removeTheElement(3);
                }
            }
            i++;
            
        }
        newClientListSend();
        iniciarJogo();
        

    }

    
    public void removeTheElement( int index)
    {
  
        if (this.arrayCartas != null){
           
  
      
        Card[][] anotherArray = new Card[this.arrayCartas.length - 1][];
  
        for (int i = 0, k = 0; i < this.arrayCartas.length; i++) {
  
            if (i == index) {
                System.out.println(i);
                continue;
            }
  
            anotherArray[k++] = this.arrayCartas[i];
            
        }
            this.arrayCartas=anotherArray;
        }
    }
    
    
    public  static void main(String[] args) {

        try {
            estado="pausa";
            System.out.println("IP do servidor RMI: " + InetAddress.getLocalHost().getHostAddress());
            Servidor blackjack = new Servidor();

            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("Blackjack", blackjack);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public synchronized void iniciarJogo() throws RemoteException {
        Card card;

        Deck gameDeck = new Deck();         // cria um baralho
        gameDeck.shuffle();
        
        try {
            if(!estado.equalsIgnoreCase("jogar")){
            if (listaLogin.size() < 3) {
                    
                arrayCartas = new Card[listaLogin.size()+1][2];
            } else {
                arrayCartas = new Card[4][2];
            }

            for (int i = 0; i < arrayCartas.length; i++) {
                for (int k = 0; k < arrayCartas[i].length; k++){
                card = gameDeck.deal();
                arrayCartas[i][k] = card;
                }
            }}
            
            Iterator<LoginCliente> ite = listaLogin.iterator();

            
            while (ite.hasNext()) {
                LoginCliente obj2 = ite.next();
                
                try {

                    obj2.getInterfaceCliente().jogar(arrayCartas);
                   
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            this.estado="jogar";
            sendMensagens(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void  hit(int nPlayer)throws RemoteException{
         Card card;

        Deck gameDeck = new Deck();         // cria um baralho
        gameDeck.shuffle();
        card = gameDeck.deal();
         Card[][] longer = new Card[arrayCartas.length][arrayCartas[nPlayer].length+1];
        for (int i = 0; i < longer.length; i++){
            for (int k = 0; k < longer[i].length; k++){
                if(k<arrayCartas[i].length){
                longer[i][k] = arrayCartas[i][k];
                }
                if(k>=arrayCartas[i].length && nPlayer==i ){

                   longer[i][k] =card;
                }
            }
        }
        arrayCartas=longer;
         Iterator<LoginCliente> ite = listaLogin.iterator();

            
            while (ite.hasNext()) {
                LoginCliente obj2 = ite.next();
                
                try {

                    obj2.getInterfaceCliente().jogar(arrayCartas);
                   
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
  
    }
    
    
    public synchronized void sendMensagens(int codigoMensagem) throws RemoteException {
        Iterator<LoginCliente> ite = listaLogin.iterator();
        
        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();
            try {

                obj2.getInterfaceCliente().mensagemGeral(codigoMensagem);

            } catch (Exception ex) {
                System.out.println("projetoFinal.Servidor.newClientListSend(): " + ex);
                logout(obj2.getNome(), obj2.getInterfaceCliente());
            }
        }
    }

    public synchronized  void threadIniciarJogo() {
        new Thread() {

            @Override
            public void run() {

                try {
                    int iniciar = 10;
                    
                    while (iniciar != 0) {
                        
                        try {
                            
                            Thread.sleep(1000L);// 1000L = 1000ms = 1 second
                            System.err.println("Segundos" + iniciar);
                            iniciar--;
                            
                            if (iniciar == 5) {
                                sendMensagens(0);
                            }
                        } catch (Exception e) {
                            System.err.println(e);
                        }

                    }

                    
                    iniciarJogo();
              
                } catch (RemoteException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }.start();

    }

}
