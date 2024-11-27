/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsalesystem;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Michael Paul Sebando
 */
public class ProfitPerItem extends javax.swing.JFrame {

    /**
     * Creates new form ProfitPerItem
     */
    public ProfitPerItem() {
        initComponents();
        ImageIcon imgicon=new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        this.setTitle("Store Profit Per Item");
        display_item_profit();
    }
    private void display_item_profit(){
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("SELECT menus.menuID,menus.menuDesc,sum(sales.Quantity) AS total_sold_item,menus.origPrice, sum(sales.salesAmount) AS total_sales FROM menus INNER JOIN sales ON menus.menuID=sales.menuID GROUP BY menus.menuID LIMIT 200");
            ResultSet rs=pst.executeQuery();
            DefaultTableModel dtm=(DefaultTableModel)SalesTable.getModel();
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment( JLabel.CENTER );
            SalesTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
            
            dtm.setRowCount(0);
            while(rs.next()){
                String prod_id = rs.getString("menuID");
                double cap = 0;
                PreparedStatement pst1 = con.prepareStatement("SELECT SUM(original_price) * SUM(quantity) AS capital FROM stocks_expiration WHERE product_id = "+prod_id+"");
                ResultSet rs1 = pst1.executeQuery();
                if(rs1.next()){
                    cap = rs1.getDouble("capital");
                }

                //double capital=rs.getDouble("total_sold_item")  * rs.getDouble("menus.origPrice");
                
                double prof_loss=rs.getDouble("total_sales")-cap;
                
                Object proloss_data[]={rs.getInt("menuId"), rs.getString("menus.menuDesc"),rs.getInt("total_sold_item"),cap,rs.getDouble("total_sales"),prof_loss};
                dtm.addRow(proloss_data);
                
                
            }
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void display_item_profit_by_name(){
        try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("SELECT menus.menuID,menus.menuDesc,sum(sales.Quantity) AS total_sold_item,menus.origPrice, sum(sales.salesAmount) AS total_sales FROM menus INNER JOIN sales ON menus.menuID=sales.menuID WHERE menus.menuDesc LIKE '%"+ProductEntry.getText()+"%' OR menus.menuCode LIKE '%"+ProductEntry.getText()+"%' GROUP BY menus.menuID LIMIT 200");
            ResultSet rs=pst.executeQuery();
            DefaultTableModel dtm=(DefaultTableModel)SalesTable.getModel();
            dtm.setRowCount(0);
            while(rs.next()){
                String prod_id = rs.getString("menuID");
                double cap = 0;
                PreparedStatement pst1 = con.prepareStatement("SELECT SUM(original_price) * SUM(quantity) AS capital FROM stocks_expiration WHERE product_id = "+prod_id+"");
                ResultSet rs1 = pst1.executeQuery();
                if(rs1.next()){
                    cap = rs1.getDouble("capital");
                }

                //double capital=rs.getDouble("total_sold_item")  * rs.getDouble("menus.origPrice");
                
                double prof_loss=rs.getDouble("total_sales")-cap;
                
                
                //double capital=rs.getDouble("total_sold_item")  * rs.getDouble("menus.origPrice");
                //double prof_loss=rs.getDouble("total_sales")-cap;
                Object proloss_data[]={rs.getInt("menuId"), rs.getString("menus.menuDesc"),rs.getInt("total_sold_item"),cap,rs.getDouble("total_sales"),prof_loss};
                dtm.addRow(proloss_data);
                
                
            }
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void BarChartExample() {

        JInternalFrame internalFrame = new JInternalFrame("", true, true, true, true);
        internalFrame.setResizable(true);
        internalFrame.setSize(new java.awt.Dimension(jPanel6.getWidth(), jPanel6.getHeight()));
        internalFrame.setLayout(new FlowLayout());
        internalFrame.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
        internalFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        //((BasicInternalFrameUI) internalFrame.getUI()).setNorthPane(null);
        //((javax.swing.plaf.basic.BasicInternalFrameUI)internalFrame.getUI()).setNorthPane(null);
        internalFrame.setBorder(null);
        internalFrame.setVisible(true);

        // Create the dataset (replace this with your data)
        CategoryDataset dataset = createDataset();

        // Create the bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Capital versus Sales", // Chart title
                "Product", // X-axis label
                "Comparison", // Y-axis label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot orientation
                true, // Show legend
                true, // Use tooltips
                false // Generate URLs
        );

        // Add the chart to a ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(jPanel6.getWidth(), jPanel6.getHeight()));
        chartPanel.setSize(new java.awt.Dimension(jPanel6.getWidth(), jPanel6.getHeight()));
        internalFrame.add(chartPanel,BorderLayout.CENTER);

        jPanel6.add(internalFrame);
        jPanel6.validate();
        //setContentPane(jDesktopPane1);

    }
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
    private CategoryDataset createDataset() {

        
          try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("SELECT menus.menuPrice, menus.menuID,menus.menuDesc,sum(sales.Quantity) AS total_sold_item,menus.origPrice, sum(sales.salesAmount) AS total_sales FROM menus INNER JOIN sales ON menus.menuID=sales.menuID WHERE YEAR(sales.salesDate) = YEAR(CURRENT_DATE()) GROUP BY menus.menuID LIMIT 20");
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                String prod_id = rs.getString("menuID");
                double cap = 0;
                PreparedStatement pst1 = con.prepareStatement("SELECT SUM(original_price) * SUM(quantity) AS capital FROM stocks_expiration WHERE product_id = "+prod_id+"");
                ResultSet rs1 = pst1.executeQuery();
                if(rs1.next()){
                    cap = rs1.getDouble("capital");
                }

                double sales=rs.getDouble("total_sold_item")  * rs.getDouble("menus.menuPrice");
                
                double prof_loss=rs.getDouble("total_sales")-cap;
                
                dataset.addValue(cap, "Capital", rs.getString("menuDesc"));
                
                dataset.addValue(sales, "Sales", rs.getString("menuDesc"));
                
                
            }
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
            
            
        
        // Replace these sample values with your actual data

        return dataset;
    }
    private CategoryDataset createDatasetByName() {

            dataset.clear();
          try{
            Connection con=DBConnection.DBConnection();
            PreparedStatement pst=con.prepareStatement("SELECT menus.menuPrice, menus.menuID,menus.menuDesc,sum(sales.Quantity) AS total_sold_item,menus.origPrice, sum(sales.salesAmount) AS total_sales FROM menus INNER JOIN sales ON menus.menuID=sales.menuID WHERE menus.menuDesc LIKE '%"+ProductEntry.getText()+"%' GROUP BY menus.menuID LIMIT 200");
            ResultSet rs=pst.executeQuery();
            while(rs.next()){
                String prod_id = rs.getString("menuID");
                double cap = 0;
                PreparedStatement pst1 = con.prepareStatement("SELECT SUM(original_price) * SUM(quantity) AS capital FROM stocks_expiration WHERE product_id = "+prod_id+"");
                ResultSet rs1 = pst1.executeQuery();
                if(rs1.next()){
                    cap = rs1.getDouble("capital");
                }

                double sales=rs.getDouble("total_sold_item")  * rs.getDouble("menus.menuPrice");
                
                double prof_loss=rs.getDouble("total_sales")-cap;
                
                dataset.addValue(cap, "Capital", rs.getString("menuDesc"));
                
                dataset.addValue(sales, "Sales", rs.getString("menuDesc"));
                
                
            }
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
            
            
        
        // Replace these sample values with your actual data

        return dataset;
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
        jLabel14 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        SalesTable = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        ProductEntry = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 768));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Profit Viewer Per Item");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 440, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 790, 40));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SalesTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Prod ID", "Item", "Item Sold", "Capital", "Sales", "Profit/Loss"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        SalesTable.setRowHeight(24);
        SalesTable.setSelectionBackground(new java.awt.Color(0, 204, 0));
        jScrollPane1.setViewportView(SalesTable);
        if (SalesTable.getColumnModel().getColumnCount() > 0) {
            SalesTable.getColumnModel().getColumn(0).setPreferredWidth(8);
            SalesTable.getColumnModel().getColumn(1).setPreferredWidth(220);
        }

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 770, 170));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Search_48px.png"))); // NOI18N
        jPanel5.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, 70, 40));

        ProductEntry.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ProductEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductEntryActionPerformed(evt);
            }
        });
        ProductEntry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ProductEntryKeyTyped(evt);
            }
        });
        jPanel5.add(ProductEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 520, 40));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 758, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 328, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel6);

        jPanel5.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 760, 330));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 6, -1, 610));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 790, 620));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 830, 690));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 870, 710));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ProductEntryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProductEntryKeyTyped
        display_item_profit_by_name();
        createDatasetByName();
    }//GEN-LAST:event_ProductEntryKeyTyped

    private void ProductEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductEntryActionPerformed
        display_item_profit_by_name();
        createDatasetByName();
        
    }//GEN-LAST:event_ProductEntryActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        BarChartExample();
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        double width = screenSize.getWidth();
//        double height = screenSize.getHeight();
//        setBounds(0,0,screenSize.width, screenSize.height);
//        jPanel2.setSize(screenSize.width, screenSize.height);
//
//        System.out.println(height);
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(ProfitPerItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProfitPerItem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ProductEntry;
    private javax.swing.JTable SalesTable;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
