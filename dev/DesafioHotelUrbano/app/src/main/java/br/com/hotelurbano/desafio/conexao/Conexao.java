package br.com.hotelurbano.desafio.conexao;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.AbstractMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import br.com.hotelurbano.desafio.DesafioHotelUrbanoApplication;
import br.com.hotelurbano.desafio.excecao.ErroConexaoCanceladaException;
import br.com.hotelurbano.desafio.excecao.ErroConexaoException;
import br.com.hotelurbano.desafio.util.IOUtils;

public abstract class Conexao extends AsyncTask<Void, Void, Integer> {

    private ConexaoListener listener;
    private int indice;
    private Object resultado;
    protected Exception erro;
    private boolean rodando;

    public Conexao(ConexaoListener listener) {
        this.listener = listener;
    }

    public void iniciar() {
        rodando = true;
        this.execute();
    }

    public void cancelar() {
        this.cancel(true);
        rodando = false;
    }

    protected abstract String getUrl();

    protected abstract String getMethod();

    protected abstract String getParametros() throws JSONException, IOException;

    protected abstract Object trataResposta(String json) throws JSONException,
            ErroConexaoException, ParseException;

    protected String getUrlCompleta() {
        return (getBaseUrl()!=null)? getBaseUrl() + getUrl():getUrl();
    }

    protected String getBaseUrl() {
        return DesafioHotelUrbanoApplication.getUrl();
    }

    protected String getEncoding() {
        return "UTF-8";
    }


    protected Map.Entry<String, String>[] addHeaders() {
        Map.Entry<String, String>[] headers = new Map.Entry[2];

        Map.Entry<String, String> header1 = new AbstractMap.SimpleEntry("Accept", "application/json");
        headers[0] = header1;

        Map.Entry<String, String> header2 = new AbstractMap.SimpleEntry("Content-type", "application/json");
        headers[1] = header2;

        return headers;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            resultado = enviarHttp();

            return 0;
        } catch (ErroConexaoException e) {
            e.printStackTrace();
            erro = e;
            return -1;
        } catch (JSONException e) {
            e.printStackTrace();
            erro = new Exception("Erro ao ler documento", e);
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            erro = new Exception("Erro de conexão inesperado", e);
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null)
                erro = e;
            else {
                erro = new Exception("Erro inesperado", e);
            }
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (listener != null) {
            if (result == 0) {
                Conexao.this.listener
                        .conexaoRetornouComSucesso(Conexao.this);
            } else {
                Conexao.this.listener
                        .conexaoRetornouComErro(Conexao.this);
            }
        }
        rodando = false;
    }

    @Override
    protected void onCancelled() {
    }

    private Object enviarHttp() throws Exception {
        HttpURLConnection urlConnection = null;
        Integer statusCode;

        String resposta;

        try {
            URL url = new URL(getUrlCompleta());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(getMethod());

            Map.Entry[] headers = addHeaders();
            for (int i = 0; i < headers.length; i++) {
                Map.Entry<String, String> header = headers[i];
                if (header.getKey() != null) {
                    urlConnection.setRequestProperty(header.getKey(),
                            header.getValue());
                }
            }
            interromperConexaoSeCancelada();

            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(120000);
            urlConnection.setReadTimeout(60000);

            interromperConexaoSeCancelada();

            String parametros = getParametros();
            if (parametros != null) {
                urlConnection.setDoOutput(true);
            } else {
                urlConnection.setDoOutput(false);
            }

            interromperConexaoSeCancelada();

            if (parametros != null) {
                OutputStream os = urlConnection.getOutputStream();
                os.write(parametros.getBytes());
                os.flush();
                os.close();
            }
            interromperConexaoSeCancelada();

            try {
                statusCode = urlConnection.getResponseCode();

            } catch (IOException e) {
                // Algumas versões do HttpURLConnection lançam exceção quando recebem 401 sem challenge.
                // 	Ao chamar novamente, não acontece o erro.
                statusCode = urlConnection.getResponseCode();
                if (statusCode != 401) {
                    throw e;
                }
            }

            InputStream instream = null;
            if (statusCode < HttpsURLConnection.HTTP_BAD_REQUEST) {
                try {
                    instream = urlConnection.getInputStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    instream = urlConnection.getErrorStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            interromperConexaoSeCancelada();

            resposta = null;

            if (instream != null) {
                resposta = IOUtils.converteStreamEmString(instream, getEncoding());
                instream.close();
            }
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

//        * 200: Pedido aceito e retornando corpo na resposta.
//                * 201: Pedido aceito e a informação enviada foi criada.
//        * 401: Falha na autenticação. Em caso de email ou senha errados, devolveremos
//        os erros no corpo da resposta.
//        * 403: Permissão negada. É necessário ser Premium para concluir esse pedido.
//        * 404: Endpoint não encontrado.

        if (statusCode == HttpURLConnection.HTTP_OK || statusCode == HttpURLConnection.HTTP_CREATED) {
            try {
                return trataResposta(resposta);
            } catch (Exception e) {
                logResponse(statusCode, resposta);
                throw e;
            }
        }  else if (statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
            String erro = trataMensagemErro(resposta);
            logResponse(statusCode, resposta);
            throw new Exception(erro);
        } else {
            logResponse(statusCode, resposta);
            throw new Exception();
        }
    }

    private void interromperConexaoSeCancelada()
            throws ErroConexaoCanceladaException {
        if (this.isCancelled())
            throw new ErroConexaoCanceladaException();
    }

    private void logResponse(Integer status, String responseBody) {
        String body = "NO-BODY";

        if (responseBody != null) {
            body = responseBody;
        }
    }

    public boolean isRodando() {
        return rodando;
    }

    public void setListener(ConexaoListener listener) {
        this.listener = listener;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public Object getResultado() {
        return resultado;
    }

    public Exception getErro() {
        return erro;
    }

    private String trataMensagemErro(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        String erro = jsonObject.getString("errors");

        if (erro != null) {
            return erro;
        }
        return "Erro inesperado";
    }
}
