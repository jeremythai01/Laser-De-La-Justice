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
public class AppMiroirTest extends JFrame {

	private JPanel contentPane;
	private SceneMiroir sceneMiroir;
	private static AppMiroirTest frame;

	/**
	 * Lancer l'application
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new AppMiroirTest();
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
	public AppMiroirTest() {
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
		btnJouer.setBounds(578, 67, 132, 23);
		contentPane.add(btnJouer);
		
		JButton btnArreter = new JButton("Arreter");
		btnArreter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.arreter();
			}
		});
		btnArreter.setBounds(578, 157, 132, 23);
		contentPane.add(btnArreter);
		
		JButton btnMioirPlan = new JButton("Mioir plan");
		btnMioirPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.setMiroirPlan(true);
				sceneMiroir.setMiroirConvexe(false);
				sceneMiroir.setMiroiConcave(false);
			}
		});
		btnMioirPlan.setBounds(578, 247, 132, 23);
		contentPane.add(btnMioirPlan);
		
		JButton btnMiroirConvexe = new JButton("Miroir convexe");
		btnMiroirConvexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.setMiroirPlan(false);
				sceneMiroir.setMiroirConvexe(true);
				sceneMiroir.setMiroiConcave(false);
			}
		});
		btnMiroirConvexe.setBounds(578, 337, 132, 23);
		contentPane.add(btnMiroirConvexe);
		
		sceneMiroir = new SceneMiroir();
		sceneMiroir.setBounds(10, 11, 558, 496);
		contentPane.add(sceneMiroir);
		
		JButton btnMiroirConcave = new JButton("Miroir concave");
		btnMiroirConcave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.setMiroirPlan(false);
				sceneMiroir.setMiroirConvexe(false);
				sceneMiroir.setMiroiConcave(true);
			}
		});
		btnMiroirConcave.setBounds(585, 427, 125, 23);
		contentPane.add(btnMiroirConcave);
	}
}