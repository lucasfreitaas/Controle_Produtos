package org.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CadastrosController implements Initializable {

    @FXML
    private Button btnTrocarUsuario;
    @FXML
    private TextField labelUsuario;

    LoginController loginController = new LoginController();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelUsuario.setText("Usuário: " + LoginController.usuario_logado);
    }

    @FXML
    private void onProdutosClick(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/produtos-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Produtos");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e){
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro.");
            alert.setHeaderText("Não foi possível abrir a tela de produtos.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onFornecedorClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fornecedores-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Fornecedores");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e){
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de fornecedores.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onFuncionariosClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/funcionarios-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Funcionários");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de funcionários.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onEntradaClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/entrada-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Entrada de Mercadoria");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de entrada.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onSaidaClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/saida-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Saída de Mercadoria");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de entrada.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onRelatorioProdutosClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/relatorio-produtos-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Relatório de Produtos");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de relatório.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onTrocarUsuarioClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) btnTrocarUsuario.getScene().getWindow();
            currentStage.close();
        }catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de Login.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onRelatorioEtoqueClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/relatorio-movimentacao-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Relatório de Estoque");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de relatório.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onRelatorioFornecedoresClick(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/relatorio-fornecedor-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Relatório de Fornecedores");
            stage.setScene(new Scene(root));
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Não foi possível abrir a tela de relatório.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onAjudaClick(){

    }
}
