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
    private int vezJogador;
    private int numJogadoresTable;
    private boolean exitTrhead = false;
    int tempoThread = 0;

    public Servidor() throws RemoteException {
        super();
        listaLogin = new Vector<LoginCliente>();

        estado = "pausa";
    }

    public static void main(String[] args) {

        try {
            estado = "pausa";
            System.out.println("IP do servidor RMI: " + InetAddress.getLocalHost().getHostAddress());
            Servidor blackjack = new Servidor();

            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("Blackjack", blackjack);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public synchronized LoginCliente login(String nome, InterfaceCliente cliente) throws RemoteException {

        LoginCliente loginCliente;
        boolean minhaVez = false;

        if (listaLogin.size() < 3) {
            loginCliente = new LoginCliente(nome, cliente, "jogador");
            vezJogador = 0;

            if (listaLogin.isEmpty()) {
                minhaVez = true;
                loginCliente.setMinhaVez(minhaVez);
            }

            System.out.println("projetoFinal.Servidor.login() - " + loginCliente.toString());

        } else {
            loginCliente = new LoginCliente(nome, cliente, "observador");

        }

        loginCliente.getInterfaceCliente().vezJogador(minhaVez, listaLogin.size());

        this.listaLogin.add(loginCliente);
        System.out.println("nome do utilizador: " + nome);
        System.out.println("numeroo de utilizadofre: " + listaLogin.size());
        newClientListSend();
        System.out.println(estado + " " + listaLogin.size());
        if (listaLogin.size() == 1 && estado.equalsIgnoreCase("pausa")) {
            threadTempo();
            tempoThread = 10;
        } else if (estado.equalsIgnoreCase("jogar")) {
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
        int i = 0;
        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();
            if (obj2.getNome().equalsIgnoreCase(nome) && obj2.getInterfaceCliente().equals(iCliente)) {
                ite.remove();

                if (i == 0) {
                    removeTheElement(1);
                } else if (i == 1) {
                    removeTheElement(2);
                } else if (i == 2) {
                    removeTheElement(3);
                }
            }
            i++;

        }
        newClientListSend();
        iniciarJogo();

    }

    public void removeTheElement(int index) {

        if (this.arrayCartas != null) {

            Card[][] anotherArray = new Card[this.arrayCartas.length - 1][];

            for (int i = 0, k = 0; i < this.arrayCartas.length; i++) {

                if (i == index) {
                    System.out.println(i);
                    continue;
                }

                anotherArray[k++] = this.arrayCartas[i];

            }
            this.arrayCartas = anotherArray;
        }
    }

    public synchronized void iniciarJogo() throws RemoteException {
        Card card;

        Deck gameDeck = new Deck();         // cria um baralho
        gameDeck.shuffle();

        try {
            if (!estado.equalsIgnoreCase("jogar")) {
                if (listaLogin.size() < 3) {

                    arrayCartas = new Card[listaLogin.size() + 1][2];
                } else {
                    arrayCartas = new Card[4][2];
                }

                for (int i = 0; i < arrayCartas.length; i++) {
                    for (int k = 0; k < arrayCartas[i].length; k++) {
                        card = gameDeck.deal();
                        arrayCartas[i][k] = card;
                    }
                }
            }

            Iterator<LoginCliente> ite = listaLogin.iterator();

            while (ite.hasNext()) {
                LoginCliente obj2 = ite.next();

                try {

                    obj2.getInterfaceCliente().jogar(arrayCartas);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            this.estado = "jogar";

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void hit(int nPlayer) throws RemoteException {
        Card card;

        Deck gameDeck = new Deck();         // cria um baralho
        gameDeck.shuffle();
        card = gameDeck.deal();
        Card[][] longer = new Card[arrayCartas.length][arrayCartas[nPlayer].length + 1];
        for (int i = 0; i < longer.length; i++) {
            for (int k = 0; k < longer[i].length; k++) {
                if (k < arrayCartas[i].length) {
                    longer[i][k] = arrayCartas[i][k];
                }
                if (k >= arrayCartas[i].length && nPlayer == i) {

                    longer[i][k] = card;
                }
            }
        }
        arrayCartas = longer;
        Iterator<LoginCliente> ite = listaLogin.iterator();

        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();

            try {

                obj2.getInterfaceCliente().jogar(arrayCartas);
                obj2.getInterfaceCliente().mensagemGeral(2, vezJogador);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        tempoThread = 20;

    }

    public synchronized void stand(int nPlayer) throws RemoteException {

        //Sempre que um jogador faz stand incrementa o contador vezJogador e passa para a vez do próximo jogador. até o fim da rodada
        vezJogador++;
        System.out.println("projetoFinal.Servidor.stand() - numJogadoresTable: " + numJogadoresTable + " - vezJogador: " + vezJogador + " - vezJogador: " + vezJogador);
        if (numJogadoresTable == vezJogador) {
            vezJogador = 0;
            //novaRodada(); Depois é só descometntar essa linha, é aqui onde o servidor vai chamar a função de nova rodada e essa função vai verificar os pontos de cada utilizador
        }

        Iterator<LoginCliente> ite = listaLogin.iterator();

        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();

            try {

                if (vezJogador == 0) {
                    obj2.getInterfaceCliente().mensagemGeral(3, vezJogador);
                } else {
                    obj2.getInterfaceCliente().mensagemGeral(3, vezJogador - 1);
                }

                obj2.getInterfaceCliente().vezJogador(false, 0);
                obj2.setMinhaVez(false);
                obj2.getInterfaceCliente().mensagemGeral(1, vezJogador);
                obj2.getInterfaceCliente().jogar(arrayCartas);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        listaLogin.get(vezJogador).getInterfaceCliente().vezJogador(true, vezJogador);
        listaLogin.get(vezJogador).getInterfaceCliente().jogar(arrayCartas);
        listaLogin.get(vezJogador).setMinhaVez(true);
        tempoThread = 20;

    }

    //Começa aqui a nova rodada
    public synchronized void novaRodada() {

    }

    public synchronized void sendMensagens(int codigoMensagem, int vezJogador) throws RemoteException {
        Iterator<LoginCliente> ite = listaLogin.iterator();

        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();
            try {

                obj2.getInterfaceCliente().mensagemGeral(codigoMensagem, vezJogador);

            } catch (Exception ex) {
                System.out.println("projetoFinal.Servidor.newClientListSend(): " + ex);
                logout(obj2.getNome(), obj2.getInterfaceCliente());
            }
        }
    }

    public synchronized void threadTempo() {
        new Thread() {

            @Override
            public void run() {

                while (true) {

                    try {
                        
                        //Manda o tempo que falta para os clientes
                        Iterator<LoginCliente> ite = listaLogin.iterator();
                        while (ite.hasNext()) {
                            LoginCliente obj2 = ite.next();

                            try {
                                obj2.getInterfaceCliente().tempo(tempoThread);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        Thread.sleep(1000L);// 1000L = 1000ms = 1 second

                        tempoThread--;

                        if (estado.equals("pausa")) {
                            System.err.println(".run() - Tempo: " + tempoThread);
                        } else {
                            System.out.println(".run() - Tempo: " + tempoThread);
                        }

                        //Verrifica se o tempo da thread já chegou ao fim, a trhead agora o corre sempre, assim não precisamos de estar a matar e a reiniciar a trhead
                        if (tempoThread == 0) {
                            if (estado.equals("pausa")) {

                                if (listaLogin.size() > 3) {
                                    numJogadoresTable = 3;
                                } else {
                                    numJogadoresTable = listaLogin.size();
                                }
                                iniciarJogo();
                                sendMensagens(1, vezJogador);
                                tempoThread = 20;
                            } else if (estado.equals("jogar")) {
                                tempoThread = 20;
                                stand(vezJogador);
                            }
                        }

                    } catch (Exception e) {
                        System.err.println(e);
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, e);
                    }

                }

            }
        }.start();

    }

}

//Código das mensagens
//0 - Faltam 5 segundos para o jogo começar
//1 - vez do jogador #
//2 - O jogador deu HIT
//3 - O jogadir # fez stand
//4 - o Jogador # tem mais 10 segundos para a fazer a jogada
//5 - o Jogador # tem mais 5 segundos para a fazer a jogada
//Estados do jogo
// - pausa -> A espera que o jogo comece, demora 10 segundos
// - jogar -> os jogadores podem jogar
// - pausaRodada -> estado de pausa entre as rodadas (ainda não foi utilizado)
