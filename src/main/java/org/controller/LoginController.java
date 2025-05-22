package org.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.dao.FuncionariosDAO;
import org.model.Funcionarios;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;

    private final FuncionariosDAO dao = new FuncionariosDAO();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public static String usuario_logado;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtSenha.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")){
                onEntrarClick();
            }
        });
    }

    @FXML
    private void onEntrarClick() {
        String usuario = txtUsuario.getText();
        String senha = txtSenha.getText();

        if (usuario.isEmpty() || senha.isEmpty()){
            mostrarAlerta("Preencha usuário e senha.");
            return;
        }

        Funcionarios funcionario = dao.buscarPorNome(usuario);
        usuario_logado = usuario;

        if (funcionario == null){
            mostrarAlerta("Usuário não encontrado.");
            return;
        }

        boolean senhaCorreta = encoder.matches(senha, funcionario.getSenha());

        if (senhaCorreta){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/cadastros-view.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Tela de Cadastros");
                stage.setScene(new Scene(root));
                stage.show();

                Stage telaLogin = (Stage) txtUsuario.getScene().getWindow();
                telaLogin.close();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarAlerta("Erro ao carregar a tela de cadastros.");
            }
        } else if(!senhaCorreta){
            mostrarAlerta("Senha incorreta.");
        }
        else {
            mostrarAlerta("Não foi possível carregar a tela de cadastros.");
        }
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
