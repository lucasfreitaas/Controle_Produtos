package org.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.dao.FuncionariosDAO;
import org.model.Funcionarios;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FuncionariosController implements Initializable {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtSenha;
    @FXML
    private TextField txtBairro;
    @FXML
    private TextField txtEndereco;
    @FXML
    private TextField txtComplemento;
    @FXML
    private TableView<Funcionarios> tabelaFuncionarios;
    @FXML
    private TableColumn<Funcionarios, Long> colId;
    @FXML
    private TableColumn<Funcionarios, String> colNome;
    @FXML
    private TableColumn<Funcionarios, String> colBairro;
    @FXML
    private TableColumn<Funcionarios, String> colEndereco;
    @FXML
    private TableColumn<Funcionarios, String> colComplemento;

    private ObservableList<Funcionarios> listaFuncionarios = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("funcionario_id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colBairro.setCellValueFactory(new PropertyValueFactory<>("bairro"));
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colComplemento.setCellValueFactory(new PropertyValueFactory<>("complemento"));

        carregarDadosFuncionario();

        tabelaFuncionarios.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                carregarDadosFuncionarios(newValue);
            }
        });
    }

    private void carregarDadosFuncionarios(Funcionarios funcionario) {
        txtId.setText(String.valueOf(funcionario.getFuncionario_id()));
        txtNome.setText(String.valueOf(funcionario.getNome()));
        txtEndereco.setText(String.valueOf(funcionario.getEndereco()));
        txtSenha.setText(String.valueOf(funcionario.getSenha()));
        txtBairro.setText(String.valueOf(funcionario.getBairro()));
        txtComplemento.setText(funcionario.getComplemento());
    }

    @FXML
    public void onSalvarClick(){
        Funcionarios funcionarioSelecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();
        Funcionarios funcionarios = new Funcionarios();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (funcionarioSelecionado != null){
            String nome = txtNome.getText().trim();
            String senha = txtSenha.getText().trim();
            String bairro = txtBairro.getText().trim();
            String endereco = txtEndereco.getText().trim();
            String complemento = txtComplemento.getText().trim();

            String senhaCriptografada = encoder.encode(senha);

            funcionarioSelecionado.setNome(nome);
            funcionarioSelecionado.setSenha(senhaCriptografada);
            funcionarioSelecionado.setBairro(bairro);
            funcionarioSelecionado.setEndereco(endereco);
            funcionarioSelecionado.setComplemento(complemento);

            FuncionariosDAO dao = new FuncionariosDAO();
            dao.salvar(funcionarioSelecionado);

            tabelaFuncionarios.refresh();
            carregarDadosFuncionario();
            limparCampos();
        } else {
            String nome = txtNome.getText().trim();
            if (nome.length() < 3 || nome.isEmpty()){
                mostrarAlerta("O nome deve conter no mínimo 3 caracteres.");
            }
            String senha = txtSenha.getText().trim();
            if (senha.isEmpty()){
                mostrarAlerta("É obrigatório criar uma senha!");
            }
            String bairro = txtBairro.getText().trim();
            if (bairro.isEmpty()){
                mostrarAlerta("O bairro não pode ser nulo!");
            }
            String endereco = txtEndereco.getText().trim();
            if (endereco.length() < 3 || endereco.isEmpty()){
                mostrarAlerta("O endereço deve conter no mínimo 3 caracteres!");
            }
            String complemento = txtComplemento.getText().trim();
            String senhaCriptografada = encoder.encode(senha);

            funcionarios.setNome(nome);
            funcionarios.setSenha(senhaCriptografada);
            funcionarios.setEndereco(endereco);
            funcionarios.setBairro(bairro);
            funcionarios.setComplemento(complemento);

            FuncionariosDAO dao = new FuncionariosDAO();
            dao.salvar(funcionarios);

            tabelaFuncionarios.refresh();
            carregarDadosFuncionario();
        }
        try{
            informacao("Funcionário salvo com sucesso!");
            limparCampos();
        } catch (Exception e){
            mostrarAlerta("Erro ao salvar o funcionário!");
            e.printStackTrace();
        }
    }

    public void onExcluirClick(){
        Funcionarios funcionarioSelecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();

        if (funcionarioSelecionado != null){
            FuncionariosDAO dao = new FuncionariosDAO();
            dao.excluir(funcionarioSelecionado.getFuncionario_id());

            carregarDadosFuncionario();
            limparCampos();
        } else {
            mostrarAlerta("Escolha um funcionário para excluir.");
        }
    }

    private void carregarDadosFuncionario() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU");
        EntityManager em = emf.createEntityManager();

        List<Funcionarios> funcionarios = em.createQuery
                ("SELECT f FROM Funcionarios f", Funcionarios.class).getResultList();

        listaFuncionarios.setAll(funcionarios);
        tabelaFuncionarios.setItems(listaFuncionarios);

        em.close();
        emf.close();
    }

    private void limparCampos() {
        txtNome.clear();
        txtBairro.clear();
        txtComplemento.clear();
        txtEndereco.clear();
        txtId.clear();
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
