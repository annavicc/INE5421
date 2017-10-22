package GUI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JButton;

public class OperationsFrame {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OperationsFrame window = new OperationsFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OperationsFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 270);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JComboBox cbOpRL1 = new JComboBox();
		cbOpRL1.setBounds(12, 55, 127, 24);
		frame.getContentPane().add(cbOpRL1);
		
		JComboBox cbOpRL2 = new JComboBox();
		cbOpRL2.setBounds(290, 55, 127, 24);
		frame.getContentPane().add(cbOpRL2);
		
		JComboBox cbOpOperations = new JComboBox();
		cbOpOperations.setBounds(151, 55, 127, 24);
		frame.getContentPane().add(cbOpOperations);
		
		JLabel lbOpSelectRL1 = new JLabel("Select RL 1");
		lbOpSelectRL1.setBounds(32, 28, 80, 15);
		frame.getContentPane().add(lbOpSelectRL1);
		
		JLabel lbOpSelectOp = new JLabel("Select Operation");
		lbOpSelectOp.setBounds(151, 28, 120, 15);
		frame.getContentPane().add(lbOpSelectOp);
		
		JLabel lbOpSelectRL2 = new JLabel("Select RL 2");
		lbOpSelectRL2.setBounds(320, 28, 80, 15);
		frame.getContentPane().add(lbOpSelectRL2);
		
		JButton btnOpCancel = new JButton("Cancel");
		btnOpCancel.setBounds(123, 172, 90, 25);
		frame.getContentPane().add(btnOpCancel);
		
		JButton btnOpSave = new JButton("Save");
		btnOpSave.setBounds(327, 172, 90, 25);
		frame.getContentPane().add(btnOpSave);
		
		JButton btnOpView = new JButton("View");
		btnOpView.setBounds(225, 172, 90, 25);
		frame.getContentPane().add(btnOpView);
	}
}
