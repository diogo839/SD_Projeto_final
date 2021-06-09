/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoFinal;

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

    private String estado;

    public Servidor() throws RemoteException {
        super();
        listaLogin = new Vector<LoginCliente>();

        this.estado = "espera";
    }

    @Override
    public synchronized LoginCliente login(String nome, InterfaceCliente cliente) throws RemoteException {

        LoginCliente loginCliente;

        if (listaLogin.isEmpty()) {
            threadIniciarJogo();
        }

        if (listaLogin.size() < 3) {
            loginCliente = new LoginCliente(nome, cliente, "jogo");

        } else {
            loginCliente = new LoginCliente(nome, cliente, "espera");

        }

        this.listaLogin.add(loginCliente);
        System.out.println("nome do utilizador: " + nome);
        System.out.println("numeroo de utilizadofre: " + listaLogin.size());
        newClientListSend();
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
        System.out.println(listaLogin.size());
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

        while (ite.hasNext()) {
            LoginCliente obj2 = ite.next();
            if (obj2.getNome().equalsIgnoreCase(nome) && obj2.getInterfaceCliente().equals(iCliente)) {
                ite.remove();
            }
        }
        newClientListSend();

    }

    public static void main(String[] args) {

        try {

            System.out.println("IP do servidor RMI: " + InetAddress.getLocalHost().getHostAddress());
            Servidor blackjack = new Servidor();

            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("Blackjack", blackjack);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void iniciarJogo() {

        try {
            if (listaLogin.size() >= 3) {
                listaLogin.get(2).getInterfaceCliente().jogar(3);
            } else if (listaLogin.size() == 2) {
                listaLogin.get(1).getInterfaceCliente().jogar(2);
            } else if (listaLogin.size() == 1) {
                listaLogin.get(0).getInterfaceCliente().jogar(1);
            }

            sendMensagens(1);
        } catch (Exception e) {
            System.out.println("projetoFinal.Servidor.iniciarJogo()" + e);
        }

    }

    public synchronized void sendMensagens(int codigoMensagem) throws RemoteException {
        Iterator<LoginCliente> ite = listaLogin.iterator();
        System.out.println(listaLogin.size());
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

    public void threadIniciarJogo() {
        new Thread() {

            @Override
            public void run() {

                int iniciar = 20;

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

                estado = "jogar";
                iniciarJogo();

            }
        }.start();

    }

}
