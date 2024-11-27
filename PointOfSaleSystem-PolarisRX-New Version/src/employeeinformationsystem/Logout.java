/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeeinformationsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author pswor
 */
public class Logout {
     
            

    public void Logout() {
        try{
            Connection conn=DatabaseConnection.DBConnect();
            String SetStatus="UPDATE ProgramUser SET Status='Inactive' WHERE Status='Active'";
            PreparedStatement pst=conn.prepareStatement(SetStatus);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Successfuly Logout");
        
        }
        catch(SQLException ex){
                JOptionPane.showMessageDialog(null, ex);
        }
    }
           
        
        
       
}
