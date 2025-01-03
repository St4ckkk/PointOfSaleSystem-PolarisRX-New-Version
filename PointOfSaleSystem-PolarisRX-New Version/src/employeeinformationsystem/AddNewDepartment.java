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
public class AddNewDepartment extends javax.swing.JFrame {

    /**
     * Creates new form AddNewUser
     */
    public AddNewDepartment() {
        initComponents();
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        specification = new javax.swing.JTextField();
        departmenthead = new javax.swing.JTextField();
        departmentname = new javax.swing.JTextField();
        usernamestatus = new javax.swing.JLabel();
        emailwarning = new javax.swing.JLabel();
        loc = new javax.swing.JTextField();
        save = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FrameBackGround.setBackground(new java.awt.Color(0, 0, 0));
        FrameBackGround.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)), "DEPARMENT INFO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Century Gothic", 1, 24), new java.awt.Color(51, 255, 255))); // NOI18N
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 255, 204));
        jLabel4.setText("Department Head");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 255, 204));
        jLabel5.setText("Department Name");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 255, 204));
        jLabel6.setText("Specification");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 255, 204));
        jLabel7.setText("Location");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 220, -1, -1));

        specification.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                specificationFocusLost(evt);
            }
        });
        jPanel2.add(specification, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 340, 30));
        jPanel2.add(departmenthead, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 340, 30));

        departmentname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                departmentnameFocusLost(evt);
            }
        });
        departmentname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departmentnameActionPerformed(evt);
            }
        });
        jPanel2.add(departmentname, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 110, 340, 30));
        jPanel2.add(usernamestatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 160, 130, 30));
        jPanel2.add(emailwarning, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 130, 30));

        loc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                locFocusLost(evt);
            }
        });
        jPanel2.add(loc, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 340, 30));

        save.setBackground(new java.awt.Color(0, 204, 51));
        save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/employeeinformationsystem/Icons/Add Link_40px.png"))); // NOI18N
        save.setText("ADD");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        jPanel2.add(save, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 250, 130, 40));

        FrameBackGround.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 600, 300));

        getContentPane().add(FrameBackGround, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 630, 330));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
       AddDepartment();
        
        
    }//GEN-LAST:event_saveActionPerformed

    private void departmentnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departmentnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_departmentnameActionPerformed

    private void specificationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_specificationFocusLost

      
      
      
      
        
    }//GEN-LAST:event_specificationFocusLost

    
    private void departmentnameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_departmentnameFocusLost
   
    }//GEN-LAST:event_departmentnameFocusLost

    private void locFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_locFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_locFocusLost
    private void AddDepartment(){
        
       if(departmentname.getText().equals("")) {
        JOptionPane.showMessageDialog(null, "Department Name must not Empty!");
       
       
       }
       else{
          Connection conn=DatabaseConnection.DBConnect();
     
        String addDept = "INSERT INTO Department(DepartmentHead,DepartmentName,Specification,Location)"+"VALUES(?,?,?,?)";
        try{
            PreparedStatement pstmt=conn.prepareStatement(addDept);
            
            pstmt.setString(1,departmenthead.getText());
            pstmt.setString(2,departmentname.getText());
            pstmt.setString(3,specification.getText());
            pstmt.setString(4,loc.getText());
            
            pstmt.executeUpdate();
            int selectedOption = JOptionPane.showConfirmDialog(null, 
                                  "Department added successfully! \n Do you want to add another Department?", 
                                  "Choose", 
                                  JOptionPane.YES_NO_OPTION); 
                            if (selectedOption == JOptionPane.YES_OPTION) {
                                departmenthead.setText("");
                                departmentname.setText("");
                                specification.setText("");
                                loc.setText("");
                            
                            }
                            else{
                                AdminDashBoard ad=new AdminDashBoard();
                                ad.setVisible(true);
                                dispose();
                            }
            
        }
        
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        } 
       }
    }
 
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddNewDepartment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddNewDepartment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FrameBackGround;
    private javax.swing.JTextField departmenthead;
    private javax.swing.JTextField departmentname;
    private javax.swing.JLabel emailwarning;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField loc;
    private javax.swing.JButton save;
    private javax.swing.JTextField specification;
    private javax.swing.JLabel usernamestatus;
    // End of variables declaration//GEN-END:variables
}
