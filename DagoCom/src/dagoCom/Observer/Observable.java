package dagoCom.Observer;

import dagoCom.socket.SocketConnexion.States;

public interface Observable {
	public void addObserver(Observer ob);
	public void removeObserver(Observer ob);
	public void notifyObserver(States state);
}
