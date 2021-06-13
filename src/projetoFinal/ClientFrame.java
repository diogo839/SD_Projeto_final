package projetoFinal;

import cliente.Card;
import cliente.CardLabel;
import cliente.Deck;
import java.awt.Container;
import java.awt.Label;
import javax.swing.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import projetoFinal.Cliente;
import projetoFinal.LoginCliente;

public class ClientFrame extends JFrame {

    private Card card;
    private CardLabel cardLabel;
    private Deck gameDeck;
    private Cliente cliente;
    private LoginCliente user;
    Vector<LoginCliente> lista;
    private DefaultListModel listMensagem;
     private Vector<LoginCliente> listaTresJogadores;
     private Card[][] cardArray;
     private int[] arrayPositionX={270,130,300,600};
     private int[] arrayPositionY={150,350};

     private int jogador;

    public ClientFrame() {

        initComponents();
        lista = new Vector<LoginCliente>();
        this.LoginPanel.setVisible(true);
        this.TablePanel.setVisible(false);
        this.listMensagem = new DefaultListModel();
        listaTresJogadores = new Vector<LoginCliente>();
        gameDeck = new Deck();         // cria um baralho
        gameDeck.shuffle();            // embaralha


    }



    // apresenta as 13 primeiras cartas do deck (para teste apenas)
    private void drawHand() {

        CardLabel[] myCardLabels = new CardLabel[13];
        List<CardLabel> labelList = new ArrayList<CardLabel>();

        for (int i = 0; i < 13; i++) {
            myCardLabels[i] = new CardLabel();            // cria o label da carta (vazio)
            this.TablePanel.add(myCardLabels[i]);         // adiciona a carta no panel da mesa
        }
        Card[] myHand = gameDeck.getHand();   // obtem as cartas 

        // para cada carta
        for (int i = 0; i < myHand.length; i++) {
            labelList.add(myCardLabels[i]);                     // adiciona o label numa lista    
            myCardLabels[i].setCardImage(myHand[i].getName());  // define a imagem da carta

        }

        // posiciona as cartas 
        boolean covered = true;
        int nCards = labelList.size();
        for (int i = 0; i < nCards; i++) {
            if (i == (nCards - 1)) {
                covered = false;  // a última é totalmente visivel
            }
            labelList.get(i).setCardCovered(covered);
            labelList.get(i).setLocation(200 + i * 20, 20);
        }
    }

    private void mensagemErro(String mensagem) {
        JOptionPane.showMessageDialog(rootPane, mensagem, "Mensagem de erro", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textField1 = new java.awt.TextField();
        textField2 = new java.awt.TextField();
        label4 = new java.awt.Label();
        TablePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaJogador = new javax.swing.JList<>();
        sair = new java.awt.Button();
        label1 = new java.awt.Label("");
        label2 = new java.awt.Label();
        label3 = new java.awt.Label();
        jLabel4 = new javax.swing.JLabel();
        JScrollPane2 = new javax.swing.JScrollPane();
        listaMensagem = new javax.swing.JList<>();
        jLabel5 = new javax.swing.JLabel();
        hitJogador2 = new javax.swing.JButton();
        hitJogador1 = new javax.swing.JButton();
        hitJogador3 = new javax.swing.JButton();
        standJogador3 = new javax.swing.JButton();
        standJogador2 = new javax.swing.JButton();
        standJogador1 = new javax.swing.JButton();
        pontos1 = new java.awt.Label();
        pontos2 = new java.awt.Label();
        pontos3 = new java.awt.Label();
        LoginPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ipTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        portoTextField = new javax.swing.JTextField();
        nomeTextField = new javax.swing.JTextField();
        JbuttonEntrar = new javax.swing.JButton();

        textField1.setText("textField1");

        textField2.setText("textField2");

        label4.setText("label4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Card Game GUI");
        setResizable(false);
        setSize(new java.awt.Dimension(700, 526));
        getContentPane().setLayout(new java.awt.CardLayout());

        TablePanel.setBackground(new java.awt.Color(0, 153, 0));

        jScrollPane1.setViewportView(listaJogador);

        sair.setLabel("sair");
        sair.setLabel("sair");
        sair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sairActionPerformed(evt);
            }
        });

        label1.setAlignment(java.awt.Label.CENTER);
        label1.setText("label1");

        label2.setAlignment(java.awt.Label.CENTER);

        label3.setAlignment(java.awt.Label.CENTER);

        jLabel4.setText("Observadores");

        JScrollPane2.setViewportView(listaMensagem);

        jLabel5.setText("Mensagens");

        hitJogador2.setText("Hit");
        hitJogador2.setEnabled(false);

        hitJogador1.setText("Hit");
        hitJogador1.setEnabled(false);
        hitJogador1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hitJogador1ActionPerformed(evt);
            }
        });

        hitJogador3.setText("Hit");
        hitJogador3.setEnabled(false);

        standJogador3.setText("Stand");
        standJogador3.setEnabled(false);
        standJogador3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                standJogador3ActionPerformed(evt);
            }
        });

        standJogador2.setText("Stand");
        standJogador2.setEnabled(false);
        standJogador2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                standJogador2ActionPerformed(evt);
            }
        });

        standJogador1.setText("Stand");
        standJogador1.setEnabled(false);
        standJogador1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                standJogador1ActionPerformed(evt);
            }
        });

        pontos1.setText("");

        pontos2.setText("");

        pontos3.setText("");

        javax.swing.GroupLayout TablePanelLayout = new javax.swing.GroupLayout(TablePanel);
        TablePanel.setLayout(TablePanelLayout);
        TablePanelLayout.setHorizontalGroup(
            TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TablePanelLayout.createSequentialGroup()
                .addGap(519, 519, 519)
                .addGroup(TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TablePanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(JScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(36, 36, 36))
                    .addGroup(TablePanelLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(hitJogador3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(standJogador3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 332, Short.MAX_VALUE))))
            .addGroup(TablePanelLayout.createSequentialGroup()
                .addGroup(TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TablePanelLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(hitJogador1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(standJogador1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95)
                        .addComponent(hitJogador2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(standJogador2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(TablePanelLayout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(140, 140, 140)
                        .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(149, 149, 149)
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TablePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TablePanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(75, 75, 75))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TablePanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(80, 80, 80))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TablePanelLayout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(pontos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(170, 170, 170)
                .addComponent(pontos2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(187, 187, 187)
                .addComponent(pontos3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sair, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
        );
        TablePanelLayout.setVerticalGroup(
            TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TablePanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TablePanelLayout.createSequentialGroup()
                        .addGroup(TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sair, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pontos2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pontos3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addGroup(TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(TablePanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(hitJogador1)
                                    .addComponent(standJogador1))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TablePanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                                .addGroup(TablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(hitJogador2)
                                    .addComponent(standJogador2)
                                    .addComponent(hitJogador3)
                                    .addComponent(standJogador3))
                                .addGap(25, 25, 25))))
                    .addGroup(TablePanelLayout.createSequentialGroup()
                        .addComponent(pontos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        getContentPane().add(TablePanel, "card2");

        LoginPanel.setEnabled(false);

        jLabel1.setText("Porto:");

        jLabel2.setText("Nome:");

        jLabel3.setText("IP:");

        JbuttonEntrar.setText("Entrar");
        JbuttonEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JbuttonEntrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LoginPanelLayout = new javax.swing.GroupLayout(LoginPanel);
        LoginPanel.setLayout(LoginPanelLayout);
        LoginPanelLayout.setHorizontalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGroup(LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LoginPanelLayout.createSequentialGroup()
                        .addGap(249, 249, 249)
                        .addGroup(LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(36, 36, 36)
                        .addGroup(LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(portoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(LoginPanelLayout.createSequentialGroup()
                        .addGap(343, 343, 343)
                        .addComponent(JbuttonEntrar)))
                .addContainerGap(639, Short.MAX_VALUE))
        );
        LoginPanelLayout.setVerticalGroup(
            LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LoginPanelLayout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addGroup(LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ipTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(portoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(LoginPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(JbuttonEntrar)
                .addContainerGap(280, Short.MAX_VALUE))
        );

        getContentPane().add(LoginPanel, "card3");
        LoginPanel.getAccessibleContext().setAccessibleName("LoginPanel");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sairActionPerformed
        try {
            cliente.logout();
            System.exit(0);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_sairActionPerformed

    private void JbuttonEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JbuttonEntrarActionPerformed
        String ip = this.ipTextField.getText();
        String portoString = this.portoTextField.getText();
        String nome = this.nomeTextField.getText();
        boolean entrar = true;
        int porto = 0;

        try {
            cliente = new Cliente(this);
        } catch (RemoteException e) {
            mensagemErro("Houve um erro!!");

        }

        if (ip.equalsIgnoreCase("")) {
            mensagemErro("Introduza o ip!!");

            entrar = false;
        }

        if (portoString.equalsIgnoreCase("")) {
            mensagemErro("Introduza o porto!!");

            entrar = false;
        }

        if (nome.equalsIgnoreCase("")) {
            mensagemErro("Introduza o nome!!");
            entrar = false;
        }

        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";

        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(ip);

        if (!m.matches()) {
            mensagemErro("O endereço IP não é válido!!\nVolte a Introduzir.");

            this.ipTextField.setText("");
            entrar = false;

        }

        m = Pattern.compile("\\d{0,5}").matcher(portoString);

        if (!m.matches()) {
            mensagemErro("O porto não é válido!!\nVolte a Introduzir.");
            entrar = false;

        }

        try {
            porto = Integer.parseInt(portoString);
        } catch (NumberFormatException e) {
            entrar = false;
        }

        if (entrar) {

            cliente.login(nome, ip, porto);

            this.LoginPanel.setVisible(false);
            this.TablePanel.setVisible(true);

        }
    }//GEN-LAST:event_JbuttonEntrarActionPerformed

    private void standJogador3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_standJogador3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_standJogador3ActionPerformed

    private void standJogador2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_standJogador2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_standJogador2ActionPerformed

    private void standJogador1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_standJogador1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_standJogador1ActionPerformed

    private void hitJogador1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hitJogador1ActionPerformed

        try {
            cliente.hit(1);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }//GEN-LAST:event_hitJogador1ActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane JScrollPane2;
    private javax.swing.JButton JbuttonEntrar;
    private javax.swing.JPanel LoginPanel;
    private javax.swing.JPanel TablePanel;
    private javax.swing.JButton hitJogador1;
    private javax.swing.JButton hitJogador2;
    private javax.swing.JButton hitJogador3;
    private javax.swing.JTextField ipTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    java.awt.Label label1;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private static javax.swing.JList<String> listaJogador;
    private javax.swing.JList<String> listaMensagem;
    private javax.swing.JTextField nomeTextField;
    private java.awt.Label pontos1;
    private java.awt.Label pontos2;
    private java.awt.Label pontos3;
    private javax.swing.JTextField portoTextField;
    private java.awt.Button sair;
    private javax.swing.JButton standJogador1;
    private javax.swing.JButton standJogador2;
    private javax.swing.JButton standJogador3;
    private java.awt.TextField textField1;
    private java.awt.TextField textField2;
    // End of variables declaration//GEN-END:variables

    public void listar(LoginCliente[] lista) {

        DefaultListModel listModel = new DefaultListModel();

        for (int i = 0; i < lista.length; i++) {
           
            switch (i) {
                case 0:

                    label1.setText(lista[i].getNome());
                    pontos1.setText(String.valueOf(lista[i].getFichas()));
                    standJogador1.setVisible(true);
                    hitJogador1.setVisible(true);
                    break;
                case 1:
                    label2.setText(lista[i].getNome());
                    pontos2.setText(String.valueOf(lista[i].getFichas()));
                    standJogador2.setVisible(true);
                    hitJogador2.setVisible(true);
                    break;
                case 2:
                    label3.setText(lista[i].getNome());
                    pontos3.setText(String.valueOf(lista[i].getFichas()));
                    standJogador3.setVisible(true);
                    hitJogador3.setVisible(true);
                    break;
                default:
                    listModel.addElement(lista[i].getNome());

            }
            
          

        }
        
        //listaTresJogadores.add(user)
              Collections.addAll(listaTresJogadores, lista);

        if (listaTresJogadores.size() >= 3) {
            jogador = 2;
        } else if (listaTresJogadores.size() < 3) {
            label3.setText(" ");
            pontos3.setText(" ");
            standJogador3.setVisible(false);
            hitJogador3.setVisible(false);
            if (listaTresJogadores.size() < 2) {
                label2.setText("");
                pontos2.setText("");
                standJogador2.setVisible(false);
                hitJogador2.setVisible(false);
            }
        } else {
            jogador = listaTresJogadores.size() - 1;
        }

        listMensagem.addElement("O jogador " + lista[lista.length - 1].getNome() + " entrou no jogo!!");
       

        listaMensagem.setModel(listMensagem);
        listaJogador.setModel(listModel);
          repaint();

    }

    public void jogar(Card[][] cartas) {
        this.TablePanel.revalidate();

        this.TablePanel.repaint();
           // System.out.println(cartas.length);
        hitJogador1.setEnabled(true);
        standJogador1.setEnabled(true);
        hitJogador2.setEnabled(false);
        standJogador2.setEnabled(false);
        hitJogador3.setEnabled(false);
        standJogador3.setEnabled(false);
            cardArray=cartas;
            int espaco=0;
        for (int i = 0; i < cartas.length; i++) {
             for (int k = 0; k < cartas[i].length; k++) {
                if(cartas[i][k]!=null) {
                 if(i==0){
                     if(k==0){
                    cardLabel = new CardLabel();
                    card = gameDeck.deal();
                    cardLabel.setCardImage("bv");
                    cardLabel.setCardCovered(false);
                    cardLabel.setLocation(this.arrayPositionX[i], this.arrayPositionY[i]);
                    this.TablePanel.add(cardLabel);
                     }else{
                  cardLabel = new CardLabel();
                    card = gameDeck.deal();
                    cardLabel.setCardImage(cartas[i][k].getName());
                    cardLabel.setCardCovered(false);
                    cardLabel.setLocation(this.arrayPositionX[0]+k*20, this.arrayPositionY[0]);
                    this.TablePanel.add(cardLabel);}
                 }else{
                     if(k==0){
                         espaco=0;
                     }else{
                         espaco=k*20;
                     }
                    
                    cardLabel = new CardLabel();
                    card = gameDeck.deal();
                    cardLabel.setCardImage(cartas[i][k].getName());
                    cardLabel.setCardCovered(false);
                    cardLabel.setLocation(this.arrayPositionX[i]+espaco, this.arrayPositionY[1]);
                    this.TablePanel.add(cardLabel);
                 }}


        }}
        repaint();
       

    }

    public void mensagemGeral(int codMensagem) {
       

     
        switch (codMensagem) {
            case 0:

                 listMensagem.addElement("Faltam 5 segundos para o jogo começar");

                break;
            case 1:
                
                listMensagem.addElement("Vez do jogador " + listaTresJogadores.get(jogador).getNome());

                break;
            case 2:
                listMensagem.addElement(codMensagem);
                break;
            default:
                listMensagem.addElement(codMensagem);

        }

        listaMensagem.setModel(listMensagem);
    }

}
