import java.io.File;
import java.io.Serializable;

public class MensagemArquivo implements Serializable {
	
	private String cliente;
	private File file;
	
	
	public MensagemArquivo(String cliente, File file) {
		super();
		this.cliente = cliente;
		this.file = file;
	}
	
	public MensagemArquivo(File file) {
		super();
		this.file = file;
	}

	public MensagemArquivo() {
	
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	
	
	
}
