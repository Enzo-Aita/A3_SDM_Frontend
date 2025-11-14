package visao;

/**
 * Representa uma exceção para exibir/mover mensagens na visão do aplicativo
 * 
 * <p>
 * Estende {@link Exception}, permitindo personalização e "tratamento" das
 * mensagens
 * </p>
 */
public class Mensagem extends Exception {
    /**
     * Cria uma nova instância do {@code Mensagem} com o texto desejado
     * @param mensagem Mensagem a ser enviada
     */
    public Mensagem(String mensagem){
        super(mensagem);
    }
}
