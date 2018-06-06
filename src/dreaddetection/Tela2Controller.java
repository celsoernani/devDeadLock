/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dreaddetection;

import java.io.IOException;
import static java.lang.ProcessBuilder.Redirect.to;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author celso
 */
public class Tela2Controller implements Initializable {
    
 @FXML
    private AnchorPane AncoraRecursos;

    @FXML
    private Text NameRecurso;

    @FXML
    private TextField Nome1;

    @FXML
    private Button buttonGO;

    @FXML
    private Text titleRec;

    @FXML
    private AnchorPane AncoraSO;

    @FXML
    private Text titleSO1;

    @FXML
    private TextField time;


    
   

        private static int contrec=0;
        private final int quant_recursos;
        private int tempo;
        private ArrayList<String> resourcesNames = new ArrayList<>();
        private ArrayList<String> resourcesQuantity = new ArrayList<>();
        private ArrayList<Recursos> recursos = new ArrayList<>();
    
        
        public  Tela2Controller(int quant_recursos){
        this.quant_recursos = quant_recursos;
             }

     @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
    
		ArrayList<Recursos> recursos = buildResources();
                
            
		if(recursos != null) {
                    
                tempo = Integer.parseInt(time.getText());
                FXMLLoader TelaGrafo = new FXMLLoader(getClass().getResource("TelaGrafo.fxml")); 
                TelaGrafo.setController(new TelaGrafoController(tempo,recursos));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(TelaGrafo.load()));
                stage.show();
			

		} else {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERRO");
                alert.setHeaderText(null);
                alert.setContentText(" Valores inválidos! ");
                alert.showAndWait();
		}
        
                
               
    }
    
     @FXML
    private void RecursosSetup(ActionEvent event) throws IOException {
        //interface
        NameRecurso.setText("Recurso "+(contrec+2));
        
        RecursosSetup();
        contrec++;
        if(contrec == quant_recursos){
            recursos =  buildResources();
            AncoraRecursos.setVisible(false);
            AncoraSO.setVisible(true);
        
            }
    
                  
    }
    
    private void RecursosSetup(){
        	resourcesNames.add(Nome1.getText());
                resourcesQuantity.add("1");
             
                  
    }
    
    private ArrayList<Recursos> buildResources() {
                
		ArrayList<Recursos> resources = new ArrayList<>();
		int amount;

		for(int i=0; i<this.quant_recursos; i++) {

			if(this.resourcesNames.get(i).trim().isEmpty() || this.resourcesQuantity.get(i).trim().isEmpty()) {
				return null;
			}

			try {
				amount = Integer.parseInt(this.resourcesQuantity.get(i).trim());
			} catch(Exception e) {
				return null;
			}

			resources.add(new Recursos(this.resourcesNames.get(i).trim(), amount, i+1));
		}

		return resources;
	}
   
     
 
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         
    }    
    
}
