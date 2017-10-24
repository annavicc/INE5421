package GUI;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import RegularLanguages.RegularLanguage;

public class ViewEditFrame {

	private JRadioButton rdbtnViewEditRE;
	private JRadioButton rdbtnViewEditRG;
	private JRadioButton rdbtnViewEditFA;
	private JEditorPane edpViewEditRE;
	private JEditorPane edpViewEditRG;
	private JEditorPane edpViewEditFA;
	
	private JFrame frmRegularLanguagesViewEdit;
	private RegularLanguage language = null;
	private MainFrame mainFrame = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewEditFrame window = new ViewEditFrame();
					window.frmRegularLanguagesViewEdit.setVisible(true);
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
		frmRegularLanguagesViewEdit.setVisible(false);
	}

	/**
	 * Create the application.
	 */
	public ViewEditFrame() {
		initialize();
	}
	
	public ViewEditFrame(MainFrame f, RegularLanguage l) {
		try {
			this.mainFrame = f;
			this.language = l;
			initialize();
			this.frmRegularLanguagesViewEdit.setVisible(true);
			mainFrame.hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRegularLanguagesViewEdit = new JFrame();
		frmRegularLanguagesViewEdit.setTitle("View and Edit Regular Languages");
		frmRegularLanguagesViewEdit.setResizable(false);
		frmRegularLanguagesViewEdit.setBounds(100, 100, 500, 500);
		frmRegularLanguagesViewEdit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRegularLanguagesViewEdit.getContentPane().setLayout(null);
		
		rdbtnViewEditRE = new JRadioButton("Regular Expression");
		rdbtnViewEditRE.setBounds(8, 77, 165, 23);
		frmRegularLanguagesViewEdit.getContentPane().add(rdbtnViewEditRE);

		rdbtnViewEditFA = new JRadioButton("Finite Automata");
		rdbtnViewEditFA.setBounds(182, 77, 145, 23);
		frmRegularLanguagesViewEdit.getContentPane().add(rdbtnViewEditFA);
		
		rdbtnViewEditRG = new JRadioButton("Regular Grammar");
		rdbtnViewEditRG.setBounds(330, 77, 170, 23);
		frmRegularLanguagesViewEdit.getContentPane().add(rdbtnViewEditRG);
		
		edpViewEditRE = new JEditorPane();
		edpViewEditRE.setBounds(24, 108, 141, 268);
		frmRegularLanguagesViewEdit.getContentPane().add(edpViewEditRE);
		
		edpViewEditFA = new JEditorPane();
		edpViewEditFA.setBounds(176, 108, 141, 268);
		frmRegularLanguagesViewEdit.getContentPane().add(edpViewEditFA);
		
		edpViewEditRG = new JEditorPane();
		edpViewEditRG.setBounds(328, 108, 141, 268);
		frmRegularLanguagesViewEdit.getContentPane().add(edpViewEditRG);
		
		
		ButtonGroup editableGroup = new ButtonGroup();
		editableGroup.add(rdbtnViewEditFA);
		editableGroup.add(rdbtnViewEditRE);
		editableGroup.add(rdbtnViewEditRG);
		setRadioButtonListeners();
		enableTextPane();
		
		JButton btnViewEditSave = new JButton("Save");
		btnViewEditSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame currentFrame = ViewEditFrame.this.frmRegularLanguagesViewEdit;
				String input = getPaneText(); // Gets text from pane
				RegularLanguage l = RegularLanguage.validate(input); // Gets RL object
				l.setId(language.toString());
				RegularLanguage.InputType lType = l.getType(); // Gets RL type
				if(lType.equals(RegularLanguage.InputType.UNDEFINED)) { // If type is not valid
					JOptionPane.showMessageDialog(currentFrame, "Invalid input!");
					return;
				}
				// add Regular Language to Main Panel
				ViewEditFrame.this.mainFrame.addToPanel(l);
				ViewEditFrame.this.hide();
				
			}
		});
		btnViewEditSave.setBounds(356, 429, 117, 30);
		frmRegularLanguagesViewEdit.getContentPane().add(btnViewEditSave);
		
		JButton btnViewEditCancel = new JButton("Cancel");
		btnViewEditCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ViewEditFrame.this.hide();
			}
		});
		btnViewEditCancel.setVerticalAlignment(SwingConstants.BOTTOM);
		btnViewEditCancel.setBounds(234, 429, 117, 30);
		frmRegularLanguagesViewEdit.getContentPane().add(btnViewEditCancel);
	}
	
	private void setRadioButtonListeners() {
		rdbtnViewEditRG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearPanes();
				edpViewEditRG.setText(language.getDefinition());
				edpViewEditRG.setEnabled(true);
				edpViewEditRE.setEnabled(false);
				edpViewEditFA.setEnabled(false);
			}
		});
		
		rdbtnViewEditRE.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearPanes();
				edpViewEditRE.setText(language.getDefinition());
				edpViewEditRE.setEnabled(true);
				edpViewEditRG.setEnabled(false);
				edpViewEditFA.setEnabled(false);
			}
		});
		
		rdbtnViewEditFA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearPanes();
				edpViewEditFA.setText(language.getDefinition());
				edpViewEditRG.setEnabled(false);
				edpViewEditRE.setEnabled(false);
				edpViewEditFA.setEditable(false);
			}
		});
	}
	
	
	private void enableTextPane() {
		RegularLanguage.InputType type = language.getType();
		if (type.equals(RegularLanguage.InputType.RG)) {
			rdbtnViewEditRG.setSelected(true);
			edpViewEditRG.setText(language.getDefinition());
		} else if (type.equals(RegularLanguage.InputType.RE)) {
			rdbtnViewEditRE.setSelected(true);
			edpViewEditRE.setText(language.getDefinition());
		} else {
			rdbtnViewEditFA.setSelected(true);
			edpViewEditFA.setText(language.getDefinition());
		}
	}
	private String getPaneText() {
		if (rdbtnViewEditRG.isSelected()) {
			return edpViewEditRG.getText();
		} else if (rdbtnViewEditRE.isSelected()) {
			return edpViewEditRE.getText();
		} else {
			return edpViewEditFA.getText();
		}
	}
	private void clearPanes() {
		edpViewEditRG.setText("");
		edpViewEditRE.setText("");
		edpViewEditFA.setText("");
	}
}
