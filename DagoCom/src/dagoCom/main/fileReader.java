package dagoCom.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class fileReader {
	
	protected File path;
	protected String[] defVal;
	protected String[] content;
	
	public void init(File path, String[] defVal) {
		this.path = path;
		this.defVal = defVal;
		
		if(!path.exists()) {
			createFile();
		}
		
		update();
		checkFile();
		
	}

	
	public void update() {
		BufferedReader br = null;
		try {
			content=null;
			br = new BufferedReader(new FileReader(path));
			String line;
			String brut = "";
			int i = 0;
			while((line = br.readLine()) != null){
				brut += line + "\n";
				i++;
			}
			content = brut.split("\n");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public boolean checkFile() {
		for(int i = 0; i < content.length; i++) {
			if(!(content[i].substring(0, content[i].lastIndexOf("="))).equals(defVal[i].substring(0, defVal[i].lastIndexOf("=")))) {
				System.out.println(path.toString() + " est incorrecte");
				System.out.println(content[i].substring(0, content[i].lastIndexOf("=")));
				System.out.println(defVal[i].substring(0, defVal[i].lastIndexOf("=")));
				path.delete();
				createFile();
				update();
				
				return false;
			}
				
		}
		return true;
	}

	
	private void createFile() {
		System.out.println("Création de : " + path.toString());
		BufferedWriter bw = null;
		try {
			 bw = new BufferedWriter(new FileWriter(path));
			for(int i = 0; i<defVal.length; i++) {
				bw.write(defVal[i] + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public String getContent(int line) {
		return content[line].substring(content[line].lastIndexOf("=")+1);
	}
	
	
	public String getContent(String val) {
		for(int i = 0; i < defVal.length; i++){
			if((defVal[i]).substring(0, defVal[i].lastIndexOf("=")).equals(val)) {
				return content[i].substring(content[i].lastIndexOf("=")+1);
			}
		}
		return null;
	}
}
