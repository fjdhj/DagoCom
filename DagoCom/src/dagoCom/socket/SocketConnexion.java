package dagoCom.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import dagoCom.Observer.Observer;

public class SocketConnexion{
	
	protected static final String PING = "$$ping$$";
	public static enum States{
		ATTENTE_CONNEXION,CONNECTE,DECONNECTE,CONNEXION,LOG,ERREUR_CONNEXION,ERREUR_COMM,ERREUR_CONSTRUCTEUR;
	}
	
	protected SocketListener listener;
	protected Thread listenerThread;
	protected Socket socket;
	protected BufferedOutputStream out;
	private String adresse;
	protected String logs = "";
	protected boolean Running;
	protected int port;
	protected Timer timer = new Timer();
	protected ArrayList<Observer> observers = new ArrayList<Observer>();

	public SocketConnexion(String adresse, int port) {
		this.adresse = adresse;
		this.port = port;
	}
	
	//Connecte la Socket à l'Hôte et notifie les Observers
	public void connect() {		
		notifyObserver(States.CONNEXION);
		new Thread(){
			 @Override public void run () {
					try {
						socket = new Socket(adresse,port);
						init();
						notifyObserver(States.CONNECTE);
					 } catch (UnknownHostException e) {
						notifyObserver(States.ERREUR_CONNEXION);
				        e.printStackTrace();
				     }catch (IOException e) {
				     }						
			 }
		}.start();
	}
	
	//Initialise les Objets de la classe une fois connecté
	protected void init() {
		System.out.println("Initialisation");		
		Running = true;
		timer = new Timer();
		listener = new SocketListener();
		listenerThread = new Thread(listener);
		listenerThread.start();
		
		try {
			out = new BufferedOutputStream(socket.getOutputStream());
			//Timer qui envoie un message a intervalle régulier pour détecter une perte de connexion
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				  public void run() {
					try {
						if(!socket.isClosed()) {
							out.write(new String(PING).getBytes());
							out.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
						connexionClosed();//<- La connexion est interrompue
					}
				  }
			}, 2*1000, 2*1000);
		
		} catch (IOException e1) {
			e1.printStackTrace();
			connexionClosed();
		}
		notifyObserver(States.CONNECTE);
	}
	
	//Envoie le message dans la socket
	protected void send(String message) {
		if(Running) {
			try {
				out.write(message.getBytes());
				out.flush();
				log("SENT: "+message+"\n");
			} catch (SocketException e) {
				e.printStackTrace();
				connexionClosed();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}

	protected String getLogs() {
		return logs;
	}

	protected void log(String string) {
		logs+=string;
		notifyObserver(States.LOG);
	}
	//Ferme les flux et arrete les Threads
	protected void close() {
		try {
			Running = false;
			if(socket!=null) {
				socket.close();
				timer.cancel();
				timer.purge();
				listenerThread.join();
			}
			out.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
			notifyObserver(States.DECONNECTE);
	}
	
	/*Appelé lorsque la connexion échoue ou est annulée (évite des nullPointerException dans le ServerSocket)
	* Peut d'ailleurs etre remplacé par close en mettant des if( bidule!= null)
	*/
	protected void connexionClosed() {
		try {
			Running = false;
			timer.cancel();
			timer.purge();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		notifyObserver(States.ERREUR_COMM);
	}
	
	/*Délais avant de re lire l'entree de la Socket (optimise les Perfs)*/
	protected void delay() {
        try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Ajoute un Observeur
	public void addObserver(Observer ob) {
		observers.add(ob);
	}
	
	//Enleve un Observeur
	public void removeObserver(Observer ob) {
		observers.remove(ob);
	}

	//Notifie un Observeur
	public void notifyObserver(States state) {
		for(Observer obs : observers)
		      obs.update(state);
	}
	
	
	
	
	// Thread qui écoute la socket
	private class SocketListener implements Runnable{
		private volatile String toWait = "";
		private BufferedInputStream bis = null;

		@Override
		public void run() {
			String stringBuffer = "";
			try {
				bis = new BufferedInputStream(socket.getInputStream());
				while(Running) {
					 if(bis.available()>0) {
						 byte[] b = new byte[bis.available()];
						 int stream = bis.read(b);
						 stringBuffer = new String(b,0,stream);
						 
						 /*FILTRAGE DU PING*/
						 while(stringBuffer.contains(PING)) {
								stringBuffer = stringBuffer.substring(0, stringBuffer.indexOf(PING)) + stringBuffer.substring(stringBuffer.indexOf(PING)+PING.length());
							 }
						 /*FIN FILTRAGE*/
						 
						 if(stringBuffer.length()>0) {
							log(socket.getInetAddress().getHostAddress()+":  "+stringBuffer+"\n");	
						 }
					stringBuffer = "";
					}
					delay();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					bis.close();
				} catch (IOException e) {}
			}
		}
	}
}
