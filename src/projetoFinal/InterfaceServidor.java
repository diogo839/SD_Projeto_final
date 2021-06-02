/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoFinal;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author gjafa
 */
public interface InterfaceServidor extends Remote{
    
     public boolean login(String nome, InterfaceCliente clinte) throws RemoteException;
    
}
