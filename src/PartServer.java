import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

public class PartServer{
	
	PartServer(){
		try {
			System.setProperty("java.rmi.server.hostname", "192.168.15.6");
			LocateRegistry.createRegistry(1099);
			PartRepository pr = new PartRepositoryImple();
			Naming.bind("PartServer", (Remote) pr);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new PartServer();
	}
}
