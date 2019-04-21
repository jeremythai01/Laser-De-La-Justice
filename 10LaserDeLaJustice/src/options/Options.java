package options;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.swing.border.EmptyBorder;

import aaplication.FenetreJeu;
/**
 * Cette classe permet a l'utilisateur de modifier les parametres du jeu. 
 * Ces parametres sont le niveau, la gravite, les touches de clavier et la couleur du rayon 
 * @author Miora R. Rakoto 
 * @author Arnaud
 */

public class Options extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private int toucheGauche = 37, toucheDroite = 39;
	private JSpinner snpAcc;
	private JButton btnG;
	private JButton btnD;
	private Color couleurLaser = null;
	private boolean dansScene = false;
	private static boolean isIni = false;


	public boolean isDansScene() {
		return dansScene;
	}

	public void setDansScene(boolean dansScene) {
		this.dansScene = dansScene;
	}

	private FenetreJeu jeu;
	private boolean isModifie = false;
	private JLabel lblCouleur;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Options frame = new Options(isIni);
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
	 */
	public Options(boolean isIni) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 787, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblOptJeu = new JLabel("Options de jeu");
		lblOptJeu.setFont(new Font("Lucida Sans", Font.PLAIN, 50));
		lblOptJeu.setBounds(233, 47, 394, 64);
		contentPane.add(lblOptJeu);

		JLabel lblAccG = new JLabel("Acc\u00E9l\u00E9ration gravitationnelle :\r\n");
		lblAccG.setBounds(30, 166, 274, 22);
		lblAccG.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		contentPane.add(lblAccG);

		JLabel lblTouchePourSe = new JLabel("Touches pour se d\u00E9placer :");
		lblTouchePourSe.setBounds(30, 233, 274, 30);
		lblTouchePourSe.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		contentPane.add(lblTouchePourSe);

		JLabel lblCouleurDuRayon = new JLabel("Couleur du rayon :");
		lblCouleurDuRayon.setBounds(30, 369, 240, 22);
		lblCouleurDuRayon.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		contentPane.add(lblCouleurDuRayon);

		snpAcc = new JSpinner();
		snpAcc.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		snpAcc.setModel(new SpinnerNumberModel(new Double(9.8), new Double(0), null, new Double(0.1)));
		snpAcc.setBounds(467, 157, 54, 43);
		contentPane.add(snpAcc);

		JLabel lblGaucheTxt = new JLabel();
		lblGaucheTxt.setText("Gauche :");
		lblGaucheTxt.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		lblGaucheTxt.setBounds(353, 233, 77, 21);
		contentPane.add(lblGaucheTxt);

		JLabel lblDroiteTxt = new JLabel("Droite :");
		lblDroiteTxt.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		lblDroiteTxt.setBounds(555, 240, 63, 19);
		contentPane.add(lblDroiteTxt);

		btnG = new JButton("Gauche");
		btnG.setBackground(Color.WHITE);
		btnG.setFont(new Font("Lucida Sans", Font.PLAIN, 12));
		btnG.setOpaque(true);

		btnG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnG.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						modificationGauche(e);
					}
				});
			}
		});
		btnG.setBounds(373, 265, 154, 55);
		contentPane.add(btnG);

		btnD = new JButton("Droite");
		btnD.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 12));
		btnD.setBackground(Color.WHITE);
		btnD.setBounds(595, 270, 154, 55);
		btnD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnD.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						modificationDroite(e);
					}
				});
			}
		});
		btnG.setOpaque(true);
		contentPane.add(btnD);

		JButton btnSauvegarder = new JButton("Sauvegarder");
		btnSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(dansScene) {
					System.out.println("dans scene option");
					ecritureFichier(true);
					jeu = new FenetreJeu (false,"temporaire");
					jeu.setVisible(true);
				}
				ecritureFichier(true);
				setVisible(false);
			}
		});
		btnSauvegarder.setBounds(595, 399, 154, 43);
		contentPane.add(btnSauvegarder);

		JButton btnChangerLaCouleur = new JButton("Changer la couleur");
		btnChangerLaCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				choixCouleur();
				changerJLabel();
			}
		});
		btnChangerLaCouleur.setBounds(219, 374, 192, 21);
		contentPane.add(btnChangerLaCouleur);

		JLabel lblMs = new JLabel("m\\s\u00B2");
		lblMs.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		lblMs.setBounds(531, 165, 63, 27);
		contentPane.add(lblMs);

		lblCouleur = new JLabel("");
		lblCouleur.setBackground(Color.GRAY);
		lblCouleur.setBounds(441, 358, 86, 53);
		lblCouleur.setOpaque(true);
		contentPane.add(lblCouleur);
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
			System.out.println(customDir + " already exists");
		} else if (customDir.mkdirs()) {
			System.out.println(customDir + " was created");
		} else {
			System.out.println(customDir + " was not created");
		}
		//Fin creation dossier

		String nomFichier = "";
		if(isIni) {
			nomFichier= "modifie.d3t";
		}else {
			nomFichier = "DonneeOption.d3t";
		}
		File fichierDeTravail = new File(direction, nomFichier);

		ObjectOutputStream fluxSortie = null;
		try {
			fluxSortie = new ObjectOutputStream(new BufferedOutputStream(	new FileOutputStream(fichierDeTravail)));
			fluxSortie.writeDouble(Double.parseDouble(snpAcc.getValue().toString()));
			fluxSortie.writeInt(toucheGauche);
			fluxSortie.writeInt(toucheDroite);
			fluxSortie.writeObject(couleurLaser);
			JOptionPane.showMessageDialog(null,"Vos modifications ont ete sauvegardees avec succes");
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

	/**
	 * Cette methode retourne vrai si le fichier option a ete modifie
	 * @return vrai si le fichier option a ete modifie
	 */
	public boolean getIsModifie() {
		return isModifie;
	}
}
