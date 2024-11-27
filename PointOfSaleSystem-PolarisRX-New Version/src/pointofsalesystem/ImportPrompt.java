/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsalesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author pswor
 */
public class ImportPrompt extends javax.swing.JFrame {

    FileInputStream fs = null;
    File f = null;
    String fname = null;

    private void selectFile() throws FileNotFoundException {
        JFileChooser fchoser = new JFileChooser();
        fchoser.showOpenDialog(null);
        f = fchoser.getSelectedFile();
        fname = f.getAbsolutePath();
        fs = new FileInputStream(f);
        jTextField1.setText(fname);
    }

    public void StartImport() throws InterruptedException {
        
        try {
            File file = new File(fname);   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); 
            int rowNum = sheet.getLastRowNum()+1;
            Iterator<Row> itr = sheet.iterator();
            int process = 0;
            //iterating over excel file  
            while (itr.hasNext()) {
                process = process + 1;
                Status.setText("Importing "+ process + " out of "+ rowNum);
                String code = "";
                String desc = "New Product";
                String unit = "Unit";
                String price = "0";
                String priceOrig = "0";
                String quantity = "0";
                String expiration = "N/A";
                Row row = itr.next();

                Cell cellCode = row.getCell(0);
                Cell cellDesc = row.getCell(1);
                Cell cellUnit = row.getCell(2);
                Cell cellPriceOrig = row.getCell(3);
                Cell cellPrice = row.getCell(4);
                Cell cellQuantity = row.getCell(5);
                Cell cellExpiration = row.getCell(6);

                switch (cellCode.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        code = cellCode.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        double code_double = cellCode.getNumericCellValue();
                        code = String.valueOf(code_double).split("\\.")[0];
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        double code_formula = cellCode.getNumericCellValue();
                        code = String.valueOf(code_formula).split("\\.")[0];
                        break;
                    default:
                }
                switch (cellDesc.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        desc = cellDesc.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        double desc_double = cellDesc.getNumericCellValue();
                        desc = String.valueOf(desc_double).split("\\.")[0];
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        double desc_formula = cellDesc.getNumericCellValue();
                        desc = String.valueOf(desc_formula).split("\\.")[0];
                        break;
                    default:
                }
                switch (cellUnit.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        unit = cellUnit.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        double unit_double = cellUnit.getNumericCellValue();
                        unit = String.valueOf(unit_double).split("\\.")[0];
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        double unit_formula = cellUnit.getNumericCellValue();
                        unit = String.valueOf(unit_formula).split("\\.")[0];
                        break;
                    default:
                }
                switch (cellPriceOrig.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        priceOrig = cellPriceOrig.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        double price_double_orig = cellPriceOrig.getNumericCellValue();
                        priceOrig = String.valueOf(price_double_orig).split("\\.")[0];
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        double price_formula_orig = cellPriceOrig.getNumericCellValue();
                        priceOrig = String.valueOf(price_formula_orig).split("\\.")[0];
                        break;
                    default:
                }
                switch (cellPrice.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        price = cellPrice.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        double price_double = cellPrice.getNumericCellValue();
                        price = String.valueOf(price_double).split("\\.")[0];
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        double price_formula = cellPrice.getNumericCellValue();
                        price = String.valueOf(price_formula).split("\\.")[0];
                        break;
                    default:
                }
                switch (cellQuantity.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        quantity = cellQuantity.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        double quantity_double = cellQuantity.getNumericCellValue();
                        quantity = String.valueOf(quantity_double).split("\\.")[0];
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        double quantity_formula = cellQuantity.getNumericCellValue();
                        quantity = String.valueOf(quantity_formula).split("\\.")[0];
                        break;
                    default:
                }
                switch (cellExpiration.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        expiration = cellExpiration.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        double expiration_double = cellExpiration.getNumericCellValue();
                        expiration = String.valueOf(expiration_double).split("\\.")[0];
                        break;
                    case Cell.CELL_TYPE_FORMULA:
                        double expiration_formula = cellExpiration.getNumericCellValue();
                        expiration = String.valueOf(expiration_formula).split("\\.")[0];
                        break;
                    default:
                }
                
                try{
                    Connection con=DBConnection.DBConnection();
                    PreparedStatement pst = con.prepareStatement("SELECT * FROM menus WHERE menuCode = '"+code+"' OR menuDesc = '"+desc+"'");
                    ResultSet rs = pst.executeQuery();
                    if(rs.next()){
                        int prod_id = rs.getInt("menuID");
                        PreparedStatement pstUpdate = con.prepareStatement("UPDATE stocks_inventory SET available_stock = '"+quantity+"' WHERE prod_id = '"+prod_id+"'");
                        pstUpdate.executeUpdate();
                        //Update stocks expiration
                        PreparedStatement pstInsertStockPE = con.prepareStatement("INSERT INTO stocks_expiration(product_id, quantity, original_price, expiration_date) VALUES(?,?,?,?)");
                        pstInsertStockPE.setInt(1, prod_id);
                        pstInsertStockPE.setInt(2, Integer.parseInt(quantity));
                        pstInsertStockPE.setString(3, priceOrig);
                        pstInsertStockPE.setString(4, expiration);
                        pstInsertStockPE.executeUpdate();
                        //Update menu
                        double orPrice = Double.parseDouble(priceOrig);
                        double newPrice = Double.parseDouble(price);
                        PreparedStatement pstUpdateMenu = con.prepareStatement("UPDATE menus SET origPrice='"+orPrice+"', menuPrice = '"+newPrice+"' WHERE menuID = '"+prod_id+"'");
                        pstUpdateMenu.executeUpdate();
                        
                    }else{
                        PreparedStatement pstInsert = con.prepareStatement("INSERT INTO menus(menuCode,menuDesc,origPrice,menuPrice) VALUES(?,?,?,?)");
                        pstInsert.setString(1, code);
                        pstInsert.setString(2, desc);
                        pstInsert.setDouble(3, Double.parseDouble(priceOrig));
                        pstInsert.setDouble(4, Double.parseDouble(price));
                        pstInsert.executeUpdate();
                        //Insert Inventory
                        PreparedStatement getID = con.prepareStatement("SELECT menuID FROM menus WHERE menuCode = '"+code+"'");
                        ResultSet rsID = getID.executeQuery();
                        if(rsID.next()){
                            int prodIDInventory = rsID.getInt("menuID");
                            //Insert Inventory
                            PreparedStatement pstInsertInventory = con.prepareStatement("INSERT INTO stocks_inventory(prod_id, unit, available_stock) VALUES(?,?,?)");
                            pstInsertInventory.setInt(1, prodIDInventory);
                            pstInsertInventory.setString(2, unit);
                            pstInsertInventory.setInt(3, Integer.parseInt(quantity));
                            pstInsertInventory.executeUpdate();
                            //Insert Product Expiration
                            PreparedStatement pstInsertPE = con.prepareStatement("INSERT INTO stocks_expiration(product_id, quantity, original_price, expiration_date) VALUES(?,?,?,?)");
                            pstInsertPE.setInt(1, prodIDInventory);
                            pstInsertPE.setInt(2, Integer.parseInt(quantity));
                            pstInsertPE.setString(3, priceOrig);
                            pstInsertPE.setString(4, expiration);
                            pstInsertPE.executeUpdate();
                            
                        }
                    }
                }
                catch(SQLException ex){
                    System.out.println(ex);
                }
                /*
                System.out.print(code + "|");
                System.out.print(desc + "|");
                System.out.print(unit + "|");
                System.out.print(priceOrig + "|");
                System.out.print(price + "|");
                System.out.print(quantity + "|");
                System.out.print(expiration + "|");
                System.out.println("");
                */
                Thread.sleep(500);   
            }
        } catch (IOException e) {
            System.out.println( e);
        }
        Status.setText("Product and inventory imported.");
    }

    /**
     * Creates new form QuantPrompt
     */
    public ImportPrompt() {
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        AddButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        Status = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 150));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Product Import");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, -1, -1));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 50));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Void Sales");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Please select File to import");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, -1, -1));

        AddButton.setBackground(new java.awt.Color(0, 204, 0));
        AddButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        AddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/AddMenu.png"))); // NOI18N
        AddButton.setText("Import");
        AddButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        jPanel1.add(AddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 130, 40));

        jButton1.setText("Browse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 90, -1, 30));
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 300, 30));

        Status.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel1.add(Status, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 240, 40));

        jLabel1.setText("Ask Import file first");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        Status.setText("Importing please wait..");
        int sure = JOptionPane.showConfirmDialog(null, "Before importing, make sure that the excel file contains exact quantity base on the latest inventory, Click Yes to continue", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if(sure == JOptionPane.YES_OPTION){
            
            try {
                StartImport();
            } catch (InterruptedException ex) {
                Logger.getLogger(ImportPrompt.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
           Status.setText("Import Cancelled"); 
        }
        
    }//GEN-LAST:event_AddButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            selectFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoreInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        System.out.print("Clicked");
        OutputStream out = null;
        
        
    }//GEN-LAST:event_jLabel1MouseClicked

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
            java.util.logging.Logger.getLogger(ImportPrompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImportPrompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImportPrompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImportPrompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ImportPrompt().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton AddButton;
    public javax.swing.JLabel Status;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
