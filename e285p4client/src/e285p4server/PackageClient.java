package e285p4server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PackageClient {
	private String host;
	private int port;
	private ResponseHandler rh;
	PackageClient(String host, int port, ResponseHandler rh){
		this.host = host;
		this.port = port;
		this.rh = rh;
	}
	
	void sendPackage(Package p){
		byte[] bytes = Package.serialize(p);
		Package res = null;
		try {
			Socket socket = new Socket(host, port);
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeInt(bytes.length);
			System.out.println("write length "+ bytes.length);
			out.write(bytes);
			out.flush();
			
			
		    DataInputStream in = new DataInputStream(socket.getInputStream());
		    int pSize = in.readInt();
//			System.out.println("Check Point");

		    byte[] packageBytes = new byte[pSize];
		    in.readFully(packageBytes);
		    res = Package.deserialize(packageBytes);
		    // get other 
			out.close();
		    in.close();
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rh.handle(res);
		
	}
	public static void main(String[] args) {
		PackageClient pc = new PackageClient("127.0.0.1", 7777, new ResponseHandler());
		Package p = new Package(null);
		p.setP("hello", "world");
		pc.sendPackage(p);
	}
}
