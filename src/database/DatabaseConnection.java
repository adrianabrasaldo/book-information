package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
        
    static Connection conn = null;   
    
    public void connectDatabase() {        
       try {        
            //  Database credentials
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/";
            String database = "book_information";
            String username = "root";
            String password = "";

            Class.forName(driver);

            conn = DriverManager.getConnection(url + database, username, password);       
       } 
       catch (ClassNotFoundException ex) {
           
            System.out.println("Class Exception: " + ex.getMessage());
           
           Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
       } 
       catch (SQLException ex) {
           
            System.out.println("Sql Exception: " + ex.getMessage());
                           
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    public void closeConnection() {
        try {
            
            conn.close();
        } 
        catch (SQLException ex) {
            
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Connection getConnection() {
        return conn;
    }    
}
