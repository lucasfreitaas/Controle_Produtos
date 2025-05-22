package org.controllerRelatorios;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import org.dao.FornecedoresDAO;
import org.daoRelatorios.RelatorioFornecedorDAO;
import org.hibernate.engine.jdbc.BlobImplementer;
import org.model.Fornecedores;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class RelatorioFornecedorController implements Initializable {

    @FXML
    private TextField txtCodigoFornecedor;
    @FXML
    private TextField txtNome;
    @FXML
    private CheckBox checkAtivos;
    @FXML
    private CheckBox checkInativos;
    @FXML
    private MenuButton menuOrdenar;

    private String ordemSelecionada = "nenhuma";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtCodigoFornecedor.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")){
                buscarFornecedorPorId();
            }
        });
    }

    @FXML
    public void onOrdemPadrao(){
        menuOrdenar.setText("Padrão");
        ordemSelecionada = "padrao";
    }

    @FXML
    public void onFornecedorId(){
        menuOrdenar.setText("Código do Fornecedor");
        ordemSelecionada = "fornecedor_id";
    }

    @FXML
    public void onCidade(){
        menuOrdenar.setText("Cidade");
        ordemSelecionada = "cidade";
    }

    @FXML
    public void onEstado(){
        menuOrdenar.setText("Estado");
        ordemSelecionada = "estado";
    }

    @FXML
    public void onImprimirClick(){
        RelatorioFornecedorDAO dao = new RelatorioFornecedorDAO();
        String fornecedor_id = txtCodigoFornecedor.getText().trim();
        boolean ativo = false;
        boolean inativo = false;

        if (checkAtivos.isSelected()){
            ativo = true;
        }
        if (checkInativos.isSelected()){
            inativo = true;
        }

        List<Fornecedores> resultado = new ArrayList<>();

        if (!fornecedor_id.isEmpty()){
            resultado = dao.listarFornecedorPorId(fornecedor_id);
        } else if(ativo && !inativo){
            resultado = dao.listarAtivos();
        } else if(!ativo && inativo){
            resultado = dao.listar();
        } else {
            resultado = dao.listar();
        }

        switch (ordemSelecionada){
            case "fornecedor_id":
            resultado.sort(Comparator.comparing(Fornecedores::getFornecedor_id));
            break;
            case "estado":
            resultado.sort(Comparator.comparing(Fornecedores::getEstado));
            break;
            case "cidade":
            resultado.sort(Comparator.comparing(Fornecedores::getCidade));
            break;
            case "padrao":
            resultado.sort(Comparator.comparing(Fornecedores::getNome));
        }

        try{
            gerarPDF(resultado);
            limparCampos();
        } catch (Exception e){
            e.printStackTrace();
            mostrarAlerta("Erro ao gerar o relatório: " + e.getMessage());
        }
    }

    public void gerarPDF(List<Fornecedores> fornecedoresList){
        try{
            Document document = new Document();
            String caminho = "relatorio_fornecedor.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(caminho));
            document.open();

            Paragraph titulo = new Paragraph("Relatório de Fornecedor");
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph(""));

            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{1.5f, 5f, 2f, 2f, 2f});

            tabela.addCell(celulaCabecalho("ID"));
            tabela.addCell(celulaCabecalho("Razão Social"));
            tabela.addCell(celulaCabecalho("CNPJ"));
            tabela.addCell(celulaCabecalho("Estado"));
            tabela.addCell(celulaCabecalho("Cidade"));

            for (Fornecedores f : fornecedoresList){
                tabela.addCell(String.valueOf(f.getFornecedor_id()));
                tabela.addCell(String.valueOf(f.getNome()));
                tabela.addCell(String.valueOf(f.getCNPJ()));
                tabela.addCell(String.valueOf(f.getEstado()));
                tabela.addCell(String.valueOf(f.getCidade()));
            }

            document.add(tabela);
            document.close();

            File arquivo = new File(caminho);
            if (arquivo.exists()){
                Desktop.getDesktop().open(arquivo);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public PdfPCell celulaCabecalho(String texto){
        PdfPCell cell = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private void limparCampos(){
        txtCodigoFornecedor.clear();
        txtNome.clear();
        checkAtivos.isDisable();
        checkInativos.isDisable();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Aviso!");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void buscarFornecedorPorId() {
        String fornecedor_id = txtCodigoFornecedor.getText().trim();
        if (fornecedor_id.isEmpty()){
            return;
        }
        FornecedoresDAO dao = new FornecedoresDAO();
        Fornecedores fornecedores = dao.buscarFornecedorPorId(fornecedor_id);
        if (fornecedores != null){
            txtNome.setText(fornecedores.getNome());
        } else {
            txtNome.setText("Fornecedor não encontrado");
            txtCodigoFornecedor.setText(null);
        }
    }
}
