// Documentado para javadoc

package modelo;

import dao.CategoriaDao;
import java.util.ArrayList;

/**
 * Categoria de produto
 * Contém os atributos para identificação: nome, embalagem e tamanho
*/
public class Categoria {

    private int id;
    private String nome;
    private String embalagem;
    private String tamanho;
    private CategoriaDao dao;

    /**
     * Construtor padrão
     */
    public Categoria() {
        this(0, "", "", "");
    }

    /**
     * Construtor completo
     * @param id    Código de identificação
     * @param nome  Nome da categoria
     * @param embalagem     Tipo de embalagem
     * @param tamanho       Tamanho da embalagem
     */
    public Categoria(int id, String nome, String embalagem, String tamanho) {
        this.id = id;
        this.nome = nome;
        this.embalagem = embalagem;
        this.tamanho = tamanho;
        this.dao = new CategoriaDao();
    }

    /**
     * Construtor Básico para categoria
     * @param idCategoria   Código identificação da categoria
     * @param nomeCategoria     Nome da categoria
     */
    public Categoria(int idCategoria, String nomeCategoria){
        this.id = idCategoria;
        this.nome = nomeCategoria;
    }

    /**
     * @return Retorna o ID da categoria
     */
    public int getId() {
        return id;
    }

    /**
     * @param id Seta o ID da categoria
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Retorna o Nome da categoria
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome Seta o nome da categoria
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return Retorna o tipo de embalagem
     */
    public String getEmbalagem() {
        return embalagem;
    }

    /**
     * @param embalagem Seta o tipo de embalagem
     */
    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }

    /**
     * @return Retorna o tamanho da embalagem
     */
    public String getTamanho() {
        return tamanho;
    }

    /**
     * @param tamanho Seta o tamanho da embalagem
     */
    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    /**
     * @return Retorna o objeto CategoriaDao
     */
    public CategoriaDao getDao() {
        return dao;
    }

    /**
     * @param dao Seta o objeto CategoriaDao
     */
    public void setDao(CategoriaDao dao) {
        this.dao = dao;
    }


    /**
     * Obtém a lista para todas as categorias registradas
     * @return {@link Categoria}
     */
    public ArrayList<Categoria> getMinhaLista() {
        return dao.getMinhaLista();
    }

    /**
     * Adiciona uma nova categoria ao BD
     * @param nome Nome da categoria
     * @param embalagem Tipo de embalagem
     * @param tamanho Tamanho da embalagem
     * @return Retorna {@code true} se a função funcionar
     */
    public boolean insertCategoriaBD(String nome, String embalagem, String tamanho) {
        int id = this.maiorID() + 1;
        Categoria objeto = new Categoria(id, nome, embalagem, tamanho);
        dao.insertCategoriaBD(objeto);
        return true;
    }
    
    /**
     * Deleta uma categoria do BD
     * @param id Código de Identificação da categoria a ser excluída
     * @return Retorna {@code true} se a função funcionar
     */
    public boolean deleteCategoriaBD(int id) {
        dao.deleteCategoriaBD(id);
        return true;
    }

/**
 * Atualiza os dados de uma categoria
 * @param id Código de identificação da categoria
 * @param nome Novo nome
 * @param embalagem Nova embalagem
 * @param tamanho Novo tamanho
 * @return Retorna {@code true} caso a atualização funcione
 */    
public boolean updateCategoriaBD(int id, String nome, String embalagem, String tamanho) {
        Categoria objeto = new Categoria(id, nome, embalagem, tamanho);
        return dao.updateCategoriaBD(objeto);
    }
    
    /**
     * Carrega uma categoria
     * @param id Código de identificação da categoria
     * @return Retorna o objeto {@link Categoria} correspondente
     */
    public Categoria carregaCategoria(int id) {
        return dao.carregaCategoria(id);
    }

    /**
     * Retorna o maior identificador no BD
     * @return Retorna o maior valor de identificação
     */
    public int maiorID() {
        return dao.maiorID();
    }
}
