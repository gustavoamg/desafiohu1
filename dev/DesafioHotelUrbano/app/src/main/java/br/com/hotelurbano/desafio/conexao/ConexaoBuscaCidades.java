package br.com.hotelurbano.desafio.conexao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.hotelurbano.desafio.domain.City;


public class ConexaoBuscaCidades extends Conexao {
    private int pagina;
    private boolean temProximaPagina;

    public ConexaoBuscaCidades(ConexaoListener listener) {
        super(listener);
    }

    @Override
    protected String getUrl() {
        return "hotel-urbano/hotel/_search";
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getParametros() throws JSONException,
            IOException {
        return "{\n" +
                "  \"size\": 0, \n" +
                "  \"aggs\": {\n" +
                "    \"group_by_city\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"cidade.keyword\"\n" +
                "        , \"size\": 500\n" +
                "        , \"order\": {\n" +
                "          \"_term\": \"asc\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    @Override
    protected Object trataResposta(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        JSONObject aggregations = jsonObject.getJSONObject("aggregations").getJSONObject("group_by_city");

        JSONArray jsonCidades = aggregations.getJSONArray("buckets");

        List<City> cities = new ArrayList<>();

        for (int i = 0; i < jsonCidades.length(); i++) {
            JSONObject jsonPerfil = jsonCidades.getJSONObject(i);

            City city = new City();
            city.setName(jsonPerfil.getString("key"));
            cities.add(city);
        }

        return cities;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public boolean temProximaPagina() {
        return temProximaPagina;
    }


}
