package barcodeSticker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Toolkit;
import javax.swing.JButton;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.awt.event.ActionEvent;

public class mainForm {

	private JFrame frmBarcodeSticker;
	private JTextField txtCommand;
	private JTable database;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainForm window = new mainForm();
					window.frmBarcodeSticker.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public mainForm() {		
		frmBarcodeSticker = new JFrame();
		frmBarcodeSticker.setResizable(false);
		frmBarcodeSticker.setIconImage(Toolkit.getDefaultToolkit().getImage("icons.png"));
		frmBarcodeSticker.setTitle("Barcode Sticker");
		frmBarcodeSticker.setBounds(100, 100, 415, 349);
		frmBarcodeSticker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBarcodeSticker.getContentPane().setLayout(null);
		
		JLabel commandNumber = new JLabel("หมายเลขคดี");
		commandNumber.setBounds(27, 11, 72, 14);
		frmBarcodeSticker.getContentPane().add(commandNumber);
		commandNumber.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		txtCommand = new JTextField();
		txtCommand.setBounds(22, 35, 152, 20);
		frmBarcodeSticker.getContentPane().add(txtCommand);
		txtCommand.setColumns(10);
		
		JButton btnSearch = new JButton("ค้นหา");
		btnSearch.setBounds(184, 33, 89, 23);
		btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		frmBarcodeSticker.getContentPane().add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PopulateData();
			}
		});
		
		JButton btnPrint = new JButton("พิมพ์บาร์โค้ด");
		btnPrint.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnPrint.setBounds(283, 33, 105, 23);
		frmBarcodeSticker.getContentPane().add(btnPrint);
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<String> barcodeList = new ArrayList<String>();
				for (int i = 0; i < database.getRowCount(); i++) {
					
					Boolean chkBox = Boolean.valueOf(database.getValueAt(i, 0).toString());
					if(chkBox) //Select
					{
						String strBlackRecGen = database.getValueAt(i, 3).toString();
						barcodeList.add(strBlackRecGen);
					}
				}
				PrintBarcode(barcodeList);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 68, 367, 185);
		frmBarcodeSticker.getContentPane().add(scrollPane);
		
		database = new JTable();
		database.setFont(new Font("Tahoma", Font.PLAIN, 12));
		database.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
			    int col = database.columnAtPoint(evt.getPoint());
			    if (col > 0) {
					int index = database.getSelectedRow();
					String ID = database.getValueAt(index, 1).toString();

					updateForm update = new updateForm(ID);
					update.setModal(true);
					update.setVisible(true);

					PopulateData(); // Reload Table
			    }
			}
		});
		scrollPane.setViewportView(database);
		
		JButton btnInsert = new JButton("เพิ่มใหม่");
		btnInsert.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnInsert.setBounds(22, 275, 89, 23);
		frmBarcodeSticker.getContentPane().add(btnInsert);
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InsertForm insert = new InsertForm();
				insert.setModal(true);
				insert.setVisible(true);
				
				PopulateData();
			}
		});
		
		JButton btnDelete = new JButton("ลบ");
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete.setBounds(121, 275, 89, 23);
		frmBarcodeSticker.getContentPane().add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "ใช่", "ไม่" };
				int n = JOptionPane.showOptionDialog(null, "คุณต้องการลบข้อมูลที่เลือกทั้งหมด", "ยืนยันการลบข้อมูล", 
											JOptionPane.YES_NO_CANCEL_OPTION, 
											JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n == 0) // Confirm Delete = Yes
				{
					for (int i = 0; i < database.getRowCount(); i++) {
						Boolean chkDel = Boolean.valueOf(database.getValueAt(i, 0).toString());
						
						if(chkDel)
						{
							String strID = database.getValueAt(i, 1).toString();
							DeleteData(strID); //Delete Data
						}
					}
				}
			}
		});
		
		PopulateData();
		}

	private void PopulateData() {
		
		database.setModel(new DefaultTableModel());
		@SuppressWarnings("serial")
		DefaultTableModel model = new DefaultTableModel() {
			
			public Class<?> getColumnClass(int column) {
				switch (column) {
				case 0:
					return Boolean.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				default:
					return String.class;
				}
			}
		};
		database.setModel(model);
		
		//Add Column
		model.addColumn("เลือก ");
		model.addColumn("ลำดับที่");
		model.addColumn("หมายเลขคดี");
		model.addColumn("รหัส Barcode");
		
		Connection connect = null;
		Statement s = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connect =  DriverManager.getConnection("jdbc:mysql://localhost/GP_LawCourt" +
					"?user=root&password=");
			
			s = connect.createStatement();
			
			String sql = "SELECT * FROM  thCommand " +
					"WHERE CommandNumber like '%" + txtCommand.getText().trim() + "%' " +
					"ORDER BY ID ASC";
			
			ResultSet rec = s.executeQuery(sql);
			int row = 0;
			while((rec!=null) && (rec.next()))
            {			
				model.addRow(new Object[0]);
				model.setValueAt(false, row, 0);
				model.setValueAt(rec.getString("ID"), row, 1);
				model.setValueAt(rec.getString("CommandNumber"), row, 2);
				model.setValueAt(rec.getString("BlackRecGen"), row, 3);
				row++;
            }
             
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ไม่สามารถเชื่อมต่อกับฐานข้อมูลได้");
			e.printStackTrace();
		}
		
		try {
			if(s != null) {
				s.close();
				connect.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void PrintBarcode(ArrayList<String> barcodeList) {
		Connection connect = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect =  DriverManager.getConnection("jdbc:mysql://localhost/GP_LawCourt" +
					"?user=root&password=");
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			// Parameters
			HashMap<String, Object> param;
			param = new HashMap<String, Object>();
			param.put("sBlackRecGen", barcodeList);
			
			// Report Viewer
			InputStream file = getClass().getResourceAsStream("myReport.jasper");
			JasperReport jr = (JasperReport) JRLoader.loadObject(file);
			JasperPrint jp = JasperFillManager.fillReport(jr, param, connect);
			JasperViewer.viewReport(jp, false);
			
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void DeleteData(String strID) {

		Connection connect = null;
		Statement s = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://localhost/GP_LawCourt"
							+ "?user=root&password=");

			s = connect.createStatement();

			String sql = "DELETE FROM thCommand WHERE " + "ID = '"
					+ strID + "' ";
			s.execute(sql);
			
			JOptionPane.showMessageDialog(null, "ลบข้อมูลเสร็จสิ้นแล้ว");
			PopulateData();

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
}
