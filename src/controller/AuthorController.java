package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import database.DatabaseConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import model.Notification;

public class AuthorController implements Initializable {
   

    // =========================================================================
    // ========================== ETCETERA =====================================
    // =========================================================================
    @FXML
    StackPane stackpane;
    DatabaseConnection dbConn;
    RequiredFieldValidator validator;
    
    java.sql.Date authorDate;
    // =========================================================================
    // =========================================================================
    // =========================================================================
    @FXML
    JFXTextField authorIDnoTextField, 
                 authorNameTextField;
    @FXML
    JFXComboBox authorGenderComboBox;
    @FXML
    JFXDatePicker authorBirthDatePicker;
    
    @FXML
    JFXButton newAuthorButton,
              saveAuthorButton,
              updateAuthorButton;    
    @FXML
        JFXTreeTableView<Author> authorTableView;
    JFXTreeTableColumn<Author, String> authorIDnoColumn,
                                       authorNameColumn,
                                       authorGenderColumn,
                                       authorBirthDatePickerColumn;
    ObservableList<Author> authorList = FXCollections.observableArrayList();
    // =========================================================================
    // =========================================================================
    // =========================================================================
    @FXML
    public void authorHandleEvent(ActionEvent event) {
        
        if (event.getSource().equals(saveAuthorButton)) {
                            
            if (this.checkAccountEmptyFieldEvent() == true) {
                
                this.saveAuthorEvent();
            }
        }
        else if (event.getSource().equals(updateAuthorButton)) {
                       
            if (authorIDnoTextField.getText().isEmpty()) {
                
                Notification.notificationEvent(stackpane, "Update Author", "Please select from author's table", "WARNING");
            }
            else {
                
                if (this.checkAccountEmptyFieldEvent() == true) {

                    this.updateAuthorEvent();
                }
            }
        }
        else if (event.getSource().equals(newAuthorButton)) {
         
            this.clearAccountFieldEvent();
        }
    }
    // =========================================================================
    // =========================================================================
    // =========================================================================
    // CHECK AUTHOR EMPTY FIELD
    public boolean checkAccountEmptyFieldEvent() {
        
        boolean bool = true;
        if (authorNameTextField.getText().isEmpty()) {
            
            bool = false;
            authorNameTextField.validate();
            authorNameTextField.requestFocus();
        }
        if (authorGenderComboBox.getSelectionModel().getSelectedIndex() == -1) {
            
            bool = false;
            authorGenderComboBox.validate();
            authorGenderComboBox.requestFocus();
        }
        if (authorBirthDatePicker.getValue() == null) {
            
            bool = false;
            authorBirthDatePicker.validate();
            authorBirthDatePicker.requestFocus();
        }
        
        return bool;
    }
    // CLEAR AUTHOR FIELD 
    public void clearAccountFieldEvent() {
          
        authorIDnoTextField.clear();
        authorNameTextField.clear();
        authorGenderComboBox.getSelectionModel().select(-1);
        authorBirthDatePicker.setValue(null);
        
        authorNameTextField.requestFocus();
        
        this.getAuthorTableViewEvent();
    }
    // SAVE AUTHOR
    public void saveAuthorEvent() {
        
        try {
            
            String sql = "INSERT INTO author_table  " +
                         "(author_name,             " +
                         " author_gender,           " +
                         " author_birthdate)        " +
                         "VALUES(?,?,?)";
            
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, authorNameTextField.getText());
            preparedStatement.setString(2, authorGenderComboBox.getSelectionModel().getSelectedItem().toString());
            preparedStatement.setDate(3, authorDate);
            preparedStatement.executeUpdate();
            
            this.clearAccountFieldEvent();
            
            Notification.notificationEvent(stackpane, "Add Author ", "Author has been succesfully added!", "SUCCESSFUL");
        } 
        catch (SQLException ex) {
            
            Notification.notificationEvent(stackpane, "Saving Author Failed", "SQL Exception: " + ex.getMessage(), "ERROR");
        }
    }
    // UPDATE AUTHOR
    public void updateAuthorEvent() {
        
        try {
            
            String sql = "UPDATE author_table           " +
                         "SET author_name       = ?,    " +
                         "    author_gender     = ?,    " +
                         "    author_birthdate  = ?     " +
                         "WHERE author_id = ?           " ;
            
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, authorNameTextField.getText());
            preparedStatement.setString(2, authorGenderComboBox.getSelectionModel().getSelectedItem().toString());
            preparedStatement.setDate(3, authorDate);
            preparedStatement.setString(4, authorIDnoTextField.getText());
            preparedStatement.executeUpdate();
            
            this.clearAccountFieldEvent();
            
            Notification.notificationEvent(stackpane, "Update Author ", "Author has been succesfully update!", "SUCCESSFUL");
        } 
        catch (SQLException ex) {
            
            Notification.notificationEvent(stackpane, "Saving Author Failed", "SQL Exception: " + ex.getMessage(), "ERROR");
        }
    }
    // FETCH ACCOUNT TABLE VIEW 
    public void getAuthorTableViewEvent() {
        
        try {
            
            authorList.clear();
            
            String sql = "SELECT * FROM author_table";
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {                
                
                authorList.add(new Author(rs.getString("author_id"),
                                        rs.getString("author_name"),
                                        rs.getString("author_gender"), 
                                        rs.getString("author_birthdate")));
            }
            TreeItem<Author> root = new RecursiveTreeItem<>(authorList, RecursiveTreeObject::getChildren);
            authorTableView.getColumns().setAll(authorIDnoColumn, 
                                                authorNameColumn,
                                                authorGenderColumn, 
                                                authorBirthDatePickerColumn);
            authorTableView.setRoot(root);
            authorTableView.setShowRoot(false);
        }
        catch (SQLException ex) {
            
            Notification.notificationEvent(stackpane, "Fetching Author Table View Failed", "SQL Exception: " + ex.getMessage(), "ERROR");
        }
    }
    // =========================================================================
    // =========================================================================
    // =========================================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        dbConn = new DatabaseConnection();
        dbConn.connectDatabase();
        
        validator = new RequiredFieldValidator();
        validator.setMessage("Required Field");
        
        authorNameTextField.requestFocus();
        authorNameTextField.getValidators().add(validator);
        authorGenderComboBox.getValidators().add(validator);
        authorBirthDatePicker.getValidators().add(validator);        
        authorBirthDatePicker.setValue(LocalDate.now());
        
        authorGenderComboBox.getItems().addAll("Male", "Female");
        
        authorDate = java.sql.Date.valueOf(authorBirthDatePicker.getValue());        
    // =========================================================================
    // =========================================================================
    // =========================================================================
        authorIDnoColumn = new JFXTreeTableColumn<>();
        authorIDnoColumn.setText("ID");
        authorIDnoColumn.setPrefWidth(authorTableView.getPrefWidth() / 7);
        authorIDnoColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Author, String> param) -> {
            if(authorIDnoColumn.validateValue(param)) return param.getValue().getValue().id;
            else return authorIDnoColumn.getComputedValue(param);
        });
        authorNameColumn = new JFXTreeTableColumn<>();
        authorNameColumn.setText("Author's Name");
        authorNameColumn.setPrefWidth(authorTableView.getPrefWidth() / 4);
        authorNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Author, String> param) -> {
            if(authorNameColumn.validateValue(param)) return param.getValue().getValue().name;
            else return authorNameColumn.getComputedValue(param);
        });
        authorGenderColumn = new JFXTreeTableColumn<>();
        authorGenderColumn.setText("Author's Gender");
        authorGenderColumn.setPrefWidth(authorTableView.getPrefWidth() / 4);
        authorGenderColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Author, String> param) -> {
            if(authorGenderColumn.validateValue(param)) return param.getValue().getValue().gender;
            else return authorGenderColumn.getComputedValue(param);
        });
        authorBirthDatePickerColumn = new JFXTreeTableColumn<>();
        authorBirthDatePickerColumn.setText("Author's BirthDate");
        authorBirthDatePickerColumn.setPrefWidth(authorTableView.getPrefWidth() / 4);
        authorBirthDatePickerColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Author, String> param) -> {
            if(authorBirthDatePickerColumn.validateValue(param)) return param.getValue().getValue().birthdate;
            else return authorBirthDatePickerColumn.getComputedValue(param);
        });
        this.getAuthorTableViewEvent();
        authorTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<Author>> observable, TreeItem<Author> oldValue, TreeItem<Author> newValue) -> {

            try {
                
                authorIDnoTextField.setText(newValue.getValue().id.getValue());
                authorNameTextField.setText(newValue.getValue().name.getValue());
                authorGenderComboBox.setValue(newValue.getValue().gender.getValue());

                LocalDate dt = LocalDate.parse(newValue.getValue().birthdate.getValue());
                authorBirthDatePicker.setValue(dt);
            }
            catch(NullPointerException ex) {}
        });
    }
    
    public class Author extends RecursiveTreeObject<Author> {
        
        SimpleStringProperty id;
        SimpleStringProperty name;
        SimpleStringProperty gender;
        SimpleStringProperty birthdate;
        
        public Author(String id, String name, String gender, String birthdate) {
            
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.gender = new SimpleStringProperty(gender);
            this.birthdate = new SimpleStringProperty(birthdate);
        }
    }
}
