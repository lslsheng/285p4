package JvodClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import JvodInfrastructure.Datas.Package;
import JvodInfrastructure.FileServers.FileServer;
import JvodInfrastructure.Handlers.PackageHandler;
import JvodInfrastructure.PackageServers.PackageServer;

public class JvodClientFileServer extends Thread{
	FileServer fs;
	PackageServer ps;
	
	public JvodClientFileServer(int port, Map<String, String> filePaths){
		
	    try {
			System.out.println("Initing ClientFileServer at " + InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    PackageHandler ph = new JCFSPackageHandler();
		this.ps = new PackageServer(port, ph);
		this.fs = new FileServer(filePaths);
	}
	
	public void start(){
		ps.run();
	}
	
	private class JCFSPackageHandler extends PackageHandler{
		public Package handle(Package p){
//			randomLag();
			return fs.handle(p);
		}
	}
	
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
	
	public static void main(String args[]) {
		
		// do unit test multiple servers
		
		Map<String, String> filePaths = new HashMap<String, String>();
		filePaths.put("v.mkv", "/Users/wjkcow/Desktop/v.mkv");
		for(int port = 6000; port < 6001; ++port){
			JvodClientFileServer jcfs = new JvodClientFileServer(port, filePaths);
			jcfs.start();
		}
	}
}
