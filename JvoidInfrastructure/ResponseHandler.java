package JvoidInfrastructure;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.apache.commons.io.*;

public class ResponseHandler {
	String filePath = "";
	Package orignal  = new Package(null);
	String ip = "";
	int portNum = 0;

	ResponseHandler(String _filePath, int _portNum) throws UnknownHostException{
		this.filePath = _filePath;
		this.portNum = _portNum;
		this.ip = InetAddress.getLocalHost().getHostAddress();
	}

	Package register(String filename) throws IOException{
		Package res = new Package(null);
		int size = (int) new File(filePath + filename).length();
		res.setP("op", "registerTorrent");
		res.setP("ip", this.ip);
		res.setP("port", ""+this.portNum);
		res.setT(new Torrent(this.filePath + filename, size));
		orignal = res;
		return res;
	}

	void createTorrentFile (Package P) throws IOException{
		Torrent tempT = P.getT();
		FileOutputStream out = new FileOutputStream(filePath + "torrents/" + tempT.fileName + ".torrent");
		out.write(Package.serialize(P));
		out.close();
	}

	Package startProgram() throws IOException{
		Package res = new Package(null);
		res.setP("op", "start");
		res.setP("ip", this.ip);
		res.setP("port", this.portNum + "");
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<Torrent> listT = new ArrayList<Torrent>();
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	      	String fileName = listOfFiles[i].getName();
	        String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("torrent")){
	         	Path path = Paths.get(filePath + fileName);
				byte[] data = Files.readAllBytes(path);
				Package temp = Package.deserialize(data);
				listT.add(temp.getT());
            }
	      } 
	    }
		res.setTList(listT);
		return res;
	}

	Package endProgram(){
		Package res = new Package(null);
		res.setP("op", "shutDown");
		res.setP("ip", this.ip);
		res.setP("port", this.portNum + "");
		return res;
	}

	Package getPeer(Torrent T){
		Package res = new Package(null);
		res.setP("op", "getPeer");
		res.setP("ip", this.ip);
		res.setP("port", this.portNum + "");
		res.setT(T);
		return res;
	}

	void handle(Package res) throws IOException{
		String opT = res.getP("op");
		String res_message = res.getP("res_message");
		if (res_message != "successful"){
			System.out.println("Fail Message!!!");
		}
		if (opT == "registerTorrent"){
			if (res.getD().toString().equals("success")){
				createTorrentFile(this.orignal);
			} else {
				System.out.println("failed!!!");
			}
		} else if (opT == "start"){



		} else if (opT == "shutDown"){



		} else if (opT == "getPeer"){
//			ArrayList<User> userList = new ArrayList<User>();
//			userList = res.getUserList();
		}
	}
}