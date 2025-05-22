package org.controllerRelatorios;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import org.dao.ProdutosDAO;
import org.daoRelatorios.RelatorioEstoqueDAO;
import org.daoRelatorios.RelatorioProdutosDAO;
import org.model.Estoque;
import org.model.Produtos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class RelatorioEstoqueController implements Initializable {
    @FXML
    private TextField txtCodigoProduto;
    @FXML
    private TextField txtNome;
    @FXML
    private CheckBox checkAtivos;
    @FXML
    private CheckBox checkInativos;
    @FXML
    private MenuButton menuOrdenar;

    private String ordemSelecionada = "nenhuma";

    RelatorioEstoqueDAO dao = new RelatorioEstoqueDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtCodigoProduto.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")){
                buscarProdutoPorId();
            }
        });
    }

    @FXML
    public void onOrdemEstoque() {
        menuOrdenar.setText("Estoque Atual");
        ordemSelecionada = "estoque";
    }

    @FXML
    public void onOrdemEntradas(){
        menuOrdenar.setText("Entradas");
        ordemSelecionada = "entradas";
    }

    @FXML
    public void onOrdemSaidas(){
        menuOrdenar.setText("Saidas");
        ordemSelecionada = "saidas";
    }

    @FXML
    public void onOrdemPadrao(){
        menuOrdenar.setText("Padrão");
        ordemSelecionada = "padrao";
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

        List<Estoque> resultado = new ArrayList<>();

        if (!produto_id.isEmpty()){
            resultado = dao.listarPorProdutoId(produto_id);
        } else if(ativo && !inativo){
            resultado = dao.listarAtivos();
        } else if (!ativo && inativo){
            resultado = dao.listarInativos();
        } else {
            resultado = dao.listarEstoque();
        }

        switch (ordemSelecionada){
            case "estoque":
                resultado.sort(Comparator.comparing(Estoque::getEstoque_atual));
            break;
            case "entradas":
                resultado.sort(Comparator.comparing(Estoque::getEntradas));
            break;
            case "saidas":
                resultado.sort(Comparator.comparing(Estoque::getSaidas));
            break;
            case "padrao":
                resultado.sort(Comparator.comparing(Estoque::getProduto_id));
            break;
        }

        try {
            gerarPdf(resultado);
            limparCampos();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao gerar o relatório: " + e.getMessage());
        }
    }

    public void gerarPdf(List<Estoque> estoqueList) {
        try {
            Document document = new Document();
            String caminho = "relatorio_estoque.pdf";
            PdfWriter.getInstance(document, new FileOutputStream(caminho));
            document.open();

            // Título
            Paragraph titulo = new Paragraph("Relatório de Estoque", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph(" ")); // Espaço em branco

            // Tabela com 5 colunas
            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{1.5f, 5f, 2f, 2f, 2f});

            // Cabeçalhos
            tabela.addCell(celulaCabecalho("ID"));
            tabela.addCell(celulaCabecalho("Nome do Produto"));
            tabela.addCell(celulaCabecalho("Estoque Atual"));
            tabela.addCell(celulaCabecalho("Entradas"));
            tabela.addCell(celulaCabecalho("Saídas"));

            // Conteúdo da tabela
            for (Estoque e : estoqueList) {
                tabela.addCell(String.valueOf(e.getProduto_id())); // ID do produto
                tabela.addCell(e.getProduto() != null ? e.getProduto().getNome() : ""); // Nome do produto
                tabela.addCell(String.valueOf(e.getEstoque_atual()));
                tabela.addCell(String.valueOf(e.getEntradas()));
                tabela.addCell(String.valueOf(e.getSaidas()));
            }

            document.add(tabela);
            document.close();

            System.out.println("PDF gerado com sucesso: " + caminho);

            // Abrir o PDF automaticamente
            File arquivo = new File(caminho);
            if (arquivo.exists()) {
                Desktop.getDesktop().open(arquivo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private PdfPCell celulaCabecalho(String texto){
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
}
