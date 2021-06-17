import java.io.Serializable;

public class Pair<T1, T2> implements Serializable{
	private static final long serialVersionUID = 1L;
	public T1 quantity;
	public T2 item;
	
	public Pair (T1 q, T2 p){
		this.quantity = q;
		this.item = p;
	}
	
}
