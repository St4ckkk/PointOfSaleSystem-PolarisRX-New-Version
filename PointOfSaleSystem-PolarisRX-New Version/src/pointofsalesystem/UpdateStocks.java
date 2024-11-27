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
import java.awt.event.ActionEvent;
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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import static pointofsalesystem.POSController.DateLabel;
import static pointofsalesystem.POSController.psrvc;

/**
 *
 * @author Michael Paul Sebando
 */
public class UpdateStocks extends javax.swing.JFrame {

    /**
     * Creates new form UpdateStocks
     */
    public UpdateStocks() {
        initComponents();
        ImageIcon imgicon = new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        this.setTitle("Stocks Inventory");
        load_stocks();
    }
    static String code;
    String product_stock;
    int ending;
    int reStck;
    int beginning;

    //Start of New Block Codes
    private void load_stocks() {
        try {
            Connection con = DBConnection.DBConnection();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM menus INNER JOIN stocks_inventory ON menus.menuID=stocks_inventory.prod_id ORDER by(menus.menuDesc) ASC");
            ResultSet rs = pst.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel) ProductList.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                int beginning_inventory = 0;
                int ending_inventory = 0;
                String latestStock = "Not Available";
                int menuID = rs.getInt("menus.menuID");
                PreparedStatement pstGetBeginEnd = con.prepareStatement("SELECT * FROM stck_begin_end WHERE prod_id='"+menuID+"'");
                ResultSet rsGetBeginEnd = pstGetBeginEnd.executeQuery();
                if(rsGetBeginEnd.next()){
                    beginning_inventory = rsGetBeginEnd.getInt("begin");
                    ending_inventory = rsGetBeginEnd.getInt("end");
                    latestStock = rsGetBeginEnd.getString("added");
                }
                
                Object o[] = {rs.getInt("menus.menuID"), rs.getString("menus.menuCode"),rs.getString("menus.menuDesc"),ending_inventory, beginning_inventory, rs.getString("menus.menuPrice"), rs.getInt("stocks_inventory.available_stock"), latestStock};
                dtm.addRow(o);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    private void search_stocks() {
        try {
            Connection con = DBConnection.DBConnection();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM menus INNER JOIN stocks_inventory ON menus.menuID=stocks_inventory.prod_id WHERE menus.menuDesc LIKE '%" + prodDesc.getText() + "%' ORDER by(menus.menuDesc) ASC");
            ResultSet rs = pst.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel) ProductList.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                int beginning_inventory = 0;
                int ending_inventory = 0;
                String latestStock = "Not Available";
                int menuID = rs.getInt("menus.menuID");
                PreparedStatement pstGetBeginEnd = con.prepareStatement("SELECT * FROM stck_begin_end WHERE prod_id='"+menuID+"'");
                ResultSet rsGetBeginEnd = pstGetBeginEnd.executeQuery();
                if(rsGetBeginEnd.next()){
                    beginning_inventory = rsGetBeginEnd.getInt("begin");
                    ending_inventory = rsGetBeginEnd.getInt("end");
                    latestStock = rsGetBeginEnd.getString("added");
                }
                
                Object o[] = {rs.getInt("menus.menuID"), rs.getString("menus.menuCode"),rs.getString("menus.menuDesc"),ending_inventory, beginning_inventory, rs.getString("menus.menuPrice"), rs.getInt("stocks_inventory.available_stock"), latestStock};
                dtm.addRow(o);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }


    }

    private void search_code() {
        try {
            Connection con = DBConnection.DBConnection();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM menus INNER JOIN stocks_inventory ON menus.menuID=stocks_inventory.prod_id WHERE menus.menuCode = '" + ProdCode.getText() + "' ORDER by(menus.menuDesc) ASC");
            ResultSet rs = pst.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel) ProductList.getModel();
            dtm.setRowCount(0);
            while (rs.next()) {
                int beginning_inventory = 0;
                int ending_inventory = 0;
                String latestStock = "Not Available";
                int menuID = rs.getInt("menus.menuID");
                PreparedStatement pstGetBeginEnd = con.prepareStatement("SELECT * FROM stck_begin_end WHERE prod_id='"+menuID+"'");
                ResultSet rsGetBeginEnd = pstGetBeginEnd.executeQuery();
                if(rsGetBeginEnd.next()){
                    beginning_inventory = rsGetBeginEnd.getInt("begin");
                    ending_inventory = rsGetBeginEnd.getInt("end");
                    latestStock = rsGetBeginEnd.getString("added");
                }
                
                Object o[] = {rs.getInt("menus.menuID"), rs.getString("menus.menuCode"),rs.getString("menus.menuDesc"),ending_inventory, beginning_inventory, rs.getString("menus.menuPrice"), rs.getInt("stocks_inventory.available_stock"), latestStock};
                dtm.addRow(o);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }


    }

    private void select_stock() {
        int selected_row = ProductList.getSelectedRow();
        int prod_id = Integer.parseInt(ProductList.getModel().getValueAt(selected_row, 0).toString());
        String prod_code = ProductList.getModel().getValueAt(selected_row, 1).toString();
        String prod_desc = ProductList.getModel().getValueAt(selected_row, 2).toString();
        String prod_price = ProductList.getModel().getValueAt(selected_row, 5).toString();
        int prod_quantity = Integer.parseInt(ProductList.getModel().getValueAt(selected_row, 6).toString());
        ProdID.setText(Integer.toString(prod_id));
        ProdCode.setText(prod_code);
        prodDesc.setText(prod_desc);
        product_stock = prod_desc;
        av_stocks.setText(Integer.toString(prod_quantity));
        MenuPrice.setText(prod_price);
    }

    private void restock() {
        int old_stock = Integer.parseInt(av_stocks.getText());
        int new_stock = Integer.parseInt(newStock.getText());
        int updated_stock = old_stock + new_stock;
        try {
            Connection con = DBConnection.DBConnection();
            PreparedStatement pst = con.prepareStatement("UPDATE stocks_inventory SET available_stock = ? WHERE prod_id='" + ProdID.getText() + "'");
            pst.setInt(1, updated_stock);
            int update = JOptionPane.showConfirmDialog(null, "Are you sure you want to update this stock?", "Warning", JOptionPane.YES_NO_OPTION);
            if (update == JOptionPane.YES_OPTION) {
                pst.executeUpdate();
                updateExpiration();
                
                updateStckBeginEnd(Integer.parseInt(ProdID.getText()), old_stock, updated_stock, new_stock);
                
                updatePrice();
                JOptionPane.showMessageDialog(null, "Stock Inventory Updated!");
                load_stocks();
                clearFields();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void updatePrice() {
        try {
            Connection con = DBConnection.DBConnection();
            PreparedStatement pst = con.prepareStatement("UPDATE menus SET menuPrice = '" + MenuPrice.getText() + "' WHERE menuID = '" + ProdID.getText() + "'");
            pst.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void updateExpiration() {
        int product_id = Integer.parseInt(ProdID.getText());
        int new_stock = Integer.parseInt(newStock.getText());
        String expiration_date = ((JTextField) ExpirationDate.getDateEditor().getUiComponent()).getText();
        
        try {
            Connection con = DBConnection.DBConnection();
            PreparedStatement pst = con.prepareStatement("INSERT INTO stocks_expiration(product_id, quantity, original_price, expiration_date, lot_no) VALUES(?,?,?,?,?)");
            pst.setInt(1, product_id);
            pst.setInt(2, new_stock);     
            pst.setString(3, OrigPrice.getText());
            pst.setString(4, expiration_date);
            pst.setString(5, LotNo.getText());
            pst.executeUpdate();
            
            PreparedStatement pst1 = con.prepareStatement("UPDATE menus SET origPrice = '"+OrigPrice.getText()+"' WHERE menuID = '"+product_id+"'");
            pst1.executeUpdate();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    private void updateStckBeginEnd(int prodID, int oldStck, int newStck, int reStock ){
        ending = oldStck;
        reStck = reStock;
        beginning = newStck;
        try {
            Connection con = DBConnection.DBConnection();
            PreparedStatement pstgetStock = con.prepareStatement("SELECT * FROM stck_begin_end LEFT JOIN menus ON stck_begin_end.prod_id = menus.menuID WHERE stck_begin_end.prod_id = '"+prodID+"'");
            ResultSet rsgetStock = pstgetStock.executeQuery();
            if(rsgetStock.next()){
                //Update inventory
                PreparedStatement pstUpdateStck = con.prepareStatement("UPDATE stck_begin_end SET begin = '"+newStck+"', end = '"+oldStck+"' WHERE prod_id ='"+prodID+"'");
                pstUpdateStck.executeUpdate();
            }
            else{
                //Insert Inventory
                PreparedStatement pstInsertStck = con.prepareStatement("INSERT INTO stck_begin_end(prod_id,begin,end) VALUES(?,?,?)");
                pstInsertStck.setInt(1, prodID);
                pstInsertStck.setInt(2, beginning);
                pstInsertStck.setInt(3, ending);
                pstInsertStck.executeUpdate();
            }
            int printDR = JOptionPane.showConfirmDialog(null, "Do you want to print Restock/DR Reciept?","Print Restock/DR",JOptionPane.YES_NO_OPTION);
            if(printDR == JOptionPane.YES_OPTION){
                printDR();
            }
            else{
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(UpdateStocks.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printOutputReciept() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new UpdateStocks.BillPrintable(), getPageFormat(pj));
        try {
            pj.setPrintService(psrvc);
        } catch (PrinterException ex) {
            Logger.getLogger(LowStocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        pj.setJobName("Remaining Stocks Printing");
        try {
            pj.print();

        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private void printDR() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new UpdateStocks.DRPrintable(), getPageFormat(pj));
        try {
            pj.setPrintService(psrvc);
        } catch (PrinterException ex) {
            Logger.getLogger(LowStocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        pj.setJobName("Delivery Reciept");
        try {
            pj.print();

        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public PageFormat getPageFormat(PrinterJob pj) {

        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        double middleHeight = 8.0;
        double headerHeight = 2.0;
        double footerHeight = 2.0;
        double width = convertCMToPPI(6);      //printer know only point per inch.default value is 72ppi
        double height = convertCMToPPI(headerHeight * middleHeight * footerHeight);
        paper.setSize(width, pf.getHeight());
        /*paper.setImageableArea(
                0,
                8,
                width,
                height - convertCMToPPI(1000)
        );   //define boarder size    after that print area width is about 180 points
        */
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
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            int result = NO_SUCH_PAGE;
            if (pageIndex == 0) {

                Graphics2D g2d = (Graphics2D) graphics;

                double width = pageFormat.getImageableWidth();

                g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

                ////////// code by alqama//////////////
                FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.BOLD, 7));
                //    int idLength=metrics.stringWidth("000000");
                //int idLength=metrics.stringWidth("00");
                int idLength = metrics.stringWidth("000");
                int amtLength = metrics.stringWidth("000000");
                int qtyLength = metrics.stringWidth("00000");
                int priceLength = metrics.stringWidth("000000");
                int prodLength = (int) width - idLength - amtLength - qtyLength - priceLength - 17;

                //    int idPosition=0;
                //    int productPosition=idPosition + idLength + 2;
                //    int pricePosition=productPosition + prodLength +10;
                //    int qtyPosition=pricePosition + priceLength + 2;
                //    int amtPosition=qtyPosition + qtyLength + 2;
                int productPosition = 0;
                int discountPosition = prodLength + 5;
                int pricePosition = discountPosition + idLength + 10;
                int qtyPosition = pricePosition + priceLength + 4;
                int amtPosition = qtyPosition + qtyLength;

                try {
                    /*Draw Header*/
                    int y = 17;
                    int yShift = 6;
                    int headerRectHeight = 13;
                    int headerRectHeighta = 40;

                    g2d.setColor(Color.black);
                    g2d.drawImage(POSController.background, 44, 1, null);
                    y += 64;
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    g2d.drawString("------------------------------------", 2, y);
                    y += yShift+3;
                    g2d.drawString("STOCK INVENTORY ", 2, y);
                    y += yShift+3;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift+3;
                    g2d.drawString("ITEM | REM. STCK | COUNT", 2, y);
                    y += yShift+3;
                    g2d.drawString("", 2, y);
                    y += headerRectHeight;

                    try {
                        Connection con = DBConnection.DBConnection();
                        PreparedStatement pst = con.prepareStatement("SELECT * FROM menus INNER JOIN stocks_inventory ON menus.menuID=stocks_inventory.prod_id  ORDER by(menus.menuDesc) ASC");
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            String product = rs.getString("menuDesc");
                            String stock = rs.getString("available_stock");
                            g2d.drawString("" + product, 2, y);
                            y += yShift+3;
                            g2d.drawString("           " + stock + "     [____]", 2, y);
                            y += yShift+3;
                            g2d.drawString("--------------------------", 2, y);
                            y += yShift+3;
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(UpdateStocks.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift+3;
                    g2d.drawString("Point of Sale System    ", 2, y);
                    y += yShift+3;
                    g2d.drawString("Developed by MPOS          ", 2, y);
                    y += yShift+3;
                    g2d.drawString("All rights reserved         ", 2, y);
                    y += yShift+3;
                    g2d.drawString("*************************************", 2, y);
                    y += yShift+3;
//            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift; 
                } catch (NumberFormatException r) {
                    JOptionPane.showMessageDialog(null, r);
                }
                result = PAGE_EXISTS;
            }
            return result;
        }
    }
    
    public class DRPrintable implements Printable {

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            int result = NO_SUCH_PAGE;
            if (pageIndex == 0) {

                Graphics2D g2d = (Graphics2D) graphics;

                double width = pageFormat.getImageableWidth();

                g2d.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

                ////////// code by alqama//////////////
                FontMetrics metrics = g2d.getFontMetrics(new Font("Arial", Font.BOLD, 7));
                //    int idLength=metrics.stringWidth("000000");
                //int idLength=metrics.stringWidth("00");
                int idLength = metrics.stringWidth("000");
                int amtLength = metrics.stringWidth("000000");
                int qtyLength = metrics.stringWidth("00000");
                int priceLength = metrics.stringWidth("000000");
                int prodLength = (int) width - idLength - amtLength - qtyLength - priceLength - 17;

                //    int idPosition=0;
                //    int productPosition=idPosition + idLength + 2;
                //    int pricePosition=productPosition + prodLength +10;
                //    int qtyPosition=pricePosition + priceLength + 2;
                //    int amtPosition=qtyPosition + qtyLength + 2;
                int productPosition = 0;
                int discountPosition = prodLength + 5;
                int pricePosition = discountPosition + idLength + 10;
                int qtyPosition = pricePosition + priceLength + 4;
                int amtPosition = qtyPosition + qtyLength;

                try {
                    /*Draw Header*/
                    int y = 17;
                    int yShift = 6;
                    int headerRectHeight = 13;
                    int headerRectHeighta = 40;
                    g2d.setColor(Color.black);
                    g2d.drawImage(POSController.background, 44, 1, null);
                    y += 64;
                    
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    g2d.drawString("------------------------------------", 2, 0);
                    y += yShift+6;
                    g2d.drawString("DR AND PRODUCT RESTOCK ", 2, y);
                    y += yShift+3;
                    g2d.drawString("Date: " + DateLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift+3;
                    g2d.drawString("" + product_stock, 2, y);
                    y += yShift+3;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift+3;
                    g2d.drawString("ENDING | NEW | BEGINNING", 2, y);
                    y += yShift+3;
                    g2d.drawString("", 2, y);
                    y += headerRectHeight;
                    g2d.drawString("" + ending + "       "+reStck+"      " +beginning+"", 2, y);
                    y += yShift+3;
                    g2d.drawString("--------------------------", 2, y);
                    y += yShift+3;
                    g2d.drawString("Point of Sale System    ", 2, y);
                    y += yShift+3;
                    g2d.drawString("Developed by MPOS          ", 2, y);
                    y += yShift+3;
                    g2d.drawString("All rights reserved         ", 2, y);
                    y += yShift+3;
                    g2d.drawString("*************************************", 2, y);
                    y += yShift+3;
//            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift; 
                } catch (NumberFormatException r) {
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        ProdID = new javax.swing.JTextField();
        newStock = new javax.swing.JTextField();
        Exapiration = new javax.swing.JLabel();
        ExpirationDate = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        OrigPrice = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        MenuPrice = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ProductList = new javax.swing.JTable();
        AddButton = new javax.swing.JButton();
        av_stocks = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        AddButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Product ID");
        jPanel4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 23, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Product Code");
        jPanel4.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Product Desc.");
        jPanel4.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 90, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("New Stocks");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 146, -1, -1));

        ProdID.setEditable(false);
        ProdID.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(ProdID, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 302, -1));

        ProdCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ProdCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProdCodeActionPerformed(evt);
            }
        });
        jPanel4.add(ProdCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 302, -1));

        prodDesc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        prodDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                prodDescKeyTyped(evt);
            }
        });
        jPanel4.add(prodDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 302, -1));

        newStock.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        newStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                newStockKeyTyped(evt);
            }
        });
        jPanel4.add(newStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 100, 30));

        Exapiration.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        Exapiration.setText("Expiration");
        jPanel4.add(Exapiration, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 80, 28));

        ExpirationDate.setDateFormatString("yyyy-MM-dd");
        jPanel4.add(ExpirationDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, 120, 30));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel8.setText("Lot #");
        jPanel4.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 180, -1, 28));

        OrigPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        OrigPrice.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel4.add(OrigPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 70, 30));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel7.setText("New Price");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 110, 80, 28));

        MenuPrice.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        MenuPrice.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jPanel4.add(MenuPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, 80, 30));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel9.setText("Cost");
        jPanel4.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 110, -1, 28));

        LotNo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        LotNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                LotNoKeyTyped(evt);
            }
        });
        jPanel4.add(LotNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, 140, 30));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 450, 230));

        ProductList.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ProductList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "^", "Product Code", "Product Desc", "Ending", "Beginning", "Price", "Stocks", "Latest Restock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ProductList.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ProductList.setGridColor(new java.awt.Color(0, 0, 0));
        ProductList.setSelectionBackground(new java.awt.Color(0, 204, 0));
        ProductList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProductListMouseClicked(evt);
            }
        });
        ProductList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProductListKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(ProductList);
        if (ProductList.getColumnModel().getColumnCount() > 0) {
            ProductList.getColumnModel().getColumn(0).setMinWidth(1);
            ProductList.getColumnModel().getColumn(0).setPreferredWidth(1);
            ProductList.getColumnModel().getColumn(0).setMaxWidth(1);
            ProductList.getColumnModel().getColumn(1).setPreferredWidth(8);
            ProductList.getColumnModel().getColumn(2).setPreferredWidth(200);
            ProductList.getColumnModel().getColumn(6).setPreferredWidth(8);
        }

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 250, 640, 190));

        AddButton.setBackground(new java.awt.Color(0, 204, 0));
        AddButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        AddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Update User_50px.png"))); // NOI18N
        AddButton.setText("Update");
        AddButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        jPanel3.add(AddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 180, 60));

        av_stocks.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        av_stocks.setForeground(new java.awt.Color(0, 204, 51));
        av_stocks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        av_stocks.setText("0");
        jPanel3.add(av_stocks, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 200, 120, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Available Stocks");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 170, -1, -1));

        AddButton1.setBackground(new java.awt.Color(0, 204, 0));
        AddButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        AddButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Print.png"))); // NOI18N
        AddButton1.setText("Print Stocks");
        AddButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(AddButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 80, 180, 50));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 660, 450));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 204, 51));
        jLabel5.setText("STOCKS INVENTORY");
        jPanel5.add(jLabel5);

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 660, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 680, 510));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ProductListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProductListMouseClicked
        select_stock();
    }//GEN-LAST:event_ProductListMouseClicked

    private void ProductListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProductListKeyPressed

    }//GEN-LAST:event_ProductListKeyPressed

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        String expiration_date = ((JTextField) ExpirationDate.getDateEditor().getUiComponent()).getText();
        if (newStock.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please fill in additional stocks!", "Error!", JOptionPane.WARNING_MESSAGE);
        } else if (OrigPrice.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Product Purchase price is required!", "Error!", JOptionPane.WARNING_MESSAGE);
        }else if(LotNo.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Lot Number is required!", "Error!", JOptionPane.WARNING_MESSAGE);
        } 
        else if(expiration_date.isEmpty()){
            JOptionPane.showMessageDialog(null, "Expiration date is required!", "Error!", JOptionPane.WARNING_MESSAGE);
        }
        else {
            restock();
        }

    }//GEN-LAST:event_AddButtonActionPerformed

    private void clearFields() {
        ProdID.setText("");
        ProdCode.setText("");
        prodDesc.setText("");
        newStock.setText("");
        OrigPrice.setText("");
        ((JTextField) ExpirationDate.getDateEditor().getUiComponent()).setText("");
    }
    private void prodDescKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_prodDescKeyTyped
        search_stocks();
    }//GEN-LAST:event_prodDescKeyTyped

    private void newStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newStockKeyTyped
        char c = evt.getKeyChar();
        if (!(c == '-' || (c >= '0' && (c <= '9')))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_newStockKeyTyped

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        if(ProdCode.getText().isEmpty()){
           ProdCode.requestFocus(); 
        }
        else{
            
        }
        
    }//GEN-LAST:event_formWindowActivated

    private void ProdCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProdCodeActionPerformed
        av_stocks.setText("");
        search_code();
    }//GEN-LAST:event_ProdCodeActionPerformed

    private void AddButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButton1ActionPerformed
        printOutputReciept();
    }//GEN-LAST:event_AddButton1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
//        if ("".equals(code)) {
//
//        } else {
//            Timer tm = new Timer(300, (ActionEvent e) -> {
//                ProdCode.setText(code);
//                search_code();
//                
//            });
//            tm.start();
//            tm.setRepeats(false);
//        }
    }//GEN-LAST:event_formWindowOpened

    private void LotNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LotNoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_LotNoKeyTyped

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
            java.util.logging.Logger.getLogger(UpdateStocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateStocks().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddButton;
    private javax.swing.JButton AddButton1;
    private javax.swing.JLabel Exapiration;
    private com.toedter.calendar.JDateChooser ExpirationDate;
    public static final javax.swing.JTextField LotNo = new javax.swing.JTextField();
    private javax.swing.JFormattedTextField MenuPrice;
    private javax.swing.JFormattedTextField OrigPrice;
    public static final javax.swing.JTextField ProdCode = new javax.swing.JTextField();
    public static javax.swing.JTextField ProdID;
    private javax.swing.JTable ProductList;
    private javax.swing.JLabel av_stocks;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JTextField newStock;
    public static final javax.swing.JTextField prodDesc = new javax.swing.JTextField();
    // End of variables declaration//GEN-END:variables
}
