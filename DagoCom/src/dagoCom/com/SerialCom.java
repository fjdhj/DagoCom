package dagoCom.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialCom {
	public static CommPortIdentifier portIdentifier;
	public static CommPort commPort;
	public static SerialPort serialPort;
	public static InputStream in;
	
	private static SerialReader Reader; 
	private static SerialWriter Writer;
	private static Thread ReaderThread;
	private static Thread serialThread;

	
	public static boolean open() {
		try {
			
			portIdentifier = CommPortIdentifier.getPortIdentifier("COM5");
			if(portIdentifier.isCurrentlyOwned()) {
				System.err.println("Une erreure est survenu : le port est déja utilisé par une autre application (" + portIdentifier.getCurrentOwner() + ")");
				
			}else{
				System.out.println(portIdentifier.getCurrentOwner());
				commPort = portIdentifier.open("DagoCom", 2000);
				System.out.println(portIdentifier.getCurrentOwner());
				
				serialPort = (SerialPort) commPort;
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
				serialPort.setSerialPortParams(250000, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			    in = serialPort.getInputStream();
				
				Reader = new SerialReader(in);
				ReaderThread = new Thread(Reader);
				ReaderThread.start();
				
				Writer = new SerialWriter();
				serialThread = new Thread(Writer);
				serialThread.start();
				return true;
			}
			

		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUseException e) {
			new JOptionPane();
			// TODO Auto-generated catch block
			JOptionPane PortInUseException = new JOptionPane();
			PortInUseException.showMessageDialog(null, "Une erreur est survenu lors de la connexion : le port est deja en cours d'ultisation part une autre appliation", "Erreur", JOptionPane .ERROR_MESSAGE);
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

		
	}
	
	public static Thread getReaderThread() {
		return ReaderThread;
	}
	
	public static boolean close() {
		
		try {
		serialThread.interrupt();
		SerialWriter.stop();
		
		ReaderThread.interrupt();
		SerialReader.stop();
	/*		serialPort.close();
			commPort.close();*/
			in.close();
		} catch (IOException | SecurityException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
}
