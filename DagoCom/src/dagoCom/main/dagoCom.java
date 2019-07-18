package dagoCom.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class dagoCom {
	
	public static String version = "a0.1";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(System.getProperty("os.name").substring(0, 7)); 
		
		mainMenu frame = new mainMenu();
	}
	
}
