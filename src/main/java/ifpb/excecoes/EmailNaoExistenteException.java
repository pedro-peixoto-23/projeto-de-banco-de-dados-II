package ifpb.excecoes;

import lombok.Setter;

@Setter
public class EmailNaoExistenteException extends Exception {
	private String emailNaoEncontrado;
	
	public EmailNaoExistenteException(String emailNaoEncontrado) {
		this.setEmailNaoEncontrado(emailNaoEncontrado);
	}

    public String getMessage() {
		return "O endereço de e-mail inserido não existe no sistema.";
	}
}
