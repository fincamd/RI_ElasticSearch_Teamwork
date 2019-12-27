package neo4j;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblNeojApplication;
	private JPanel pnQueries;
	private JLabel lblChooseAQuery;
	private JPanel pnMain;
	private JScrollPane scTable;
	private JTable tbQueries;
	private JPanel pnResult;
	private JScrollPane scText;
	private JTextArea taResults;
	private JButton btnQuery;
	private DefaultTableModel tbModel;
	private Program program;
	private Auth auth;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(MAXIMIZED_BOTH);
		contentPane = new JPanel();
		showAuth();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(getLblNeojApplication(), BorderLayout.NORTH);
		contentPane.add(getPnMain(), BorderLayout.CENTER);
		getRootPane().setDefaultButton(btnQuery);
		
	}

	private JLabel getLblNeojApplication() {
		if (lblNeojApplication == null) {
			lblNeojApplication = new JLabel("Neo4J Application");
			lblNeojApplication.setFont(new Font("Cambria Math", Font.BOLD, 30));
			lblNeojApplication.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblNeojApplication;
	}

	private JPanel getPnQueries() {
		if (pnQueries == null) {
			pnQueries = new JPanel();
			pnQueries.setLayout(new BorderLayout(0, 0));
			pnQueries.add(getLblChooseAQuery(), BorderLayout.NORTH);
			pnQueries.add(getScTable(), BorderLayout.CENTER);
		}
		return pnQueries;
	}

	private JLabel getLblChooseAQuery() {
		if (lblChooseAQuery == null) {
			lblChooseAQuery = new JLabel("Choose a Query to be performed and click \"query\"");
			lblChooseAQuery.setFont(new Font("Cambria Math", Font.BOLD, 15));
			lblChooseAQuery.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblChooseAQuery;
	}

	private JPanel getPnMain() {
		if (pnMain == null) {
			pnMain = new JPanel();
			pnMain.setLayout(new GridLayout(2, 0, 0, 0));
			pnMain.add(getPnQueries());
			pnMain.add(getPnResult());
		}
		return pnMain;
	}

	private JScrollPane getScTable() {
		if (scTable == null) {
			scTable = new JScrollPane();
			scTable.setViewportView(getTbQueries());
		}
		return scTable;
	}

	private JTable getTbQueries() {
		if (tbQueries == null) {
			tbModel = new DefaultTableModel(new String[] { "ID", "Description" }, 0);
			tbQueries = new JTable(tbModel) {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}

			};

			for (String[] row : program.getQueries()) {
				tbModel.addRow(row);
			}

			tbQueries.getColumnModel().getColumn(0).setMaxWidth(30);
			tbQueries.setRowHeight(30);
			tbQueries.setFont(new Font("Cambria Math", Font.PLAIN, 20));
		}
		return tbQueries;
	}

	private JPanel getPnResult() {
		if (pnResult == null) {
			pnResult = new JPanel();
			pnResult.setLayout(new BorderLayout(0, 0));
			pnResult.add(getScText(), BorderLayout.CENTER);
			pnResult.add(getBtnQuery(), BorderLayout.NORTH);
		}
		return pnResult;
	}

	private JScrollPane getScText() {
		if (scText == null) {
			scText = new JScrollPane();
			scText.setBorder(new TitledBorder(null, "Results", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			scText.setViewportView(getTaResults());
		}
		return scText;
	}

	private JTextArea getTaResults() {
		if (taResults == null) {
			taResults = new JTextArea();
			taResults.setFont(new Font("Cambria Math", Font.PLAIN, 16));
			taResults.setEditable(false);
		}
		return taResults;
	}

	private JButton getBtnQuery() {
		if (btnQuery == null) {
			btnQuery = new JButton("Query");
			btnQuery.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					query();
				}
			});
		}
		return btnQuery;
	}

	private void query() {
		int row = tbQueries.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "No query is selected!!");
		} else {
			program.processOption(tbModel.getValueAt(row, 0).toString());
		}
	}
	
	public void setResult(String str) {
		taResults.setText(str);
	}

	public void auth(String user, String password) {
		try {
			program = new Program(user, password,this);
		}catch(RuntimeException e) {
			JOptionPane.showMessageDialog(this, "Couldn't connect to the database");
			showAuth();
		}
		auth.dispose();
	}

	private void showAuth() {
		if(auth!=null) {
			auth.dispose();
		}
		auth = new Auth(this);
		auth.setModal(true);
		auth.setLocationRelativeTo(this);
		auth.setVisible(true);
		
		
	}
}
