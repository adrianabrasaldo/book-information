package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/document/MainDocument.fxml"));
        Parent root = (Parent) fxmlLoader.load();                
        root.setStyle("-fx-background-color: white;");
        primaryStage.setTitle("Book Information");
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(new Scene(root));  
        primaryStage.setResizable(true);    
        primaryStage.setMinWidth(1481);    
        primaryStage.setMinHeight(986);
        primaryStage.show();
    }    
}
