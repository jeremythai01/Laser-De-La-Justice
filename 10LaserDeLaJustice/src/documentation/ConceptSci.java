package documentation;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;

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
 * Cette classe permet de lire les concepts scientifique du jeu
 * @author Miora
 *
 */
public class ConceptSci extends JFrame {

	private JPanel contentPane;
	private int compteur = 0;
	private Font titre= new Font("Levenim MT", Font.BOLD, 45);
	private JLabel lblLesBalles;
	private JButton btnSuivant;
	private JButton btnPrcdent;
	private JLabel lblImgBalle;
	private JLabel lblBalle;
	private JLabel lblImgBalle2;

	/**
	 * Lancer l'application
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConceptSci frame = new ConceptSci();
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
	public ConceptSci() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 762, 925);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(211, 211, 211));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Icon imgIcon = new ImageIcon(this.getClass().getClassLoader().getResource("science.jpg"));
		JLabel lblPage1 = new JLabel(imgIcon);
		lblPage1.setBounds(50,27, 638, 450);
		contentPane.add(lblPage1);
		
	
		
		JLabel lblConcept = new JLabel("Concepts scientifiques");
		lblConcept.setBackground(Color.WHITE);
		lblConcept.setFont(new Font("Segoe Script", Font.PLAIN, 44));
		lblConcept.setBounds(128, 568, 516, 63);
		contentPane.add(lblConcept);
		
		
		btnPrcdent = new JButton("Pr\u00E9c\u00E9dent");
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
					lblPage1.setVisible(false);
					lblConcept.setVisible(false);

					lblImgBalle.setVisible(true);
					break;
				case 2 :
					lblImgBalle.setVisible(false);
					lblImgBalle2.setVisible(true);
					break;
				}
			}
		});
		btnSuivant.setBounds(504, 794, 140, 55);
		contentPane.add(btnSuivant);
		
		Icon imgBalle1 = new ImageIcon(this.getClass().getClassLoader().getResource("pageBalle.png"));
		lblImgBalle = new JLabel(imgBalle1);
		lblImgBalle.setBounds(-16,-70, 762, 925);
		lblImgBalle.setVisible(false);
		contentPane.add(lblImgBalle);
		
		Icon imgBalle2 = new ImageIcon(this.getClass().getClassLoader().getResource("pageBalle2.png"));
		lblImgBalle2 = new JLabel(imgBalle2);
		lblImgBalle2.setBounds(-16,-70, 762, 925);
		lblImgBalle2.setVisible(false);
		contentPane.add(lblImgBalle2);

		
	}
}
