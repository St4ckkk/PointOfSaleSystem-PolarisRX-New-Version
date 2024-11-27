/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsalesystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static pointofsalesystem.POSController.psrvc;

/**
 *
 * @author Michael Paul Sebando
 */
public class LowStocks extends javax.swing.JFrame {

    /**
     * Creates new form LowStocks
     */
    public LowStocks() {
        initComponents();
        ImageIcon imgicon=new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        this.setTitle("View Lower Stocks");
        show_low_stocks();
    }
    
    private void show_low_stocks(){
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("SELECT * FROM stocks_inventory INNER JOIN menus ON menus.menuID=stocks_inventory.prod_id WHERE stocks_inventory.available_stock <= 20");
            ResultSet rs=pst.executeQuery();
            DefaultTableModel dtm=(DefaultTableModel)LowStocksTable.getModel();
            while(rs.next()){
                Object low_stocks[]={rs.getInt("prod_id"),rs.getString("menuDesc"), rs.getInt("available_stock"), rs.getString("unit")};
                dtm.addRow(low_stocks);
                
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void printOutputReciept(){
        
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new LowStocks.BillPrintable(),getPageFormat(pj));
        try {
            pj.setPrintService(psrvc);
        } catch (PrinterException ex) {
            Logger.getLogger(LowStocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        pj.setJobName("Low Stocks Printing");
        try {
             pj.print();
          
        }
         catch (PrinterException ex) {
                 JOptionPane.showMessageDialog(null, ex);
        }
    }
    public PageFormat getPageFormat(PrinterJob pj){
    
    PageFormat pf = pj.defaultPage();
    Paper paper = pf.getPaper();    

    double middleHeight =8.0;  
    double headerHeight = 2.0;                  
    double footerHeight = 2.0;                  
    double width = convertCMToPPI(6);      //printer know only point per inch.default value is 72ppi
    double height = convertCMToPPI(headerHeight+middleHeight+footerHeight); 
    paper.setSize(width, pf.getHeight());
    paper.setImageableArea(                    
        0,
        8,
        width,            
        height - convertCMToPPI(1000)
    );   //define boarder size    after that print area width is about 180 points
            
    pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
    pf.setPaper(paper);    

    return pf;
}   
    protected static double convertCMToPPI(double cm) {            
	        return toPPI(cm * 0.393600787);            
}
    protected static double toPPI(double inch) {            
	        return inch * 72d;            
}
    public class BillPrintable implements Printable { 
        @Override
        public int print(Graphics graphics, PageFormat pageFormat,int pageIndex) throws PrinterException {    
            int result = NO_SUCH_PAGE;    
            if (pageIndex == 0) {                    
        
            Graphics2D g2d = (Graphics2D) graphics;                    

            double width = pageFormat.getImageableWidth();                    
           
            g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY()); 

            ////////// code by alqama//////////////

            FontMetrics metrics=g2d.getFontMetrics(new Font("Arial",Font.BOLD,7));
        //    int idLength=metrics.stringWidth("000000");
            //int idLength=metrics.stringWidth("00");
            int idLength=metrics.stringWidth("000");
            int amtLength=metrics.stringWidth("000000");
            int qtyLength=metrics.stringWidth("00000");
            int priceLength=metrics.stringWidth("000000");
            int prodLength=(int)width - idLength - amtLength - qtyLength - priceLength-17;

        //    int idPosition=0;
        //    int productPosition=idPosition + idLength + 2;
        //    int pricePosition=productPosition + prodLength +10;
        //    int qtyPosition=pricePosition + priceLength + 2;
        //    int amtPosition=qtyPosition + qtyLength + 2;
            
            int productPosition = 0;
            int discountPosition= prodLength+5;
            int pricePosition = discountPosition +idLength+10;
            int qtyPosition=pricePosition + priceLength + 4;
                int amtPosition = qtyPosition + qtyLength;
                     
        try{ 
            /*Draw Header*/
            int y=17;
            int yShift = 6;
            int headerRectHeight=13;
            int headerRectHeighta=40;
            g2d.setColor(Color.black);
            g2d.drawImage(POSController.background, 44, 1, null);
            y += 64;
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
            g2d.drawString("------------------------------------",2,y);
            y+=yShift;
            g2d.setFont(new Font("Monospaced", Font.BOLD, 9));
            g2d.drawString("Low Stock Items " ,2,y);
            y+=yShift;
            g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
            g2d.drawString("-------------------------------------",2,y);
            y+=yShift;
            g2d.drawString("ITEM      REM STOCK",2,y);
            y+=yShift;
            g2d.drawString("-------------------------------------",2,y);
            y+=headerRectHeight;
            int rows=LowStocksTable.getRowCount();
            for(int row=0; row<rows; row++){
                int quantity=Integer.parseInt(LowStocksTable.getValueAt(row, 2).toString());
                String orderItem=(String)LowStocksTable.getValueAt(row, 1).toString();
                String unit=(String)LowStocksTable.getValueAt(row, 3).toString();
                int lenghtofItem=orderItem.length();
                
                
               
                g2d.drawString(""+orderItem                         ,2,y);
                y+=9;
                g2d.drawString("           "+quantity+"  "+unit ,2,y);
                y+=yShift;
                g2d.drawString("------------------------------------",2,y);y+=yShift;
                y+=yShift;
                
    
            }
            g2d.drawString("-------------------------------------",2,y);
            y+=yShift+4;
            g2d.drawString("Point of Sale System    "  ,2,y);
            y+=yShift+4;
            g2d.drawString("Developed by MPOS          "  ,2,y);
            y+=yShift+4;
            g2d.drawString("All rights reserved         "  ,2,y);
            y+=yShift+4;
            g2d.drawString("*************************************",2,y);
            y+=yShift+4;
//            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift; 
        }
        catch(NumberFormatException r){
            JOptionPane.showMessageDialog(null, r);
        }
              result = PAGE_EXISTS;    
          }    
          return result;    
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

        MenuList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        LowStocksTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        AddButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        MenuList.setBackground(new java.awt.Color(0, 255, 255));
        MenuList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LowStocksTable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LowStocksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Code", "Description", "Quantity", "Unit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        LowStocksTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        LowStocksTable.setSelectionBackground(new java.awt.Color(0, 204, 0));
        LowStocksTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LowStocksTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(LowStocksTable);
        if (LowStocksTable.getColumnModel().getColumnCount() > 0) {
            LowStocksTable.getColumnModel().getColumn(0).setPreferredWidth(8);
            LowStocksTable.getColumnModel().getColumn(1).setPreferredWidth(200);
            LowStocksTable.getColumnModel().getColumn(2).setPreferredWidth(40);
        }

        MenuList.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 139, 750, 350));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AddButton.setBackground(new java.awt.Color(0, 204, 0));
        AddButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        AddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/AddMenu.png"))); // NOI18N
        AddButton.setText("Print");
        AddButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        jPanel1.add(AddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, 130, 40));

        MenuList.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 750, 80));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 51));
        jLabel2.setText("LOW STOCKS VIEWER");
        jPanel3.add(jLabel2);

        MenuList.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 750, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuList, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MenuList, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void LowStocksTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LowStocksTableMouseClicked
    
    }//GEN-LAST:event_LowStocksTableMouseClicked

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        printOutputReciept();
        dispose();
    }//GEN-LAST:event_AddButtonActionPerformed

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
            java.util.logging.Logger.getLogger(LowStocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LowStocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LowStocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LowStocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LowStocks().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton AddButton;
    private javax.swing.JTable LowStocksTable;
    public javax.swing.JPanel MenuList;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
