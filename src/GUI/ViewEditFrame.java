package GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import RegularLanguages.RegularLanguage;

public class ViewEditFrame extends JFrame{
	
	private MainFrame mainFrame;
	private JEditorPane edpViewEditRE, edpViewEditRG, edpViewEditFA;
	private JScrollPane scpViewEditRE, scpViewEditRG, scpViewEditFA;
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
		this.setTitle("View and Edit Regular Languages");
//		this.setResizable(false);
		this.setBounds(100, 100, 500, 500);
		this.setMinimumSize(new Dimension(475, 400));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Tabbed scrollable JEditorPanes:
		
		viewEditTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		edpViewEditRE = MainFrame.newScrollableEditorPane();
		scpViewEditRE = new JScrollPane();
		scpViewEditRE.setViewportView(edpViewEditRE);
		viewEditTabbedPane.addTab(strRE, null, scpViewEditRE, null);
		
		edpViewEditFA = MainFrame.newScrollableEditorPane();
		scpViewEditFA = new JScrollPane();
		scpViewEditFA.setViewportView(edpViewEditFA);
		viewEditTabbedPane.addTab(strFA, null, scpViewEditFA, null);
		
		edpViewEditRG = MainFrame.newScrollableEditorPane();
		scpViewEditRG = new JScrollPane();
		scpViewEditRG.setViewportView(edpViewEditRG);
		viewEditTabbedPane.addTab(strRG, null, scpViewEditRG, null);
		
		enableTextPane();
		
		// JButtons:
		
		JButton btnViewEditSave = new JButton("Save");
		btnViewEditSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = getPaneText(); // Gets text from pane
				RegularLanguage l = RegularLanguage.validate(input); // Gets RL object
				if(l == null) { // If type is not valid
					JOptionPane.showMessageDialog(ViewEditFrame.this, "Invalid input!");
					return;
				}
				l.setId(language.toString());
//				// add Regular Language to Main Panel
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
			edpViewEditRG.setText(language.getDefinition());
		} else if (type.equals(RegularLanguage.InputType.RE)) {
			viewEditTabbedPane.setSelectedComponent(edpViewEditRE);
			edpViewEditRE.setText(language.getDefinition());
		} else {
			viewEditTabbedPane.setSelectedComponent(edpViewEditFA);
			edpViewEditFA.setText(language.getDefinition());
		}
	}
	private String getPaneText() {
		int selectedTabIndex = viewEditTabbedPane.getSelectedIndex();
		String selectedTab = viewEditTabbedPane.getTitleAt(selectedTabIndex);
		if (selectedTab.equals(strRG)) {
			return edpViewEditRG.getText();
		} else if (selectedTab.equals(strRE)) {
			return edpViewEditRE.getText();
		} else {
			return edpViewEditFA.getText();
		}
	}

}
