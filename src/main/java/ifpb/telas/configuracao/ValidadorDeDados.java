package ifpb.telas.configuracao;


import ifpb.excecoes.ValorQtdTicketsInvalidoException;
import ifpb.excecoes.ValorTicketInvalidoException;
import org.apache.commons.validator.routines.EmailValidator;

import java.math.BigDecimal;

public class ValidadorDeDados {
    public static boolean isEmailValido(String email) {
        EmailValidator validadorEmail = EmailValidator.getInstance();

        if (validadorEmail.isValid(email)) {
        	return true;
        }

        return false;
    }
    
    public static boolean isAnoValido(String ano) {
    	if (ano.length() != 4) return false;
    	
    	try {
    		Integer.parseInt(ano);
    	} catch (NumberFormatException exception) {
    		return false;
    	}
    	
    	return true;
    }

    public static void isValorTicketValido(String valor) throws ValorTicketInvalidoException {
        BigDecimal valorFinal;

        try {
            valorFinal = new BigDecimal(valor);
        } catch (NumberFormatException exception) {
            throw new ValorTicketInvalidoException();
        }

        if (valorFinal.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValorTicketInvalidoException();
        }
    }

    public static void isValorQtdTicketsValido(String valor) throws ValorQtdTicketsInvalidoException {
        int valorFinal;
        try {
            valorFinal = Integer.parseInt(valor);
        } catch (NumberFormatException exception) {
            throw new ValorQtdTicketsInvalidoException();
        }

        if (valorFinal <= 0) {
            throw new ValorQtdTicketsInvalidoException();
        }
    }
}
