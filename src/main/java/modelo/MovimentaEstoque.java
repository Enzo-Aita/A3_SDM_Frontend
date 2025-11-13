// Documentado para javadoc

package modelo;

import dao.ProdutoDao;
import dao.MovimentaDao;
import java.util.List;
import java.util.Map;


/**
 * Serve para gerenciar as entradas e saídas de produtos no estoque
 * <p>
 * Atualiza o estoque no BD, registra o histórico de movimentações e valida os
 * limites máximos de entrada/saída
 * </p>
 */
public class MovimentaEstoque {
    
    private final ProdutoDao produtoDao;
    private final MovimentaDao movimentaDao;
    
    
    private int limiteEntrada = 150;
    private int limiteSaida = 25;
    
    /**
     * Construtor para inicializar os DAOs
     */
    public MovimentaEstoque() {
        this.produtoDao = new ProdutoDao();
        this.movimentaDao = new MovimentaDao();
    }
    
    /**
     * Construtor para definir os limites de entrada e saída
     * @param limiteEntrada limita o máximo de unidades adicionadas ao estoque
     * @param limiteSaida limita o mínimo de unidades retiradaos do estoque
     */
    public MovimentaEstoque(int limiteEntrada, int limiteSaida) {
        this();
        this.limiteEntrada = limiteEntrada;
        this.limiteSaida = limiteSaida;
    }
    
   /**
    * Realiza a movimentação do estoque
    * @param id identificação do produto
    * @param quantidade quantidade de unidades do produto
    * @param adicionar se {@code true}, representa uma entrada. Se {@code false}, representa uma saída.
    * @param observacao Informa o resultado da operação
    * @return 
    */
    public String movimentarEstoque(int id, int quantidade, boolean adicionar, String observacao) {
       
        if (quantidade <= 0) {
            return "Erro: Quantidade deve ser maior que zero.";
        }
        
        Produto produto = produtoDao.carregaProduto(id);
        if (produto == null) {
            return "Erro: Produto não encontrado.";
        }
        
       
        if (adicionar && quantidade > limiteEntrada) {
            return String.format("Erro: Quantidade de entrada excede o limite de %d unidades.", limiteEntrada);
        }
        
        if (!adicionar && quantidade > limiteSaida) {
            return String.format("Erro: Quantidade de saída excede o limite de %d unidades.", limiteSaida);
        }
        
       
        int novaQuantidade = adicionar ? 
                produto.getQuantidade() + quantidade : 
                produto.getQuantidade() - quantidade;
        
        if (novaQuantidade < 0) {
            return "Erro: Estoque insuficiente para esta saída.";
        }
        
       
        boolean estoqueAtualizado = produtoDao.updateQuantidadeBD(id, novaQuantidade);
        if (!estoqueAtualizado) {
            return "Erro: Falha ao atualizar estoque do produto.";
        }
        
       
        String tipo = adicionar ? "ENTRADA" : "SAÍDA";
        boolean movimentacaoRegistrada = movimentaDao.registrarMovimentacao(
                id, quantidade, tipo, observacao);
        
        if (!movimentacaoRegistrada) {
            return "Operação concluída com aviso: Estoque atualizado, mas histórico não registrado.";
        }
        
       
        produto.setQuantidade(novaQuantidade);
        
        if (!adicionar && novaQuantidade < produto.getQuantidademin()) {
            return "Operação concluída. ATENÇÃO: Estoque abaixo do mínimo (" + produto.getQuantidademin() + " unidades).";
        }
        
        if (adicionar && novaQuantidade > produto.getQuantidademax()) {
            return "Operação concluída. ATENÇÃO: Estoque acima do máximo (" + produto.getQuantidademax() + " unidades).";
        }
        
        return "Movimentação registrada com sucesso.";
    }
    
    /**
     * Retorna o histórico de movimentações de um produto
     * @param idProduto ID do produto
     * @return Retorna uma lista com as movimentações do produto
     */
    public List<Map<String, Object>> getHistoricoPorProduto(int idProduto) {
        return movimentaDao.getHistoricoPorProduto(idProduto);
    }
    
    /**
     * Retorna todo o histórico de todas as movimentações
     * @return Retorna uma lista com todas as movimentações
     */
    public List<Map<String, Object>> getTodasMovimentacoes() {
        return movimentaDao.getTodasMovimentacoes();
    }
    
    /**
     * Retorna o limite máximo de entrada de uma movimentação
     * @return Retorna o limite de entrada
     */
    public int getLimiteEntrada() {
        return limiteEntrada;
    }
    
    /**
     * Retorna o limite máximo de saída de uma movimentação
     * @return Retorna o limite de saída
     */
    public int getLimiteSaida() {
        return limiteSaida;
    }
    
    /**
     * Define o limite de entrada da movimentação
     * @param limiteEntrada Novo limite de entrada
     */
    public void setLimiteEntrada(int limiteEntrada) {
        this.limiteEntrada = limiteEntrada;
    }
    
    /**
     * Defino o limite de saída da movimentação
     * @param limiteSaida Novo limite de saída
     */
    public void setLimiteSaida(int limiteSaida) {
        this.limiteSaida = limiteSaida;
    }
}