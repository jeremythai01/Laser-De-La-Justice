package physique;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.SpinnerNumberModel;

public class AplicationTestMiroire extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private SceneTestMiroire scene;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AplicationTestMiroire frame = new AplicationTestMiroire();
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
	public AplicationTestMiroire() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 623, 594);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scene.demarrer();
			}
		});
		btnStart.setBounds(36, 449, 89, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scene.arreter();
			}
		});
		btnStop.setBounds(159, 449, 89, 23);
		contentPane.add(btnStop);
		
		scene = new SceneTestMiroire();
		scene.setBounds(10, 11, 484, 439);
		contentPane.add(scene);
		
		JSpinner spinner = new JSpinner();
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int valeur = (int)spinner.getValue(); 
				scene.setAngle(valeur);
			}
		});
		spinner.setBounds(465, 450, 46, 20);
		contentPane.add(spinner);
		
		JLabel lblAngle = new JLabel("angle");
		lblAngle.setBounds(436, 453, 29, 14);
		contentPane.add(lblAngle);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setModel(new SpinnerNumberModel(new Integer(150), null, null, new Integer(1)));
		spinner_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int valeur = (int)spinner_1.getValue(); 
				scene.setGrosseur(valeur);
			}
		});
		spinner_1.setBounds(465, 481, 46, 20);
		contentPane.add(spinner_1);
		
		JLabel lblGrosseur = new JLabel("grosseur");
		lblGrosseur.setBounds(419, 484, 46, 14);
		contentPane.add(lblGrosseur);
		
	
	}
}
