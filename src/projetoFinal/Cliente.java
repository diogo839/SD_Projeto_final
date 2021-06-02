/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoFinal;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author gjafa
 */
public class Cliente extends UnicastRemoteObject implements InterfaceCliente{

     public Cliente() throws RemoteException {
        super();
    }
    
    public boolean login(String nome, String ip, int porto) {

        try {
              String url = "//" + ip + ":1099/" + "Blackjack";

        InterfaceServidor objRemoto = (InterfaceServidor) Naming.lookup(url);
        Cliente cliente = new Cliente();
        
        objRemoto.login(nome, cliente);
        
        return true;
            
        } catch (Exception e) {
            return false;
        }
        
      

    }

}
