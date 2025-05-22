package org.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.dao.FornecedoresDAO;
import org.model.Fornecedores;
import org.model.Funcionarios;
import org.model.Produtos;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FornecedoresController implements Initializable {

    @FXML
    private TextField idFornecedorField;
    @FXML
    private TextField nomeFornecedorField;
    @FXML
    private TextField CNPJFornecedorField;
    @FXML
    private TextField EstadoField;
    @FXML
    private TextField CidadeField;
    @FXML
    private CheckBox AtivoCheckBox;
    @FXML
    private TableView<Fornecedores> tabelaFornecedores;
    @FXML
    private TableColumn<Fornecedores, Long> colId;
    @FXML
    private TableColumn<Fornecedores, String> colRazaoSocial;
    @FXML
    private TableColumn<Fornecedores, String> colCNPJ;
    @FXML
    private TableColumn<Fornecedores, String> colEstado;
    @FXML
    private TableColumn<Fornecedores, String> colCidade;
    @FXML
    private TableColumn<Fornecedores, Boolean> colAtivo;

    private ObservableList<Fornecedores> listaFornecedores = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("fornecedor_id"));
        colRazaoSocial.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCNPJ.setCellValueFactory(new PropertyValueFactory<>("CNPJ"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("Estado"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("Cidade"));
        colAtivo.setCellFactory(column -> new TableCell<Fornecedores, Boolean>(){
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

        carregarFornecedoresdoBanco();

        tabelaFornecedores.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                carregarDadosFornecedores(newValue);
            }
        });
    }

    @FXML
    public void onSalvarClick(){
            Fornecedores fornecedorSelecionado = tabelaFornecedores.getSelectionModel().getSelectedItem();
            Fornecedores fornecedores = new Fornecedores();

            if(fornecedorSelecionado != null){
                String razao_social = nomeFornecedorField.getText().trim();
                String cnpj = CNPJFornecedorField.getText().trim();
                String estado = EstadoField.getText().trim();
                String cidade = CidadeField.getText().trim();
                boolean ativo = AtivoCheckBox.isSelected();

                fornecedorSelecionado.setNome(razao_social);
                fornecedorSelecionado.setCNPJ(cnpj);
                fornecedorSelecionado.setEstado(estado);
                fornecedorSelecionado.setCidade(cidade);
                fornecedorSelecionado.setAtivo(ativo);

                FornecedoresDAO dao = new FornecedoresDAO();
                dao.editar(fornecedorSelecionado);

                tabelaFornecedores.refresh();
                tabelaFornecedores.getSelectionModel().clearSelection();
                carregarFornecedoresdoBanco();
                limparCampos();
            } else {
                String nome = nomeFornecedorField.getText().trim();
                if (nome.length() < 3 || nome.isEmpty()){
                    mostrarAlerta("A razão social deve conter no mínimo 3 caracteres!");
                    return;
                }
                String CNPJ = CNPJFornecedorField.getText().trim();
                if (CNPJ.length() != 14){
                    mostrarAlerta("O CNPJ precisa conter 14 dígitos!");
                    return;
                }
                String estado = EstadoField.getText().trim();
                if (estado == null){
                    mostrarAlerta("É obrigatório informar o estado!");
                }
                String cidade = CidadeField.getText().trim();
                boolean ativo = AtivoCheckBox.isSelected();

                fornecedores.setNome(nome);
                fornecedores.setCNPJ(CNPJ);
                fornecedores.setEstado(estado);
                fornecedores.setCidade(cidade);
                fornecedores.setAtivo(ativo);

                FornecedoresDAO dao = new FornecedoresDAO();;
                dao.cadastrar(fornecedores);

                tabelaFornecedores.refresh();
                carregarFornecedoresdoBanco();
            }
        try {
            informacao("Fornecedor salvo com sucesso!");
            limparCampos();
        } catch (Exception e) {
            mostrarAlerta("Erro ao salvar o Fornecedor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onExcluirClick(){
        try {
            Fornecedores fornecedorSelecionado = tabelaFornecedores.getSelectionModel().getSelectedItem();

            if (fornecedorSelecionado != null) {
                FornecedoresDAO dao = new FornecedoresDAO();
                dao.excluir(fornecedorSelecionado.getFornecedor_id());

                carregarFornecedoresdoBanco();
                limparCampos();
            } else {
                mostrarAlerta("Selecione um Fornecedor para excluir!");
            }
            } catch (Exception e){
                if (e.getMessage().contains("integridade")){
                    mostrarAlerta("Não é possível excluir: existem registros relacionados a esse fornecedor.");
                } else {
                    mostrarAlerta("Erro ao excluir esse fornecedor: " + e.getMessage());
                }
        }
    }

    private void limparCampos() {
        idFornecedorField.clear();
        nomeFornecedorField.clear();
        CNPJFornecedorField.clear();
        EstadoField.clear();
        CidadeField.clear();
        AtivoCheckBox.setSelected(false);
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

    private void carregarDadosFornecedores(Fornecedores fornecedor) {
        idFornecedorField.setText(String.valueOf(fornecedor.getFornecedor_id()));
        nomeFornecedorField.setText(String.valueOf(fornecedor.getNome()));
        CNPJFornecedorField.setText(String.valueOf(fornecedor.getCNPJ()));
        EstadoField.setText(String.valueOf(fornecedor.getEstado()));
        CidadeField.setText(String.valueOf(fornecedor.getCidade()));
        AtivoCheckBox.setSelected(fornecedor.isAtivo());
    }

    public void carregarFornecedoresdoBanco(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");
        EntityManager em = emf.createEntityManager();

        List<Fornecedores> fornecedores = em.createQuery("SELECT f FROM Fornecedores f", Fornecedores.class).getResultList();
        listaFornecedores.setAll(fornecedores);
        tabelaFornecedores.setItems(listaFornecedores);

        em.close();
        emf.close();
    }
}
