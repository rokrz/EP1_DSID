import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class ComputerPartManagerClient {
	
	private static PartRepository currentRepository;
	private static ArrayList<Pair<Integer, Part>> currentComponentsList = new ArrayList<Pair<Integer, Part>>();
	private static Part currentPart = null;
	private static ArrayList<String> listedRepositories;
	private static Scanner s = new Scanner(System.in);
	private static final String INSERTCOMMAND = "\nAguardando próximo comando...\n";
		
	public static void main(String[] args) {
		try {
			currentRepository = (PartRepository) Naming.lookup("rmi://192.168.15.6:1099/"+args[0]);
			addRepoNameToList(args[0]);
		}catch(Exception e) {
			e.printStackTrace();
		}
		printHelp();
		while(s.hasNext()) {
			String nextCommand = s.nextLine();
			readInput(nextCommand);
			System.out.println(INSERTCOMMAND);
		}
	}
	
	public static void printHelp() {
		System.out.println("Olá! Seja bem vindo ao Gerenciador de Peças. Para utilizar o programa, use os seguintes comandos:\n"
				+ "\tbind REPONAME: para alterar o repositorio atual para o repositorio REPONAME.\n"
				+ "\tlistP: para mostrar as peças existentes no repositorio atual.\n"
				+ "\tgetP ID: para buscar no repositório a peça com código ID, onde ID é o cóodigo numérico da peça.\n"
				+ "\tshowP: para mostrar os atributos da peça atual selecionada.\n"
				+ "\tclearList: para limpar a lista de componentes atual.\n"
				+ "\taddSubPart QTD: para adicionar uma quantidade QTD da peça atual na lista de componentes atual, onde QTD é aquantidade, em números, dessa peça.\n"
				+ "\taddPart NAME DESC: para adicionar uma nova peça ao repositório. Essa peça terá nome NAME e uma descrição DESC, que é opcional. Além disso, receberá a lista atual de componentes como sua lista de componentes.Adicionar uma peça limpa a lista de componentes.\n"
				+ "\tlistRepos: para imprimir uma lista com os repositórios já acessados nessa seção.\n"
				+ "\thelp: para imprimir essa lista de comandos novamente.\n");
	}
	
	public static void readInput(String nextCommand) {
		if(nextCommand.toLowerCase().contains("help".toLowerCase())){
			printHelp();
		}else if(nextCommand.toLowerCase().contains("bind".toLowerCase())) {
			String repoName = nextCommand.split(" ")[0];
			bind(repoName);
		}else if(nextCommand.toLowerCase().contains("listP".toLowerCase())) {
			listP();
		}else if(nextCommand.toLowerCase().contains("getP".toLowerCase())) {
			int partId = Integer.parseInt(nextCommand.split(" ")[1]);
			getP(partId);
		}else if(nextCommand.toLowerCase().contains("showP".toLowerCase())) {
			showP();
		}else if(nextCommand.toLowerCase().contains("clearList".toLowerCase())) {
			clearList();
		}else if(nextCommand.toLowerCase().contains("addSubPart".toLowerCase())) {
			int componentQtd = Integer.parseInt(nextCommand.split(" ")[1]);
			addSubPart(componentQtd);
		}else if(nextCommand.toLowerCase().contains("addPart".toLowerCase())) {
			String name = null;
			String desc = null;
			String[] commandParameters = nextCommand.split(" ");
			if(commandParameters.length>3) {
				name = commandParameters[1];
				desc = "";
				for(int i = 2; i<commandParameters.length;i++) {
					desc+=commandParameters[i]+" ";
				}
			}else if(commandParameters.length==2) {
				name = commandParameters[1];
				desc = "";
			}
			addPart(name, desc);
		}else if(nextCommand.toLowerCase().contains("listRepos".toLowerCase())) {
			printKnownRepositories();
		}else if(nextCommand.toLowerCase().contains("quit".toLowerCase())) {
			quit();
		}
	}
	
	//Imprime uma lista com nos repoName dos repositorios ja acessados
	public static void printKnownRepositories() {
		System.out.println("Lista de repositórios conhecidos: ");
		for(String repo : listedRepositories) {
			System.out.println("\t"+repo);
		}
	}
	
	//Adiciona o repoName do repositorio  mais recente à lista de repositorios conhecidos
	public static void addRepoNameToList(String repoName) {
		if(listedRepositories!=null) {
			if(!listedRepositories.contains(repoName)) {
				listedRepositories.add(repoName);
			}
		}else {
			listedRepositories = new ArrayList<String>();
			listedRepositories.add(repoName);
		}
	}
	
	//Altera o currentRepository para um repositorio com o nome recebido
	public static void bind(String repoName) {
		System.out.println("Tentando se conectar ao repositorio "+ repoName);
		try {
			currentRepository = (PartRepository) Naming.lookup("rmi://192.168.15.6:1099/"+repoName);
			addRepoNameToList(repoName);
		}catch(NotBoundException nb) {
			System.out.println("O repositório que tentou acessar não existe...");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Imprime a lista de peças existentes armazenadas no currentRepository
	public static void listP() {
		System.out.println("Lista de partes cadastradas no repositório.\n"
				+ "ID - Nome da peça \n");
		try {
			for(Part p : currentRepository.getPartList()) {
				System.out.println(p.getId()+" - "+p.getPartName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Busca por uma parte no currentRepository com base no ID do objeto
	public static void getP(int id) {
		System.out.println("Buscando peça de Identificador: "+id);
		try {
			Part auxPart = currentRepository.findPartById(id);
			if(auxPart!=null) {
				currentPart = auxPart;
			}else {
				System.out.println("Não foi encontrada uma peça com o identificador desejado...");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//Imprime os atributos da currentPart
	public static void showP() {
		if(currentPart!=null) {
			try {
				System.out.println("Mostrando peça atual.\n"
						+ "ID - Nome - Descrição - Lista de componentes \n");
				System.out.println(currentPart.getId()+" - "+currentPart.getPartName()+" - "+currentPart.getPartDesc()+" "+currentPart.printComponentList());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("A peça atual não foi definida... Busque por uma peça para selecioná-la");
		}
	}
	
	//Limpa a atual lista de componentes 
	public static void clearList() {
		System.out.println("Limpando atual lista de componentes.");
		currentComponentsList = new ArrayList<Pair<Integer,Part>>();
	}
	
	//Adiciona X unidades da peça Y na lista de componentes, onde X é quantity, e Y é a currentPart
	public static void addSubPart(int quantity) {
		if(currentPart!=null) {
			try {
				System.out.println("Adicionando "+quantity+" quantidades da peça "+currentPart.getPartName());
				currentComponentsList.add(new Pair<Integer,Part>(quantity, currentPart));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			System.out.println("A peça atual não foi definida... Busque por uma peça para selecioná-la");
		}
	}
	
	public static void addPart(String name, String desc) {
		try {
			System.out.println("Adicionando uma nova peça de nome "+name+" e descrição "+desc);
			currentRepository.addPartToRepo(name, desc, currentComponentsList);
			clearList();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	//Retorna uma mensagem de encerramento para o usuário e encerra a execução do cliente. Não encerra os servidores
	public static void quit() {
		s.close();
		System.out.println("Encerrando aplicação, nos vemos em breve!");
		System.exit(0);
	}

}
