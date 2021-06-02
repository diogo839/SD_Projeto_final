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
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author gjafa
 */
public class Servidor extends UnicastRemoteObject implements InterfaceServidor {
    
    private Vector<LoginCliente> listaLogin;
    
     public Servidor(Vector<LoginCliente> listaLogin) throws RemoteException {
        super();
        this.listaLogin = listaLogin;
    }
    
    @Override
    public boolean login(String nome, InterfaceCliente cliente){
       
            LoginCliente loginCliente = new LoginCliente(cliente);
            this.listaLogin.add(loginCliente);
            System.out.println("nome do utilizador: " + nome);

         return true;
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {

            System.out.println("IP do servidor RMI: " + InetAddress.getLocalHost().getHostAddress());
            Vector<LoginCliente> listaLogin = new Vector<LoginCliente>();
            Servidor blackjack = new Servidor(listaLogin);

            Registry reg = LocateRegistry.createRegistry(1099);
            reg.rebind("Blackjack", blackjack);

            System.out.println("servidor RMI iniciado");

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
