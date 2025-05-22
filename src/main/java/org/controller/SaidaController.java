package org.controller;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.dao.EstoqueDAO;
import org.dao.ProdutosDAO;
import org.model.Estoque;
import org.model.Produtos;

import java.net.URL;
import java.util.ResourceBundle;

public class SaidaController implements Initializable {

    @FXML
    private TextField txtCodigoProduto;
    @FXML
    private TextField txtDescricao;
    @FXML
    private TextField txtQuantidade;

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtCodigoProduto.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")){
                buscarProdutoPorId();
            }
        });
    }

    private void buscarProdutoPorId() {
        String produto_id = txtCodigoProduto.getText().trim();

        if (produto_id.isEmpty()){
            return;
        }

        ProdutosDAO dao = new ProdutosDAO();
        Produtos produto = dao.buscarProdutoNoBanco(produto_id);
        if (produto != null){
            txtDescricao.setText(produto.getDescricao());

            Estoque estoque = new Estoque();
            if (estoque != null){
                txtQuantidade.setText(String.valueOf(estoque.getEstoque_atual()));
            } else {
                txtQuantidade.setText("0");
            }
        } else {
            txtDescricao.setText("Produto não encontrado");
            txtCodigoProduto.setText(null);
        }

    }

    @FXML
    public void onSalvarClick() {
        Estoque estoque = new Estoque();
        EstoqueDAO estoqueDAO = new EstoqueDAO();
        ProdutosDAO produtoDAO = new ProdutosDAO();

        String produto_id = txtCodigoProduto.getText().trim();
        String descricao = txtDescricao.getText().trim();
        int quantidade;

        if (produto_id.isEmpty()) {
            mostrarAlerta("Escolha o produto para poder efetuar a saída.");
            return;
        }

        try {
            quantidade = Integer.parseInt(txtQuantidade.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Quantidade inválida.");
            return;
        }

        Produtos produto = produtoDAO.buscarProdutoNoBanco(produto_id); // Certifique-se que esse método existe
        if (produto == null) {
            mostrarAlerta("Produto não encontrado.");
            return;
        }

        estoque.setProduto(produto); // ESSENCIAL para @MapsId funcionar

        Estoque estoqueExistente = estoqueDAO.buscarEstoquePorProdutoId(produto_id); // método deve retornar Estoque ou null

        if (estoqueExistente == null || estoqueExistente.getEstoque_atual() < 0) {
            mostrarAlerta("O produto ainda não possui Estoque! Efetue a entrada de mercadorias!");
        } else if (quantidade > estoqueExistente.getEstoque_atual()) {
            mostrarAlerta("A quantidade informada é maior que a quantidade em estoque: "
                    + estoqueExistente.getEstoque_atual());
        } else {
            estoqueExistente.setSaidas(estoqueExistente.getSaidas() + quantidade);
            estoqueExistente.setEstoque_atual(estoqueExistente.getEstoque_atual() - quantidade);
            estoqueDAO.atualizar(estoqueExistente, Long.valueOf(produto_id));
            informacao("Saída realizada com sucesso!");
        }
        limparCampos();
    }

    private void limparCampos() {
        txtCodigoProduto.clear();
        txtDescricao.clear();
        txtQuantidade.clear();
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

}