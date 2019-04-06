package miroir;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JLabel;

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
		setBounds(100, 100, 771, 655);
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
		
		JSlider sldMiroir = new JSlider();
		sldMiroir.setMajorTickSpacing(10);
		sldMiroir.setToolTipText("");
		sldMiroir.setSnapToTicks(true);
		sldMiroir.setPaintLabels(true);
		sldMiroir.setPaintTicks(true);
		sldMiroir.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sceneMiroir.setAngleMiroir(sldMiroir.getValue());
			}
		});
		sldMiroir.setValue(0);
		sldMiroir.setMaximum(180);
		sldMiroir.setBounds(116, 518, 452, 38);
		contentPane.add(sldMiroir);
		
		JLabel lblAngleMiroir = new JLabel("Angle miroir :");
		lblAngleMiroir.setBounds(10, 518, 96, 38);
		contentPane.add(lblAngleMiroir);
		
		JLabel lblAngleLaser = new JLabel("Angle laser :");
		lblAngleLaser.setBounds(10, 567, 73, 23);
		contentPane.add(lblAngleLaser);
		
		JSlider sldLaser = new JSlider();
		sldLaser.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sceneMiroir.setAngleLaser(sldLaser.getValue());
			}
		});
		sldLaser.setValue(90);
		sldLaser.setPaintLabels(true);
		sldLaser.setPaintTicks(true);
		sldLaser.setMajorTickSpacing(10);
		sldLaser.setMaximum(180);
		sldLaser.setBounds(116, 567, 452, 39);
		contentPane.add(sldLaser);
	}
}
