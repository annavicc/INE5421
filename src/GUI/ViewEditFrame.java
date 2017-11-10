package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import RegularLanguages.RegularLanguage;
import RegularLanguages.RegularLanguage.InputType;

public class ViewEditFrame extends JFrame{
	
	// Auto-generated UID
	private static final long serialVersionUID = -3107653144045018225L;
	
	private MainFrame mainFrame;
	private JScrollPane scpViewEditRE, scpViewEditRG, scpViewEditFA;
	private JTextArea txtaViewEditRE, txtaViewEditRG, txtaViewEditFA;
	private JTabbedPane viewEditTabbedPane;
	
	private RegularLanguage language = null;
	
	static final String strRE = "Regular Expression";
	static final String strFA = "Finite Automata";
	static final String strRG = "Regular Grammar";
	


	/**
	 * Exit back to main frame
	 */
	public void exit() {
		mainFrame.setVisible(true);
		this.dispose();
	}

	/**
	 * Create the application.
	 */
	public ViewEditFrame(MainFrame f, RegularLanguage l) {
		try {
			this.mainFrame = f;
			this.language = l;
			initialize();
			this.setVisible(true);
			mainFrame.setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		if (this.language == null ) {
			this.setTitle("View and Edit Regular Languages");
		} else {
			this.setTitle("View and Edit - " + this.language.toString());
		}
//		this.setResizable(false);
		this.setBounds(100, 100, 500, 500);
		this.setMinimumSize(new Dimension(475, 400));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Tabbed scrollable JEditorPanes:
		
		viewEditTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		txtaViewEditRE = new JTextArea();
		scpViewEditRE = new JScrollPane(txtaViewEditRE);
		viewEditTabbedPane.addTab(strRE, null, scpViewEditRE, null);
		
		txtaViewEditFA = new JTextArea();
		txtaViewEditFA.setFont(new Font("monospaced", Font.PLAIN, 14));
		txtaViewEditFA.setEditable(false);
		scpViewEditFA = new JScrollPane(txtaViewEditFA);
		viewEditTabbedPane.addTab(strFA, null, scpViewEditFA, null);
		
		txtaViewEditRG = new JTextArea();
		scpViewEditRG = new JScrollPane(txtaViewEditRG);
		viewEditTabbedPane.addTab(strRG, null, scpViewEditRG, null);
		
		
		enableTextPane();
		
		// JButtons:
		
		JButton btnViewEditSave = new JButton("Save");
		btnViewEditSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = getPaneText(); // Gets text from pane
				String type;
				RegularLanguage l; // Gets RL object
				if (input == null) {
					l = ViewEditFrame.this.language.getFA();
					type = strFA;
				}
				else {
					l = RegularLanguage.validate(input);
					if(l == null) { // If type is not valid
						JOptionPane.showMessageDialog(ViewEditFrame.this, "Invalid input!");
						return;
					}
					if (l.getType() == InputType.RG) {
						type = strRG;
					} else {
						type = strRE;
					}
				}
				l.setId(language.toString());
				
				int answer = JOptionPane.showConfirmDialog(
						ViewEditFrame.this,
						"Replace '" + language.toString()+ "' by this new " + type + "?",
						"Overwrite?",
						JOptionPane.YES_NO_OPTION
				);
				if (answer != JOptionPane.YES_OPTION) {
					return;
				}
				// add Regular Language to Main Panel
				ViewEditFrame.this.mainFrame.addToPanel(l);
				ViewEditFrame.this.exit();
				
			}
		});
		
		JButton btnViewEditCancel = new JButton("Cancel");
		btnViewEditCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ViewEditFrame.this.exit();
			}
		});
		btnViewEditCancel.setVerticalAlignment(SwingConstants.BOTTOM);
		btnViewEditCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ViewEditFrame.this.exit();
			}
		});
		
		// Close Window action:

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ViewEditFrame.this.exit();
			}
		});

		// Layout definitions:
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(viewEditTabbedPane)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnViewEditCancel, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnViewEditSave, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(12)
					.addComponent(viewEditTabbedPane, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnViewEditSave, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnViewEditCancel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}
	
//	private void setRadioButtonListeners() {
//	}
	
	
	private void enableTextPane() {
		RegularLanguage.InputType type = language.getType();
		if (type.equals(RegularLanguage.InputType.RG)) {
			viewEditTabbedPane.setSelectedComponent(scpViewEditRG);
			viewEditTabbedPane.setEnabledAt(0, false);
		} else if (type.equals(RegularLanguage.InputType.RE)) {
			viewEditTabbedPane.setSelectedComponent(scpViewEditRE);
			txtaViewEditRE.setText(language.getRE().getDefinition());
			txtaViewEditRE.setCaretPosition(0);
		} else {
			viewEditTabbedPane.setSelectedComponent(scpViewEditFA);
			viewEditTabbedPane.setEnabledAt(0, false);
		}
		txtaViewEditRG.setText(language.getRG().getDefinition());
		txtaViewEditRG.setCaretPosition(0);
		txtaViewEditFA.setText(language.getFA().getDefinition());
		txtaViewEditFA.setCaretPosition(0);
	}
	private String getPaneText() {
		int selectedTabIndex = viewEditTabbedPane.getSelectedIndex();
		String selectedTab = viewEditTabbedPane.getTitleAt(selectedTabIndex);
		if (selectedTab.equals(strRG)) {
			return txtaViewEditRG.getText();
		} else if (selectedTab.equals(strRE)) {
			return txtaViewEditRE.getText();
		} else {
			return null;
		}
	}

}