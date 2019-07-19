package dagoCom.com;

import java.io.IOException;
import java.io.InputStream;

import dagoCom.main.mainMenu;

public class SerialReader implements Runnable{

	private InputStream in;
	private boolean debug = false;
	public static boolean running = true;
	
	public SerialReader(InputStream in) {
		this.in = in;
	}
	
	@Override
	public void run() {
        while(running) {
        	System.out.println("Thread Reader lancé");
			byte[] buffer = new byte[1024];
	        int len = -1;
	        
	        //Lecture dans le port
	        try{
	            while ( ( len = in.read(buffer)) > -1 ){
	            	String ligne = new String(buffer,0,len);
	                System.out.print(ligne);
	                
	                if(ligne.equals("ok")) {
	                	isOk();
	                }else if(ligne.equals("T:")) {
	                	
	                }else {
	                	mainMenu.addText(ligne);
	                }
   
	            }
	            
	        }catch ( IOException e ){
	            e.printStackTrace();
	        }            
		}	
	}
	
	private void isOk() {
		this.notifyAll();
			
		}
	
	public static void stop(){
		running = false;
	}
	}


