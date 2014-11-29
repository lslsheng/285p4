package JvodInfrastructure.Handlers;

import JvodInfrastructure.Datas.Package;


public class PackageHandler {
	public Package handle(Package p){
//		Worker w = new Worker(p);
//		Thread t = new Thread(w);
//		t.start();
		
		System.out.println("get package");
		System.out.println(p.getP("hello"));
	//	p.setP("", "");
		Package res = new Package(null);
		res.setP("haha", "nono");
		return res;
	}
}
