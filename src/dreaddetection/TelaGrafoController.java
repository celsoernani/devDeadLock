/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dreaddetection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author celso
 */
public class TelaGrafoController implements Initializable  {
    @FXML
    private AnchorPane AncoraP6;

    @FXML
    private TextField SolNew;

    @FXML
    private TextField UtiNew;

    @FXML
    private TextArea Log;

    @FXML
    private AnchorPane AncoraP61;

    @FXML
    private TextField idRemove;

    @FXML
    private ImageView Processo1;

    @FXML
    private ImageView Processo2;

    @FXML
    private ImageView Processo3;

    @FXML
    private ImageView Processo4;

    @FXML
    private ImageView Processo5;

    @FXML
    private ImageView Processo6;

    @FXML
    private ImageView Processo7;

    @FXML
    private ImageView Processo8;

    @FXML
    private ImageView Processo9;

    @FXML
    private ImageView Processo10;

    @FXML
    private ImageView Recurso1;

    @FXML
    private ImageView Recurso2;

    @FXML
    private ImageView Recurso3;

    @FXML
    private ImageView Recurso4;

    @FXML
    private ImageView Recurso5;

    @FXML
    private ImageView Recurso6;

    @FXML
    private ImageView Recurso7;

    @FXML
    private ImageView Recurso8;

    @FXML
    private ImageView Recurso9;

    @FXML
    private ImageView Recurso10;
    
    @FXML
    private Button buttonRemoveProcesso;
    
    @FXML
    private Button buttonAddProcesso;


    
    private SystemOperacional operationalSystem; 

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    private void CriarProcesso(ActionEvent event) throws IOException {
        
        if(SolNew.getText().isEmpty() || UtiNew.getText().isEmpty())
		{
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERRO");
                alert.setHeaderText(null);
                alert.setContentText("Digite os campos necessários.");
                alert.showAndWait();
		}
                
    }
    
    
}
