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
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author gjafa
 */
public class Cliente extends UnicastRemoteObject implements InterfaceCliente{
     
    InterfaceServidor objRemoto;
    ClientFrame cliFrame;
    public LoginCliente user;
    public Cliente(ClientFrame clientFrame) throws RemoteException {
       
        super();
         this.cliFrame = clientFrame;
    }
 
    public Cliente()throws RemoteException{
        super();
    }
    
    public int login(String nome, String ip, int porto) {

        try {
              String url = "//" + ip + ":"+porto+"/" + "Blackjack";

        objRemoto = (InterfaceServidor) Naming.lookup(url);
       // Cliente cliente = new Cliente(cliFrame);
        
        user=objRemoto.login(nome, this);
        
            if (user != null) {
                return 1; 
            }else{
                return 0;
            }
      
            
        } catch (Exception e) {
            return -1;
        }
        
      

    }
    
    public synchronized void NewListaUsers(LoginCliente[] lista)throws RemoteException{
        
        
       cliFrame.listar(lista);
       
        
    }
    
    
    public void logout() throws RemoteException{
        objRemoto.logout(user.getNome(),user.getInterfaceCliente());
    }

    
    public LoginCliente[] GetListaUsers()throws RemoteException{
        return objRemoto.getJogadores();
    }
    
    public synchronized void mensagemGeral(int codigo, int vezJogador, String nome, Date date){
       
        cliFrame.mensagemGeral(codigo, vezJogador, nome, date);
        
        
    }
    
 
    
//    public void mensagem(int Codmensagem){
//        cliFrame.mensagem(Codmensagem);
//        System.out.println(Codmensagem);
//    }

    @Override
    public synchronized void jogar(Card[][] cartas,int[] valores,String estado) throws RemoteException {
        
    cliFrame.jogar(cartas,valores,estado);
    }
    
    public void hit(int nPlayer)throws RemoteException{
        objRemoto.hit(nPlayer, true);
    }
    
    public void stand(int nPlayer)throws RemoteException{
        objRemoto.stand(nPlayer, false);
    }
    
    public  synchronized void vezJogador(boolean minhaVez, int posicaoQuadro)throws RemoteException{
        cliFrame.setVezJogador(minhaVez, posicaoQuadro);
    }
    
     public synchronized void tempo(int tempo)throws RemoteException{
         cliFrame.tempo(tempo);
     }
    
     public synchronized void disableButton(int nPlayer)throws RemoteException{
        cliFrame.disableButton(nPlayer);
    }
     
        public synchronized void lost()throws RemoteException{
        cliFrame.lost();
    }
     
//      public void vezJogador(int vezJogador)throws RemoteException{
//          
//      }
    
    
}
