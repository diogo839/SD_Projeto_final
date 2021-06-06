/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoFinal;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.Vector;

/**
 *
 * @author gjafa
 */
public class Cliente extends UnicastRemoteObject implements InterfaceCliente{
     
    InterfaceServidor objRemoto;
    ClientFrame cliFrame= new ClientFrame();
    private LoginCliente user;
    public Cliente() throws RemoteException {
        
        super();
    }
 
    public  InterfaceServidor login(String nome, String ip, int porto) {

        try {
              String url = "//" + ip + ":"+porto+"/" + "Blackjack";

        objRemoto = (InterfaceServidor) Naming.lookup(url);
        Cliente cliente = new Cliente();
        
        user=objRemoto.login(nome, cliente);
       
               
        return objRemoto;
            
        } catch (Exception e) {
            return null;
        }
        
      

    }
    
    public void NewListaUsers(LoginCliente[] lista)throws RemoteException{
          
       cliFrame.listar(lista);
    }
    
    
    public void logout() throws RemoteException{
        objRemoto.logout(user.getNome(),user.getInterfaceCliente());
    }

    
    public LoginCliente[] GetListaUsers()throws RemoteException{
        return objRemoto.getJogadores();
    }
}
