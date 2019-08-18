package dagoCom.main;

import java.io.IOException;

import dagoCom.graph.graphMenuMapping;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class mainDagoCom extends Application {
	
	private Stage stagePrincipale;
	private BorderPane conteneurPrincipale;

	@Override
	public void start(Stage primaryStage) {
		stagePrincipale = primaryStage;
		stagePrincipale.setTitle("DagoCom");
		
		initConteneurPrincipale();
		initGraphMenu();
	}

	private void initConteneurPrincipale() {
		FXMLLoader loader = new FXMLLoader();
		
		loader.setLocation(mainDagoCom.class.getResource("/dagoCom/graph/MenuBar.fxml"));
		try {
			conteneurPrincipale = (BorderPane) loader.load();
			
			Scene scene = new Scene(conteneurPrincipale);
			
			stagePrincipale.setScene(scene);
			
			stagePrincipale.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	private void initGraphMenu() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(mainDagoCom.class.getResource("/dagoCom/graph/graphMenu.fxml"));
		

		try {
			AnchorPane graphMenu = (AnchorPane) loader.load();
			conteneurPrincipale.setCenter(graphMenu);
			
			graphMenuMapping controleur = loader.getController();
			controleur.setMainApp(this);	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
