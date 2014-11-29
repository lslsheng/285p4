package JvodInfrastructure.Handlers;

import JvodInfrastructure.Datas.Package;

public class ResponseHandler {
	public void handle(Package p) throws Exception{
//		Worker w = new Worker(p);
//		Thread t = new Thread(w);
//		t.start();
		System.out.println("get package");
		System.out.println(p.getP("haha"));
	}

//	class Worker implements Runnable{
//		Package p;
//		Worker(Package p){
//			this.p = p;
//		}
//
//		@Override
//		public void run() {
//			System.out.println("get package");
//			System.out.println(p.getP("haha"));
//		}
//	}
}
