package org.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.dao.EstoqueDAO;
import org.dao.ProdutosDAO;
import org.model.Produtos;
import org.model.Fornecedores;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProdutosController implements Initializable {

    @FXML
    private TextField codigoProdutoField;
    @FXML
    private TextField nomeProdutoField;
    @FXML
    private TextField descricaoField;
    @FXML
    private TextField unidadeVendaField;
    @FXML
    private TextField fornecedorField;
    @FXML
    private CheckBox ativoCheckBox;
    @FXML
    private TableView<Produtos> tabelaProdutos;
    @FXML
    private TableColumn<Produtos, Long> colCodigo;
    @FXML
    private TableColumn<Produtos, String> colNome;
    @FXML
    private TableColumn<Produtos, String> colDescricao;
    @FXML
    private TableColumn<Produtos, String> colUnidade;
    @FXML
    private TableColumn<Produtos, String> colFornecedor;
    @FXML
    private TableColumn<Produtos, Boolean> colAtivo;

    private ObservableList<Produtos> listaProdutos = FXCollections.observableArrayList();

    EstoqueDAO estoqueDAO = new EstoqueDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("produto_id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colUnidade.setCellValueFactory(new PropertyValueFactory<>("unidade"));
        colFornecedor.setCellValueFactory(cellData ->
           new SimpleStringProperty(cellData.getValue().getFornecedores().getNome())
        );
        colAtivo.setCellFactory(column -> new TableCell<Produtos, Boolean>(){
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(Boolean.TRUE.equals(item) ? "Sim" : "Não");
                }
            }
        });

        carregarProdutosDoBanco();

        tabelaProdutos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                carregarDadosProduto(newValue);
            }
        });
    }

    @FXML
    public void onSalvarClick() {
        Produtos produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        Produtos produto = new Produtos();

        String nome = nomeProdutoField.getText().trim();
        String descricao = descricaoField.getText().trim();
        String unidade = unidadeVendaField.getText().trim();
        String fornecedorNome = fornecedorField.getText().trim();
        boolean ativo = ativoCheckBox.isSelected();

        Fornecedores fornecedor = buscarFornecedorPorNome(fornecedorNome);

        if (produtoSelecionado != null) {
            produtoSelecionado.setNome(nome);
            produtoSelecionado.setDescricao(descricao);
            produtoSelecionado.setUnidade(unidade);
            produtoSelecionado.setAtivo(ativo);
            produtoSelecionado.setFornecedores(fornecedor);  // Set fornecedor

            ProdutosDAO dao = new ProdutosDAO();
            dao.editar(produtoSelecionado);

            tabelaProdutos.refresh();
            tabelaProdutos.getSelectionModel().clearSelection();
            carregarProdutosDoBanco();
            limparCampos();
        } else {
            if (nome.isEmpty() || nome.length() < 3) {
                mostrarAlerta("Nome do produto deve conter pelo menos 3 caracteres.");
                return;
            }
            if (descricao.isEmpty()) {
                mostrarAlerta("O produto não pode ficar sem uma descrição!");
                return;
            }
            if (unidade.isEmpty()) {
                informacao("A unidade de venda não pode ser vazia. Será cadastrada como 'UN' por padrão.");
                unidade = "UN";
            }

            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setUnidade(unidade);
            produto.setFornecedores(fornecedor);  // Set fornecedor
            produto.setAtivo(ativo);

            ProdutosDAO dao = new ProdutosDAO();
            dao.cadastrar(produto);
            carregarProdutosDoBanco();
        }

        informacao("Produto salvo com sucesso!");
        limparCampos();
    }

    @FXML
    public void onExcluirClick() {
        Produtos produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        boolean entrada = estoqueDAO.verificarEntradas(produtoSelecionado.getProduto_id());

        if (!entrada){
            if (produtoSelecionado != null) {
                ProdutosDAO dao = new ProdutosDAO();
                dao.excluir(produtoSelecionado.getProduto_id());

                carregarProdutosDoBanco();
                limparCampos();
            } else {
                mostrarAlerta("Selecione um produto para excluir!");
            }
        } else {
            mostrarAlerta("Não é permitido excluir um produto que já houve movimentação.");
            limparCampos();
        }
    }

    @FXML
    public void onBuscarFornecedorClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/busca-view.fxml"));
            Parent root = loader.load();

            BuscaController buscaController = loader.getController();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            Fornecedores selecionado = buscaController.getFornecedorSelecionado();
            if (selecionado != null){
                fornecedorField.setText(selecionado.getNome());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro.");
            alert.setHeaderText("Não foi possível abrir a tela de busca.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void carregarProdutosDoBanco() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");
        EntityManager em = emf.createEntityManager();

        List<Produtos> produtos = em.createQuery("SELECT p FROM Produtos p", Produtos.class).getResultList();
        listaProdutos.setAll(produtos);
        tabelaProdutos.setItems(listaProdutos);

        em.close();
        emf.close();
    }

    private Fornecedores buscarFornecedorPorNome(String nome) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");
        EntityManager em = emf.createEntityManager();

        Fornecedores fornecedor = em.createQuery("SELECT f FROM Fornecedores f WHERE f.nome = :nome", Fornecedores.class)
                .setParameter("nome", nome)
                .getSingleResult();

        em.close();
        emf.close();

        return fornecedor;
    }

    private void mostrarAlerta(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Aviso!");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void informacao(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação!");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparCampos() {
        codigoProdutoField.clear();
        nomeProdutoField.clear();
        descricaoField.clear();
        unidadeVendaField.clear();
        fornecedorField.clear();
        ativoCheckBox.setSelected(false);
    }

    private void carregarDadosProduto(Produtos produto) {
        codigoProdutoField.setText(String.valueOf(produto.getProduto_id()));
        nomeProdutoField.setText(produto.getNome());
        descricaoField.setText(produto.getDescricao());
        unidadeVendaField.setText(produto.getUnidade());
        fornecedorField.setText(produto.getFornecedores().getNome());  // Preencher com o nome do fornecedor
        ativoCheckBox.setSelected(produto.isAtivo());
    }
}
