/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsalesystem;

import Payroll.PayrollScreen;
import employeeinformationsystem.LoginForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import static java.awt.PageAttributes.MediaType.D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author Michael Paul Sebando
 */
public class POSController extends javax.swing.JFrame {

    /**
     * Creates new form POSController
     */
    public POSController() {
        initComponents();
        ImageIcon imgicon = new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        //this.setTitle("Accounts Recievable");

    }
    Connection con = DBConnection.DBConnection();
    PreparedStatement pst;
    ResultSet rs;
    //Static Variables here
    double menuPrice;
    double itemPrice;
    public static double staticTotalAmount;
    public static double discountPrice;
    double prevSale;
    double prevDisc;
    String prevSalesDate;
    int prevCustID;
    double prevCash;
    double prevChange;
    String prevCashier;
    String prevTime;
    String orderMode = "Walk In";
    DefaultListModel lm = new DefaultListModel();
    String ContactNumber;
    String TIN;
    String storeName;
    String storeEmail;
    String storeAddress;
    String userTypeAccess;
    String userType;
    public static int creditor_id;
    public static String trans_type;
    String user_role;
    JFileChooser chooser;
    String choosertitle;
    String store;
    Blob aBlob = null;
    public static BufferedImage background;
    public static final float DPI = 72;
    String recieptNumber;
    String Reciept;
    public static boolean disCountOkay = false;
    public static PrintService psrvc;

    private void getDefaultPrinter() throws Exception {
        if (psrvc == null) {
            PrintService[] lps = PrintServiceLookup.lookupPrintServices(null, null);
            psrvc = (PrintService) JOptionPane.showInputDialog(this, "Select POS Printer first", "No POS Printer assigned", JOptionPane.PLAIN_MESSAGE, null, lps, lps[0]);
        } else {

        }
    }

    public static File selectDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null; // User canceled the selection
        }
    }

    public static void changeMode(String t_type, int customer) {
        creditor_id = customer;
        trans_type = t_type;
        OrderDesc.setText("Controller is in Credit Mode");
        OrderDesc.invalidate();
        OrderDesc.validate();
        OrderDesc.repaint();
        System.out.print(trans_type);
        System.out.print(creditor_id);

    }

    private void backupData() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDateTime = currentDateTime.format(formatter);
        String backUpName = NameOfStore.getText().substring(0, 5) + formattedDateTime + ".sql";
        String backupPath = "D:\\BackupDatabase\\" + backUpName + "";

        File selectedDirectory = selectDirectory();
        if (selectedDirectory != null) {

            String unsanitizedbackupPath = selectedDirectory.getAbsolutePath();

            String sanitizedPath = unsanitizedbackupPath.replace("\\", "\\\\");
            backupPath = sanitizedPath + "\\" + backUpName + "";

        } else {
            backupPath = "D:\\BackupDatabase\\" + backUpName + "";
            System.out.println("No directory selected.");
        }

        String database = "conveniencestore";
        String username = "root";
        String password = "";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "C:/xampp/mysql/bin/mysqldump", // Change this path if necessary
                    "--user=" + username,
                    "--password=" + password,
                    "--databases",
                    database);

            processBuilder.redirectOutput(ProcessBuilder.Redirect.to(new File(backupPath)));
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                JOptionPane.showMessageDialog(null, "Backup created successfully at " + backupPath + ".", "Backup Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error while creating the backup.", "Backup Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void getPrice() {
        try {
            pst = con.prepareStatement("SELECT menuPrice FROM menus WHERE menuID='" + OrderItem.getText() + "'");
            rs = pst.executeQuery();

            if (rs.next()) {
                menuPrice = rs.getDouble("menuPrice");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void listItem() {
        DefaultTableModel orderList = (DefaultTableModel) OrderListTable.getModel();
        try {
            pst = con.prepareStatement("SELECT * FROM menus WHERE menuID='" + OrderItem.getText() + "'");
            rs = pst.executeQuery();

            while (rs.next()) {
                Object o[] = {rs.getInt("menuID"), Quantity.getText(), rs.getString("menuDesc"), itemPrice + "0"};

                int isListed = getRowIndexByValue(OrderItem.getText());

                if (isListed == -1) {
                    orderList.addRow(o);
                } else {

                    DefaultTableModel dtm = (DefaultTableModel) OrderListTable.getModel();

                    int prevValue = Integer.parseInt(dtm.getValueAt(isListed, 1).toString());

                    int newValue = prevValue + Integer.parseInt(Quantity.getText());
                    double newItemAmount = newValue * menuPrice;
                    dtm.setValueAt(newValue, isListed, 1);
                    dtm.setValueAt(newItemAmount, isListed, 3);
                }

                Quantity.setText("");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void displayDate() {
        new Timer(0, (ActionEvent e) -> {
            Date d1 = new Date();
            SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");
            DateLabel.setText(s1.format(d1));

        })
                .start();

    }

    private void displayTime() {
        new Timer(0, (ActionEvent e) -> {
            Date d = new Date();
            SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss a");
            TimeLabel.setText(s.format(d));

        })
                .start();

    }

    private void getCustomer() {
        try {
            pst = con.prepareStatement("SELECT MAX(customerID) FROM sales WHERE salesDate='" + DateLabel.getText() + "'");
            rs = pst.executeQuery();
            while (rs.next()) {
                int customerID = rs.getInt(1) + 1;
                CustomerLabel.setText(Integer.toString(customerID));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void showCashier() {
        try {
            pst = con.prepareStatement("SELECT fullname,role FROM AccessLevel WHERE logStatus=1");
            rs = pst.executeQuery();
            if (rs.next()) {
                CashierLabel.setText(rs.getString("fullname"));

                String type = rs.getString("role");
                //JOptionPane.showMessageDialog(null, type);
                if (type.equals("User")) {
                    l.AddButton.setEnabled(false);
                    l.DeleteButton.setEnabled(false);
                }

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void InsertSales() {

        try {
            pst = con.prepareStatement("INSERT INTO sales(salesDate,customerID,Quantity,menuItem,salesCashier,recieptNumber,salesAmount,discount,cash,customerChange,menuID)" + "VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            int rows = OrderListTable.getRowCount();

            for (int row = 0; row < rows; row++) {
                int menu_ID = Integer.parseInt(OrderListTable.getValueAt(row, 0).toString());
                int quantity = Integer.parseInt(OrderListTable.getValueAt(row, 1).toString());
                String orderItem = (String) OrderListTable.getValueAt(row, 2).toString();
                double price = Double.parseDouble(OrderListTable.getValueAt(row, 3).toString());
                double customerDiscount = discountPrice / rows;
                double tot_price = price - customerDiscount;

                pst.setString(1, DateLabel.getText());
                pst.setInt(2, Integer.parseInt(CustomerLabel.getText()));
                pst.setInt(3, quantity);
                pst.setString(4, orderItem);
                pst.setString(5, CashierLabel.getText());
                pst.setString(6, recieptNumber);
                pst.setDouble(7, tot_price);
                pst.setDouble(8, customerDiscount);
                pst.setDouble(9, Double.parseDouble(Cash.getText()));
                pst.setDouble(10, Double.parseDouble(ChangeText.getText()));
                pst.setDouble(11, menu_ID);
                pst.addBatch();
            }
            pst.executeBatch();
            //getCustomer();
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void InsertCreditSales() {

        try {
            pst = con.prepareStatement("INSERT INTO credit_sales(salesDate,customerID,Quantity,menuItem,salesCashier,recieptNumber,salesAmount,discount,menuID)" + "VALUES(?,?,?,?,?,?,?,?,?)");
            int rows = OrderListTable.getRowCount();

            for (int row = 0; row < rows; row++) {
                int menu_ID = Integer.parseInt(OrderListTable.getValueAt(row, 0).toString());
                int quantity = Integer.parseInt(OrderListTable.getValueAt(row, 1).toString());
                String orderItem = (String) OrderListTable.getValueAt(row, 2).toString();
                double price = Double.parseDouble(OrderListTable.getValueAt(row, 3).toString());
                double customerDiscount = discountPrice / rows;
                double tot_price = price - customerDiscount;

                pst.setString(1, DateLabel.getText());
                pst.setInt(2, creditor_id);
                pst.setInt(3, quantity);
                pst.setString(4, orderItem);
                pst.setString(5, CashierLabel.getText());
                pst.setString(6, recieptNumber);
                pst.setDouble(7, tot_price);
                pst.setDouble(8, customerDiscount);
                //pst.setDouble(9, Double.parseDouble(Cash.getText()));
                //pst.setDouble(10, Double.parseDouble(ChangeText.getText()));
                pst.setDouble(9, menu_ID);
                pst.addBatch();
            }
            pst.executeBatch();
            //getCustomer();
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void newCustomer() {

        Quantity.setEnabled(true);
        Quantity.setText("");
        TotalAmount.setText("0.00");
        Discount.setText("0");
        Cash.setText("");
        lm.removeAllElements();
        OrderItem.setText("Order Code");
        OrderDesc.setText("Controller is in Cash Mode");
        ProductEntry.setText("");
        trans_type = "Cash";
        ScannedCode.setText("");
        ScannedCode.requestFocus();
        DefaultTableModel dtm1 = (DefaultTableModel) SearchProductResult.getModel();
        DefaultTableModel dtm2 = (DefaultTableModel) OrderListTable.getModel();
        dtm1.setRowCount(0);
        dtm2.setRowCount(0);

    }

    private void CreateVisualReciept() {
        lm.addElement("   " + storeName);
        lm.addElement("" + storeAddress);
        lm.addElement("  " + ContactNumber + "|" + TIN);
        lm.addElement("====================================");
        lm.addElement("ITEM          QTY          AMOUNT");
        int rows = OrderListTable.getRowCount();
        for (int row = 0; row < rows; row++) {
            int quantity = Integer.parseInt(OrderListTable.getValueAt(row, 1).toString());
            String orderItem = (String) OrderListTable.getValueAt(row, 2).toString();
            double price = Double.parseDouble(OrderListTable.getValueAt(row, 3).toString());
            int lenghtofItem = orderItem.length();
            String priceString = Double.toString(price);

            lm.addElement("" + orderItem);
            lm.addElement("                 " + quantity + "         " + priceString + "0");

        }
        lm.addElement("====================================");
        lm.addElement("Total:       " + TotalAmount.getText());
        lm.addElement("Discount:    " + discountPrice);
        lm.addElement("Cash:        " + Cash.getText());
        lm.addElement("Change:      " + ChangeText.getText());
        lm.addElement("====================================");
        lm.addElement("CUSTOMER: 00000000" + CustomerLabel.getText());
        lm.addElement("Date: " + DateLabel.getText());
        lm.addElement("Time: " + TimeLabel.getText());
        lm.addElement("Cashier: " + CashierLabel.getText());
        lm.addElement("Invoice: " + DateLabel.getText() + "000000" + CustomerLabel.getText());
        lm.addElement("This will not serve as your O.R.");
        lm.addElement("====================================");
        lm.addElement("We love to hear a feedback from you");
        lm.addElement("Email us @" + storeEmail);
        lm.addElement("Text only @" + ContactNumber);
        lm.addElement("====================================");
        lm.addElement("     Polaris Point of Sale System    ");
        lm.addElement("        Developed by MPOS          ");
        lm.addElement("       All rights reserved         ");
        lm.addElement("====================================");
        //RecieptView.setModel(lm);

    }

    private void ReViewVisualReciept() {

        lm.addElement("   " + storeName);
        lm.addElement("" + storeAddress);
        lm.addElement("  " + ContactNumber + "|" + TIN);
        lm.addElement("====================================");
        lm.addElement("ITEM          QTY          AMOUNT");
        int rows = OrderListTable.getRowCount();
        for (int row = 0; row < rows; row++) {
            int quantity = Integer.parseInt(OrderListTable.getValueAt(row, 0).toString());
            String orderItem = (String) OrderListTable.getValueAt(row, 1).toString();
            double price = Double.parseDouble(OrderListTable.getValueAt(row, 3).toString());
            int lenghtofItem = orderItem.length();
            String priceString = Double.toString(price);

            lm.addElement("" + orderItem);
            lm.addElement("                 " + quantity + "         " + priceString + "0");

        }
        lm.addElement("====================================");
        lm.addElement("Total:       " + prevSale);
        lm.addElement("Discount:    " + prevDisc);
        lm.addElement("Cash:        " + prevCash);
        lm.addElement("Change:      " + prevChange);
        lm.addElement("====================================");
        lm.addElement("CUSTOMER: 00000000" + prevCustID);
        lm.addElement("Date: " + prevSalesDate);
        lm.addElement("Time: " + prevTime);
        lm.addElement("Cashier: " + prevCashier);
        lm.addElement("Invoice: " + recentSalesTable.getModel().getValueAt(recentSalesTable.getSelectedRow(), 0).toString());
        lm.addElement("This will not serve as your O.R.");
        lm.addElement("====================================");
        lm.addElement("We love to hear a feedback from you");
        lm.addElement("Email us @" + storeEmail);
        lm.addElement("Text only @" + ContactNumber);
        lm.addElement("====================================");
        lm.addElement("     Polaris Point of Sale System    ");
        lm.addElement("        Developed by MPOS          ");
        lm.addElement("       All rights reserved         ");
        lm.addElement("====================================");
        //RecieptView.setModel(lm);

    }

    private void TotalSalesToday() {
        try {
            pst = con.prepareStatement("SELECT sum(salesAmount) FROM sales WHERE salesDate='" + DateLabel.getText() + "'");
            rs = pst.executeQuery();
            if (rs.next()) {
                double salesToday = rs.getDouble(1);
                TotalSalesToday.setText(Double.toString(salesToday) + "0");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void showRecentSales() {
        try {
            pst = con.prepareStatement("SELECT recieptNumber, sum(salesAmount) AS Sales FROM sales WHERE salesDate='" + DateLabel.getText() + "' GROUP BY(customerID) ORDER BY recieptNumber DESC");
            rs = pst.executeQuery();
            DefaultTableModel recentSales = (DefaultTableModel) recentSalesTable.getModel();
            recentSales.setRowCount(0);
            while (rs.next()) {
                Object rSales[] = {rs.getString("recieptNumber"), rs.getDouble("Sales") + "0"};
                recentSales.addRow(rSales);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void viewRecentPurchases() {
        int row = recentSalesTable.getSelectedRow();
        Reciept = recentSalesTable.getModel().getValueAt(row, 0).toString();
        try {
            pst = con.prepareStatement("SELECT * FROM sales WHERE recieptNumber='" + Reciept + "'");
            rs = pst.executeQuery();
            DefaultTableModel purchases = (DefaultTableModel) OrderListTable.getModel();
            purchases.setRowCount(0);
            while (rs.next()) {
                Object purchaseList[] = {rs.getInt("menuID"), rs.getInt("Quantity"), rs.getString("menuItem"), rs.getDouble("salesAmount") + "0"};
                purchases.addRow(purchaseList);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void logOut() {
        try {
            PreparedStatement pst1 = con.prepareStatement("UPDATE accesslevel SET logStatus=0 WHERE logStatus=1");
            int logout = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout", "Warning", JOptionPane.YES_NO_OPTION);
            if (logout == JOptionPane.YES_OPTION) {
                pst1.executeUpdate();
                LoginScreen ls = new LoginScreen();
                ls.setVisible(true);
                dispose();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void getPreviousPurchaseDetails() {
        int row = recentSalesTable.getSelectedRow();
        String orNumber = recentSalesTable.getModel().getValueAt(row, 0).toString();
        try {
            pst = con.prepareStatement("SELECT sum(salesAmount) as sale, ROUND(sum(discount),2) as disc,salesDate,customerID,cash,customerChange,salesCashier,timeGenerated FROM sales WHERE recieptNumber='" + orNumber + "'");
            rs = pst.executeQuery();
            if (rs.next()) {
                prevSale = rs.getDouble("sale");
                prevDisc = rs.getDouble("disc");
                prevCustID = rs.getInt("customerID");
                prevSalesDate = rs.getString("salesDate");
                prevCash = rs.getDouble("cash");
                prevChange = rs.getDouble("customerChange");
                prevCashier = rs.getString("salesCashier");
                prevTime = rs.getString("timeGenerated");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void printOutputReciept() {

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new BillPrintable(), getPageFormat(pj));
        try {
            pj.setPrintService(psrvc);
        } catch (PrinterException ex) {
            Logger.getLogger(LowStocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        pj.setJobName(DateLabel.getText() + "000000" + CustomerLabel.getText());
        try {
            pj.print();

        } catch (PrinterException ex) {
            //JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void ReprintOutputReciept() {
        recieptNumber = recentSalesTable.getModel().getValueAt(recentSalesTable.getSelectedRow(), 0).toString();
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new ReBillPrintable(), getPageFormat(pj));
        try {
            pj.setPrintService(psrvc);
        } catch (PrinterException ex) {
            Logger.getLogger(LowStocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        pj.setJobName("RecieptReprinting");
        pj.setJobName(recentSalesTable.getModel().getValueAt(recentSalesTable.getSelectedRow(), 0).toString());
        try {
            pj.print();

        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void viewRecieptDetails() {
        try {
            DefaultTableModel NewrecentSalesTable = (DefaultTableModel) recentSalesTable.getModel();
            NewrecentSalesTable.setRowCount(0);
            pst = con.prepareStatement("SELECT recieptNumber, sum(salesAmount) AS totalsales FROM sales WHERE recieptNumber='" + SearchOR.getText() + "'");
            rs = pst.executeQuery();

            if (rs.next()) {
                String amount = rs.getString("totalsales");
                Object recentsales[] = {rs.getString("recieptNumber"), amount};
                NewrecentSalesTable.addRow(recentsales);
                SearchOR.setText("");

            } else {
                Object recentsales[] = {"No Reciept Found"};
                NewrecentSalesTable.addRow(recentsales);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void endOfTheDayReport() {
        try {
            pst = con.prepareStatement("SELECT ROUND(sum(salesAmount),2) AS totSales, ROUND(sum(discount),2) AS totDiscount FROM sales WHERE salesDate='" + DateLabel.getText() + "'");
            rs = pst.executeQuery();
            if (rs.next()) {
                double totalSalesEND = rs.getDouble("totSales");
                double totalDiscountEND = rs.getDouble("totDiscount");
                totalSalesFinal = totalSalesEND;
                totalDiscountFinal = totalDiscountEND;

                printEOTDReport();

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void printEOTDReport() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new endBillPrintable(), getPageFormat(pj));
        try {
            pj.setPrintService(psrvc);
        } catch (PrinterException ex) {
            Logger.getLogger(LowStocks.class.getName()).log(Level.SEVERE, null, ex);
        }
        pj.setJobName("EndOfTheDayReport");
        try {
            pj.print();

        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void getStoreDetails() {
        try {
            pst = con.prepareStatement("SELECT * FROM store");
            rs = pst.executeQuery();
            if (rs.next()) {
                ContactNumber = rs.getString("contactNumber");
                TIN = rs.getString("TIN");
                storeName = rs.getString("storename");
                storeEmail = rs.getString("email");
                storeAddress = rs.getString("address");
                aBlob = rs.getBlob("logo");
                if (aBlob.length() == 0) {

                } else {
                    byte[] imageByte = aBlob.getBytes(1, (int) aBlob.length());
                    InputStream is = new ByteArrayInputStream(imageByte);
                    try {
                        background = ImageIO.read(is);
                    } catch (IOException ex) {
                        //Logger.getLogger(POSController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                NameOfStore.setText(storeName + "(Evaluation Copy)");

            } else {
                StoreInfo si = new StoreInfo();
                si.setVisible(true);
                dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void Restriction() {
        try {
            pst = con.prepareStatement("SELECT role FROM AccessLevel WHERE logStatus=1");
            rs = pst.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                switch (role) {
                    case "User":
                        UserManagement.setVisible(false);
                        MManagement.setVisible(false);
                        EmployeeMenu.setVisible(false);
                        ExpenseMenu.setVisible(false);
                        Payroll.setVisible(false);
                        AddStock.setVisible(false);
                        StockManagement.setVisible(false);
                        user_role = "User";
                        jMenuItem11.setVisible(false);
                        break;
                    case "Cashier":
                        UserManagement.setVisible(false);
                        MManagement.setVisible(false);
                        EmployeeMenu.setVisible(false);
                        Payroll.setVisible(false);
                        AddStock.setVisible(false);
                        StockManagement.setVisible(false);
                        jMenuItem11.setVisible(false);
                        user_role = "Cashier";
                        break;
                    default:
                        user_role = "Admin";
                        break;
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void searchProduct() {
        try {
            Connection con1 = DBConnection.DBConnection();
            PreparedStatement pst1 = con1.prepareStatement("SELECT * FROM menus INNER JOIN stocks_inventory ON menus.menuID=stocks_inventory.prod_id WHERE menus.menuDesc LIKE '%" + ProductEntry.getText() + "%' OR menus.menuCode LIKE '%" + ProductEntry.getText() + "%' ORDER BY(menus.menuID) ASC");
            ResultSet rs1 = pst1.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel) SearchProductResult.getModel();
            dtm.setRowCount(0);
            while (rs1.next()) {
                int available_stock;
                int av_stock = rs1.getInt("available_stock");
                if (av_stock < 1) {
                    available_stock = 0;
                } else {
                    available_stock = av_stock;
                }
                Object o[] = {rs1.getInt("menuID"), rs1.getString("menuDesc"), available_stock, rs1.getString("unit"), rs1.getDouble("menuPrice") + "0"};
                dtm.addRow(o);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void searchScannedProduct() {
        try {
            Connection con1 = DBConnection.DBConnection();
            PreparedStatement pst1 = con1.prepareStatement("SELECT * FROM menus INNER JOIN stocks_inventory ON menus.menuID=stocks_inventory.prod_id WHERE menus.menuCode = '" + ScannedCode.getText() + "' ORDER BY(menus.menuID) ASC");
            ResultSet rs1 = pst1.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel) SearchProductResult.getModel();
            dtm.setRowCount(0);
            while (rs1.next()) {
                int available_stock;
                int av_stock = rs1.getInt("available_stock");
                if (av_stock < 1) {
                    available_stock = 0;
                } else {
                    available_stock = av_stock;
                }
                Object o[] = {rs1.getInt("menuID"), rs1.getString("menuDesc"), available_stock, rs1.getString("unit"), rs1.getDouble("menuPrice") + "0"};
                dtm.addRow(o);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void nextCustomer() {

        int a = JOptionPane.showConfirmDialog(null, "Transaction completed successfully, Press Yes for next Customer", "Next Customer? ", JOptionPane.YES_OPTION);
        if (a == JOptionPane.YES_OPTION) {
            newCustomer();
            showRecentSales();
            TotalSalesToday();
            getCustomer();
            DefaultTableModel dtm1 = (DefaultTableModel) SearchProductResult.getModel();
            dtm1.setRowCount(0);
            DefaultTableModel dtm2 = (DefaultTableModel) OrderListTable.getModel();
            dtm2.setRowCount(0);
            trans_type = "Cash";
        }

    }

    private void updateInventory() {
        DefaultTableModel dtm = (DefaultTableModel) OrderListTable.getModel();
        try {
            int numRows = dtm.getRowCount();
            for (int i = 0; i < numRows; i++) {
                int prodID = Integer.parseInt(dtm.getValueAt(i, 0).toString());
                int quantity = Integer.parseInt(dtm.getValueAt(i, 1).toString());
                String sql = "UPDATE stocks_inventory SET available_stock = (available_stock - '" + quantity + "') WHERE prod_id = '" + prodID + "'";
                pst = con.prepareStatement(sql);
                pst.executeUpdate();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void RestoreInventory() {
        try {
            Connection con1 = DBConnection.DBConnection();
            PreparedStatement pst1 = con1.prepareStatement("SELECT available_stock FROM stocks_inventory WHERE prod_id='" + OrderItem.getText() + "' ");
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                int av_stock = rs1.getInt(1);
                int selectedRow = OrderListTable.getSelectedRow();

                int quantity_to_restore = Integer.parseInt(OrderListTable.getModel().getValueAt(selectedRow, 1).toString());

                int rem_stock = av_stock + quantity_to_restore;
                try {
                    PreparedStatement pst2 = con1.prepareStatement("UPDATE stocks_inventory SET available_stock=? WHERE prod_id='" + OrderItem.getText() + "'");
                    pst2.setInt(1, rem_stock);
                    pst2.executeUpdate();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void checkRegistration() {
        try {
            Connection con1 = DBConnection.DBConnection();
            PreparedStatement pst1 = con1.prepareStatement("SELECT storename FROM store");
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                String store_name = rs1.getString("storename");
                try {
                    PreparedStatement pst2 = con1.prepareStatement("SELECT * FROM systemstatus WHERE company_name='" + store_name + "'");
                    ResultSet rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        //check_license_validity();
                    } else {
                        int reg = JOptionPane.showConfirmDialog(null, "Unregistered Software! Please register to continue use of this Point of Sale System! \n Press Yes to Start registration if you have a product Key.", "Register Now?", JOptionPane.YES_NO_OPTION);
                        if (reg == JOptionPane.YES_OPTION) {
                            RegistrationForm rf = new RegistrationForm();
                            rf.set_comp_name(store_name);
                            rf.setVisible(true);
                            dispose();

                        } else {
                            System.exit(0);
                        }
                    }

                } catch (HeadlessException | SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    private void get_scanned_id() {
        try {
            Connection con1 = DBConnection.DBConnection();
            PreparedStatement pst1 = con1.prepareStatement("SELECT * FROM menus INNER JOIN stocks_inventory ON menus.menuID=stocks_inventory.prod_id WHERE menus.menuCode = '" + ScannedCode.getText() + "'");
            ResultSet rs1 = pst1.executeQuery();

            if (rs1.next()) {
                int av_stk = rs1.getInt("available_stock");
                quant = av_stk;
                int prod_id = rs1.getInt("prod_id");

                if (av_stk < 1 || Integer.toString(av_stk).contains("-")) {
                    PreparedStatement pst2 = con1.prepareStatement("UPDATE stocks_inventory SET available_stock = 0 WHERE prod_id = " + prod_id + "");
                    pst2.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Unable to process! No Stocks available!", "Warning", JOptionPane.WARNING_MESSAGE);

                    if (user_role.equals("Admin")) {
                        int update_stk = JOptionPane.showConfirmDialog(null, "Do you want to update Inventory? ", "Hi there!", JOptionPane.YES_NO_OPTION);

                        if (update_stk == JOptionPane.YES_OPTION) {
                            UpdateStocks us = new UpdateStocks();
                            us.setVisible(true);
                            this.setState(1);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please contact System Administrator to update Stocks", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    OrderItem.setText(Integer.toString(rs1.getInt("menuID")));
                    if (Quantity.getText().isEmpty()) {
                        Quantity.setText("1");
                    } else {
                        String input = Quantity.getText();
                        Quantity.setText(input);
                    }

                    Quantity.requestFocus();

                    Timer tm = new Timer(300, (ActionEvent e) -> {
                        auto_compute_scanned();
                    });
                    tm.start();
                    tm.setRepeats(false);

                }

            } else {

                if (user_role.equals("Admin")) {
                    int option = JOptionPane.showConfirmDialog(null, "Unregistered Product! Would you like to add this to your stock? ", "Warning", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        ListOfMenus lmen = new ListOfMenus();
                        lmen.set_code(ScannedCode.getText());
                        lmen.setVisible(true);
                        this.setState(1);

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Unregistered product, Please Contact your System Administrator for stocking", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    private void auto_compute_scanned() {
        if (OrderItem.getText().equals("Order Code")) {
            JOptionPane.showMessageDialog(null, "Please Select Order first!");
            if (Quantity.getText().isEmpty()) {
                Quantity.setText("1");
            } else {
                String input = Quantity.getText();
                Quantity.setText(input);
            }

        } else if (Quantity.getText().equals("0")) {
            JOptionPane.showMessageDialog(null, "Invalid Quantity!");
            if (Quantity.getText().isEmpty()) {
                Quantity.setText("1");
            } else {
                String input = Quantity.getText();
                Quantity.setText(input);
            }
        } else if (quant < Integer.parseInt(Quantity.getText())) {
            JOptionPane.showMessageDialog(null, "Product supply is not enough please check quantity! Remaining stock is " + Integer.toString(quant) + "");
            Quantity.setText(Integer.toString(quant));
            Quantity.requestFocus();
            int itemQuantity = Integer.parseInt(Quantity.getText());
            double itemAmount = itemQuantity * menuPrice;
            itemPrice = itemAmount;
            double amountPayable = Double.parseDouble(TotalAmount.getText());
            double totalPayable = itemAmount + amountPayable;
            TotalAmount.setText(Double.toString(totalPayable) + "0");
            staticTotalAmount = Double.parseDouble(TotalAmount.getText());
            //UpdateInventory();
            listItem();
            ScannedCode.setText("");

            ScannedCode.requestFocus();

        } else {

            int itemQuantity = Integer.parseInt(Quantity.getText());
            double itemAmount = itemQuantity * menuPrice;
            itemPrice = itemAmount;
            double amountPayable = Double.parseDouble(TotalAmount.getText());
            double totalPayable = itemAmount + amountPayable;
            TotalAmount.setText(Double.toString(totalPayable) + "0");
            staticTotalAmount = Double.parseDouble(TotalAmount.getText());
            //UpdateInventory();
            listItem();
            ScannedCode.setText("");
            ScannedCode.requestFocus();

        }
    }

    private void update_batch_inventory() {

        int rows1 = OrderListTable.getRowCount();

        for (int row1 = 0; row1 < rows1; row1++) {
            int menu_ID = Integer.parseInt(OrderListTable.getValueAt(row1, 0).toString());
            int quant_to_remove = Integer.parseInt(OrderListTable.getValueAt(row1, 1).toString());
            try {
                Connection con1 = DBConnection.DBConnection();
                PreparedStatement pst1 = con1.prepareStatement("SELECT available_stock FROM stocks_inventory WHERE prod_id='" + menu_ID + "' ");
                ResultSet rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    int av_stock = rs1.getInt(1);
                    int rem_stock = av_stock - quant_to_remove;
                    try {
                        PreparedStatement pst2 = con1.prepareStatement("UPDATE stocks_inventory SET available_stock=? WHERE prod_id='" + menu_ID + "'");
                        pst2.setInt(1, rem_stock);
                        pst2.executeUpdate();

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void get_creditor_id(int id) {
        creditor_id = id;
        trans_type = "Credit";

    }

    private void auto_load() {
        newCustomer();
        showRecentSales();
        TotalSalesToday();
        getCustomer();
    }

    private void record_sales() {

    }

    private void check_license_validity() {
        try {
            Connection con1 = DBConnection.DBConnection();
            PreparedStatement pst1 = con1.prepareStatement("SELECT systemstatus.reg_key AS key_text, systemstatus.date_installed AS ins_date, reg_keys.reg_key_id As validity_month  FROM systemstatus INNER JOIN reg_keys ON systemstatus.reg_key=reg_keys.reg_key");
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                String date_installed = rs1.getString("ins_date");
                int validity = rs1.getInt("validity_month");
                String date_today = DateLabel.getText();

                String month_installed = date_installed.substring(5, 7);
                String month_today = date_today.substring(5, 7);

                int m_installed = Integer.parseInt(month_installed);
                int m_today = Integer.parseInt(month_today);

                int consumed_month = m_today - m_installed;

                if (consumed_month >= validity) {
                    JOptionPane.showMessageDialog(null, "License Expired!");
                    //Delete
                    try {
                        Connection con2 = DBConnection.DBConnection();
                        PreparedStatement pst2 = con2.prepareStatement("DELETE FROM systemstatus WHERE date_installed='" + date_installed + "'");
                        pst2.executeUpdate();
                        System.exit(0);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void createPieChart() {
        jPanel6.setLayout(new java.awt.BorderLayout());
        JFreeChart chart = createChart(createDataset());
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        ChartPanel CP = new ChartPanel(chart);

        CP.setBackground(Color.WHITE);
        jPanel6.add(CP, BorderLayout.CENTER);
        jPanel6.validate();

    }

    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try {
            String sql = "SELECT MONTHNAME(salesDate) AS Month, SUM(salesAmount) AS monthlySales FROM sales  WHERE YEAR(salesDate) = YEAR(CURRENT_DATE) GROUP BY MONTH(salesDate)";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                dataset.setValue(rs.getString("Month").substring(0, 3), new Double(rs.getString("monthlySales")));
            }
        } catch (SQLException ex) {

        }

        return dataset;
    }

    private static JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Monthly Sales", // chart title 
                dataset, // data    
                true, // include legend   
                true,
                false);

        return chart;
    }

    public int getRowIndexByValue(String targetValue) {
        DefaultTableModel model = (DefaultTableModel) OrderListTable.getModel();
        int rowCount = model.getRowCount();
        int index = -1;
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {

            Object cellValue = model.getValueAt(rowIndex, 0);
            if (cellValue.toString().equals(targetValue)) {
                index = rowIndex;

            }

        }

        // If the target value is not found, return -1.
        return index;
    }

    //Reciept Printing Method
    public PageFormat getPageFormat(PrinterJob pj) {

        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();

        double middleHeight = 8.0;
        double headerHeight = 2.0;
        double footerHeight = 2.0;
        double width = convertCMToPPI(6);      //printer know only point per inch.default value is 72ppi
        double height = convertCMToPPI(headerHeight + middleHeight + footerHeight);
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
                    int yShift = 9;
                    int headerRectHeight = 13;
                    int headerRectHeighta = 40;
                    g2d.setColor(Color.black);
                    g2d.drawImage(background, 44, 1, null);
                    y += 64;

                    g2d.drawString("------------------------------------", 10, y);
                    y += yShift;
                    g2d.setFont(new Font("Monospaced", Font.BOLD, 11));
                    g2d.drawString("" + storeName, 2, y);
                    y += yShift;
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    g2d.drawString("" + storeAddress, 2, y);
                    y += yShift;
                    g2d.drawString("" + ContactNumber + "|" + TIN, 2, y);
                    y += yShift;
                    g2d.drawString("Reg. Code: 201320726541 ", 2, y);
                    y += yShift;
                    g2d.drawString("------------------------------------", 2, y);
                    y += headerRectHeight;
                    g2d.drawString("" + orderMode.toUpperCase(), 2, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("ITEM    QTY    AMOUNT", 2, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += headerRectHeight;
                    int rows = OrderListTable.getRowCount();
                    for (int row = 0; row < rows; row++) {
                        int quantity = Integer.parseInt(OrderListTable.getValueAt(row, 1).toString());
                        String orderItem = (String) OrderListTable.getValueAt(row, 2).toString();
                        double price = Double.parseDouble(OrderListTable.getValueAt(row, 3).toString());
                        int lenghtofItem = orderItem.length();
                        String priceString = Double.toString(price);

                        g2d.drawString("" + orderItem, 2, y);
                        y += yShift + 4;
                        g2d.drawString("     " + quantity + "       " + priceString + "0", 2, y);
                        y += yShift;

                    }

                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.setFont(new Font("Monospaced", Font.BOLD, 11));
                    g2d.drawString("Total:    " + TotalAmount.getText(), 2, y);
                    y += 10;
                    g2d.drawString("Discount: " + discountPrice, 2, y);
                    y += 10;
                    g2d.drawString("Cash:     " + Cash.getText(), 2, y);
                    y += 10;
                    g2d.drawString("Change:   " + ChangeText.getText(), 2, y);
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    double totalSales = Double.parseDouble(TotalAmount.getText());
                    double vat = totalSales * 0.03;
                    double vatSales = totalSales - vat;

//                    g2d.drawString("VAT Sales       "+ vatSales, 8, y);
//                    y += yShift;
//                    g2d.drawString("VAT-Exempt Sale  0.00", 8, y);
//                    y += yShift;
//                    g2d.drawString("VAT Amount      "+ vat, 8, y);
//                    y += yShift;
//                    g2d.drawString("Amount Due      "+ totalSales, 8, y);
//                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("CUSTOMER: 00000000" + CustomerLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("Date: " + DateLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("Time: " + TimeLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("Cashier: " + CashierLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("Invoice: " + recieptNumber, 2, y);
                    y += yShift;
                    g2d.drawString("Customer Name:_______________________", 2, y);
                    y += yShift;
                    g2d.drawString("Customer Add:________________________", 2, y);
                    y += yShift;
                    g2d.drawString("Customer TIN:________________________", 2, y);
                    y += yShift;
                    g2d.drawString("", 2, y);
                    y += yShift;
                    g2d.drawString("This will not serve as your O.R.", 2, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("We love to hear from you", 2, y);
                    y += yShift;
                    g2d.drawString("Email us: " + storeEmail, 2, y);
                    y += yShift;
                    g2d.drawString("Text only @" + ContactNumber, 2, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("Point of Sale System(Evaluation)    ", 2, y);
                    y += yShift;
                    g2d.drawString("All rights reserved         ", 2, y);
                    y += yShift;
                    g2d.drawString("*************************************", 2, y);
                    y += yShift;
                    g2d.drawString("THANKS FOR CHOOSING US ", 2, y);
                    y += yShift;
                    g2d.drawString("*************************************", 2, y);
                    y += yShift;
//            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift; 
                } catch (NumberFormatException r) {
                    //JOptionPane.showMessageDialog(null, r);
                }
                result = PAGE_EXISTS;
            }
            return result;
        }
    }

    //Reciept Reprinting Method
    public PageFormat RegetPageFormat(PrinterJob repj) {

        PageFormat repf = repj.defaultPage();
        Paper repaper = repf.getPaper();

        double middleHeight = 8.0;
        double headerHeight = 2.0;
        double footerHeight = 2.0;
        double width = convertCMToPPI(8);      //printer know only point per inch.default value is 72ppi
        double height = convertCMToPPI(headerHeight + middleHeight + footerHeight);
        repaper.setSize(width, repf.getHeight());
        repaper.setImageableArea(
                0,
                10,
                width,
                height - convertCMToPPI(1)
        );   //define boarder size    after that print area width is about 180 points

        repf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
        repf.setPaper(repaper);

        return repf;
    }

    protected static double ReconvertCMToPPI(double cm) {
        return RetoPPI(cm * 0.393600787);
    }

    protected static double RetoPPI(double inch) {
        return inch * 72d;
    }

    public class ReBillPrintable implements Printable {

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
                    int yShift = 9;
                    int headerRectHeight = 13;
                    int headerRectHeighta = 40;
                    g2d.setColor(Color.black);
                    g2d.drawImage(background, 44, 1, null);
                    y += 52;
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));

                    g2d.drawString("------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("" + storeName, 2, y);
                    y += yShift;
                    g2d.drawString("" + storeAddress, 2, y);
                    y += yShift;
                    g2d.drawString("" + ContactNumber + "|" + TIN, 2, y);
                    y += yShift;
                    g2d.drawString("Reg. Code: 201320726541 ", 2, y);
                    y += yShift;
                    g2d.drawString("------------------------------------", 2, y);
                    y += headerRectHeight;
                    g2d.drawString("" + orderMode.toUpperCase(), 2, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("ITEM    QTY     AMOUNT", 2, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += headerRectHeight;
                    int rows = OrderListTable.getRowCount();
                    for (int row = 0; row < rows; row++) {
                        int quantity = Integer.parseInt(OrderListTable.getValueAt(row, 1).toString());
                        String orderItem = (String) OrderListTable.getValueAt(row, 2).toString();
                        double price = Double.parseDouble(OrderListTable.getValueAt(row, 3).toString());
                        int lenghtofItem = orderItem.length();
                        String priceString = Double.toString(price);

                        g2d.drawString("" + orderItem, 2, y);
                        y += yShift + 4;
                        g2d.drawString("       " + quantity + "        " + priceString + "0", 2, y);
                        y += yShift;

                    }
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.setFont(new Font("Monospaced", Font.BOLD, 11));
                    g2d.drawString("Total:    " + prevSale, 2, y);
                    y += 10;
                    g2d.drawString("Discount: " + prevDisc, 2, y);
                    y += 10;
                    g2d.drawString("Cash:     " + prevCash, 2, y);
                    y += 10;
                    g2d.drawString("Change:   " + prevChange, 2, y);
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    double totalSales = prevSale;
                    double vat = totalSales * 0.03;
                    double vatSales = totalSales - vat;

//                    g2d.drawString("VAT Sales             "+ vatSales, 2, y);
//                    y += yShift;
//                    g2d.drawString("VAT-Exempt Sale        0.00", 2, y);
//                    y += yShift;
//                    g2d.drawString("VAT Amount            "+ vat, 2, y);
//                    y += yShift;
//                    g2d.drawString("Amount Due            "+ totalSales, 2, y);
//                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("CUSTOMER: 00000000" + CustomerLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("Date: " + DateLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("Time: " + TimeLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("Cashier: " + CashierLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("Invoice: " + recieptNumber, 2, y);
                    y += yShift;
                    g2d.drawString("Customer Name:_______________________", 2, y);
                    y += yShift;
                    g2d.drawString("Customer Add:________________________", 2, y);
                    y += yShift;
                    g2d.drawString("Customer TIN:________________________", 2, y);
                    y += yShift;
                    g2d.drawString("", 2, y);
                    y += yShift;
                    g2d.drawString("This will not serve as your O.R.", 2, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("We love to hear from you", 2, y);
                    y += yShift;
                    g2d.drawString("Email us: " + storeEmail, 2, y);
                    y += yShift;
                    g2d.drawString("Text only @" + ContactNumber, 2, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("Point of Sale System(Evaluation)    ", 2, y);
                    y += yShift;
                    g2d.drawString("All rights reserved         ", 2, y);
                    y += yShift;
                    g2d.drawString("*************************************", 2, y);
                    y += yShift;
                    g2d.drawString("THANKS FOR CHOOSING US ", 2, y);
                    y += yShift;
                    g2d.drawString("*************************************", 2, y);
                    y += yShift;
//            g2d.setFont(new Font("Monospaced",Font.BOLD,10));
//            g2d.drawString("Customer Shopping Invoice", 30,y);y+=yShift; 
                } catch (NumberFormatException r) {
                    //JOptionPane.showMessageDialog(null, r);
                }
                result = PAGE_EXISTS;
            }
            return result;
        }
    }

    //End-Of-The_Day Reprinting Method
    double totalSalesFinal;
    double totalDiscountFinal;

    public PageFormat endgetPageFormat(PrinterJob repj) {

        PageFormat repf = repj.defaultPage();
        Paper repaper = repf.getPaper();

        double middleHeight = 8.0;
        double headerHeight = 2.0;
        double footerHeight = 2.0;
        double width = convertCMToPPI(8);      //printer know only point per inch.default value is 72ppi
        double height = convertCMToPPI(headerHeight + middleHeight + footerHeight);
        repaper.setSize(width, height);
        repaper.setImageableArea(
                0,
                10,
                width,
                height - convertCMToPPI(1)
        );   //define boarder size    after that print area width is about 180 points

        repf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
        repf.setPaper(repaper);

        return repf;
    }

    protected static double endconvertCMToPPI(double cm) {
        return endtoPPI(cm * 0.393600787);
    }

    protected static double endtoPPI(double inch) {
        return inch * 72d;
    }

    public class endBillPrintable implements Printable {

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
                    int yShift = 9;
                    int headerRectHeight = 13;
                    int headerRectHeighta = 40;

                    g2d.setColor(Color.black);
                    g2d.drawImage(background, 44, 1, null);
                    y += 64;
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    g2d.drawString("------------------------------------", 10, y);
                    y += yShift;
                    g2d.drawString("" + storeName, 2, y);
                    y += yShift;
                    g2d.drawString("" + storeAddress, 2, y);
                    y += yShift;
                    g2d.drawString("" + ContactNumber + "|" + TIN, 2, y);
                    y += yShift;
                    g2d.drawString("Machine Reg. Code: 201320726541 ", 2, y);
                    y += yShift;
                    g2d.drawString("------------------------------------", 2, y);
                    y += headerRectHeight;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("End-of-the-Day Report", 2, y);
                    y += yShift;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += headerRectHeight;
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.setFont(new Font("Monospaced", Font.BOLD, 9));
                    g2d.drawString("Total Sales:   " + totalSalesFinal, 2, y);
                    y += yShift;
                    g2d.drawString("Total Discount " + totalDiscountFinal, 2, y);
                    y += yShift;
                    g2d.drawString("Date: " + DateLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("Time: " + TimeLabel.getText(), 2, y);
                    y += yShift;
                    g2d.drawString("Cashier: " + CashierLabel.getText(), 2, y);
                    y += yShift;
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    y += yShift;
                    g2d.drawString("INVOICES", 2, y);
                    y += yShift;
                    g2d.drawString("#                AMOUNT", 2, y);
                    y += yShift;
                    g2d.setFont(new Font("Monospaced", Font.BOLD, 9));
                    int rows = recentSalesTable.getRowCount();
                    for (int row = 0; row < rows; row++) {
                        String invoice = recentSalesTable.getValueAt(row, 0).toString();
                        String amount = (String) recentSalesTable.getValueAt(row, 1).toString();

                        g2d.drawString("" + invoice + " - " + amount + "0", 2, y);
                        y += yShift;

                    }
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 9));
                    g2d.drawString("-------------------------------------", 2, y);
                    y += yShift;
                    g2d.drawString("" + storeName + "   ", 2, y);
                    y += yShift;
                    g2d.drawString("All rights reserved         ", 2, y);
                    y += yShift;
                    g2d.drawString("*************************************", 2, y);
                    y += yShift;
                    g2d.drawString("MPS Development         ", 2, y);
                    y += yShift;
                    g2d.drawString("*************************************", 2, y);
                    y += yShift;
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

        jMenu3 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        RightPanel = new javax.swing.JPanel();
        CashTransactions = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Cash = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        ChangeText = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Discount = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        Quantity = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        OrderItem = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        ButtonPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        SearchProductResult = new javax.swing.JTable();
        ProductEntry = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        ScannedCode = new javax.swing.JTextField();
        SwitchPanel = new javax.swing.JPanel();
        OrderList = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        OrderListTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        RecentSales = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        recentSalesTable = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        SearchOR = new javax.swing.JTextField();
        VoidBtn = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        TotalSalesToday = new javax.swing.JLabel();
        PCHost1 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        ActiveUserName8 = new javax.swing.JLabel();
        NameOfStore = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        newCust = new javax.swing.JMenuItem();
        MManagement = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        EndOfTheDay = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        Logout = new javax.swing.JMenuItem();
        EmployeeMenu = new javax.swing.JMenu();
        UserManagement = new javax.swing.JMenuItem();
        ExpenseMenu = new javax.swing.JMenu();
        Expense = new javax.swing.JMenuItem();
        Payroll = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        AddStock = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        StockManagement = new javax.swing.JMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        RecieveCash = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();

        jMenu3.setText("jMenu3");

        jMenu2.setText("jMenu2");

        jMenu5.setText("File");
        jMenuBar2.add(jMenu5);

        jMenu6.setText("Edit");
        jMenuBar2.add(jMenu6);

        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1366, 760));
        setMinimumSize(new java.awt.Dimension(1366, 760));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1366, 760));
        setResizable(false);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel1.setMaximumSize(new java.awt.Dimension(1366, 730));
        jPanel1.setMinimumSize(new java.awt.Dimension(1366, 730));
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 730));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        RightPanel.setBackground(new java.awt.Color(0, 255, 255));
        RightPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CashTransactions.setBackground(new java.awt.Color(255, 255, 255));
        CashTransactions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Total");
        CashTransactions.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, 44));

        TotalAmount.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        TotalAmount.setForeground(new java.awt.Color(255, 51, 0));
        TotalAmount.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TotalAmount.setText("0.00");
        TotalAmount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        CashTransactions.add(TotalAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 180, 40));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel4.setText("Cash");
        CashTransactions.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, 50));

        Cash.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        Cash.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Cash.setText("0");
        Cash.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Cash.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CashFocusGained(evt);
            }
        });
        Cash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CashActionPerformed(evt);
            }
        });
        Cash.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                CashKeyTyped(evt);
            }
        });
        CashTransactions.add(Cash, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 180, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel5.setText("Change");
        CashTransactions.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, 40));

        ChangeText.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        ChangeText.setForeground(new java.awt.Color(255, 51, 0));
        ChangeText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChangeText.setText("0.00");
        ChangeText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        CashTransactions.add(ChangeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 270, 180, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel7.setText("Disc. %");
        CashTransactions.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, 50));

        Discount.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        Discount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Discount.setText("0");
        Discount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Discount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DiscountActionPerformed(evt);
            }
        });
        CashTransactions.add(Discount, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 180, -1));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel8.setText("QTY");
        CashTransactions.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, 50));

        Quantity.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        Quantity.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        Quantity.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Quantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                QuantityFocusGained(evt);
            }
        });
        Quantity.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                QuantityInputMethodTextChanged(evt);
            }
        });
        Quantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuantityActionPerformed(evt);
            }
        });
        Quantity.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                QuantityPropertyChange(evt);
            }
        });
        Quantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                QuantityKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                QuantityKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                QuantityKeyTyped(evt);
            }
        });
        CashTransactions.add(Quantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 180, -1));
        CashTransactions.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 310, 10));

        OrderItem.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        OrderItem.setForeground(new java.awt.Color(255, 51, 0));
        OrderItem.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        OrderItem.setText("Order Code");
        OrderItem.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                OrderItemPropertyChange(evt);
            }
        });
        CashTransactions.add(OrderItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 160, 30));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Mobile Order_38px.png"))); // NOI18N
        CashTransactions.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 10, -1, 30));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("(F8)");
        CashTransactions.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, -1, 30));

        RightPanel.add(CashTransactions, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 300, 330));

        jPanel1.add(RightPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 10, 320, 350));

        ButtonPanel.setBackground(new java.awt.Color(0, 255, 255));
        ButtonPanel.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                ButtonPanelInputMethodTextChanged(evt);
            }
        });
        ButtonPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SearchProductResult.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        SearchProductResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "^", "Product Description", "Available Stocks", "Unit", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        SearchProductResult.setRowHeight(28);
        SearchProductResult.setSelectionBackground(new java.awt.Color(0, 204, 51));
        SearchProductResult.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SearchProductResultMouseClicked(evt);
            }
        });
        SearchProductResult.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SearchProductResultKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(SearchProductResult);

        ButtonPanel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 980, 250));

        ProductEntry.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ProductEntry.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ProductEntryKeyTyped(evt);
            }
        });
        ButtonPanel.add(ProductEntry, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 360, 50));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Search_48px.png"))); // NOI18N
        ButtonPanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        ScannedCode.setBackground(new java.awt.Color(0, 255, 255));
        ScannedCode.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        ScannedCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ScannedCode.setBorder(null);
        ScannedCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ScannedCodeActionPerformed(evt);
            }
        });
        ScannedCode.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ScannedCodePropertyChange(evt);
            }
        });
        ScannedCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ScannedCodeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ScannedCodeKeyReleased(evt);
            }
        });
        ButtonPanel.add(ScannedCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 370, 50));

        jPanel1.add(ButtonPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1000, 350));

        SwitchPanel.setBackground(new java.awt.Color(0, 255, 255));

        javax.swing.GroupLayout SwitchPanelLayout = new javax.swing.GroupLayout(SwitchPanel);
        SwitchPanel.setLayout(SwitchPanelLayout);
        SwitchPanelLayout.setHorizontalGroup(
            SwitchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        SwitchPanelLayout.setVerticalGroup(
            SwitchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        jPanel1.add(SwitchPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1000, 350));

        OrderList.setBackground(new java.awt.Color(0, 255, 255));
        OrderList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        OrderListTable.setBackground(new java.awt.Color(204, 204, 204));
        OrderListTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        OrderListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order Code", "QTY", "ITEM", "AMOUNT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        OrderListTable.setGridColor(new java.awt.Color(255, 255, 255));
        OrderListTable.setRowHeight(22);
        OrderListTable.setSelectionBackground(new java.awt.Color(0, 204, 0));
        OrderListTable.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                OrderListTableCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        OrderListTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OrderListTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(OrderListTable);

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 610, 190));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Order List");
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, 22));

        OrderList.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 610, 250));

        jPanel1.add(OrderList, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 630, 270));

        RecentSales.setBackground(new java.awt.Color(0, 255, 255));
        RecentSales.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        recentSalesTable.setBackground(new java.awt.Color(204, 204, 204));
        recentSalesTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        recentSalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice", "Sales"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        recentSalesTable.setGridColor(new java.awt.Color(255, 255, 255));
        recentSalesTable.setSelectionBackground(new java.awt.Color(0, 204, 0));
        recentSalesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recentSalesTableMouseClicked(evt);
            }
        });
        recentSalesTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                recentSalesTableKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                recentSalesTableKeyTyped(evt);
            }
        });
        jScrollPane4.setViewportView(recentSalesTable);

        jPanel11.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 330, 210));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Recent Sales");
        jPanel11.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 6, -1, 22));

        SearchOR.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        SearchOR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchORActionPerformed(evt);
            }
        });
        jPanel11.add(SearchOR, new org.netbeans.lib.awtextra.AbsoluteConstraints(115, 6, 140, -1));

        VoidBtn.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        VoidBtn.setText("VOID");
        VoidBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VoidBtnActionPerformed(evt);
            }
        });
        jPanel11.add(VoidBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 3, 60, 30));

        RecentSales.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 330, -1));

        jPanel1.add(RecentSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 410, 350, 270));

        OrderDesc.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        OrderDesc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        OrderDesc.setText("Description Here");
        OrderDesc.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                OrderDescPropertyChange(evt);
            }
        });
        jPanel1.add(OrderDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, 1000, 50));

        jPanel6.setBackground(new java.awt.Color(0, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 410, 330, 270));

        jPanel7.setBackground(new java.awt.Color(235, 235, 235));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CashierLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CashierLabel.setText("Active User Here");
        jPanel7.add(CashierLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 230, 20));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 990, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel7.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-30, 40, 990, 30));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 690, 240, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 204, 0));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Workspace_24px.png"))); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 690, -1, 30));

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Today.png"))); // NOI18N
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 690, -1, 30));

        jPanel13.setBackground(new java.awt.Color(235, 235, 235));
        jPanel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DateLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        DateLabel.setText("12/12/19");
        jPanel13.add(DateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 90, 20));
        DateLabel.getAccessibleContext().setAccessibleName("YYYY-MM-dd");

        jPanel1.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 690, 100, 30));

        jPanel14.setBackground(new java.awt.Color(235, 235, 235));
        jPanel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TimeLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        TimeLabel.setText("12:23 PM");
        jPanel14.add(TimeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 90, 20));

        jPanel1.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 690, 100, 30));

        jPanel3.setBackground(new java.awt.Color(235, 235, 235));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TotalSalesToday.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        TotalSalesToday.setText("Today's Sales");
        jPanel3.add(TotalSalesToday, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, 120, 30));

        PCHost1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        PCHost1.setText("Today's Sales:");
        jPanel3.add(PCHost1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 90, 30));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 690, 220, 30));

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Client.png"))); // NOI18N
        jLabel21.setText("Cust");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 690, -1, 30));

        jPanel20.setBackground(new java.awt.Color(235, 235, 235));
        jPanel20.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel20.setForeground(new java.awt.Color(255, 255, 255));
        jPanel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CustomerLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        CustomerLabel.setText("0");
        CustomerLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        CustomerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CustomerLabelMouseClicked(evt);
            }
        });
        CustomerLabel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                CustomerLabelPropertyChange(evt);
            }
        });
        jPanel20.add(CustomerLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 60, 20));

        jPanel1.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 690, 80, 30));

        jPanel16.setBackground(new java.awt.Color(235, 235, 235));
        jPanel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel17.setBackground(new java.awt.Color(235, 235, 235));
        jPanel17.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ActiveUserName8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jPanel17.add(ActiveUserName8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 60, 20));

        jPanel16.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 680, 80, 30));

        NameOfStore.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        NameOfStore.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NameOfStore.setText("Name of Store goes in here-Point of Sale System");
        jPanel16.add(NameOfStore, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 380, 30));

        jPanel1.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 690, 410, 30));

        getContentPane().add(jPanel1, new java.awt.GridBagConstraints());

        jMenuBar1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jMenuBar1.setMaximumSize(new java.awt.Dimension(300, 32769));
        jMenuBar1.setPreferredSize(new java.awt.Dimension(300, 30));

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Automatic_15px.png"))); // NOI18N
        jMenu1.setText("Options");
        jMenu1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        newCust.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newCust.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Add User Male_15px.png"))); // NOI18N
        newCust.setText("New Customer");
        newCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCustActionPerformed(evt);
            }
        });
        jMenu1.add(newCust);

        MManagement.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        MManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/merchant.png"))); // NOI18N
        MManagement.setText("Merchant Management");
        MManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MManagementActionPerformed(evt);
            }
        });
        jMenu1.add(MManagement);

        jMenuItem1.setText("Select Default Printer");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        EndOfTheDay.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        EndOfTheDay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Send to Printer_15px.png"))); // NOI18N
        EndOfTheDay.setText("End of the Day");
        EndOfTheDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EndOfTheDayActionPerformed(evt);
            }
        });
        jMenu1.add(EndOfTheDay);

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/C Drive 2.png"))); // NOI18N
        jMenuItem7.setText("Backup Data to Local Drive");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Sync_15px.png"))); // NOI18N
        jMenuItem4.setText("Upload Data to Cloud");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem11.setText("Update Store");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem11);

        Logout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        Logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Shutdown_15px.png"))); // NOI18N
        Logout.setText("Logout");
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });
        jMenu1.add(Logout);

        jMenuBar1.add(jMenu1);

        EmployeeMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Management_15px.png"))); // NOI18N
        EmployeeMenu.setText("Users");
        EmployeeMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        EmployeeMenu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        UserManagement.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        UserManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Circled User Female Skin Type 1 2_15px.png"))); // NOI18N
        UserManagement.setText("User Management");
        UserManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UserManagementActionPerformed(evt);
            }
        });
        EmployeeMenu.add(UserManagement);

        jMenuBar1.add(EmployeeMenu);

        ExpenseMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Receipt_15px.png"))); // NOI18N
        ExpenseMenu.setText("Record");
        ExpenseMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ExpenseMenu.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        Expense.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        Expense.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Open Box_15px.png"))); // NOI18N
        Expense.setText("Purchases");
        Expense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExpenseActionPerformed(evt);
            }
        });
        ExpenseMenu.add(Expense);

        Payroll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        Payroll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Payroll_15px.png"))); // NOI18N
        Payroll.setText("Payroll");
        Payroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PayrollActionPerformed(evt);
            }
        });
        ExpenseMenu.add(Payroll);

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        jMenuItem12.setText("Reprint Reciept");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        ExpenseMenu.add(jMenuItem12);

        jMenuBar1.add(ExpenseMenu);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Combo Chart_15px.png"))); // NOI18N
        jMenu4.setText("Reports");
        jMenu4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Eye_15px.png"))); // NOI18N
        jMenuItem2.setText("Quick View Report");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Open in Browser_15px.png"))); // NOI18N
        jMenuItem5.setText("Sales Viewer");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Refund_15px.png"))); // NOI18N
        jMenuItem8.setText("Billing/Credits");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Decrease_15px.png"))); // NOI18N
        jMenuItem10.setText("Profit/Loss Per Item");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuBar1.add(jMenu4);

        jMenu7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Trolley_15px.png"))); // NOI18N
        jMenu7.setText("Stocks");
        jMenu7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        AddStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Add Tag_15px.png"))); // NOI18N
        AddStock.setText("List of Stocks");
        AddStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddStockActionPerformed(evt);
            }
        });
        jMenu7.add(AddStock);

        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Microsoft Excel.png"))); // NOI18N
        jMenuItem13.setText("Import Stocks and Inventory");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem13);

        StockManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Product_15px.png"))); // NOI18N
        StockManagement.setText("Stock Inventory");
        StockManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StockManagementActionPerformed(evt);
            }
        });
        jMenu7.add(StockManagement);

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Low Stocks Viewer");
        jCheckBoxMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Sort By Follow Up Date_15px.png"))); // NOI18N
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem1);

        jMenuBar1.add(jMenu7);

        jMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Customer Skin Type 7_15px.png"))); // NOI18N
        jMenu8.setText("Customers");
        jMenu8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Registration_15px.png"))); // NOI18N
        jMenuItem6.setText("Register Customer");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem6);

        RecieveCash.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        RecieveCash.setText("Recieve Cash");
        RecieveCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RecieveCashActionPerformed(evt);
            }
        });
        jMenu8.add(RecieveCash);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Buy_15px.png"))); // NOI18N
        jMenuItem9.setText("New Purchase Order");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem9);

        jMenuBar1.add(jMenu8);

        jMenu9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Info_15px.png"))); // NOI18N
        jMenu9.setText("About");
        jMenu9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jMenu9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jMenu9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu9MouseClicked(evt);
            }
        });
        jMenu9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu9ActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenu9);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        getStoreDetails();
        displayTime();
        displayDate();
        showCashier();
        VoidBtn.setVisible(false);
        //RecieveCash.setVisible(false);
        Timer tm = new Timer(300, (ActionEvent e) -> {
            auto_load();
        });
        tm.start();
        tm.setRepeats(false);
        createPieChart();
    }//GEN-LAST:event_formWindowOpened

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped

    }//GEN-LAST:event_formKeyTyped
    String staticMenuCode;
    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
//        getCustomer();
//        newCustomer();
//        showRecentSales();
//        TotalSalesToday();
    }//GEN-LAST:event_formWindowGainedFocus

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        logOut();
    }//GEN-LAST:event_LogoutActionPerformed
    ListOfMenus l = new ListOfMenus();
    JPanel menuList = l.MenuList;
    private void ExpenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExpenseActionPerformed
        ExpenseFrame ef = new ExpenseFrame();
        ef.getCashier(CashierLabel.getText());
        ef.setVisible(true);

    }//GEN-LAST:event_ExpenseActionPerformed

    private void MManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MManagementActionPerformed
        MerchantManagement mm = new MerchantManagement();
        mm.setVisible(true);
    }//GEN-LAST:event_MManagementActionPerformed

    private void newCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCustActionPerformed
        newCustomer();
        showRecentSales();
        TotalSalesToday();
        getCustomer();
    }//GEN-LAST:event_newCustActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void UserManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UserManagementActionPerformed
        UserManagement um = new UserManagement();
        um.setVisible(true);
    }//GEN-LAST:event_UserManagementActionPerformed

    private void EndOfTheDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EndOfTheDayActionPerformed
        int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to end today's Transaction?", "Warning", JOptionPane.YES_NO_OPTION);
        if (a == JOptionPane.YES_OPTION) {
            endOfTheDayReport();
        }
    }//GEN-LAST:event_EndOfTheDayActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed

    }//GEN-LAST:event_jMenu1ActionPerformed

    private void PayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PayrollActionPerformed
        PayrollScreen ps = new PayrollScreen();
        ps.setVisible(true);
    }//GEN-LAST:event_PayrollActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        QuickView qv = new QuickView();
        qv.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

        Restriction();
        checkRegistration();
        try {
            getDefaultPrinter();
        } catch (Exception ex) {
            Logger.getLogger(POSController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ScannedCode.requestFocus();


    }//GEN-LAST:event_formWindowActivated

    private void StockManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StockManagementActionPerformed
        UpdateStocks us = new UpdateStocks();
        us.setVisible(true);
    }//GEN-LAST:event_StockManagementActionPerformed

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
        LowStocks ls = new LowStocks();
        ls.setVisible(true);
    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        SalesViewer sv = new SalesViewer();
        sv.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        NewCustomer nc = new NewCustomer();
        nc.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        SelectCreditor sc = new SelectCreditor();
        sc.setVisible(true);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        AccountsRecievable ar = new AccountsRecievable();
        ar.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenu9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu9ActionPerformed

    }//GEN-LAST:event_jMenu9ActionPerformed

    private void jMenu9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu9MouseClicked
        About a = new About();
        a.setVisible(true);
    }//GEN-LAST:event_jMenu9MouseClicked

    private void AddStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddStockActionPerformed
        ListOfMenus lm = new ListOfMenus();
        lm.setVisible(true);
    }//GEN-LAST:event_AddStockActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        ProfitPerItem ppi = new ProfitPerItem();
        ppi.setVisible(true);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void CashFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CashFocusGained
        Quantity.setEditable(false);
    }//GEN-LAST:event_CashFocusGained
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private void CashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CashActionPerformed
        try {

            java.util.Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = Year.now().getValue();
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

            recieptNumber = Integer.toString(year) + Integer.toString(month) + Integer.toString(day) + Integer.toString(hour) + Integer.toString(minute) + Integer.toString(second);

            double AmountPayable = Double.parseDouble(TotalAmount.getText());
            double CashTendered = Double.parseDouble(Cash.getText());

            if (AmountPayable > CashTendered) {

                JOptionPane.showMessageDialog(null, "Invalid Payment Amount");
                Cash.requestFocus();
            } else if (Cash.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter payment amount!");
            } else {

                if ("Credit".equals(trans_type)) {
                    orderMode = "Credit Purchase";
                    double Change = round((CashTendered - AmountPayable), 2);
                    ChangeText.setText(Double.toString(Change));

                    //CreateVisualReciept();
                    int printReciept = JOptionPane.showConfirmDialog(rootPane, "Do you want to print invoice?", "Print Invoice", JOptionPane.YES_NO_OPTION);
                    if (printReciept == JOptionPane.YES_OPTION) {
                        printOutputReciept();
                        //printOutputReciept();
                    } else {

                    }

                    InsertCreditSales();
                    updateInventory();
                    createDataset();
                    createPieChart();
                    nextCustomer();
                    jPanel6.validate();

                } else {
                    orderMode = "Cash Purchase";
                    double Change = round((CashTendered - AmountPayable), 2);
                    ChangeText.setText(Double.toString(Change));
                    int printReciept = JOptionPane.showConfirmDialog(rootPane, "Do you want to print invoice?", "Print Invoice", JOptionPane.YES_NO_OPTION);
                    if (printReciept == JOptionPane.YES_OPTION) {
                        printOutputReciept();
                        //printOutputReciept();
                    } else {

                    }
                    //CreateVisualReciept();

                    InsertSales();
                    updateInventory();
                    createDataset();
                    createPieChart();
                    nextCustomer();
                    jPanel6.validate();
                }
                discountPrice = 0.00;

            }
        } catch (HeadlessException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_CashActionPerformed

    private void CashKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CashKeyTyped
        Quantity.setEnabled(false);
    }//GEN-LAST:event_CashKeyTyped

    private void DiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DiscountActionPerformed
        DiscountPassword dcp = new DiscountPassword();
        dcp.setVisible(true);
//        if(disCountOkay){
//            int discountPercent = Integer.parseInt(Discount.getText());
//            String disLessNine = ".0";
//            String disGreaterTen = ".";
//            if (Discount.getText().equals("0") || Discount.getText().isEmpty()) {
//                TotalAmount.setText(Double.toString(staticTotalAmount));
//            } else if (discountPercent <= 9 && discountPercent >= 1) {
//                String discountDecimal = disLessNine + Discount.getText();
//                double discountLess = Double.parseDouble(discountDecimal);
//                double discount = staticTotalAmount * discountLess;
//                discountPrice = discount;
//                double finalAmount = discountPrice - discount;
//                TotalAmount.setText(Double.toString(finalAmount));
//
//            } else if (discountPercent <= 100 && discountPercent >= 10) {
//                String discountDecimal = disGreaterTen + Discount.getText();
//                double discountLess = Double.parseDouble(discountDecimal);
//                double discount = staticTotalAmount * discountLess;
//                discountPrice = discount;
//                double finalAmount = staticTotalAmount - discount;
//                TotalAmount.setText(Double.toString(finalAmount));
//
//            } else {
//                JOptionPane.showMessageDialog(null, "Invalid Discount!");
//            }
//            
//    
//        }
//        else{
//            
//        }
//        CashTransactions.validate();
        disCountOkay = false;
    }//GEN-LAST:event_DiscountActionPerformed

    private void QuantityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_QuantityFocusGained
        getPrice();

    }//GEN-LAST:event_QuantityFocusGained

    private void QuantityInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_QuantityInputMethodTextChanged
        auto_compute_scanned();        // TODO add your handling code here:
    }//GEN-LAST:event_QuantityInputMethodTextChanged

    private void QuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuantityActionPerformed

        if (OrderItem.getText().equals("Order Code")) {
            JOptionPane.showMessageDialog(null, "Please Select Order first!");
            //Quantity.setText("");

        } else if (Quantity.getText().equals("0")) {
            JOptionPane.showMessageDialog(null, "Invalid Quantity!");
            //Quantity.setText("");
        } else if (Integer.parseInt(Quantity.getText()) > quant) {
            JOptionPane.showMessageDialog(null, "Product supply is not enough please check quantity! Remaining stock is " + Integer.toString(quant) + "");
            Quantity.setText(Integer.toString(quant));
            Quantity.requestFocus();
            int itemQuantity = Integer.parseInt(Quantity.getText());
            double itemAmount = itemQuantity * menuPrice;
            itemPrice = itemAmount;
            double amountPayable = Double.parseDouble(TotalAmount.getText());
            double totalPayable = itemAmount + amountPayable;
            TotalAmount.setText(Double.toString(totalPayable) + "0");
            staticTotalAmount = Double.parseDouble(TotalAmount.getText());
            //UpdateInventory();

            listItem();

            ScannedCode.setText("");
            ScannedCode.requestFocus();
        } else {
            int itemQuantity = Integer.parseInt(Quantity.getText());
            double itemAmount = itemQuantity * menuPrice;
            itemPrice = itemAmount;
            double amountPayable = Double.parseDouble(TotalAmount.getText());
            double totalPayable = itemAmount + amountPayable;
            TotalAmount.setText(Double.toString(totalPayable) + "0");
            staticTotalAmount = Double.parseDouble(TotalAmount.getText());
            //UpdateInventory();

            listItem();

            ScannedCode.setText("");
            ScannedCode.requestFocus();

        }


    }//GEN-LAST:event_QuantityActionPerformed

    private void QuantityPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_QuantityPropertyChange

    }//GEN-LAST:event_QuantityPropertyChange

    private void QuantityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QuantityKeyPressed

    }//GEN-LAST:event_QuantityKeyPressed

    private void QuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QuantityKeyReleased

    }//GEN-LAST:event_QuantityKeyReleased

    private void QuantityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_QuantityKeyTyped
        char c = evt.getKeyChar();
        if (!((c >= '0' && (c <= '9')))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_QuantityKeyTyped

    private void OrderItemPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_OrderItemPropertyChange
        Quantity.setEditable(true);
        //RecieptView.removeAll();
        //Quantity.setText("");
        getCustomer();
        try {
            staticMenuCode = OrderItem.getText();
            pst = con.prepareStatement("SELECT menuDesc FROM menus WHERE menuID='" + staticMenuCode + "'");
            rs = pst.executeQuery();
            if (rs.next()) {
                OrderDesc.setText(rs.getString("menuDesc"));

            } else {
                //OrderDesc.setText("");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_OrderItemPropertyChange
    int quant;
    int order_check_id;
    private void SearchProductResultMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SearchProductResultMouseClicked

        Connection con1 = DBConnection.DBConnection();
        int selectedRow = SearchProductResult.getSelectedRow();
        order_check_id = Integer.parseInt(SearchProductResult.getModel().getValueAt(selectedRow, 0).toString());
        int quantity = Integer.parseInt(SearchProductResult.getModel().getValueAt(selectedRow, 2).toString());
        quant = quantity;
        if (quantity < 1 || Integer.toString(quantity).contains("-")) {
            try {
                PreparedStatement pst2 = con1.prepareStatement("UPDATE stocks_inventory SET available_stock = 0 WHERE prod_id = " + order_check_id + "");
                pst2.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

            JOptionPane.showMessageDialog(null, "Unable to process! \n No Stocks available!", "Warning", JOptionPane.WARNING_MESSAGE);
            int update_stk = JOptionPane.showConfirmDialog(null, "Do you want to update Inventory? ", "Hi there!", JOptionPane.YES_NO_OPTION);
            if (update_stk == JOptionPane.YES_OPTION) {
                UpdateStocks us = new UpdateStocks();
                us.setVisible(true);
                this.setState(1);
            }
        } else {
            ProductEntry.setText("");
            int selectedRow1 = SearchProductResult.getSelectedRow();
            int order_id = Integer.parseInt(SearchProductResult.getModel().getValueAt(selectedRow1, 0).toString());
            OrderItem.setText(Integer.toString(order_id));
            Quantity.requestFocus();
        }

    }//GEN-LAST:event_SearchProductResultMouseClicked

    private void SearchProductResultKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchProductResultKeyPressed
        char c = evt.getKeyChar();
        int selectedRow = SearchProductResult.getSelectedRow();
        if (c == java.awt.event.KeyEvent.VK_ENTER) {
            int order_id = Integer.parseInt(SearchProductResult.getModel().getValueAt(selectedRow, 0).toString());
            OrderItem.setText(Integer.toString(order_id));
            Quantity.requestFocus();
        }
    }//GEN-LAST:event_SearchProductResultKeyPressed

    private void ProductEntryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProductEntryKeyTyped
        searchProduct();
    }//GEN-LAST:event_ProductEntryKeyTyped

    private void ScannedCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ScannedCodeActionPerformed
        get_scanned_id();
        if (Quantity.getText().isEmpty()) {
            Quantity.setText("1");
        } else {
            String input = Quantity.getText();
            Quantity.setText(input);
        }
        searchScannedProduct();

    }//GEN-LAST:event_ScannedCodeActionPerformed

    private void ScannedCodePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ScannedCodePropertyChange

    }//GEN-LAST:event_ScannedCodePropertyChange

    private void ScannedCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ScannedCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ScannedCodeKeyPressed

    private void ScannedCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ScannedCodeKeyReleased

    }//GEN-LAST:event_ScannedCodeKeyReleased

    private void ButtonPanelInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_ButtonPanelInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_ButtonPanelInputMethodTextChanged

    private void OrderListTableCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_OrderListTableCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_OrderListTableCaretPositionChanged

    private void OrderListTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OrderListTableKeyPressed
        char c = evt.getKeyChar();
        DefaultTableModel dtm = (DefaultTableModel) OrderListTable.getModel();
        int selectedRow = OrderListTable.getSelectedRow();
        if (c == java.awt.event.KeyEvent.VK_DELETE) {
            double amountToRemove = Double.parseDouble(OrderListTable.getModel().getValueAt(selectedRow, 3).toString());
            double actualAmount = Double.parseDouble(TotalAmount.getText());
            double revertedAmount = actualAmount - amountToRemove;
            TotalAmount.setText(Double.toString(revertedAmount));
            RestoreInventory();
            dtm.removeRow(OrderListTable.getSelectedRow());

        }
    }//GEN-LAST:event_OrderListTableKeyPressed

    private void recentSalesTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentSalesTableMouseClicked
        newCustomer();
        VoidBtn.setVisible(true);
        viewRecentPurchases();
        getPreviousPurchaseDetails();
        ReViewVisualReciept();
    }//GEN-LAST:event_recentSalesTableMouseClicked

    private void recentSalesTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_recentSalesTableKeyPressed
        char c = evt.getKeyChar();
        if (c == java.awt.event.KeyEvent.VK_F12) {
            ReprintOutputReciept();
        } else if (c == java.awt.event.KeyEvent.VK_DELETE) {
            System.out.print("Delete");
            String recieptnumber = recentSalesTable.getModel().getValueAt(recentSalesTable.getSelectedRow(), 0).toString();
            AdminPassword ap = new AdminPassword();
            ap.getRecipetNumber(recieptnumber);
            ap.setVisible(true);
        }
    }//GEN-LAST:event_recentSalesTableKeyPressed

    private void SearchORActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchORActionPerformed
        viewRecieptDetails();
    }//GEN-LAST:event_SearchORActionPerformed

    private void OrderDescPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_OrderDescPropertyChange
        TotalSalesToday();
        ChangeText.setText("0.00");
        Cash.setText("");
        Quantity.setEnabled(true);
    }//GEN-LAST:event_OrderDescPropertyChange

    private void CustomerLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CustomerLabelMouseClicked

    }//GEN-LAST:event_CustomerLabelMouseClicked

    private void CustomerLabelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_CustomerLabelPropertyChange

    }//GEN-LAST:event_CustomerLabelPropertyChange

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        JOptionPane.showMessageDialog(null, "You have discovered a Premium Feature, Please contact the developer to know more about this feature.", "Premium Feature", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void RecieveCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RecieveCashActionPerformed
        Cash.requestFocus();
    }//GEN-LAST:event_RecieveCashActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        //backupDB();
        backupData();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void recentSalesTableKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_recentSalesTableKeyTyped
        char c = evt.getKeyChar();
        if (c == java.awt.event.KeyEvent.VK_ENTER) {
            ReprintOutputReciept();
        } else if (c == java.awt.event.KeyEvent.VK_DELETE) {
            System.out.print("Delete");
            String recieptnumber = recentSalesTable.getModel().getValueAt(recentSalesTable.getSelectedRow(), 0).toString();
            AdminPassword ap = new AdminPassword();
            ap.getRecipetNumber(recieptnumber);
            ap.setVisible(true);
        }
    }//GEN-LAST:event_recentSalesTableKeyTyped

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        StoreInfo si = new StoreInfo();
        si.setVisible(true);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void VoidBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VoidBtnActionPerformed
        AdminPassword ap = new AdminPassword();
        ap.or = Reciept;
        ap.setVisible(true);
    }//GEN-LAST:event_VoidBtnActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed

        ReprintOutputReciept();

    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        ImportPrompt ip = new ImportPrompt();
        ip.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        PrintService[] lps = PrintServiceLookup.lookupPrintServices(null, null);

        PrintService srvc = (PrintService) JOptionPane.showInputDialog(this, "Select default printer", "Printer", JOptionPane.PLAIN_MESSAGE, null, lps, lps[0]);
        try {
            Connection conn = DBConnection.DBConnection();
            PreparedStatement pst1 = conn.prepareStatement("SELECT * FROM printer WHERE id = 1");
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                PreparedStatement pst2 = conn.prepareStatement("UPDATE printer SET location ='" + srvc + "' WHERE id = 1");
                pst2.executeUpdate();
            } else {
                PreparedStatement pst2 = conn.prepareStatement("INSERT INTO printer(id,location) VALUES(?,?)");
                pst2.setInt(1, 1);
                pst2.setString(2, srvc.toString());
                pst2.executeUpdate();
            }
        } catch (SQLException ex) {

        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
            java.util.logging.Logger.getLogger(POSController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(POSController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(POSController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(POSController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new POSController().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ActiveUserName8;
    private javax.swing.JMenuItem AddStock;
    private javax.swing.JPanel ButtonPanel;
    private javax.swing.JTextField Cash;
    private javax.swing.JPanel CashTransactions;
    public static final javax.swing.JLabel CashierLabel = new javax.swing.JLabel();
    private javax.swing.JLabel ChangeText;
    public static final javax.swing.JLabel CustomerLabel = new javax.swing.JLabel();
    public static final javax.swing.JLabel DateLabel = new javax.swing.JLabel();
    public static javax.swing.JTextField Discount;
    private javax.swing.JMenu EmployeeMenu;
    private javax.swing.JMenuItem EndOfTheDay;
    private javax.swing.JMenuItem Expense;
    private javax.swing.JMenu ExpenseMenu;
    private javax.swing.JMenuItem Logout;
    private javax.swing.JMenuItem MManagement;
    private javax.swing.JLabel NameOfStore;
    public static final javax.swing.JLabel OrderDesc = new javax.swing.JLabel();
    private javax.swing.JLabel OrderItem;
    private javax.swing.JPanel OrderList;
    private javax.swing.JTable OrderListTable;
    private javax.swing.JLabel PCHost1;
    private javax.swing.JMenuItem Payroll;
    private javax.swing.JTextField ProductEntry;
    private javax.swing.JTextField Quantity;
    private javax.swing.JPanel RecentSales;
    private javax.swing.JMenuItem RecieveCash;
    private javax.swing.JPanel RightPanel;
    private javax.swing.JTextField ScannedCode;
    private javax.swing.JTextField SearchOR;
    private javax.swing.JTable SearchProductResult;
    private javax.swing.JMenuItem StockManagement;
    public javax.swing.JPanel SwitchPanel;
    public static final javax.swing.JLabel TimeLabel = new javax.swing.JLabel();
    public static final javax.swing.JLabel TotalAmount = new javax.swing.JLabel();
    private javax.swing.JLabel TotalSalesToday;
    private javax.swing.JMenuItem UserManagement;
    private javax.swing.JButton VoidBtn;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem newCust;
    public javax.swing.JTable recentSalesTable;
    // End of variables declaration//GEN-END:variables
}
