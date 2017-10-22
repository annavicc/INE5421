package GUI;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;

public class PropertiesFrame {

	private JFrame frmRegularLanguagesProperties;
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
		frmRegularLanguagesProperties.setBounds(100, 100, 450, 270);
		frmRegularLanguagesProperties.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRegularLanguagesProperties.getContentPane().setLayout(null);
		
		JComboBox cbPrLR1 = new JComboBox();
		cbPrLR1.setBounds(12, 55, 127, 24);
		frmRegularLanguagesProperties.getContentPane().add(cbPrLR1);
		
		JComboBox cbPrLR2 = new JComboBox();
		cbPrLR2.setBounds(290, 55, 127, 24);
		frmRegularLanguagesProperties.getContentPane().add(cbPrLR2);
		
		JComboBox cbPrProperties = new JComboBox();
		cbPrProperties.setBounds(151, 55, 127, 24);
		frmRegularLanguagesProperties.getContentPane().add(cbPrProperties);
		
		JLabel lbPrSelectRL1 = new JLabel("Select LR 1");
		lbPrSelectRL1.setBounds(32, 28, 80, 15);
		frmRegularLanguagesProperties.getContentPane().add(lbPrSelectRL1);
		
		JLabel lbPrSelectProperty = new JLabel("Select Property");
		lbPrSelectProperty.setBounds(151, 28, 120, 15);
		frmRegularLanguagesProperties.getContentPane().add(lbPrSelectProperty);
		
		JLabel lbPrSelectRL2 = new JLabel("Select RL 2");
		lbPrSelectRL2.setBounds(320, 28, 80, 15);
		frmRegularLanguagesProperties.getContentPane().add(lbPrSelectRL2);
		
		JButton btnPrCancel = new JButton("Cancel");
		btnPrCancel.setBounds(215, 162, 90, 30);
		frmRegularLanguagesProperties.getContentPane().add(btnPrCancel);
		btnPrCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PropertiesFrame.this.hide();
			}
		});
		
		JButton btnPrVerify = new JButton("Verify");
		btnPrVerify.setBounds(321, 161, 90, 30);
		frmRegularLanguagesProperties.getContentPane().add(btnPrVerify);
	}

}
