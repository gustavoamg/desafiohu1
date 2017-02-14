package br.com.hotelurbano.desafio.excecao;

import java.io.IOException;

public class ErroConexaoException extends IOException {

	private static final long serialVersionUID = -125225607291734339L;

	public ErroConexaoException(String mensagem) {
		super(mensagem);
	}
}
