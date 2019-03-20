package objets;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import geometrie.Vecteur;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Application qui permet de tester les differents objets sans jouer dans les classes des autres
 * @author Arnaud Lefebvre
 *
 */
public class ApplicationObjetsTest extends JFrame {

	private JPanel contentPane;
	private SceneTestPls scene;
	private static  ApplicationObjetsTest frame;

	/**
	 * Demarrer  l'application.
	 */
	public static void main(String[] args) {
		/*
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ApplicationObjetsTest();
					frame.setVisible(true);
					frame.scene.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		*/
		
		
		BlocDEau b = new BlocDEau(new Vecteur(0.0, 0.0));
		Vecteur v = b.refraction(new Vecteur(-1.0, -1.0), new Vecteur(0.0, 1.0), 1.0, 1.33);
		
		
		
		/*BlocDEau b = new BlocDEau(new Vecteur(0.0, 0.0));
		Vecteur v = b.refraction(new Vecteur(1.0, -1.0), new Vecteur(0.0, 1.0), 1.0, 1.33);
		
		*/
		System.out.println(v);
	}

	/**
	 * Creer la fenetre.
	 */
	public ApplicationObjetsTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 623, 594);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scene.requestFocusInWindow();
				scene.demarrer();
			}
		});
		btnStart.setBounds(36, 449, 89, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scene.arreter();
			}
		});
		btnStop.setBounds(159, 449, 89, 23);
		contentPane.add(btnStop);
		
		scene = new SceneTestPls();
		scene.setBounds(10, 11, 484, 439);
		contentPane.add(scene);
		
	
	}
}
