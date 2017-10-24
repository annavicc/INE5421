package GUI;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

public class PropertiesFrame extends JFrame {

	private MainFrame mainFrame;

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
		
		// JLabels:

		JLabel lbPrSelectRL1 = new JLabel("Select LR 1");
		JLabel lbPrSelectProperty = new JLabel("Select Property");
		JLabel lbPrSelectRL2 = new JLabel("Select RL 2");

		// JComboBoxes:
		
		JComboBox<String> cbPrLR1 = new JComboBox<String>();
		JComboBox<String> cbPrLR2 = new JComboBox<String>();
		String[] languages = mainFrame.getLanguagesNames();
		for (String lang : languages) {
			cbPrLR1.addItem(lang);
			cbPrLR2.addItem(lang);
		}
		
		JComboBox<String> cbPrProperties = new JComboBox<String>();
		
		// JButtons:
		
		JButton btnPrCancel = new JButton("Cancel");
		btnPrCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PropertiesFrame.this.exit();
			}
		});
		
		JButton btnPrVerify = new JButton("Verify");
		
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
		this.getContentPane().add(propertiesFramePanel);
		
	}

}
