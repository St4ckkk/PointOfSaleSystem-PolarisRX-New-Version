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
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import static pointofsalesystem.POSController.DateLabel;

/**
 *
 * @author Michael Paul Sebando
 */
public final class ListOfMenus extends javax.swing.JFrame {

    /**
     * Creates new form ListOfMenus
     */
    public ListOfMenus() {
        initComponents();
        ImageIcon imgicon = new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        this.setTitle("Store Items Management");
        showMenus();

    }
    Connection con = DBConnection.DBConnection();
    PreparedStatement pst;
    ResultSet rs;
    int idMenu;

    private void generateBarcode() {
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = Year.now().getValue();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        MenuCode.setText(Integer.toString(year).substring(2, 4) + Integer.toString(month) + Integer.toString(hour) + Integer.toString(minute) + Integer.toString(second));
    }

    public void showMenus() {
        try {
            pst = con.prepareStatement("SELECT * FROM menus ORDER BY(menuID) ASC");
            rs = pst.executeQuery();
            DefaultTableModel menuList = (DefaultTableModel) ListOfMenu.getModel();
            menuList.setRowCount(0);
            while (rs.next()) {
                Object list[] = {rs.getInt("menuID"), rs.getString("menuCode"), rs.getString("menuDesc"), rs.getDouble("OrigPrice") + "0", rs.getDouble("menuPrice") + "0"};
                menuList.addRow(list);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void showMenusByName() {
        try {
            pst = con.prepareStatement("SELECT * FROM menus WHERE menuDesc LIKE '%" + MenuDesc.getText() + "%'ORDER BY(menuID) ASC");
            rs = pst.executeQuery();
            DefaultTableModel menuList = (DefaultTableModel) ListOfMenu.getModel();
            menuList.setRowCount(0);
            while (rs.next()) {
                Object list[] = {rs.getInt("menuID"), rs.getString("menuCode"), rs.getString("menuDesc"), rs.getDouble("OrigPrice") + "0", rs.getDouble("menuPrice") + "0"};
                menuList.addRow(list);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void showIndividualMenu() {
        try {
            pst = con.prepareStatement("SELECT * FROM menus WHERE menuCode = '" + MenuCode.getText() + "' ORDER BY(menuID) ASC");
            rs = pst.executeQuery();
            DefaultTableModel menuList = (DefaultTableModel) ListOfMenu.getModel();
            menuList.setRowCount(0);
            while (rs.next()) {
                Object list[] = {rs.getInt("menuID"), rs.getString("menuCode"), rs.getString("menuDesc"), rs.getDouble("OrigPrice") + "0", rs.getDouble("menuPrice") + "0"};
                menuList.addRow(list);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void assignmenuID() {
        try {
            pst = con.prepareStatement("SELECT MAX(menuID) FROM menus");
            rs = pst.executeQuery();
            if (rs.next()) {
                int nextID = rs.getInt(1) + 1;
                idMenu = nextID;
                MenuID.setText(Integer.toString(nextID));
                MenuCode.setText("");
                //MenuPrice.setText("");
                MenuDesc.setText("");
                //OrigPrice.setText("");
                AddButton.setText("ADD");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void addMenu() {

        try {
            pst = con.prepareStatement("SELECT * FROM menus WHERE menuCode='" + MenuCode.getText() + "' AND menuDesc='" + MenuDesc.getText() + "'");
            rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Duplicated Product Code or Product Name, Please provide a Unique Menu Code", "Warning", JOptionPane.WARNING_MESSAGE);
                MenuCode.setText("");
            } else {
                pst = con.prepareStatement("INSERT INTO menus(menuID,menuCode,menuDesc)" + "VALUES(?,?,?)");
                pst.setInt(1, Integer.parseInt(MenuID.getText()));
                pst.setString(2, MenuCode.getText().toUpperCase());
                pst.setString(3, MenuDesc.getText());
                // pst.setDouble(4, Double.parseDouble(OrigPrice.getText()));
                // pst.setDouble(5, Double.parseDouble(MenuPrice.getText()));
                pst.executeUpdate();
                setInventory();
                JOptionPane.showMessageDialog(null, "Product Added!");

                int update_stock = JOptionPane.showConfirmDialog(null, "Do you want to update stock inventory? ", "Hi there ", JOptionPane.YES_NO_OPTION);
                if (update_stock == JOptionPane.YES_OPTION) {

                    UpdateStocks us = new UpdateStocks();
                    UpdateStocks.ProdID.setText(MenuID.getText());
                    UpdateStocks.ProdCode.setText(MenuCode.getText());
                    UpdateStocks.prodDesc.setText(MenuDesc.getText());
                    us.setVisible(true);
                    dispose();
                }
                assignmenuID();
            }
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void setInventory() {
        try {
            Connection con1 = DBConnection.DBConnection();
            PreparedStatement pst1 = con1.prepareStatement("INSERT INTO stocks_inventory(prod_id,unit)" + "VALUES(?,?)");
            pst1.setInt(1, Integer.parseInt(MenuID.getText()));
            pst1.setString(2, UnitDesc.getText());
            pst1.executeUpdate();
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void updateMenu() {
        try {
            pst = con.prepareStatement("UPDATE menus SET menuCode=?,menuDesc=?, menuID=? WHERE menuID='" + idMenu + "'");
            pst.setString(1, MenuCode.getText().toUpperCase());
            pst.setString(2, MenuDesc.getText());
            //pst.setDouble(3, Double.parseDouble(OrigPrice.getText()));
            //pst.setDouble(4, Double.parseDouble(MenuPrice.getText()));
            pst.setInt(3, Integer.parseInt(MenuID.getText()));

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Product Updated!");
            showMenus();
            assignmenuID();

        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    private static JButton btnDelete = new JButton("Delete");

    private void getNearToExpireProduct() {

        try {
            String sql = "SELECT stocks_expiration.id, menus.menuCode, menus.menuDesc, stocks_expiration.expiration_date,stocks_expiration.status, stocks_expiration.original_price, stocks_expiration.expiration_date, stocks_expiration.quantity, stocks_expiration.lot_no,  DATEDIFF(stocks_expiration.expiration_date, NOW() ) AS expiration_day FROM stocks_expiration LEFT JOIN menus ON menus.menuID = stocks_expiration.product_id WHERE DATEDIFF(stocks_expiration.expiration_date, NOW()) < 60 ORDER BY DATEDIFF(stocks_expiration.expiration_date, NOW()) ASC";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            DefaultTableModel dtm = (DefaultTableModel) NearToExpireProduct.getModel();
            dtm.setRowCount(0);
            Date d1 = new Date();
            SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd");

            String currentDate = s1.format(d1);
            String isExpired;

            String status;

            while (rs.next()) {
                String expiration_date = rs.getString("expiration_date");
                if (expiration_date.equals(currentDate)) {
                    isExpired = "Expired";
                } else {
                    if (Integer.parseInt(rs.getString("expiration_day")) > 0) {
                        isExpired = rs.getString("expiration_day") + " days to expire";
                    } else {
                        isExpired = "Product expired";
                    }

                }

                String stats = rs.getString("status");
                if (stats.equals("1")) {
                    status = "In Stock";
                } else {
                    status = "Pulled Out";
                }

                Object o[] = {rs.getString("id"), rs.getString("menuCode"), rs.getString("menuDesc"), rs.getString("original_price"), isExpired, rs.getString("lot_no"), rs.getString("quantity"), status};
                dtm.addRow(o);

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void fillLabelsFromTable() {
        int row = ListOfMenu.getSelectedRow();
        String menuid = ListOfMenu.getModel().getValueAt(row, 0).toString();
        String menucode = ListOfMenu.getModel().getValueAt(row, 1).toString();
        String origprice = ListOfMenu.getModel().getValueAt(row, 3).toString();
        String menuprice = ListOfMenu.getModel().getValueAt(row, 4).toString();
        String menudesc = ListOfMenu.getModel().getValueAt(row, 2).toString();
        idMenu = Integer.parseInt(menuid);
        MenuID.setText(menuid);
        MenuCode.setText(menucode);
        //OrigPrice.setText(origprice);
        price = menuprice;
        MenuDesc.setText(menudesc);
        AddButton.setText("UPDATE");

    }

    private void deleteMenu() {
        try {
            pst = con.prepareStatement("DELETE FROM menus WHERE menuID='" + MenuID.getText() + "'");
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this Menu?", "Warning", JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Successfully deleted! You can now add another Menu to button " + MenuID.getText(), "Message", JOptionPane.INFORMATION_MESSAGE);
                assignmenuID();
                showMenus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void Strict() {
        try {
            pst = con.prepareStatement("SELECT * FROM AccessLevel WHERE logStatus=1");
            rs = pst.executeQuery();
            if (rs.next()) {
                String userType = rs.getString("role");
                if (!userType.equals("Admin")) {

                }
            } else {

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void set_code(String code) {
        MenuCode.setText(code);
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
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        MenuID = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        MenuDesc = new javax.swing.JTextArea();
        UnitDesc = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        DeleteButton = new javax.swing.JButton();
        AddButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        AddButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        ListOfMenu = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        NearToExpireProduct = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        transfered_table = new javax.swing.JTable();

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
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        MenuList.setBackground(new java.awt.Color(0, 255, 255));
        MenuList.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Product ID");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 30));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setText("Code");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("Product Description");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 140, -1));

        MenuCode.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        MenuCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                MenuCodeFocusLost(evt);
            }
        });
        MenuCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuCodeActionPerformed(evt);
            }
        });
        jPanel2.add(MenuCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 120, 30));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setText("Unit Description");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, 20));

        MenuID.setEditable(false);
        MenuID.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        MenuID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                MenuIDFocusLost(evt);
            }
        });
        jPanel2.add(MenuID, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 160, -1));

        MenuDesc.setColumns(20);
        MenuDesc.setFont(new java.awt.Font("Calibri", 0, 13)); // NOI18N
        MenuDesc.setRows(5);
        MenuDesc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuDescMouseClicked(evt);
            }
        });
        MenuDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                MenuDescKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(MenuDesc);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 250, 120));

        UnitDesc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel2.add(UnitDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 160, 30));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Barcode.png"))); // NOI18N
        jButton1.setToolTipText("Generate Barcode");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 40, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 560, 170));

        DeleteButton.setBackground(new java.awt.Color(255, 51, 0));
        DeleteButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        DeleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Delete Document_28px.png"))); // NOI18N
        DeleteButton.setText("DELETE");
        DeleteButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });
        jPanel1.add(DeleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 60, 130, 40));

        AddButton.setBackground(new java.awt.Color(0, 204, 0));
        AddButton.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        AddButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/AddMenu.png"))); // NOI18N
        AddButton.setText("ADD");
        AddButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        jPanel1.add(AddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 10, 130, 40));

        jButton2.setBackground(new java.awt.Color(255, 255, 0));
        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("Clear");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 160, 150, 40));

        AddButton1.setBackground(new java.awt.Color(0, 204, 0));
        AddButton1.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        AddButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pointofsalesystem/Icons/Print.png"))); // NOI18N
        AddButton1.setText("Print Barcode");
        AddButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        AddButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(AddButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 110, 130, -1));

        MenuList.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 750, 200));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 51));
        jLabel2.setText("PRODUCT ENTRY");
        jPanel3.add(jLabel2);

        MenuList.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 750, 30));

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        ListOfMenu.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ListOfMenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "^", "Product Code", "Description", "Original Price", "Store Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ListOfMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ListOfMenu.setSelectionBackground(new java.awt.Color(0, 204, 0));
        ListOfMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ListOfMenuMouseClicked(evt);
            }
        });
        ListOfMenu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ListOfMenuKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(ListOfMenu);
        if (ListOfMenu.getColumnModel().getColumnCount() > 0) {
            ListOfMenu.getColumnModel().getColumn(0).setResizable(false);
            ListOfMenu.getColumnModel().getColumn(0).setPreferredWidth(0);
            ListOfMenu.getColumnModel().getColumn(2).setPreferredWidth(250);
        }

        jTabbedPane1.addTab("All Products", jScrollPane1);

        NearToExpireProduct.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        NearToExpireProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Code", "Description", "Purchase Price", "Expiration Date", "Lot #", "Quantity", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        NearToExpireProduct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        NearToExpireProduct.setSelectionBackground(new java.awt.Color(0, 204, 0));
        NearToExpireProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NearToExpireProductMouseClicked(evt);
            }
        });
        NearToExpireProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NearToExpireProductKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(NearToExpireProduct);
        if (NearToExpireProduct.getColumnModel().getColumnCount() > 0) {
            NearToExpireProduct.getColumnModel().getColumn(0).setMinWidth(0);
            NearToExpireProduct.getColumnModel().getColumn(0).setPreferredWidth(0);
            NearToExpireProduct.getColumnModel().getColumn(0).setMaxWidth(0);
            NearToExpireProduct.getColumnModel().getColumn(2).setPreferredWidth(250);
            NearToExpireProduct.getColumnModel().getColumn(4).setPreferredWidth(250);
        }

        jTabbedPane1.addTab("Stocks Expirations", jScrollPane3);

        transfered_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Date", "Item", "Quantity", "Notes"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(transfered_table);
        if (transfered_table.getColumnModel().getColumnCount() > 0) {
            transfered_table.getColumnModel().getColumn(0).setMinWidth(0);
            transfered_table.getColumnModel().getColumn(0).setPreferredWidth(0);
            transfered_table.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        jTabbedPane1.addTab("Pulled-Out Items", jScrollPane4);

        MenuList.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 750, 210));

        getContentPane().add(MenuList, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 770, 490));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        try {
            pst = con.prepareStatement("SELECT count(*) FROM menus");
            rs = pst.executeQuery();
            if (rs.next()) {
                int totalMenus = rs.getInt(1);
                String total = Integer.toString(totalMenus);
                if (total.equals("10000000000")) {
                    JOptionPane.showMessageDialog(null, "Sorry you have reached the maximum Stocks");
                } else {
                    if (AddButton.getText().equals("ADD")) {
                        if (MenuCode.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "Stock Code is Empty!");
                        } //                       else if(MenuPrice.getText().equals("")){
                        //                           JOptionPane.showMessageDialog(null, "Stock Price is Empty!");
                        //                       }
                        else if (MenuDesc.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "Stock Description is Empty!");
                        } else if (UnitDesc.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "Unit of Description is Empty!");
                        } else {
                            addMenu();
                            //assignmenuID();
                            showMenus();
                        }
                    } else if (AddButton.getText().equals("UPDATE")) {
                        int sure = JOptionPane.showConfirmDialog(null, "Are you sure you want to update Product Informations?", "Please confirm", JOptionPane.YES_NO_OPTION);
                        if (sure == JOptionPane.YES_OPTION) {
                            if (MenuCode.getText().equals("")) {
                                JOptionPane.showMessageDialog(null, "Stock Code is Empty!");
                            } //                       else if(MenuPrice.getText().equals("")){
                            //                           JOptionPane.showMessageDialog(null, "Stock Price is Empty!");
                            //                       }
                            else if (MenuDesc.getText().equals("")) {
                                JOptionPane.showMessageDialog(null, "Stock Description is Empty!");
                            } else {
                                updateMenu();
                                //assignmenuID();
                                showMenus();
                            }
                        } else {

                        }

                    }
                }
            }
        } catch (HeadlessException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        if ("ADD".equals(AddButton.getText())) {
            MenuCode.setEditable(true);
        } else {
            MenuCode.setEditable(false);
        }

    }//GEN-LAST:event_AddButtonActionPerformed
    private void pullOut(String stockID, String quantity) {
        try {
            String sql = "SELECT * FROM stocks_expiration WHERE id = '" + stockID + "'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String prodID = rs.getString("product_id");
                try {
                    String sql_a = "UPDATE stocks_expiration SET status = '0' WHERE id = '" + stockID + "'";
                    PreparedStatement pst_a = con.prepareStatement(sql_a);
                    pst_a.executeUpdate();

                    String sql_b = "UPDATE stocks_inventory SET available_stock = (available_stock - '" + quantity + "') WHERE prod_id = '" + prodID + "'";
                    PreparedStatement pst_b = con.prepareStatement(sql_b);
                    pst_b.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Product Inventory pulled-out successfully from the database");

                } catch (HeadlessException | SQLException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    private void getPullOuts(){
        try {
            Connection conn = DBConnection.DBConnection();
            PreparedStatement psts = conn.prepareStatement("SELECT * FROM pulled_out_stck LEFT JOIN menus ON menus.menuID = pulled_out_stck.prod_id ORDER BY pulled_out_stck.added DESC");
            ResultSet rss = psts.executeQuery();
            DefaultTableModel dtms = (DefaultTableModel) transfered_table.getModel();
            dtms.setRowCount(0);
            while(rss.next()){
                Object os[] = {rss.getString("pulled_out_stck.id"), rss.getString("pulled_out_stck.added"),rss.getString("menus.menuDesc"),rss.getString("pulled_out_stck.quantity"),rss.getString("pulled_out_stck.notes")};
                dtms.addRow(os);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ListOfMenus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jButton2.getText().equals("Clear")) {
            assignmenuID();
        } else if (jButton2.getText().equals("Pull Out")) {
            int selectedRow = NearToExpireProduct.getSelectedRow();
            String stockID = NearToExpireProduct.getModel().getValueAt(selectedRow, 0).toString();
            String quantity = NearToExpireProduct.getModel().getValueAt(selectedRow, 6).toString();
            if (stockID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please select product from the table below to pull-out");
            } else {
                int ask = JOptionPane.showConfirmDialog(null, "Are you sure you want to pull-out this product inventory?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (ask == JOptionPane.YES_OPTION) {
                    pullOut(stockID, quantity);
                    getNearToExpireProduct();
                    showMenus();

                } else {

                }

            }
        } else if (jButton2.getText().equals("Pull Out-Transfer")) {

            PullOutStock postock = new PullOutStock();
            postock.prod_id = MenuID.getText();
            PullOutStock.Stock.setText(MenuDesc.getText());
            postock.setVisible(true);

        }

        // MenuID.setText(Integer.toString(idMenu));
        //MenuCode.setText("");
        //MenuPrice.setText("");
        //MenuDesc.setText("");
        //AddButton.setText("ADD");
        //OrigPrice.setText("");

    }//GEN-LAST:event_jButton2ActionPerformed

    private void ListOfMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ListOfMenuMouseClicked
        MenuCode.setEditable(false);
        fillLabelsFromTable();

        int selectedRow = ListOfMenu.getSelectedRow();

        jButton2.setVisible(true);
        jButton2.setText("Pull Out-Transfer");
        jButton2.setBackground(Color.red);

    }//GEN-LAST:event_ListOfMenuMouseClicked

    private void MenuCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MenuCodeFocusLost

    }//GEN-LAST:event_MenuCodeFocusLost

    private void MenuIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MenuIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuIDFocusLost

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        MenuCode.requestFocus();
        getPullOuts();
    }//GEN-LAST:event_formWindowActivated

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
        if (MenuCode.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Please select Menu to be removed!");
        } else {
            deleteMenu();
        }
    }//GEN-LAST:event_DeleteButtonActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        assignmenuID();
        Strict();
        getNearToExpireProduct();
        getPullOuts();
    }//GEN-LAST:event_formWindowOpened

    private void ListOfMenuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ListOfMenuKeyPressed
        fillLabelsFromTable();
    }//GEN-LAST:event_ListOfMenuKeyPressed

    private void NearToExpireProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NearToExpireProductMouseClicked

        int selectedRow = NearToExpireProduct.getSelectedRow();
        String stats = NearToExpireProduct.getModel().getValueAt(selectedRow, 7).toString();

        if (stats.equals("In Stock")) {
            jButton2.setVisible(true);
            jButton2.setText("Pull Out");
            jButton2.setBackground(Color.red);
        } else {
            jButton2.setVisible(false);
            jButton2.setText("Clear");
            jButton2.setBackground(Color.green);
        }


    }//GEN-LAST:event_NearToExpireProductMouseClicked

    private void NearToExpireProductKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NearToExpireProductKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NearToExpireProductKeyPressed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        jButton2.setText("Clear");
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void MenuCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuCodeActionPerformed
        showIndividualMenu();
    }//GEN-LAST:event_MenuCodeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (!"".equals(MenuCode.getText())) {
            int sure = JOptionPane.showConfirmDialog(null, "Are you sure you want to regenerate barcode?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (sure == JOptionPane.YES_OPTION) {

            } else {

            }

        } else {
            generateBarcode();
        }

    }//GEN-LAST:event_jButton1ActionPerformed
    String price;
    private void AddButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButton1ActionPerformed

        if (MenuCode.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select product below to print its barcode", "Select Product", JOptionPane.QUESTION_MESSAGE);
        } else {
            QuantPrompt qp = new QuantPrompt();
            qp.code = MenuCode.getText();
            qp.prodName = MenuDesc.getText();
            qp.price = "₱" + price;
            qp.setVisible(true);
        }
    }//GEN-LAST:event_AddButton1ActionPerformed

    private void MenuDescMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuDescMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuDescMouseClicked

    private void MenuDescKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenuDescKeyTyped
        showMenusByName();
    }//GEN-LAST:event_MenuDescKeyTyped

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
            java.util.logging.Logger.getLogger(ListOfMenus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ListOfMenus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ListOfMenus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListOfMenus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ListOfMenus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton AddButton;
    public javax.swing.JButton AddButton1;
    public javax.swing.JButton DeleteButton;
    private javax.swing.JTable ListOfMenu;
    public static final javax.swing.JTextField MenuCode = new javax.swing.JTextField();
    private javax.swing.JTextArea MenuDesc;
    private javax.swing.JTextField MenuID;
    public javax.swing.JPanel MenuList;
    private javax.swing.JTable NearToExpireProduct;
    private javax.swing.JTextField UnitDesc;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable transfered_table;
    // End of variables declaration//GEN-END:variables
}
