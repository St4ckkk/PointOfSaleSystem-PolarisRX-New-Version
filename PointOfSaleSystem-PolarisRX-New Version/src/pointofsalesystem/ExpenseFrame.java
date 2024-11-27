/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsalesystem;

import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Michael Paul Sebando
 */
public class ExpenseFrame extends javax.swing.JFrame {

    /**
     * Creates new form ExpenseFrame
     */
    public ExpenseFrame() {
        initComponents(); 
        ImageIcon imgicon=new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        this.setTitle("Expense Screen");
    }
    
    Connection con=ExpensesDB.DBConnection();
    PreparedStatement pst;
    ResultSet rs;
    String cashier;
    String user;
    String roleType;
    String userFullName;
    void getCashier(String cashiername){
        cashier=cashiername;
    }
    public void loadMerchants(){
        try{
            pst=con.prepareStatement("SELECT merchantName FROM merchants ORDER BY(merchantName) ASC");
            rs=pst.executeQuery();
            while(rs.next()){
                MerchantList.addItem(rs.getString("merchantName"));
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    public void RecordExpense(){
    
    try{
       pst=con.prepareStatement("SELECT * FROM expenses WHERE orNumber='"+OR.getText()+"' AND particular='"+Particular.getText()+"' AND amount='"+Amount.getText()+"'");
       rs=pst.executeQuery();
       
       if(rs.next()){
           JOptionPane.showMessageDialog(null, "The same OR Number, Particular and Amount exist in your recorded expense");
       }
       else{
        try{
            String date=((JTextField)PurchaseDate.getDateEditor().getUiComponent()).getText();
            pst=con.prepareStatement("INSERT INTO expenses(merchant,orNumber,particular,amount,user,purchaseDate,expenseType)"+"VALUES(?,?,?,?,?,?,?)");
            pst.setString(1, MerchantList.getSelectedItem().toString().toUpperCase());
            pst.setString(2, OR.getText());
            pst.setString(3, Particular.getText().toUpperCase());
            pst.setDouble(4, Double.parseDouble(Amount.getText()));
            pst.setString(5, cashier);
            pst.setString(6, date);
            pst.setString(7, ExpenseTypeCombo.getSelectedItem().toString());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Expense with OR Number "+OR.getText()+" is successfully recorded");
            showExpenses();
            setFieldsClear();
        }
        catch(NumberFormatException | SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        }
        }
    catch(HeadlessException | NumberFormatException | SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    public void UpdateExpense(){
        try{
            String date=((JTextField)PurchaseDate.getDateEditor().getUiComponent()).getText();
            pst=con.prepareStatement("UPDATE expenses SET merchant=?,orNumber=?,particular=?,amount=?,user=?,purchaseDate=?,expenseType=? WHERE expenseID='"+exID+"'");
            pst.setString(1, MerchantLabel.getText());
            pst.setString(2, OR.getText());
            pst.setString(3, Particular.getText().toUpperCase());
            pst.setDouble(4, Double.parseDouble(Amount.getText()));
            pst.setString(5, cashier);
            pst.setString(6, date);
            pst.setString(7, ExpenseTypeCombo.getSelectedItem().toString());
            if(roleType.equals("Cashier")){
                JOptionPane.showMessageDialog(null, "Sorry! "+userFullName +", only admin can edit this expense!","Warning",JOptionPane.WARNING_MESSAGE);
            }
            else{
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Expense with OR Number "+OR.getText()+" is successfully updated");
                showExpenses();
                setFieldsClear();
            }
            
        }
        catch(NumberFormatException | SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
       
    }
    public void showExpenses(){
        try{
            pst=con.prepareStatement("SELECT * FROM expenses ORDER BY(timeStamp) DESC LIMIT 200");
            rs=pst.executeQuery();
            DefaultTableModel expenselist=(DefaultTableModel)ListOfExpenses.getModel();
            expenselist.setRowCount(0);
            while(rs.next()){
                Object list[]={rs.getInt("expenseID"),rs.getString("merchant"),rs.getString("particular"),rs.getDouble("amount")+"0",rs.getString("User")};
                expenselist.addRow(list);
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    public void totalExpense(){
        try{
            pst=con.prepareStatement("SELECT sum(amount) AS totalExpenses FROM expenses");
            rs=pst.executeQuery();
            if(rs.next()){
                double expense=rs.getDouble("totalExpenses");
                try{
                    pst=con.prepareStatement("SELECT sum(gross) AS totalPayroll FROM payrollsummary");
                    rs=pst.executeQuery();
                    if(rs.next()){
                        double payroll=rs.getDouble("totalPayroll");
                        double totaExpense=expense+payroll;
                        TotalExpense.setText(Double.toString(totaExpense)+"0");
                    }
                }
                catch(SQLException ex){
                    JOptionPane.showMessageDialog(null, ex);
                }
                
            }
        }
        catch(SQLException ex){
            JOptionPane.showConfirmDialog(null, ex);
        }
    }
    public void totalSales(){
        try{
            pst=con.prepareStatement("SELECT sum(salesAmount) FROM sales");
            rs=pst.executeQuery();
            if(rs.next()){
                double sales=rs.getDouble(1);
                TotalSales.setText(Double.toString(sales)+"0");
            }
        }
        catch(SQLException ex){
            JOptionPane.showConfirmDialog(null, ex);
        }
    }
    public void calculateIncome(){
        try{
            double getSales=Double.parseDouble(TotalSales.getText());
            double getExpense=Double.parseDouble(TotalExpense.getText());
            double TotalIncome=getSales-getExpense;
            Income.setText(Double.toString(TotalIncome)+"0");
            try{
                String income=Income.getText();
                if(income.contains("-")){
                    Income.setForeground(Color.red);
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);
            }
        }
        catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    public void setFieldsClear(){
        MerchantLabel.setVisible(false);
        MerchantList.setVisible(true);
        OR.setText("");
        Amount.setText("");
        Particular.setText("");
        PurchaseDate.getDateEditor().setDate(null);
        MerchantList.setSelectedIndex(0);
        AddButton.setText("Record");
    }
    public void loadExpenseType(){
        try{
            pst=con.prepareStatement("SELECT accountName FROM accountingaccounts WHERE accountType='Credit' ORDER BY accountName ASC");
            rs=pst.executeQuery();
            while(rs.next()){
                ExpenseTypeCombo.addItem(rs.getString("accountName"));
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void Restriction(){
        try{
            pst=con.prepareStatement("SELECT role,fullname FROM AccessLevel WHERE logStatus=1");
            rs=pst.executeQuery();
            if(rs.next()){
                String role=rs.getString("role");
                String FullName=rs.getString("fullname");
                userFullName=FullName;
                roleType=role;
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
        ListOfExpenses = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        OR = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        Particular = new javax.swing.JTextField();
        MerchantList = new javax.swing.JComboBox<>();
        MerchantLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        PurchaseDate = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        ExpenseTypeCombo = new javax.swing.JComboBox<>();
        Amount = new javax.swing.JFormattedTextField();
        AddButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        TotalSales = new javax.swing.JLabel();
        TotalExpense = new javax.swing.JLabel();
        Income = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ExpenseTypeLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setSize(new java.awt.Dimension(790, 440));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ExpenseManagement.setBackground(new java.awt.Color(0, 255, 255));
        ExpenseManagement.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ListOfExpenses.setBackground(new java.awt.Color(0, 0, 0));
        ListOfExpenses.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ListOfExpenses.setForeground(new java.awt.Color(255, 204, 0));
        ListOfExpenses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Expense ID", "Merchant", "Particular", "Amount", "Added By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ListOfExpenses.setSelectionBackground(new java.awt.Color(0, 204, 0));
        ListOfExpenses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ListOfExpensesMouseClicked(evt);
            }
        });
        ListOfExpenses.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ListOfExpensesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(ListOfExpenses);
        if (ListOfExpenses.getColumnModel().getColumnCount() > 0) {
            ListOfExpenses.getColumnModel().getColumn(0).setPreferredWidth(38);
            ListOfExpenses.getColumnModel().getColumn(1).setPreferredWidth(200);
            ListOfExpenses.getColumnModel().getColumn(4).setPreferredWidth(150);
        }

        ExpenseManagement.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 750, 180));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Supplier");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 24));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Purchase Date");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, -1, 20));

        OR.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        OR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ORFocusLost(evt);
            }
        });
        jPanel2.add(OR, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 210, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Type");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, 28));

        Particular.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Particular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ParticularActionPerformed(evt);
            }
        });
        jPanel2.add(Particular, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, 210, 30));

        MerchantList.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        MerchantList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Supplier", "Add New" }));
        MerchantList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MerchantListActionPerformed(evt);
            }
        });
        jPanel2.add(MerchantList, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 210, 30));

        MerchantLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel2.add(MerchantLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 210, 30));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("OR Number");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 24));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setText("Particular");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, -1, 20));

        PurchaseDate.setDateFormatString("yyyy-MM-dd");
        jPanel2.add(PurchaseDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 90, 210, 30));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setText("Amount");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, 28));

        ExpenseTypeCombo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ExpenseTypeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Expense Type", "Add New" }));
        ExpenseTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExpenseTypeComboActionPerformed(evt);
            }
        });
        jPanel2.add(ExpenseTypeCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 130, 210, 30));

        Amount.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jPanel2.add(Amount, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 210, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 540, 170));

        AddButton.setBackground(new java.awt.Color(0, 204, 0));
        AddButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        AddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/AddMenu.png"))); // NOI18N
        AddButton.setText("Record");
        AddButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        jPanel1.add(AddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 20, 180, 40));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Total Sales");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 10, 60, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Total Expenses");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 40, 90, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Income");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 70, 50, -1));

        TotalSales.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TotalSales.setForeground(new java.awt.Color(0, 153, 51));
        TotalSales.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotalSales.setText("0.00");
        jPanel4.add(TotalSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 70, -1));

        TotalExpense.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TotalExpense.setForeground(new java.awt.Color(51, 0, 255));
        TotalExpense.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotalExpense.setText("0.00");
        jPanel4.add(TotalExpense, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 70, -1));

        Income.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        Income.setForeground(new java.awt.Color(255, 204, 0));
        Income.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Income.setText("0.00");
        jPanel4.add(Income, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 70, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 70, 180, 110));

        ExpenseManagement.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 750, 190));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 51));
        jLabel2.setText("EXPENSES FRAME");
        jPanel3.add(jLabel2);

        ExpenseManagement.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 750, 30));

        ExpenseTypeLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ExpenseManagement.add(ExpenseTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 260, 210, 30));

        jPanel5.add(ExpenseManagement, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 770, 440));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 790, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    int exID;
    private void ListOfExpensesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ListOfExpensesMouseClicked

        MerchantList.setVisible(false);
        MerchantLabel.setVisible(true);
        int selectedRow=ListOfExpenses.getSelectedRow();
        int expenseID=Integer.parseInt(ListOfExpenses.getModel().getValueAt(selectedRow, 0).toString());
        try{
            
            pst=con.prepareStatement("SELECT * FROM expenses WHERE expenseID='"+expenseID+"'");
            rs=pst.executeQuery();
            if(rs.next()){
                int eID=rs.getInt("expenseID");
                String merchant=rs.getString("merchant");
                String or=rs.getString("orNumber");
                String particular=rs.getString("particular");
                Date pDate=rs.getDate("purchaseDate");
                double amount=rs.getDouble("amount");
                try{
                    exID=eID;
                    MerchantLabel.setText(merchant);
                    OR.setText(or);
                    Amount.setText(Double.toString(amount));
                    Particular.setText(particular);
                    PurchaseDate.getDateEditor().setDate(pDate);
                    AddButton.setText("Update");
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_ListOfExpensesMouseClicked

    private void ORFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ORFocusLost
        
    }//GEN-LAST:event_ORFocusLost

    private void AmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AmountKeyTyped
         char c=evt.getKeyChar();
        if(!((c>='0' &&(c <= '9') ))){
           getToolkit().beep();
           evt.consume();
        }
    }//GEN-LAST:event_AmountKeyTyped

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed

            if(AddButton.getText().equals("Record")){
                    String date=((JTextField)PurchaseDate.getDateEditor().getUiComponent()).getText();
                    if(MerchantList.getSelectedItem().toString().equals("Select Merchant")){
                        JOptionPane.showMessageDialog(null, "Please select Merchant first");
                    }
                    else if(Amount.getText().equals("")){
                        JOptionPane.showMessageDialog(null,"Please enter amount");
                    }
                    else if(Particular.getText().equals("")){
                        JOptionPane.showMessageDialog(null, "Please specify particular");
                    }
                    else if(date.isEmpty()){
                        JOptionPane.showMessageDialog(null, "Please provide purchase date");
                    }
                    else{
                        RecordExpense();
                        totalExpense();
                        totalSales();
                        calculateIncome();
                    }
            }
            
            else if(AddButton.getText().equals("Update")){
                        UpdateExpense(); 
            }
   
    }//GEN-LAST:event_AddButtonActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        loadMerchants();
        showExpenses();
        totalExpense();
        totalSales();
        calculateIncome();
        loadExpenseType();
    }//GEN-LAST:event_formWindowOpened

    private void ParticularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ParticularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ParticularActionPerformed

    private void ListOfExpensesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ListOfExpensesKeyPressed
        char c = evt.getKeyChar();
      int selectedRow=ListOfExpenses.getSelectedRow();
      if (c == java.awt.event.KeyEvent.VK_DELETE){
          int option=JOptionPane.showConfirmDialog(null, "Do you want to delete this expense?", "Warning",JOptionPane.YES_NO_OPTION); 
          if(option==JOptionPane.YES_OPTION){
              int expenseID=Integer.parseInt(ListOfExpenses.getModel().getValueAt(selectedRow, 0).toString());
              try{
                  pst=con.prepareStatement("DELETE FROM expenses WHERE expenseID='"+expenseID+"'");
                  pst.executeUpdate();
                  JOptionPane.showMessageDialog(null, "Expense successfully removed from records");
                  showExpenses();
              }
              catch(SQLException ex){
                  JOptionPane.showMessageDialog(null, ex);
              }
          }
      }
    }//GEN-LAST:event_ListOfExpensesKeyPressed

    private void MerchantListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MerchantListActionPerformed

        if(MerchantList.getSelectedItem().toString().equals("Add New")){
            MerchantManagement mm=new MerchantManagement();
            mm.setVisible(true);
        }
    }//GEN-LAST:event_MerchantListActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        Restriction();
    }//GEN-LAST:event_formWindowActivated

    private void ExpenseTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExpenseTypeComboActionPerformed
        if(ExpenseTypeCombo.getSelectedItem().toString().equals("Add New")){
            AddAccountingAccount aaa=new AddAccountingAccount();
            aaa.setVisible(true);
        }
    }//GEN-LAST:event_ExpenseTypeComboActionPerformed

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
            java.util.logging.Logger.getLogger(ExpenseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExpenseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExpenseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExpenseFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExpenseFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JFormattedTextField Amount;
    public javax.swing.JPanel ExpenseManagement;
    private javax.swing.JComboBox<String> ExpenseTypeCombo;
    private javax.swing.JLabel ExpenseTypeLabel;
    private javax.swing.JLabel Income;
    private javax.swing.JTable ListOfExpenses;
    private javax.swing.JLabel MerchantLabel;
    private javax.swing.JComboBox<String> MerchantList;
    private javax.swing.JTextField OR;
    private javax.swing.JTextField Particular;
    private com.toedter.calendar.JDateChooser PurchaseDate;
    private javax.swing.JLabel TotalExpense;
    private javax.swing.JLabel TotalSales;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
