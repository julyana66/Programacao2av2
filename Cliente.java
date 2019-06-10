
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Cliente {



	private Socket socket;
	private ObjectOutputStream outputStream;

	public Cliente () throws UnknownHostException, IOException {
		this.socket = new Socket ("localhost", 5070);
		this.outputStream = new ObjectOutputStream (socket.getOutputStream());

		new Thread (new ListenerSocket(socket)).start();

		// enviar mensagens
		menu();
	}

	private void menu () throws IOException {
		Scanner sc = new Scanner (System.in); 
		System.out.println("Digite seu nome");

		String nome = sc.nextLine();

		this.outputStream.writeObject(new MensagemArquivo(nome, null));

		int option =0;

		while (option != -1) {
			System.out.println("1 - sair | 2- enviar: ");
			option = sc.nextInt();

			if (option ==2) {
				Send(nome);
			} else if( option == 1){
				System.exit(0);
			}
		}
	}

	private void Send(String nome) throws IOException {


		JFileChooser fileChooser = new JFileChooser();

		int opt = fileChooser.showOpenDialog(null);

		if (opt == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();

			this.outputStream.writeObject(new MensagemArquivo (nome, file));
		}
	}

	// receber as mensagens enviadas pelos outros clientes
	private class ListenerSocket implements Runnable {

		private ObjectInputStream inputStream;

		public ListenerSocket (Socket socket) throws IOException {
			this.inputStream = new ObjectInputStream(socket.getInputStream());


		}

		@Override
		public void run() {

			MensagemArquivo mensagem = null;

			try {
				while ((mensagem = (MensagemArquivo) inputStream.readObject()) != null) {
					System.out.println("você recebeu um arquivo de: " + mensagem.getCliente());
					System.out.println("O arquivo é: " + mensagem.getFile().getName());

					//imprime (mensagem);
					
				
					System.out.println("1 - sair | 2- enviar: ");

				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}

		
		public void salvar (MensagemArquivo mensagem) {
			try {
				
				long time = System.currentTimeMillis();
				
				
				FileInputStream fileImputStream = new FileInputStream(mensagem.getFile());
				FileOutputStream fileOutputStream = new FileOutputStream("/home/jubello/Documentos/z/" + time + "_" + mensagem.getFile().getName());

				FileChannel fin = fileImputStream.getChannel();
				FileChannel fout = fileOutputStream.getChannel();

				try {
					long size = fin.size();
					
					
					fin.transferTo(0, size, fout);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}

		
		
		public void imprime(MensagemArquivo mensagem) {
			try {
				FileReader fileReader = new FileReader (mensagem.getFile());

				BufferedReader bufferedReader = new BufferedReader(fileReader);
				String linha;
				try {
					while ((linha = bufferedReader.readLine()) != null) {
						System.out.println(linha);

					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}

	}

	public static void main (String [] args) {

		try {
			new Cliente ();



		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
