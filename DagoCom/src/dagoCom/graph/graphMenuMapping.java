package dagoCom.graph;

import javax.swing.JOptionPane;

import dagoCom.com.SerialCom;
import dagoCom.main.mainDagoCom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class graphMenuMapping{
	
	@FXML
	private ComboBox<String> CBconType;
	@FXML
	private ComboBox<String> CBbauds;
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
	
	
	private ObservableList<String> type = FXCollections.observableArrayList("COM1","COM5","Local");
	private ObservableList<String> bauds = FXCollections.observableArrayList("2400", "9600", "19200", "38400", "57600", "115200", "250000");
	
	private boolean stateBcon = true;
	
	private mainDagoCom main;
	
	public graphMenuMapping() {}
	
	@FXML
	public void connect() {
		if(stateBcon){ //true = boutton affiche connexion
			if(SerialCom.open()) {
				Bcon.setText("Deconnexion");
				SMBprint.setDisable(false);
				stateBcon = !stateBcon;
			}else{
				JOptionPane.showMessageDialog(null, "Une erreur est survenu lors de la connexion. Esseyez ultèrieurement", "Erreur", JOptionPane .ERROR_MESSAGE);
			}
			
		}else{
			if(SerialCom.close()) {
				Bcon.setText("Connexion");
				SMBprint.setDisable(false);
				stateBcon = !stateBcon;
			}else{
				JOptionPane.showMessageDialog(null, "Une erreur est survenu lors de la déconnexion. Esseyez ultèrieurement", "Erreur", JOptionPane .ERROR_MESSAGE);
			}
		}
	}
	
	
	public void setMainApp(mainDagoCom mainApp) {
		this.main = mainApp;
		CBconType.setItems(type);
		CBbauds.setItems(bauds);
	}

}
