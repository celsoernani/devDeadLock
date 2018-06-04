/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dreaddetection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 *
 * @author celso
 */
public class FXMLDocumentController implements Initializable {
    
   
   @FXML
    private Button StartButton;

    @FXML
    private Slider Slider;

  
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
                int rec = (int) Slider.getValue();
                FXMLLoader tela2 = new FXMLLoader(getClass().getResource("Tela2.fxml"));
                tela2.setController(new Tela2Controller(rec));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(tela2.load()));
                stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
