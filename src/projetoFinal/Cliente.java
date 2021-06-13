/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoFinal;

import cliente.Card;
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
    ClientFrame cliFrame;
    private LoginCliente user;
    public Cliente(ClientFrame clientFrame) throws RemoteException {
       
        super();
         this.cliFrame = clientFrame;
    }
 
    public  InterfaceServidor login(String nome, String ip, int porto) {

        try {
              String url = "//" + ip + ":"+porto+"/" + "Blackjack";

        objRemoto = (InterfaceServidor) Naming.lookup(url);
       // Cliente cliente = new Cliente(cliFrame);
        
        user=objRemoto.login(nome, this);
       
               
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
    
    public void mensagemGeral(int tipo){
        cliFrame.mensagemGeral(tipo);
    }
    
 
    
//    public void mensagem(int Codmensagem){
//        cliFrame.mensagem(Codmensagem);
//        System.out.println(Codmensagem);
//    }

    @Override
    public void jogar(Card[][] cartas) throws RemoteException {
        
    cliFrame.jogar(cartas);
    }
    
    public void hit(int nPlayer)throws RemoteException{
        objRemoto.hit(nPlayer);
    }
    
    
}
