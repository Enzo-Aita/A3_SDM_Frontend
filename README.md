## Gerenciador de Estoque (Frontend)




| Nome                                  | RA          | GIH Nome  
| ------------------------------------- | ----------- | ---------------------- |
| Enzo Aita Naime                       | 10725113352 | Enzo-Aita              |
| Franco Borin Maranhão                 | 1072519160  | francobm06             |
| Giovanna Bosquerolli                  | 10725110130 | gibosquerolli, camil   |
| Pedro Borges                          | 10725114947 | pedroborges578, pedro  |


| Tecnologia     /     Versão      |                                    
|----------------------------------|
| NetBeans  25                     | 
| Swing (JDK)                      | 
| GitHub (web)                     | 
| MySQL Workbench 8.0 CE           | 
| JDK 24                           |                                             
| Git 2.49.0                       | 

Link para o Backend: https://github.com/Enzo-Aita/A3_SDM_Backend

## X Requisitos não funcionais 
➞ RNF001 – O sistema deve empregar o padrão de projeto DAO , garantindo a separação entre o acesso aos dados, a regra de negócio e a camada de apresentação.

➞ RNF002 – A persistência das informações deve ser assegurada através de um banco de dados relacional MySQL.

➞ RNF003 – O projeto deve contar com sistema de versionamento utilizando Git, com o repositório hospedado no GitHub, e todas as alterações devidamente registradas de forma colaborativa.

➞ RNF004 – A interface gráfica do sistema deve ser clara e fácil de usar, desenvolvida com Swing, possibilitando uma navegação intuitiva entre as funcionalidades principais.

➞ RNF005 – O código-fonte do sistema deve seguir uma estrutura modular, com boas práticas de organização, legibilidade e documentação adequada nas classes e métodos.

➞ RNF006 – A arquitetura do sistema deve permitir fácil adaptação para futuros crescimentos, como adição de novos módulos ou aumento no volume de dados.

##  ✓ Requisitos Funcionais 
➞RF001 – Cadastro de Produtos, O sistema deve permitir o registro de novos produtos, informando dados como nome, categoria, quantidade , quantidade máxima, quantidade minima e valor unitário.

➞RF002 – Atualização de Produtos, O sistema deve permitir ao usuário editar os dados de produtos já cadastrados como nome, categoria, quantidade , quantidade máxima, quantidade minima e valor unitário.

➞RF003 – Exclusão de Produtos, O sistema deve permitir a exclusão de produtos do estoque.

➞RF004 – Consulta de Estoque, O sistema deve permitir a visualização da lista de produtos já cadastrados.

➞RF005 – Entrada de Estoque, O sistema deve permitir o registro da entrada de produtos, atualizando a quantidade disponível no estoque.

➞RF005 - Saída de Estoque, O sistema deve permitir o registro da entrada de produtos, atualizando a quantidade disponível no estoque.

➞RF006 - Cadastro de Categorias, O sistema deve permitir o registro de novas categorias, informando dados como nome da categoria, embalagem e tamanho.

## 📊 Banco de Dados 
Para executar o projeto, é preciso criar um banco de dados com base no script fornecido: 

1º Localize o arquivo banco.sql do projeto.

2ºExecute o script em sua instância local usando o MySQL.

3ºIsso ira criar o banco de dados db_produtos com todas as tabelas.

