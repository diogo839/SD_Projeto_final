/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoFinal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 *
 * @author gjafa
 */
public interface InterfaceServidor extends Remote{
    
     public LoginCliente login(String nome, InterfaceCliente clinte) throws RemoteException;
     public LoginCliente[] getJogadores()throws RemoteException;
      public void  hit(int nPlayer)throws RemoteException;
      public void stand(int nPlayer)throws RemoteException;
     public void logout(String nome, InterfaceCliente iCliente) throws RemoteException;
 }
