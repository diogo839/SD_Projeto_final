/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoFinal;

import cliente.Card;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 *
 * @author gjafa
 */
public interface InterfaceCliente extends Remote{
    
    public void NewListaUsers(LoginCliente[] lista)throws RemoteException;
    public void jogar(Card[][] cartas, int []valores,String estado)throws RemoteException;
      public void mensagemGeral(int codigo, int vezJogador, String nome, Date date)throws RemoteException;
      public void vezJogador(boolean minhaVez, int posicaoQuadro)throws RemoteException;
       public void tempo(int tempo)throws RemoteException;
        public void disableButton(int nPlayer)throws RemoteException;
        public void lost()throws RemoteException;
    
}
