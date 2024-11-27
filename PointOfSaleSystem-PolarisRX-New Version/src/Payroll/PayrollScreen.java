/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Payroll;

import java.awt.HeadlessException;
import java.awt.Point;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import pointofsalesystem.DBConnection;
import pointofsalesystem.POSController;

/**
 *
 * @author Michael Paul Sebando
 */
public class PayrollScreen extends javax.swing.JFrame {
    
    /**
     * Creates new form PayrollScreen
     */
    public PayrollScreen() {
        initComponents();
        showEmployees();
        getOperator();
    }
    Connection con=DBConnection.DBConnection();
    PreparedStatement pst;
    ResultSet rs;
    String operation;
    String date;
    String operator;
    private void showEmployees(){
        try{
           pst=con.prepareStatement("SELECT basicinformation.EmployeeID, Lastname, Firstname, Middlename, Position FROM basicinformation INNER JOIN employmentinformations ON basicinformation.EmployeeID=employmentinformations.EmployeeID ORDER BY basicinformation.Lastname ASC");
           rs=pst.executeQuery();
           DefaultTableModel listEmp=(DefaultTableModel)ListOfEmployeesTable.getModel();
           listEmp.setRowCount(0);
           while(rs.next()){
               String fullname=rs.getString("Lastname")+","+rs.getString("Firstname")+" "+rs.getString("Middlename");
               Object employees[]={rs.getString("EmployeeID"),fullname,rs.getString("Position")};
               listEmp.addRow(employees);
           }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void showDetails(){
        String empID=ListOfEmployeesTable.getModel().getValueAt(ListOfEmployeesTable.getSelectedRow(), 0).toString();
        try{
           pst=con.prepareStatement("SELECT payrollMode, Salary FROM employmentinformations WHERE EmployeeID='"+empID+"'");
           rs=pst.executeQuery();
           if(rs.next()){
               IDNumber.setText(empID);
               EmpName.setText(ListOfEmployeesTable.getModel().getValueAt(ListOfEmployeesTable.getSelectedRow(), 1).toString());
               Position.setText(ListOfEmployeesTable.getModel().getValueAt(ListOfEmployeesTable.getSelectedRow(), 2).toString());
               PayrollMode.setText(rs.getString("payrollMode"));
               Salary.setText(rs.getString("Salary"));
           }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void createPayroll(){
        String PayrollDate=((JTextField)dateChooser.getDateEditor().getUiComponent()).getText();
        DefaultTableModel payrollDetails=(DefaultTableModel)PayrollTable.getModel();
        if(MonthDays.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please enter number of worked days this payroll month","Warning",JOptionPane.WARNING_MESSAGE);
            WorkedHours.setText("");
        }
        else if(Salary.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Please select employee to generate payroll","Warning",JOptionPane.WARNING_MESSAGE);
            WorkedHours.setText("");
        }
        else{
            
            if(operation.equals("Credit")){
                try {
                    double basicSalary=Double.parseDouble(Salary.getText());
                    double workedDays=Double.parseDouble(MonthDays.getText());
                    double dailyRate=basicSalary/workedDays;
                    double hourRate= dailyRate/8;
                    double regOT=hourRate*.25+hourRate;
                    double specHour=hourRate*.30+hourRate;
                    double specOT=specHour*.25+specHour;
                    double holidayHour=hourRate*2;
                    double holidayOT=holidayHour*.25+holidayHour;
                    
                    pst=con.prepareStatement("SELECT ROUND('"+hourRate+"',2)");
                    rs=pst.executeQuery();
                    if(rs.next()){
                        double hourlyRate=rs.getDouble(1);
                        String worktype=WorkedType.getSelectedItem().toString();
                        switch(worktype){
                            case "Select Working Type":
                                JOptionPane.showMessageDialog(null, "Please Select type of duties!", "Warning",JOptionPane.WARNING_MESSAGE);
                                WorkedHours.setText("");
                            break;
                            case "REGULAR DAYS":
                                double workHours=Double.parseDouble(WorkedHours.getText());
                                double salary=workHours*hourlyRate;
                                pst=con.prepareStatement("SELECT ROUND('"+salary+"',2)");
                                rs=pst.executeQuery();
                                rs.next();
                                double finalSalary=rs.getDouble(1);
                                Object fillTable[]={"Regular Day","",finalSalary};
                                
                                WorkedHours.setText("");
                                try{
                                    if(PayrollDate.equals("")){
                                       JOptionPane.showMessageDialog(null, "Please select payroll date first!");
                                    }
                                    else{
                                        pst=con.prepareStatement("INSERT INTO workeddayscalculation(payrollDate,empID,WorkingType,WorkingHours,Amount)"+"VALUES(?,?,?,?,?)");
                                        pst.setString(1, PayrollDate);
                                        pst.setString(2, IDNumber.getText());
                                        pst.setString(3, worktype);
                                        pst.setDouble(4, workHours);
                                        pst.setDouble(5, finalSalary);
                                        pst.executeUpdate();
                                        payrollDetails.addRow(fillTable);
                                    }
                                }
                                catch(HeadlessException ex){
                                    JOptionPane.showMessageDialog(null, ex);
                                }
                            break;
                            case "REGULAR OVERTIME":
                                double regworkHoursOT=Double.parseDouble(WorkedHours.getText());
                                double regsalaryOT=regworkHoursOT*regOT;
                                pst=con.prepareStatement("SELECT ROUND('"+regsalaryOT+"',2)");
                                rs=pst.executeQuery();
                                rs.next();
                                double regfinalSalaryOT=rs.getDouble(1);
                                Object fillTableregOT[]={"Regular Overtime","",regfinalSalaryOT};
                                
                                WorkedHours.setText("");
                                try{
                                    if(PayrollDate.equals("")){
                                       JOptionPane.showMessageDialog(null, "Please select payroll date first!");
                                    }
                                    else{
                                        pst=con.prepareStatement("INSERT INTO workeddayscalculation(payrollDate,empID,WorkingType,WorkingHours,Amount)"+"VALUES(?,?,?,?,?)");
                                        pst.setString(1, PayrollDate);
                                        pst.setString(2, IDNumber.getText());
                                        pst.setString(3, worktype);
                                        pst.setDouble(4, regworkHoursOT);
                                        pst.setDouble(5, regfinalSalaryOT);
                                        pst.executeUpdate();
                                        payrollDetails.addRow(fillTableregOT);
                                    }
                                }
                                catch(HeadlessException ex){
                                    JOptionPane.showMessageDialog(null, ex);
                                }
                            break;
                            case "SPECIAL HOLIDAY":
                                double workHoursspecH=Double.parseDouble(WorkedHours.getText());
                                double salaryspecH=workHoursspecH*specHour;
                                pst=con.prepareStatement("SELECT ROUND('"+salaryspecH+"',2)");
                                rs=pst.executeQuery();
                                rs.next();
                                double finalSalaryspecH=rs.getDouble(1);
                                Object fillTablespecH[]={"Special Holiday","",finalSalaryspecH};
                                
                                WorkedHours.setText("");
                                try{
                                    if(PayrollDate.equals("")){
                                       JOptionPane.showMessageDialog(null, "Please select payroll date first!");
                                    }
                                    else{
                                        pst=con.prepareStatement("INSERT INTO workeddayscalculation(payrollDate,empID,WorkingType,WorkingHours,Amount)"+"VALUES(?,?,?,?,?)");
                                        pst.setString(1, PayrollDate);
                                        pst.setString(2, IDNumber.getText());
                                        pst.setString(3, worktype);
                                        pst.setDouble(4, workHoursspecH);
                                        pst.setDouble(5, finalSalaryspecH);
                                        pst.executeUpdate();
                                        payrollDetails.addRow(fillTablespecH);
                                    }
                                }
                                catch(HeadlessException ex){
                                    JOptionPane.showMessageDialog(null, ex);
                                }
                            break;
                            case "SPECIAL HOLIDAY OVERTIME":
                                double workHoursspecHOT=Double.parseDouble(WorkedHours.getText());
                                double salaryspecHOT=workHoursspecHOT*specOT;
                                pst=con.prepareStatement("SELECT ROUND('"+salaryspecHOT+"',2)");
                                rs=pst.executeQuery();
                                rs.next();
                                double finalSalaryspecHOT=rs.getDouble(1);
                                Object fillTablespecHOT[]={"Special Holiday Overtime","",finalSalaryspecHOT};
                                
                                WorkedHours.setText("");
                                try{
                                    if(PayrollDate.equals("")){
                                       JOptionPane.showMessageDialog(null, "Please select payroll date first!");
                                    }
                                    else{
                                        pst=con.prepareStatement("INSERT INTO workeddayscalculation(payrollDate,empID,WorkingType,WorkingHours,Amount)"+"VALUES(?,?,?,?,?)");
                                        pst.setString(1, PayrollDate);
                                        pst.setString(2, IDNumber.getText());
                                        pst.setString(3, worktype);
                                        pst.setDouble(4, workHoursspecHOT);
                                        pst.setDouble(5, finalSalaryspecHOT);
                                        pst.executeUpdate();
                                        payrollDetails.addRow(fillTablespecHOT);
                                    }
                                }
                                catch(HeadlessException ex){
                                    JOptionPane.showMessageDialog(null, ex);
                                }
                            break;
                            case "HOLIDAY":
                                double workHoursHOL=Double.parseDouble(WorkedHours.getText());
                                double salaryHOL=workHoursHOL*holidayHour;
                                pst=con.prepareStatement("SELECT ROUND('"+salaryHOL+"',2)");
                                rs=pst.executeQuery();
                                rs.next();
                                double finalSalaryHOL=rs.getDouble(1);
                                Object fillTableHOL[]={"Holiday","",finalSalaryHOL};
                                
                                WorkedHours.setText("");
                                try{
                                    if(PayrollDate.equals("")){
                                       JOptionPane.showMessageDialog(null, "Please select payroll date first!");
                                    }
                                    else{
                                        pst=con.prepareStatement("INSERT INTO workeddayscalculation(payrollDate,empID,WorkingType,WorkingHours,Amount)"+"VALUES(?,?,?,?,?)");
                                        pst.setString(1, PayrollDate);
                                        pst.setString(2, IDNumber.getText());
                                        pst.setString(3, worktype);
                                        pst.setDouble(4, workHoursHOL);
                                        pst.setDouble(5, finalSalaryHOL);
                                        pst.executeUpdate();
                                        payrollDetails.addRow(fillTableHOL);
                                    }
                                }
                                catch(HeadlessException ex){
                                    JOptionPane.showMessageDialog(null, ex);
                                }
                            break;
                            case "HOLIDAY OVERTIME":
                                double workHoursHOLOT=Double.parseDouble(WorkedHours.getText());
                                double salaryHOLOT=workHoursHOLOT*holidayOT;
                                pst=con.prepareStatement("SELECT ROUND('"+salaryHOLOT+"',2)");
                                rs=pst.executeQuery();
                                rs.next();
                                double finalSalaryHOLOT=rs.getDouble(1);
                                Object fillTableHOLOT[]={"Holiday Overtime","",finalSalaryHOLOT};
                                
                                WorkedHours.setText("");
                                try{
                                    if(PayrollDate.equals("")){
                                       JOptionPane.showMessageDialog(null, "Please select payroll date first!");
                                    }
                                    else{
                                        pst=con.prepareStatement("INSERT INTO workeddayscalculation(payrollDate,empID,WorkingType,WorkingHours,Amount)"+"VALUES(?,?,?,?,?)");
                                        pst.setString(1, PayrollDate);
                                        pst.setString(2, IDNumber.getText());
                                        pst.setString(3, worktype);
                                        pst.setDouble(4, workHoursHOLOT);
                                        pst.setDouble(5, finalSalaryHOLOT);
                                        pst.executeUpdate();
                                        payrollDetails.addRow(fillTableHOLOT);
                                    }
                                }
                                catch(HeadlessException ex){
                                    JOptionPane.showMessageDialog(null, ex);
                                }
                            break;
                            case "13 MONTH PAY":
                                Object ThirtenthMonthPay[]={"13 Month Pay","",WorkedHours.getText()};
                                double tmp=Double.parseDouble(WorkedHours.getText());
                                WorkedHours.setText("");
                                try{
                                    if(PayrollDate.equals("")){
                                       JOptionPane.showMessageDialog(null, "Please select payroll date first!");
                                    }
                                    else{
                                        pst=con.prepareStatement("INSERT INTO workeddayscalculation(payrollDate,empID,WorkingType,WorkingHours,Amount)"+"VALUES(?,?,?,?,?)");
                                        pst.setString(1, PayrollDate);
                                        pst.setString(2, IDNumber.getText());
                                        pst.setString(3, worktype);
                                        pst.setDouble(4, 0);
                                        pst.setDouble(5, tmp);
                                        pst.executeUpdate();
                                        payrollDetails.addRow(ThirtenthMonthPay);
                                    }
                                }
                                catch(HeadlessException ex){
                                    JOptionPane.showMessageDialog(null, ex);
                                }
                            break;
                            case "BONUS":
                                Object bonus[]={"Bonus","",WorkedHours.getText()};
                                double bonusAmount=Double.parseDouble(WorkedHours.getText());
                                WorkedHours.setText("");
                                try{
                                    if(PayrollDate.equals("")){
                                       JOptionPane.showMessageDialog(null, "Please select payroll date first!");
                                    }
                                    else{
                                        pst=con.prepareStatement("INSERT INTO workeddayscalculation(payrollDate,empID,WorkingType,WorkingHours,Amount)"+"VALUES(?,?,?,?,?)");
                                        pst.setString(1, PayrollDate);
                                        pst.setString(2, IDNumber.getText());
                                        pst.setString(3, worktype);
                                        pst.setDouble(4, 0);
                                        pst.setDouble(5, bonusAmount);
                                        pst.executeUpdate();
                                        payrollDetails.addRow(bonus);
                                    }
                                }
                                catch(HeadlessException ex){
                                    JOptionPane.showMessageDialog(null, ex);
                                }
                            break;
                        }
                    }
   
                }   catch (SQLException ex) {
                    Logger.getLogger(PayrollScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(operation.equals("Debit")){
                if(Deduction.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter deduction first!", "Warning",JOptionPane.WARNING_MESSAGE);
                    DeductionAmount.setText("");
                }
                else if(Deduction.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter deduction amount first!", "Warning",JOptionPane.WARNING_MESSAGE);
                    DeductionAmount.setText("");
                }
                else if(PayrollDate.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter payroll date first!", "Warning",JOptionPane.WARNING_MESSAGE);
                    DeductionAmount.setText("");
                }
                else{
                    double deduction=Double.parseDouble(DeductionAmount.getText());
                    Object debit[]={Deduction.getText().toUpperCase(),deduction,""};
                    try{
                       pst=con.prepareStatement("INSERT INTO deductions(EmployeeID,payrollDate,deductionName,deductionAmount)"+"VALUES(?,?,?,?)");
                       pst.setString(1, IDNumber.getText());
                       pst.setString(2, PayrollDate);
                       pst.setString(3, Deduction.getText().toUpperCase());
                       pst.setDouble(4, deduction);
                       pst.executeUpdate();
                       
                    }
                    catch(SQLException ex){
                        JOptionPane.showMessageDialog(null, ex);
                    }
                    payrollDetails.addRow(debit);
                }
            }
        }
    }
    private void GeneratePayroll(){
        String PayrollDate=((JTextField)dateChooser.getDateEditor().getUiComponent()).getText();
        date=PayrollDate;
        try{
            pst=con.prepareStatement("SELECT ROUND(sum(Amount),2) AS GrossPayable FROM workeddayscalculation WHERE payrollDate='"+PayrollDate+"' AND empID='"+IDNumber.getText()+"'");
            rs=pst.executeQuery();
            if(rs.next()){
                double gross=rs.getDouble("GrossPayable");
                GrossAmount.setText(Double.toString(gross));
                try{
                    pst=con.prepareStatement("SELECT ROUND(sum(deductionAmount),2) AS TotalDeductions FROM deductions WHERE payrollDate='"+PayrollDate+"' AND EmployeeID='"+IDNumber.getText()+"'");
                    rs=pst.executeQuery();
                    if(rs.next()){
                        double deduction=rs.getDouble("TotalDeductions");
                        totalDeductions.setText(Double.toString(deduction));
                        
                        try{
                            double net=gross-deduction;
                            NetPay.setText(Double.toString(net));
                        }
                        catch(Exception ex){
                            JOptionPane.showMessageDialog(null, ex);
                        }
                    }
                }
                catch(SQLException ex){
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        
    }
    private void DisplayPayroll(){
        DefaultTableModel payrollList=(DefaultTableModel)PayrollTable.getModel();
        payrollList.setRowCount(0);
        try{
            
            pst=con.prepareStatement("SELECT * FROM workeddayscalculation WHERE payrollDate='"+date+"' AND empID='"+IDNumber.getText()+"'");
            rs=pst.executeQuery();
            while(rs.next()){
                String type=rs.getString("WorkingType");
                double amount=rs.getDouble("Amount");
                Object c[]={type,"",amount};
                payrollList.addRow(c);
               
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
            try{
                pst=con.prepareStatement("SELECT deductionName, ROUND(deductionAmount,2) AS deductionRound FROM deductions WHERE EmployeeID='"+IDNumber.getText()+"' AND payrollDate='"+date+"'");
                rs=pst.executeQuery();
                while(rs.next()){
                    String deductionType=rs.getString("deductionName");
                    double deductionAmount=rs.getDouble("deductionRound");
                    Object d[]={deductionType,deductionAmount,""};
                    payrollList.addRow(d);
                           
                }
            }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        GeneratePayroll();        
    }
    private void insertPayrollSummarry(){
        try{
            pst=con.prepareStatement("INSERT INTO payrollsummary(payrollDate,empID,gross,deductions,net,MachineOperator)"+"VALUES(?,?,?,?,?,?)");
            pst.setString(1, date);
            pst.setString(2, IDNumber.getText());
            pst.setDouble(3, Double.parseDouble(GrossAmount.getText()));
            pst.setDouble(4, Double.parseDouble(totalDeductions.getText()));
            pst.setDouble(5, Double.parseDouble(NetPay.getText()));
            pst.setString(6, operator);
            
            int option=JOptionPane.showConfirmDialog(null, "Are you sure you want to finalize this payroll?","Warning", JOptionPane.YES_NO_OPTION);
            if(option==JOptionPane.YES_OPTION){
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Payroll has been successfully billed","Congratulations",JOptionPane.INFORMATION_MESSAGE);
                showRecentPayrolls();
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void getOperator(){
        try{
            pst=con.prepareStatement("SELECT fullname FROM AccessLevel WHERE logStatus=1");
            rs=pst.executeQuery();
            if(rs.next()){
                operator=rs.getString("fullname");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    } 
    private void showRecentPayrolls(){
        
        try{
            DefaultTableModel recentPayroll=(DefaultTableModel)RecentPayrolls.getModel();
            recentPayroll.setRowCount(0);
            pst=con.prepareStatement("SELECT payrollDate,net FROM payrollsummary WHERE empID='"+IDNumber.getText()+"'");
            rs=pst.executeQuery();
            while(rs.next()){
                Object rp[]={rs.getString("payrollDate"),rs.getDouble("net")};
                recentPayroll.addRow(rp);
            }
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void newPayroll(){

        DefaultTableModel dtm=(DefaultTableModel)PayrollTable.getModel();
        dtm.getDataVector().removeAllElements();
        dtm.fireTableDataChanged();
        DefaultTableModel dtm1=(DefaultTableModel)RecentPayrolls.getModel();
        dtm1.getDataVector().removeAllElements();
        dtm1.fireTableDataChanged();

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
        EmpName = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Position = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        IDNumber = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        PayrollMode = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        Salary = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        PayrollTable = new javax.swing.JTable();
        DeductionAmount = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        dateChooser = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        WorkedHours = new javax.swing.JTextField();
        Deduction = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        WorkedType = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        RecentPayrolls = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        OperationButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        MonthDays = new javax.swing.JTextField();
        NetPay = new javax.swing.JLabel();
        GrossAmount = new javax.swing.JLabel();
        totalDeductions = new javax.swing.JLabel();
        OperationButton1 = new javax.swing.JButton();
        Clear = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListOfEmployeesTable = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        EmpName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        EmpName.setForeground(new java.awt.Color(255, 153, 0));
        jPanel3.add(EmpName, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 220, 20));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Name:");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Position:");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        Position.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Position.setForeground(new java.awt.Color(255, 153, 0));
        jPanel3.add(Position, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 220, 20));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("ID No.");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        IDNumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        IDNumber.setForeground(new java.awt.Color(255, 153, 0));
        jPanel3.add(IDNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 160, 20));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Payroll Mode:");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, -1, -1));

        PayrollMode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        PayrollMode.setForeground(new java.awt.Color(255, 153, 0));
        jPanel3.add(PayrollMode, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 10, 160, 20));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("No. of Days:");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, -1, -1));

        Salary.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Salary.setForeground(new java.awt.Color(255, 153, 0));
        jPanel3.add(Salary, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 160, 20));

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 0), 2));
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Payroll Date");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 17, -1, 20));

        PayrollTable.setBackground(new java.awt.Color(0, 0, 0));
        PayrollTable.setForeground(new java.awt.Color(255, 255, 255));
        PayrollTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Particular", "Debit", "Credit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        PayrollTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PayrollTable.setGridColor(new java.awt.Color(0, 0, 0));
        PayrollTable.setSelectionBackground(new java.awt.Color(0, 204, 0));
        PayrollTable.setShowHorizontalLines(false);
        PayrollTable.setShowVerticalLines(false);
        PayrollTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PayrollTableMouseClicked(evt);
            }
        });
        PayrollTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PayrollTableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(PayrollTable);
        if (PayrollTable.getColumnModel().getColumnCount() > 0) {
            PayrollTable.getColumnModel().getColumn(0).setResizable(false);
            PayrollTable.getColumnModel().getColumn(0).setPreferredWidth(200);
            PayrollTable.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, 270, 180));

        DeductionAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        DeductionAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        DeductionAmount.setBorder(null);
        DeductionAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeductionAmountActionPerformed(evt);
            }
        });
        jPanel5.add(DeductionAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 160, 90, 30));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Amount");
        jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 140, -1, -1));

        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel5.add(dateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 170, 30));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Worked Type");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, -1, -1));
        jPanel5.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 290, 10));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Deduction");
        jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        WorkedHours.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        WorkedHours.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        WorkedHours.setBorder(null);
        WorkedHours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WorkedHoursActionPerformed(evt);
            }
        });
        jPanel5.add(WorkedHours, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 90, 30));

        Deduction.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Deduction.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Deduction.setBorder(null);
        Deduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeductionActionPerformed(evt);
            }
        });
        jPanel5.add(Deduction, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 190, 30));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Worked Hours");
        jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, -1, -1));

        WorkedType.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        WorkedType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Working Type", "REGULAR DAYS", "REGULAR OVERTIME", "SPECIAL HOLIDAY", "SPECIAL HOLIDAY OVERTIME", "HOLIDAY", "HOLIDAY OVERTIME", "13 MONTH PAY", "BONUS" }));
        WorkedType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WorkedTypeActionPerformed(evt);
            }
        });
        jPanel5.add(WorkedType, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 190, 30));

        jPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 590, 200));

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 0), 2));

        RecentPayrolls.setBackground(new java.awt.Color(0, 0, 0));
        RecentPayrolls.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        RecentPayrolls.setForeground(new java.awt.Color(255, 255, 255));
        RecentPayrolls.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Payroll Date", "Take Home Pay"
            }
        ));
        RecentPayrolls.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        RecentPayrolls.setGridColor(new java.awt.Color(0, 0, 0));
        RecentPayrolls.setSelectionBackground(new java.awt.Color(0, 204, 51));
        RecentPayrolls.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RecentPayrollsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(RecentPayrolls);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 310, 180));

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 204, 0));
        jLabel14.setText("Net Pay:");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 460, -1, 30));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 204, 0));
        jLabel15.setText("Gross Amount:");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 400, -1, 30));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 204, 0));
        jLabel16.setText("Deduction:");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 430, -1, 30));

        OperationButton.setBackground(new java.awt.Color(255, 51, 0));
        OperationButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        OperationButton.setText("Check");
        OperationButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        OperationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OperationButtonActionPerformed(evt);
            }
        });
        jPanel3.add(OperationButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 330, 150, 50));

        jSeparator3.setForeground(new java.awt.Color(0, 204, 0));
        jSeparator3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel3.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 610, 10));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Base Salary:");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 40, -1, -1));

        MonthDays.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        MonthDays.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        MonthDays.setText("31");
        MonthDays.setBorder(null);
        MonthDays.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                MonthDaysFocusLost(evt);
            }
        });
        jPanel3.add(MonthDays, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, 110, 30));

        NetPay.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        NetPay.setForeground(new java.awt.Color(255, 153, 0));
        NetPay.setText("0.00");
        jPanel3.add(NetPay, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 460, 120, 30));

        GrossAmount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        GrossAmount.setForeground(new java.awt.Color(255, 153, 0));
        GrossAmount.setText("0.00");
        jPanel3.add(GrossAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 400, 110, 30));

        totalDeductions.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalDeductions.setForeground(new java.awt.Color(255, 153, 0));
        totalDeductions.setText("0.00");
        jPanel3.add(totalDeductions, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 430, 110, 30));

        OperationButton1.setBackground(new java.awt.Color(0, 204, 51));
        OperationButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        OperationButton1.setText("Print");
        OperationButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        OperationButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OperationButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(OperationButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 330, 110, 50));

        Clear.setText("Clear");
        Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearActionPerformed(evt);
            }
        });
        jPanel3.add(Clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 490, 90, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 610, 520));

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ListOfEmployeesTable.setBackground(new java.awt.Color(0, 0, 0));
        ListOfEmployeesTable.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        ListOfEmployeesTable.setForeground(new java.awt.Color(255, 255, 255));
        ListOfEmployeesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Employee Name", "Position"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ListOfEmployeesTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ListOfEmployeesTable.setRowHeight(20);
        ListOfEmployeesTable.setSelectionBackground(new java.awt.Color(0, 204, 0));
        ListOfEmployeesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ListOfEmployeesTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(ListOfEmployeesTable);
        if (ListOfEmployeesTable.getColumnModel().getColumnCount() > 0) {
            ListOfEmployeesTable.getColumnModel().getColumn(0).setPreferredWidth(32);
        }

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 340, 460));
        jPanel4.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 290, 30));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Payroll/icons/Search_32px.png"))); // NOI18N
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 360, 520));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1000, 540));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 560));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ListOfEmployeesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ListOfEmployeesTableMouseClicked
        OperationButton.setText("Check");
        showDetails();
        showRecentPayrolls();
        
    }//GEN-LAST:event_ListOfEmployeesTableMouseClicked

    private void WorkedHoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WorkedHoursActionPerformed
        operation="Credit";
        String PayrollDate=((JTextField)dateChooser.getDateEditor().getUiComponent()).getText();
        try{
            pst=con.prepareStatement("SELECT * FROM workeddayscalculation WHERE payrollDate='"+PayrollDate+"' AND WorkingType='"+WorkedType.getSelectedItem().toString()+"'AND empID='"+IDNumber.getText()+"'");
            rs=pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null, WorkedType.getSelectedItem().toString()+" for "+PayrollDate +" Payroll Already added","Warning",JOptionPane.WARNING_MESSAGE);
            }
            else{
                int sure=JOptionPane.showConfirmDialog(null, "Please make sure that total working hours is correct", "Attention",JOptionPane.YES_NO_OPTION);
                if(sure==JOptionPane.YES_OPTION){
                    createPayroll();
                    DisplayPayroll();
                }
                        
                
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        
    }//GEN-LAST:event_WorkedHoursActionPerformed

    private void WorkedTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WorkedTypeActionPerformed
        switch (WorkedType.getSelectedItem().toString()) {
            case "13 MONTH PAY":
                jLabel20.setText("Amount");
                WorkedHours.setText(Salary.getText());
                break;
            case "BONUS":
                jLabel20.setText("Amount");
                break;
            default:
                break;
        }
    }//GEN-LAST:event_WorkedTypeActionPerformed

    private void PayrollTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PayrollTableKeyPressed
      date=((JTextField)dateChooser.getDateEditor().getUiComponent()).getText();
      char c = evt.getKeyChar();
      DefaultTableModel dtm=(DefaultTableModel)PayrollTable.getModel();
      int selectedRow=PayrollTable.getSelectedRow();
      if (c == java.awt.event.KeyEvent.VK_DELETE){
          
              String base=dtm.getValueAt(selectedRow, 1).toString();
              if(base.equals("")){
                 try{
                     String particular=dtm.getValueAt(selectedRow, 0).toString();
                     JOptionPane.showMessageDialog(null, particular);
                     pst=con.prepareStatement("DELETE FROM workeddayscalculation WHERE WorkingType='"+particular+"' AND payrollDate='"+date+"'");
                     pst.executeUpdate();
                     dtm.removeRow(selectedRow);
                 }
                 catch(SQLException ex){
                     JOptionPane.showMessageDialog(null, ex);
                    }
                }
              else{
                  try{
                     String deduction=dtm.getValueAt(selectedRow, 0).toString();
                     pst=con.prepareStatement("DELETE FROM deductions WHERE deductionName='"+deduction+"' AND payrollDate='"+date+"'");
                     pst.executeUpdate();
                     dtm.removeRow(selectedRow);
                 }
                 catch(SQLException ex){
                     JOptionPane.showMessageDialog(null, ex);
                    }
              }

          
          
      }
    }//GEN-LAST:event_PayrollTableKeyPressed

    private void DeductionAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeductionAmountActionPerformed
        operation="Debit";
        String PayrollDate=((JTextField)dateChooser.getDateEditor().getUiComponent()).getText();
        try{
            pst=con.prepareStatement("SELECT * FROM deductions WHERE payrollDate='"+PayrollDate+"' AND deductionName='"+Deduction.getText()+"'");
            rs=pst.executeQuery();
            if(rs.next()){
                JOptionPane.showMessageDialog(null, Deduction.getText()+" Deduction for "+PayrollDate +" Already added","Warning",JOptionPane.WARNING_MESSAGE);
            }
            else{
                createPayroll();
                Deduction.setText("");
                DeductionAmount.setText("");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        DisplayPayroll();
    }//GEN-LAST:event_DeductionAmountActionPerformed

    private void DeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DeductionActionPerformed

    private void PayrollTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PayrollTableMouseClicked

    }//GEN-LAST:event_PayrollTableMouseClicked

    private void OperationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OperationButtonActionPerformed
        //date=((JTextField)dateChooser.getDateEditor().getUiComponent()).getText();
        
        if(OperationButton.getText().equals("Regenerate")){
           try{
               pst=con.prepareStatement("SELECT * FROM payrollsummary WHERE payrollDate='"+date+"' AND empID='"+IDNumber.getText()+"'");
               rs=pst.executeQuery();
               if(rs.next()){
                   DisplayPayroll();
                   pst=con.prepareStatement("DELETE FROM payrollsummary WHERE payrollDate='"+date+"'AND empID='"+IDNumber.getText()+"'");
                   pst.executeUpdate();
                   insertPayrollSummarry();
                   OperationButton.setText("Generate");
                   newPayroll();
                   
               }
               else{

               }
               
           }
           catch(SQLException ex){
               JOptionPane.showMessageDialog(null, ex);
           }
           
        }
        else{
           date=((JTextField)dateChooser.getDateEditor().getUiComponent()).getText();
           DisplayPayroll();
        }
        
        
    }//GEN-LAST:event_OperationButtonActionPerformed

    private void MonthDaysFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MonthDaysFocusLost
        int noOfDays=Integer.parseInt(MonthDays.getText());
        if(noOfDays>=28 && noOfDays<32){

        }
        else {
            JOptionPane.showMessageDialog(null, "Invalid number of days in a month!","Warning",JOptionPane.WARNING_MESSAGE);
            MonthDays.setText("");
        }
    }//GEN-LAST:event_MonthDaysFocusLost

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void RecentPayrollsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RecentPayrollsMouseClicked
        OperationButton.setText("Regenerate");
        String recentDate=RecentPayrolls.getModel().getValueAt(RecentPayrolls.getSelectedRow(), 0).toString();
        date=recentDate;
        dateChooser.getDateEditor().setDate(Date.valueOf(recentDate));
        DisplayPayroll();
    }//GEN-LAST:event_RecentPayrollsMouseClicked

    private void OperationButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OperationButton1ActionPerformed
        if(IDNumber.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Unable to process payroll, Please check details above","Warning",JOptionPane.WARNING_MESSAGE);
        }
        else{
        insertPayrollSummarry();
        }
    }//GEN-LAST:event_OperationButton1ActionPerformed

    private void ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearActionPerformed
        DefaultTableModel dtm=(DefaultTableModel)PayrollTable.getModel();
        dtm.setRowCount(0);
        DefaultTableModel dtm1=(DefaultTableModel)RecentPayrolls.getModel();
        dtm1.setRowCount(0);
        IDNumber.setText("");
        EmpName.setText("");
        Position.setText("");
        PayrollMode.setText("");
        Salary.setText("");
        GrossAmount.setText("");
        totalDeductions.setText("");
        NetPay.setText("");
    }//GEN-LAST:event_ClearActionPerformed

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
            java.util.logging.Logger.getLogger(PayrollScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PayrollScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PayrollScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PayrollScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PayrollScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Clear;
    private javax.swing.JTextField Deduction;
    private javax.swing.JTextField DeductionAmount;
    private javax.swing.JLabel EmpName;
    private javax.swing.JLabel GrossAmount;
    private javax.swing.JLabel IDNumber;
    private javax.swing.JTable ListOfEmployeesTable;
    private javax.swing.JTextField MonthDays;
    private javax.swing.JLabel NetPay;
    private javax.swing.JButton OperationButton;
    private javax.swing.JButton OperationButton1;
    private javax.swing.JLabel PayrollMode;
    public javax.swing.JTable PayrollTable;
    private javax.swing.JLabel Position;
    private javax.swing.JTable RecentPayrolls;
    private javax.swing.JLabel Salary;
    private javax.swing.JTextField WorkedHours;
    private javax.swing.JComboBox<String> WorkedType;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel totalDeductions;
    // End of variables declaration//GEN-END:variables
}
