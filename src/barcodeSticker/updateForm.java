package barcodeSticker;

import javax.swing.JDialog;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class updateForm extends JDialog {
	private JTextField txtCommandNumber;
	private JTextField txtBlackRecGen;
	
	/**
	 * Create the dialog.
	 */
	public updateForm(String sID) {
		getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
		getContentPane().setLayout(null);
		setTitle("\u0E02\u0E49\u0E2D\u0E21\u0E39\u0E25");
		setBounds(100, 100, 371, 328);
		
		JLabel label = new JLabel("\u0E23\u0E32\u0E22\u0E25\u0E30\u0E40\u0E2D\u0E35\u0E22\u0E14");
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds(143, 11, 72, 14);
		getContentPane().add(label);
		
		JLabel ID = new JLabel("\u0E25\u0E33\u0E14\u0E31\u0E1A\u0E17\u0E35\u0E48 :");
		ID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ID.setBounds(52, 61, 46, 14);
		getContentPane().add(ID);
		
		JLabel CommandNumber = new JLabel("\u0E2B\u0E21\u0E32\u0E22\u0E40\u0E25\u0E02\u0E04\u0E14\u0E35 :");
		CommandNumber.setFont(new Font("Tahoma", Font.PLAIN, 12));
		CommandNumber.setBounds(52, 86, 68, 14);
		getContentPane().add(CommandNumber);
		
		JLabel Barcode = new JLabel("\u0E23\u0E2B\u0E31\u0E2A Barcode :");
		Barcode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Barcode.setBounds(52, 117, 76, 14);
		getContentPane().add(Barcode);
		
		JLabel txtID = new JLabel("txtID");
		txtID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtID.setBounds(235, 61, 28, 14);
		getContentPane().add(txtID);
		
		txtCommandNumber = new JTextField("");
		txtCommandNumber.setBounds(201, 84, 101, 20);
		getContentPane().add(txtCommandNumber);
		txtCommandNumber.setColumns(10);
		
		txtBlackRecGen = new JTextField("");
		txtBlackRecGen.setColumns(10);
		txtBlackRecGen.setBounds(201, 115, 101, 20);
		getContentPane().add(txtBlackRecGen);
		
		JButton btnSave = new JButton("\u0E15\u0E01\u0E25\u0E07");
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSave.setBounds(86, 244, 89, 23);
		getContentPane().add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveData(txtID.getText(), txtCommandNumber.getText(), txtBlackRecGen.getText());
				dispose();
			}
		});
		
		JButton btnClose = new JButton("\u0E1B\u0E34\u0E14");
		btnClose.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnClose.setBounds(185, 244, 89, 23);
		getContentPane().add(btnClose);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		// ***Bind Data ***//
		Connection connect = null;
		Statement s = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/GP_LawCourt"
							+ "?user=root&password=");

			s = connect.createStatement();
			String sql = "SELECT * FROM  thCommand " + "WHERE ID = '" + sID + "' ";

			ResultSet rec = s.executeQuery(sql);

			if (rec != null) {
				rec.next();
				txtID.setText(rec.getString("ID"));
				txtCommandNumber.setText(rec.getString("CommandNumber"));
				txtBlackRecGen.setText(rec.getString("BlackRecGen"));
				
				Barcode barcode = BarcodeFactory.create3of9(txtBlackRecGen.getText(), false);
				JLabel barcodePic = new JLabel("");
				barcodePic.setBounds(86, 146, 182, 87);
				getContentPane().add(barcodePic);
				barcodePic.add(barcode);
				
			}
			rec.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}

		try {
			if (s != null) {
				s.close();
				connect.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	// Update
	private void SaveData(String strID, String strCommandNumber, String strBlackRecGen) {
		
		Connection connect = null;
		Statement s = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connect =  DriverManager.getConnection("jdbc:mysql://localhost/GP_LawCourt" +
					"?user=root&password=");
			s = connect.createStatement();
			
			String sql = "UPDATE thCommand " +
					"SET CommandNumber = '" + strCommandNumber + "' " +
					", BlackRecGen = '" + strBlackRecGen + "' " +
					" WHERE ID = '"+strID+"' ";
             s.execute(sql);
            
     		JOptionPane.showMessageDialog(null, "แก้ไขข้อมูลเรียบร้อยแล้ว");
             
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
		
		try {
			if(s != null) {
				s.close();
				connect.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
