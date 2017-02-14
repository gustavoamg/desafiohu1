package br.com.hotelurbano.desafio.excecao;

public class ErroConexaoCanceladaException extends Exception {

	private static final long serialVersionUID = -5715518481602128400L;

	public String getMessage() {
		return "Conexão cancelada.";
	}

}
