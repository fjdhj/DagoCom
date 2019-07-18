package dagoCom.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PrintManager{
	
	static Thread ReaderThread;
	static Thread PrinterThread;
	
	public static void Print(File fichier) {
		if(!Print.isRunning()) {
			ReaderThread = SerialCom.getReaderThread();
			PrinterThread = new Thread(new Print(fichier));
			
		}
	}
	
	private static class Print implements Runnable{
		
		private static boolean print = false;
		private static File fichier;
		
		public Print(File fichier){
			Print.fichier = fichier;
		}
		
		@Override
		public void run() {
			print = true;
			BufferedReader br;
			try {
			      br = new BufferedReader(new FileReader(fichier)); 
			      String ligne = "";
			      while ((ligne = br.readLine()) != null){
			    	  if(!ligne.isEmpty() && !ligne.startsWith(";")) {
			    		  SerialWriter.sendCommand(ligne);
							waitOk();
			    	  }
			     } 
			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			print = false;
		}
	
		public static boolean isRunning() {
			return print;
		}

		private void waitOk() {
			synchronized(ReaderThread){
	            try{
	                System.out.println("Attente de la fin de l'opération...");
	                ReaderThread.wait();
	            }catch(InterruptedException e){
	                e.printStackTrace();
	            }
	 
	            System.out.println("Opération terminée");
	        }
			
		}
	}

}
