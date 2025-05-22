# 🗃️ Sistema de Gerenciamento de Produtos

Este projeto é uma aplicação Java desenvolvida com **JavaFX** e **Hibernate (JPA)** para realizar o gerenciamento completo de produtos, fornecedores e funcionários, incluindo controle de estoque com entradas e saídas e geração de relatórios em PDF. O sistema segue o padrão **MVC**, com uso de **DAO**, **FXML** e **Lombok**, além de oferecer autenticação de usuários.

---

## 🛠 Tecnologias Utilizadas

- Java 17+
- JavaFX (FXML)
- JPA / Hibernate
- Lombok
- Maven (se aplicável)
- Banco de dados relacional (H2, MySQL, PostgreSQL, etc.)
- iText ou JasperReports (para geração de relatórios PDF)

---

## 📁 Estrutura do Projeto

src/
├── java/
│ └── org/
│ ├── controller/ # Controladores das telas principais
│ ├── controllerRelatorios/ # Controladores específicos para relatórios PDF
│ ├── dao/ # DAOs de produtos, funcionários, etc.
│ ├── daoRelatorios/ # DAOs voltados para geração de dados dos relatórios
│ ├── model/ # Entidades JPA (Produto, Fornecedor, etc.)
│ └── principal/ # Classe principal do projeto
│ └── module-info.java # Configuração do módulo Java
├── resources/
│ └── META-INF/
│ └── persistence.xml # Configuração do Hibernate


---

## 🚀 Funcionalidades

- 🔐 Login de usuários com troca de conta
- ✅ Cadastro completo de produtos, fornecedores e funcionários
- ✏️ Edição e exclusão de registros
- 🔄 Entradas e saídas de produtos no estoque
- 📄 Geração de relatórios em PDF:
    - Produtos cadastrados
    - Fornecedores
    - Movimentações de estoque
- 🔍 Filtros e ordenações em tabelas
- 📦 Exibição de detalhes dos registros
- 🟢 Marcação de produtos como ativos/inativos

---

## 🧪 Validações

- Nome do produto com no mínimo 3 caracteres
- Descrição obrigatória
- Unidade de venda padrão: `UN` (caso não informada)
- Validações visuais e de banco

---

## 📦 Como Executar

1. Clone o repositório:

```bash
git clone https://github.com/lucasfreitaas/Controle_Produtos.git
cd Controle_Produtos

