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
public class AccountsRecievable extends javax.swing.JFrame {

    /**
     * Creates new form AccountsRecievable
     */
    public AccountsRecievable() {
        initComponents();
        ImageIcon imgicon=new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        this.setTitle("Accounts Recievable");
        display_recievables();
        total_all_recievable();
    }
    int cust_id;
    
    private void display_recievables(){
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("SELECT *, SUM(salesAmount) AS payable FROM credit_sales INNER JOIN registered_customer ON credit_sales.customerID=registered_customer.id GROUP BY(credit_sales.recieptNumber) ORDER BY(registered_customer.full_name) ASC LIMIT 100");
            ResultSet rs=pst.executeQuery();
            DefaultTableModel dtm=(DefaultTableModel)ListOfRecievables.getModel();
            dtm.setRowCount(0);
            while(rs.next()){
                Object recievable[]={rs.getString("recieptNumber"), rs.getString("full_name"), rs.getDouble("payable"), rs.getString("salesCashier"), rs.getString("salesDate")};
                dtm.addRow(recievable);
            }
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void search_recievables(){
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("SELECT *, SUM(salesAmount) AS payable FROM credit_sales INNER JOIN registered_customer ON credit_sales.customerID=registered_customer.id WHERE registered_customer.full_name LIKE '%"+SearchName.getText()+"%' GROUP BY(credit_sales.recieptNumber) ORDER BY(registered_customer.full_name) ASC LIMIT 100");
            ResultSet rs=pst.executeQuery();
            DefaultTableModel dtm=(DefaultTableModel)ListOfRecievables.getModel();
            dtm.setRowCount(0);
            while(rs.next()){
                Object recievable[]={rs.getString("recieptNumber"), rs.getString("full_name"), rs.getDouble("payable"), rs.getString("salesCashier"), rs.getString("salesDate")};
                dtm.addRow(recievable);
            }
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void total_all_recievable(){
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("SELECT sum(salesAmount) AS total_recievable FROM credit_sales");
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                AmountRecievable.setText(Double.toString(rs.getDouble("total_recievable"))+"0");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void move_sales(){
        String datePaid = POSController.DateLabel.getText();
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("INSERT INTO sales(salesDate,customerID,Quantity,menuItem,salesCashier,menuID,recieptNumber,salesAmount,discount) SELECT salesDate,customerID,Quantity,menuItem,salesCashier,menuID,recieptNumber,salesAmount,discount FROM credit_sales WHERE recieptNumber='"+OR.getText()+"'");
            pst.executeUpdate();
            try{
                PreparedStatement pst_del=con.prepareStatement("DELETE FROM credit_sales WHERE recieptNumber='"+OR.getText()+"'");
                pst_del.executeUpdate();
                
                try{
                    PreparedStatement pst_update_sales=con.prepareStatement("UPDATE sales SET cash=?, customerChange=?, salesDate =? WHERE recieptNumber='"+OR.getText()+"'");
                    pst_update_sales.setDouble(1, Double.parseDouble(Cash.getText()));
                    pst_update_sales.setDouble(2, Double.parseDouble(Change.getText()));
                    pst_update_sales.setString(3, datePaid);
                    pst_update_sales.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Account with Invoice number "+OR.getText()+" successfully settled. ", "Success ", JOptionPane.INFORMATION_MESSAGE);
                    
                    CustomerName.setText("");
                    OR.setText("");
                    Cash.setText("");
                    Amount.setText("");
                    Change.setText("0.00");
                    
                    display_recievables();
                    total_all_recievable();
                }
                catch(HeadlessException | NumberFormatException | SQLException ex){
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
            catch(SQLException ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void get_customer_id(){
        int selectedRow=ListOfRecievables.getSelectedRow();
        String cust_name=ListOfRecievables.getModel().getValueAt(selectedRow, 1).toString();
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("SELECT id FROM registered_customer WHERE full_name='"+cust_name+"'");
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                cust_id=rs.getInt("id");
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

        jPanel5 = new javax.swing.JPanel();
        ExpenseManagement = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListOfRecievables = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        CustomerName = new javax.swing.JLabel();
        Amount = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        OR = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        Cash = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        Change = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        AddButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        AmountRecievable = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        SearchName = new javax.swing.JTextField();
        View = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ExpenseManagement.setBackground(new java.awt.Color(0, 255, 255));
        ExpenseManagement.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ListOfRecievables.setBackground(new java.awt.Color(0, 0, 0));
        ListOfRecievables.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ListOfRecievables.setForeground(new java.awt.Color(255, 204, 0));
        ListOfRecievables.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OR Number", "Customer", "Amount", "Issued By", "Credit Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ListOfRecievables.setRowHeight(24);
        ListOfRecievables.setSelectionBackground(new java.awt.Color(0, 204, 0));
        ListOfRecievables.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ListOfRecievablesMouseClicked(evt);
            }
        });
        ListOfRecievables.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ListOfRecievablesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(ListOfRecievables);
        if (ListOfRecievables.getColumnModel().getColumnCount() > 0) {
            ListOfRecievables.getColumnModel().getColumn(1).setPreferredWidth(200);
            ListOfRecievables.getColumnModel().getColumn(3).setPreferredWidth(200);
        }

        ExpenseManagement.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, 750, 230));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CustomerName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        CustomerName.setForeground(new java.awt.Color(0, 204, 0));
        CustomerName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(CustomerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 450, 30));

        Amount.setEditable(false);
        Amount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        Amount.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel2.add(Amount, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 210, 30));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setText("Amount");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, 28));

        OR.setEditable(false);
        OR.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        OR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ORFocusLost(evt);
            }
        });
        jPanel2.add(OR, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 210, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setText("Change");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 60, -1, 28));

        Cash.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        Cash.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Cash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CashActionPerformed(evt);
            }
        });
        jPanel2.add(Cash, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 210, 30));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setText("Cash");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, 28));

        Change.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Change.setForeground(new java.awt.Color(255, 51, 51));
        Change.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Change.setText("0.00");
        jPanel2.add(Change, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 90, 180, 40));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setText("OR Number");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 24));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 540, 170));

        AddButton.setBackground(new java.awt.Color(255, 51, 51));
        AddButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        AddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/AddMenu.png"))); // NOI18N
        AddButton.setText("Settle");
        AddButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        jPanel1.add(AddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, 180, 40));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 255, 0));
        jLabel9.setText("Amount Recievable");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 60, 140, 40));

        AmountRecievable.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        AmountRecievable.setForeground(new java.awt.Color(255, 0, 0));
        AmountRecievable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(AmountRecievable, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 100, 180, 40));

        ExpenseManagement.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 750, 190));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 51));
        jLabel2.setText("ACCOUNTS RECIEVABLE");
        jPanel3.add(jLabel2);

        ExpenseManagement.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 750, 30));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Search_48px.png"))); // NOI18N
        ExpenseManagement.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 250, -1, 40));

        SearchName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        SearchName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                SearchNameFocusLost(evt);
            }
        });
        SearchName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                SearchNameKeyTyped(evt);
            }
        });
        ExpenseManagement.add(SearchName, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 250, 280, -1));

        View.setText("View Purchases");
        View.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewActionPerformed(evt);
            }
        });
        ExpenseManagement.add(View, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 120, 30));

        jPanel5.add(ExpenseManagement, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 770, 530));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 550));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ListOfRecievablesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ListOfRecievablesMouseClicked
        int selectedRow=ListOfRecievables.getSelectedRow();
        String or=ListOfRecievables.getModel().getValueAt(selectedRow, 0).toString();
        double amount=Double.parseDouble(ListOfRecievables.getModel().getValueAt(selectedRow, 2).toString());
        String name=ListOfRecievables.getModel().getValueAt(selectedRow, 1).toString();
        
        CustomerName.setText(name);
        OR.setText(or);
        Amount.setText(Double.toString(amount)+"0");
        get_customer_id();
    }//GEN-LAST:event_ListOfRecievablesMouseClicked

    private void ListOfRecievablesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ListOfRecievablesKeyPressed
       
    }//GEN-LAST:event_ListOfRecievablesKeyPressed

    private void ORFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ORFocusLost

    }//GEN-LAST:event_ORFocusLost

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        if(CustomerName.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please select account to settle. ", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else if(Cash.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please enter cash tendered first and press ENTER KEY. ", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else{
            int update=JOptionPane.showConfirmDialog(null, "Are you sure? you want to settle this account? ", "Please verify. ", JOptionPane.YES_NO_OPTION);
            if(update==JOptionPane.YES_OPTION){
                move_sales();
            }
        }

    }//GEN-LAST:event_AddButtonActionPerformed

    private void SearchNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchNameFocusLost

    private void SearchNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchNameKeyTyped
        search_recievables();
    }//GEN-LAST:event_SearchNameKeyTyped

    private void CashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CashActionPerformed
        double amount_payable=Double.parseDouble(Amount.getText());
        double cash=Double.parseDouble(Cash.getText());
        
        if(amount_payable>cash){
            JOptionPane.showMessageDialog(null, "Invalid Payment Amount! ", "Error! ", JOptionPane.ERROR_MESSAGE);
            Cash.setText("");
        }
        else if(Cash.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Invalid Payment Amount! ", "Error! ", JOptionPane.ERROR_MESSAGE);
        }
        else{
            double change=cash-amount_payable;
            Change.setText(Double.toString(change)+"0");
        }
        
    }//GEN-LAST:event_CashActionPerformed

    private void ViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewActionPerformed
        if(cust_id==0){
            JOptionPane.showMessageDialog(null, "Please Select Account to view first!","Hi there! ", JOptionPane.INFORMATION_MESSAGE);
            
        }
        else{
            ListOfUtangItems lot=new ListOfUtangItems();
            lot.dispayUtang(cust_id);
            lot.setVisible(true);
        }
    }//GEN-LAST:event_ViewActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AccountsRecievable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AccountsRecievable().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JFormattedTextField Amount;
    private javax.swing.JLabel AmountRecievable;
    private javax.swing.JFormattedTextField Cash;
    private javax.swing.JLabel Change;
    private javax.swing.JLabel CustomerName;
    public javax.swing.JPanel ExpenseManagement;
    private javax.swing.JTable ListOfRecievables;
    private javax.swing.JTextField OR;
    private javax.swing.JTextField SearchName;
    private javax.swing.JButton View;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
