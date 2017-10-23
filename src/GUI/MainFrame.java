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
import javax.swing.DefaultListModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

public class MainFrame {

	private JFrame mainFrame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public void display() {
		mainFrame.setVisible(true);
	}

	public void hide() {
		mainFrame.setVisible(false);
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
		btnMainAddRL.setBounds(304, 80, 170, 30);
		btnMainAddRL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddRLFrame(MainFrame.this);
			}
		});
		mainPanel.setLayout(null);
		mainPanel.add(btnMainAddRL);
		
		JList listMainRL = new JList();
		listMainRL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMainRL.setBounds(12, 50, 280, 195);
		mainPanel.add(listMainRL);
		
		JButton btnMainViewEdit = new JButton("View/Edit");
		btnMainViewEdit.setBounds(304, 117, 170, 30);
		mainPanel.add(btnMainViewEdit);
		
		JButton btnMainRemoveRL = new JButton("Remove");
		btnMainRemoveRL.setBounds(304, 154, 170, 30);
		mainPanel.add(btnMainRemoveRL);
		
		JButton btnMainClear = new JButton("Clear");
		btnMainClear.setBounds(304, 191, 170, 30);
		mainPanel.add(btnMainClear);
		
		JButton btnMainOperations = new JButton("RL Operations");
		btnMainOperations.setBounds(46, 297, 200, 30);
		mainPanel.add(btnMainOperations);
		btnMainOperations.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	new OperationsFrame(MainFrame.this);
		    }
		});
		
		JButton btnMainVerifications = new JButton("RL Properties");
		btnMainVerifications.setBounds(260, 297, 200, 30);
		mainPanel.add(btnMainVerifications);
		btnMainVerifications.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	new PropertiesFrame(MainFrame.this);
		    }
		});
	}
}
