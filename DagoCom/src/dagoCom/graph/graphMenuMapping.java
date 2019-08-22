package dagoCom.graph;

import javax.swing.JOptionPane;

import com.sun.javafx.collections.ChangeHelper;

import dagoCom.com.SerialCom;
import dagoCom.main.mainDagoCom;
import dagoCom.socket.client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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

public class graphMenuMapping{
	
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
					JOptionPane.showMessageDialog(null, "Une erreur est survenu lors de la connexion. Esseyez ultèrieurement", "Erreur", JOptionPane .ERROR_MESSAGE);
				}
			}else if(client.connect(TFip.getText())) {
					Bcon.setText("Deconnexion");
					SMBprint.setDisable(false);
					stateBcon = !stateBcon;
				}
			
		}else{
			if(!local) {
				if(SerialCom.close()) {
					Bcon.setText("Connexion");
					SMBprint.setDisable(false);
					stateBcon = !stateBcon;
				}else{
					JOptionPane.showMessageDialog(null, "Une erreur est survenu lors de la déconnexion. Esseyez ultèrieurement", "Erreur", JOptionPane .ERROR_MESSAGE);
				}
			}else if(client.connect(TFip.getText())) {
				Bcon.setText("Deconnexion");
				SMBprint.setDisable(false);
				stateBcon = !stateBcon;
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
	 
	

}
