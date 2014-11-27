package e285p4server;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Torrent implements Serializable{
	String fileName;
	int size;
	String hashData;
	Torrent(String _filename, int _size){
		this.fileName = _filename;
		this.size = size;
		Path path = Paths.get(_filename);
		byte[] data = Files.readAllBytes(path);
		this.hashData = getMD5(data);
	}


	public static String getMD5(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input);
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

public class ResponseHandler {
	String filePath = "";
	Package orignal  = new Package(null);
	String ip = "";
	int portNum = 0;

	ResponseHandler(String _filePath, int _portNum, String _ip){
		this.filePath = _filename;
		this.portNum = _portNum;
		this.ip = _ip;
	}

	Package register(String filename, int size){
		Package res = new Package(null);
		res.setP("op", "registerTorrent");
		res.setP("ip", this.ip);
		res.setP("port", this.portNum.toString());
		res.setT(new Torrent(fileName, size));
		orignal = res;
		return res;
	}

	void createTorrentFile (Package P){
		FileOutputStream out = new FileOutputStream(P.getT.filename + ".torrent");
		out.write(Package.serialize(P));
		out.close();
	}

	Package startProgram(){
		Package res = new Package(null);
		res.setP("op", "start");
		res.setP("ip", this.ip);
		res.setP("port", this.portNum.toString());
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();
		ArrayList<Torrent> listT = new ArrayList<Torrent>();
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	      	String fileName = listOfFiles[i].getName();
	        String extension = FilenameUtils.getExtension();
            if (extension.equals("torrent"){
	         	Path path = Paths.get(filePath + fileName);
				byte[] data = Files.readAllBytes(path);
				Package temp = Package.deserialize(data);
				listT.add(temp.getT);
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
		res.setP("port", this.portNum.toString());
		return res;
	}

	Package getPeer(Torrent T){
		Package res = new Package(null);
		res.setP("op", "getPeer");
		res.setP("ip", this.ip);
		res.setP("port", this.portNum.toString());
		res.setT(T);
		return res;
	}

	void handle(Package res){
		String opT = res.getP("op");
		if (opT == "registerTorrent"){
			if (res.getD.toString().equals("success")){
				createTorrentFile(this.orignal);
			} else {
				System.out.println("failed!!!");
			}
		} else if (opT == "start"){



		} else if (opT == "shutDown"){



		} else if (opT == "getPeer"){
			ArrayList<User> userList = new ArrayList<User>();
			userList = res.getUserList();
		}
	}
}
