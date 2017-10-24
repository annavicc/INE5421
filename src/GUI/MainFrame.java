package GUI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import RegularLanguages.RegularLanguage;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;

public class MainFrame {

	private JFrame mainFrame;
	private JList<String> jListMainRL = new JList<String>();
	private HashMap<String, RegularLanguage> languages = new HashMap<String, RegularLanguage>();

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
//		mainFrame.setResizable(false);
		mainFrame.setBounds(100, 100, 500, 400);
        mainFrame.setMinimumSize(new Dimension(400, 300));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		JButton btnMainAddRL = new JButton("Add a RL");
		btnMainAddRL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddRLFrame(MainFrame.this);
			}
		});
		
		JButton btnMainViewEdit = new JButton("View/Edit");
		btnMainViewEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isLanguageSelected()) {
					String idSelected = jListMainRL.getSelectedValue();
					new ViewEditFrame(MainFrame.this, languages.get(idSelected));
				}
			}
		});
		
		JButton btnMainRemoveRL = new JButton("Remove");
		btnMainRemoveRL.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	MainFrame.this.removeSelected();
		    }
		});
		
		JButton btnMainClear = new JButton("Clear");
		btnMainClear.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	MainFrame.this.clearList();
		    }
		});
		
		JButton btnMainOperations = new JButton("RL Operations");
		btnMainOperations.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	new OperationsFrame(MainFrame.this);
		    }
		});
		
		JButton btnMainVerifications = new JButton("RL Properties");
		
		JScrollPane scrollPaneMainRL = new JScrollPane();
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPaneMainRL, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnMainAddRL, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnMainViewEdit, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnMainRemoveRL, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnMainClear, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addGap(46)
					.addComponent(btnMainOperations, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
					.addGap(14)
					.addComponent(btnMainVerifications, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
					.addGap(40))
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(scrollPaneMainRL)
							.addGap(40))
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(btnMainAddRL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnMainViewEdit, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addComponent(btnMainRemoveRL, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addComponent(btnMainClear, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 254, Short.MAX_VALUE)
							.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnMainOperations, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnMainVerifications, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		
		updateJList();
		scrollPaneMainRL.setViewportView(jListMainRL);
		jListMainRL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainPanel.setLayout(gl_mainPanel);
		btnMainVerifications.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	new PropertiesFrame(MainFrame.this);
		    }
		});

	}
	
	// Add Regular Language to JList
	public void addToPanel(RegularLanguage rl) {
		this.languages.put(rl.toString(), rl);
		this.updateJList();
	}
	
	// Return Regular Language by name
	public RegularLanguage getLanguage(String name) {
		return this.languages.get(name);
	}
	
	// Update JList elements
	private void updateJList() {
		String[] listData = languages.keySet()
			.stream()
			.toArray(size -> new String[size]);
		
		this.jListMainRL.setListData(listData);
	}
	
	// Ask for action confirmation
	private int confirm (String action) {
		return JOptionPane.showConfirmDialog(
				this.mainFrame,
				"Are you sure you want to " + action + "?",
				action + '?',
				JOptionPane.YES_NO_OPTION
		);
	}
	
	// Clear languages list on confirmation
	private void clearList() {
		if (confirm("Clear List") == JOptionPane.YES_OPTION) {
			MainFrame.this.languages.clear();
	    	updateJList();
		}
	}
	
	// Remove language selected on JList
	private void removeSelected() {
		if (jListMainRL.isSelectionEmpty()) {
			JOptionPane.showMessageDialog(this.mainFrame, "No language selected!");
		} else {
			String id = jListMainRL.getSelectedValue();
			if (confirm("Remove \"" + id + '"') == JOptionPane.YES_OPTION) {
				MainFrame.this.languages.remove(id);
		    	updateJList();
			}
		}
	}
	
	// Verifies if a language was selected
	private boolean isLanguageSelected() {
		if (jListMainRL.isSelectionEmpty()) {
			JOptionPane.showMessageDialog(this.mainFrame, "No language selected!");
			return false;
		}
		return true;
	}
	
	// Return the Regular Language list
	public HashMap<String, RegularLanguage> getLanguages() {
		return this.languages;
	}
	
}
