import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class ComputerPartManagerClient {
	
	private static PartRepository currentRepository;
	private static ArrayList<Pair<Integer, Part>> currentComponentsList = new ArrayList<Pair<Integer, Part>>();
	private static Part currentPart = null;
	private static ArrayList<String> listedRepositories;
	private static Scanner s = new Scanner(System.in);
	private static final String INSERTCOMMAND = "\nAguardando próximo comando...\n";
	private static Registry registry;
	
	//Inicia o client
	public static void main(String[] args) {
		listedRepositories = new ArrayList<String>();
		try {
        	registry = LocateRegistry.getRegistry(1099);
        	for(String repo : registry.list()) {
        		listedRepositories.add(repo);
        	}
        	currentRepository = (PartRepository) registry.lookup(listedRepositories.get(0));
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
	
	//Imprime a lista de comandos
	public static void printHelp() {
		System.out.println("Olá! Seja bem vindo ao Gerenciador de Peças. Para utilizar o programa, use os seguintes comandos:\n"
				+ "\tbind REPONAME: para alterar o repositório atual para o repositório REPONAME.\n"
				+ "\tlistP: para mostrar as peças existentes no repositorio atual.\n"
				+ "\tgetP ID: para buscar no repositório a peça com código ID, onde ID é o código numérico da peça.\n"
				+ "\tshowP: para mostrar os atributos da peça atual selecionada.\n"
				+ "\tshowList: para mostrar as peças presentes na lista de componentes.\n"
				+ "\tclearList: para limpar a lista de componentes atual.\n"
				+ "\taddSubPart QTD: para adicionar uma quantidade QTD da peça atual na lista de componentes atual, onde QTD é aquantidade, em números, dessa peça.\n"
				+ "\taddP: para adicionar uma nova peça ao repositório. Essa peça terá nome NAME e uma descrição DESC, que é opcional.\n"
				+ "\taddCopyP: para adicionar uma coópia da peça atual no repositório. O id dessa peça será recalculado para evitar conflito no repo\n"
				+ "\tlistRepos: para imprimir uma lista com os repositórios já acessados nessa seção.\n"
				+ "\thelp: para imprimir essa lista de comandos novamente.\n"
				+ "\tquit: encerra a aplicação cliente.\n");
	}
	
	//Trata a entrada do usuario e executa o comando desejado
	public static void readInput(String nextCommand) {
		if(nextCommand.toLowerCase().contains("help".toLowerCase())){
			printHelp();
		}else if(nextCommand.toLowerCase().contains("bind".toLowerCase())) {
			try {
				String repoName = nextCommand.split(" ")[1];
				bind(repoName);
			}catch(ArrayIndexOutOfBoundsException ae) {
				System.out.println("ERRO: Não foi recebido um nome de repositorio como parametro. Verifique e tente novamente...");
			}
		}else if(nextCommand.toLowerCase().contains("listP".toLowerCase())) {
			listP();
		}else if(nextCommand.toLowerCase().contains("getP".toLowerCase())) {
			try {
				int partId = Integer.parseInt(nextCommand.split(" ")[1]);
				getP(partId);
			}catch(NumberFormatException ne) {
				System.out.println("ERRO: Não foi recebido um numero como parametro. Verifique e tente novamente...");
			}catch(ArrayIndexOutOfBoundsException ae) {
				System.out.println("ERRO: Não foi recebido um parametro. Verifique e tente novamente...");
			}
		}else if(nextCommand.toLowerCase().contains("showP".toLowerCase())) {
			showP();
		}else if(nextCommand.toLowerCase().contains("showList".toLowerCase())){
			showCurrentComponentList();
		}else if(nextCommand.toLowerCase().contains("clearList".toLowerCase())) {
			clearList();
		}else if(nextCommand.toLowerCase().contains("addSubPart".toLowerCase())) {
			try {
				int componentQtd = Integer.parseInt(nextCommand.split(" ")[1]);
				addSubPart(componentQtd);
			}catch(NumberFormatException ne) {
				System.out.println("ERRO: Não foi recebido um numero como parametro. Verifique e tente novamente...");
			}catch(ArrayIndexOutOfBoundsException ae) {
				System.out.println("ERRO: Não foi recebido um parametro. Verifique e tente novamente...");
			}
		}else if(nextCommand.toLowerCase().contains("addCopyP".toLowerCase())) {
			addCopyPart();
		}else if(nextCommand.toLowerCase().contains("addP".toLowerCase())) {
			String name = null;
			String desc = null;
			System.out.println("Digite o NOME da peça:");
			name = s.nextLine();
			System.out.println("Digite a descrição do produto:");
			desc = s.nextLine();
			if(name!=null) {
				if(!name.equals("")) {
					addPart(name, desc);
				}else {
					System.out.println("ERRO: O Nome da peça não pode ser nulo. Verifique e tente novamente...");

				}
			}else {
				System.out.println("ERRO: O Nome da peça não pode ser nulo. Verifique e tente novamente...");
			}
		}else if(nextCommand.toLowerCase().contains("listRepos".toLowerCase())) {
			printKnownRepositories();
		}else if(nextCommand.toLowerCase().contains("quit".toLowerCase())) {
			quit();
		}else {
			System.out.println("Não há um comando correspondente. Tente novamente...");
		}
	}
	
	//Mostra as peçs na lista de componentes atual
	private static void showCurrentComponentList() {
		if(currentComponentsList!=null && currentComponentsList.size()>0) {
			System.out.println("Lista de componentes atuais:\n"
					+"Quantidade, Nome da peça\n");
			for(Pair<Integer,Part> p : currentComponentsList) {
				try {
					System.out.println(p.quantity+", "+p.item.getPartName());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}else {
			System.out.println("A lista de componentes está vazia...");
		}
	}

	//Adiciona uma copia da currentPart no currentRepository
	private static void addCopyPart() {
		try {
			if(currentPart!=null) {
				currentRepository.addCopyPartToRepo(currentPart);
				System.out.println("Adicionada uma cópia de "+currentPart.getId()+" - "+currentPart.getPartName()+" ao repositorio");
			}else {
				System.out.println("Não há uma peça selecionada. Não será inserida nenhuma cópia");
			}
		} catch (RemoteException e) {
			e.printStackTrace();
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
			int index =listedRepositories.indexOf(repoName);
			if(index!=-1) {
				currentRepository = (PartRepository) registry.lookup(listedRepositories.get(index));
				System.out.println("Conectado ao repositorio "+repoName);
			}else {
				System.out.println("O Repositorio que voce está tentando acessar não existe. Por favor verifique o nome do repositorio e tente novamente...");
			}
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
				System.out.println("Peça encontrada. ID: "+currentPart.getId()+", Name: "+currentPart.getPartName());
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
						+ "ID - Nome - Primaria? - Descrição - Lista de componentes \n");
				System.out.println(currentPart.getId()+" - "+currentPart.getPartName()+" - "+currentPart.isPrimary()+" - "+currentPart.getPartDesc()+" - "+currentPart.printComponentList());
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
		System.out.println("Encerrando aplicação, nos vemos em breve! <3 é isso <3");
		System.exit(0);
	}

}
