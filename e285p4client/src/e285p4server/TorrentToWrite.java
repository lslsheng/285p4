//package eecs285;

import java.util.ArrayList;

class writerThread implements Runnable {
  private Thread t;
  private Peer peer;
  private ResponseHandler handler;
  private FileWriter file;
  
  writerThread(Peer inpeer, ResponseHandler inhandler, FileWriter infile){
    peer = inpeer;
    handler = inhandler;
    file = infile;
  }
  public void run() {
    PackageClient pc = new PackageClient(peer.ip, peer.port, handler);
    while (true)
    {
      Package req = file.getRequest();
      if (req == null)
        break;
      pc.sendPackage(req);
    }
  }
  
  public void start ()
  {
     if (t == null)
     {
        t = new Thread (this, peer, handler, file);
        t.start ();
     }
  }

}

public class TorrentToWrite
{
  
  public TorrentToWrite(String filename, int size, String path, ArrayList<Peer> peers)
  {
    FileWriter file = new FileWriter(filename, size, path);
    ResponseHandler handler = new ResponseHandler();
    for (int i = 0; i < peers.size(); i++)
    {
      writerThread t = new writerThread(peers[i], handler, file);
      t.start();
    }
    
  }
}

public class serverHandler
{
  public serverHandler(Package req)
  {
    server.newFile(req.fileName, req.length, "not used", "path");
    return server.handle(req); 
  }
}
