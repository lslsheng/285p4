package JvodInfrastructure.Handlers;

import JvodClient.JvodClientFileDownloader;
import JvodClient.JvodClientFileServer;
import JvodInfrastructure.Datas.Package;
import JvodInfrastructure.Datas.Torrent;
import JvodInfrastructure.Datas.User;
import JvoidInfrastructure.constData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.apache.commons.io.*;

public class ResponseHandler {
  Package orignal  = new Package(null);
  String ip = "";
  boolean endOfProgram;
  JvodClientFileServer localServer = null;
  
  public ResponseHandler() throws UnknownHostException{
   
  }

  public ResponseHandler(String _filePath, int _portNum) throws UnknownHostException{
    this.ip = InetAddress.getLocalHost().getHostAddress();
    this.endOfProgram = false;
  }

  public Package register(String filename) throws IOException{
    Package res = new Package(null);
    int size = (int) new File(constData.filePath + filename).length();
    res.setP("op", "registerTorrent");
    res.setP("ip", this.ip);
    res.setP("port", "" + constData.peerPort);
    res.setT(new Torrent(filename, size));
    orignal = res;
    return res;
  }

  void createTorrentFile (Package P) throws IOException{
    Torrent tempT = P.getT();
    FileOutputStream out = new FileOutputStream(constData.torrentPath + tempT.hashData + ".torrent");
    out.write(Package.serialize(P));
    out.close();
  }
  
  public Torrent readTorrentFle (String fileName) throws IOException{
    Path path = Paths.get(constData.torrentPath + fileName);
    byte[] data = Files.readAllBytes(path);
    Package tempP = Package.deserialize(data);
    return tempP.getT();
  }

  public Package startProgram() throws IOException{
    Map<String,String> newMap = new HashMap<String, String>();
    Package res = new Package(null);
    res.setP("op", "start");
    res.setP("ip", this.ip);
    res.setP("port", constData.peerPort + "");
    File folder = new File(constData.torrentPath);
    File[] listOfFiles = folder.listFiles();
    ArrayList<Torrent> listT = new ArrayList<Torrent>();
    System.out.println(listOfFiles.length);
      for (int i = 0; i < listOfFiles.length; i++) {
        if (listOfFiles[i].isFile()) {
          String fileName = listOfFiles[i].getName();
          
          System.out.println(fileName);
          String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("torrent")){
              Path path = Paths.get(constData.torrentPath + fileName);
              
              byte[] data = Files.readAllBytes(path);
              Package temp = Package.deserialize(data);
              listT.add(temp.getT());
              newMap.put(temp.getT().fileName, constData.filePath + temp.getT().fileName);
            }
        } 
      }
      for (Torrent T : listT){
        System.out.println(T.fileName);
        System.out.println(T.hashData);
        System.out.println(T.size);
      }
      localServer = new JvodClientFileServer(constData.peerPort,newMap);
      localServer.start();
    res.setTList(listT);
    return res;
  }

  public Package endProgram(){
    Package res = new Package(null);
    res.setP("op", "shutDown");
    res.setP("ip", this.ip);
    res.setP("port", constData.peerPort + "");
    return res;
  }
  
  public boolean checkEnd(){
      return endOfProgram;
  }
  
  private void setEnd(){
    endOfProgram = true;
  }

  public Package getPeer(Torrent T){
    Package res = new Package(null);
    res.setP("op", "getPeer");
    res.setP("ip", this.ip);
    res.setP("port", constData.peerPort + "");
    res.setT(T);
    return res;
  }

  public void handle(Package res) throws Exception{
    String opT = res.getP("op");
    String res_message = res.getP("res_message");
    System.out.println(res_message);
    if (!res_message.equals("success")){
      System.out.println("Fail Message!!!" + opT);
      return;
    } else {
      System.out.println("success Message!!!" + opT);
    }
    if (opT.equals("registerTorrent")){
      createTorrentFile(this.orignal);
    } else if (opT.equals("start")){
      


    } else if (opT.equals("shutDown")){
        setEnd();
    } else if (opT.equals("getPeer")){
      System.out.println(" get in Peer ");
      ArrayList<User> userList = new ArrayList<User>();
      userList = res.getUList();
      for (User U : userList){
        System.out.println(U.ip + "  " + U.port);
        
      }
      Torrent tempT = res.getT();
      JvodClientFileDownloader tempDownloader = new JvodClientFileDownloader();
      if (tempDownloader.download(tempT, userList, constData.filePath + tempT.fileName))
      {
        // pop out a window 
      }
      else {
        //pop out error message
      }
      
    }
  }
}
