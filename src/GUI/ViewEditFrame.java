package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class ViewEditFrame {

	private JFrame frmRegularLanguagesOperations;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewEditFrame window = new ViewEditFrame();
					window.frmRegularLanguagesOperations.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ViewEditFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRegularLanguagesOperations = new JFrame();
		frmRegularLanguagesOperations.setTitle("View and Edit Regular Languages");
		frmRegularLanguagesOperations.setResizable(false);
		frmRegularLanguagesOperations.setBounds(100, 100, 500, 500);
		frmRegularLanguagesOperations.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRegularLanguagesOperations.getContentPane().setLayout(null);
		
		JRadioButton rdbtnViewEditRE = new JRadioButton("Regular Expression");
		rdbtnViewEditRE.setBounds(8, 77, 165, 23);
		frmRegularLanguagesOperations.getContentPane().add(rdbtnViewEditRE);
		
		JRadioButton rdbtnViewEditFA = new JRadioButton("Finite Automata");
		rdbtnViewEditFA.setBounds(182, 77, 145, 23);
		frmRegularLanguagesOperations.getContentPane().add(rdbtnViewEditFA);
		
		JRadioButton rdbtnViewEditRG = new JRadioButton("Regular Grammar");
		rdbtnViewEditRG.setBounds(330, 77, 170, 23);
		frmRegularLanguagesOperations.getContentPane().add(rdbtnViewEditRG);
		
		JEditorPane edpViewEditRE = new JEditorPane();
		edpViewEditRE.setBounds(24, 108, 141, 268);
		frmRegularLanguagesOperations.getContentPane().add(edpViewEditRE);
		
		JEditorPane edpViewEditFA = new JEditorPane();
		edpViewEditFA.setBounds(176, 108, 141, 268);
		frmRegularLanguagesOperations.getContentPane().add(edpViewEditFA);
		
		JEditorPane edpViewEditRG = new JEditorPane();
		edpViewEditRG.setBounds(328, 108, 141, 268);
		frmRegularLanguagesOperations.getContentPane().add(edpViewEditRG);
		
		JButton btnViewEditSave = new JButton("Save");
		btnViewEditSave.setBounds(356, 429, 117, 25);
		frmRegularLanguagesOperations.getContentPane().add(btnViewEditSave);
		
		JButton btnViewEditCancel = new JButton("Cancel");
		btnViewEditCancel.setVerticalAlignment(SwingConstants.BOTTOM);
		btnViewEditCancel.setBounds(234, 429, 117, 25);
		frmRegularLanguagesOperations.getContentPane().add(btnViewEditCancel);
	}
}
