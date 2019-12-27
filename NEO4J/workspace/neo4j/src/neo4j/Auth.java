package neo4j;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Auth extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private JTextField txtNeoj;
	private JTextField textField;

	/**
	 * Create the dialog.
	 */
	public Auth(MainWindow mw) {
		setBounds(100, 100, 450, 300);
		
		JLabel lblAuthentication = new JLabel("Authentication");
		lblAuthentication.setHorizontalAlignment(SwingConstants.CENTER);
		lblAuthentication.setFont(new Font("Cambria Math", Font.BOLD, 30));
		getContentPane().add(lblAuthentication, BorderLayout.NORTH);
		
		JPanel pnCenter = new JPanel();
		getContentPane().add(pnCenter, BorderLayout.CENTER);
		pnCenter.setLayout(new GridLayout(0, 1, 0, 0));
		
		JPanel pnUser = new JPanel();
		pnCenter.add(pnUser);
		
		JLabel lblUser = new JLabel("User:");
		lblUser.setFont(new Font("Cambria Math", Font.PLAIN, 15));
		pnUser.add(lblUser);
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		
		txtNeoj = new JTextField();
		txtNeoj.setFont(new Font("Cambria Math", Font.PLAIN, 15));
		txtNeoj.setHorizontalAlignment(SwingConstants.CENTER);
		pnUser.add(txtNeoj);
		txtNeoj.setText("neo4j");
		txtNeoj.setColumns(10);
		
		JPanel pnPassword = new JPanel();
		pnCenter.add(pnPassword);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Cambria Math", Font.PLAIN, 15));
		pnPassword.add(lblPassword);
		
		textField = new JTextField();
		textField.setFont(new Font("Cambria Math", Font.PLAIN, 15));
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		pnPassword.add(textField);
		textField.setColumns(10);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		pnCenter.add(rigidArea);
		
		Component rigidArea_1 = Box.createRigidArea(new Dimension(20, 20));
		pnCenter.add(rigidArea_1);
		
		JButton btnAccept = new JButton("Accept");
		getContentPane().add(btnAccept, BorderLayout.SOUTH);
		btnAccept.addActionListener(new ActionListener() {
		
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mw.auth(txtNeoj.getText(),textField.getText());
			}
		});
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(-1);
			}
			
		});
		
		this.getRootPane().setDefaultButton(btnAccept);
	}

}
