package dagoCom.com;

import java.io.IOException;
import java.io.InputStream;

public class SerialReader implements Runnable{

	private InputStream in;
	private boolean debug = false;
	
	public SerialReader(InputStream in) {
		this.in = in;
	}
	
	@Override
	public void run() {
        while(true) {
			byte[] buffer = new byte[1024];
	        int len = -1;
	        
	        //Lecture dans le port
	        try{
	            while ( ( len = in.read(buffer)) > -1 ){
	            	String ligne = new String(buffer,0,len);
	                System.out.print(ligne);
	                isOk(ligne);
	            }
	            
	        }catch ( IOException e ){
	            e.printStackTrace();
	        }            
		}	
	}
	
	private void isOk(String ligne) {
		if(ligne.equals("ok")) {
			this.notifyAll();
			if(debug) {
                System.out.print(ligne);
			}
		}
	}

}
