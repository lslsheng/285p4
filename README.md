## 285p4
This project is about a peer-to-peer file sharing software. This application can be viewed as a distributed file system with a master which has all the information for a certain file transfer.
### The Messager
We utilise a generalised messager, called package, to communicate between different services. The structure of package comprises “properities” which is a map can store key value pairs, “data” which is an array of bytes that can be used in data transfer, “torrentT” which is a torrent file, “Tlist” which can be a list of torrent files. For a certain message under a given circumstance, one or more or fields inside a package structure may be used during communications.
### The GUI
The GUI is developed under the standard built-in Windows Builder that has various elements and corresponding listener to evoke certain event any the activities of users.
### The Server
The master is developed by mocking the standard peer-to-peer protocol that is widely used by marketed p2p sharing software. The procedure can be naturally divided in the 3 parties, a server, a tracker, and a client. When one of the clients want to share a certain file, it makes a torrent that has relevant file information and then send it to server. The server then register the torrent with tracker which do all the bookkeeping of the files to share. If another client want to download a certain file “on” the server, it request information of all the available peers that have the requested file where server get that piece of information from tracker. After the client finishing download the file, it then register itself with the server stating that it become available to transfer the downloaded file to other clients may want to have that file.
### File Transfer
The process of transferring can be naturally divided into 2 parts to elaborate, one is the party (C) initiating a download request and the other is the party (S) with the requested file on hand. For the former party C, there are 3 layers of services that works together to accomplish this task. From top to bottom, the first layer consists several “package clients” that doing the talking to the counterparts in S that do the actual communication via network. The “package clients” belongs to a same second layer, called “JvodClientFileDownloader”, which do the initialisation of a download request and keep pull requests from the third layer, called “FileWriter”, until the “FileWriter” no long need any more data to store. The “FileWriter” has a bookkeeping thread that only awake when it receives data to complete a file transfer and keep tracking the overall process of  the received partition of the file. When “FileWriter” sees the file transfer is complete, it flush the completed file to disk and thus finish receiving a file. For the later party S, it has a somewhat mirroring layers of structure which also has 3 layers. From top to bottom, the first layer, called “PackageServer” which talks to “PackageClient”. “JvodFileServer” may have multiple “PackageServer”. “JvodFileServer” passes the information from “PackageServer” to “FileServer”. The “FileServer” gets the requested file and send a response back to “JvodFileServer” and then arrives at “PackageServer”. Then, transfer the requested file over the network.
