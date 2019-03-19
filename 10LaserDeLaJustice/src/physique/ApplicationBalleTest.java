package physique;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
/**
 * 
 * @author Jeremy
 *
 */
public class ApplicationBalleTest extends JFrame {

	private JPanel contentPane;
	private SceneTest scene;
	private static  ApplicationBalleTest frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new ApplicationBalleTest();
					frame.setVisible(true);
					frame.scene.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ApplicationBalleTest() {
		setTitle("Test de collision entre balles");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 989, 808);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scene.requestFocusInWindow();
				scene.demarrer();
			}
		});
		btnStart.setBounds(10, 735, 89, 23);
		contentPane.add(btnStart);
		
		JButton btnStop = new JButton("stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scene.arreter();
			}
		});
		btnStop.setBounds(109, 735, 89, 23);
		contentPane.add(btnStop);
		
		scene = new SceneTest();
		scene.setBounds(10, 11, 953, 713);
		contentPane.add(scene);
		
	
	}
}
