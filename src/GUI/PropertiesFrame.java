package GUI;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import RegularLanguages.RegularLanguage;
import RegularLanguages.Operators.FAOperator;

public class PropertiesFrame extends JFrame {

	// Auto-generated UID
	private static final long serialVersionUID = 82271491239769704L;
	
	private MainFrame mainFrame;
	private JComboBox<RegularLanguage> cbPrRL1;
	private JComboBox<RegularLanguage> cbPrRL2;

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
	public PropertiesFrame(MainFrame mainFrame) {
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
		this.setTitle("Regular Languages Properties");
		this.setResizable(false);
		this.setBounds(100, 200, 750, 190);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel propertiesFramePanel = new JPanel();
		this.getContentPane().setLayout(new BorderLayout(0, 0));
		
		cbPrRL1 = new JComboBox<RegularLanguage>();
		cbPrRL2 = new JComboBox<RegularLanguage>();

		// JLabels:
		JLabel lbPrSelectRL1 = new JLabel("Select RL 1");
		JLabel lbPrSelectProperty = new JLabel("Select Property");
		JLabel lbPrSelectRL2 = new JLabel("Select RL 2");

		// JComboBox
		JComboBox<String> cbPrProperties = new JComboBox<String>();
		cbPrProperties.addItem("Containment");
		cbPrProperties.addItem("Equivalence");
		cbPrProperties.addItem("Emptiness");
		cbPrProperties.addItem("Finiteness");
		cbPrProperties.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	String selected = String.valueOf(cbPrProperties.getSelectedItem());
		    	if (selected.equals("Containment") || selected.equals("Equivalence")) {
		    		cbPrRL2.setEnabled(true);
		    	} else {
		    		cbPrRL2.setEnabled(false);
		    	}
		    }
		});
		
		// JButtons:
		JButton btnPrCancel = new JButton("Cancel");
		btnPrCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PropertiesFrame.this.exit();
			}
		});
		
		JButton btnPrVerify = new JButton("Verify");
		btnPrVerify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RegularLanguage rl1 = (RegularLanguage) cbPrRL1.getSelectedItem();
				RegularLanguage rl2 = (RegularLanguage) cbPrRL2.getSelectedItem();
				String property = String.valueOf(cbPrProperties.getSelectedItem());
				if (rl1 == null) { 
					return;
				}
				String response = rl1.getId();
				if (property.equals("Containment")) {
					if (rl2 == null) { 
						return;
					}
					if (FAOperator.isSubset(rl1.getFA(), rl2.getFA())) {
						response += "\n\u2286 (is contained in)\n"; 
					} else {
						response += "\n\u2288 (is not contained in)\n";
					}
					response += rl2.getId();
				}
				if (property.equals("Equivalence")) {
					if (rl2 == null) { 
						return;
					}
					if (FAOperator.isEquivalent(rl1.getFA(), rl2.getFA())) {
						response += "\n= (is equivalent to)\n"; 
					} else {
						response += "\n\u2260 (is not equivalent to)\n";
					}
					response += rl2.getId();
				}
				if (property.equals("Emptiness")) {
					if (FAOperator.isEmptyLanguage(rl1.getFA())) {
						response += "\n= \u03d5 (is empty)\n"; 
					} else {
						response += "\n\u2260 \u03d5 (is not empty)\n";
					}
				}
				if (property.equals("Finiteness")) {
					if (FAOperator.isInfiniteLanguage(rl1.getFA())) {
						response += "\nis infinite \u221e\n"; 
					} else {
						response += "\nis finite\n";
					}
				}
				JOptionPane.showMessageDialog(PropertiesFrame.this, response);
			}
		});		
		
		// Close Window action:
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				PropertiesFrame.this.exit();
			}
		});
		
		// Layout definitions:
		
		GroupLayout gl_propertiesFramePanel = new GroupLayout(propertiesFramePanel);
		gl_propertiesFramePanel.setHorizontalGroup(
			gl_propertiesFramePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_propertiesFramePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_propertiesFramePanel.createSequentialGroup()
							.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(cbPrRL1, 0, 200, Short.MAX_VALUE)
								.addComponent(lbPrSelectRL1))
							.addGap(14)
							.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lbPrSelectProperty, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbPrProperties, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(cbPrRL2, 0, 200, Short.MAX_VALUE)
								.addComponent(lbPrSelectRL2, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_propertiesFramePanel.createSequentialGroup()
							.addComponent(btnPrCancel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnPrVerify, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
					.addGap(12))
		);
		gl_propertiesFramePanel.setVerticalGroup(
			gl_propertiesFramePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_propertiesFramePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lbPrSelectProperty)
						.addComponent(lbPrSelectRL2)
						.addComponent(lbPrSelectRL1))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbPrProperties, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbPrRL2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbPrRL1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
					.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPrVerify, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnPrCancel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		propertiesFramePanel.setLayout(gl_propertiesFramePanel);
		this.getContentPane().add(propertiesFramePanel);
		
		this.populateComboBoxes();
	}

	// Populate combo boxes with regular languages from the list
	public void populateComboBoxes() {
		HashMap<String, RegularLanguage> languages = mainFrame.getLanguages();
		for (String id : languages.keySet()) {
			cbPrRL1.addItem(languages.get(id));
			cbPrRL2.addItem(languages.get(id));
		}
	}

}
