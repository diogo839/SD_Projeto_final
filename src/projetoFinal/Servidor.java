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
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ConcurrentModificationException;
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
    public int[] valores;
    private int vezJogador;
    private int numJogadoresTable;
    private boolean exitTrhead = false;
    private boolean inGame = true;
    int tempoThread = 0;
    public LoginCliente[] playing;

    public Servidor() throws RemoteException {
        super();
        listaLogin = new Vector<LoginCliente>();
        playing = new LoginCliente[3];
        estado = "pausa";
    }

    public static void main(String[] args) {

        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Introduza o porto: ");
            int porto = sc.nextInt();

            estado = "pausa";
            System.out.println("IP do servidor RMI: " + InetAddress.getLocalHost().getHostAddress());
            Servidor blackjack = new Servidor();

            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("Blackjack", blackjack);

        } catch (RemoteException | UnknownHostException re) {
            System.out.println("Não foi possivel iniciar o servidor!!");
        } catch (java.util.InputMismatchException IE) {
            System.out.println("Introduza um número!!");
            main(args);
        }
    }

    public boolean verificarNome(String nome) {

        Iterator<LoginCliente> ite = listaLogin.iterator();

        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();
            if (obj2.getNome().equalsIgnoreCase(nome)) {
              
                return true;
            }
        }

        return false;
    }

    @Override
    public synchronized LoginCliente login(String nome, InterfaceCliente cliente) throws RemoteException {

        LoginCliente loginCliente;
        boolean minhaVez = false;

        if (verificarNome(nome)) {
            return null;
        } else {

            if (listaLogin.size() < 3) {
                loginCliente = new LoginCliente(nome, cliente, "jogador");
                vezJogador = 0;

                if (listaLogin.isEmpty() && playing.length == 0) {
                    minhaVez = true;
                    loginCliente.setMinhaVez(minhaVez);
                }

                //System.out.println("projetoFinal.Servidor.login() - " + loginCliente.toString());
            } else {
                loginCliente = new LoginCliente(nome, cliente, "observador");

            }

            this.listaLogin.add(loginCliente);
            //  System.out.println("nome do utilizador: " + nome);
            //  System.out.println("numeroo de utilizadofre: " + listaLogin.size());
            newClientListSend();
            //  System.out.println(estado + " " + listaLogin.size());
            if (listaLogin.size() == 1 && estado.equalsIgnoreCase("pausa")) {
                vezJogador = 0;
                tempoThread = 10;
                threadTempo();

            } else if (estado.equalsIgnoreCase("jogar")) {
                // iniciarJogo();
            }
            return loginCliente;

        }

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
                //.println("projetoFinal.Servidor.newClientListSend(): " + ex);

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
//                 
            }
            i++;

        }

        for (int k = 0; k < playing.length; k++) {
            if (playing[k].getInterfaceCliente().equals(iCliente) && playing[k].getNome().equalsIgnoreCase(nome)) {
                System.out.println("eliminar-" + playing[k].getNome());
                numJogadoresTable--;
                vezJogador--;
                newClientListSend();
                iniciarJogo();
                return;

            }

        }

        newClientListSend();
        iniciarJogo();

    }

    public synchronized void removeTheElement(int index) {

        if (this.arrayCartas != null) {

            Card[][] anotherArray = new Card[this.arrayCartas.length - 1][];

            for (int i = 0, k = 0; i < this.arrayCartas.length; i++) {

                if (i == index) {
                    continue;
                }

                anotherArray[k++] = this.arrayCartas[i];

            }
            this.arrayCartas = anotherArray;
        }

    }

    public synchronized void iniciarJogo() throws RemoteException {
        Card card;

        if (listaLogin.isEmpty()) {
            estado = "waiting";

        }
        if (estado != "waiting") {
            Deck gameDeck = new Deck();         // cria um baralho
            gameDeck.shuffle();
            int nAsh = 0, flag = 1;
            try {
                if (!estado.equalsIgnoreCase("jogar") && !estado.equalsIgnoreCase("pausar")) {
                    vezJogador = 0;
                    if (listaLogin.size() < 3) {

                        arrayCartas = new Card[listaLogin.size() + 1][2];
                        valores = new int[listaLogin.size() + 1];
                    } else {
                        arrayCartas = new Card[4][2];
                        valores = new int[4];
                    }

                    for (int i = 0; i < arrayCartas.length; i++) {
                        valores[i] = 0;
                        nAsh = 0;
                        for (int k = 0; k < arrayCartas[i].length; k++) {
                            card = gameDeck.deal();
                            arrayCartas[i][k] = card;
                            if (isNumeric(arrayCartas[i][k].getName().substring(1))) {
                                if (Integer.parseInt(arrayCartas[i][k].getName().substring(1)) != 1) {
                                    valores[i] += Integer.parseInt(arrayCartas[i][k].getName().substring(1));
                                } else {
                                    nAsh++;
                                }
                            } else {
                                valores[i] += 10;
                            }
                        }
                        if (nAsh != 0) {
                            for (int j = 0; j < nAsh; j++) {
                                int temp = valores[i];
                                temp += 11;
                                if (temp > 21) {
                                    valores[i] += 1;
                                } else {
                                    valores[i] += 11;
                                }

                            }
                        }

                    }

                    Iterator<LoginCliente> ite = listaLogin.iterator();

                    while (ite.hasNext()) {
                        LoginCliente obj2 = ite.next();

                        try {

                            obj2.getInterfaceCliente().jogar(arrayCartas, valores, estado);
                            obj2.getInterfaceCliente().mensagemGeral(8, flag);

                            if (valores[flag] == 21) {
                                stand(flag);
                                obj2.getInterfaceCliente().disableButton(flag);

                            }

                        } catch (ConcurrentModificationException e) {
                            logout(obj2.getNome(), obj2.getInterfaceCliente());
                            ite = listaLogin.iterator();
                            flag = 1;
                            while (ite.hasNext()) {
                                obj2 = ite.next();

                                obj2.getInterfaceCliente().jogar(arrayCartas, valores, estado);
                                obj2.getInterfaceCliente().mensagemGeral(8, flag);

                                if (valores[flag] >= 21) {
                                    stand(flag);
                                    obj2.getInterfaceCliente().disableButton(flag);

                                }
                                flag++;
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            newClientListSend();
                        } catch (ConnectException e) {
                            newClientListSend();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        flag++;
                    }

                }

                if (playing.length == 0) {
                    listaLogin.get(vezJogador).getInterfaceCliente().vezJogador(true, vezJogador);

                } else if (listaLogin.get(vezJogador).getInterfaceCliente() == playing[vezJogador].getInterfaceCliente()) {

                    listaLogin.get(vezJogador).getInterfaceCliente().vezJogador(true, vezJogador);
                }
                if (this.inGame) {
                    Iterator<LoginCliente> ite = listaLogin.iterator();
                    int i = 0;
                    while (ite.hasNext()) {
                        LoginCliente obj2 = ite.next();

                        try {

                            obj2.getInterfaceCliente().jogar(arrayCartas, valores, estado);

                            if (i < 3) {
                                this.playing[i] = obj2;
                            }

                        } catch (ConnectException e) {
                            logout(obj2.getNome(), obj2.interfaceCliente);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                    this.inGame = false;
                }

                this.estado = "jogar";

            } catch (ArrayIndexOutOfBoundsException e) {

                iniciarJogo();
            } catch (NullPointerException e) {
                listaLogin.get(vezJogador).getInterfaceCliente().vezJogador(true, vezJogador);

                Iterator<LoginCliente> ite = listaLogin.iterator();
                int i = 0;
                while (ite.hasNext()) {
                    LoginCliente obj2 = ite.next();

                    obj2.getInterfaceCliente().jogar(arrayCartas, valores, estado);

                    if (i < 3) {
                        this.playing[i] = obj2;
                    }

                    i++;
                }
                this.estado = "jogar";
            } catch (ConnectException e) {
                logout(listaLogin.get(vezJogador).getNome(), listaLogin.get(vezJogador).getInterfaceCliente());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized void hit(int nPlayer) throws RemoteException {
        Card card;
        int nAsh = 0;
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
        this.arrayCartas = longer;

        for (int i = 0; i < arrayCartas.length; i++) {
            valores[i] = 0;
            nAsh = 0;
            for (int k = 0; k < arrayCartas[i].length; k++) {
                if (arrayCartas[i][k] != null) {
                    if (isNumeric(arrayCartas[i][k].getName().substring(1))) {
                        if (Integer.parseInt(arrayCartas[i][k].getName().substring(1)) != 1) {
                            valores[i] += Integer.parseInt(arrayCartas[i][k].getName().substring(1));
                        } else {
                            nAsh++;
                        }
                    } else {
                        valores[i] += 10;
                    }
                }
            }

            if (nAsh != 0) {
                for (int j = 0; j < nAsh; j++) {
                    int temp = valores[i];
                    temp += 11;
                    if (temp >= 22) {
                        valores[i] += 1;
                    } else {
                        valores[i] += 11;
                    }

                }
            }

        }

        Iterator<LoginCliente> ite = listaLogin.iterator();
        int flag = 1;
        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();

            try {

                obj2.getInterfaceCliente().jogar(arrayCartas, valores, estado);
                obj2.getInterfaceCliente().mensagemGeral(2, vezJogador);
                if (flag == nPlayer) {
                    if (valores[flag] >= 21) {
                        stand(nPlayer);
                        obj2.getInterfaceCliente().disableButton(nPlayer);

                    }
                }
            } catch (ConcurrentModificationException e) {
                logout(obj2.getNome(), obj2.getInterfaceCliente());
                ite = listaLogin.iterator();
                flag = 1;
                while (ite.hasNext()) {
                    obj2 = ite.next();

                    obj2.getInterfaceCliente().jogar(arrayCartas, valores, estado);
                    obj2.getInterfaceCliente().mensagemGeral(2, vezJogador);
                    if (flag == nPlayer) {
                        if (valores[flag] >= 21) {
                            stand(nPlayer);
                            obj2.getInterfaceCliente().disableButton(nPlayer);

                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                newClientListSend();
            } catch (ConnectException e) {
                newClientListSend();
            } catch (Exception e) {
                e.printStackTrace();
            }
            flag++;
        }

        tempoThread = 20;

    }

    public synchronized static boolean isNumeric(final String str) {

        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;

    }

    public synchronized void stand(int nPlayer) throws RemoteException {

        //Sempre que um jogador faz stand incrementa o contador vezJogador e passa para a vez do próximo jogador. até o fim da rodada
        vezJogador++;

        System.out.println("projetoFinal.Servidor.stand() - numJogadoresTable: " + numJogadoresTable + " - vezJogador: " + vezJogador + " - vezJogador: ");
        if (numJogadoresTable <= vezJogador) {
            vezJogador = 0;
            novaRodada();// Depois é só descometntar essa linha, é aqui onde o servidor vai chamar a função de nova rodada e essa função vai verificar os pontos de cada utilizador
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
                obj2.getInterfaceCliente().jogar(arrayCartas, valores, estado);
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
                logout(obj2.getNome(), obj2.getInterfaceCliente());
            } catch (ConnectException e) {
                e.printStackTrace();
                logout(obj2.getNome(), obj2.getInterfaceCliente());
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                logout(obj2.getNome(), obj2.getInterfaceCliente());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try {
            listaLogin.get(vezJogador).getInterfaceCliente().vezJogador(true, vezJogador);
            listaLogin.get(vezJogador).getInterfaceCliente().jogar(arrayCartas, valores, estado);
            listaLogin.get(vezJogador).setMinhaVez(true);
            if (valores[vezJogador] == 21 && vezJogador != 0) {
                stand(nPlayer);
                listaLogin.get(vezJogador).getInterfaceCliente().mensagemGeral(8, vezJogador);

            }
            tempoThread = 20;
        } catch (ConnectException e) {
            e.printStackTrace();
            logout(listaLogin.get(vezJogador).getNome(), listaLogin.get(vezJogador).getInterfaceCliente());
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();

            stand(vezJogador);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Começa aqui a nova rodada
    public synchronized void novaRodada() throws RemoteException {

        estado = "server";
        tempoThread = 0;
        while (true) {

            if (valores[0] == 18 || valores[0] == 19 || valores[0] == 20 || valores[0] == 21 || valores[0] > 21) {
                estado = "startNewGame";
                break;

            }
            hit(0);

        }
        vezJogador = 0;
        if (valores[0] > 21) {
            estado = "startNewGame";
        }

    }

    public synchronized void sendMensagens(int codigoMensagem, int vezJogador) throws RemoteException {
        Iterator<LoginCliente> ite = listaLogin.iterator();

        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();
            try {

                obj2.getInterfaceCliente().mensagemGeral(codigoMensagem, vezJogador);

            } catch (Exception ex) {
                ex.printStackTrace();
                logout(obj2.getNome(), obj2.getInterfaceCliente());
            }
        }
    }

    public synchronized void threadTempo() {
        new Thread() {

            @Override
            public void run() {
                int timer = 5;
                boolean result = false;
                while (true) {

                    if (listaLogin.isEmpty()) {
                        estado = "waiting";
                    }
                    try {
                        if (!estado.equals("server") && !estado.equals("startNewGame") && !estado.equals("waiting")) {
                            //Manda o tempo que falta para os clientes
                            result = false;
                            Iterator<LoginCliente> ite = listaLogin.iterator();
                            while (ite.hasNext()) {
                                LoginCliente obj2 = ite.next();

                                try {
                                    obj2.getInterfaceCliente().tempo(tempoThread);
                                } catch (Exception e) {

                                }

                            }

                            Thread.sleep(1000L);// 1000L = 1000ms = 1 second

                            tempoThread--;

                            ite = listaLogin.iterator();
                            while (ite.hasNext()) {
                                LoginCliente obj2 = ite.next();
                                try {

                                    obj2.getInterfaceCliente().mensagemGeral(9, 0);
                                } catch (Exception e) {
                                    logout(obj2.getNome(), obj2.getInterfaceCliente());
                                }
                            }

                            if (listaLogin.isEmpty()) {
                                estado = "waiting";
                            } else if (estado.equals("pausa")) {
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
                        }
                        if (estado.equals("startNewGame")) {
                            if (!result) {

                                int classific = 0;
                                int id = 0;
                                int index = 0;
                                for (int i = 0; i < valores.length; i++) {
                                    if (classific <= valores[i] && valores[i] <= 21) {
                                        classific = valores[i];
                                        id = i;

                                    }
                                }
                                if (classific == 0) {
                                    result = true;
                                    sendMensagens(6, vezJogador);
                                    newClientListSend();
                                } else {
                                    result = true;

                                    sendMensagens(7, id);

                                    Iterator<LoginCliente> ite = listaLogin.iterator();

                                    if (id != 0) {
                                        listaLogin.get(id - 1).setFichas(listaLogin.get(id - 1).getFichas() + 4);
                                        int flag = 0;
                                        while (ite.hasNext()) {
                                            LoginCliente obj2 = ite.next();
                                            System.out.println(playing[flag].getNome());
                                            if (obj2.getInterfaceCliente().equals(playing[flag].getInterfaceCliente())) {
                                                System.out.println(playing[flag].getNome());
                                                try {
                                                    obj2.getInterfaceCliente().disableButton(1);
                                                    obj2.getInterfaceCliente().disableButton(2);
                                                    obj2.getInterfaceCliente().disableButton(3);

                                                    obj2.setFichas(obj2.getFichas() - 2);

                                                    if (obj2.getFichas() <= 0 && obj2.getInterfaceCliente().equals(playing[flag].getInterfaceCliente())) {
                                                        obj2.getInterfaceCliente().lost();
                                                        logout(obj2.getNome(), obj2.getInterfaceCliente());
                                                        if (listaLogin.isEmpty()) {
                                                            estado = "waiting";
                                                        }
                                                    }

                                                } catch (UnmarshalException ex) {
                                                    estado = "waiting";
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                    logout(obj2.getNome(), obj2.getInterfaceCliente());

                                                }
                                            }
                                            flag++;
                                        }

                                    } else {
                                        int flag = 0;
                                        while (ite.hasNext()) {
                                            LoginCliente obj2 = ite.next();

                                            try {

                                                obj2.setFichas(obj2.getFichas() - 2);
                                                if (obj2.getFichas() <= 0 && obj2.getInterfaceCliente().equals(playing[flag].getInterfaceCliente())) {
                                                    obj2.getInterfaceCliente().lost();
                                                    logout(obj2.getNome(), obj2.getInterfaceCliente());
                                                    if (listaLogin.isEmpty()) {
                                                        estado = "waiting";
                                                    }
                                                }

                                            } catch (UnmarshalException ex) {
                                                estado = "waiting";
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                                logout(obj2.getNome(), obj2.getInterfaceCliente());
                                            }
                                            flag++;
                                        }
                                    }
                                    newClientListSend();

                                }
                            } else {
                                if (timer != 0) {
                                    Thread.sleep(1000L);// 1000L = 1000ms = 1 second

                                    timer--;
                                } else {
                                    inGame = true;
                                    estado = "pausa";

                                    tempoThread = 10;
                                }
                            }

                        }
                        if (estado.equals("waiting")) {

                            if (!listaLogin.isEmpty()) {
                                iniciarJogo();
                                numJogadoresTable = 0;
                                arrayCartas = new Card[4][2];
                                valores = new int[4];
                                tempoThread = 20;
                                listaLogin.get(0).getInterfaceCliente().vezJogador(true, 0);
                                estado = "pausa";

                            }
                        }

                    } catch (ConcurrentModificationException e) {

                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (ConnectException e) {
                    } catch (Exception e) {

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
