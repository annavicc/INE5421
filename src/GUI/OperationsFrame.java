package GUI;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import RegularLanguages.RegularLanguage;

public class OperationsFrame {

	private JFrame frmRegularLanguagesOperations;
	private JComboBox<RegularLanguage> cbOpRL1;
	private JComboBox<RegularLanguage> cbOpRL2;
	private MainFrame mainFrame = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OperationsFrame window = new OperationsFrame();
					window.frmRegularLanguagesOperations.setVisible(true);
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
		frmRegularLanguagesOperations.setVisible(false);
	}

	/**
	 * Create the application.
	 */
	public OperationsFrame() {
		initialize();
	}

	public OperationsFrame(MainFrame mainFrame) {
		try {
			this.mainFrame = mainFrame;
			initialize();
			this.frmRegularLanguagesOperations.setVisible(true);
			mainFrame.hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRegularLanguagesOperations = new JFrame();
		frmRegularLanguagesOperations.setTitle("Regular Languages Operations");
		frmRegularLanguagesOperations.setResizable(false);
		frmRegularLanguagesOperations.setBounds(100, 100, 600, 180);
		frmRegularLanguagesOperations.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRegularLanguagesOperations.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel operationsFramePanel = new JPanel();
		frmRegularLanguagesOperations.getContentPane().add(operationsFramePanel, BorderLayout.CENTER);
		
		cbOpRL1 = new JComboBox<RegularLanguage>();
		
		cbOpRL2 = new JComboBox<RegularLanguage>();
		
		JComboBox<String> cbOpOperations = new JComboBox<String>();
		
		cbOpOperations.addItem("Union");
		cbOpOperations.addItem("Intersection");
		cbOpOperations.addItem("Difference");
		cbOpOperations.addItem("Concatenation");
		
		JLabel lbOpSelectRL1 = new JLabel("Select RL 1");
		
		JLabel lbOpSelectOp = new JLabel("Select Operation");
		
		JLabel lbOpSelectRL2 = new JLabel("Select RL 2");
		
		JButton btnOpCancel = new JButton("Cancel");
		btnOpCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperationsFrame.this.hide();
			}
		});
		
		JButton btnOpSave = new JButton("Save");
		
		JButton btnOpView = new JButton("View");
		GroupLayout gl_operationsFramePanel = new GroupLayout(operationsFramePanel);
		gl_operationsFramePanel.setHorizontalGroup(
			gl_operationsFramePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_operationsFramePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_operationsFramePanel.createSequentialGroup()
							.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_operationsFramePanel.createSequentialGroup()
									.addComponent(cbOpRL1, 0, 138, Short.MAX_VALUE)
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
								.addComponent(cbOpRL2, 0, 138, Short.MAX_VALUE)))
						.addGroup(gl_operationsFramePanel.createSequentialGroup()
							.addComponent(btnOpCancel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOpView)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOpSave)))
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
					.addPreferredGap(ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
					.addGroup(gl_operationsFramePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOpView)
						.addComponent(btnOpCancel)
						.addComponent(btnOpSave))
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
