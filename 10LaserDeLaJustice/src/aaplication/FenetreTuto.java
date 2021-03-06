package aaplication;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.print.attribute.standard.DocumentName;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;

import documentation.FenetreConcept;
import interfaces.SceneTutorielListener;
import son.Bruit;

import javax.swing.UIManager;
/**
 * C'est la classe du tutoriel. Elle permet au joueur de comprendre le but du jeu.
 * @author Arnaud Lefebvre, Miora R. Rakoto
 *
 */

public class FenetreTuto extends JFrame {

	private JPanel contentPane;
	private SceneTutoriel sceneAnimee;
	private Bruit son = new Bruit();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreTuto frame = new FenetreTuto();
					frame.setVisible(true);
					frame.sceneAnimee.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de la fenetre
	 */
	//Arnaud
	public FenetreTuto() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 908);
		Dimension ecranDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(ecranDimension.width/2-getSize().width/2, ecranDimension.height/2-getSize().height/2);
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
		sceneAnimee.setBounds(27, 56, 488, 704);
		contentPane.add(sceneAnimee);
		
		JLabel lblCommentJouer = new JLabel("COMMENT JOUER?");
		lblCommentJouer.setForeground(Color.GREEN);
		lblCommentJouer.setFont(new Font("Linux Libertine G", Font.PLAIN, 40));
		lblCommentJouer.setBounds(246, 11, 447, 34);
		contentPane.add(lblCommentJouer);
		
		JLabel lblSeDplacer = new JLabel("Se d\u00E9placer");
		lblSeDplacer.setBounds(27, 844, 116, 14);
		contentPane.add(lblSeDplacer);
		
		JLabel lblTirerUnLaser = new JLabel("Tirer un laser");
		lblTirerUnLaser.setBounds(26, 819, 117, 14);
		contentPane.add(lblTirerUnLaser);
		
		JLabel lblIns = new JLabel("Instructions");
		lblIns.setFont(new Font("Stencil", Font.PLAIN, 16));
		lblIns.setBounds(696, 54, 131, 14);
		contentPane.add(lblIns);
		
		JLabel lblCommandesImportantes = new JLabel("Commandes importantes");
		lblCommandesImportantes.setBounds(179, 790, 152, 14);
		contentPane.add(lblCommandesImportantes);
		
		SceneInstructionJeu sceneInstructionJeu = new SceneInstructionJeu();
		sceneInstructionJeu.setBounds(525, 79, 431, 754);
		contentPane.add(sceneInstructionJeu);
		
		JScrollBar scrollBarX = new JScrollBar();
		scrollBarX.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				sceneInstructionJeu.setX((double)-3*scrollBarX.getValue());
				sceneAnimee.requestFocusInWindow();
			}
		});
		scrollBarX.setBounds(525, 841, 342, 17);
		contentPane.add(scrollBarX);
		scrollBarX.setOrientation(JScrollBar.HORIZONTAL);
		
		JScrollBar scrollBarY = new JScrollBar();
		scrollBarY.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				sceneInstructionJeu.setY((double)-3*scrollBarY.getValue());
				sceneAnimee.requestFocusInWindow();
			}
		});
		scrollBarY.setBounds(957, 79, 17, 754);
		contentPane.add(scrollBarY);
		
		JButton btnPageSuivante = new JButton("page suivante");
		btnPageSuivante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneInstructionJeu.augmenterPage();
				sceneAnimee.requestFocusInWindow();
			}
		});
		btnPageSuivante.setBounds(837, 50, 119, 23);
		contentPane.add(btnPageSuivante);
		
		JButton btnPagePrcdente = new JButton("page pr\u00E9c\u00E9dente");
		btnPagePrcdente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneInstructionJeu.baisserPage();
				sceneAnimee.requestFocusInWindow();
			}
		});
		btnPagePrcdente.setBounds(525, 50, 131, 23);
		contentPane.add(btnPagePrcdente);
		
		JButton btnDfaut = new JButton("D\u00E9faut");
		btnDfaut.setBounds(885, 840, 89, 23);
		contentPane.add(btnDfaut);
		
		JButton btnRetour = new JButton("Retour");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				App10LaserDeLaJustice demarrage = new App10LaserDeLaJustice();
				demarrage.setVisible(true);
				setVisible(false);
				sceneAnimee.requestFocusInWindow();
			}
		});
		btnRetour.setBounds(31, 22, 89, 23);
		contentPane.add(btnRetour);
		
		JButton btnConcept = new JButton("Concept scientifique");
		btnConcept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				son.joue("beep");
				affichierConcept();
				sceneAnimee.requestFocusInWindow();
			}
		});
		btnConcept.setBounds(810, 11, 152, 23);
		contentPane.add(btnConcept);
		
		JButton btnPropos = new JButton("\u00C0 propos");
		btnPropos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FenetreAuteur auteur = new FenetreAuteur();
				auteur.setVisible(true);
			}
		});
		btnPropos.setBounds(646, 11, 152, 23);
		contentPane.add(btnPropos);
		btnDfaut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				son.joue("beep");
				sceneInstructionJeu.defaut();
				sceneAnimee.requestFocusInWindow();
			}
		});
		
		sceneAnimee.ajoutBalleGrosse();
		sceneAnimee.demarrer();
	}
	
	//Par Miora
	/**
	 * Cette methode permet d'ouvrir une fenetre pour consulter les concepts scientifiques
	 * derriere l'application
	 */
	private void affichierConcept() {
		FenetreConcept fenetreConcept = new FenetreConcept();
		fenetreConcept.setVisible(true);
		//setVisible(false);
		
	}
}
