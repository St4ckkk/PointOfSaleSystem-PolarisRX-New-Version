/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsalesystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Michael Paul Sebando
 */
public class AdminPassword extends javax.swing.JFrame {

    /**
     * Creates new form AdminPassword
     */
    public AdminPassword() {
        initComponents();
        //showOR();
        ImageIcon imgicon=new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        this.setTitle("Void Sales");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        PasswordText = new javax.swing.JPasswordField();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Void Sales");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 50));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Pincode Keyboard_32px.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, -1, -1));

        PasswordText.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        PasswordText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        PasswordText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        PasswordText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordTextActionPerformed(evt);
            }
        });
        jPanel1.add(PasswordText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 210, 40));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Void Sales");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Type Admin Password and Press Enter to Void");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    String or;
    String fullName;
    void getRecipetNumber(String recieptNumber){
        or=recieptNumber;
        JOptionPane.showMessageDialog(null, or);
    }
    private void validateUserType(){
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("SELECT * FROM accesslevel WHERE password='"+PasswordText.getText()+"' AND role='Admin'");
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                fullName=rs.getString("fullname");
                VoidSales();
            }
            else{
                JOptionPane.showMessageDialog(null, "You are not authorize to void this transaction", "Warning",JOptionPane.WARNING_MESSAGE);
                dispose();
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void VoidSales(){
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("INSERT INTO voidsales(salesID,salesDate,customerID,Quantity,menuItem,salesCashier,recieptNumber,salesAmount,discount,cash,customerChange,voidBy) SELECT salesID,salesDate,customerID,Quantity,menuItem,salesCashier,recieptNumber,salesAmount,discount,cash,customerChange,'"+fullName+"' FROM sales WHERE recieptNumber='"+or+"'");
            pst.executeUpdate();
            PreparedStatement pst1 = con.prepareStatement("SELECT Quantity, menuID FROM sales WHERE recieptNumber='"+or+"'");
            ResultSet rs1 = pst1.executeQuery();
            while(rs1.next()){
                int quantityToRestore = rs1.getInt("Quantity");
                int menuID = rs1.getInt("menuID");
                PreparedStatement pst2 = con.prepareStatement("UPDATE stocks_inventory SET available_stock = available_stock + '"+quantityToRestore+"' WHERE prod_id = '"+menuID+"'");
                pst2.executeUpdate();
            }
            removeFromSales();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void removeFromSales(){
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("DELETE FROM sales WHERE recieptNumber='"+or+"' ");
            pst.executeUpdate();
            dispose();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void PasswordTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasswordTextActionPerformed

        if(PasswordText.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Please type Admin password!", "Warning",JOptionPane.WARNING_MESSAGE);
        }
        else{
            validateUserType();
        }
    }//GEN-LAST:event_PasswordTextActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPassword().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField PasswordText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}