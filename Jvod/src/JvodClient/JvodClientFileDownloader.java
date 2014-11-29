package JvodClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JvodInfrastructure.Datas.Torrent;
import JvodInfrastructure.Datas.User;
import JvodInfrastructure.Datas.Package;
import JvodInfrastructure.FileServers.FileWriter;
import JvodInfrastructure.PackageServers.PackageClient;

public class JvodClientFileDownloader {

	
	// this method will block the caller thread
	// return true if successful
	boolean download(Torrent t, List<User> users, String writePath){
		FileWriter fw = new FileWriter(t.fileName, t.size, writePath);
		List<Worker> workers = new ArrayList<Worker>();
		System.out.println("Find " + users.size() + " Peers");
		for(User u : users){
			Worker w = new Worker(u, fw);
			workers.add(w);
			w.start();
		}
		
		for(Worker w : workers){
			try {
				w.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.err.println("Joint fail");
				e.printStackTrace();
			}
		}
		System.out.println("Download finish");
	
		return true;
	}
	
	
	private class Worker extends Thread{
		FileWriter fw;
		User user;
		Worker(User u, FileWriter fw){
			this.fw = fw;
			this.user = u;
		}
		@Override
		public void run(){
			System.out.println("Run work thread for " + user.ip + " "+ user.port );
			PackageClient pc = new PackageClient(user.ip, user.port, fw.getHandler());
			Package p = fw.getRequest();
			while(p != null){
				pc.sendPackage(p);
				p = fw.getRequest();

			}
		}
		
	}
	
	public static void main(String args[]) {
		
		// do unit test multiple servers
		Torrent t = null;
		try {
			t = new Torrent("t.dmg", 59520083);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String writePath = "/Users/wjkcow/Desktop/t1.dmg";
		List<User> users = new ArrayList<User>();
		for(int port = 6000; port < 6001; ++port){
			User u = new User(port, "127.0.0.1");
			users.add(u);
		}
		
		JvodClientFileDownloader jcfd = new JvodClientFileDownloader();
		jcfd.download(t, users, writePath);
	}
}
