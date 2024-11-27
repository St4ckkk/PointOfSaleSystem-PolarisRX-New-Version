/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeeinformationsystem;


import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author pswor
 */
public class AddNewEmployee extends javax.swing.JFrame {

    /**
     * Creates new form EmployeeManagement
     */
    public AddNewEmployee() {
        initComponents();
       
        showUser();
        fillDepartment();
    }
    private void showUser(){

       }
    private void viewProfile(){
        try{
            
            Connection con=DatabaseConnection.DBConnect();
            PreparedStatement pst1=con.prepareStatement("SELECT * FROM basicinformation WHERE EmployeeID='"+empID.getText()+"'");
            ResultSet rs1=pst1.executeQuery();  
            if(rs1.next()){
                ImageIcon icon=new ImageIcon("EmpPhotos/"+empID.getText()+".png");
                Image img=icon.getImage();
                Image newImage=img.getScaledInstance(jBrgyLogo.getWidth(), jBrgyLogo.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon image=new ImageIcon(newImage);
                
                jBrgyLogo.setIcon(image);
                
                
                lastname.setText(rs1.getString("LastName"));
                firstname.setText(rs1.getString("FirstName"));
                middlename.setText(rs1.getString("MiddleName"));
                address.setText(rs1.getString("Address"));
                course.setText(rs1.getString("Course"));
                Date date=rs1.getDate("BirthDay");
                birthdate.getDateEditor().setDate(date);
                gender.addItem(rs1.getString("Gender"));
                status.addItem(rs1.getString("CivilStatus"));
                empnumber.setText(rs1.getString("ContactNumber"));
                //for restriction
                idstatus.setText("");
                addemployee.setEnabled(true);
                addemployee.setText("UPDATE EMPLOYEE");
                empID.setEditable(false);
                Browse.setEnabled(true);
                
            }
            else{
                idstatus.setText("Unassigned ID Number!");
                
            }
            PreparedStatement pst2=con.prepareStatement("SELECT * FROM emergencycontacts WHERE EmployeeID='"+empID.getText()+"'");
            ResultSet rs2=pst2.executeQuery();
            if(rs2.next()){
                contactperson.setText(rs2.getString("ContactPerson"));
                relation.setText(rs2.getString("Relation"));
                contactnumber.setText(rs2.getString("ContactNumber"));
                contactaddress.setText(rs2.getString("ContactAddress"));
            }
            PreparedStatement pst3=con.prepareStatement("SELECT * FROM employmentinformations WHERE EmployeeID='"+empID.getText()+"'");
            ResultSet rs3=pst3.executeQuery();
            if(rs3.next()){
               Date dateofhirement=rs3.getDate("DateOfHire");
                dateofhire.getDateEditor().setDate(dateofhirement); 
                position.setText(rs3.getString("Position"));
                department.addItem(rs3.getString("Department"));
                salary.setText(rs3.getString("Salary"));
                role.setText(rs3.getString("Role"));
                previousemployer.setText(rs3.getString("PreviousEmployer"));
            }
            PreparedStatement pst4=con.prepareStatement("SELECT * FROM governmentid WHERE EmployeeID='"+empID.getText()+"'");
            ResultSet rs4=pst4.executeQuery();
            if(rs4.next()){
               sss.setText(rs4.getString("SSS"));
               philhealth.setText(rs4.getString("PhilHealth"));
               pagibig.setText(rs4.getString("HDMF"));
                
            }
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void fillDepartment(){
         Connection conn=DatabaseConnection.DBConnect();
        
        
        String selectDepartment="SELECT DepartmentName FROM Department";
        
        try{
            PreparedStatement pst=conn.prepareStatement(selectDepartment);
            ResultSet opset = pst.executeQuery();
            while(opset.next()){
                String dept=opset.getString("DepartmentName");
                department.addItem(dept);
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
     }
    private void addBasic(){
        Connection conn=DatabaseConnection.DBConnect();
        //File imgfile = new File("EmpPhotos\\"+empID.getText()+".png");
        //FileInputStream fin = new FileInputStream(imgfile);
        
        String addEmp = "INSERT INTO BasicInformation(PhotoID,EmployeeID,Lastname,Firstname,Middlename,Address,Course,Birthday,Gender,CivilStatus,ContactNumber)"+"VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement pstmt=conn.prepareStatement(addEmp);
            
            pstmt.setBinaryStream(1,null);
            pstmt.setString(2,empID.getText());
            pstmt.setString(3,lastname.getText());
            pstmt.setString(4,firstname.getText());
            pstmt.setString(5,middlename.getText());
            pstmt.setString(6,address.getText());
            pstmt.setString(7,course.getText());
            pstmt.setString(8,((JTextField)birthdate.getDateEditor().getUiComponent()).getText());
            pstmt.setString(9, (String) gender.getSelectedItem());
            pstmt.setString(10, (String) status.getSelectedItem());
            pstmt.setString(11, empnumber.getText());
            
            
            pstmt.executeUpdate();
            addEmergency();
            addEmploymentInfo();
            addGovernmentID();
            
        }
        
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }

    }
    private void addEmergency(){
        Connection conn=DatabaseConnection.DBConnect();
        
        
        String addEmp = "INSERT INTO EmergencyContacts(EmployeeID,ContactPerson,Relation,ContactNumber,ContactAddress)"+"VALUES(?,?,?,?,?)";
        
       
        try{
            PreparedStatement pstmt=conn.prepareStatement(addEmp);
            
            pstmt.setString(1,empID.getText());
            pstmt.setString(2,contactperson.getText());
            pstmt.setString(3,relation.getText());
            pstmt.setString(4,contactnumber.getText());
            pstmt.setString(5,contactaddress.getText());
            
            pstmt.executeUpdate();
            
        }
        
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }

    }
    private void addEmploymentInfo(){
        Connection conn=DatabaseConnection.DBConnect();
        
       
      
        String addEmp = "INSERT INTO EmploymentInformations(EmployeeID,DateOfHire,Position,Department,Salary,Role,PreviousEmployer)"+"VALUES(?,?,?,?,?,?,?)";
        
        
        try{
            PreparedStatement pstmt=conn.prepareStatement(addEmp);
            
            pstmt.setString(1,empID.getText());
            pstmt.setString(2,((JTextField)dateofhire.getDateEditor().getUiComponent()).getText());
            pstmt.setString(3,position.getText());
            pstmt.setString(4, (String) department.getSelectedItem());
            pstmt.setString(5,salary.getText());
            pstmt.setString(6,role.getText());
            pstmt.setString(7,previousemployer.getText());
            
            pstmt.executeUpdate();
            
        }
        
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }

    }
    private void addGovernmentID(){
         
        Connection conn=DatabaseConnection.DBConnect();
        
       
       
        String addEmp = "INSERT INTO GovernmentID(EmployeeID,SSS,PhilHealth,HDMF)"+"VALUES(?,?,?,?)";
        
        try{
            PreparedStatement pstmt=conn.prepareStatement(addEmp);
            
            pstmt.setString(1,empID.getText());
            pstmt.setString(2,sss.getText());
            pstmt.setString(3,philhealth.getText());
            pstmt.setString(4,pagibig.getText());
            
            pstmt.executeUpdate();
            
        }
        
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }

    }
    private void UpdateEmployee(){
        Connection conn=DatabaseConnection.DBConnect();
        try{
             File imgfile = new File("EmpPhotos\\"+empID.getText()+".png");
            FileInputStream fin = new FileInputStream(imgfile);
            
                String addEmp = "UPDATE BasicInformation SET PhotoID=?,EmployeeID=?,Lastname=?,Firstname=?,Middlename=?,Address=?,Course=?,Birthday=?,Gender=?,CivilStatus=?,ContactNumber=? WHERE EmployeeID='"+empID.getText()+"'";            
                try{
                PreparedStatement pstmt=conn.prepareStatement(addEmp);
                pstmt.setBinaryStream(1,(InputStream)fin,(int)imgfile.length());
                pstmt.setString(2,empID.getText());
                pstmt.setString(3,lastname.getText());
                pstmt.setString(4,firstname.getText());
                pstmt.setString(5,middlename.getText());
                pstmt.setString(6,address.getText());
                pstmt.setString(7,course.getText());
                pstmt.setString(8,((JTextField)birthdate.getDateEditor().getUiComponent()).getText());
                pstmt.setString(9, (String) gender.getSelectedItem());
                pstmt.setString(10, (String) status.getSelectedItem());
                pstmt.setString(11, empnumber.getText()); 
                pstmt.executeUpdate();
              
                }
            
                catch(SQLException ex){
                    JOptionPane.showMessageDialog(null, ex);  
                }
                String addEm = "UPDATE EmergencyContacts SET EmployeeID=?,ContactPerson=?,Relation=?,ContactNumber=?,ContactAddress=? WHERE EmployeeID='"+empID.getText()+"'";
        
       
        try{
            PreparedStatement pstmt=conn.prepareStatement(addEm);
            
            pstmt.setString(1,empID.getText());
            pstmt.setString(2,contactperson.getText());
            pstmt.setString(3,relation.getText());
            pstmt.setString(4,contactnumber.getText());
            pstmt.setString(5,contactaddress.getText());
            
            pstmt.executeUpdate();
            
        }
        
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        
        String addEmpInfo = "UPDATE EmploymentInformations SET EmployeeID=?,DateOfHire=?,Position=?,Department=?,Salary=?,Role=?,PreviousEmployer=? WHERE EmployeeID='"+empID.getText()+"'";
        
        
        try{
            PreparedStatement pstmt=conn.prepareStatement(addEmpInfo);
            
            pstmt.setString(1,empID.getText());
            pstmt.setString(2,((JTextField)dateofhire.getDateEditor().getUiComponent()).getText());
            pstmt.setString(3,position.getText());
            pstmt.setString(4, (String) department.getSelectedItem());
            pstmt.setString(5,salary.getText());
            pstmt.setString(6,role.getText());
            pstmt.setString(7,previousemployer.getText());
            
            pstmt.executeUpdate();
            
        }
        
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
            
        String addEmpGID = "UPDATE GovernmentID SET EmployeeID=?,SSS=?,PhilHealth=?,HDMF=? WHERE EmployeeID='"+empID.getText()+"'";
        
        try{
            PreparedStatement pstmt=conn.prepareStatement(addEmpGID);
            
            pstmt.setString(1,empID.getText());
            pstmt.setString(2,sss.getText());
            pstmt.setString(3,philhealth.getText());
            pstmt.setString(4,pagibig.getText());
            
            pstmt.executeUpdate();
            
        }
        
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
                
                
                
                
        }
        catch(HeadlessException | FileNotFoundException ex){
            
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Background = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        PhotoID = new javax.swing.JPanel();
        jBrgyLogo = new javax.swing.JLabel();
        Browse = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lastname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        firstname = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        middlename = new javax.swing.JTextField();
        address = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        course = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        contactperson = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        relation = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        contactaddress = new javax.swing.JTextField();
        contactwarning = new javax.swing.JLabel();
        contactnumber = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        gender = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        status = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        empID = new javax.swing.JTextField();
        birthdate = new com.toedter.calendar.JDateChooser();
        idstatus = new javax.swing.JLabel();
        empnumber = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        empnumberwarning = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        position = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        department = new javax.swing.JComboBox<>();
        salary = new javax.swing.JTextField();
        role = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        previousemployer = new javax.swing.JTextField();
        dateofhire = new com.toedter.calendar.JDateChooser();
        salarywarning = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        ssswarning = new javax.swing.JLabel();
        phicwarning = new javax.swing.JLabel();
        hdmfwarning = new javax.swing.JLabel();
        sss = new javax.swing.JTextField();
        philhealth = new javax.swing.JTextField();
        pagibig = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        addemployee = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        addemployee1 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Background.setBackground(new java.awt.Color(0, 0, 0));
        Background.setMinimumSize(new java.awt.Dimension(1366, 760));
        Background.setPreferredSize(new java.awt.Dimension(1366, 760));
        Background.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 255, 255), 5));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("EMPLOYEE PROFILE");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 30, -1, -1));

        Background.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 1330, 90));

        jPanel4.setBackground(new java.awt.Color(0, 153, 153));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 5, true));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 1, true), "Basic Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(51, 255, 255))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PhotoID.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jBrgyLogo.setForeground(new java.awt.Color(255, 255, 255));
        PhotoID.add(jBrgyLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 100));

        jPanel1.add(PhotoID, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 19, -1, -1));

        Browse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/employeeinformationsystem/Icons/Open_30px.png"))); // NOI18N
        Browse.setText("Browse");
        Browse.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BrowseActionPerformed(evt);
            }
        });
        jPanel1.add(Browse, new org.netbeans.lib.awtextra.AbsoluteConstraints(417, 125, -1, 36));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 255, 255));
        jLabel1.setText("Last Name");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 85, -1, -1));

        lastname.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(lastname, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 110, 114, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 255, 255));
        jLabel2.setText("First Name");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 82, -1, -1));

        firstname.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(firstname, new org.netbeans.lib.awtextra.AbsoluteConstraints(153, 110, 114, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 255, 255));
        jLabel4.setText("Middle Name");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 82, -1, -1));

        middlename.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(middlename, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 110, 114, -1));

        address.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(address, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 151, 329, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 255, 255));
        jLabel5.setText("Address:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 154, -1, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 255, 255));
        jLabel6.setText("Course");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 195, -1, -1));

        course.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(course, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 192, 327, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 255, 255));
        jLabel8.setText("Birthdate");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 236, -1, -1));

        jPanel7.setBackground(new java.awt.Color(0, 153, 153));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 1, true), "In case of Emergency", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(0, 255, 255))); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 255, 255));
        jLabel21.setText("Person to Contact");
        jPanel7.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 28, -1, -1));

        contactperson.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel7.add(contactperson, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 52, 222, -1));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 255, 255));
        jLabel22.setText("Relation");
        jPanel7.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 28, -1, -1));

        relation.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        relation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relationActionPerformed(evt);
            }
        });
        jPanel7.add(relation, new org.netbeans.lib.awtextra.AbsoluteConstraints(255, 52, 128, -1));

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(102, 255, 255));
        jLabel23.setText("Contact Number");
        jPanel7.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 78, -1, 24));

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(102, 255, 255));
        jLabel24.setText("Contact Address");
        jPanel7.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 137, -1, 24));

        contactaddress.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel7.add(contactaddress, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 167, 379, -1));
        jPanel7.add(contactwarning, new org.netbeans.lib.awtextra.AbsoluteConstraints(241, 108, 264, 20));

        contactnumber.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        contactnumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                contactnumberFocusLost(evt);
            }
        });
        contactnumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactnumberActionPerformed(evt);
            }
        });
        contactnumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                contactnumberKeyTyped(evt);
            }
        });
        jPanel7.add(contactnumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 108, 222, -1));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 333, 498, 220));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 255, 255));
        jLabel9.setText("Gender");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 267, -1, -1));

        gender.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        gender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        jPanel1.add(gender, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 264, 157, -1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(102, 255, 255));
        jLabel19.setText("Status");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 301, -1, -1));

        status.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        status.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Single", "Married" }));
        jPanel1.add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(83, 298, 157, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 255, 255));
        jLabel17.setText("Employee ID");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 44, -1, -1));

        empID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        empID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                empIDFocusLost(evt);
            }
        });
        empID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empIDActionPerformed(evt);
            }
        });
        jPanel1.add(empID, new org.netbeans.lib.awtextra.AbsoluteConstraints(117, 41, 105, -1));

        birthdate.setDateFormatString("yyyy-MM-dd");
        birthdate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(birthdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 233, 155, -1));

        idstatus.setForeground(new java.awt.Color(255, 255, 0));
        jPanel1.add(idstatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 41, 129, 20));

        empnumber.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        empnumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                empnumberFocusLost(evt);
            }
        });
        empnumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empnumberActionPerformed(evt);
            }
        });
        empnumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                empnumberKeyTyped(evt);
            }
        });
        jPanel1.add(empnumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 264, 149, -1));

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(102, 255, 255));
        jLabel27.setText("Contact Number");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(258, 234, -1, 24));
        jPanel1.add(empnumberwarning, new org.netbeans.lib.awtextra.AbsoluteConstraints(413, 264, 110, 20));

        jPanel4.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 530, 580));

        jPanel5.setBackground(new java.awt.Color(0, 153, 153));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 1, true), "Employment Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(51, 255, 255))); // NOI18N
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 255, 255));
        jLabel7.setText("Date of Hire:");
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 56, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 255, 255));
        jLabel10.setText("Position");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 104, -1, -1));

        position.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel5.add(position, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 101, 197, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 255, 255));
        jLabel11.setText("Department");
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 151, -1, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(102, 255, 255));
        jLabel12.setText("Salary");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 192, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 255, 255));
        jLabel13.setText("Role");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 234, -1, -1));

        department.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        department.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));
        jPanel5.add(department, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 141, 329, 30));

        salary.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        salary.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                salaryFocusLost(evt);
            }
        });
        salary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salaryActionPerformed(evt);
            }
        });
        salary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                salaryKeyTyped(evt);
            }
        });
        jPanel5.add(salary, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 189, 194, -1));

        role.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        role.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roleActionPerformed(evt);
            }
        });
        jPanel5.add(role, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 231, 360, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 255, 255));
        jLabel18.setText("Previous Employer");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 280, -1, -1));

        previousemployer.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel5.add(previousemployer, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 277, 360, -1));

        dateofhire.setDateFormatString("yyyy-MM-dd");
        dateofhire.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel5.add(dateofhire, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 56, 197, 27));
        jPanel5.add(salarywarning, new org.netbeans.lib.awtextra.AbsoluteConstraints(347, 189, 125, 23));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, 510, 340));

        jPanel6.setBackground(new java.awt.Color(0, 153, 153));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 1, true), "Government ID", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14), new java.awt.Color(51, 255, 255))); // NOI18N
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 255, 255));
        jLabel14.setText("Social Security System");
        jPanel6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 47, -1, -1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 255, 255));
        jLabel15.setText("Phil Health ID Number");
        jPanel6.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 85, -1, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 255, 255));
        jLabel16.setText("PagIBIG ID Number");
        jPanel6.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 126, -1, -1));
        jPanel6.add(ssswarning, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 41, 163, 20));
        jPanel6.add(phicwarning, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 82, 163, 20));
        jPanel6.add(hdmfwarning, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 126, 155, 20));

        sss.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sss.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                sssFocusLost(evt);
            }
        });
        sss.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                sssKeyTyped(evt);
            }
        });
        jPanel6.add(sss, new org.netbeans.lib.awtextra.AbsoluteConstraints(169, 41, 153, -1));

        philhealth.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        philhealth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                philhealthFocusLost(evt);
            }
        });
        philhealth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                philhealthKeyTyped(evt);
            }
        });
        jPanel6.add(philhealth, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 82, 152, -1));

        pagibig.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pagibig.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                pagibigFocusLost(evt);
            }
        });
        pagibig.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pagibigKeyTyped(evt);
            }
        });
        jPanel6.add(pagibig, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 123, 152, -1));

        jPanel4.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 360, 510, 230));

        Background.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 1080, 620));

        jPanel3.setBackground(new java.awt.Color(0, 153, 153));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 4));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addemployee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/employeeinformationsystem/Icons/Profile_30px.png"))); // NOI18N
        addemployee.setText("ADD EMPLOYEE");
        addemployee.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addemployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addemployeeActionPerformed(evt);
            }
        });
        jPanel3.add(addemployee, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 170, 40));

        jPanel8.setBackground(new java.awt.Color(0, 153, 153));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 255, 255), 1, true), "Quick Links", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12), new java.awt.Color(0, 255, 255))); // NOI18N
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/employeeinformationsystem/Icons/Dashboard_30px.png"))); // NOI18N
        jButton4.setText("ADMIN DASHBOARD");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 47, 190, 40));

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/employeeinformationsystem/Icons/User Groups_30px.png"))); // NOI18N
        jButton6.setText("MANGE USER");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 101, 190, 40));

        addemployee1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/employeeinformationsystem/Icons/Department_30px.png"))); // NOI18N
        addemployee1.setText("ADD EPARTMENT");
        addemployee1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addemployee1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addemployee1ActionPerformed(evt);
            }
        });
        jPanel8.add(addemployee1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 159, 190, 40));

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/employeeinformationsystem/Icons/Bulleted List_30px.png"))); // NOI18N
        jButton7.setText("EMPLOYEE LIST");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 218, 190, 40));

        jPanel3.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 220, 350));

        jButton1.setBackground(new java.awt.Color(255, 51, 0));
        jButton1.setText("LOG OUT");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 550, 170, 40));

        Background.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 110, 240, 620));

        getContentPane().add(Background, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void salaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salaryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_salaryActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        AdminDashBoard ad=new AdminDashBoard();
        ad.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        AddNewUser anu=new AddNewUser();
        anu.setVisible(true);
       
    }//GEN-LAST:event_jButton6ActionPerformed

    private void relationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_relationActionPerformed

    private void roleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roleActionPerformed

    private void BrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BrowseActionPerformed
        
        
        String empId=empID.getText();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        //filter the files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png");
        fileChooser.setFileFilter(filter);
        
        
        if(fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
      
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            jBrgyLogo.setIcon(ResizeImage(path));
            BufferedImage image;
            
            try{
                image=ImageIO.read(selectedFile);
                ImageIO.write(image, "jpg", new File("EmpPhotos\\"+empId+".png"));
    
            }
            catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                    }  
             
        } 
    }//GEN-LAST:event_BrowseActionPerformed

    private void addemployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addemployeeActionPerformed
        if(addemployee.getText().equals("UPDATE EMPLOYEE")){
            UpdateEmployee();
            JOptionPane.showMessageDialog(null, lastname.getText() + " Profile successfully updated!");
        }
        else{
        int selectedOption = JOptionPane.showConfirmDialog(null, 
                                  "Do you want to add this employee?", 
                                  "Warning", 
                                  JOptionPane.YES_NO_OPTION); 
                            if (selectedOption == JOptionPane.YES_OPTION) {
                               addBasic();
                               
                                int selected = JOptionPane.showConfirmDialog(null,
                                        "Employee added successfully! \n Do you want to add another employee?",
                                        "Choose",
                                        JOptionPane.YES_NO_OPTION);
                                if (selected == JOptionPane.YES_OPTION) {
                                    empID.setText("");
                                    jBrgyLogo.setIcon(null);
                                    lastname.setText("");
                                    firstname.setText("");
                                    middlename.setText("");
                                    address.setText("");
                                    course.setText("");
                                    contactperson.setText("");
                                    relation.setText("");
                                    contactnumber.setText("");
                                    contactaddress.setText("");
                                    position.setText("");
                                    salary.setText("");
                                    role.setText("");
                                    previousemployer.setText("");
                                    sss.setText("");
                                    philhealth.setText("");
                                    pagibig.setText("");
                                }
                                else{
                                    AdminDashBoard ad=new AdminDashBoard();
                                    ad.setVisible(true);
                                    dispose();
                                }
                  }
        }
    }//GEN-LAST:event_addemployeeActionPerformed
  
    private void empIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_empIDFocusLost
        if(!empID.getText().equals("")){
              if(!empID.getText().equals("")){
        try {
                Connection conn=DatabaseConnection.DBConnect();
                String compare="SELECT EmployeeID FROM BasicInformation WHERE EmployeeID='"+empID.getText()+"'";
                Statement stmnt=conn.createStatement();
                ResultSet resSet=stmnt.executeQuery(compare);
                
                
                if(resSet.next() && lastname.getText().equals("")){
                    idstatus.setText("Assigned already!");
                    
                    idstatus.setForeground(Color.red);
                    Browse.setEnabled(false);
                    //addemployee.setEnabled(false);
                    //viewProfile();
                    //addemployee.setText("UPDATE EMPLOYEE");
                    //empID.setEditable(false);
                    }
                  else{
                  idstatus.setText("");
              
                  idstatus.setForeground(Color.blue);
                  Browse.setEnabled(true);
                  addemployee.setEnabled(true);
                }
        }
        catch(SQLException ex){
         JOptionPane.showMessageDialog(null,ex);
        }
       }
      
        
        }
        else{
            
        }
    }//GEN-LAST:event_empIDFocusLost

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        Browse.setEnabled(false);
        addemployee.setEnabled(false);
    }//GEN-LAST:event_formWindowOpened

    private void salaryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salaryFocusLost
       
    }//GEN-LAST:event_salaryFocusLost

    private void salaryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salaryKeyTyped
        char c = evt.getKeyChar();
       
      if (!((c >= '0') && (c <= '9') ||
         (c == KeyEvent.VK_BACK_SPACE) ||
         (c == KeyEvent.VK_DELETE)))  {
        getToolkit().beep();
        evt.consume();
        salarywarning.setText("Number only");
        salarywarning.setForeground(Color.red);
      }
      
      else{
        salarywarning.setText("");
        
      }
    }//GEN-LAST:event_salaryKeyTyped

    private void sssKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sssKeyTyped
              char c = evt.getKeyChar();
       
      if (!((c >= '0') && (c <= '9') ||
         (c == KeyEvent.VK_BACK_SPACE) ||
         (c == KeyEvent.VK_DELETE)))  {
        getToolkit().beep();
        evt.consume();
        ssswarning.setText("Number only");
        ssswarning.setForeground(Color.red);
      }
     
      else{
        ssswarning.setText("");
        
      }
    }//GEN-LAST:event_sssKeyTyped

    private void contactnumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contactnumberKeyTyped
            char c = evt.getKeyChar();
       
      if (!((c >= '0') && (c <= '9') ||
         (c == KeyEvent.VK_BACK_SPACE) ||
         (c == KeyEvent.VK_DELETE)))  {
        getToolkit().beep();
        evt.consume();
        contactwarning.setText("Number only");
        contactwarning.setForeground(Color.red);
      }
     
      else{
        contactwarning.setText("");
        
      }
    }//GEN-LAST:event_contactnumberKeyTyped

    private void contactnumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactnumberFocusLost
        if(contactnumber.getText().length()!=11){
            contactwarning.setText("Invalid Contact!");
            addemployee.setEnabled(false);
            getToolkit().beep();
        }
        else{
            addemployee.setEnabled(true);
        }
    }//GEN-LAST:event_contactnumberFocusLost

    private void contactnumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactnumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_contactnumberActionPerformed

    private void empnumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_empnumberFocusLost
        if(empnumber.getText().length()!=11){
            empnumberwarning.setText("Invalid Contact!");
            addemployee.setEnabled(false);
            getToolkit().beep();
        }
        else{
            empnumberwarning.setText("");
            addemployee.setEnabled(true);
        }
    }//GEN-LAST:event_empnumberFocusLost

    private void empnumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empnumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_empnumberActionPerformed

    private void empnumberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_empnumberKeyTyped
               char c = evt.getKeyChar();
       
      if (!((c >= '0') && (c <= '9') ||
         (c == KeyEvent.VK_BACK_SPACE) ||
         (c == KeyEvent.VK_DELETE)))  {
        getToolkit().beep();
        evt.consume();
        empnumberwarning.setText("Number only");
        empnumberwarning.setForeground(Color.red);
      }
     
      else{
        contactwarning.setText("");
        
      }
    }//GEN-LAST:event_empnumberKeyTyped

    private void philhealthKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_philhealthKeyTyped
              char c = evt.getKeyChar();
       
      if (!((c >= '0') && (c <= '9') ||
         (c == KeyEvent.VK_BACK_SPACE) ||
         (c == KeyEvent.VK_DELETE)))  {
        getToolkit().beep();
        evt.consume();
        phicwarning.setText("Number only");
        phicwarning.setForeground(Color.red);
      }
     
      else{
        phicwarning.setText("");
        
      }
    }//GEN-LAST:event_philhealthKeyTyped

    private void pagibigKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pagibigKeyTyped
              char c = evt.getKeyChar();
       
      if (!((c >= '0') && (c <= '9') ||
         (c == KeyEvent.VK_BACK_SPACE) ||
         (c == KeyEvent.VK_DELETE)))  {
        getToolkit().beep();
        evt.consume();
        hdmfwarning.setText("Number only");
        hdmfwarning.setForeground(Color.red);
      }
     
      else{
        contactwarning.setText("");
        
      }
    }//GEN-LAST:event_pagibigKeyTyped

    private void sssFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sssFocusLost
        if(sss.getText().length()!=10){
            ssswarning.setText("Invalid SSS ID!");
            addemployee.setEnabled(false);
            getToolkit().beep();
        }
        else{
            ssswarning.setText("");
            addemployee.setEnabled(true);
        }
    }//GEN-LAST:event_sssFocusLost

    private void philhealthFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_philhealthFocusLost
         if(philhealth.getText().length()!=12){
            phicwarning.setText("Invalid PHIC ID!");
            addemployee.setEnabled(false);
            getToolkit().beep();
        }
        else{
            phicwarning.setText("");
            addemployee.setEnabled(true);
        }
    }//GEN-LAST:event_philhealthFocusLost

    private void pagibigFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pagibigFocusLost
         if(pagibig.getText().length()!=12){
            hdmfwarning.setText("Invalid HDMF ID!");
            addemployee.setEnabled(false);
            getToolkit().beep();
        }
        else{
            hdmfwarning.setText("");
            addemployee.setEnabled(true);
        }
    }//GEN-LAST:event_pagibigFocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Logout log=new Logout();
        log.Logout();
        dispose();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void empIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empIDActionPerformed
        viewProfile();
    }//GEN-LAST:event_empIDActionPerformed

    private void addemployee1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addemployee1ActionPerformed
        AddNewDepartment and=new AddNewDepartment();
        and.setVisible(true);
       
    }//GEN-LAST:event_addemployee1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        EmployeeManagement em=new EmployeeManagement();
        em.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton7ActionPerformed
        public ImageIcon ResizeImage(String ImagePath)
    {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(jBrgyLogo.getWidth(), jBrgyLogo.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
  
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
            java.util.logging.Logger.getLogger(AddNewEmployee.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new AddNewEmployee().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Background;
    private javax.swing.JButton Browse;
    private javax.swing.JPanel PhotoID;
    private javax.swing.JButton addemployee;
    private javax.swing.JButton addemployee1;
    private javax.swing.JTextField address;
    private com.toedter.calendar.JDateChooser birthdate;
    private javax.swing.JTextField contactaddress;
    private javax.swing.JTextField contactnumber;
    private javax.swing.JTextField contactperson;
    private javax.swing.JLabel contactwarning;
    private javax.swing.JTextField course;
    private com.toedter.calendar.JDateChooser dateofhire;
    private javax.swing.JComboBox<String> department;
    private javax.swing.JTextField empID;
    private javax.swing.JTextField empnumber;
    private javax.swing.JLabel empnumberwarning;
    private javax.swing.JTextField firstname;
    private javax.swing.JComboBox<String> gender;
    private javax.swing.JLabel hdmfwarning;
    private javax.swing.JLabel idstatus;
    private javax.swing.JLabel jBrgyLogo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTextField lastname;
    private javax.swing.JTextField middlename;
    private javax.swing.JTextField pagibig;
    private javax.swing.JLabel phicwarning;
    private javax.swing.JTextField philhealth;
    private javax.swing.JTextField position;
    private javax.swing.JTextField previousemployer;
    private javax.swing.JTextField relation;
    private javax.swing.JTextField role;
    private javax.swing.JTextField salary;
    private javax.swing.JLabel salarywarning;
    private javax.swing.JTextField sss;
    private javax.swing.JLabel ssswarning;
    private javax.swing.JComboBox<String> status;
    // End of variables declaration//GEN-END:variables
}
