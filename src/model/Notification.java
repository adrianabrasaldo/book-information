package model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

public class Notification {
    
        static String color;
        
        static public void notificationEvent(StackPane stackpane, String heading, 
                                            String body, String type) {
                    
            if(type.equals("SUCCESSFUL")) { color = "#0d47a1"; }
            else if(type.equals("WARNING")) { color = "#ef6c00 "; }
            else if(type.equals("ERROR")) { color = "#CC0000"; }

            JFXDialog dialog;
            JFXDialogLayout content = new JFXDialogLayout();

            JFXButton closeButton = new JFXButton("Continue");
            closeButton.setStyle("-fx-font-size: 18px;      " +
                                " -jfx-button-type: RAISED; " +
                                " -fx-text-fill: #007E33;   " +
                                " -fx-cursor: hand;         " +
                                " -fx-font-weight: bold;    " +
                                " -fx-pref-height: 45px;    " +
                                " -fx-pref-width: 120px;    " +
                                "}");
            
            Label bodylabel = new Label(body);
            bodylabel.setStyle("-fx-text-fill: black;");
              
            content.setHeading(new Label(heading));
            content.setBody(bodylabel);
            content.setActions(closeButton);

            dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);          
            dialog.lookup(".jfx-layout-heading").setStyle("-fx-background-color: " + color + ";");
            dialog.lookup(".jfx-layout-heading .label").setStyle("-fx-text-fill: white;           "+  
                                                                 "-fx-font-family: Roboto Medium; " +
                                                                 "-fx-font-style: Regular;        " +
                                                                 "-fx-font-size: 18px;");
            dialog.lookup(".jfx-layout-body").setStyle("-fx-font-weight: bold;" + 
                                                       "-fx-padding: 20px;             " +
                                                       "-fx-font-family: Roboto Medium;" +
                                                       "-fx-font-style: Regular;       " +
                                                       "-fx-font-size: 16px;"); 
            dialog.show();              
            closeButton.requestFocus();
            closeButton.setOnAction((ActionEvent event) -> {
                dialog.close();
            });   
        }            
    }   

