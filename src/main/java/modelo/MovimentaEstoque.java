package modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa uma movimentação de estoque de produtos
 *
 *
 *
 */
public class MovimentaEstoque implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomeProduto;
    private Date dataMovimentacao;
    private int quantidade;
    private String tipo; // ENTRADA ou SAIDA

    /**
     * Construtor padrão
     */
    public MovimentaEstoque() {
    }

    /**
     * Construtor completo
     *
     * @param nomeProduto Nome do produto movimentado
     * @param dataMovimentacao Data da movimentação
     * @param quantidade Quantidade movimentada
     * @param tipo Tipo de movimentação (ENTRADA/SAIDA)
     */
    public MovimentaEstoque(String nomeProduto, Date dataMovimentacao, int quantidade, String tipo) {
        this.nomeProduto = nomeProduto;
        this.dataMovimentacao = dataMovimentacao;
        this.quantidade = quantidade;
        this.tipo = tipo;
    }

    /**
     * @return Nome do produto
     */
    public String getNomeProduto() {
        return nomeProduto;
    }

    /**
     * @param nomeProduto Nome do produto
     */
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    /**
     * @return Data da movimentação
     */
    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    /**
     * @param dataMovimentacao Data da movimentação
     */
    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    /**
     * @return Quantidade movimentada
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade Quantidade movimentada
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * @return Tipo de movimentação (ENTRADA/SAIDA)
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo Tipo de movimentação (ENTRADA/SAIDA)
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Movimentacao{"
                + "nomeProduto='" + nomeProduto + '\''
                + ", dataMovimentacao=" + dataMovimentacao
                + ", quantidade=" + quantidade
                + ", tipo='" + tipo + '\''
                + '}';
    }
}
