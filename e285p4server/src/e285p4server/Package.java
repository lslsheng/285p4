package e285p4server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Package implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -530208061360449588L;
	private Map<String, String> properities = new HashMap<String, String>();
	private byte[] data;
	
	Package(byte[] inData){
		data = inData;
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
	public static void main(String[] args){
		Package p = new Package(null);
		p.setP("hello", "world");
		byte[] bytes = Package.serialize(p);
		System.out.println(bytes);
		Package p2 = Package.deserialize(bytes);
		System.out.println(p2.getP("hello"));
	}
	
}
