package dagoCom.main;

import java.io.File;

public class dagoCom {
	
	public static String version = "a0.2";
	public static String[] defVal = {"run=client", "conect=serial"};
	
	public static fileReader optionTXT = new fileReader();

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(System.getProperty("os.name").substring(0, 7)); 
		
				optionTXT.init(new File("option.txt"), defVal);
		
		mainMenu frame = new mainMenu();
	}
	
}
