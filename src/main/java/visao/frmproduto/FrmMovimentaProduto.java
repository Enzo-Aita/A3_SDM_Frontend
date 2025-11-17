package visao.frmproduto;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.MovimentaEstoque;
import modelo.Produto;
import socket.EstoqueCliente;
import visao.Mensagem;

/**
 * Formulário para movimentação de produtos no estoque
 *
 */
public class FrmMovimentaProduto extends javax.swing.JFrame {

    private MovimentaEstoque movimentaEstoque;
    private Produto objetoproduto;
    private DefaultTableModel tableModel;

    /**
     * Construtor do formulário de movimentação de produtos
     */
    public FrmMovimentaProduto() {
        initComponents();
        this.setTitle("Controle de Movimentação de Produtos");
        this.objetoproduto = new Produto();
        this.inicializarTabela();
        this.carregaTabela();
    }

    /**
     * Carrega os dados na tabela de produtos
     */
    public void carregaTabela() {
        DefaultTableModel modelo = (DefaultTableModel) this.JTableProduto.getModel();
        modelo.setNumRows(0);

        try {
            EstoqueCliente cliente = new EstoqueCliente("localhost", 12345);
            ArrayList<Produto> minhaLista = cliente.listarProdutos();

            for (Produto p : minhaLista) {
                modelo.addRow(new Object[]{
                    p.getId(),
                    p.getProduto(),
                    String.format("R$ %.2f", p.getPreco()),
                    p.getCategoria(),
                    p.getQuantidade(),
                    p.getEstoqueminimo(),
                    p.getEstoquemaximo(),
                    getStatusEstoque(p)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    /**
     * Inicializa a tabela com colunas definidas
     */
    private void inicializarTabela() {
        String[] colunas = {"ID", "Produto", "Preço", "Categoria", "Quantidade", "Estoque Mínimo", "Estoque Máximo", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTableProduto.setModel(tableModel);
    }

    /**
     * Retorna o status do estoque baseado na quantidade atual
     *
     * @param p Produto a ser verificado
     * @return Status do estoque (BAIXO, ALTO, NORMAL)
     */
    private String getStatusEstoque(Produto p) {
        if (p.getQuantidade() < p.getEstoqueminimo()) {
            return "BAIXO";
        } else if (p.getQuantidade() > p.getEstoquemaximo()) {
            return "ALTO";
        }
        return "NORMAL";
    }

    /**
     * Exibe o histórico de movimentações de um produto
     *
     * @param idProduto ID do produto
     */
    private void mostrarHistorico(int idProduto) {
        try {
            String nomeProduto = "";
            for (int i = 0; i < JTableProduto.getRowCount(); i++) {
                if ((int) JTableProduto.getValueAt(i, 0) == idProduto) {
                    nomeProduto = JTableProduto.getValueAt(i, 1).toString();
                    break;
                }
            }

            System.out.println("🔍 Buscando histórico para produto ID: " + idProduto + " - " + nomeProduto);

            EstoqueCliente cliente = new EstoqueCliente("localhost", 12345);
            List<Map<String, Object>> historico = cliente.obterHistoricoMovimentacoes(idProduto);

            System.out.println("📊 Movimentações recebidas no frontend: " + historico.size());

            // ⭐⭐ DEBUG: Mostrar os dados recebidos
            for (int i = 0; i < historico.size(); i++) {
                Map<String, Object> mov = historico.get(i);
                System.out.println("   " + (i + 1) + ". " + mov.get("tipo") + " " + mov.get("quantidade") + " - " + mov.get("data_hora"));
            }

            if (historico.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nenhuma movimentação encontrada para: " + nomeProduto,
                        "Histórico: " + nomeProduto,
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Data/Hora");
            model.addColumn("Tipo");
            model.addColumn("Quantidade");
            model.addColumn("Observação");

            for (Map<String, Object> mov : historico) {
                model.addRow(new Object[]{
                    mov.get("data_hora"),
                    mov.get("tipo"),
                    mov.get("quantidade"),
                    mov.get("observacao") != null ? mov.get("observacao") : ""
                });
            }

            JTable JBHistorico = new JTable(model);
            JBHistorico.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(JBHistorico);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Histórico: " + nomeProduto + " (" + historico.size() + " movimentações)",
                    JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar histórico: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar histórico: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cria movimentações de teste para um produto
     */
    private void criarMovimentacoesTeste(int idProduto, String nomeProduto) {
        try {
            EstoqueCliente cliente = new EstoqueCliente("localhost", 12345);

            System.out.println("🔄 Criando movimentações de teste para: " + nomeProduto);

            // Criar várias movimentações de teste
            cliente.movimentarEstoque(idProduto, 20);  // Entrada 1
            cliente.movimentarEstoque(idProduto, 15);  // Entrada 2  
            cliente.movimentarEstoque(idProduto, -10); // Saída 1
            cliente.movimentarEstoque(idProduto, -5);  // Saída 2

            JOptionPane.showMessageDialog(this,
                    "✅ 4 movimentações de teste criadas para: " + nomeProduto + "\n"
                    + "Agora tente ver o histórico novamente.",
                    "Teste Criado", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao criar movimentações teste: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Atualiza a tabela de produtos
     */
    public void atualizarTabela() {
        carregaTabela();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        JTableProduto = new javax.swing.JTable();
        JBAdicionar = new javax.swing.JButton();
        JBSubtrair = new javax.swing.JButton();
        JBHistorico = new javax.swing.JButton();
        JBCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        JTableProduto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Produto", "Preço", "Categoria", "Quantidade", "Estoque Mínimo", "Estoque Máximo", "Status"
            }
        ));
        jScrollPane1.setViewportView(JTableProduto);

        JBAdicionar.setText("Adicionar");
        JBAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBAdicionarActionPerformed(evt);
            }
        });

        JBSubtrair.setText("Subtrair");
        JBSubtrair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBSubtrairActionPerformed(evt);
            }
        });

        JBHistorico.setText("Hitórico");
        JBHistorico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBHistoricoActionPerformed(evt);
            }
        });

        JBCancelar.setText("Cancelar");
        JBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JBCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(JBAdicionar)
                .addGap(49, 49, 49)
                .addComponent(JBSubtrair)
                .addGap(53, 53, 53)
                .addComponent(JBHistorico)
                .addGap(42, 42, 42)
                .addComponent(JBCancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JBAdicionar)
                    .addComponent(JBSubtrair)
                    .addComponent(JBHistorico)
                    .addComponent(JBCancelar))
                .addGap(141, 141, 141))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_JBCancelarActionPerformed

    private void JBSubtrairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBSubtrairActionPerformed
        try {
            int linhaSelecionada = JTableProduto.getSelectedRow();
            if (linhaSelecionada == -1) {
                throw new Mensagem("Selecione um produto na tabela");
            }

            int id = Integer.parseInt(JTableProduto.getValueAt(linhaSelecionada, 0).toString());
            String nomeProduto = JTableProduto.getValueAt(linhaSelecionada, 1).toString();

            String quantidadeStr = JOptionPane.showInputDialog(this,
                    "Quantidade a remover:", "Saída do Estoque - " + nomeProduto, JOptionPane.QUESTION_MESSAGE);

            if (quantidadeStr == null || quantidadeStr.trim().isEmpty()) {
                return;
            }

            int quantidade = Integer.parseInt(quantidadeStr);

            EstoqueCliente cliente = new EstoqueCliente("localhost", 12345);
            String resultado = cliente.movimentarEstoque(id, -quantidade);
            JOptionPane.showMessageDialog(this, resultado);

            carregaTabela();

        } catch (Mensagem e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um valor numérico válido", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_JBSubtrairActionPerformed

    private void JBHistoricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBHistoricoActionPerformed
        int linhaSelecionada = JTableProduto.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para ver o histórico",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(JTableProduto.getValueAt(linhaSelecionada, 0).toString());
        mostrarHistorico(id);


    }//GEN-LAST:event_JBHistoricoActionPerformed

    private void JBAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JBAdicionarActionPerformed
        try {
            int linhaSelecionada = JTableProduto.getSelectedRow();
            if (linhaSelecionada == -1) {
                throw new Mensagem("Selecione um produto na tabela");
            }

            int id = Integer.parseInt(JTableProduto.getValueAt(linhaSelecionada, 0).toString());
            String nomeProduto = JTableProduto.getValueAt(linhaSelecionada, 1).toString();

            String quantidadeStr = JOptionPane.showInputDialog(this,
                    "Quantidade a adicionar:", "Entrada no Estoque - " + nomeProduto, JOptionPane.QUESTION_MESSAGE);

            if (quantidadeStr == null || quantidadeStr.trim().isEmpty()) {
                return;
            }

            int quantidade = Integer.parseInt(quantidadeStr);

            EstoqueCliente cliente = new EstoqueCliente("localhost", 12345);
            String resultado = cliente.movimentarEstoque(id, quantidade);

            JOptionPane.showMessageDialog(this, resultado);
            carregaTabela();

        } catch (Mensagem e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe um valor numérico válido", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_JBAdicionarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmMovimentaProduto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMovimentaProduto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMovimentaProduto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMovimentaProduto.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMovimentaProduto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBAdicionar;
    private javax.swing.JButton JBCancelar;
    private javax.swing.JButton JBHistorico;
    private javax.swing.JButton JBSubtrair;
    private javax.swing.JTable JTableProduto;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
