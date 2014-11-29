package JvodInfrastructure.FileServers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import JvodInfrastructure.Datas.Package;
import JvodInfrastructure.Handlers.ResponseHandler;

public class FileWriter {
	private static int SEG_LEN = 1024*1024;
	private String filename;
	private AtomicInteger size = new AtomicInteger();
	private String path;
	private ConcurrentLinkedQueue<Package> requests = new ConcurrentLinkedQueue<Package>();
	private DiskWriter writerThread;
	
	public FileWriter(String filename, int size, String path){
		this.filename = filename;
		this.size.set(size);
		//this.hash = hash;
		this.path = path;
		writerThread = new DiskWriter(path, size);
		writerThread.start();
		requests.addAll(requestFactory(filename, size, path));
	}
	
	static private List<Package> requestFactory(String filename, int size, String path){
		Integer offset = 0;
		List<Package> reqs = new ArrayList<Package>();
		System.out.println("Initing requests for file " + filename);
		while( offset < size){
			Integer seg_length = SEG_LEN;
			if(seg_length > size - offset){
				seg_length = size - offset;
			}
			Package req = new Package(null);
			req.setP("package_type", "data_request");
			req.setP("filename", filename);
			req.setP("seg_offset", offset.toString());
			req.setP("seg_length", seg_length.toString());
			offset = offset + seg_length;
			reqs.add(req);
		}
		System.out.println("Total number of requests needed " + reqs.size());
		return reqs;
	}
	
	// get the request for the next part of file
	// this method should be thread safe
	// this function will return null if the file is all requested
	// package_type -> data_request
	// filename -> <name>
	// seg_offset -> <offset>
	// seg_length -> <seg_length>
	public Package getRequest(){
		Package p = requests.poll();
		if(p != null){
			System.out.println("New request polled " + p.getP("filename")
				+ " offset "+ p.getP("seg_offset") + " length " + p.getP("seg_length"));
			System.out.println("Request remain " + requests.size());
		}
		return p;
	}
	
	
	public ResponseHandler getHandler(){		
		return new FileResponseHandler();
	}
	
	
	private class DiskWriter extends Thread{
		private ConcurrentLinkedQueue<Package> responses = new ConcurrentLinkedQueue<Package>();
		private AtomicInteger receivedSize = new AtomicInteger();
		byte[] buffer;
		String path;
		DiskWriter(String path, int size){
			this.buffer = new byte[size];
			this.path = path;
			this.receivedSize.set(0);
		}
		@Override
		public void run() {
			writeBuffer();
			flush();
		}		
		
		public synchronized void newPackage(Package p){
			System.out.println(p);
			System.out.println("Responses size " + responses.size());
			responses.add(p);
			notify();
		}
		private synchronized void writeBuffer(){
			while(receivedSize.get() < size.get()){
				while(responses.isEmpty()){
					try {
						System.out.println("Writer buffer going to sleep ");
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Package p = responses.poll();
				if(p != null){
					write(p);			
					System.out.println("Writer Thread working " + receivedSize + "," + size);
					System.out.println("Response remain " + responses.size());
				}
				System.out.println("Buffer write done, isFinish " + receivedSize.equals(size));

			}

		}
		
		// write the package for a seg of file
		// this method should be thread safe
		// package_type -> data_response
		// file_writer_id -> 1
		// 
		private void write(Package p){
		     int length = Integer.parseInt(p.getP("seg_length"));
		     int offset = Integer.parseInt(p.getP("seg_offset"));
		     receivedSize.addAndGet(length);
		     System.arraycopy(p.getD(), 0, buffer, offset, length);
		}
		private void flush(){
			System.out.println("going to flush file " + path);
			FileOutputStream fos;
			BufferedOutputStream bos;
			try {
				fos = new FileOutputStream(path);
				bos = new BufferedOutputStream(fos);
				bos.write(buffer);
				bos.flush();
				buffer = null;
				bos.close();
				fos.close();
				System.out.println("File write sucessfully " + path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	
	class FileResponseHandler extends ResponseHandler{
		public void handle(Package p){
			System.out.println("Response received " + p.getP("filename")
					+ " offset "+ p.getP("seg_offset") + " length " + p.getP("seg_length"));
			writerThread.newPackage(p);
		}
	}
	
	static class UnitTest{
		static FileWriter fw;
		static FileServer fs;
		static void randomLag(){
			Random generator = new Random();
			int i = generator.nextInt(1000);
            try {
				Thread.sleep(i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		static private class Client extends Thread{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Package req;
				while((req=fw.getRequest()) != null){
					randomLag();
					fw.getHandler().handle(fs.handle(req));
				}
			}
			
		}
		
		
		
		static void run(){
		    File file = new File("/Users/wjkcow/Desktop/t.dmg");
		    long length = file.length();
		    if (length > Integer.MAX_VALUE) {
		        System.out.println("File is too large.");
		    }
		    System.out.println(length);
			fw = new FileWriter("t.dmg", (int) (length), "/Users/wjkcow/Desktop/t1.dmg");
			fs = new FileServer();
			//fs.newFile("t.dmg", (int) (length), "not used", "/Users/wjkcow/Desktop/t.dmg");
			fs.newFile("t.dmg", "/Users/wjkcow/Desktop/v.mkv");
			Thread t1 = new Client();
			Thread t2 = new Client();
			Thread t3 = new Client();
			Thread t4 = new Client();
			Thread t5 = new Client();
			Thread t6 = new Client();
			t1.start();
//			t2.start();
//			t3.start();
//			t4.start();
//			t5.start();
//			t6.start();
			
		}
		
		
	}
	
	public static void main(String[] args){
		UnitTest.run();
	}
}
