package miroir;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;

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
		setBounds(100, 100, 934, 783);
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
		btnJouer.setBounds(250, 648, 132, 23);
		contentPane.add(btnJouer);

		JButton btnArreter = new JButton("Arreter");
		btnArreter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.arreter();
			}
		});
		btnArreter.setBounds(426, 648, 132, 23);
		contentPane.add(btnArreter);

		JButton btnMioirPlan = new JButton("Miroir plan");
		btnMioirPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.setMiroirPlan(true);
				sceneMiroir.setMiroirConvexe(false);
				sceneMiroir.setMiroiConcave(false);
			}
		});
		btnMioirPlan.setBounds(250, 700, 132, 23);
		contentPane.add(btnMioirPlan);

		JButton btnMiroirConvexe = new JButton("Miroir convexe");
		btnMiroirConvexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.setMiroirPlan(false);
				sceneMiroir.setMiroirConvexe(true);
				sceneMiroir.setMiroiConcave(false);
			}
		});
		btnMiroirConvexe.setBounds(426, 700, 132, 23);
		contentPane.add(btnMiroirConvexe);


		sceneMiroir = new SceneMiroir();
		sceneMiroir.setBounds(21, 48, 535, 531);
		contentPane.add(sceneMiroir);

		JLabel lblAngleMioir = new JLabel("Angle miroir :");
		lblAngleMioir.setBounds(33, 648, 72, 37);
		contentPane.add(lblAngleMioir);

		JLabel lblAngleLaser = new JLabel("Angle laser :");
		lblAngleLaser.setBounds(38, 696, 80, 28);
		contentPane.add(lblAngleLaser);
		
		JSpinner spnLaser = new JSpinner();
		spnLaser.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sceneMiroir.setAngleLaser(Integer.parseInt(spnLaser.getValue().toString()));
				
			}
		});
		spnLaser.setModel(new SpinnerNumberModel(90, 0, 180, 1));
		spnLaser.setBounds(117, 696, 47, 37);
		contentPane.add(spnLaser);
		
		JSpinner spnMiroir = new JSpinner();
		spnMiroir.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sceneMiroir.setAngleMiroir(Integer.parseInt(spnMiroir.getValue().toString()));
			}
		});
		spnMiroir.setModel(new SpinnerNumberModel(0, 0, 1800, 1));
		spnMiroir.setBounds(117, 648, 48, 37);
		contentPane.add(spnMiroir);
		
		JButton btnCorbeille = new JButton("Corbeille");
		btnCorbeille.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneMiroir.nettoyer();
			}
		});
		btnCorbeille.setBounds(152, 585, 89, 23);
		contentPane.add(btnCorbeille);
		
		JCheckBox chkScience = new JCheckBox("Mode scientifique");
		chkScience.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chkScience.isSelected()) {
					sceneMiroir.setScientifique();
				}
			}
		});
		chkScience.setBounds(588, 188, 149, 37);
		contentPane.add(chkScience);
	}
}
