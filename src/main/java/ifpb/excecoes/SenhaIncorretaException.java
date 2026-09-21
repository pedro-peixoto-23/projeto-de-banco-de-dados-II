package ifpb.excecoes;
public class SenhaIncorretaException extends Exception {
	public String getMessage() {
		return "A senha inserida está incorreta";
	}
}
