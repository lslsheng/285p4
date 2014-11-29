package JvodInfrastructure.FileServers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import JvodInfrastructure.Datas.Package;
public class FileServer {
//	private ConcurrentMap<String, SingleFileServer> serverMap;
	private ConcurrentMap<String, String> filePath;
//
//	synchronized void newFile(String filename, int size, String hash, String path){
//		if(serverMap == null){
//			serverMap = new ConcurrentHashMap<String, SingleFileServer>();
//		}
//		serverMap.putIfAbsent(filename, new SingleFileServer(path));
//	}
	
//	
//	synchronized void doneFile(String filename){
//		serverMap.remove(filename);
//	}
	public FileServer(){
		filePath = new ConcurrentHashMap<String, String>();
	}
	public FileServer (Map<String, String> filePath){
		this.filePath = new ConcurrentHashMap<String, String>();

		for(String filename : filePath.keySet()){
			this.filePath.putIfAbsent(filename, filePath.get(filename));
		}
		
	}
	synchronized void newFile(String filename, String path){
		if(filePath == null){
			filePath = new ConcurrentHashMap<String, String>();
		}
		filePath.putIfAbsent(filename, path);
	}
	
	public synchronized Package handle(Package req){
		String filename = req.getP("filename");
	    String path = filePath.get(filename);
	    int length = Integer.parseInt(req.getP("seg_length"));
	    int offset = Integer.parseInt(req.getP("seg_offset"));
	    File file = new File(path);
	    System.out.println("Serve file  with offset, length , total length:"
	    + offset + "," + length + "," + file.length() + " "+ path);

	    RandomAccessFile raFile;
	    byte data[] = new byte[length];
		try {
			raFile = new RandomAccessFile(path, "r");
		    raFile.seek(offset);
		    raFile.read(data);
		    raFile.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		req.setD(data);
		return req; 
	}
//	Package handle(Package req){
//		String name = req.getP("filename");
//		System.out.println("FileServer received request for " + name);
//		if(serverMap.containsKey(name)){
//			SingleFileServer sfs = serverMap.get(name);
//			return sfs.serveData(req);
//		} else {
//			System.out.println("Not servering this file");
//			return null;
//		}
//	}
	
//	class SingleFileServer{
//		byte[] fileData;
//		AtomicInteger size;
//		String path;
//		SingleFileServer(String path){
//			this.path = path;
//		    File file = new File(path);
//		    long length = file.length();
//		    if (length > Integer.MAX_VALUE) {
//		        System.out.println("File is too large.");
//		    }
//		    fileData = new byte[(int) length];
//		    FileInputStream fis;
//			try {
//				fis = new FileInputStream(file);
//			    BufferedInputStream bis = new BufferedInputStream(fis);
//			    bis.read(fileData);
//			    fis.close();
//			    bis.close();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		void load(){
//		    File file = new File(path);
//		    long length = file.length();
//		    if (length > Integer.MAX_VALUE) {
//		        System.out.println("File is too large.");
//		    }
//		    fileData = new byte[(int) length];
//		    FileInputStream fis;
//			try {
//				fis = new FileInputStream(file);
//			    BufferedInputStream bis = new BufferedInputStream(fis);
//			    bis.read(fileData);
//			    fis.close();
//			    bis.close();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		void clear(){
//			fileData = null;
//		}
//		Package serveData(Package p){
//			load();
//		    int length = Integer.parseInt(p.getP("seg_length"));
//		    int offset = Integer.parseInt(p.getP("seg_offset"));
//		    System.out.println("Serve file  with offset, length , total length:"
//		    + offset + "," + length + "," + fileData.length + " "+ path);
//
//		    byte data[] = Arrays.copyOfRange(fileData, offset, offset + length);
//			p.setD(data);
//			clear();
//			return p;
//		}
//		
//	}
}
