/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointofsalesystem;

import employeeinformationsystem.DatabaseConnection;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Michael Paul Sebando
 */
public class StoreInfo extends javax.swing.JFrame {

    /**
     * Creates new form StoreInfo
     */
    public StoreInfo() {
        initComponents();
        ImageIcon imgicon = new ImageIcon(getClass().getResource("/pointofsalesystem/Icons/POS Terminal_100px.png"));
        this.setIconImage(imgicon.getImage());
        this.setTitle("Store Setup");
        Value.requestFocus();

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
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        Note = new javax.swing.JLabel();
        Value = new javax.swing.JTextField();
        Note1 = new javax.swing.JLabel();
        Address = new javax.swing.JTextField();
        Note2 = new javax.swing.JLabel();
        Value2 = new javax.swing.JTextField();
        Note3 = new javax.swing.JLabel();
        Value3 = new javax.swing.JTextField();
        Note4 = new javax.swing.JLabel();
        Value4 = new javax.swing.JTextField();
        Value5 = new javax.swing.JTextField();
        Note5 = new javax.swing.JLabel();
        Add = new javax.swing.JButton();
        Note6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("STORE SETUP");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 10, -1, -1));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 540, 50));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Note.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Note.setText("Store name");
        jPanel4.add(Note, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 110, 30));

        Value.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Value.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(Value, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 20, 370, 30));

        Note1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Note1.setText("Address");
        jPanel4.add(Note1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 110, 30));

        Address.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Address.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(Address, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 370, 30));

        Note2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Note2.setText("E-Mail");
        jPanel4.add(Note2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 110, 20));

        Value2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Value2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(Value2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 120, 370, 30));

        Note3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Note3.setText("Contact No.");
        jPanel4.add(Note3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 110, 30));

        Value3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Value3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(Value3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 370, 30));

        Note4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Note4.setText("TIN");
        jPanel4.add(Note4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 110, 30));

        Value4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Value4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(Value4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 370, 30));

        Value5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Value5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(Value5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 370, 30));

        Note5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Note5.setText("Logo");
        jPanel4.add(Note5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 310, 110, 30));

        Add.setBackground(new java.awt.Color(0, 204, 0));
        Add.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        Add.setText("Update");
        Add.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddActionPerformed(evt);
            }
        });
        jPanel4.add(Add, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 310, 110, 80));

        Note6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        Note6.setText("Owner");
        jPanel4.add(Note6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 110, 30));

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setText("Select");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 310, 110, 80));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 110, 80));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, 110, 80));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 540, 400));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 560, 480));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    private ImageIcon format = null;
    String fname = null;
    int s = 0;
    byte[] pimage = null;
    InputStream in = null;
    Connection con = DBConnection.DBConnection();
    PreparedStatement pst;
    ResultSet rs;
    FileInputStream fs = null;
    File f = null;

    private void selectImage() throws FileNotFoundException {
        JFileChooser fchoser = new JFileChooser();
        fchoser.showOpenDialog(null);
        f = fchoser.getSelectedFile();
        fname = f.getAbsolutePath();
        System.out.print(fname);

        fs = new FileInputStream(f);
        ImageIcon micon = new ImageIcon(fname);
        jLabel2.setIcon(micon);

    }

    public ImageIcon resizeImage(String imagePath, byte[] pic) {

        ImageIcon myImage = null;
        if (imagePath != null) {
            myImage = new ImageIcon(imagePath);

        } else {
            myImage = new ImageIcon(pic);
        }

        Image img = myImage.getImage();
        Image img2 = img.getScaledInstance(jLabel2.getHeight(), jLabel2.getWidth(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img2);

        return image;
    }

    private void getStoreInfo() throws IOException {
        try {

            PreparedStatement pst1 = con.prepareStatement("SELECT * FROM store");
            ResultSet rs1 = pst1.executeQuery();
            if (rs1.next()) {
                Value.setText(rs1.getString("storename"));
                Address.setText(rs1.getString("address"));
                Value2.setText(rs1.getString("email"));
                Value3.setText(rs1.getString("contactNumber"));
                Value4.setText(rs1.getString("TIN"));
                Value5.setText(rs1.getString("owner"));
                try {
                    Blob aBlob = rs1.getBlob("logo");
                    byte[] imageByte = aBlob.getBytes(1, (int) aBlob.length());
                    InputStream is = new ByteArrayInputStream(imageByte);
                    BufferedImage imag = ImageIO.read(is);
                    Image image = imag;
                    Image img = Toolkit.getDefaultToolkit().createImage(imageByte);
                    img.getScaledInstance(200,200,Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(img);
                    jLabel2.setIcon(icon);

                } catch (IOException | SQLException ex) {
                        JOptionPane.showMessageDialog(null, ex);
                }

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            getStoreInfo();
        } catch (IOException ex) {
            Logger.getLogger(StoreInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowOpened

    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        if (Value.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Store name is required. ", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (Address.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Store address is required. ", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (Address.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Store address is required. ", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to update Store?", "Warning", JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                UpdateStore();
            }
        }

    }//GEN-LAST:event_AddActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            selectImage();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoreInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void UpdateStore() {
        try {
            pst = con.prepareStatement("SELECT storename FROM store");
            rs = pst.executeQuery();
            if (rs.next()) {
                //JOptionPane.showMessageDialog(null, "Store is already setup!");
                pst = con.prepareStatement("UPDATE store SET "
                        + "storename = ?,"
                        + "address=?,"
                        + "email=?,"
                        + "contactnumber=?,"
                        + "TIN=?,"
                        + "owner=?,"
                        + "logo=? WHERE storeID !=0 ");
                pst.setString(1, Value.getText());
                pst.setString(2, Address.getText());
                pst.setString(3, Value2.getText());
                pst.setString(4, Value3.getText());
                pst.setString(5, Value4.getText());
                pst.setString(6, Value5.getText());
                pst.setBinaryStream(7, fs, (int) f.length());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Store is successfully updated");
                POSController pc = new POSController();
                pc.setVisible(true);
                dispose();
            } else {
                pst = con.prepareStatement("INSERT INTO store(storename,address,email,contactnumber,TIN,owner,logo)" + "VALUES(?,?,?,?,?,?,?)");
                pst.setString(1, Value.getText().toUpperCase());
                pst.setString(2, Address.getText().toUpperCase());
                pst.setString(3, Value2.getText());
                pst.setString(4, Value3.getText());
                pst.setString(5, Value4.getText());
                pst.setString(6, Value5.getText());
                pst.setBinaryStream(7, fs, (int) f.length());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Store is successfully set up");
                POSController pc = new POSController();
                pc.setVisible(true);
                dispose();
            }
        } catch (HeadlessException | SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

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
            java.util.logging.Logger.getLogger(StoreInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StoreInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StoreInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StoreInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StoreInfo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private javax.swing.JTextField Address;
    private javax.swing.JLabel Note;
    private javax.swing.JLabel Note1;
    private javax.swing.JLabel Note2;
    private javax.swing.JLabel Note3;
    private javax.swing.JLabel Note4;
    private javax.swing.JLabel Note5;
    private javax.swing.JLabel Note6;
    private javax.swing.JTextField Value;
    private javax.swing.JTextField Value2;
    private javax.swing.JTextField Value3;
    private javax.swing.JTextField Value4;
    private javax.swing.JTextField Value5;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables
}
