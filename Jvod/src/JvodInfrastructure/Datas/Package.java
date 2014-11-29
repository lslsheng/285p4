package JvodInfrastructure.Datas;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Package implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -530208061360449588L;
	private Map<String, String> properities = new HashMap<String, String>();
	private byte[] data;
	private Torrent torrentT;
	private ArrayList<Torrent> Tlist;
	// private class torrent {
	// 	String fileName;
	// 	int size;
	// 	String hashData;
	// 	torrent(String _filename, int _size, String _hashdata){
	// 		fileName = _filename;
	// 		size = _size;
	// 		hashData = _hashdata;
	// 	}
	// }
	public Package(byte[] inData){
		data = inData;
	}

	public void setT(Torrent input){
		torrentT = input;
	}
	
	public Torrent getT(){
		return torrentT;
	}

	public void setTList(ArrayList<Torrent> input){
		Tlist = input;
	}
	
	public ArrayList<Torrent> getTList(){
		return Tlist;
	}

	public void setP(String p, String v){
		properities.put(p, v);
	}
	
	public String getP(String p){
		return properities.get(p);
	}
	
	public void setD(byte[] inData){
		data = inData;
	}
	
	public byte[] getD(){
		return data;
	}
	static public byte[] serialize(Package p){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] bytes = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(p);
		  bytes = bos.toByteArray();
		} catch(Exception e){
			System.out.println("Exception should be handled");
		} finally {
		  try {
		    if (out != null) {
		      out.close();
		    }
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		  try {
		    bos.close();
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		}
		return bytes;
	}
	static public Package deserialize(byte[] bytes){
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		Package p = null;
		try {
		  in = new ObjectInputStream(bis);
		  p = (Package)in.readObject(); 
		} catch(Exception e) {
			System.out.println("Exception should be handled");
		}
		finally {
		  try {
		    bis.close();
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		  try {
		    if (in != null) {
		      in.close();
		    }
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		}
		return p;
	}
}
