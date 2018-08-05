package barcodeSticker;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;


public class InsertForm extends JDialog {
	private JTextField txtID;
	private JTextField txtCommandNumber;
	private JTextField txtBlackRecGen;
	
	/**
	 * Create the dialog.
	 */
	public InsertForm() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\hipho\\eclipse-workspace\\barcodeSticker\\lib\\icons.png"));
		setTitle("เพิ่มข้อมูล");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 385, 266);
		setResizable(false);
		getContentPane().setLayout(null);
		
		// Header Insert Data
		JLabel insert = new JLabel("\u0E40\u0E1E\u0E34\u0E48\u0E21\u0E02\u0E49\u0E2D\u0E21\u0E39\u0E25");
		insert.setFont(new Font("Tahoma", Font.BOLD, 14));
		insert.setBounds(162, 11, 62, 17);
		getContentPane().add(insert);
		
		// *** Header *** //
		JLabel ID = new JLabel("\u0E25\u0E33\u0E14\u0E31\u0E1A\u0E17\u0E35\u0E48 :");
		ID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ID.setBounds(70, 60, 53, 14);
		getContentPane().add(ID);
		
		JLabel CommandNumber = new JLabel("\u0E2B\u0E21\u0E32\u0E22\u0E40\u0E25\u0E02\u0E04\u0E14\u0E35 :");
		CommandNumber.setFont(new Font("Tahoma", Font.PLAIN, 12));
		CommandNumber.setBounds(70, 96, 68, 14);
		getContentPane().add(CommandNumber);
		
		JLabel Barcode = new JLabel("\u0E23\u0E2B\u0E31\u0E2A Barcode :");
		Barcode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Barcode.setBounds(70, 132, 76, 14);
		getContentPane().add(Barcode);
		
		// *** Insert Form ***//
		// ID
		txtID = new JTextField();
		txtID.setBounds(201, 58, 117, 20);
		getContentPane().add(txtID);
		txtID.setColumns(10);
		
		// Command Number
		txtCommandNumber = new JTextField();
		txtCommandNumber.setColumns(10);
		txtCommandNumber.setBounds(201, 94, 117, 20);
		getContentPane().add(txtCommandNumber);
		
		// BlackRecGen
		txtBlackRecGen = new JTextField();
		txtBlackRecGen.setColumns(10);
		txtBlackRecGen.setBounds(201, 130, 117, 20);
		getContentPane().add(txtBlackRecGen);
		
		// Save Button
		JButton btnSave = new JButton("\u0E1A\u0E31\u0E19\u0E17\u0E36\u0E01");
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSave.setBounds(93, 183, 89, 23);
		getContentPane().add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				Connection connect = null;
				Statement statement = null;
				
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");

					connect = DriverManager.getConnection("jdbc:mysql://localhost/GP_LawCourt"
							+ "?user=root&password=");

					statement = connect.createStatement();
					
					String sql = "INSERT INTO thCommand "
							+ "(ID, CommandNumber, BlackRecGen) "
							+ "VALUES ('" + txtID.getText() + "','"
							+ txtCommandNumber.getText() + "','"
							+ txtBlackRecGen.getText() + "') ";
					statement.execute(sql);
					
					// Reset Text Fields
					txtID.setText("");
					txtCommandNumber.setText("");
					txtBlackRecGen.setText("");
					
					JOptionPane.showMessageDialog(null, "บันทึกข้อมูลเสร็จสิ้นแล้ว");

				} catch (Exception ex) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, ex.getMessage());
					ex.printStackTrace();
				}

				try {
					if (statement != null) {
						statement.close();
						connect.close();
					}
				} catch (SQLException ex) {
					// TODO Auto-generated catch block
					System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
			}
		});
		
		// Cancel Button
		JButton btnCancel = new JButton("\u0E22\u0E01\u0E40\u0E25\u0E34\u0E01");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.setBounds(201, 183, 89, 23);
		getContentPane().add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		
	}
}
