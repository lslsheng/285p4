package e285p4server;

public class FileWriter {
	private static int SEG_LEN = 1024*1024;
	
	void FiLeWriter(String filename, int size, String hash, String path){
		
	}
	
	// get the request for the next part of file
	// this method should be thread safe
	// this function will return null if the file is all requested
	// package_type -> data_request
	// file_writer_id -> 1
	// filename -> <name>
	// file_length -> <length>
	// seg_offset -> <offset>
	// seg_length -> <seg_length>
	Package getRequest(){
		
		return null;
	}
	
	// write the package for a seg of file
	// this method should be thread safe
	// package_type -> data_response
	// file_writer_id -> 1
	// 
	void write(Package p){
		
		
	}
	
	boolean writeFinish(){
		return true;
	}
	
}
