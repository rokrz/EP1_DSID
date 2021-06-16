import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

//Trabalha nas operações sobre a listaLigada de Partes do servidor
public interface PartRepository extends Remote{
	
	public Part findPartById(int id) throws RemoteException; //Busca uma parte com o ID correspondente na lista de partes
	public boolean addPartToRepo(String name, String desc, ArrayList<Pair<Integer,Part>> components) throws RemoteException; //Adiciona uma nova parte à lista. Recebe como parametro o nome e a descrição, e dento do metodo cria a nova parte, com um ID procedural e a Lista atual de subpartes
	public Part removeFromRepo(int id) throws RemoteException; //Busca e remove a peça da lista ligada. Retorna uma copia da peça
	public ArrayList<Part> getPartList() throws RemoteException; //Retorna a lista de peças do repo
	public boolean addCopyPartToRepo(Part p) throws RemoteException; //Adiciona uma copia de um objeto no repo
}
