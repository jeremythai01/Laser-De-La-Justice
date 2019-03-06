package miroir;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

/**
 * Cette classe permet de tester les miroirs concaves et convexes
 * @author Miora
 *
 */
public class AplicationTestMiroire extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private SceneTestMiroireConcave sceneMiroir;
	private static AplicationTestMiroire frame;

	/**
	 * Lancer l'application
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AplicationTestMiroire();
					frame.setVisible(true);
					frame.sceneMiroir.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creer la fenetre
	 */
	public AplicationTestMiroire() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 736, 556);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnJouer = new JButton("Jouer");
		btnJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.requestFocusInWindow();
				sceneMiroir.demarrer();
			}
		});
		btnJouer.setBounds(578, 85, 132, 23);
		contentPane.add(btnJouer);
		
		JButton btnArreter = new JButton("Arreter");
		btnArreter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.arreter();
			}
		});
		btnArreter.setBounds(578, 193, 132, 23);
		contentPane.add(btnArreter);
		
		JButton btnMioirPlan = new JButton("Mioir plan");
		btnMioirPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.setMiroirPlan(true);
			}
		});
		btnMioirPlan.setBounds(578, 301, 132, 23);
		contentPane.add(btnMioirPlan);
		
		JButton btnMiroirConcave = new JButton("Miroir concave");
		btnMiroirConcave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.setMiroirPlan(false);
			}
		});
		btnMiroirConcave.setBounds(578, 409, 132, 23);
		contentPane.add(btnMiroirConcave);
		
		sceneMiroir = new SceneTestMiroireConcave();
		sceneMiroir.setBounds(10, 11, 558, 496);
		contentPane.add(sceneMiroir);
	}
}
