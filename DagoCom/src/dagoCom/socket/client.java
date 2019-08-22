package dagoCom.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class client {
	
	private static Socket com;
	
	public static boolean connect(String IP){
		
		try {
			com = new Socket(IP,186);
			com.connect(null, 200);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Alert probleme = new Alert(AlertType.ERROR);
			probleme.setTitle("Erreur");
			probleme.setHeaderText("Une erreur est survenue lors de la connexion au serveur.\nVérifier votre connexion réseau et l'addresse IP.");
			probleme.setContentText(e.getMessage());
			probleme.show();
			e.printStackTrace();
			
		}
		return false;
	}
	
	public static void disconnect() {
		try {
			com.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
