package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.RequiredFieldValidator;
import database.DatabaseConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Notification;

public class BookController implements Initializable {

    // =========================================================================
    // ========================== ETCETERA =====================================
    // =========================================================================
    @FXML
    StackPane stackpane;
    DatabaseConnection dbConn;
    RequiredFieldValidator validator;
    
    java.sql.Date bookDate;
    // =========================================================================
    // =========================================================================
    // =========================================================================
    @FXML
    JFXTextField bookISBNTextField,
                 bookIDnoTextField,
                 bookTitleTextField;
    @FXML
    JFXTextArea bookDescriptionTextArea;
    @FXML
    JFXDatePicker bookDatePicker;
    
    @FXML
    JFXComboBox bookMajorFormsComboBox, 
                bookGenresComboBox;
    @FXML
    JFXTextField bookColorTextField, 
                 bookNoOfPagesTextField;
    @FXML
    JFXComboBox bookShelfComboBox;
    
    @FXML
    JFXButton saveBookButton,
              updateBookButton,
              deleteBookButton,
              newBookButton, 
              viewAuthorBookButton;
    
    @FXML
    JFXTreeTableView<Book> bookTableView;
    JFXTreeTableColumn<Book, String> bookIDnoColumn,
                                     bookISBNDateColumn,
                                     bookTitleDateColumn,
                                     bookDescriptionColumn,
                                     bookDateColumn,
                                     bookMajorColumn,
                                     bookGenresColumn,
                                     bookColorColumn,
                                     bookNoOfPagesColumn,
                                     bookShelfColumn;
    ObservableList<Book> bookList = FXCollections.observableArrayList();
    // =========================================================================
    // =========================================================================
    // =========================================================================
    
    @FXML
    public void bookHandleEvent(ActionEvent event) {
        
        if (event.getSource().equals(saveBookButton)) {
            
            if (this.checkBookEmptyFieldEvent() == true) {
                
                this.saveBookEvent();
            }
        }        
        else if (event.getSource().equals(updateBookButton)) {
           
            if (bookIDnoTextField.getText().isEmpty()) {
                
                Notification.notificationEvent(stackpane, "Update Book", "Please select from book's table", "WARNING");
            }
            else {
                
                if (this.checkBookEmptyFieldEvent() == true) {

                    this.updateBookEvent();
                }
            }
        }        
        else if (event.getSource().equals(deleteBookButton)) {
            if (bookIDnoTextField.getText().isEmpty()) {
                
                Notification.notificationEvent(stackpane, "Delete Book", "Please select from book's table", "WARNING");
            }
            else {

            JFXDialog dialog;
            JFXDialogLayout content = new JFXDialogLayout();

            JFXButton cancelButton = new JFXButton("Cancel");
            JFXButton confirmButton = new JFXButton("Confirm");
            
            cancelButton.setStyle("-fx-font-size: 18px;      " +
                                " -jfx-button-type: RAISED; " +
                                " -fx-text-fill: #CC0000;   " +
                                " -fx-cursor: hand;         " +
                                " -fx-font-weight: bold;    " +
                                " -fx-pref-height: 45px;    " +
                                " -fx-pref-width: 120px;    " +
                                "}");
            confirmButton.setStyle("-fx-font-size: 18px;      " +
                                " -jfx-button-type: RAISED; " +
                                " -fx-text-fill: #007E33;   " +
                                " -fx-cursor: hand;         " +
                                " -fx-font-weight: bold;    " +
                                " -fx-pref-height: 45px;    " +
                                " -fx-pref-width: 120px;    " +
                                "}");
            
            Label bodylabel = new Label("Delete Book");
            bodylabel.setStyle("-fx-text-fill: black;");
              
            content.setHeading(bodylabel);
            content.setBody(new Label("Are you sure you want to delete book? It will affect the book's author"));
            content.setActions(cancelButton, confirmButton);

            dialog = new JFXDialog(stackpane, content, JFXDialog.DialogTransition.CENTER);      
            
            dialog.lookup(".jfx-layout-heading").setStyle("-fx-background-color: #CC0000");
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
            
            cancelButton.setOnAction((ActionEvent e) -> {
                dialog.close();
            });
            confirmButton.setOnAction((ActionEvent e) -> {
                dialog.close();
                this.deleteBookEvent();
            });
               
            }
        }
        else if(event.getSource().equals(newBookButton)) {
            
            this.clearBookFieldEvent();   
        }
        else if(event.getSource().equals(viewAuthorBookButton)) {
            this.openAuthorBookWindowEvent();
        }
    }
    
    public void openAuthorBookWindowEvent() {
        if (bookIDnoTextField.getText().isEmpty()) {

            Notification.notificationEvent(stackpane, "View Book's Author", "Please select from book's table", "WARNING");
        }
        else {
            try {
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/document/AuthorBookDocument.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                AuthorBookController controller = fxmlLoader.<AuthorBookController>getController();
                root.setStyle("-fx-background-color: transparent;");
                stage.setTitle("Book's Authors");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);
                stage.show();   
                controller.viewAuthorListEvent(bookIDnoTextField.getText());
            } catch (IOException ex) {
                Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    // CHECk BOOK TEXTFIELD
    public boolean checkBookEmptyFieldEvent() {
        
        boolean bool = true;
        
        if (bookISBNTextField.getText().isEmpty()) {
            
            bool = false;
            bookISBNTextField.validate();
            bookISBNTextField.requestFocus();
        }
        else if (bookTitleTextField.getText().isEmpty()) {
            
            bool = false;
            bookTitleTextField.validate();
            bookTitleTextField.requestFocus();
        }

        else if (bookDescriptionTextArea.getText().isEmpty()) {
            
            bool = false;
            bookDescriptionTextArea.validate();
            bookDescriptionTextArea.requestFocus();
        }
        else if (bookDatePicker.getValue() == null) {
            
            bool = false;
            bookDatePicker.validate();
            bookDatePicker.requestFocus();
        }
        
        return bool;
    }
    // CLEAR BOOK TEXTFIELD
    public void clearBookFieldEvent() {
     
        bookIDnoTextField.clear();
        bookISBNTextField.clear();
        bookTitleTextField.clear(); 
        bookDescriptionTextArea.clear();
        bookDatePicker.setValue(LocalDate.now());
        
        bookMajorFormsComboBox.getSelectionModel().select(-1);
        bookGenresComboBox.getSelectionModel().select(-1);
        bookColorTextField.clear();
        bookNoOfPagesTextField.clear(); 
        bookShelfComboBox.getSelectionModel().select(-1);
        
        bookISBNTextField.requestFocus();
        
        this.getBookTableViewEvent();
    }
    // SAVE BOOK 
    public void saveBookEvent() {
        
        try {
            String sql = "INSERT INTO book_table            " +
                         "(book_isbn,                       " + 
                         " book_title,                      " +
                         " book_description,                " +
                         " book_date,                       " + 
                         " book_major,                      " + 
                         " book_genres,                     " + 
                         " book_color,                      " + 
                         " book_noOfPages,                  " + 
                         " book_shelf)                      " +
                         "VALUES(?,?,?,?,?,?,?,?,?)";
                        
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, bookISBNTextField.getText());
            preparedStatement.setString(2, bookTitleTextField.getText());
            preparedStatement.setString(3, bookDescriptionTextArea.getText());
            preparedStatement.setDate(4, bookDate);
            preparedStatement.setString(5, bookMajorFormsComboBox.getSelectionModel().getSelectedIndex() == -1 ? "N/A": bookMajorFormsComboBox.getSelectionModel().getSelectedItem().toString());
            preparedStatement.setString(6, bookGenresComboBox.getSelectionModel().getSelectedIndex() == -1 ? "N/A": bookGenresComboBox.getSelectionModel().getSelectedItem().toString());
            preparedStatement.setString(7, bookColorTextField.getText().isEmpty() ? "N/A": bookColorTextField.getText());
            preparedStatement.setInt(8, Integer.parseInt(bookNoOfPagesTextField.getText().isEmpty() ? "0": bookNoOfPagesTextField.getText()));
            preparedStatement.setString(9, bookShelfComboBox.getSelectionModel().getSelectedIndex() == -1 ? "N/A": bookShelfComboBox.getSelectionModel().getSelectedItem().toString());
                        
            preparedStatement.executeUpdate();
            
            this.clearBookFieldEvent();
            
            Notification.notificationEvent(stackpane, "Add Book", "Book has been successfully added!", "SUCCESSFUL");
            
        } 
        catch (SQLException ex) {
            
            Notification.notificationEvent(stackpane, "Saving Book Failed", "Sql Exception: " + ex.getMessage() , "ERROR");
        }
    }
    // UPDATE BOOK 
    public void updateBookEvent() {
        
        try {
        
            String sql = "UPDATE book_table           " +
                         "SET book_isbn          = ?, " + 
                         "    book_title         = ?, " +
                         "    book_description   = ?, " +
                         "    book_date          = ?, " +
                         "    book_major         = ?, " + 
                         "    book_genres        = ?, " + 
                         "    book_color         = ?, " + 
                         "    book_noOfPages     = ?, " + 
                         "    book_shelf         = ?  " +    
                         "WHERE book_id = ?" ;
            
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, bookISBNTextField.getText());
            preparedStatement.setString(2, bookTitleTextField.getText());
            preparedStatement.setString(3, bookDescriptionTextArea.getText());
            preparedStatement.setDate(4, bookDate);
            preparedStatement.setString(5, bookMajorFormsComboBox.getSelectionModel().getSelectedIndex() == -1 ? "N/A": bookMajorFormsComboBox.getSelectionModel().getSelectedItem().toString());
            preparedStatement.setString(6, bookGenresComboBox.getSelectionModel().getSelectedIndex() == -1 ? "N/A": bookGenresComboBox.getSelectionModel().getSelectedItem().toString());
            preparedStatement.setString(7, bookColorTextField.getText().isEmpty() ? "N/A": bookColorTextField.getText());
            preparedStatement.setString(8, bookNoOfPagesTextField.getText().isEmpty() ? "0": bookColorTextField.getText());
            preparedStatement.setInt(8, Integer.parseInt(bookNoOfPagesTextField.getText().isEmpty() ? "0": bookNoOfPagesTextField.getText()));
            preparedStatement.setString(9, bookShelfComboBox.getSelectionModel().getSelectedIndex() == -1 ? "N/A": bookShelfComboBox.getSelectionModel().getSelectedItem().toString());
            preparedStatement.setString(10, bookIDnoTextField.getText());
            preparedStatement.executeUpdate();
            
            this.clearBookFieldEvent();
            
            Notification.notificationEvent(stackpane, "Update Book", "Book has been successfully updated!", "SUCCESSFUL");
        }
        catch (SQLException ex) {
            
            Notification.notificationEvent(stackpane, "Updating Book Failed", "Sql Exception: " + ex.getMessage() , "ERROR");
        }
    }
    // DELETE BOOk
    public void deleteBookEvent(){
        
        try {
            
            String sql = "DELETE FROM book_table " +
                         "WHERE book_id = ?";
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, bookIDnoTextField.getText());
            preparedStatement.executeUpdate();
            
            this.clearBookFieldEvent();
            
            Notification.notificationEvent(stackpane, "Delete Book", "Book has been successfully deleted!", "SUCCESSFUL");
        } 
        catch (SQLException ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // FETCH BOOK TABLE VIEW
    public void getBookTableViewEvent() {
        
        try {
            
            bookList.clear();
            
            String sql = "SELECT * FROM book_table";
            PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {                
                
                bookList.add(new Book(rs.getString("book_id"),
                                      rs.getString("book_isbn"),
                                      rs.getString("book_title"), 
                                      rs.getString("book_description"), 
                                      rs.getString("book_date"),
                                      rs.getString("book_major"),
                                      rs.getString("book_genres"),
                                      rs.getString("book_color"), 
                                      rs.getString("book_noOfPages"), 
                                      rs.getString("book_shelf")));
            }
            TreeItem<Book> root = new RecursiveTreeItem<>(bookList, RecursiveTreeObject::getChildren);
            bookTableView.getColumns().setAll(bookIDnoColumn, 
                                              bookISBNDateColumn,
                                              bookTitleDateColumn, 
                                              bookDescriptionColumn, 
                                              bookDateColumn,
                                              bookMajorColumn,
                                              bookGenresColumn,
                                              bookColorColumn,
                                              bookNoOfPagesColumn,
                                              bookShelfColumn);
            bookTableView.setRoot(root);
            bookTableView.setShowRoot(false);
        }
        catch (SQLException ex) {
            Notification.notificationEvent(stackpane, "Fetching Book Table View Failed", "Sql Exception: " + ex.getMessage() , "ERROR");
        }
    }    
    // =====================================================================
    // =====================================================================
    // =====================================================================  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        dbConn = new DatabaseConnection();
        dbConn.connectDatabase();
        
        validator = new RequiredFieldValidator();
        validator.setMessage("Required Field");        
            
        bookISBNTextField.getValidators().add(validator);
        bookTitleTextField.getValidators().add(validator);
        bookDescriptionTextArea.getValidators().add(validator);
        bookDatePicker.getValidators().add(validator);
        
        bookDatePicker.setValue(LocalDate.now());
        bookDate = java.sql.Date.valueOf(bookDatePicker.getValue());
                
        bookMajorFormsComboBox.getItems().addAll("Novel","Poem","Drama","Short Story","Novella");
        bookGenresComboBox.getItems().addAll("Comedy","Drama","Epic","Erotic","Nonsense","Tragedy"); 
        bookShelfComboBox.getItems().addAll("Circulation");
        // =====================================================================
        // =====================================================================
        // =====================================================================        
        bookIDnoColumn = new JFXTreeTableColumn<>();
        bookIDnoColumn.setText("ID");
        bookIDnoColumn.setPrefWidth(bookTableView.getPrefWidth() / 7);
        bookIDnoColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if(bookIDnoColumn.validateValue(param)) return param.getValue().getValue().id;
            else return bookIDnoColumn.getComputedValue(param);
        });
        bookISBNDateColumn = new JFXTreeTableColumn<>();
        bookISBNDateColumn.setText("ISBN");
        bookISBNDateColumn.setPrefWidth(bookTableView.getPrefWidth() / 4);
        bookISBNDateColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if(bookISBNDateColumn.validateValue(param)) return param.getValue().getValue().isbn ;
            else return bookISBNDateColumn.getComputedValue(param);
        });
        bookTitleDateColumn = new JFXTreeTableColumn<>();
        bookTitleDateColumn.setText("Title");
        bookTitleDateColumn.setPrefWidth(bookTableView.getPrefWidth() / 4);
        bookTitleDateColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if(bookTitleDateColumn.validateValue(param)) return param.getValue().getValue().title;
            else return bookTitleDateColumn.getComputedValue(param);
        });
        bookDescriptionColumn = new JFXTreeTableColumn<>();
        bookDescriptionColumn.setText("Description");
        bookDescriptionColumn.setPrefWidth(bookTableView.getPrefWidth() / 4);
        bookDescriptionColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if(bookDescriptionColumn.validateValue(param)) return param.getValue().getValue().description;
            else return bookDescriptionColumn.getComputedValue(param);
        });
        bookDateColumn = new JFXTreeTableColumn<>();
        bookDateColumn.setText("Book Added");
        bookDateColumn.setPrefWidth(bookTableView.getPrefWidth() / 4);
        bookDateColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if(bookDateColumn.validateValue(param)) return param.getValue().getValue().date;
            else return bookDateColumn.getComputedValue(param);
        });
        bookMajorColumn = new JFXTreeTableColumn<>();
        bookMajorColumn.setText("Book Major");
        bookMajorColumn.setPrefWidth(bookTableView.getPrefWidth() / 4);
        bookMajorColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if(bookMajorColumn.validateValue(param)) return param.getValue().getValue().major;
            else return bookMajorColumn.getComputedValue(param);
        });
        bookGenresColumn = new JFXTreeTableColumn<>();
        bookGenresColumn.setText("Book Genres");
        bookGenresColumn.setPrefWidth(bookTableView.getPrefWidth() / 4);
        bookGenresColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if(bookGenresColumn.validateValue(param)) return param.getValue().getValue().genres;
            else return bookGenresColumn.getComputedValue(param);
        });
        bookColorColumn = new JFXTreeTableColumn<>();
        bookColorColumn.setText("Book Color");
        bookColorColumn.setPrefWidth(bookTableView.getPrefWidth() / 4);
        bookColorColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if(bookColorColumn.validateValue(param)) return param.getValue().getValue().color;
            else return bookColorColumn.getComputedValue(param);
        });
        bookNoOfPagesColumn = new JFXTreeTableColumn<>();
        bookNoOfPagesColumn.setText("No Of Pages");
        bookNoOfPagesColumn.setPrefWidth(bookTableView.getPrefWidth() / 4);
        bookNoOfPagesColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if(bookNoOfPagesColumn.validateValue(param)) return param.getValue().getValue().noOfPages;
            else return bookNoOfPagesColumn.getComputedValue(param);
        });
        bookShelfColumn = new JFXTreeTableColumn<>();
        bookShelfColumn.setText("Shelf");
        bookShelfColumn.setPrefWidth(bookTableView.getPrefWidth() / 4);
        bookShelfColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if(bookShelfColumn.validateValue(param)) return param.getValue().getValue().shelf;
            else return bookShelfColumn.getComputedValue(param);
        });
        this.getBookTableViewEvent();
        bookTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<Book>> observable, TreeItem<Book> oldValue, TreeItem<Book> newValue) -> {
        
            try {
                
                bookIDnoTextField.setText("" + newValue.getValue().id.getValue());
                bookISBNTextField.setText("" + newValue.getValue().isbn.getValue());
                bookTitleTextField.setText("" + newValue.getValue().title.getValue());
                bookDescriptionTextArea.setText("" + newValue.getValue().description.getValue());
                
                LocalDate dt = LocalDate.parse(newValue.getValue().date.getValue());
                bookDatePicker.setValue(dt);

                bookMajorFormsComboBox.getSelectionModel().select(newValue.getValue().major.getValue());
                bookGenresComboBox.getSelectionModel().select(newValue.getValue().genres.getValue()  );
                bookColorTextField.setText(newValue.getValue().color.getValue());
                bookNoOfPagesTextField.setText(newValue.getValue().noOfPages.getValue());
                bookShelfComboBox.getSelectionModel().select(newValue.getValue().shelf.getValue());
            }
            catch(NullPointerException ex) {}
        });
        
        bookTableView.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 2) {
                              
                this.openAuthorBookWindowEvent();
            }
        });
        
        bookTableView.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                 this.openAuthorBookWindowEvent();
            }
        });
    }
    
    public class Book extends RecursiveTreeObject<Book> {
        
        SimpleStringProperty id;
        SimpleStringProperty isbn;
        SimpleStringProperty title;
        SimpleStringProperty description;
        SimpleStringProperty date;
        SimpleStringProperty major;
        SimpleStringProperty genres;
        SimpleStringProperty color;
        SimpleStringProperty noOfPages;
        SimpleStringProperty shelf;
              
        public Book(String id, 
                    String isbn, 
                    String title, 
                    String description, 
                    String date,
                    String major,
                    String genres,
                    String color,
                    String noOfPages,
                    String shelf) {
            
            this.id = new SimpleStringProperty(id);
            this.isbn = new SimpleStringProperty(isbn);
            this.title = new SimpleStringProperty(title);
            this.description = new SimpleStringProperty(description);
            this.date = new SimpleStringProperty(date);
            this.major = new SimpleStringProperty(major);
            this.genres = new SimpleStringProperty(genres);
            this.color = new SimpleStringProperty(color);
            this.noOfPages = new SimpleStringProperty(noOfPages);
            this.shelf = new SimpleStringProperty(shelf);
        }
    }
}
