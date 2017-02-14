package br.com.hotelurbano.desafio.conexao;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.hotelurbano.desafio.domain.Availability;
import br.com.hotelurbano.desafio.domain.City;
import br.com.hotelurbano.desafio.domain.Hotel;
import br.com.hotelurbano.desafio.excecao.ErroConexaoException;

/**
 * Created by gustavoamg on 13/02/17.
 */

public class ConexaoBuscaDisponibilidade extends Conexao {



    private String city;
    private Date from;
    private Date to;
    private SimpleDateFormat df;

    private int page = 0;
    private int size = 25;
    private int total = 0;

    public ConexaoBuscaDisponibilidade(ConexaoListener listener, String city, Date from, Date to) {
        super(listener);
        this.city = city;
        this.from = from;
        this.to = to;
    }

    public ConexaoBuscaDisponibilidade(ConexaoListener listener, String city, Date from, Date to, int page) {
        super(listener);
        this.city = city;
        this.from = from;
        this.to = to;
        this.page = page;
    }

    @Override
    protected String getUrl() {
        return "hotel-urbano/disponibilidade/_search";
    }

    @Override
    protected String getMethod() {
        return "GET";
    }

    @Override
    protected String getParametros() throws JSONException, IOException {

        df = new SimpleDateFormat("dd/MM/yyyy");

        String params =  "{\n" +
                " \"size\" : " + size + ",\n" +
                " \"from\": " + (page * size) + ", " +
                " \"query\": {\n" +
                "   \"bool\": { \n" +
                "     \"must\": [\n" +
                "       {\n" +
                "         \"match\" : {\"disponibilidadde\": \"1\"}" +
                "       }";
        if(city != null && !"".equals(city))
            params += ",\n" +
                "       {\n" +
                "         \"nested\": {\n" +
                "           \"path\": \"hotel\",\n" +
                "           \"query\": {\n" +
                "            \"bool\" : {\n" +
                "              \"must\" : [\n" +
                "                {\"match_phrase\": {\"hotel.cidade\" : \"" + city + "\"}}\n" +
                "              ]\n" +
                "            }\n" +
                "           }\n" +
                "         }\n" +
                "       }";
        if(from != null ||  to != null) {
            params += ",\n" +
                    "       {\n" +
                    "        \"range\" : {\n" +
                    "            \"data\" : {\n";
            if (from != null)
                params += "                \"gte\": \"" + df.format(from) + "\",\n";
            if (to != null)
                params += "                \"lte\": \"" + df.format(to) + "\",\n" +
                        "                \"format\": \"dd/MM/yyyy||dd/MM/yyyy\"\n" +
                        "            }\n" +
                        "        }\n" +
                        "       }\n";
        }
        params += "      ]\n" +
                "    }\n" +
                " }\n" +
                "}";

        return params;
    }

    @Override
    protected Object trataResposta(String json) throws JSONException, ErroConexaoException, ParseException {

        JSONObject jsonObject = new JSONObject(json);

        JSONObject searchResults = jsonObject.getJSONObject("hits");

        this.total = searchResults.getInt("total");

        JSONArray resultsArray = searchResults.getJSONArray("hits");

        List<Availability> availabilityList = new ArrayList<>();

        for(int i = 0; i < resultsArray.length(); i++){

            JSONObject item = resultsArray.getJSONObject(i).getJSONObject("_source");

            JSONObject hotel = item.getJSONObject("hotel");

            Hotel h = new Hotel();
            h.setName(hotel.getString("nome"));

            City city = new City();
            city.setName(hotel.getString("cidade"));

            h.setCity(city);

            Date date = df.parse(item.getString("data"));

            Availability av = new Availability();
            av.setHotel(h);
            av.setDate(date);
            av.setAvailable(item.getInt("disponibilidadde"));
            av.setMinimumNights(item.getInt("minimo_de_noites"));

            availabilityList.add(av);
        }
        return availabilityList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
