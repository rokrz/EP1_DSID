import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PartRepositoryImple extends UnicastRemoteObject implements PartRepository{
	private static Registry r;
	private static final long serialVersionUID = 1L;
	private ArrayList<Part> repository;
	public String repositoryName;
		
	public static void main(String[] args) {
		try {
			r = LocateRegistry.createRegistry(1099);
			PartRepositoryImple[] servers = new PartRepositoryImple[args.length];
			for(int j =0; j<servers.length;j++) {
				servers[j] = new PartRepositoryImple(args[j]);
				System.out.println("New Repository Created: "+args[j]);
			}
			for(int i = 0;i<args.length;i++) {
				r.bind(args[i], servers[i]);
			}
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
	
	public PartRepositoryImple(String repoName) throws RemoteException{
		this.repository = new ArrayList<Part>();
		this.repositoryName = repoName;
	}
	
	@Override
	public Part findPartById(int id) throws RemoteException {
		repository.sort(null);
		for(Part p : repository){
			if(p.getId() == id) {
				Part auxPart = new PartImple(p);
				return auxPart;
			}
		}
		return null;
	}

	@Override
	public boolean addPartToRepo(String name, String desc, ArrayList<Pair<Integer,Part>> components) throws RemoteException {
		int newId = repository.size()+1;
		Part p = new PartImple(name, desc, components, newId);
		return repository.add(p);
	}

	@Override
	public Part removeFromRepo(int id) throws RemoteException {
		Part p = findPartById(id);
		if(p!=null) {
			if(repository.remove(p)) {
				return p;
			}else {
				return null;
			}
		}	
		else {
			return null;
		}
	}

	@Override
	public ArrayList<Part> getPartList() throws RemoteException {
		return repository;
	}

	@Override
	public boolean addCopyPartToRepo(Part p) throws RemoteException {
		p.setId(repository.size()+1);
		return repository.add(p);
	}

}
