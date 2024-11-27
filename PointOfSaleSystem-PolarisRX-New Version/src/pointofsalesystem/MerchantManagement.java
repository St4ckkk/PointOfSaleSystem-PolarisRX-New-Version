/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsalesystem;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Michael Paul Sebando
 */
public class MerchantManagement extends javax.swing.JFrame {

    /**
     * Creates new form AddNewMerchant
     */
    public MerchantManagement() {
        initComponents();
        ImageIcon imgicon=new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        this.setTitle("Manage Merchant");
        showMerchantList();
    }
    Connection con=DBConnection.DBConnection();
    PreparedStatement pst;
    ResultSet rs;
    int merchantID;
    private void showMerchantList(){
        try{
            pst=con.prepareStatement("SELECT * FROM merchants");
            rs=pst.executeQuery();
            DefaultTableModel mlist=(DefaultTableModel)MerchantList.getModel();
            mlist.setRowCount(0);
            while(rs.next()){
                Object list[]={rs.getInt("merchantID"),rs.getString("merchantName"),rs.getString("merchantNumber"),rs.getString("contactPerson")};
                mlist.addRow(list);
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void addMerchant(){
        try{
            pst=con.prepareStatement("SELECT * FROM merchants WHERE merchantName='"+MerchantName.getText()+"'");
            rs=pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null, "This Merchant already exist");
            }
            else{
                pst=con.prepareStatement("INSERT INTO merchants(merchantName,merchantNumber,contactPerson,merchantAddress) VALUES(?,?,?,?)");
                pst.setString(1, MerchantName.getText());
                pst.setString(2, MerchantNumber.getText());
                pst.setString(3, ContactPerson.getText());
                pst.setString(4, MerchantAddress.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, MerchantName.getText() +" is successfully added");
                showMerchantList();
                MerchantName.setText("");
                MerchantNumber.setText("");
                MerchantAddress.setText("");
                ContactPerson.setText("");
                
            }
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null, "Please remove ' character");
        }
    }
    private void updateMerchant(){
        try{
           
                pst=con.prepareStatement("UPDATE merchants SET merchantName=?,merchantNumber=?,contactPerson=?,merchantAddress=? WHERE merchantID='"+merchantID+"'");
                pst.setString(1, MerchantName.getText());
                pst.setString(2, MerchantNumber.getText());
                pst.setString(3, ContactPerson.getText());
                pst.setString(4, MerchantAddress.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, MerchantName.getText() +" is successfully updated");
                showMerchantList();
                AddButton.setText("Add");
                MerchantName.setText("");
                MerchantNumber.setText("");
                ContactPerson.setText("");
                MerchantAddress.setText("");
                
            
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void setLabels(){
        int selectedRow=MerchantList.getSelectedRow();
        int id=Integer.parseInt(MerchantList.getModel().getValueAt(selectedRow, 0).toString());
        merchantID=id;
        try{
            pst=con.prepareStatement("SELECT * FROM merchants WHERE merchantID='"+id+"'");
            rs=pst.executeQuery();
            if(rs.next()){
                MerchantName.setText(rs.getString("merchantName"));
                MerchantNumber.setText(rs.getString("merchantNumber"));
                MerchantAddress.setText(rs.getString("merchantAddress"));
                ContactPerson.setText(rs.getString("contactPerson"));
                AddButton.setText("Update");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void showtotalPurchase(){
        int selectedRow=MerchantList.getSelectedRow();
        String merchantName=MerchantList.getModel().getValueAt(selectedRow, 1).toString();
        try{
            pst=con.prepareStatement("SELECT sum(amount) FROM expenses WHERE merchant='"+merchantName+"'");
            rs=pst.executeQuery();
            if(rs.next()){
                double totalPurchases=rs.getDouble(1);
                totalPurchase.setText(Double.toString(totalPurchases)+"0");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        MerchantName = new javax.swing.JTextField();
        MerchantNumber = new javax.swing.JTextField();
        MerchantAddress = new javax.swing.JTextField();
        ContactPerson = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        MerchantList = new javax.swing.JTable();
        AddButton = new javax.swing.JButton();
        totalPurchase = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Supplier");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 23, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Number");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 64, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Address");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Contact Person");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 146, -1, -1));

        MerchantName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(MerchantName, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 20, 302, -1));

        MerchantNumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(MerchantNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 61, 302, -1));

        MerchantAddress.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(MerchantAddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 102, 302, -1));

        ContactPerson.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(ContactPerson, new org.netbeans.lib.awtextra.AbsoluteConstraints(129, 143, 302, -1));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 450, 190));

        MerchantList.setBackground(new java.awt.Color(0, 0, 0));
        MerchantList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MerchantList.setForeground(new java.awt.Color(255, 204, 0));
        MerchantList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier ID", "Supplier Name", "Contact Number", "Contact Person"
            }
        ));
        MerchantList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        MerchantList.setGridColor(new java.awt.Color(0, 0, 0));
        MerchantList.setRowHeight(20);
        MerchantList.setSelectionBackground(new java.awt.Color(0, 204, 0));
        MerchantList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MerchantListMouseClicked(evt);
            }
        });
        MerchantList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MerchantListKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(MerchantList);
        if (MerchantList.getColumnModel().getColumnCount() > 0) {
            MerchantList.getColumnModel().getColumn(0).setPreferredWidth(50);
            MerchantList.getColumnModel().getColumn(1).setPreferredWidth(150);
        }

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 640, 170));

        AddButton.setBackground(new java.awt.Color(0, 204, 0));
        AddButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        AddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Update User_50px.png"))); // NOI18N
        AddButton.setText("Add");
        AddButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        jPanel3.add(AddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 180, 60));

        totalPurchase.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        totalPurchase.setForeground(new java.awt.Color(0, 204, 51));
        totalPurchase.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalPurchase.setText("0.00");
        jPanel3.add(totalPurchase, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 140, 120, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Total Purchase");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, -1, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 660, 390));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 204, 51));
        jLabel5.setText("SUPPLIER MANAGEMENT");
        jPanel5.add(jLabel5);

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 660, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 680, 450));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        if(AddButton.getText().equals("Add")){
            if(MerchantName.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Merchant Name is needed");
            }
            else{
                addMerchant();
            }
        }
        else{
                updateMerchant();
        }
    }//GEN-LAST:event_AddButtonActionPerformed

    private void MerchantListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MerchantListMouseClicked
        setLabels();
        showtotalPurchase();
    }//GEN-LAST:event_MerchantListMouseClicked

    private void MerchantListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MerchantListKeyPressed
        char c = evt.getKeyChar();
      DefaultTableModel dtm=(DefaultTableModel)MerchantList.getModel();
      int selectedRow=MerchantList.getSelectedRow();
      if (c == java.awt.event.KeyEvent.VK_DELETE){
          int mID=Integer.parseInt(MerchantList.getModel().getValueAt(selectedRow, 0).toString());
          String mName=MerchantList.getModel().getValueAt(selectedRow, 1).toString();
          try{
              pst=con.prepareStatement("DELETE FROM merchants WHERE merchantID='"+mID+"'");
              int option=JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this merchant?","Warning",JOptionPane.YES_NO_OPTION);
              if(option==JOptionPane.YES_OPTION){
                  pst.executeUpdate();
                  JOptionPane.showMessageDialog(null, mName+" is successfully deleted!","Warning",JOptionPane.INFORMATION_MESSAGE);
                  MerchantName.setText("");
                  MerchantNumber.setText("");
                  MerchantAddress.setText("");
                  ContactPerson.setText("");
              }
          }
          catch(SQLException ex){
              JOptionPane.showMessageDialog(null, ex);
          }
          
          dtm.removeRow(MerchantList.getSelectedRow());
          
      }
      
    }//GEN-LAST:event_MerchantListKeyPressed

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
            java.util.logging.Logger.getLogger(MerchantManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MerchantManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MerchantManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MerchantManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MerchantManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JTextField ContactPerson;
    private javax.swing.JTextField MerchantAddress;
    private javax.swing.JTable MerchantList;
    private javax.swing.JTextField MerchantName;
    private javax.swing.JTextField MerchantNumber;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel totalPurchase;
    // End of variables declaration//GEN-END:variables
}
