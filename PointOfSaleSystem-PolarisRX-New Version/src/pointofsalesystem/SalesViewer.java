/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsalesystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Michael Paul Sebando
 */
public class SalesViewer extends javax.swing.JFrame {

    /**
     * Creates new form SalesViewer
     */
    public SalesViewer() {
        initComponents();
        ImageIcon imgicon = new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        this.setTitle("View Sales");
    }
    Connection con = DBConnection.DBConnection();

    private void view_sales() {
        String dateFrom = ((JTextField) FromDate.getDateEditor().getUiComponent()).getText();
        String dateTo = ((JTextField) toDate.getDateEditor().getUiComponent()).getText();
        try {
            Connection con = DBConnection.DBConnection();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM sales WHERE salesDate BETWEEN '" + dateFrom + "' AND '" + dateTo + "'");
            ResultSet rs = pst.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel) SalesTable.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                Object sales[] = {rs.getInt("salesID"), rs.getString("salesDate"), rs.getString("recieptNumber"), rs.getString("menuItem"), rs.getInt("Quantity"), rs.getString("salesCashier")};
                dtm.addRow(sales);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void compute_sales() {
        String dateFrom = ((JTextField) FromDate.getDateEditor().getUiComponent()).getText();
        String dateTo = ((JTextField) toDate.getDateEditor().getUiComponent()).getText();
        try {
            Connection con = DBConnection.DBConnection();
            PreparedStatement pst = con.prepareStatement("SELECT SUM(salesAmount) AS total_sales FROM sales WHERE salesDate BETWEEN '" + dateFrom + "' AND '" + dateTo + "'");
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Income.setText(Integer.toString(rs.getInt("total_sales")));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private static int SLIDER_INITIAL_VALUE = 50;
    private JSlider slider;
    private DateAxis domainAxis;
    private int lastValue = SLIDER_INITIAL_VALUE;
    private void BarChartExample() {
        
        JInternalFrame internalFrame = new JInternalFrame("", false, false, false, false);
        internalFrame.setSize(new java.awt.Dimension(jPanel9.getWidth(), jPanel9.getHeight()));
      

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
                "Top 20 Selling Product", // Chart title
                "Product", // X-axis label
                "Sold", // Y-axis label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot orientation
                true, // Show legend
                true, // Use tooltips
                false // Generate URLs
        );

        // Add the chart to a ChartPanel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);
        
        Border border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4,4,4,4),BorderFactory.createEtchedBorder() );
        chartPanel.setBorder(border);
        internalFrame.add(chartPanel,BorderLayout.CENTER);
        jPanel9.setBorder(BorderFactory.createEmptyBorder(0,4,4,4));
        
        
        jPanel9.add(internalFrame);
        //setContentPane(jDesktopPane1);
        

    }
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    private CategoryDataset createDataset() {
        
        String dateFrom = ((JTextField) FromDate.getDateEditor().getUiComponent()).getText();
        String dateTo = ((JTextField) FromDate.getDateEditor().getUiComponent()).getText();

        if (dateFrom.isEmpty() || dateTo.isEmpty()) {
            try {
                String sql = "SELECT menuID, menuItem, SUM(Quantity) AS totalSales FROM sales GROUP BY menuID ORDER BY totalSales DESC LIMIT 20";
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    double rem = 0;
                    String menuID = rs.getString("menuID");
                    PreparedStatement pst1 = con.prepareStatement("SELECT available_stock FROM stocks_inventory WHERE prod_id = '"+menuID+"'");
                    ResultSet rs1 = pst1.executeQuery();
                    if(rs1.next()){
                        rem = rs1.getDouble("available_stock");
                    }
                    dataset.addValue(rs.getDouble("totalSales"), "Sold", rs.getString("menuItem"));
                    dataset.addValue(rem, "Available Stock", rs.getString("menuItem"));
                }
            } catch (SQLException ex) {

            }
        } else {
            try {
                String sql = "SELECT menuItem, SUM(Quantity) AS totalSales FROM sales WHERE salesDate BETWEEN '"+dateFrom+"' AND '"+dateTo+"' GROUP BY menuID ORDER BY totalSales DESC";
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    dataset.addValue(rs.getDouble("totalSales"), "Product", rs.getString("menuItem"));
                }
            } catch (Exception ex) {

            }
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
        jLabel1 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        FromDate = new com.toedter.calendar.JDateChooser();
        toDate = new com.toedter.calendar.JDateChooser();
        FilterButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        Income = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();

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
        jLabel14.setText("Sales Viewer");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, 440, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 790, 40));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SalesTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        SalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Sales Date", "Reciept Number", "Item", "QTY", "Cashier"
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
        jScrollPane1.setViewportView(SalesTable);
        if (SalesTable.getColumnModel().getColumnCount() > 0) {
            SalesTable.getColumnModel().getColumn(0).setPreferredWidth(4);
            SalesTable.getColumnModel().getColumn(1).setPreferredWidth(80);
            SalesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            SalesTable.getColumnModel().getColumn(3).setPreferredWidth(200);
            SalesTable.getColumnModel().getColumn(4).setPreferredWidth(8);
            SalesTable.getColumnModel().getColumn(5).setPreferredWidth(200);
        }

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, 150));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 41, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Sales Invoices");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 13, -1, -1));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 790, 200));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Report Filter");
        jPanel11.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel11.setText("FROM");
        jPanel8.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 30));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel12.setText("TO");
        jPanel8.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, 30));

        FromDate.setDateFormatString("yyyy-MM-dd");
        FromDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel8.add(FromDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 224, 30));

        toDate.setDateFormatString("yyyy-MM-dd");
        toDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel8.add(toDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 224, 30));

        FilterButton.setBackground(new java.awt.Color(0, 204, 51));
        FilterButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        FilterButton.setText("Review");
        FilterButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        FilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FilterButtonActionPerformed(evt);
            }
        });
        jPanel8.add(FilterButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, -1, 60));

        jPanel11.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 45, 426, 80));

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, 450, 140));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Total Sales");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 13, -1, 30));

        Income.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        Income.setForeground(new java.awt.Color(0, 204, 0));
        Income.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Income.setText("0");
        Income.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.add(Income, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 195, 70));

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 528, 220, 130));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 770, 240));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 830, 670));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 870, 710));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void FilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FilterButtonActionPerformed
        
        String dateFrom = ((JTextField) FromDate.getDateEditor().getUiComponent()).getText();
        String dateTo = ((JTextField) FromDate.getDateEditor().getUiComponent()).getText();
        
        if (dateFrom.equals("")) {
            JOptionPane.showMessageDialog(null, "Please select beginning date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (dateTo.equals("")) {
            JOptionPane.showMessageDialog(null, "Please select ending date", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            view_sales();
            compute_sales();
            BarChartExample();
           

        if (dateFrom.isEmpty() || dateTo.isEmpty()) {
            try {
                String sql = "SELECT menuItem, SUM(Quantity) AS totalSales FROM sales GROUP BY menuID ORDER BY totalSales DESC";
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                dataset.clear();
                while (rs.next()) {
                    
                    dataset.addValue(rs.getDouble("totalSales"), "", rs.getString("menuItem"));
                }
            } catch (SQLException ex) {

            }
        } else {
            try {
                String sql = "SELECT menuItem, SUM(Quantity) AS totalSales FROM sales WHERE salesDate BETWEEN '"+dateFrom+"' AND '"+dateTo+"' GROUP BY menuID ORDER BY totalSales DESC";
                PreparedStatement pst = con.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();
                dataset.clear();
                while (rs.next()) {
                    
                    dataset.addValue(rs.getDouble("totalSales"), "Product", rs.getString("menuItem"));
                }
            } catch (SQLException ex) {

            }
        }
            
        }
    }//GEN-LAST:event_FilterButtonActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        BarChartExample();
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SalesViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SalesViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SalesViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SalesViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SalesViewer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton FilterButton;
    private com.toedter.calendar.JDateChooser FromDate;
    private javax.swing.JLabel Income;
    private javax.swing.JTable SalesTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser toDate;
    // End of variables declaration//GEN-END:variables
}