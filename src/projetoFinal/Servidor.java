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
    
     public Servidor() throws RemoteException {
        super();
        listaLogin = new Vector<LoginCliente>();
    }
    
    @Override
    public synchronized LoginCliente login(String nome, InterfaceCliente cliente)throws RemoteException{
     
            LoginCliente loginCliente = new LoginCliente(nome,cliente);
            this.listaLogin.add(loginCliente);
            System.out.println("nome do utilizador: " + nome);
            newClientListSend();
         return loginCliente;
    }
    
    
    public synchronized LoginCliente[] getJogadores()throws RemoteException{
        if (listaLogin != null) {
            LoginCliente[] array = listaLogin.toArray(new LoginCliente[listaLogin.size()]);
       
            return array;
        }
        return null;
        
    }
    
    public synchronized void newClientListSend() throws RemoteException{
        Iterator<LoginCliente> ite = listaLogin.iterator();
           while (ite.hasNext()) {
             LoginCliente obj2 = ite.next();
               try {
                System.out.println("nome do utilizador: " + obj2.getNome());
                obj2.getInterfaceCliente().NewListaUsers(listaLogin.toArray(new LoginCliente[listaLogin.size()]));
            } 
            catch (Exception ex) {
               logout(obj2.getNome(),obj2.getInterfaceCliente());
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
    
}
