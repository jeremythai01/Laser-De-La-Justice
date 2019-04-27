package aaplication;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextArea;
import interfaces.SceneTutorielListener;
/**
 * C'est la classe du tutoriel. Elle permet au joueur de comprendre le but du jeu.
 * @author Arnaud Lefebvre
 *
 */
public class FenetreTuto extends JFrame {

	private JPanel contentPane;
	private SceneTutoriel sceneAnimee;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreTuto frame = new FenetreTuto();
					frame.setVisible(true);
					frame.sceneAnimee.requestFocus();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FenetreTuto() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 904, 504);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		sceneAnimee = new SceneTutoriel();
		sceneAnimee.addSceneTutorielListener(new SceneTutorielListener() {
			public void changerEtapeTutoriel() {
				sceneAnimee.changerEtapeTir();
			}
		});
		sceneAnimee.setBounds(27, 56, 488, 378);
		contentPane.add(sceneAnimee);
		
		JLabel lblCommentJouer = new JLabel("COMMENT JOUER?");
		lblCommentJouer.setForeground(Color.GREEN);
		lblCommentJouer.setFont(new Font("Linux Libertine G", Font.PLAIN, 40));
		lblCommentJouer.setBounds(246, 11, 348, 34);
		contentPane.add(lblCommentJouer);
		
		JLabel lblSeDplacer = new JLabel("Se d\u00E9placer");
		lblSeDplacer.setBounds(543, 370, 116, 14);
		contentPane.add(lblSeDplacer);
		
		JLabel lblTirerUnLaser = new JLabel("Tirer un laser");
		lblTirerUnLaser.setBounds(542, 409, 117, 14);
		contentPane.add(lblTirerUnLaser);
		
		JLabel lblIns = new JLabel("Instructions");
		lblIns.setFont(new Font("Stencil", Font.PLAIN, 16));
		lblIns.setBounds(649, 67, 131, 14);
		contentPane.add(lblIns);
		
		JLabel lblCommandesImportantes = new JLabel("Commandes importantes");
		lblCommandesImportantes.setBounds(636, 340, 152, 14);
		contentPane.add(lblCommandesImportantes);
		
		JTextArea txtrPourBienMaitriser = new JTextArea();
		txtrPourBienMaitriser.setWrapStyleWord(true);
		txtrPourBienMaitriser.setLineWrap(true);
		txtrPourBienMaitriser.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtrPourBienMaitriser.setBackground(Color.LIGHT_GRAY);
		txtrPourBienMaitriser.setText("Pour bien maitriser ce jeu, il faut suivre quelques instructions bien simples. Le joueur a 5 vies, chaque balle qui le touche lui retire une vie(un coeur). Une fois toutes les vies perdues, la partie se termine. Pour les \u00E9viter, le joueur peut d\u00E9placer le personnage \u00E0 l'aide des fl\u00E8ches horizontales(ou autres dans les options s'il le d\u00E9sire). Pour \u00E9liminer une balle, il suffit de la tirer avec son canon \u00E0 lasers. L'angle du canon peut \u00EAtre modifi\u00E9 par le joueur pour une meilleure pr\u00E9cision! Une fois toutes les balles \u00E9limin\u00E9es, et surtout si le temps ne s'est pas \u00E9coul\u00E9 alors le niveau est termin\u00E9. Voici un petit tutoriel pour vos apprendre les bases du jeu!!");
		txtrPourBienMaitriser.setBounds(542, 81, 336, 259);
		contentPane.add(txtrPourBienMaitriser);
		
		sceneAnimee.ajoutBalleGrosse();
		sceneAnimee.demarrer();
	}
}
