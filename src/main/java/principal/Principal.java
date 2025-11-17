package principal;

import visao.FrmMenuPrincipal;

/**
 * Classe main/principal do sistema
 * <p>
 * Contém o método {@code main}
 * Inicializa e exibe a janela principal do sistema, 
 * {@link visao.FrmMenuPrincipal}
 * </p>
 * 
 * <p>
 * O pacote {@code principal} é responsável por iniciar a aplicação e coordenar
 * a inicialização dos módulos
 * </p>
 */
public class Principal {

    /**
     * Método principal
     * <p>
     * Cria uma instância da tela principal, ({@link FrmMenuPrincipal}), e torna
     * visível para o usuário
     * </p>
     * 
     * @param args 
     */
    public static void main(String[] args) {
        FrmMenuPrincipal objetotela = new FrmMenuPrincipal();
        objetotela.setVisible(true);
    }
    
}

