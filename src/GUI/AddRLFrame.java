package GUI;

import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JEditorPane;
import javax.swing.JTextField;

import RegularLanguages.RegularLanguage;

import javax.swing.DropMode;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddRLFrame {

	private JFrame frmAddRegularLanguage;
	private MainFrame mainFrame = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddRLFrame window = new AddRLFrame();
					window.frmAddRegularLanguage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Hide operations frame.
	 */
	public void hide() {
		if (mainFrame != null) {
			mainFrame.display();
		}
		frmAddRegularLanguage.setVisible(false);
	}

	/**
	 * Create the application.
	 */
	public AddRLFrame() {
		initialize();
	}
	public AddRLFrame(MainFrame mainFrame) {
		try {
			this.mainFrame = mainFrame;
			initialize();
			this.frmAddRegularLanguage.setVisible(true);
			mainFrame.hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAddRegularLanguage = new JFrame();
		frmAddRegularLanguage.setResizable(false);
		frmAddRegularLanguage.setBounds(100, 100, 500, 500);
		frmAddRegularLanguage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAddRegularLanguage.getContentPane().setLayout(null);
		
		JLabel lblAddRLAdd = new JLabel("Enter a Regular Grammar or a Regular Expression below:");
		lblAddRLAdd.setBounds(12, 24, 500, 15);
		frmAddRegularLanguage.getContentPane().add(lblAddRLAdd);
		
		JEditorPane edpAddRL = new JEditorPane();
		edpAddRL.setToolTipText("");
		edpAddRL.setBounds(12, 57, 466, 345);
		frmAddRegularLanguage.getContentPane().add(edpAddRL);
		
		JButton btnAddRLCancel = new JButton("Cancel");
		btnAddRLCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddRLFrame.this.hide();
			}
		});
		btnAddRLCancel.setBounds(286, 426, 90, 30);
		frmAddRegularLanguage.getContentPane().add(btnAddRLCancel);
		
		JButton btnAddRLAdd = new JButton("Add");
		btnAddRLAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = edpAddRL.getText(); // Gets text from pane
				RegularLanguage l = RegularLanguage.validate(input); // Gets RL object
				RegularLanguage.InputType lType = l.getType(); // Gets RL type
				if(lType.equals(RegularLanguage.InputType.UNDEFINED)) { // If type is not valid
					JOptionPane.showMessageDialog(AddRLFrame.this.frmAddRegularLanguage, "Invalid input!");
				}
				
				// add Regular Language to Main Panel
				AddRLFrame.this.mainFrame.addToPanel(l);
				AddRLFrame.this.hide();
			}
		});
		btnAddRLAdd.setBounds(388, 426, 90, 30);
		frmAddRegularLanguage.getContentPane().add(btnAddRLAdd);
		
		edpAddRL.setToolTipText("<html>RE format.: a*(b?c|d)*<br>"
				+ "RG format: S->aS|b</html>");
	}
}
