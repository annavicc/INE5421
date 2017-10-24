package GUI;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import RegularLanguages.RegularLanguage;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class PropertiesFrame {

	private JFrame frmRegularLanguagesProperties;
	private JComboBox<RegularLanguage> cbPrLR1;
	private JComboBox<RegularLanguage> cbPrLR2;
	private MainFrame mainFrame = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PropertiesFrame window = new PropertiesFrame();
					window.frmRegularLanguagesProperties.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Hide properties frame.
	 */
	public void hide() {
		if (mainFrame != null) {
			mainFrame.display();
		}
		frmRegularLanguagesProperties.setVisible(false);
	}
	
	/**
	 * Create the application.
	 */
	public PropertiesFrame() {
		initialize();
	}
	
	public PropertiesFrame(MainFrame mainFrame) {
		try {
			this.mainFrame = mainFrame;
			initialize();
			this.frmRegularLanguagesProperties.setVisible(true);
			mainFrame.hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRegularLanguagesProperties = new JFrame();
		frmRegularLanguagesProperties.setTitle("Regular Languages Properties");
		frmRegularLanguagesProperties.setResizable(false);
		frmRegularLanguagesProperties.setBounds(100, 100, 600, 180);
		frmRegularLanguagesProperties.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel propertiesFramePanel = new JPanel();
		
		cbPrLR1 = new JComboBox<RegularLanguage>();
		
		JLabel lbPrSelectRL1 = new JLabel("Select LR 1");
		
		JButton btnPrCancel = new JButton("Cancel");
		btnPrCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PropertiesFrame.this.hide();
			}
		});
		frmRegularLanguagesProperties.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JLabel lbPrSelectProperty = new JLabel("Select Property");
		
		JComboBox<String> cbPrProperties = new JComboBox<String>();
		cbPrProperties.addItem("Emptiness");
		cbPrProperties.addItem("Finitness");
		
		
		JButton btnPrVerify = new JButton("Verify");
		
		cbPrLR2 = new JComboBox<RegularLanguage>();
		
		JLabel lbPrSelectRL2 = new JLabel("Select RL 2");
		GroupLayout gl_propertiesFramePanel = new GroupLayout(propertiesFramePanel);
		gl_propertiesFramePanel.setHorizontalGroup(
			gl_propertiesFramePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_propertiesFramePanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_propertiesFramePanel.createSequentialGroup()
							.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(cbPrLR1, 0, 200, Short.MAX_VALUE)
								.addComponent(lbPrSelectRL1))
							.addGap(14)
							.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lbPrSelectProperty, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbPrProperties, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.LEADING)
								.addComponent(cbPrLR2, 0, 200, Short.MAX_VALUE)
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
						.addComponent(cbPrLR2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(cbPrLR1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
					.addGroup(gl_propertiesFramePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPrVerify, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnPrCancel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		propertiesFramePanel.setLayout(gl_propertiesFramePanel);
		frmRegularLanguagesProperties.getContentPane().add(propertiesFramePanel);
		this.populateComboBoxes();
	}

	// Populate combo boxes with regular languages from the list
	public void populateComboBoxes() {
		HashMap<String, RegularLanguage> languages = mainFrame.getLanguages();
		for (String id : languages.keySet()) {
			cbPrLR1.addItem(languages.get(id));
			cbPrLR2.addItem(languages.get(id));
		}
	}

}
