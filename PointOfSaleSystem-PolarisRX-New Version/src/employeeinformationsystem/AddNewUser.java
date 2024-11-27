/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeeinformationsystem;

import java.awt.Color;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import javax.swing.JOptionPane;

/**
 *
 * @author pswor
 */
public class AddNewUser extends javax.swing.JFrame {

    /**
     * Creates new form AddNewUser
     */
    public AddNewUser() {
        initComponents();
    }
    String userName;
    private void viewUser(){
         try {
                Connection conn=DatabaseConnection.DBConnect();
                String compare="SELECT * FROM ProgramUser WHERE FullName LIKE '%"+completename.getText()+"%'";
                Statement stmnt=conn.createStatement();
                ResultSet resSet=stmnt.executeQuery(compare);
                
                
                if(resSet.next()){
                    completename.setText(resSet.getString("FullName"));
                    emailaddress.setText(resSet.getString("Password"));
                    jusername.setText(resSet.getString("Username"));
                    password.setText(resSet.getString("Password"));
                    userName=resSet.getString("Username");
                    save.setText("Update");
                    }
                  else{
                  usernamestatus.setText("Unknown User!");
                  save.setEnabled(true);
                  usernamestatus.setForeground(Color.white);
                }
        }
        catch(SQLException ex){
         JOptionPane.showMessageDialog(null,ex);
        }
    }
    private void AddUser(){
       
        String fullname=completename.getText();
        String email=emailaddress.getText();
        String username=jusername.getText();
        String pass=password.getText();
        Object usertype=jusertype.getSelectedItem();
        if(completename.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Name Field is Empty!");
        }
        else if(emailaddress.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Email Address is needed for Recovery!");
        }
        else if(jusername.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Username is Empty!");
        }
        else if(password.getText().equals("")){
            JOptionPane.showMessageDialog(null,"Password is Empty!");
        }
        
        else{
            
            if(save.getText().equals("Add")){

                    try{
                        
                        Connection con=DatabaseConnection.DBConnect();
                        Statement stmt=con.createStatement();
                        stmt.executeUpdate("INSERT INTO ProgramUser(Fullname,EmailAddress,Username,Password,UserType) VALUES('"+fullname+"','"+email+"','"+username+"','"+pass+"','"+usertype+"')");
                        
                        JOptionPane.showMessageDialog(null,"Program user has been successfully added!");
                        int selectedOption = JOptionPane.showConfirmDialog(null, 
                                  "Department added successfully! \n Do you want to add another User?", 
                                  "Choose", 
                                  JOptionPane.YES_NO_OPTION); 
                            if (selectedOption == JOptionPane.YES_OPTION) {
                                completename.setText("");
                                emailaddress.setText("");
                                jusername.setText("");
                                password.setText("");
                            
                            }
                            else{
                                AdminDashBoard ad=new AdminDashBoard();
                                ad.setVisible(true);
                                dispose();
                            }
                    }
                    catch(SQLException ex){
                        JOptionPane.showMessageDialog(null,ex);
                    }
            }
            else{
                    
                    try{
                        
                        Connection con=DatabaseConnection.DBConnect();
                        Statement stmt=con.createStatement();
                        stmt.executeUpdate("UPDATE ProgramUser SET Fullname='"+fullname+"',EmailAddress='"+email+"',Username='"+username+"',Password='"+pass+"',UserType='"+usertype+"' WHERE Username='"+userName+"'");
                        
                        JOptionPane.showMessageDialog(null,"Program user has been successfully updated!");
                        
                           
                                completename.setText("");
                                emailaddress.setText("");
                                jusername.setText("");
                                password.setText("");
                                save.setText("Add");
                            
                            
                    }
                    catch(SQLException ex){
                        JOptionPane.showMessageDialog(null,ex);
                    }
            }
       
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        FrameBackGround = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jusertype = new javax.swing.JComboBox<>();
        jusername = new javax.swing.JTextField();
        completename = new javax.swing.JTextField();
        emailaddress = new javax.swing.JTextField();
        password = new javax.swing.JPasswordField();
        usernamestatus = new javax.swing.JLabel();
        emailwarning = new javax.swing.JLabel();
        save = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FrameBackGround.setBackground(new java.awt.Color(0, 0, 0));
        FrameBackGround.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "USER INFO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century Gothic", 1, 24), new java.awt.Color(0, 255, 204))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(0, 255, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 255, 204));
        jLabel2.setText("User Type:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 255, 204));
        jLabel4.setText("Name:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 255, 204));
        jLabel5.setText("Email Add:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 255, 204));
        jLabel6.setText("Username:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 255, 204));
        jLabel7.setText("Password:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 220, -1, -1));

        jusertype.setBackground(new java.awt.Color(0, 153, 153));
        jusertype.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jusertype.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "User" }));
        jPanel2.add(jusertype, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 260, 250, 30));

        jusername.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jusername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jusernameFocusLost(evt);
            }
        });
        jPanel2.add(jusername, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 250, 30));

        completename.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        completename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completenameActionPerformed(evt);
            }
        });
        jPanel2.add(completename, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 340, 30));

        emailaddress.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        emailaddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                emailaddressFocusLost(evt);
            }
        });
        emailaddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailaddressActionPerformed(evt);
            }
        });
        jPanel2.add(emailaddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 250, 30));

        password.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanel2.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 250, 30));

        usernamestatus.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(usernamestatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 150, 30));
        jPanel2.add(emailwarning, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 130, 30));

        save.setBackground(new java.awt.Color(0, 204, 51));
        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/employeeinformationsystem/Icons/Save_40px.png"))); // NOI18N
        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        jPanel2.add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 230, 140, 60));

        FrameBackGround.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 600, 310));

        getContentPane().add(FrameBackGround, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 620, 330));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
       AddUser();
    }//GEN-LAST:event_saveActionPerformed

    private void emailaddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailaddressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailaddressActionPerformed

    private void jusernameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jusernameFocusLost
       
      if(!jusername.getText().equals("")){
        try {
                Connection conn=DatabaseConnection.DBConnect();
                String compare="SELECT Username FROM ProgramUser WHERE Username='"+jusername.getText()+"'";
                Statement stmnt=conn.createStatement();
                ResultSet resSet=stmnt.executeQuery(compare);
                
                
                if(resSet.next()){
                    usernamestatus.setText("Unvailable!");
                    getToolkit().beep();
                    save.setEnabled(false);
                    usernamestatus.setForeground(Color.black);
                    }
                  else{
                  usernamestatus.setText("Available!");
                  save.setEnabled(true);
                  usernamestatus.setForeground(Color.white);
                }
        }
        catch(SQLException ex){
         JOptionPane.showMessageDialog(null,ex);
        }
       }
      
      
      
      
        
    }//GEN-LAST:event_jusernameFocusLost
    

    
    private void emailaddressFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailaddressFocusLost
        if(!emailaddress.getText().contains("@")||!emailaddress.getText().contains(".")){
            emailwarning.setText("Invalid Email!");
            getToolkit().beep();
            save.setEnabled(false);
        }

        else{
            emailwarning.setText("");
            save.setEnabled(true);
        }
    }//GEN-LAST:event_emailaddressFocusLost

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    
    }//GEN-LAST:event_formWindowClosed

    private void completenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completenameActionPerformed
        viewUser();
    }//GEN-LAST:event_completenameActionPerformed
    
 
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddNewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddNewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddNewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddNewUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FrameBackGround;
    private javax.swing.JTextField completename;
    private javax.swing.JTextField emailaddress;
    private javax.swing.JLabel emailwarning;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jusername;
    private javax.swing.JComboBox<String> jusertype;
    private javax.swing.JPasswordField password;
    private javax.swing.JButton save;
    private javax.swing.JLabel usernamestatus;
    // End of variables declaration//GEN-END:variables
}
