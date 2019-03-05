package miora;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MiroirConvexeFrame extends JFrame {

	private JPanel contentPane;
	private SceneMiroirConvexe sceneMiroirConvexe;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MiroirConvexeFrame frame = new MiroirConvexeFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MiroirConvexeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 623, 594);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroirConvexe.requestFocusInWindow();
				sceneMiroirConvexe.demarrer();
			}
		});
		btnStart.setBounds(36, 449, 89, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroirConvexe.arreter();
			}
		});
		btnStop.setBounds(159, 449, 89, 23);
		contentPane.add(btnStop);
		
		sceneMiroirConvexe = new SceneMiroirConvexe();
		sceneMiroirConvexe.setBounds(10, 11, 424, 362);
		sceneMiroirConvexe.requestFocusInWindow();
		contentPane.add(sceneMiroirConvexe);
	}
}
