package GUI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainFrame {

	private JFrame mainFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setTitle("LR Manager");
		mainFrame.setResizable(false);
		mainFrame.setBounds(100, 100, 500, 400);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		JButton btnMainAddRL = new JButton("Add a RL");
		btnMainAddRL.setBounds(304, 80, 170, 25);
		btnMainAddRL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mainPanel.setLayout(null);
		
		JList listMainRL = new JList();
		listMainRL.setBounds(12, 50, 280, 195);
		mainPanel.add(listMainRL);
		mainPanel.add(btnMainAddRL);
		
		JButton btnMainViewEdit = new JButton("View/Edit");
		btnMainViewEdit.setBounds(304, 117, 170, 25);
		mainPanel.add(btnMainViewEdit);
		
		JButton btnMainRemoveRL = new JButton("Remove");
		btnMainRemoveRL.setBounds(304, 154, 170, 25);
		mainPanel.add(btnMainRemoveRL);
		
		JButton btnMainClear = new JButton("Clear");
		btnMainClear.setBounds(304, 191, 170, 25);
		mainPanel.add(btnMainClear);
		
		JButton btnMainOperations = new JButton("RL Operations");
		btnMainOperations.setBounds(46, 297, 200, 25);
		mainPanel.add(btnMainOperations);
		
		JButton btnMainVerifications = new JButton("RL Properties");
		btnMainVerifications.setBounds(260, 297, 200, 25);
		mainPanel.add(btnMainVerifications);
	}
}
