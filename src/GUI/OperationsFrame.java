package GUI;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JButton;

public class OperationsFrame {

	private JFrame frmRegularLanguagesOperations;
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
		frmRegularLanguagesOperations.setBounds(100, 100, 450, 270);
		frmRegularLanguagesOperations.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRegularLanguagesOperations.getContentPane().setLayout(null);
		
		JComboBox cbOpRL1 = new JComboBox();
		cbOpRL1.setBounds(12, 55, 127, 24);
		frmRegularLanguagesOperations.getContentPane().add(cbOpRL1);
		
		JComboBox cbOpRL2 = new JComboBox();
		cbOpRL2.setBounds(290, 55, 127, 24);
		frmRegularLanguagesOperations.getContentPane().add(cbOpRL2);
		
		JComboBox cbOpOperations = new JComboBox();
		cbOpOperations.setBounds(151, 55, 127, 24);
		frmRegularLanguagesOperations.getContentPane().add(cbOpOperations);
		
		JLabel lbOpSelectRL1 = new JLabel("Select RL 1");
		lbOpSelectRL1.setBounds(32, 28, 80, 15);
		frmRegularLanguagesOperations.getContentPane().add(lbOpSelectRL1);
		
		JLabel lbOpSelectOp = new JLabel("Select Operation");
		lbOpSelectOp.setBounds(151, 28, 120, 15);
		frmRegularLanguagesOperations.getContentPane().add(lbOpSelectOp);
		
		JLabel lbOpSelectRL2 = new JLabel("Select RL 2");
		lbOpSelectRL2.setBounds(320, 28, 80, 15);
		frmRegularLanguagesOperations.getContentPane().add(lbOpSelectRL2);
		
		JButton btnOpCancel = new JButton("Cancel");
		btnOpCancel.setBounds(123, 172, 90, 30);
		frmRegularLanguagesOperations.getContentPane().add(btnOpCancel);
		btnOpCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OperationsFrame.this.hide();
			}
		});
		
		JButton btnOpSave = new JButton("Save");
		btnOpSave.setBounds(327, 172, 90, 30);
		frmRegularLanguagesOperations.getContentPane().add(btnOpSave);
		
		JButton btnOpView = new JButton("View");
		btnOpView.setBounds(225, 172, 90, 30);
		frmRegularLanguagesOperations.getContentPane().add(btnOpView);
	}
}
