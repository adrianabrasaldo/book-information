package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import java.time.Year;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import model.Notification;

public class AuthorBookController implements Initializable {
    // =========================================================================
    // ========================== ETCETERA =====================================
    // =========================================================================
    @FXML
    StackPane stackpane;
    DatabaseConnection dbConn;    
    // =========================================================================
    // =========================================================================
    // =========================================================================
    @FXML
    Label bookIDnoLabel, 
          bookISBNLabel, 
          bookTitleLabel, 
          bookDescriptionLabel, 
          bookPublishDateLabel,
          bookMajorFormsLabel,
          bookGenresLabel,
          bookColorLabel,
          bookNoOfPagesLabel,
          bookShelfLabel;
    // =========================================================================
    // =========================================================================
    // =========================================================================
    @FXML
    JFXComboBox authorListComboBox;
    
    @FXML
    JFXButton saveAuthorButton,
              removeAuthorButton;
    
    @FXML
    JFXTreeTableView<Author> authorTableView;
    JFXTreeTableColumn<Author, String> authorIDnoColumn,
                                       authorNameColumn;
    ObservableList<Author> authorList = FXCollections.observableArrayList();
    // =========================================================================
    // =========================================================================
    // =========================================================================
    @FXML
    public void authorBookHandleEvent(ActionEvent event){ 
        
        if (event.getSource().equals(saveAuthorButton)) {
            
            if (authorListComboBox.getSelectionModel().getSelectedIndex() == -1) {
                
                Notification.notificationEvent(stackpane, "Add Author To Book", "Please select from author list", "WARNING");
            }
            else {
                
                try {
                    String sql = "SELECT author_id, book_id " + 
                                 "FROM author_book_table    " + 
                                 "WHERE author_id = ?       " + 
                                 "AND book_id = ?"; 
                    
                    int author_id = 0;                  
                    int book_id = 0;

                    PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
                    preparedStatement.setInt(1, this.findAuthorIDnoEvent(authorListComboBox.getSelectionModel().getSelectedItem().toString()));
                    preparedStatement.setInt(2, Integer.parseInt(bookIDnoLabel.getText()));
                    ResultSet rs = preparedStatement.executeQuery();
                    
                    while (rs.next()) {                        
                        author_id = rs.getInt("author_id");    
                        book_id = rs.getInt("book_id");
                    }
                    
                    if (author_id == this.findAuthorIDnoEvent(authorListComboBox.getSelectionModel().getSelectedItem().toString()) && 
                        book_id == Integer.parseInt(bookIDnoLabel.getText())) {
                        
                            Notification.notificationEvent(stackpane, "Add Author To Book", "Author already add to book", "WARNING");
                    }
                    else {
                        
                        this.saveAuthorToBookEvent();
                    }
                }
                catch (SQLException ex) {
                    
                    Notification.notificationEvent(stackpane, "Verifying Author Failed", "SQL Exception: " + ex.getMessage(), "ERROR");
                }
            }            
        }
        else if (event.getSource().equals(removeAuthorButton)) {
            
            if (authorListComboBox.getSelectionModel().getSelectedIndex() == -1) {
                
                Notification.notificationEvent(stackpane, "Add Author To Book", "Please select from author list", "WARNING");
            }
            else {
                
                this.removeAuthorFromBookEvent();
            }
        }
    }
    // =========================================================================
    // =========================================================================
    // =========================================================================    
    // FETCH AUTHOR NAME 
    public void getAuthorListEvent() {
        
        try {
            
            String sql = "SELECT * FROM author_table";
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {                
                
                authorListComboBox.getItems().addAll(rs.getString("author_name"));
            }
        } 
        catch (SQLException ex) {
            
            Notification.notificationEvent(stackpane, "Fetching Author List Name Failed", "SQL Exception: " + ex.getMessage(), "ERROR");
        }
        
    }
    // FETCH AUTHOR LIST IN THE BOOK
    public void viewAuthorListEvent(String id) {
        
        try {
            authorList.clear();
            
            String sql = "SELECT author_book_table.author_book_id               AS ID,              " +
                    "            author_table.author_name                       AS Name,            " +
                    "            book_table.book_id                             AS book_id,         " +
                    "            book_table.book_isbn                           AS book_isbn,       " +
                    "            book_table.book_title                          AS book_title,      " +
                    "            book_table.book_description                    AS book_description," +
                    "            book_table.book_date                           AS book_date,       " +
                    "            book_table.book_major                          AS book_major,      " +
                    "            book_table.book_genres                         AS book_genres,     " +
                    "            book_table.book_color                          AS book_color,      " +
                    "            book_table.book_noOfPages                      AS book_noOfPages,  " +
                    "            book_table.book_shelf                          AS book_shelf       " +
                    "FROM author_table                                                              " +
                    "JOIN author_book_table                                                         " +
                    "   ON author_book_table.author_id = author_table.author_id                     " +
                    "RIGHT JOIN book_table                                                          " +
                    "   ON book_table.book_id = author_book_table.book_id                           " + 
                    "WHERE book_table.book_id = ? " ;
            
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {                
                
                authorList.add(new Author(rs.getString("ID"), rs.getString("Name")));
                
                bookIDnoLabel.setText(rs.getString("book_id"));
                bookISBNLabel.setText(rs.getString("book_isbn"));
                bookTitleLabel.setText(rs.getString("book_title"));
                bookDescriptionLabel.setText(rs.getString("book_description"));
                bookPublishDateLabel.setText(rs.getString("book_date"));
                bookMajorFormsLabel.setText(rs.getString("book_major"));
                bookGenresLabel.setText(rs.getString("book_genres"));
                bookColorLabel.setText(rs.getString("book_color"));
                bookNoOfPagesLabel.setText(rs.getString("book_noOfPages"));
                bookShelfLabel.setText(rs.getString("book_shelf"));
            }
            TreeItem<Author> root = new RecursiveTreeItem<>(authorList, RecursiveTreeObject::getChildren);
            authorTableView.getColumns().setAll(authorIDnoColumn, 
                                                authorNameColumn);
            authorTableView.setRoot(root);
            authorTableView.setShowRoot(false);
        } 
        catch (SQLException ex) {
            
            Notification.notificationEvent(stackpane, "Fetching Author List Table Failed", "SQL Exception: " + ex.getMessage(), "ERROR");
        }
    }
    //FIND AUTHOR ID NO
    public int findAuthorIDnoEvent(String name) {
    
        int i = 0;
        try {
            String sql = "SELECT author_id  " +
                        "FROM author_table " +
                        "WHERE author_name = ?";
            
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {                
                
                i = rs.getInt("author_id");
                System.out.println("AUTHOR ID NO: " + i);
            }
        }
        catch (SQLException ex) {
            
            Notification.notificationEvent(stackpane, "Fetching Author ID no Failed", "SQL Exception: " + ex.getMessage(), "ERROR");
        }
        return i;
    }
    // SAVE AUTHOR TO BOOK
    public void saveAuthorToBookEvent() {
        
        try {
            String sql = "INSERT INTO author_book_table " +
                         "(author_id, book_id)          " +
                         "VALUES(?,?)                   " ;
            
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, this.findAuthorIDnoEvent(authorListComboBox.getSelectionModel().getSelectedItem().toString()));
            preparedStatement.setInt(2, Integer.parseInt(bookIDnoLabel.getText()));
            preparedStatement.executeUpdate();
            
            this.viewAuthorListEvent(bookIDnoLabel.getText());
            
             Notification.notificationEvent(stackpane, "Add Author to Book ", "Author has been succesfully added to book: " + bookTitleLabel.getText(), "SUCCESSFUL");
            
        }
        catch (SQLException ex) {
            
            Notification.notificationEvent(stackpane, "Saving Author To Book Failed", "SQL Exception: " + ex.getMessage(), "ERROR");
        }
    }
    // REMOVE AUTHOR FROM BOOK
    public void removeAuthorFromBookEvent() {
        
        try {
            String sql = "DELETE FROM author_book_table " +
                         "WHERE author_book_id = ? ";
            
            TreeItem<Author> author =  authorTableView.getSelectionModel().getSelectedItem();
                    
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, author.getValue().id.getValue());
            preparedStatement.executeUpdate();
            
            this.viewAuthorListEvent(bookIDnoLabel.getText());
            
            Notification.notificationEvent(stackpane, "Remove Author From Book ", "Author has been succesfully removed from book: " + bookTitleLabel.getText(), "SUCCESSFUL");
            
        }
        catch(NullPointerException ex) {
            Notification.notificationEvent(stackpane, "Removing Author From Book", "Please select from author's list", "WARNING");
        }
        catch (SQLException ex) {
            
            Notification.notificationEvent(stackpane, "Removing Author From Book Failed", "SQL Exception: " + ex.getMessage(), "ERROR");
        }
    }
    // =========================================================================
    // =========================================================================
    // =========================================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        dbConn = new DatabaseConnection();
        dbConn.connectDatabase();
        
        this.getAuthorListEvent();
        
        authorIDnoColumn = new JFXTreeTableColumn<>();
        authorIDnoColumn.setText("ID");
        authorIDnoColumn.setPrefWidth(authorTableView.getPrefWidth() / 7);
        authorIDnoColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Author, String> param) -> {
            if(authorIDnoColumn.validateValue(param)) return param.getValue().getValue().id;
            else return authorIDnoColumn.getComputedValue(param);
        });
        authorNameColumn = new JFXTreeTableColumn<>();
        authorNameColumn.setText("Author's Name");
        authorNameColumn.setPrefWidth(authorTableView.getPrefWidth() / 2);
        authorNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Author, String> param) -> {
            if(authorNameColumn.validateValue(param)) return param.getValue().getValue().name;
            else return authorNameColumn.getComputedValue(param);
        });
        authorTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<Author>> observable, TreeItem<Author> oldValue, TreeItem<Author> newValue) -> {
            try {
                authorListComboBox.setValue(newValue.getValue().name.getValue());
            }
            catch(NullPointerException ex) {}
        });
    }   
        
    public class Author extends RecursiveTreeObject<Author> {
        
        SimpleStringProperty id;
        SimpleStringProperty name;
        
        public Author(String id, String name) {
            
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
        }
    }
}
