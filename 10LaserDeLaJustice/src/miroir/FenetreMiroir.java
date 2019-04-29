package miroir;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import interfaces.MiroirListener;
import interfaces.SceneListener;

public class FenetreMiroir extends JFrame {

	/**
	 * Cette fenetre permet de modifier de facon plus detaille les miroirs
	 * @author Miora
	 */
	private JPanel contentPane;
	private ArrayList <MiroirListener> listeEcouteur = new ArrayList() ;
	private boolean isMiroirPlan, isMiroirCourbe;

	/**
	 * Lancer l'application
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreMiroir frame = new FenetreMiroir();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de la fenetre
	 */
	public FenetreMiroir() {
		setBackground(Color.GRAY);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 602, 577);
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		VisualisationMiroir editeurMiroir = new VisualisationMiroir();
		editeurMiroir.setBackground(Color.WHITE);
		editeurMiroir.setBounds(98, 46, 380, 380);
		contentPane.add(editeurMiroir);
		
		JLabel lblAngleMioir = new JLabel("Angle miroir :");
		lblAngleMioir.setBackground(Color.WHITE);
		lblAngleMioir.setOpaque(false);
		lblAngleMioir.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));
		lblAngleMioir.setBounds(43, 437, 91, 37);
		contentPane.add(lblAngleMioir);
		
		JSpinner spnMiroir = new JSpinner();
		spnMiroir.setModel(new SpinnerNumberModel(0, 0, 360, 1));
		spnMiroir.setSize(55, 30);
		spnMiroir.setLocation(179, 437);
		spnMiroir.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				editeurMiroir.setAngle(Integer.parseInt(spnMiroir.getValue().toString()));
				leverEvenChangementAngle(Integer.parseInt(spnMiroir.getValue().toString()));
			}
		});
		contentPane.add(spnMiroir);
		
		JLabel lblLongueurMiroir = new JLabel("Longueur miroir :");
		lblLongueurMiroir.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));
		lblLongueurMiroir.setBounds(40, 486, 134, 30);
		contentPane.add(lblLongueurMiroir);
		
		JSpinner spnLon = new JSpinner();
		spnLon.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				editeurMiroir.setLongueur(Integer.parseInt(spnLon.getValue().toString()));
				leverEvenChangementLongueur(Integer.parseInt(spnLon.getValue().toString()));
			}
		});
		spnLon.setModel(new SpinnerNumberModel(2, 2, 20, 1));
		spnLon.setBounds(179, 488, 55, 30);
		contentPane.add(spnLon);
		
		JButton btnMioirPlan = new JButton("Miroir plan");
		btnMioirPlan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editeurMiroir.dessinerMiroirPlan();
				isMiroirPlan = true;
				isMiroirCourbe = false;
			}
		});
		btnMioirPlan.setBounds(356, 495, 107, 23);
		contentPane.add(btnMioirPlan);

		JButton btnMiroirConvexe = new JButton("Miroir convexe");
		btnMiroirConvexe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				editeurMiroir.dessinerMiroirCourbe();
				isMiroirPlan = false;
				isMiroirCourbe = true;
			}
		});
		btnMiroirConvexe.setBounds(356, 460, 107, 23);
		contentPane.add(btnMiroirConvexe);
		
		JLabel lblVisualisationMiroirs = new JLabel("Visualisation miroirs ");
		lblVisualisationMiroirs.setFont(new Font("Vani", Font.PLAIN, 27));
		lblVisualisationMiroirs.setBounds(164, 11, 314, 46);
		contentPane.add(lblVisualisationMiroirs);
		
		JButton btnDessiner = new JButton("Dessiner");
		btnDessiner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				leverEvenDessin();
			}
		});
		btnDessiner.setBounds(485, 460, 91, 46);
		contentPane.add(btnDessiner);
		
		JLabel lblDegr = new JLabel("degr\u00E9s");
		lblDegr.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));
		lblDegr.setBounds(244, 437, 77, 24);
		contentPane.add(lblDegr);
		
		JLabel lblM = new JLabel("m");
		lblM.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));
		lblM.setBounds(244, 489, 31, 24);
		contentPane.add(lblM);
		
		JLabel lblNewLabel = new JLabel("Type de miroir :");
		lblNewLabel.setFont(new Font("Microsoft PhagsPa", Font.PLAIN, 14));
		lblNewLabel.setBounds(307, 437, 107, 19);
		contentPane.add(lblNewLabel);
		

	}
	
	/**
	 * Cette methode permet d'ajouter la liste d'écouteur de miroir
	 * @param miroirListener : l'ecouteur
	 */
	public void addMiroirListener(MiroirListener miroirListener) {
		listeEcouteur.add(miroirListener);
	}

	
	/**
	 * Cette methode permet de savoir si l'angle a ete change
	 * @param l'angle
	 */
	public void leverEvenChangementAngle(int angle) {
		for (MiroirListener ecout : listeEcouteur) {
			ecout.changementAngleMiroirListener(angle);
		}
	}
	
		/**
		 * Cette methode permet de savoir si la longueur a ete change
		 */
	public void leverEvenChangementLongueur(int longueur) {
		for (MiroirListener ecout : listeEcouteur) {
			ecout.changementLongueurMiroirListener(longueur);
		}
	}
	
	/**
	 * Cette methode permet de savoir si on veut dessiner un miroir
	 */
	public void leverEvenDessin() {
		for (MiroirListener ecout : listeEcouteur) {
			if(isMiroirPlan) {
				ecout.dessinerLeMiroirListener("MiroirPlan");
			}else {
				ecout.dessinerLeMiroirListener("MiroirCourbe");
			}
		}
	}
}
