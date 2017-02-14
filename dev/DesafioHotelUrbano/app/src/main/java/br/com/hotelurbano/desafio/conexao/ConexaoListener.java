package br.com.hotelurbano.desafio.conexao;

public interface ConexaoListener {
	public void conexaoRetornouComSucesso(Conexao conexao);
	public void conexaoRetornouComErro(Conexao conexao);
}
