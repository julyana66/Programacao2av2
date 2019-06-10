


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

	private ServerSocket serverSocket;
	private Socket socket;
	private Map<String, ObjectOutputStream> streamMap = new HashMap<String, ObjectOutputStream>();

	public Servidor () {
		try {
			serverSocket = new ServerSocket (5070);
			System.out.println("Servidor on!");

			//quando alguem se conecta na porta 5555 o acept cria um objeto socket
			while (true) {
				socket = serverSocket.accept();

				new Thread (new ListenerSocket (socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ListenerSocket implements Runnable {
		private ObjectOutputStream outputStream;
		private ObjectInputStream inputStream;


		public ListenerSocket (Socket socket) throws IOException {
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream = new ObjectInputStream (socket.getInputStream());
		}

		@Override
		public void run() {

			MensagemArquivo mensagem = null;

			try {
				while ((mensagem = (MensagemArquivo) inputStream.readObject()) != null) {
					streamMap.put (mensagem.getCliente(), outputStream);

					if (mensagem.getFile() !=null) {
						for (Map.Entry<String, ObjectOutputStream> kv: streamMap.entrySet()) {
							if (!mensagem.getCliente().equals(kv.getKey())) {
								kv.getValue().writeObject(mensagem);
							}
						}

					}
				}

			} catch (IOException e) {
				streamMap.remove(mensagem.getCliente());	
				System.out.println(mensagem.getCliente() + " :desconectou");
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}



		}
	}
	
	public static void main(String[] args) {
		new Servidor ();
	}
}




