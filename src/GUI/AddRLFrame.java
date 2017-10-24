package GUI;

import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JTextField;

import RegularLanguages.RegularLanguage;

import javax.swing.DropMode;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AddRLFrame {

	private JFrame frmAddRegularLanguage;
	private MainFrame mainFrame;
	private JTextField txtAddRLName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddRLFrame window = new AddRLFrame(null);
					window.frmAddRegularLanguage.setVisible(true);
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
		mainFrame.display();
		frmAddRegularLanguage.setVisible(false);
	}

	/**
	 * Create the application.
	 */
	public AddRLFrame(MainFrame mainFrame) {
		try {
			this.mainFrame = mainFrame;
			initialize();
			this.frmAddRegularLanguage.setVisible(true);
			mainFrame.hide();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAddRegularLanguage = new JFrame();
//		frmAddRegularLanguage.setResizable(false);
		frmAddRegularLanguage.setBounds(100, 100, 500, 500);
		frmAddRegularLanguage.setMinimumSize(new Dimension(450, 300));
		frmAddRegularLanguage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel();
		
		JLabel lblAddRLName = new JLabel("Enter a name for the Language:");
		
		txtAddRLName = new JTextField();
		txtAddRLName.setColumns(10);
		
		JButton btnAddRLCancel = new JButton("Cancel");
		btnAddRLCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddRLFrame.this.hide();
			}
		});
		
		JEditorPane edpAddRL = new JEditorPane() {
		    @Override
		    public boolean getScrollableTracksViewportWidth()
		    {
		        return true;
		    }
		};
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(edpAddRL);
		edpAddRL.setToolTipText("");
		
		edpAddRL.setToolTipText("<html>RE format.: a*(b?c|d)*<br>"
				+ "RG format: S->aS|b</html>");
		
		JButton btnAddRLAdd = new JButton("Add");
		btnAddRLAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame currentFrame = AddRLFrame.this.frmAddRegularLanguage; 
				String name = txtAddRLName.getText(); // Gets name from txtField
				if (name.equals("")) {
					JOptionPane.showMessageDialog(currentFrame, "Empty name!");
					return;
				}
				else if (AddRLFrame.this.mainFrame.getLanguage(name) != null) {
					int answer = JOptionPane.showConfirmDialog(
							currentFrame,
							'"' + name + "\" already exists!\nOverwrite?",
							"Overwrite?",
							JOptionPane.YES_NO_OPTION
					);
					if (answer != JOptionPane.YES_OPTION) {
						return;
					}
				}
				String input = edpAddRL.getText(); // Gets text from pane
				RegularLanguage l = RegularLanguage.validate(input); // Gets RL object
				RegularLanguage.InputType lType = l.getType(); // Gets RL type
				if(lType.equals(RegularLanguage.InputType.UNDEFINED)) { // If type is not valid
					JOptionPane.showMessageDialog(currentFrame, "Invalid input!");
					return;
				}
				// add Regular Language to Main Panel
				l.setId(name);
				AddRLFrame.this.mainFrame.addToPanel(l);
				AddRLFrame.this.hide();
			}
		});
		frmAddRegularLanguage.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JLabel lblAddRLAdd = new JLabel("Enter a Regular Grammar or a Regular Expression below:");
		
		
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addGap(13)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblAddRLName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
						.addComponent(txtAddRLName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
						.addComponent(lblAddRLAdd, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_mainPanel.createSequentialGroup()
							.addComponent(scrollPane)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(btnAddRLCancel, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddRLAdd, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
					.addGap(13))
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAddRLName)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtAddRLName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblAddRLAdd)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddRLCancel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnAddRLAdd, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		
		mainPanel.setLayout(gl_mainPanel);
		frmAddRegularLanguage.getContentPane().add(mainPanel, BorderLayout.CENTER);
	}
}
