package documentation;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

/**
 * Cette classe permet de lire les concepts derriere scientifiques du jeu l'application
 *
 */
public class FenetreConcept extends JFrame {

	private JPanel contentPane;
	private int compteur = 0;
	private JButton btnSuivant;
	private JButton btnPrcdent;
	private JLabel lblImgBalle;
	private JLabel lblImgBalle2;
	private JLabel lblTrou;
	private JLabel lblPrisme;
	private JLabel lblPlan3;
	private JLabel lblPlan2;
	private JLabel lblPlan1;
	private JLabel lblCourbe;
	private JLabel lblBloc;


	/**
	 * Lancer l'application
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreConcept frame = new FenetreConcept();
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
	public FenetreConcept() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 762, 925);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(211, 211, 211));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		Icon imgIcon = new ImageIcon(this.getClass().getClassLoader().getResource("science.jpg"));
		JLabel lblAcceuil = new JLabel(imgIcon);
		lblAcceuil.setBounds(50,27, 638, 450);
		contentPane.add(lblAcceuil);



		JLabel lblTextScientifique = new JLabel("Concepts scientifiques");
		lblTextScientifique.setBackground(Color.WHITE);
		lblTextScientifique.setFont(new Font("Segoe Script", Font.PLAIN, 44));
		lblTextScientifique.setBounds(128, 568, 516, 63);
		contentPane.add(lblTextScientifique);


		btnPrcdent = new JButton("Pr\u00E9c\u00E9dent");
		btnPrcdent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				compteur--;
				if(compteur<=0) {
					compteur =0 ;
				}
				switch(compteur) {
				case 0:
					lblImgBalle.setVisible(false);
					lblTextScientifique.setVisible(true);
					lblAcceuil.setVisible(true);
					btnPrcdent.setEnabled(false);
					break;
				case 1 :
					lblImgBalle2.setVisible(false);
					lblImgBalle.setVisible(true);
					btnPrcdent.setEnabled(true);
					break;
				case 2 :
					lblBloc.setVisible(false);
					lblImgBalle2.setVisible(true);
					break;
				case 3 :
					lblPrisme.setVisible(false);
					lblBloc.setVisible(true);
					break;
				case 4 :
					lblTrou.setVisible(false);
					lblPrisme.setVisible(true);
					break;
				case 5 :
					lblPlan1.setVisible(false);
					lblTrou.setVisible(true);
					break;
				case 6 :
					lblPlan2.setVisible(false);
					lblPlan1.setVisible(true);
					break;
				case 7 :
					lblPlan3.setVisible(false);
					lblPlan2.setVisible(true);
					break;
				case 8 :
					lblPlan3.setVisible(true);
					lblCourbe.setVisible(false);
					break;
				case 9 :
					lblPlan3.setVisible(false);
					lblCourbe.setVisible(true);
					break;
				}
			}
		});
		btnPrcdent.setBounds(128, 794, 140, 55);
		btnPrcdent.setEnabled(false);
		contentPane.add(btnPrcdent);

		btnSuivant = new JButton("Suivant");
		btnSuivant.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				compteur++;	
				switch(compteur) {
				case 1 :
					btnPrcdent.setEnabled(true);
					lblAcceuil.setVisible(false);
					lblTextScientifique.setVisible(false);
					lblImgBalle.setVisible(true);
					break;
				case 2 :
					lblImgBalle.setVisible(false);
					lblImgBalle2.setVisible(true);
					break;
				case 3 :
					lblImgBalle2.setVisible(false);
					lblBloc.setVisible(true);
					break;
				case 4 :
					lblBloc.setVisible(false);
					lblPrisme.setVisible(true);
					break;
				case 5 :
					lblPrisme.setVisible(false);
					lblTrou.setVisible(true);
					break;
				case 6 :
					lblTrou.setVisible(false);
					lblPlan1.setVisible(true);
					break;
				case 7 :
					lblPlan1.setVisible(false);
					lblPlan2.setVisible(true);
					break;
				case 8 :
					lblPlan2.setVisible(false);
					lblPlan3.setVisible(true);
					break;
				case 9 :
					lblPlan3.setVisible(false);
					lblCourbe.setVisible(true);
					break;
				}
				if(compteur>9) {
					compteur = 9;
				}
			}
		});
		btnSuivant.setBounds(504, 794, 140, 55);
		contentPane.add(btnSuivant);

		Icon imgBalle1 = new ImageIcon(this.getClass().getClassLoader().getResource("pageBalle.png"));
		lblImgBalle = new JLabel(imgBalle1);
		lblImgBalle.setBounds(31,27, 687, 750);
		lblImgBalle.setVisible(false);
		contentPane.add(lblImgBalle);

		Icon imgBalle2 = new ImageIcon(this.getClass().getClassLoader().getResource("pageBalle2.png"));
		lblImgBalle2 = new JLabel(imgBalle2);
		lblImgBalle2.setBounds(31,27, 687, 750);
		lblImgBalle2.setVisible(false);
		contentPane.add(lblImgBalle2);

		Icon imgBloc = new ImageIcon(this.getClass().getClassLoader().getResource("blocTxt.jpg"));
		lblBloc = new JLabel(imgBloc);
		lblBloc.setBounds(31,27, 687, 750);
		lblBloc.setVisible(false);
		contentPane.add(lblBloc);

		Icon imgCourbe = new ImageIcon(this.getClass().getClassLoader().getResource("courbeTxt.jpg"));
		lblCourbe = new JLabel(imgCourbe);
		lblCourbe.setBounds(31,27, 687, 750);
		lblCourbe.setVisible(false);
		contentPane.add(lblCourbe);

		Icon imgPlan1 = new ImageIcon(this.getClass().getClassLoader().getResource("plan1.jpg"));
		lblPlan1 = new JLabel(imgPlan1);
		lblPlan1.setBounds(31,27, 687, 750);
		lblPlan1.setVisible(false);
		contentPane.add(lblPlan1);

		Icon imgPlan2 = new ImageIcon(this.getClass().getClassLoader().getResource("plan2.jpg"));
		lblPlan2 = new JLabel(imgPlan2);
		lblPlan2.setBounds(31,27, 687, 750);
		lblPlan2.setVisible(false);
		contentPane.add(lblPlan2);

		Icon imgPlan3 = new ImageIcon(this.getClass().getClassLoader().getResource("plan3.jpg"));
		lblPlan3 = new JLabel(imgPlan3);
		lblPlan3.setBounds(31,27, 687, 750);
		lblPlan3.setVisible(false);
		contentPane.add(lblPlan3);

		Icon imgPrisme = new ImageIcon(this.getClass().getClassLoader().getResource("prismeTxt.jpg"));
		lblPrisme = new JLabel(imgPrisme);
		lblPrisme.setBounds(31,27, 687, 750);
		lblPrisme.setVisible(false);
		contentPane.add(lblPrisme);

		Icon imgTrou = new ImageIcon(this.getClass().getClassLoader().getResource("trouNoirtxt.jpg"));
		lblTrou = new JLabel(imgTrou);
		lblTrou.setBounds(31,27, 687, 750);
		lblTrou.setVisible(false);
		contentPane.add(lblTrou);

	}
}
