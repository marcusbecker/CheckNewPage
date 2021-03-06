/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mvbos.cnp;

import br.com.mvbos.cnp.model.CadastroURL;
import br.com.mvbos.cnp.model.IUpadate;
import br.com.mvbos.cnp.model.TableModel;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

/**
 *
 * @author mbecker
 */
public class Window extends javax.swing.JFrame implements IUpadate {

    private final TableModel tableModel = new TableModel();
    private JFrame window;
    private TrayIcon trayIcon;
    private SystemTray systemTray;

    /**
     * Creates new form Window
     */
    public Window() {
        create();
    }

    private void create() {
        window = this;
        initComponents();
        JToolBar toolBar = new JToolBar();
        toolBar.setMinimumSize(new Dimension(100, 100));
        this.add(toolBar);

        addEvent();
        systemTray = createSysTray();
        trayIcon = createTrayIcon(this);

        tableModel.addCadastro(Application.load());

        Application.startAutoCheck(tableModel.getValues());
        Application.update = this;
        lblTimeCheck.setText(Application.getMinutosVerificacao() + Strings.MINUTOS);;
        swork.execute();
    }

    private void addEvent() {
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    //int column = target.getSelectedColumn();

                    CadastroURL c = tableModel.getCliente(row);
                    String nURL;
                    if (c.isNovo()) {
                        nURL = Application.decodeURL(c.getUrl(), c.getContadorProximo());
                        Application.proximoLink(c);
                        tableModel.fireTableRowsUpdated(row, row);
                    } else {
                        nURL = Application.decodeURL(c.getUrl(), c.getContadorAtual());
                    }
                    Application.abrirURL(nURL);
                }
            }
        });
    }

    private SystemTray createSysTray() {
        if (SystemTray.isSupported()) {
            return SystemTray.getSystemTray();
        } else {
            return null;
        }
    }
    
    private static TrayIcon createTrayIcon(final Window w) {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return null;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon
                = new TrayIcon((new ImageIcon(Window.class.getResource("img/lupa.png"), "tray icon")).getImage());

        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem sobre = new MenuItem(Strings.SOBRE);
        MenuItem verificar = new MenuItem(Strings.VERIFICAR);
        MenuItem sair = new MenuItem(Strings.SAIR);

        popup.add(verificar);
        popup.add(sobre);
        popup.addSeparator();
        popup.add(sair);

        trayIcon.setPopupMenu(popup);
        trayIcon.setToolTip(Strings.TITLE);
        //trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return null;
        }

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                w.setVisible(true);
                w.setExtendedState(JFrame.NORMAL);
                w.requestFocus();
            }
        });
        verificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                w.verificar();
            }
        });

        sobre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, Strings.VERSAO);
            }
        });

        sair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                w.fechar();
            }
        });

        return trayIcon;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        tfUrl = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tfVarNumero = new javax.swing.JTextField();
        btnVerificar = new javax.swing.JButton();
        jToolBar = new javax.swing.JToolBar();
        lblNextCheck = new javax.swing.JLabel();
        lblTimeCheck = new javax.swing.JLabel();
        lblDesc = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuVerAuto = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuExcluir = new javax.swing.JMenuItem();
        mnuFechar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Vê se atualizou! (v0.1)");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });

        tabela.setModel(tableModel);
        tabela.setToolTipText("Clique 2x para abrir navegador.");
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabela);

        tfUrl.setText("http://www.");

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jLabel1.setText("@n");

        tfVarNumero.setText("1");

        btnVerificar.setText("Verificar");
        btnVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerificarActionPerformed(evt);
            }
        });

        jToolBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jToolBar.setFloatable(false);

        lblNextCheck.setText("Próxima verificação em:");
        jToolBar.add(lblNextCheck);

        lblTimeCheck.setText(" 0000000");
        jToolBar.add(lblTimeCheck);

        lblDesc.setText("URL");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("https://github.com/marcusbecker/CheckNewPage");

        jMenu1.setText("Aplicativo");

        mnuVerAuto.setText("Tempo");
        mnuVerAuto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVerAutoActionPerformed(evt);
            }
        });
        jMenu1.add(mnuVerAuto);
        jMenu1.add(jSeparator1);

        mnuExcluir.setText("Excluir registro");
        mnuExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExcluirActionPerformed(evt);
            }
        });
        jMenu1.add(mnuExcluir);

        mnuFechar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        mnuFechar.setText("Fechar");
        mnuFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuFecharActionPerformed(evt);
            }
        });
        jMenu1.add(mnuFechar);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tfUrl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdd))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfVarNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 532, Short.MAX_VALUE)
                        .addComponent(btnVerificar))
                    .addComponent(lblDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfVarNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVerificar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDesc)
                .addGap(15, 15, 15)
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        CadastroURL c = new CadastroURL(tfUrl.getText(), Integer.valueOf(tfVarNumero.getText()));

        if (Application.validar(c)) {
            tableModel.addCadastro(c);
            Application.save(tableModel.getValues());
            Application.verificar(c);
            tableModel.fireTableDataChanged();

        } else {
            lblDesc.setText(Strings.URL_INVALIDA);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerificarActionPerformed
        verificar();
    }//GEN-LAST:event_btnVerificarActionPerformed

    private void mnuFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuFecharActionPerformed
        fechar();
    }//GEN-LAST:event_mnuFecharActionPerformed

    private void mnuExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExcluirActionPerformed
        if (tabela.getSelectedRow() >= 0) {
            tableModel.removeCadastro(tabela.getSelectedRow());
        }
    }//GEN-LAST:event_mnuExcluirActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Application.save(tableModel.getValues());
    }//GEN-LAST:event_formWindowClosing

    private void mnuVerAutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVerAutoActionPerformed
        JDialog dlg = new JDialog(window, Strings.TEMPO_DE_VERIFICACAO);
        SetTimeCheck pn = new SetTimeCheck();
        pn.setTime(Application.getMinutosVerificacao());

        dlg.setContentPane(pn);
        dlg.pack();
        dlg.setModal(true);
        dlg.setLocationRelativeTo(window);
        dlg.setVisible(true);

        Application.setMinutosVerificacao(pn.getTime());

    }//GEN-LAST:event_mnuVerAutoActionPerformed

    private void tabelaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaMouseClicked
        if (tabela.getSelectedRow() >= 0) {
            lblDesc.setText(Application.decodeURL(
                    tableModel.getCliente(tabela.getSelectedRow()).getUrl(),
                    tableModel.getCliente(tabela.getSelectedRow()).getContadorAtual()));

            Application.save(tableModel.getValues());
        }
    }//GEN-LAST:event_tabelaMouseClicked

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        /*if (evt.getNewState() == MAXIMIZED_BOTH) {
         systemTray.remove(trayIcon);
         setVisible(true);
         }*/

        if (evt.getNewState() == ICONIFIED) {
            try {
                if (systemTray.getTrayIcons().length == 0) {
                    systemTray.add(trayIcon);
                }
                setVisible(false);
            } catch (AWTException ex) {
            }
        }

        if (evt.getNewState() == NORMAL) {
            /* systemTray.remove(trayIcon);
             setVisible(true);*/
        }
    }//GEN-LAST:event_formWindowStateChanged
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnVerificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar;
    private javax.swing.JLabel lblDesc;
    private javax.swing.JLabel lblNextCheck;
    private javax.swing.JLabel lblTimeCheck;
    private javax.swing.JMenuItem mnuExcluir;
    private javax.swing.JMenuItem mnuFechar;
    private javax.swing.JMenuItem mnuVerAuto;
    private javax.swing.JTable tabela;
    private javax.swing.JTextField tfUrl;
    private javax.swing.JTextField tfVarNumero;
    // End of variables declaration//GEN-END:variables

    private void fechar() {
        Application.save(tableModel.getValues());
        this.dispose();
        System.exit(0);
    }

    @Override
    public void update() {
        tableModel.fireTableDataChanged();
        lblTimeCheck.setText(null);

        for (CadastroURL c : tableModel.getValues()) {
            if (c.isNovo()) {
                trayIcon.displayMessage(Strings.NOVA_PAGINA,
                        Application.decodeURL(c.getUrl(), c.getContadorProximo()), TrayIcon.MessageType.INFO);

                break;
            }
        }
    }
    SwingWorker<Void, Void> swork = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            while (Application.autoCheck) {
                if (Application.nextCheck - System.currentTimeMillis() - 1000 > 0) {
                    lblTimeCheck.setText(" " + Application.timeFormat.format(
                            new Date(Application.nextCheck - System.currentTimeMillis())));
                } else {
                    lblTimeCheck.setText(Strings.AGORA);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }

            return null;
        }
    };

    private void verificar() {
        for (CadastroURL c : tableModel.getValues()) {
            Application.verificar(c);
        }
        tableModel.fireTableDataChanged();
        Application.setMinutosVerificacao(Application.getMinutosVerificacao());
    }
}
