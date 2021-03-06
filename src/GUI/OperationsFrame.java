package GUI;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import RegularLanguages.FiniteAutomata;
import RegularLanguages.RegularLanguage;
import RegularLanguages.Operators.FAOperator;

public class OperationsFrame extends JFrame {

	// Auto-generated UID
	private static final long serialVersionUID = -1372510623291211881L;
	
	private JComboBox<RegularLanguage> cbOpRL1;
	private JComboBox<RegularLanguage> cbOpRL2;
	private MainFrame mainFrame = null;
	
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
	public OperationsFrame(MainFrame mainFrame) {
		try {
			this.mainFrame = mainFrame;
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
		this.setTitle("Regular Languages Operations");
		this.setResizable(false);
		this.setBounds(100, 200, 750, 190);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel operationsFramePanel = new JPanel();
		this.getContentPane().add(operationsFramePanel, BorderLayout.CENTER);
		
		// JComboBoxes:
		cbOpRL1 = new JComboBox<RegularLanguage>();
		cbOpRL2 = new JComboBox<RegularLanguage>();
		
		JComboBox<String> cbOpOperations = new JComboBox<String>();
		cbOpOperations.addItem("Union");
		cbOpOperations.addItem("Complement");
		cbOpOperations.addItem("Intersection");
		cbOpOperations.addItem("Minimize FA");
		cbOpOperations.addItem("Determinize FA");
		cbOpOperations.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	String selected = String.valueOf(cbOpOperations.getSelectedItem());
		    	if (selected.equals("Union") || selected.equals("Intersection")) {
		    		cbOpRL2.setEnabled(true);
		    	} else {
		    		cbOpRL2.setEnabled(false);
		    	}
		    }
		});
		
		// JLabels:
		
		JLabel lbOpSelectRL1 = new JLabel("Select RL 1");
		JLabel lbOpSelectOp = new JLabel("Select Operation");
		JLabel lbOpSelectRL2 = new JLabel("Select RL 2");
		
		// JButtons:
		
		JButton btnOpCancel = new JButton("Cancel");
		btnOpCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperationsFrame.this.exit();
			}
		});
		
		JButton btnOpSave = new JButton("Save");
		btnOpSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegularLanguage rl1 = (RegularLanguage) cbOpRL1.getSelectedItem();
				RegularLanguage rl2 = (RegularLanguage) cbOpRL2.getSelectedItem();
				String operation = String.valueOf(cbOpOperations.getSelectedItem());
				if (saveOperation(operation, rl1, rl2)) {
					OperationsFrame.this.exit();
				}
			}
		});
		
//		JButton btnOpView = new JButton("View");
		
		// Close Window action:
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				OperationsFrame.this.exit();
			}
		});
	
		// Layout definitions:
		
		GroupLayout gl_operationsFramePanel = new GroupLayout(operationsFramePanel);
		gl_operationsFramePanel.setHorizontalGroup(
			gl_operationsFramePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_operationsFramePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_operationsFramePanel.createSequentialGroup()
							.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_operationsFramePanel.createSequentialGroup()
									.addComponent(cbOpRL1, 0, 283, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED))
								.addGroup(gl_operationsFramePanel.createSequentialGroup()
									.addGap(6)
									.addComponent(lbOpSelectRL1)
									.addGap(41)))
							.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lbOpSelectOp)
								.addComponent(cbOpOperations, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lbOpSelectRL2)
								.addComponent(cbOpRL2, 0, 283, Short.MAX_VALUE)))
						.addGroup(gl_operationsFramePanel.createSequentialGroup()
							.addComponent(btnOpCancel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
//							.addPreferredGap(ComponentPlacement.RELATED)
//							.addComponent(btnOpView, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOpSave, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_operationsFramePanel.setVerticalGroup(
			gl_operationsFramePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_operationsFramePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lbOpSelectRL1)
						.addComponent(lbOpSelectOp)
						.addComponent(lbOpSelectRL2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(cbOpRL1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(cbOpOperations, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(cbOpRL2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
					.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.BASELINE)
//						.addComponent(btnOpView, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOpCancel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOpSave, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		operationsFramePanel.setLayout(gl_operationsFramePanel);
		this.populateComboBoxes();
	}
	
	// Save new language based on selections
	private boolean saveOperation(String operation, RegularLanguage rl1, RegularLanguage rl2) {
		if (rl1 == null) {
			return false;
		}
		if (operation.equals("Union")) {
			if (rl2 == null) {
				return false;
			}
			RegularLanguage newL = FAOperator.union(rl1.getFA(), rl2.getFA());
			newL.setId("[ [" + rl1.getId() + "] \u222A [" + rl2.getId() + "] ]");
			mainFrame.addToPanel(newL);
			return true;
		} else if (operation.equals("Complement")) {
			RegularLanguage newL = FAOperator.complement(rl1.getFA(), null);
			newL.setId("[ [" + rl1.getId() + "]\u2201 ]");
			mainFrame.addToPanel(newL);
			return true;
		} else if (operation.equals("Intersection")) {	
			Map<String, FiniteAutomata> automatas = FAOperator.intersectionSteps(rl1.getFA(), rl2.getFA());
			automatas.get("C1").setId("[ [" + rl1.getId() + "]\u2201 ]");
			automatas.get("C2").setId("[ [" + rl2.getId() + "]\u2201 ]");
			automatas.get("U").setId("[ [" + automatas.get("C1").getId() + "] \u222A [" + automatas.get("C2").getId() + "] ]");
			automatas.get("I").setId("[ [" + rl1.getId() + "] \u2229 [" + rl2.getId() + "] ]");
			for (FiniteAutomata fa : automatas.values()) {
				mainFrame.addToPanel(fa);
			}
		} else if (operation.equals("Minimize FA")) {
			RegularLanguage newL = FAOperator.minimize(rl1.getFA());
			newL.setId("[ [" + rl1.getId() + "]min ]");
			mainFrame.addToPanel(newL);
		} else if (operation.equals("Determinize FA")) {
			RegularLanguage newL = FAOperator.determinize(rl1.getFA());
			newL.setId("[ [" + rl1.getId() + "]det ]");
			mainFrame.addToPanel(newL);
		} else {
			return false;
		}
		
		return true;
		
	}

	// Populate combo boxes with regular languages from the list
	private void populateComboBoxes() {
		HashMap<String, RegularLanguage> languages = mainFrame.getLanguages();
		for (String id : languages.keySet()) {
			cbOpRL1.addItem(languages.get(id));
			cbOpRL2.addItem(languages.get(id));
		}
	}
}
