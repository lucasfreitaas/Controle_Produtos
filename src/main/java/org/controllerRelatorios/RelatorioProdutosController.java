package org.controllerRelatorios;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.dao.ProdutosDAO;
import org.daoRelatorios.RelatorioProdutosDAO;
import org.model.Estoque;
import org.model.Produtos;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RelatorioProdutosController implements Initializable {
    @FXML
    private TextField txtCodigoProduto;
    @FXML
    private TextField txtNome;
    @FXML
    private CheckBox checkAtivos;
    @FXML
    private CheckBox checkInativos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        txtCodigoProduto.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")){
                buscarProdutoPorId();
            }
        });
    }

    @FXML
    public void onImprimirClick(){
        String produto_id = txtCodigoProduto.getText().trim();
        boolean ativo = false;
        boolean inativo = false;

        if (checkAtivos.isSelected()){
            ativo = true;
        }
        if (checkInativos.isSelected()){
            inativo = true;
        }

        RelatorioProdutosDAO dao = new RelatorioProdutosDAO();
        List<Produtos> resultado = new ArrayList<>();

        if (!produto_id.isEmpty()){
            resultado = dao.listarPorProdutoId(produto_id);
        } else if(ativo && !inativo){
            resultado = dao.listarAtivos();
        } else if(!ativo && inativo){
            resultado = dao.listarInativos();
        } else {
            resultado = dao.listarProdutos();
        }

        if (resultado.isEmpty()){
            mostrarAlerta("Nenhum produto com os filtros selecionados foram encontrados!");
            return;
        }

        try {
            gerarPdf(resultado);
            limparCampos();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao gerar o relatório: " + e.getMessage());
        }
    }

    public void gerarPdf(List<Produtos> produtos) {
        try {
            Document document = new Document();
            String caminho = "relatorio_produtos.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(caminho));
            document.open();


            // Título
            Paragraph titulo = new Paragraph("Relatório de Produtos", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph(" ")); // Espaço em branco

            // Tabela com 4 colunas
            PdfPTable tabela = new PdfPTable(4);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{1.5f, 5f, 2f, 2f});

            // Cabeçalhos
            tabela.addCell(celulaCabecalho("ID"));
            tabela.addCell(celulaCabecalho("Nome"));
            tabela.addCell(celulaCabecalho("Preço"));
            tabela.addCell(celulaCabecalho("Status"));

            // Conteúdo da tabela
            for (Produtos p : produtos) {
                tabela.addCell(String.valueOf(p.getProduto_id()));
                tabela.addCell(p.getNome());
                tabela.addCell(String.format(p.getUnidade()));
                tabela.addCell(p.isAtivo() ? "Ativo" : "Inativo");
            }

            document.add(tabela);
            document.close();

            System.out.println("PDF gerado com sucesso: " + caminho);

            // Abrir o PDF automaticamente (Windows/Linux/macOS)
            File arquivo = new File(caminho);
            if (arquivo.exists()) {
                Desktop.getDesktop().open(arquivo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPCell celulaCabecalho(String texto) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private void buscarProdutoPorId() {
        String produto_id = txtCodigoProduto.getText().trim();

        if (produto_id.isEmpty()){
            return;
        }

        ProdutosDAO dao = new ProdutosDAO();
        Produtos produto = dao.buscarProdutoNoBanco(produto_id);
        if (produto != null){
            txtNome.setText(produto.getDescricao());
        } else {
            txtNome.setText("Produto não encontrado");
            txtCodigoProduto.setText(null);
        }

    }

    private void limparCampos(){
        txtCodigoProduto.clear();
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

    private void informacao(String mensagem){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação!");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}
