package dagoCom.com;

import java.io.IOException;
import java.io.InputStream;

public class SerialReader implements Runnable{

	private InputStream in;
	
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
	                System.out.print(new String(buffer,0,len));
	            }
	            
	        }catch ( IOException e ){
	            e.printStackTrace();
	        }            
		}	
	}

}
