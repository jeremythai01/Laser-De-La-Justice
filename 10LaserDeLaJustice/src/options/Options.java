package options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import aaplication.FenetreJeu;
import geometrie.Vecteur;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
/**
 * Cette classe permet a l'utilisateur de modifier les parametres du jeu. 
 * Ces parametres sont le niveau, la gravite, les touches de clavier et la couleur du rayon 
 * @author Miora R. Rakoto 
 * @author Arnaud
 */

public class Options extends JFrame {

	private JPanel contentPane;
	private int toucheGauche = 37, toucheDroite = 39, toucheTir = 32;
	private JSpinner snpAcc;
	private JButton btnG;
	private JButton btnD;
	private Color couleurLaser = null;
	private static boolean dansScene;
	private static boolean isIni = false;
	private FenetreJeu jeu;
	private boolean isModifie = false;
	private JLabel lblCouleur;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton btnTirer;
	private JRadioButton rdbtnClavier;
	private JRadioButton btnRadSouris;


	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Options frame = new Options(isIni, dansScene);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de la fenetre.
	 * @param isIni : retourne vrai si les options ont ete ouverts
	 * @param dansScene : retourne vrai si appele quand le jeu a deja demarre
	 * @throws IOException : si mauvaise lecture
	 * @throws FileNotFoundException : si fichier non-trouve
	 */
	public Options(boolean isIni, boolean dansScene) throws FileNotFoundException, IOException {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 798, 515);
		Dimension ecranDimension = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(ecranDimension.width/2-getSize().width/2, ecranDimension.height/2-getSize().height/2);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblOptJeu = new JLabel("Options de jeu");
		lblOptJeu.setFont(new Font("Lucida Sans", Font.PLAIN, 36));
		lblOptJeu.setBounds(275, 11, 274, 64);
		contentPane.add(lblOptJeu);

		JLabel lblAccG = new JLabel("Acc\u00E9l\u00E9ration gravitationnelle :\r\n");
		lblAccG.setBounds(30, 92, 274, 22);
		lblAccG.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		contentPane.add(lblAccG);

		JLabel lblTouchePourSe = new JLabel("Touches pour se d\u00E9placer :");
		lblTouchePourSe.setBounds(30, 209, 274, 30);
		lblTouchePourSe.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		contentPane.add(lblTouchePourSe);

		JLabel lblCouleurDuRayon = new JLabel("Couleur du rayon :");
		lblCouleurDuRayon.setBounds(30, 386, 240, 22);
		lblCouleurDuRayon.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		contentPane.add(lblCouleurDuRayon);

		snpAcc = new JSpinner();
		snpAcc.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		snpAcc.setModel(new SpinnerNumberModel(9.8, 1.0, 20.0, 0.1));
		snpAcc.setBounds(398, 83, 77, 43);
		contentPane.add(snpAcc);

		JLabel lblGaucheTxt = new JLabel();
		lblGaucheTxt.setText("Gauche :");
		lblGaucheTxt.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		lblGaucheTxt.setBounds(334, 194, 77, 21);
		contentPane.add(lblGaucheTxt);

		JLabel lblDroiteTxt = new JLabel("Droite :");
		lblDroiteTxt.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		lblDroiteTxt.setBounds(574, 195, 63, 19);
		contentPane.add(lblDroiteTxt);

		btnG = new JButton("Gauche");
		btnG.setBackground(Color.WHITE);
		btnG.setFont(new Font("Lucida Sans", Font.PLAIN, 12));
		btnG.setOpaque(true);

		btnG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnG.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						modificationGauche(e);
					}
				});
			}
		});
		btnG.setBounds(334, 226, 163, 43);
		contentPane.add(btnG);

		btnD = new JButton("Droite");
		btnD.setFont(new Font("Lucida Sans", Font.PLAIN, 12));
		btnD.setBackground(Color.WHITE);
		btnD.setBounds(554, 226, 154, 43);
		btnD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnD.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						modificationDroite(e);
					}
				});
			}
		});
		btnG.setOpaque(true);
		contentPane.add(btnD);

		JButton btnSauvegarder = new JButton("Sauvegarder");
		btnSauvegarder.setBackground(Color.WHITE);
		btnSauvegarder.setFont(new Font("Lucida Sans", Font.PLAIN, 13));
		btnSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(dansScene) {
					ecritureFichier(true);
					jeu = new FenetreJeu (false,"temporaire");
					jeu.setVisible(true);
				}
				ecritureFichier(true);
				setVisible(false);
			}
		});
		btnSauvegarder.setOpaque(true);
		btnSauvegarder.setBounds(618, 423, 154, 43);
		contentPane.add(btnSauvegarder);

		JButton btnChangerLaCouleur = new JButton("Changer la couleur");
		btnChangerLaCouleur.setBackground(Color.WHITE);
		btnChangerLaCouleur.setFont(new Font("Lucida Sans", Font.PLAIN, 12));
		btnChangerLaCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				choixCouleur();
				changerJLabel();
			}
		});
		btnChangerLaCouleur.setOpaque(true);
		btnChangerLaCouleur.setBounds(334, 379, 163, 43);
		contentPane.add(btnChangerLaCouleur);

		JLabel lblMs = new JLabel("m\\s\u00B2");
		lblMs.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		lblMs.setBounds(486, 91, 63, 27);
		contentPane.add(lblMs);

		lblCouleur = new JLabel("");
		lblCouleur.setBackground(Color.GRAY);
		lblCouleur.setBounds(531, 386, 71, 36);
		lblCouleur.setOpaque(true);
		contentPane.add(lblCouleur);

		JLabel lblModeDeDplacement = new JLabel("Mode de d\u00E9placement :");
		lblModeDeDplacement.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		lblModeDeDplacement.setBounds(30, 147, 274, 30);
		contentPane.add(lblModeDeDplacement);

		
		btnRadSouris = new JRadioButton("Souris");
		btnRadSouris.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		btnRadSouris.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(btnRadSouris.isSelected()) {
					desactiverLesBoutons() ;
				}
			}
		});
		buttonGroup.add(btnRadSouris);
		btnRadSouris.setBounds(334, 152, 109, 23);
		contentPane.add(btnRadSouris);

		rdbtnClavier = new JRadioButton("Clavier");
		rdbtnClavier.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		rdbtnClavier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnClavier.isSelected()) {
					activerLesBoutons();
				}
			}
		});
		buttonGroup.add(rdbtnClavier);
		rdbtnClavier.setSelected(true);
		rdbtnClavier.setBounds(554, 152, 109, 23);
		contentPane.add(rdbtnClavier);

		JLabel btnTir = new JLabel("Touche pour tirer : ");
		btnTir.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		btnTir.setBounds(33, 292, 226, 30);
		contentPane.add(btnTir);

		btnTirer = new JButton("Espace");
		btnTirer.setBackground(Color.WHITE);
		btnTirer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnTirer.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						modificationTir(e);
					}
				});
			}
		});
		btnTirer.setOpaque(true);
		btnTirer.setFont(new Font("Lucida Sans", Font.PLAIN, 12));
		btnTirer.setBounds(334, 303, 163, 46);
		contentPane.add(btnTirer);
		
		if(dansScene && !isIni) {
			lectureFichierOption();
		}
	}

	//Par Miora
	/**
	 * Cette methode permet d'activer les boutons gauches et droites
	 */
	private void activerLesBoutons() {
		btnG.setEnabled(true);
		btnD.setEnabled(true);
	}

	//Par Miora
	/**
	 * Cette methode permet desactiver les boutons gauches et droites
	 */
	private void desactiverLesBoutons() {
		btnG.setEnabled(false);
		btnD.setEnabled(false);
	}

	// Par Miora
	/**
	 * Cette methode permet de sauvegarder les options du choisi par l'utilisateur
	 * @param isIni : retourne vrai si le fichier option a ete ouvert
	 */

	private void ecritureFichier(boolean isIni){
		String direction = System.getProperty("user.home") + File.separator + "Desktop"  + File.separator +  "Laser de la justice";
		direction += File.separator + "Option";
		File customDir = new File(direction);

		if (customDir.exists()) {
			System.out.println(customDir + "Le fichier option n'existe pas");
		} else if (customDir.mkdirs()) {
			System.out.println(customDir + "Le fichier option a ete cree");
		} else {
			System.out.println(customDir + "Le fichier option n'a pas ete cree");
		}
		//Fin creation dossier

		String nomFichier = "";
		if(isIni) {
			nomFichier= "modifie.d3t";
		}else {
			nomFichier = "DonneInitiale.d3t";
		}
		File fichierDeTravail = new File(direction, nomFichier);

		ObjectOutputStream fluxSortie = null;
		try {
			fluxSortie = new ObjectOutputStream(new BufferedOutputStream(	new FileOutputStream(fichierDeTravail)));
			fluxSortie.writeDouble(Double.parseDouble(snpAcc.getValue().toString()));
			if(btnRadSouris.isSelected()) {
				fluxSortie.writeBoolean(true);
				desactiverLesBoutons() ;
			}else {
				fluxSortie.writeBoolean(false);
			}
			fluxSortie.writeInt(toucheGauche);
			fluxSortie.writeInt(toucheDroite);
			fluxSortie.writeInt(toucheTir);
			fluxSortie.writeObject(couleurLaser);
			JOptionPane.showMessageDialog(null,"Vos modifications ont ete sauvegardées avec succès");
		} 
		catch (IOException e) {
			System.out.println("Erreur lors de l'écriture!");
			e.printStackTrace();
		}
		finally {
			//on exécutera toujours ceci, erreur ou pas
			try { 
				fluxSortie.close();  
			}
			catch (IOException e) { 
				System.out.println("Erreur rencontrée lors de la fermeture!"); 
			}
		}//fin finally
	}

	// Par Miora
	/**
	 * Cette methode permet d'initialiser la touche de gauche a la touche du clavier
	 * @param e : la touche du clavier
	 */
	private void modificationGauche(KeyEvent e) {
		toucheGauche = e.getKeyCode() ;
		btnG.setText(KeyEvent.getKeyText(e.getKeyCode()));
	}

	//Par Miora
	/**
	 * Cette methode permet d'initialiser la touche de droite a la touche du clavier
	 * @param e : la touche du clavier
	 */
	private void modificationDroite (KeyEvent e) {
		toucheDroite = e.getKeyCode() ;
		btnD.setText(KeyEvent.getKeyText(e.getKeyCode()));
	}

	//Par Miora
	/**
	 * Cette methode permet d'initialiser la touche de tir 
	 * @param e : la touche du clavier
	 */
	private void modificationTir (KeyEvent e) {
		toucheTir = e.getKeyCode() ;
		btnTirer.setText(KeyEvent.getKeyText(e.getKeyCode()));
	}

	// Par Arnaud 
	/**
	 * Cette methode permet a l'utilisateur de modifier la couleur de son laser
	 */
	private void choixCouleur() {
		couleurLaser = JColorChooser.showDialog(null,"Sélectionner la couleur voulue", couleurLaser);
	}

	//Par Miora
	/**
	 * Cette methode permet de redonne la couleur choisie
	 */
	private void changerJLabel() {
		lblCouleur.setBackground(couleurLaser);
	}

	//Par Miora
	/**
	 * Cette methode retourne vrai si le fichier option a ete modifie
	 * @return vrai si le fichier option a ete modifie
	 */
	public boolean getIsModifie() {
		return isModifie;
	}


	//Par Miora
	/**
	 * Cette methode retourne vrai si la classe est appele en plein jeu
	 * @return vrai si la classe est appele en plein jeu
	 */
	public boolean isDansScene() {
		return dansScene;
	}

	//Par Miora
	/**
	 * Cette methode modifie le type d'option si il est ou non appele dans la scene
	 * @param dansScene vrai si appele dans scene
	 */
	public void setDansScene(boolean dansScene) {
		this.dansScene = dansScene;
	}

	// Par Miora
	/**
	 * Cette methode permet de remettre les donnes de fichier option
	 *
	 */
	private void lectureFichierOption() throws FileNotFoundException, IOException {
		File fichierDeTravail;
		ObjectInputStream fluxEntree = null;

		// Path du dossier contenant les modifications, les options sont crees par
		// ordinateur et non par partie
		String direction = System.getProperty("user.home") + File.separator + "Desktop" + File.separator
				+ "Laser de la justice";
		direction += File.separator + "Option" + File.separator + "modifie.d3t";
		File f = new File(direction);
		fichierDeTravail = new File(direction);
		// Fin path

		try {
			fluxEntree = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fichierDeTravail)));
			snpAcc.setValue(fluxEntree.readDouble());
			if(fluxEntree.readBoolean()) {
				btnRadSouris.setSelected(true);
				desactiverLesBoutons() ;
			}
			btnG.setText(KeyEvent.getKeyText(fluxEntree.readInt()));
			btnD.setText(KeyEvent.getKeyText(fluxEntree.readInt()));
			btnTirer.setText(KeyEvent.getKeyText(fluxEntree.readInt()));
			try {
				lblCouleur.setBackground((Color)fluxEntree.readObject());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
			catch (FileNotFoundException e) {
				System.exit(0);
			}

			catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erreur rencontree lors de la lecture");
				e.printStackTrace();
				System.exit(0);
			}

			finally {
				// on exécutera toujours ceci, erreur ou pas
				try {
					fluxEntree.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Erreur rencontrée lors de la fermeture!");
				}
			} // fin finally
	
	}

}
