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

import aaplication.App10LaserDeLaJustice;
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
	private JSpinner snpDif;
	private JSpinner snpAcc;
	private JButton btnG;
	private JButton btnD;
	private Color couleurLaser = null;
	private static boolean dansScene = false;
	private App10LaserDeLaJustice jeu;
	private boolean isModifie = false;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Options frame = new Options();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Creation de la fenetre.
	 * @param dansScene retourne vrai si la scene jouait deja
	 */
	public Options() {
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

		JLabel lblLvlDif = new JLabel("Niveau de difficult\u00E9 :");
		lblLvlDif.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		lblLvlDif.setBounds(30, 132, 199, 30);
		contentPane.add(lblLvlDif);

		JLabel lblAccG = new JLabel("Acc\u00E9l\u00E9ration gravitationelle :\r\n");
		lblAccG.setBounds(30, 212, 274, 22);
		lblAccG.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		contentPane.add(lblAccG);

		JLabel lblTouchePourSe = new JLabel("Touches pour se d\u00E9placer :");
		lblTouchePourSe.setBounds(30, 297, 274, 30);
		lblTouchePourSe.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		contentPane.add(lblTouchePourSe);

		JLabel lblCouleurDuRayon = new JLabel("Couleur du rayon :");
		lblCouleurDuRayon.setBounds(30, 369, 240, 22);
		lblCouleurDuRayon.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
		contentPane.add(lblCouleurDuRayon);

		snpDif = new JSpinner();
		snpDif.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		snpDif.setModel(new SpinnerNumberModel(1, 1, 5, 1));
		snpDif.setBounds(460, 140, 54, 43);
		contentPane.add(snpDif);

		snpAcc = new JSpinner();
		snpAcc.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		snpAcc.setModel(new SpinnerNumberModel(new Double(9.8), new Double(0), null, new Double(0.1)));
		snpAcc.setBounds(460, 216, 54, 43);
		contentPane.add(snpAcc);

		JLabel lblGaucheTxt = new JLabel();
		lblGaucheTxt.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		lblGaucheTxt.setBounds(370, 306, 77, 21);
		contentPane.add(lblGaucheTxt);

		JLabel lblDroiteTxt = new JLabel("Droite :");
		lblDroiteTxt.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		lblDroiteTxt.setBounds(564, 304, 63, 19);
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
		btnG.setBounds(434, 304, 100, 55);
		contentPane.add(btnG);

		btnD = new JButton("Droite");
		btnD.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 12));
		btnD.setBackground(Color.WHITE);
		btnD.setBounds(636, 304, 100, 55);
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
				if(dansScene) { // dans app10
					ecritureFichier();
					jeu = new App10LaserDeLaJustice (false);
					jeu.setVisible(true);
				}else {
					ecritureFichier();
					isModifie = true;
				}
			}
		});
		btnSauvegarder.setBounds(610, 399, 139, 43);
		contentPane.add(btnSauvegarder);
		
		JButton btnChangerLaCouleur = new JButton("Changer la couleur");
		btnChangerLaCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				choixCouleur();
			}
		});
		btnChangerLaCouleur.setBounds(219, 374, 192, 21);
		contentPane.add(btnChangerLaCouleur);
	}
	
	// Par Miora
	/**
	 * Cette methode permet de sauvegarder les options du choisi par l'utilisateur
	 */
	private void ecritureFichier(){
		final String NOM_FICHIER_OPTION = "DonneeOption.d3t";
		File fichierDeTravail = new File(NOM_FICHIER_OPTION);

		ObjectOutputStream fluxSortie = null;
		try {
			fluxSortie = new ObjectOutputStream(new BufferedOutputStream(	new FileOutputStream(fichierDeTravail)));
			
			fluxSortie.writeInt(Integer.parseInt(snpDif.getValue().toString()));
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
	
	/**
	 * Cette methode retourne vrai si le fichier option a ete modifie
	 * @return vrai si le fichier option a ete modifie
	 */
	public boolean getIsModifie() {
		return isModifie;
	}
	
}
