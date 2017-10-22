package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JEditorPane;

public class ViewEditFrame {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewEditFrame window = new ViewEditFrame();
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
	public ViewEditFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JRadioButton rdbtnViewEditRE = new JRadioButton("Regular Expression");
		rdbtnViewEditRE.setBounds(8, 77, 165, 23);
		frame.getContentPane().add(rdbtnViewEditRE);
		
		JRadioButton rdbtnViewEditFA = new JRadioButton("Finite Automata");
		rdbtnViewEditFA.setBounds(182, 77, 145, 23);
		frame.getContentPane().add(rdbtnViewEditFA);
		
		JRadioButton rdbtnViewEditRG = new JRadioButton("Regular Grammar");
		rdbtnViewEditRG.setBounds(330, 77, 170, 23);
		frame.getContentPane().add(rdbtnViewEditRG);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(24, 108, 141, 268);
		frame.getContentPane().add(editorPane);
		
		JEditorPane editorPane_1 = new JEditorPane();
		editorPane_1.setBounds(176, 108, 141, 268);
		frame.getContentPane().add(editorPane_1);
		
		JEditorPane editorPane_2 = new JEditorPane();
		editorPane_2.setBounds(328, 108, 141, 268);
		frame.getContentPane().add(editorPane_2);
	}
}
