package dagoCom.graph;

import javax.swing.JOptionPane;

import com.sun.javafx.collections.ChangeHelper;

import dagoCom.Observer.Observer;
import dagoCom.com.SerialCom;
import dagoCom.main.mainDagoCom;
import dagoCom.socket.SocketConnexion;
import dagoCom.socket.SocketConnexion.States;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class graphMenuMapping implements Observer{
	
	@FXML
	private ComboBox<String> CBconType;
	@FXML
	private ComboBox<String> CBbauds;
	@FXML
	private TextField TFip;
	@FXML
	private Button Bcon;
	@FXML
	private SplitMenuButton SMBprint;
	@FXML
	private MenuItem MIprintSd;
	@FXML
	private TextArea TAcons;
	@FXML
	private Button Bsend;
	@FXML
	private TextField TFsend;
	
	
	private ObservableList<String> type;
	private ObservableList<String> bauds = FXCollections.observableArrayList("2400", "9600", "19200", "38400", "57600", "115200", "250000");
	
	private boolean stateBcon = true;
	private boolean local = false;
	private SocketConnexion client;
	Alert probleme = new Alert(AlertType.ERROR);

	private mainDagoCom main;
	
	public graphMenuMapping() {}
	
	@FXML
	public void connect() {
		if(stateBcon){ //true = boutton affiche connexion
			if(!local) {
				if(SerialCom.open()) {
					Bcon.setText("Deconnexion");
					SMBprint.setDisable(false);
					stateBcon = !stateBcon;
				}else{
					JOptionPane.showMessageDialog(null, "Une erreur est survenu lors de la connexion. Esseyez ultÃ¨rieurement", "Erreur", JOptionPane .ERROR_MESSAGE);
				}
			}else {
				client = new SocketConnexion(TFip.getText(),3000);
				client.addObserver(this);
				client.connect();
			}
			
		}else{
			if(!local) {
				if(SerialCom.close()) {
					Bcon.setText("Connexion");
					SMBprint.setDisable(false);
					stateBcon = !stateBcon;
				}else{
					JOptionPane.showMessageDialog(null, "Une erreur est survenu lors de la dÃ©connexion. Esseyez ultÃ¨rieurement", "Erreur", JOptionPane .ERROR_MESSAGE);
				}
			}else {
				client.close();
			}
		}
	}
	
	@FXML
	public void update() {
		System.out.println("Update ...");
		type = SerialCom.getCommPort();
		type.add("Local");
		System.out.println(type);
     	CBconType.setItems(type);

	}

	public void setMainApp(mainDagoCom mainApp) {
		this.main = mainApp;
		
		update(); //Mise a jour de la liste
		
		CBconType.valueProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(arg2.equals("Local")) {
					TFip.setVisible(true);
					TFip.setDisable(false);
					CBbauds.setVisible(false);
					CBbauds.setDisable(true);
					local = true;
				}else {
					TFip.setVisible(false);
					TFip.setDisable(true);
					CBbauds.setVisible(true);
					CBbauds.setDisable(false);
					local = false;
				}}});
		CBbauds.setItems(bauds);
		
	}

	@Override
	public void update(States state) {
		switch(state) {
		case ATTENTE_CONNEXION: //Attend une connexion (ServerSocket)
			break;
		case CONNECTE:			//La connexion réussi
			break;
		case CONNEXION:			//La connexion est en cours mais pas encore réussie
			Bcon.setText("Deconnexion");
			SMBprint.setDisable(false);
			stateBcon = !stateBcon;
			break;
		case DECONNECTE:		//La socket est déconnectée
			Bcon.setText("Connexion");
			SMBprint.setDisable(false);
			stateBcon = !stateBcon;
			break;
		case ERREUR_COMM:		//Une erreur de communication est survenue
			JOptionPane.showMessageDialog(null, "Une erreur de communication est survenue","Erreur",JOptionPane.OK_OPTION,null);
			break;
		case ERREUR_CONNEXION:	//La connexion a échoué
			JOptionPane.showMessageDialog(null, "La connexion a échoué","Erreur",JOptionPane.OK_OPTION,null);
			break;
		case LOG:				//La connexion a reçu des donnée et veut mettre a jour la vue
			break;
		}
	}
	 
	

}
