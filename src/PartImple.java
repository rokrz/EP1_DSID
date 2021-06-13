import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PartImple extends UnicastRemoteObject implements Part, Comparable<Part>{

	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private int id;
	private ArrayList<Pair<Integer, Integer>> components;
	
	public PartImple() throws RemoteException{}
	
	public PartImple(Part p) throws RemoteException{
		this.name = p.getPartName();
		this.id = p.getId();
		this.description = p.getPartDesc();
		this.components = p.getComponentList();
	}
	
	public PartImple(String name, int id) throws RemoteException{
		this.name = name;
		this.id = id;
	}
	
	public PartImple(String name, String description, int id) throws RemoteException{
		this.name = name;
		this.description = description;
		this.id = id;
	}
	
	public PartImple(String name, String desc, ArrayList<Pair<Integer,Integer>> components, int id) throws RemoteException{
		this.name = name;
		this.description = desc;
		this.components = components;
		this.id = id;
	}
	
	@Override
	public int getId() throws RemoteException{
		return id;
	}

	@Override
	public String getPartName() throws RemoteException{
		return name;
	}

	@Override
	public String getPartDesc() throws RemoteException{
		return description;
	}

	@Override
	public ArrayList<Pair<Integer, Integer>> getComponentList() throws RemoteException{
		return components;
	}

	@Override
	public void setId(int id) throws RemoteException{
		this.id = id;
	}

	@Override
	public void setPartName(String name) throws RemoteException{
		this.name = name;
	}

	@Override
	public void setPartDesc(String description) throws RemoteException{
		this.description = description;
	}

	@Override
	public void setComponentList(ArrayList<Pair<Integer,Integer>> components) throws RemoteException{
		this.components = components;
	}
	
	@Override
	public int compareTo(Part o){
		if(o!=null) {
			try {
				return Integer.compare(id, o.getId());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}else {
			return -1;
		}
		return -1;
	}

}
