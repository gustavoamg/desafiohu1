package br.com.hotelurbano.desafio.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.hotelurbano.desafio.DesafioHotelUrbanoApplication;
import br.com.hotelurbano.desafio.conexao.Conexao;
import br.com.hotelurbano.desafio.conexao.ConexaoListener;
import br.com.hotelurbano.desafio.conexao.ConexaoTestaBackend;
import br.com.hotelurbano.desafio.desafiohotelurbano.R;

/**
 * Created by gustavoamg on 10/02/17.
 */

public class ConfigureSearchFragment extends Fragment implements ConexaoListener{

    private EditText inputSearchAddressField;
    private Button saveButton;

    private OnConfigureSearchListener onConfigureSearchListener;
    private StringBuilder searchUrl;

    @Override
    public void conexaoRetornouComSucesso(Conexao conexao) {

        if(conexao.getResultado() != null){
            String serverName = (String) conexao.getResultado();

            if(serverName != null && !"".equals(serverName)){
                DesafioHotelUrbanoApplication.saveUrl(searchUrl.toString());

                if(onConfigureSearchListener != null) {
                    onConfigureSearchListener.onConfigureSearch();
                }
            }
            else {
                Toast.makeText(getContext(), "Nome de servidor inválido.", Toast.LENGTH_LONG).show();
            }
            if(saveButton != null)
                saveButton.setEnabled(false);
        }

    }

    @Override
    public void conexaoRetornouComErro(Conexao conexao) {
        Toast.makeText(getContext(), "Erro ao recuperar dados do endereço fornecido", Toast.LENGTH_SHORT).show();
        saveButton.setEnabled(true);
    }

    public interface OnConfigureSearchListener {
        void onConfigureSearch();

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment ConfigureSearchFragment.
     */
    public static ConfigureSearchFragment newInstance() {
        ConfigureSearchFragment fragment = new ConfigureSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View configureSearchLayout = inflater.inflate(R.layout.configure_search_fragment, container, false);

        inputSearchAddressField = (EditText) configureSearchLayout.findViewById(R.id.input_backend_ip);
        saveButton = (Button) configureSearchLayout.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String serverAddress = inputSearchAddressField.getText().toString();

                searchUrl = new StringBuilder("http://").append(serverAddress).append(":9200").append("/");

                validadeAddress(searchUrl.toString());
                saveButton.setEnabled(false);

                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(saveButton.getWindowToken(), 0);


            }
        });
        return configureSearchLayout;
    }

    public void validadeAddress(String url){
        ConexaoTestaBackend conexaoTestaBackend = new ConexaoTestaBackend(this, url);
        conexaoTestaBackend.iniciar();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConfigureSearchListener) {
            onConfigureSearchListener = (OnConfigureSearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConfigureSearchListener");
        }
    }


}
