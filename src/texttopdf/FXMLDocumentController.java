/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texttopdf;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import com.itextpdf.io.font.FontConstants;

import com.itextpdf.layout.Document;
import javafx.scene.control.Alert.AlertType;

import javafx.stage.DirectoryChooser;

/**
 *
 * @author Michel
 */
public class FXMLDocumentController implements Initializable {

    private File fout, fin;

    @FXML
    private Label input, output;

    @FXML
    private ListView<String> choice;

    ObservableList<String> selectedItems;

    @FXML
    private TextArea nouvtype;

    private String regex;
    @FXML
    private void handleInput(ActionEvent event) {
        DirectoryChooser fdConfig = new DirectoryChooser();

        fdConfig.setTitle("choix du répertoire source");
        fin = fdConfig.showDialog(null);
        input.setText(fin.getAbsolutePath());
    }

    @FXML
    private void handleOutput(ActionEvent event) {
        FileChooser fdConfig = new FileChooser();
        fdConfig.setTitle("choix du fichier");
        fout = fdConfig.showSaveDialog(null);
        String ext="";
        if(!fout.getName().endsWith(".pdf")) fout = new File(fout.getAbsolutePath()+".pdf");
        output.setText(fout.getAbsolutePath());
    }

    @FXML
    private void handleNouvType(ActionEvent event) {
        String nv = nouvtype.getText();
        choice.getItems().add(nv);
    }

    @FXML
    private void handleGo(ActionEvent event) {
        Document doc = null;
        regex = ".*\\.(";
        boolean first=true;
        for(String s : selectedItems){
            regex+= (first?"":"|")+s;
            first=false;
        }
        regex+=")$";
        System.out.println(regex);
        try {
            DocForPDF.init();
            doc = DocForPDF.getInstance(fout);
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERREUR");
            alert.setHeaderText("une erreur est survenue");
            alert.setContentText("erreur de création de document PDF " + e);
            alert.show();
            return;
        }

        gestFich(fin);

        doc.close();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setHeaderText("confirmation");
        alert.setContentText("PDF créé avec succès");
        alert.show();
    }

    private void gestFich(File fdir) {

        if (!fdir.exists() || !fdir.isDirectory()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERREUR");
            alert.setHeaderText("une erreur est survenue");
            alert.setContentText("répertoire invalide");
            alert.show();
            return;
        }

        File[] fl = fdir.listFiles();
        ParagraphBuilder pb;
        for (File f : fl) {
            if (f.isDirectory()) {
                gestFich(f);
            }
           // if (f.getName().endsWith(".java"))
                if (f.getName().matches(regex))
                {
                pb = new ParagraphBuilder(f, fout);
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        input.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        output.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        choice.getItems().addAll(new String[]{"java","ts","html","js","c","cpp","xml"});
        choice.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        choice.setOnMouseClicked((EventHandler<Event>) event -> selectedItems =  choice.getSelectionModel().getSelectedItems());
    }

}
