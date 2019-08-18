package dagoCom.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dagoCom.com.PrintManager;
import dagoCom.com.SerialCom;
import dagoCom.com.SerialWriter;

public class mainMenu extends JFrame {
	
	private JSplitPane JSPmain;	
	protected JFrame ref = this;
	
	private JPanel cons = new JPanel();
	private JPanel other = new JPanel();
	
	private static JTextArea JTAcons = new JTextArea();
	private JScrollPane scroll = new JScrollPane(JTAcons);
	private JPanel JPsend = new JPanel();
	private JTextField JTFenter = new JTextField();
	private JButton JBsend = new JButton("Envoyer");
	
	private JPanel JPcon = new JPanel();
	private JComboBox JCBcom = new JComboBox(new String[] {"COM1", "COM5"});
	private JComboBox JCBbauds = new JComboBox(new String[] {"2400", "9600", "19200", "38400", "57600", "115200", "250000"});
	private JButton JBcon = new JButton("Connexion");
	private boolean stateJBcon = true;
	private JButton JBsd = new JButton("SD");
	
	private JTextField JTFip = new JTextField();
	
	private JMenuBar menuBar = new JMenuBar();
	
	private JMenu JMoption = new JMenu("Option");
	private JMenuItem JMIparam = new JMenuItem("Paramètre");
	private static JCheckBoxMenuItem JCBMIdebug = new JCheckBoxMenuItem("Console debug");
	
	
	public mainMenu() {
		this.setTitle("DagoCom " + dagoCom.version);
		this.setSize(1000, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Ajout et gestion contenu JPanel cons -> ensemble consol
		JTAcons.setEditable(false);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JBsend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//PROGRAME POUR ENVOYER A L'IMPRIMANTE LA COMMANDE
				SerialWriter.sendCommand(JTFenter.getText());
			}			
		});
		/*
		 * Ajout composant aux JPanels
		 */
		JPsend.setLayout(new BorderLayout());
		JPsend.add(JTFenter, BorderLayout.CENTER);
		JPsend.add(JBsend, BorderLayout.LINE_END);	
		cons.setLayout(new BorderLayout());
		cons.add(scroll, BorderLayout.CENTER);
		cons.add(JPsend, BorderLayout.SOUTH);
		
		/*
		 * Ajout et gestion contenu JPanel other -> le reste de la JFrame (a définir)
		 */
		
		//1er partie : connexion imprimante
		JCBcom.setSelectedIndex(1); //On sélectionne l'item 1 par défaut soit COM5
		JCBbauds.setSelectedIndex(6); //On sélectionne l'item 6 par défaut soit 250000
		JBcon.addActionListener(new ActionListener() { //Bouton connexion/deconnexion --> connect a l'imprimante
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(stateJBcon){ //true = boutton affiche connexion
					JBsd.setEnabled(true);
					if(SerialCom.open()) {
						JBcon.setText("Deconnexion");
						stateJBcon = !stateJBcon;
					}else{
						JOptionPane.showMessageDialog(null, "Une erreur est survenu lors de la connexion. Esseyez ultèrieurement", "Erreur", JOptionPane .ERROR_MESSAGE);
					}
					
				}else{
					JBsd.setEnabled(false);
					if(SerialCom.close()) {
						JBcon.setText("Connexion");
						stateJBcon = !stateJBcon;
					}else{
						JOptionPane.showMessageDialog(null, "Une erreur est survenu lors de la déconnexion. Esseyez ultèrieurement", "Erreur", JOptionPane .ERROR_MESSAGE);
					}
				}
			}			
		});
		
		//boutton pour l'impression
		JBsd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				jfc.setMultiSelectionEnabled(false);
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int rep = jfc.showDialog(ref, "Blabla");
				if(rep == JFileChooser.APPROVE_OPTION) {
					PrintManager.Print(jfc.getSelectedFile());
				}
				
			}});
		
		JBsd.setEnabled(false);
		

		if(dagoCom.optionTXT.getContent(0).equals("client")) {
			//Si client
			/*
			 * Ajouter un choix entre Connexion USB ou via socket
			 */
			
			JTFip.setName("IP");
			
			JCBcom.addItem("Local");
			
			JCBcom.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(JCBcom.getSelectedItem().equals("Local")) {
						JPcon.remove(JBsd);
						//JPcon.add(comp);
					}
					
				}});
			
		}else {
			//Si serveur
			
		/*
		 * Ajouter l'addresse ip local en dessou du reste (JPanel -> JPcon)
		 */
		
		}
			
		JPcon.add(JCBcom);
		JPcon.add(JCBbauds);
		JPcon.add(JBcon);
		JPcon.add(JBsd);
		
		JPcon.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		//Ajout JPanel dans other
		other.setLayout(new BorderLayout());
		other.add(JPcon, BorderLayout.NORTH);
		
		
		/*
		 * Gestion JMenuBar
		 */
		JMIparam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
			
		});
		
		JMoption.add(JMIparam);
		JMoption.add(JCBMIdebug);
		
		menuBar.add(JMoption);
		
		/*
		 * Gestion JFrame avec spliter
		 */
		//Definition paramètre Spliter
		JSPmain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, other, cons);
		JSPmain.setDividerLocation(675);
		//Ajout du tout dans la JFrame
		this.add(JSPmain);
		this.setJMenuBar(menuBar);
		this.setVisible(true);
		
		
	}
	
	public static void addText(String str) {
		JTAcons.append(str);

	}

	public static boolean getDebug() {
		return(JCBMIdebug.isSelected());
	}
}
