package br.com.hotelurbano.desafio.conexao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

import br.com.hotelurbano.desafio.excecao.ErroConexaoException;

/**
 * Created by gustavoamg on 14/02/17.
 */

public class ConexaoTestaBackend extends Conexao {

    private String urlPrefix = "http://127.0.0.1:9200";

    public ConexaoTestaBackend(ConexaoListener listener) {
        super(listener);
    }

    public ConexaoTestaBackend(ConexaoListener listener, String urlPrefix) {
        super(listener);
        this.urlPrefix = urlPrefix;
    }

    @Override
    protected String getUrl() {
        return this.urlPrefix;
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getParametros() throws JSONException, IOException {
        return null;
    }

    @Override
    protected Object trataResposta(String json) throws JSONException, ErroConexaoException, ParseException {

        JSONObject result = new JSONObject(json);

        if(result.has("name")){
            return result.getString("name");
        }
        return null;
    }
}
