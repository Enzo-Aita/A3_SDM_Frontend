package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import modelo.Mensagem;
import modelo.Produto;
import modelo.Categoria;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import visao.FrmMenuPrincipal;

/**
 * Cliente para comunicação com o servidor de estoque Responsável por enviar
 * operações e receber respostas do servidor
 */
public class EstoqueCliente {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String host;
    private int port;

    /**
     * Construtor do cliente de estoque
     *
     * @param host Endereço do servidor
     * @param port Porta do servidor
     */
    public EstoqueCliente(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Método principal para inicializar o cliente
     *
     * @param args Argumentos de linha de comando
     */
    public static void main(String[] args) {
        System.out.println("SISTEMA DE CONTROLE DE ESTOQUE - CLIENTE");

        String ipServidor = JOptionPane.showInputDialog(null,
                "Digite o IP do servidor:",
                "Configuração de Conexão",
                JOptionPane.QUESTION_MESSAGE);

        if (ipServidor == null || ipServidor.trim().isEmpty()) {
            ipServidor = "localhost";
        }

        int porta = 12345;

        try {
            EstoqueCliente cliente = new EstoqueCliente(ipServidor, porta);
            boolean conectado = cliente.connect();

            if (conectado) {

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    cliente.disconnect();
                }));

                SwingUtilities.invokeLater(() -> {
                    new FrmMenuPrincipal().setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(null,
                        "Não foi possível conectar ao servidor!\n\n"
                        + "IP: " + ipServidor + ":" + porta + "\n"
                        + "Verifique:\n"
                        + "1. Servidor está rodando\n"
                        + "2. IP está correto\n"
                        + "3. Firewall permite conexão na porta " + porta,
                        "Servidor Não Encontrado",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro: " + e.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Estabelece conexão com o servidor
     *
     * @return true se conectado com sucesso, false caso contrário
     */
    public boolean connect() {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Conectado ao servidor: " + host + ":" + port);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Encerra a conexão com o servidor
     */
    public void disconnect() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao desconectar: " + e.getMessage());
        } finally {
            in = null;
            out = null;
            socket = null;
        }
    }

    /**
     * Envia mensagem para o servidor e recebe resposta
     *
     * @param mensagem Mensagem a ser enviada
     * @return Resposta do servidor
     */
    private Mensagem enviarMensagem(Mensagem mensagem) {
        try {
            // Se não está conectado, tenta conectar
            if (socket == null || socket.isClosed() || !socket.isConnected()) {
                if (!connect()) {
                    return new Mensagem("ERRO", "Não foi possível conectar ao servidor", "ERRO_CONEXAO");
                }
            }

            // Envia a mensagem
            out.writeObject(mensagem);
            out.flush();

            // Recebe a resposta
            return (Mensagem) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());

            // Tenta reconectar na próxima vez
            disconnect();
            return new Mensagem("ERRO", "Erro de comunicação: " + e.getMessage(), "ERRO_COMUNICACAO");
        }
    }

    // ========== MÉTODOS PARA CATEGORIA ==========
    /**
     * Insere uma nova categoria
     *
     * @param categoria Categoria a ser inserida
     * @return Mensagem de sucesso ou erro
     */
    public String inserirCategoria(Categoria categoria) {
        try {
            Mensagem msg = new Mensagem("CADASTRAR_CATEGORIA", categoria);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                return "Categoria inserida com sucesso!";
            } else {
                return "Erro: " + resposta.getDados();
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    /**
     * Lista todas as categorias
     *
     * @return Lista de categorias
     */
    public ArrayList<Categoria> listarCategorias() {
        try {
            Mensagem msg = new Mensagem("LISTAR_CATEGORIAS", null);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                Object dados = resposta.getDados();
                if (dados instanceof ArrayList) {
                    return (ArrayList<Categoria>) dados;
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Atualiza uma categoria existente
     *
     * @param categoria Categoria com dados atualizados
     * @return Mensagem de sucesso ou erro
     */
    public String atualizarCategoria(Categoria categoria) {
        try {
            Mensagem msg = new Mensagem("ATUALIZAR_CATEGORIA", categoria);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                return "Sucesso: Categoria atualizada";
            } else {
                return "Erro: " + resposta.getDados();
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    /**
     * Exclui uma categoria
     *
     * @param id ID da categoria a ser excluída
     * @return Mensagem de sucesso ou erro
     */
    public String deletarCategoria(int id) {
        try {
            Mensagem msg = new Mensagem("EXCLUIR_CATEGORIA", id);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                return "Sucesso: Categoria excluída";
            } else {
                return "Erro: " + resposta.getDados();
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    // ========== MÉTODOS PARA PRODUTO ==========
    /**
     * Insere um novo produto
     *
     * @param produto Produto a ser inserido
     * @return Mensagem de sucesso ou erro
     */
    public String inserirProduto(Produto produto) {
        try {
            Mensagem msg = new Mensagem("CADASTRAR_PRODUTO", produto);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                return "Sucesso: Produto cadastrado";
            } else {
                return "Erro: " + resposta.getDados();
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    /**
     * Lista todos os produtos
     *
     * @return Lista de produtos
     */
    public ArrayList<Produto> listarProdutos() {
        try {
            Mensagem msg = new Mensagem("LISTAR_PRODUTOS", null);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                Object dados = resposta.getDados();
                if (dados instanceof ArrayList) {
                    return (ArrayList<Produto>) dados;
                }
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Atualiza um produto existente
     *
     * @param produto Produto com dados atualizados
     * @return Mensagem de sucesso ou erro
     */
    public String atualizarProduto(Produto produto) {
        try {
            Mensagem msg = new Mensagem("ATUALIZAR_PRODUTO", produto);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                return "Sucesso: Produto atualizado";
            } else {
                return "Erro: " + resposta.getDados();
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    /**
     * Exclui um produto
     *
     * @param id ID do produto a ser excluído
     * @return Mensagem de sucesso ou erro
     */
    public String deletarProduto(int id) {
        try {
            Mensagem msg = new Mensagem("EXCLUIR_PRODUTO", id);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                return "Sucesso: Produto excluído";
            } else {
                return "Erro: " + resposta.getDados();
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    // ========== MÉTODOS PARA MOVIMENTAÇÃO ==========
    /**
     * Realiza movimentação de estoque (entrada ou saída)
     *
     * @param idProduto ID do produto
     * @param quantidade Quantidade a movimentar (positiva para entrada,
     * negativa para saída)
     * @return Mensagem com resultado da operação
     */
    public String movimentarEstoque(int idProduto, int quantidade) {
        try {
            String tipo = quantidade >= 0 ? "ENTRADA" : "SAIDA";
            int quantidadeAbs = Math.abs(quantidade);

            Map<String, Object> dados = new HashMap<>();
            dados.put("idProduto", idProduto);
            dados.put("quantidade", quantidadeAbs);
            dados.put("tipo", tipo);

            Mensagem msg = new Mensagem("REALIZAR_MOVIMENTACAO", dados);
            Mensagem resposta = enviarMensagem(msg);

            boolean sucesso = "SUCESSO".equals(resposta.getStatus())
                    || "PENDENTE".equals(resposta.getStatus())
                    || "AVISO_ESTOQUE_MINIMO".equals(resposta.getStatus())
                    || "AVISO_ESTOQUE_MAXIMO".equals(resposta.getStatus());

            if (sucesso) {
                Object dadosResposta = resposta.getDados();

                if (dadosResposta instanceof Map) {
                    Map<String, Object> respostaMap = (Map<String, Object>) dadosResposta;
                    Map<String, Object> produtoMap = (Map<String, Object>) respostaMap.get("produto");

                    if (produtoMap != null) {
                        String nomeProdutoAtualizado = (String) produtoMap.get("produto");
                        int novaQuantidade = ((Number) produtoMap.get("quantidade")).intValue();

                        String mensagem = "Movimentação realizada com sucesso!\n"
                                + "Produto: " + nomeProdutoAtualizado + "\n"
                                + "Nova quantidade: " + novaQuantidade;

                        if ("AVISO_ESTOQUE_MINIMO".equals(resposta.getStatus())) {
                            String alerta = (String) respostaMap.get("alerta");
                            mensagem += "\n\nAviso: " + alerta;
                        } else if ("AVISO_ESTOQUE_MAXIMO".equals(resposta.getStatus())) {
                            String alerta = (String) respostaMap.get("alerta");
                            mensagem += "\n\nAviso: " + alerta;
                        }

                        return mensagem;
                    }
                }
                return "Movimentação realizada com sucesso!";
            } else {
                return "Erro: " + resposta.getDados();
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    // ========== MÉTODOS PARA HISTÓRICO ==========
    /**
     * Obtém histórico de movimentações de um produto
     *
     * @param idProduto ID do produto
     * @return Lista de movimentações
     */
    public List<Map<String, Object>> obterHistoricoMovimentacoes(int idProduto) {
        try {
            Mensagem msg = new Mensagem("OBTER_HISTORICO_MOVIMENTACOES", idProduto);
            Mensagem resposta = enviarMensagem(msg);

            Object dados = resposta.getDados();
            if (dados instanceof List && !((List<?>) dados).isEmpty()) {
                return (List<Map<String, Object>>) dados;
            } else if ("SUCESSO".equals(resposta.getStatus()) && dados instanceof List) {
                return (List<Map<String, Object>>) dados;
            } else if ("PENDENTE".equals(resposta.getStatus()) && dados instanceof List) {
                return (List<Map<String, Object>>) dados;
            }

            return new ArrayList<>();

        } catch (Exception e) {
            System.err.println("Erro ao obter histórico: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ========== MÉTODOS PARA REAJUSTE DE PREÇOS ==========
    /**
     * Aplica reajuste de preço em um produto específico
     *
     * @param produto Produto com novo preço
     * @return Mensagem de sucesso ou erro
     */
    public String reajustarPrecos(Produto produto) {
        try {
            Mensagem msg = new Mensagem("REAJUSTAR_PRECOS", produto);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                return "Sucesso: " + resposta.getDados();
            } else {
                return "Erro: " + resposta.getDados();
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    // ========== MÉTODOS PARA RELATÓRIOS ==========
    /**
     * Gera relatório de produtos com estoque mínimo
     *
     * @return Lista de produtos com estoque abaixo do mínimo
     */
    public ArrayList<Produto> relatorioEstoqueMinimo() {
        try {
            Mensagem msg = new Mensagem("RELATORIO_ESTOQUE_MINIMO", null);
            Mensagem resposta = enviarMensagem(msg);

            Object dados = resposta.getDados();
            if (dados instanceof ArrayList && !((ArrayList<?>) dados).isEmpty()) {
                return (ArrayList<Produto>) dados;
            } else if ("SUCESSO".equals(resposta.getStatus()) && dados instanceof ArrayList) {
                return (ArrayList<Produto>) dados;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Erro no relatório estoque mínimo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gera relatório de produtos com estoque máximo
     *
     * @return Lista de produtos com estoque acima do máximo
     */
    public ArrayList<Produto> relatorioEstoqueMaximo() {
        try {
            Mensagem msg = new Mensagem("RELATORIO_ESTOQUE_MAXIMO", null);
            Mensagem resposta = enviarMensagem(msg);

            Object dados = resposta.getDados();
            if (dados instanceof ArrayList && !((ArrayList<?>) dados).isEmpty()) {
                return (ArrayList<Produto>) dados;
            } else if ("SUCESSO".equals(resposta.getStatus()) && dados instanceof ArrayList) {
                return (ArrayList<Produto>) dados;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Erro no relatório estoque máximo: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gera relatório de preços dos produtos
     *
     * @return Lista de produtos ordenada por nome
     */
    public ArrayList<Produto> relatorioPrecos() {
        try {
            Mensagem msg = new Mensagem("RELATORIO_PRECOS", null);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                return (ArrayList<Produto>) resposta.getDados();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Erro no relatório de preços: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gera balanço físico-financeiro
     *
     * @return Map com itens e valor total do estoque
     */
    public Map<String, Object> relatorioBalanco() {
        try {
            Mensagem msg = new Mensagem("RELATORIO_BALANCO", null);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                return (Map<String, Object>) resposta.getDados();
            }
            return new HashMap<>();
        } catch (Exception e) {
            System.err.println("Erro no relatório balanço: " + e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * Gera relatório de produtos por categoria
     *
     * @return Map com quantidade de produtos por categoria
     */
    public Map<String, Long> relatorioProdutosPorCategoria() {
        try {
            Mensagem msg = new Mensagem("RELATORIO_PRODUTOS_CATEGORIA", null);
            Mensagem resposta = enviarMensagem(msg);

            if ("SUCESSO".equals(resposta.getStatus())) {
                return (Map<String, Long>) resposta.getDados();
            }
            return new HashMap<>();
        } catch (Exception e) {
            System.err.println("Erro no relatório por categoria: " + e.getMessage());
            return new HashMap<>();
        }
    }
}
