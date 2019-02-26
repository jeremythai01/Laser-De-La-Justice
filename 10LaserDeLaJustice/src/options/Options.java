package options;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import utilite.ModeleDonnee;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Options extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private String toucheGauche = "Gauche", toucheDroite = "Droite";
	private ModeleDonnee modele;
	private JTable table;

	

	/**
	 * Cette classe permet a l'utilisateur de modifier les parametres du jeu. 
	 * Ces parametres sont le niveau, la gravite, les touches de clavier et la couleur du rayon 
	 * @author Miora R. Rakoto
	 */
	
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
	 * Create the frame.
	 */
	public Options() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JSpinner snpDif = new JSpinner();
		snpDif.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		snpDif.setModel(new SpinnerNumberModel(1, 1, 5, 1));
		snpDif.setBounds(460, 140, 54, 43);
		contentPane.add(snpDif);
		
		JSpinner snpAcc = new JSpinner();
		snpAcc.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		snpAcc.setModel(new SpinnerNumberModel(new Double(9.8), new Double(0), null, new Double(0.1)));
		snpAcc.setBounds(460, 216, 54, 43);
		contentPane.add(snpAcc);
		
		JLabel lblGaucheTxt = new JLabel("Gauche :");
		lblGaucheTxt.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		lblGaucheTxt.setBounds(370, 306, 77, 21);
		contentPane.add(lblGaucheTxt);
		
		JLabel lblDroiteTxt = new JLabel("Droite :");
		lblDroiteTxt.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		lblDroiteTxt.setBounds(564, 304, 63, 19);
		contentPane.add(lblDroiteTxt);
		
		JButton btnG = new JButton(toucheGauche);
		btnG.setBackground(Color.WHITE);
		btnG.setFont(new Font("Lucida Sans", Font.PLAIN, 12));
		btnG.setOpaque(true);
		
		btnG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnG.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						toucheGauche = KeyEvent.getKeyText(e.getKeyCode()) ;
						btnG.setText(toucheGauche );
					}
				});
			}
		});
		btnG.setBounds(434, 304, 100, 55);
		contentPane.add(btnG);
		
		JButton btnD = new JButton(toucheDroite);
		btnD.setFont(new Font("Lucida Sans Typewriter", Font.PLAIN, 12));
		btnD.setBackground(Color.WHITE);
		btnD.setBounds(636, 304, 100, 55);
		btnD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnD.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						toucheDroite = KeyEvent.getKeyText(e.getKeyCode()) ;
						btnD.setText(toucheDroite );
					}
				});
			}
		});
		btnG.setOpaque(true);
		contentPane.add(btnD);
		
	}
	public String getToucheGauche() {
		return toucheGauche;
	}

	public void setToucheGauche(String toucheGauche) {
		this.toucheGauche = toucheGauche;
	}

	public String getToucheDroite() {
		return toucheDroite;
	}

	public void setToucheDroite(String toucheDroite) {
		this.toucheDroite = toucheDroite;
	}
}
