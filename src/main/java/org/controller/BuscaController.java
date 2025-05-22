package org.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Getter;
import org.dao.FornecedoresDAO;
import org.model.Fornecedores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BuscaController implements Initializable {

    @FXML
    private TableView<Fornecedores> tabelaFornecedores;

    @FXML
    private TableColumn<Fornecedores, String> colNome;

    @FXML
    private TableColumn<Fornecedores, String> colCNPJ;

    @FXML
    private TableColumn<Fornecedores, String> colEstado;

    @FXML
    private TableColumn<Fornecedores, String> colCidade;

    private FornecedoresDAO dao = new FornecedoresDAO();
    @Getter
    private Fornecedores fornecedorSelecionado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabelaFornecedores.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tabelaFornecedores.getSelectionModel().isEmpty()) {
                fornecedorSelecionado = tabelaFornecedores.getSelectionModel().getSelectedItem();
                Stage stage = (Stage) tabelaFornecedores.getScene().getWindow();
                stage.close(); // Fecha a tela de busca
            }
        });

        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCNPJ.setCellValueFactory(new PropertyValueFactory<>("CNPJ"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));

        carregaFornecedores();
    }

    private void carregaFornecedores() {
        List<Fornecedores> fornecedoresList = dao.listar();
        ObservableList<Fornecedores> fornecedoresObservableList = FXCollections.observableArrayList(fornecedoresList);
        tabelaFornecedores.setItems(fornecedoresObservableList);
    }
}
