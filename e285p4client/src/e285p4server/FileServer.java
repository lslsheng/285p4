package e285p4server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FileServer {
	private ConcurrentMap<String, SingleFileServer> serverMap;
	synchronized void newFile(String filename, int size, String hash, String path){
		serverMap = new ConcurrentHashMap<String, SingleFileServer>();
		serverMap.putIfAbsent(filename, new SingleFileServer(path));
	}
	
	synchronized void doneFile(String filename){
		serverMap.remove(filename);
	}
	Package handle(Package req){
		String name = req.getP("filename");
		System.out.println("FileServer received request for " + name);
		if(serverMap.containsKey(name)){
			SingleFileServer sfs = serverMap.get(name);
			return sfs.serveData(req);
		} else {
			System.out.println("Not servering this file");
			return null;
		}
	}
	
	class SingleFileServer{
		byte[] fileData;
		AtomicInteger size;
		String path;
		SingleFileServer(String path){
			this.path = path;
		    File file = new File(path);
		    long length = file.length();
		    if (length > Integer.MAX_VALUE) {
		        System.out.println("File is too large.");
		    }
		    fileData = new byte[(int) length];
		    FileInputStream fis;
			try {
				fis = new FileInputStream(file);
			    BufferedInputStream bis = new BufferedInputStream(fis);
			    bis.read(fileData);
			    fis.close();
			    bis.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Package serveData(Package p){
		    int length = Integer.parseInt(p.getP("seg_length"));
		    int offset = Integer.parseInt(p.getP("seg_offset"));
		    System.out.println("Serve file  with offset, length , total length:"
		    + offset + "," + length + "," + fileData.length + " "+ path);

		    byte data[] = Arrays.copyOfRange(fileData, offset, offset + length);
			p.setD(data);
			return p;
		}
		
	}
}
