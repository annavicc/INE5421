package GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import RegularLanguages.RegularExpression;
import RegularLanguages.RegularLanguage;

public class ViewEditFrame {

	private MainFrame mainFrame;
	private JEditorPane edpViewEditRE, edpViewEditRG;
	private JFrame frmRegularLanguagesOperations;


	/**
	 * Exit back to main frame
	 */
	public void exit() {
		mainFrame.setVisible(true);
		this.frmRegularLanguagesOperations.dispose();
	}
	
	/**
	 * Create the application.
	 */
	public ViewEditFrame() {
		initialize();
	}
	
	/**
	 * Create the application.
	 */
	public ViewEditFrame(MainFrame mainFrame, RegularLanguage language) {
		this.mainFrame = mainFrame;
		initialize();
		edpViewEditRG.setText(language.getRG().toString());
		RegularExpression re = language.getRE();
		if (re != null) {
			edpViewEditRE.setText(re.toString());
		}
		
		this.frmRegularLanguagesOperations.setVisible(true);
		mainFrame.setVisible(false);
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
		
		edpViewEditRE = new JEditorPane();
		edpViewEditRE.setBounds(24, 108, 141, 268);
		frmRegularLanguagesOperations.getContentPane().add(edpViewEditRE);
		
		JEditorPane edpViewEditFA = new JEditorPane();
		edpViewEditFA.setBounds(176, 108, 141, 268);
		frmRegularLanguagesOperations.getContentPane().add(edpViewEditFA);
		
		edpViewEditRG = new JEditorPane();
		edpViewEditRG.setBounds(328, 108, 141, 268);
		frmRegularLanguagesOperations.getContentPane().add(edpViewEditRG);
		
		JButton btnViewEditSave = new JButton("Save");
		btnViewEditSave.setBounds(356, 429, 117, 30);
		frmRegularLanguagesOperations.getContentPane().add(btnViewEditSave);
		
		JButton btnViewEditCancel = new JButton("Cancel");
		btnViewEditCancel.setVerticalAlignment(SwingConstants.BOTTOM);
		btnViewEditCancel.setBounds(234, 429, 117, 30);
		btnViewEditCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ViewEditFrame.this.exit();
			}
		});
		
		frmRegularLanguagesOperations.getContentPane().add(btnViewEditCancel);
	}
}
