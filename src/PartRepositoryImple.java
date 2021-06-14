import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PartRepositoryImple  extends UnicastRemoteObject implements PartRepository{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Part> repository;
	
	public PartRepositoryImple() throws RemoteException{
		this.repository = new ArrayList<Part>();
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

}
