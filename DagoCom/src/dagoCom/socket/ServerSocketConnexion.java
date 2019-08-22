package dagoCom.socket;

import java.io.IOException;
import java.net.ServerSocket;



public class ServerSocketConnexion extends SocketConnexion{
	
	protected ServerSocket server;

	public ServerSocketConnexion(int port) {
		super("localhost", port);
	}
	
	@Override
	public void connect() {
			notifyObserver(States.ATTENTE_CONNEXION);
			new Thread(){
				 @Override public void run () {
						try {
							server = new ServerSocket(port);
							socket = server.accept();
							init();
							notifyObserver(States.CONNECTE);
						} catch (IOException e) {
							e.printStackTrace();
							notifyObserver(States.ERREUR_CONNEXION);
						}	
				 }
			}.start();
	}
	
	@Override
	public void close() {
		try {
			Running = false;
			if(socket != null) {
				timer.cancel();
				timer.purge();
				socket.close();
				server.close();
				listenerThread.join();
				if(Running==false && socket.isClosed() && !listenerThread.isAlive()) {
					notifyObserver(States.DECONNECTE);
				}
			}else {
				server.close();
				notifyObserver(States.DECONNECTE);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void connexionClosed() {
		Running = false;
		timer.cancel();
		timer.purge();
		try {
			socket.close();
			server.close();
			log("ATTENTION: Le client a été déconnecté\nLe serveur va maintenant attendre une nouvelle connexion\n");
			connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
