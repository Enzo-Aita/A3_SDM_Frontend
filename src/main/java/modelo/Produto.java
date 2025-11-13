// Documentado para javadoc

package modelo;

import java.util.ArrayList;
import dao.ProdutoDao;


/**
 * Representa um produto do estoque
 * <p>
 * Realizar operações pelo ProdutoDao
 * </p>
 */
public class Produto {
    
    /** Código de identificação do produto */
    private int id;
    
    /** Preço do produto */
    private double preco;
    
    // Esse aqui eu não entendi muito bem como comentar
    private String unidade;

    /** Categoria do produto */
    private String categoria;
    
    /** Nome do produto */
    private String produto;
    
    /** Quantidade em estoque do produto */
    private int quantidade;
    
    /** Quantidade máxima do produto no estoque */
    private int quantidademax;
    
    /** Quantidade mínima do produto no estoque*/
    private int quantidademin;
    
    /** Operações de acesso ao BD */
    private ProdutoDao dao;
    

    // Construtores
    
    /** Construtor para inicializar o produto */
    public Produto() {
        this(0, "", 0, "", "", 0, 0, 0);
    }

    /**
     * Construtor completo
     * @param id ID do produto
     * @param produto Nome do produto 
     * @param preco Preço do produto
     * @param unidade 
     * @param categoria Categoria do produto
     * @param quantidade Quantidade em estoque do produto
     * @param quantidademax Quantidade máxima em estoque do produto
     * @param quantidademin Quantidade mínima em estoque do produto
     */
    public Produto(int id, String produto, double preco, String unidade,
            String categoria, int quantidade, int quantidademax, int quantidademin) {
        this.id = id;
        this.preco = preco;
        this.unidade = unidade;
        this.categoria = categoria;
        this.produto = produto;
        this.quantidade = quantidade;
        this.quantidademax = quantidademax;
        this.quantidademin = quantidademin;
        this.dao = new ProdutoDao();
    }

    
    // Getters e Setters
    
    /** @return Retorna o ID do produto */
    public int getId() { return id; }
    
    /** @param id Seta o ID do produto */
    public void setId(int id) { this.id = id; }

    /** @return Retorna o preço do produto */
    public double getPreco() { return preco; }
    
    /**
     * Seta o preço do produto
     * @param preco Novo preço
     * @throws RuntimeException Caso o preço seja negativo
     */
    public void setPreco(double preco) {
        if (preco < 0) throw new RuntimeException("Preço não pode ser negativo");
        this.preco = preco;
    }
    
    // Esse aqui eu não entendi muito bem como comentar
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    /** @return Retorna a categoria do produto */
    public String getCategoria() { return categoria; }
    
    /** @param categoria Seta a categoria do produto */
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    /** @return Retorna o nome do produto */
    public String getProduto() { return produto; }
    
    /** @param produto Seta o nome do produto */
    public void setProduto(String produto) { this.produto = produto; }

    /** @return Retorna a quantidade em estoque de um produto */
    public int getQuantidade() { return quantidade; }
    
    /**
     * Define a quantidade em estoque de um produto
     * @param quantidade Nova quantidade
     * @throws RuntimeException Caso a quantidade seja negativa
     */
    public void setQuantidade(int quantidade) {
        if (quantidade < 0) throw new RuntimeException("Quantidade não pode ser negativa");
        this.quantidade = quantidade;
    }

    /** @return Retorna a quantidade máxima */
    public int getQuantidademax() { return quantidademax; }
    
    /** @param quantidademax Seta a quantidade máxima */
    public void setQuantidademax(int quantidademax) { this.quantidademax = quantidademax; }

    /** @return Retorna a quantidade mínima */
    public int getQuantidademin() { return quantidademin; }
    
    /** @param quantidademin Seta a quantidade mínima */
    public void setQuantidademin(int quantidademin) { this.quantidademin = quantidademin; }

    /** @return Retorna o ProdutoDao associado */
    public ProdutoDao getDao() { return dao; }
    
    /** @param dao Seta o Dao associado*/
    public void setDao(ProdutoDao dao) { this.dao = dao; }
    
    
    // Métodos DAO

    /**
     * Retorna a lista de produtos cadastrados
     * @return Lista de produtos {@link Produto}
     */
    public ArrayList<Produto> getMinhaLista() {
        return dao.getMinhaLista();
    }

    /**
     * Insere um produto no BD
     * @param produto Nome do produto
     * @param preco Preço do produto
     * @param unidade 
     * @param categoria Categoria do produto
     * @param quantidade Quantidade em estoque do produto
     * @param quantidademax Quantidade máxima em estoque do produto
     * @param quantidademin Quantidade mínima em estoque do produto
     * @return Retorna {@code true} caso o produto seja inserido
     */
    public boolean insertProdutoBD(String produto, double preco, String unidade, String categoria, int quantidade, int quantidademax, int quantidademin) {
        int id = this.maiorID() + 1;
        Produto objeto = new Produto(id, produto, preco, unidade, categoria, quantidade, quantidademax, quantidademin);
        dao.insertProdutoBD(objeto);
        return true;
    }

    /**
     * Exclui um produto do BD
     * @param id ID do produto a ser excluído
     * @return Retorna {@code true} caso o produto seja excluído
     */
    public boolean deleteProdutoBD(int id) {
        dao.deleteProdutoBD(id);
        return true;
    }

    /**
     * Atualiza o preço de um produto
     * @param id ID do produto
     * @param novoPreco Novo preço do produto 
     * @return Retorna {@code true} se o preço for atualizado
     */
    public boolean updatePrecoBD(int id, int novoPreco) {
        return dao.updatePrecoBD(id, novoPreco);
    }

    /**
     * Serve para atualizar todos os parâmetros de um produto
     * @param id ID do produto
     * @param produto Nome do produto
     * @param preco Preço do produto
     * @param unidade
     * @param categoria Categoria do produto
     * @param quantidade Quantidade em estoque do produto
     * @param quantidademax Quantidade máxima em estoque do produto
     * @param quantidademin Quantidade mínima em estoque do produto
     * @return Retorna {@code true} caso a atualização funcione corretamente
     */
    public boolean updateProdutoBD(int id, String produto,
            double preco, String unidade, String categoria,
            int quantidade, int quantidademax, int quantidademin) {

        Produto objeto = new Produto(id, produto, preco, unidade,
                categoria, quantidade, quantidademax, quantidademin);

       
        return dao.updateProdutoBD(objeto);
    }

    /**
     * Atualiza a quantidade de um produto no BD
     * @param id ID do produto
     * @param novaQuantidade Nova quantidade em estoque
     * @return Retorna {@code true} caso a atualização seja bem-sucedida
     */
    public boolean updateQuantidadeBD(int id, int novaQuantidade) {
        return dao.updateQuantidadeBD(id, novaQuantidade);
    }

    /**
     * Carrega um produto no BD
     * @param id ID do produto
     * @return Retorna o {@link Produto} correspondente
     */
    public Produto carregaProduto(int id) {
        return dao.carregaProduto(id);
    }

    /**
     * Retorna o maior ID no BD
     * @return Retorna o maior ID
     */
    public int maiorID() {
        return dao.maiorID();
    }

    /**
     * Reajusta todos os preços com base em um percentual
     * @param percentual Percentual de reajuste dos preços
     * @return Retorna {@code true} Se o reajuste funcionar e {@code false} caso ocorra algum erro
     */
    public boolean reajustarPrecos(double percentual) {
        try {
            ArrayList<Produto> produtos = getMinhaLista();

            for (Produto produto : produtos) {
                double precoAtual = produto.getPreco();
                int novoPreco = (int) Math.round(precoAtual * (1 + percentual / 100.0));
                produto.setPreco(novoPreco);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }
}
