Sistema de Gerenciamento de Produtos
Este projeto é uma aplicação Java desenvolvida com JavaFX e JPA (Hibernate) para realizar o gerenciamento de produtos. Permite cadastrar, editar, excluir e visualizar produtos com informações como nome, descrição, unidade de venda, fornecedor e status de ativo.

🛠 Tecnologias Utilizadas
Java 17+

JavaFX

JPA / Hibernate

FXML

Maven (se aplicável)

Banco de dados relacional (como H2, MySQL, PostgreSQL, etc.)

📁 Estrutura do Projeto
css
Copiar
Editar
src/
├── org/
│   ├── controller/
│   │   └── ProdutosController.java
│   ├── dao/
│   │   └── ProdutosDAO.java
│   └── model/
│       └── Produtos.java
resources/
└── META-INF/
    └── persistence.xml
🚀 Funcionalidades
✅ Cadastro de novo produto

✏️ Edição de produto selecionado

❌ Exclusão de produto

🔍 Exibição de lista de produtos em tabela

📦 Exibição de detalhes do produto ao selecionar

🟢 Marcação de produtos como ativos/inativos

🧪 Validações
Nome do produto deve conter ao menos 3 caracteres.

Descrição não pode ser vazia.

Unidade de venda é preenchida com "UN" por padrão, caso deixada em branco.

📦 Como Executar
Clone o repositório:

bash
Copiar
Editar
git clone https://github.com/lucasfreitaas/crud-produtos.git
cd crud-produtos
Configure o banco de dados no arquivo persistence.xml com as credenciais corretas e URL do banco.

Compile e execute com sua IDE favorita ou via terminal:

bash
Copiar
Editar
mvn clean javafx:run
Ou execute manualmente a Main.java.

📋 Exemplo de Produto
plaintext
Copiar
Editar
Nome: Paracetamol 500mg
Descrição: Analgésico e antitérmico
Unidade de Venda: CX (caixa)
Fornecedor: MedPharma
Ativo: Sim
📍 Observações
A classe ProdutosDAO é responsável pelas operações de persistência.

A tabela é atualizada após exclusão e carregada na inicialização do sistema.

O campo de ID do produto (codigoProdutoField) é apenas informativo (não editável pelo usuário).

📄 Licença
Este projeto está licenciado sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.
