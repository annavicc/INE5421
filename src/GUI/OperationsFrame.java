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
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import RegularLanguages.RegularLanguage;

public class OperationsFrame extends JFrame {

	private JFrame frmRegularLanguagesOperations;
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
		cbOpOperations.addItem("Intersection");
		cbOpOperations.addItem("Difference");
		cbOpOperations.addItem("Concatenation");
		
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
		
		JButton btnOpView = new JButton("View");
		
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
								.addComponent(cbOpOperations, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lbOpSelectRL2)
								.addComponent(cbOpRL2, 0, 283, Short.MAX_VALUE)))
						.addGroup(gl_operationsFramePanel.createSequentialGroup()
							.addComponent(btnOpCancel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOpView, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
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
						.addComponent(btnOpView, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOpCancel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOpSave, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		operationsFramePanel.setLayout(gl_operationsFramePanel);
		this.populateComboBoxes();
	}
	
	// Populate combo boxes with regular languages from the list
	public void populateComboBoxes() {
		HashMap<String, RegularLanguage> languages = mainFrame.getLanguages();
		for (String id : languages.keySet()) {
			cbOpRL1.addItem(languages.get(id));
			cbOpRL2.addItem(languages.get(id));
		}
	}
}
