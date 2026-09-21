package ifpb.excecoes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailJaCadastradoException extends Exception {
	private String emailJaCadastrado;
	
	public EmailJaCadastradoException(String emailJaCadastrado) {
		this.setEmailJaCadastrado(emailJaCadastrado);
	}

    public String getMessage() {
		return "O endereço de e-mail inserido já existe no sistema.";
	}
}
