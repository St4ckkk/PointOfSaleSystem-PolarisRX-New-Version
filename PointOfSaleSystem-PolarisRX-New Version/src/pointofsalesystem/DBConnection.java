/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsalesystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner;

/**
 *
 * @author Michael Paul Sebando
 */

public class DBConnection {
    static String pass="";
    static String username="";
    static String host="";
    public static void readCredentials() {
        File myObj = new File("settings.ini");
        String text = "";
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                text = text.concat(data + "\n");
                String[] lineVals = data.split(":");
                if (lineVals[0].equalsIgnoreCase("host")) {
                    host = lineVals[1].trim();
                    System.out.println(host);
                } else if (lineVals[0].equalsIgnoreCase("username")) {
                    username = lineVals[1].trim();
                    System.out.println(username);
                } else if (lineVals[0].equalsIgnoreCase("password")) {
                    if(lineVals[1].trim().equals("None")){
                        pass = "";
                    }
                    else{
                        pass = lineVals[1].trim();
                    }
                    
                    System.out.println(pass);
                }

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection DBConnection() {
        readCredentials();
        try {
            
            
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://"+host+"/conveniencestore", username, pass);
            return con;
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Unable to locate Database, Invalid server configuration or not running");
            LoginScreen.isSetUp = false;
//            DatabaseCredentials dbc = new DatabaseCredentials();
//            dbc.setVisible(true);
            //System.exit(0);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Unable to locate Database, Invalid server configuration or not running");
            LoginScreen.isSetUp = false;
            
            //System.exit(0);
        }
        return null;

    }
}
