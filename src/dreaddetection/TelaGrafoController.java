/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dreaddetection;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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


/**
 * FXML Controller class
 *
 * @author celso
 */
public class TelaGrafoController implements Initializable  {
    @FXML
    public AnchorPane AncoraP6;

    @FXML
    public TextField SolNew;

    @FXML
    public TextField UtiNew;

    @FXML
    public TextArea Log;

    @FXML
    public AnchorPane AncoraP61;

    @FXML
    public TextField idRemove;

    @FXML
    public ImageView Processo1;

    @FXML
    public ImageView Processo2;

    @FXML
    public ImageView Processo3;

    @FXML
    public ImageView Processo4;

    @FXML
    public ImageView Processo5;

    @FXML
    public ImageView Processo6;

    @FXML
    public ImageView Processo7;

    @FXML
    public ImageView Processo8;

    @FXML
    public ImageView Processo9;

    @FXML
    public ImageView Processo10;

    @FXML
    public ImageView Recurso1;

    @FXML
    public ImageView Recurso2;

    @FXML
    public ImageView Recurso3;

    @FXML
    public ImageView Recurso4;

    @FXML
    public ImageView Recurso5;

    @FXML
    public ImageView Recurso6;

    @FXML
    public ImageView Recurso7;

    @FXML
    public ImageView Recurso8;

    @FXML
    public ImageView Recurso9;

    @FXML
    public ImageView Recurso10;
    
    @FXML
    public Button buttonRemoveProcesso;
    
    @FXML
    public Button buttonAddProcesso;
    
    @FXML
    private void CriarProcesso(ActionEvent event) throws IOException {
        
        if(SolNew.getText().isEmpty() || UtiNew.getText().isEmpty())
		{
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERRO");
                alert.setHeaderText(null);
                alert.setContentText("Digite os campos necessários.");
                alert.showAndWait();
		}
        else{
                Processos process = new Processos(this.operationalSystem, Integer.parseInt(UtiNew.getText().trim()), Integer.parseInt(SolNew.getText().trim()),this.resources.size(),this);
		operationalSystem.addProcess(process);
		process.start();
		this.buttonRemoveProcesso.setDisable(false);
        }
               
    }
   public ArrayList<Recursos> resources;
    public int time;
    SystemOperacional operationalSystem;
      
    
public TelaGrafoController(int time , ArrayList<Recursos> resources){
        this.resources = resources;
        this.time = time;

}
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
                operationalSystem = new SystemOperacional(this.time, this);
		operationalSystem.addResources(resources);
                operationalSystem.start();

	}
        
  
    
    
    
}
