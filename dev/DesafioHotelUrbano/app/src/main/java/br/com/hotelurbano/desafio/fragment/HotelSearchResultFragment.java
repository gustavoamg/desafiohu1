package br.com.hotelurbano.desafio.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.hotelurbano.desafio.adapter.HotelSearchResultAdapter;
import br.com.hotelurbano.desafio.adapter.OnListFragmentInteractionListener;
import br.com.hotelurbano.desafio.conexao.Conexao;
import br.com.hotelurbano.desafio.conexao.ConexaoBuscaDisponibilidade;
import br.com.hotelurbano.desafio.conexao.ConexaoListener;
import br.com.hotelurbano.desafio.desafiohotelurbano.R;
import br.com.hotelurbano.desafio.domain.Availability;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HotelSearchResultFragment extends Fragment implements OnListFragmentInteractionListener, ConexaoListener {

    private static final String ARG_CITY = "city";
    private static final String ARG_FROM = "from";
    private static final String ARG_TO = "to";

    private String city;
    private Date from;
    private Date to;

    private int page = 0;
    private int total = 0;

    private List<Availability> searchResult;

    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private HotelSearchResultAdapter hotelSearchResultAdapter;
    private LinearLayout noItensFoundMessage;

    ProgressDialog loading = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HotelSearchResultFragment() {
        searchResult = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public static HotelSearchResultFragment newInstance(String city, Date from, Date to) {
        HotelSearchResultFragment fragment = new HotelSearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CITY, city);
        args.putLong(ARG_FROM, from.getTime());
        args.putLong(ARG_TO, to.getTime());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            city = getArguments().getString(ARG_CITY);
            from = new Date(getArguments().getLong(ARG_FROM));
            to = new Date(getArguments().getLong(ARG_TO));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hotel_search_result_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        noItensFoundMessage = (LinearLayout) view.findViewById(R.id.no_items_found_message_view);

        loading = new ProgressDialog(getContext());
        loading.setCancelable(false);
        loading.setMessage("Procurando Hoteis...");
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.show();

        ConexaoBuscaDisponibilidade conexao = new ConexaoBuscaDisponibilidade(this, city, from, to);
        conexao.iniciar();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListFragmentInteraction(Availability mItem) {

    }

    @Override
    public void onAdapterRequestMoreData() {
        if(searchResult.size() < total) {

            ConexaoBuscaDisponibilidade conexao = new ConexaoBuscaDisponibilidade(this, city, from, to, ++page);
            conexao.iniciar();
        }
    }

    @Override
    public void conexaoRetornouComSucesso(Conexao conexao) {

        if (conexao instanceof ConexaoBuscaDisponibilidade) {
            if(loading != null){
                loading.dismiss();
            }


            total = ((ConexaoBuscaDisponibilidade) conexao).getTotal();

            searchResult.addAll((List<Availability>) conexao.getResultado());

            if(hotelSearchResultAdapter == null) {
                hotelSearchResultAdapter = new HotelSearchResultAdapter(searchResult, this);
                recyclerView.setAdapter(hotelSearchResultAdapter);

                if(searchResult.size() > 0){
                    noItensFoundMessage.setVisibility(View.GONE);
                }
                else {
                    noItensFoundMessage.setVisibility(View.VISIBLE);
                }
            }
            else {
                hotelSearchResultAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void conexaoRetornouComErro(Conexao conexao) {
        if (conexao instanceof ConexaoBuscaDisponibilidade) {
            if (loading != null) {
                loading.dismiss();
            }

            noItensFoundMessage.setVisibility(View.VISIBLE);

            Toast.makeText(getContext(), "Erro ao recuperar dados", Toast.LENGTH_SHORT).show();
        }
    }
}
