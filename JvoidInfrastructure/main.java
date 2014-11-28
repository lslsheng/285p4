package JvoidInfrastructure;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

public class main {
	public static void main(String[] args) throws IOException {
		String path = "/Users/Stephen/Desktop/testJava/";
		String filename = "Lec02_ObjectsTypesStyle_2per.pdf";
		ResponseHandler newRH = new ResponseHandler(path, 5750);
		int size = (int) new File(path + "Lec02_ObjectsTypesStyle_2per.pdf").length();
		Package P = newRH.register(filename);
		System.out.println(P.getT().size);
		System.out.println(P.getT().hashData);
		System.out.println(P.getT().fileName);
		System.out.println(newRH.ip);
	}
}
