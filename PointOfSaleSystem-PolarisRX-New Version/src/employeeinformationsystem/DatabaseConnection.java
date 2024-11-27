/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeeinformationsystem;
    
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost/conveniencestore";
    static final String USER="root";
    static final String PASS="020894mpos";
    
     public static Connection DBConnect(){
        Connection con;
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
       
           
           con=DriverManager.getConnection(DB_URL,USER,PASS);
           
           return con;
           
            }
        catch(ClassNotFoundException | SQLException ex){
            
        JOptionPane.showMessageDialog(null, ex);
        return null;
        
        }
        
      
     
 
     
       
    
    }
}
