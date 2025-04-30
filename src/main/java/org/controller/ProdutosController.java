package org.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import org.dao.ProdutosDAO;
import org.model.Produtos;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProdutosController implements Initializable{

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("produto_id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colUnidade.setCellValueFactory(new PropertyValueFactory<>("unidade"));
        colFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        colAtivo.setCellFactory(column -> new TableCell<Produtos, Boolean>() {
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

        tabelaProdutos.getSelectionModel().selectedItemProperty().addListener
                ((observable, oldValue, newValue) -> {
                    if (newValue != null){
                        carregarDadosProduto(newValue);
                    }
                });
    }

    @FXML
    public void onSalvarClick(){
        String nome = nomeProdutoField.getText().trim();
        if (nome.isEmpty() || nome.length() < 3){
            mostrarAlerta("Nome do produto deve conter pelo menos 3 caracteres.");
            return;
        }
        String descricao = descricaoField.getText().trim();
        if (descricao.isEmpty()){
            mostrarAlerta("O produto não pode ficar sem uma descrição!");
            return;
        }
        String unidade = unidadeVendaField.getText().trim();
        if (unidade.isEmpty()){
            informacao("A unidade de venda não pode ser vazia. " +
                    "Será cadastrada como 'UN' por padrão.");
            unidade = "UN";
        } else {
            unidade = unidade.toUpperCase();
        }
        String fornecedor = fornecedorField.getText().trim();
        boolean ativo = ativoCheckBox.isSelected();

        Produtos produto = new Produtos();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setUnidade(unidade);
        produto.setFornecedor(fornecedor);
        produto.setAtivo(ativo);

        try {
            ProdutosDAO dao = new ProdutosDAO(); // nome de classe com inicial maiúscula
            dao.cadastrar(produto);
            informacao("Produto salvo com sucesso!");

            limparCampos(); // método opcional para limpar o formulário
        } catch (Exception e) {
            mostrarAlerta("Erro ao salvar produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditarClick(){
        Produtos produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();

        if (produtoSelecionado != null){
            String nome = nomeProdutoField.getText().trim();
            String descricao = descricaoField.getText().trim();
            String unidade = unidadeVendaField.getText().trim();
            String fornecedor = fornecedorField.getText().trim();
            boolean ativo = ativoCheckBox.isSelected();

            produtoSelecionado.setNome(nome);
            produtoSelecionado.setDescricao(descricao);
            produtoSelecionado.setUnidade(unidade);
            produtoSelecionado.setFornecedor(fornecedor);
            produtoSelecionado.setAtivo(ativo);

            ProdutosDAO dao = new ProdutosDAO();
            dao.editar(produtoSelecionado);

            tabelaProdutos.refresh();
            limparCampos();
        } else {
            mostrarAlerta("Selecione um produto para editar!");
        }
    }

    @FXML
    public void onExcluirClick(){
        Produtos produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();

        if (produtoSelecionado != null){
            ProdutosDAO dao = new ProdutosDAO();
            dao.excluir(produtoSelecionado.getProduto_id());

            carregarProdutosDoBanco();
            limparCampos();
        } else {
            mostrarAlerta("Selecione um produto para excluir!");
        }
    }

    private void carregarProdutosDoBanco() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU"); // mesmo nome do persistence.xml
        EntityManager em = emf.createEntityManager();

        List<Produtos> produtos = em.createQuery("SELECT p FROM Produtos p", Produtos.class).getResultList();
        listaProdutos.setAll(produtos);
        tabelaProdutos.setItems(listaProdutos);

        em.close();
        emf.close();
    }


    private void mostrarAlerta(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Aviso!");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void informacao(String mensagem){
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
        fornecedorField.setText(produto.getFornecedor());
        ativoCheckBox.setSelected(produto.isAtivo());
    }

}
