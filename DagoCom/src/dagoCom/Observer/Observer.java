package dagoCom.Observer;

import dagoCom.socket.SocketConnexion.States;

public interface Observer {
	public void update(States state);
}
