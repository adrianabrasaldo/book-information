/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Acer
 */
public class MainController implements Initializable {
    //==========================================================================
    @FXML
    AnchorPane anchorpane;
      
    @FXML
    AnchorPane drawerAnchorPane;

    @FXML
    AnchorPane mainAnchorPane;
    //==========================================================================
    VBox drawerBox;
    StackPane bookAnchorPane;
    StackPane authorAnchorPane;
    //==========================================================================
    public void setAnchorPane(StackPane pane){
        
        mainAnchorPane.getChildren().clear();
        
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        
        mainAnchorPane.getChildren().add(pane);
    }
    //==========================================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                
        try {
            
            drawerBox = FXMLLoader.load(getClass().getResource("/document/NavigationDrawerDocument.fxml"));
            bookAnchorPane = FXMLLoader.load(getClass().getResource("/document/BookDocument.fxml"));
            authorAnchorPane =  FXMLLoader.load(getClass().getResource("/document/AuthorDocument.fxml"));
            
            setAnchorPane(bookAnchorPane);
            
            AnchorPane.setTopAnchor(drawerBox, 0.0);
            AnchorPane.setRightAnchor(drawerBox, 0.0);
            AnchorPane.setBottomAnchor(drawerBox, 0.0);
            AnchorPane.setLeftAnchor(drawerBox, 0.0);
            drawerAnchorPane.getChildren().add(drawerBox);  
            
             drawerBox.getChildren().stream().filter((node) -> (node.getAccessibleText() != null)).forEach(new Consumer<Node>() {

                public void accept(Node node) {
                    node.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                        if (node.getAccessibleText().equals("Books")) {
                            setAnchorPane(bookAnchorPane);
                        } else if (node.getAccessibleText().endsWith("Authors")) {
                            setAnchorPane(authorAnchorPane);
                        }
                    });
                }
            });
        } 
        catch (IOException ex) {
            
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
