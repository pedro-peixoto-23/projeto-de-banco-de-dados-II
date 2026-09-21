package ifpb.excecoes;

public class IntervaloHorarioInvalidoException extends RuntimeException {
	public IntervaloHorarioInvalidoException() {
		super("O intervalo de horário fornecido é inválido!");
	}
}
