
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
import javafx.scene.control.Label;
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
    public Button FIM;

    @FXML
    public AnchorPane NovoProcesso;

    @FXML
    public TextField SolNew;

    @FXML
    public TextField UtiNew;

    @FXML
    public Button buttonAddProcesso;

    @FXML
    public TextArea Log;

    @FXML
    public AnchorPane ExcluirProcesso;

    @FXML
    public TextField idRemove;

    @FXML
    public Button buttonRemoveProcesso;

    @FXML
    public AnchorPane ancoraProcesso1;

    @FXML
    public Label statusPro1;

    @FXML
    public ImageView imgPro1;

    @FXML
    public Label nomePro1;

    @FXML
    public AnchorPane ancoraProcesso6;

    @FXML
    public ImageView imgPro6;

    @FXML
    public Label nomePro6;

    @FXML
    public Label statusPro6;

    @FXML
    public AnchorPane ancoraProcesso7;

    @FXML
    public Label nomePro7;

    @FXML
    public Label statusPro7;

    @FXML
    public AnchorPane ancoraProcesso8;

    @FXML
    public ImageView imgPro8;

    @FXML
    public Label nomePro8;

    @FXML
    public Label statusPro8;

    @FXML
    public AnchorPane ancoraProcesso9;

    @FXML
    public ImageView imgPro9;

    @FXML
    public Label nomePro9;

    @FXML
    public Label statusPro9;

    @FXML
    public AnchorPane ancoraProcesso10;

    @FXML
    public ImageView imgPro10;

    @FXML
    public Label nomePro10;

    @FXML
    public Label statusPro10;

    @FXML
    public AnchorPane ancoraRecurso1;

    @FXML
    public ImageView Recurso1;

    @FXML
    public Label nomeRec1;

    @FXML
    public AnchorPane ancoraRecurso2;

    @FXML
    public ImageView Recurso2;

    @FXML
    public Label nomeRec2;

    @FXML
    public AnchorPane ancoraRecurso3;

    @FXML
    public ImageView Recurso3;

    @FXML
    public Label nomeRec3;

    @FXML
    public AnchorPane ancoraRecurso4;

    @FXML
    public ImageView Recurso4;

    @FXML
    public Label nomeRec4;

    @FXML
    public AnchorPane ancoraRecurso5;

    @FXML
    public ImageView Recurso5;

    @FXML
    public Label nomeRec5;

    @FXML
    public AnchorPane ancoraRecurso6;

    @FXML
    public ImageView Recurso6;

    @FXML
    public Label nomeRec6;

    @FXML
    public AnchorPane ancoraRecurso7;

    @FXML
    public ImageView Recurso7;

    @FXML
    public Label nomeRec7;

    @FXML
    public AnchorPane ancoraRecurso8;

    @FXML
    public ImageView Recurso8;

    @FXML
    public Label nomeRec8;

    @FXML
    public AnchorPane ancoraRecurso9;

    @FXML
    public ImageView Recurso9;

    @FXML
    public Label nomeRec9;

    @FXML
    public AnchorPane ancoraRecurso10;

    @FXML
    public ImageView Recurso10;

    @FXML
    public Label nomeRec10;

    @FXML
    public Label lblProcesso3;

    @FXML
    public AnchorPane ancoraProcesso2;

    @FXML
    public Label statusPro2;

    @FXML
    public ImageView imgPro2;

    @FXML
    public Label nomePro2;

    @FXML
    public AnchorPane ancoraProcesso3;

    @FXML
    public Label statusPro3;

    @FXML
    public ImageView imgPro3;

    @FXML
    public Label nomePro3;

    @FXML
    public AnchorPane ancoraProcesso4;

    @FXML
    public Label statusPro4;

    @FXML
    public ImageView imgPro4;

    @FXML
    public Label nomePro4;

    @FXML
    public AnchorPane ancoraProcesso5;

    @FXML
    public Label statusPro5;

    @FXML
    public ImageView imgPro5;

    @FXML
    public Label nomePro5;
    
    
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
                Processos newprocesso = new Processos(this.operationalSystem, Integer.parseInt(UtiNew.getText().trim()), Integer.parseInt(SolNew.getText().trim()),this.resources.size(),this);
		operationalSystem.addProcess(newprocesso);
  
		newprocesso.start();
		this.buttonRemoveProcesso.setDisable(false);
        }
               
    }
    
     @FXML
    private void excluirProcesso(ActionEvent event) throws IOException, InterruptedException {
        if(idRemove.getText() != null) {
				
				int pid = Integer.parseInt(idRemove.getText());
                                final int index = this.operationalSystem.getindexProcess(pid);
                                 this.operationalSystem.killProcessAtIndex(index);
                   }
    }
    
     @FXML
    private void acabarTudo(ActionEvent event) throws IOException {
        
       this.operationalSystem.restartSystem();
       Stage stage = (Stage) FIM.getScene().getWindow();
     stage.close();
    }
    
    public ArrayList<Recursos> resources;
     public ArrayList<Processos> process;
    public int time;
    SystemOperacional operationalSystem;
      
    
public TelaGrafoController(int time , ArrayList<Recursos> resources){
        this.resources = resources;
        this.time = time;

}
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
                operationalSystem = new SystemOperacional(this.time, this);
		operationalSystem.addResources(this.resources);
                desenharRecursos(this.resources.size());
                operationalSystem.start();

	}
    
    
    
      private void desenharRecursos(int quant){
               switch (quant) {
            case 1:  ancoraRecurso1.setVisible(true);
                     break;
            case 2:
                
                      ancoraRecurso1.setVisible(true);
                    ancoraRecurso2.setVisible(true);
                     break;
            case 3: 
                ancoraRecurso1.setVisible(true);
                ancoraRecurso2.setVisible(true);
                ancoraRecurso3.setVisible(true);
                     break;
            case 4:  
                ancoraRecurso1.setVisible(true);
                ancoraRecurso2.setVisible(true);
                ancoraRecurso3.setVisible(true);
                ancoraRecurso4.setVisible(true);
                     break;
            case 5: 
                ancoraRecurso1.setVisible(true);
                ancoraRecurso2.setVisible(true);
                ancoraRecurso3.setVisible(true);
                ancoraRecurso4.setVisible(true);
                ancoraRecurso5.setVisible(true);
                     break;
            case 6:  
                ancoraRecurso1.setVisible(true);
                ancoraRecurso2.setVisible(true);
                ancoraRecurso3.setVisible(true);
                ancoraRecurso4.setVisible(true);
                ancoraRecurso5.setVisible(true);
                ancoraRecurso6.setVisible(true);
                     break;
            case 7:  
                ancoraRecurso1.setVisible(true);
                ancoraRecurso2.setVisible(true);
                ancoraRecurso3.setVisible(true);
                ancoraRecurso4.setVisible(true);
                ancoraRecurso5.setVisible(true);
                ancoraRecurso6.setVisible(true);
                ancoraRecurso7.setVisible(true);
                     break;
            case 8:  
                ancoraRecurso1.setVisible(true);
                ancoraRecurso2.setVisible(true);
                ancoraRecurso3.setVisible(true);
                ancoraRecurso4.setVisible(true);
                ancoraRecurso5.setVisible(true);
                ancoraRecurso6.setVisible(true);
                ancoraRecurso7.setVisible(true);
                ancoraRecurso8.setVisible(true);
                     break;
            case 9:  
                ancoraRecurso1.setVisible(true);
                ancoraRecurso2.setVisible(true);
                ancoraRecurso3.setVisible(true);
                ancoraRecurso4.setVisible(true);
                ancoraRecurso5.setVisible(true);
                ancoraRecurso6.setVisible(true);
                ancoraRecurso7.setVisible(true);
                ancoraRecurso8.setVisible(true);
                ancoraRecurso9.setVisible(true);
                     break;
            case 10:
                ancoraRecurso1.setVisible(true);
                ancoraRecurso2.setVisible(true);
                ancoraRecurso3.setVisible(true);
                ancoraRecurso4.setVisible(true);
                ancoraRecurso5.setVisible(true);
                ancoraRecurso6.setVisible(true);
                ancoraRecurso7.setVisible(true);
                ancoraRecurso8.setVisible(true);
                ancoraRecurso9.setVisible(true);
                ancoraRecurso10.setVisible(true);
                     break;
   
            default:
                     break;
        }
            

}
      

        
  
    
    
    
}
