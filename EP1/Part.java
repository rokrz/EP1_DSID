import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Part extends Remote{
	
	public int getId() throws RemoteException;
	public void setId(int id) throws RemoteException;
	public String getPartName() throws RemoteException;
	public void setPartName(String name) throws RemoteException;
	public String getPartDesc() throws RemoteException;
	public void setPartDesc(String description) throws RemoteException;
	public ArrayList<Pair<Integer,Part>> getComponentList() throws RemoteException;
	public void setComponentList(ArrayList<Pair<Integer,Part>> components) throws RemoteException;
	public String printComponentList() throws RemoteException;
	public boolean isPrimary()throws RemoteException;

}
