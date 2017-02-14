package br.com.hotelurbano.desafio.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.hotelurbano.desafio.conexao.Conexao;
import br.com.hotelurbano.desafio.conexao.ConexaoBuscaCidades;
import br.com.hotelurbano.desafio.conexao.ConexaoListener;
import br.com.hotelurbano.desafio.desafiohotelurbano.R;
import br.com.hotelurbano.desafio.domain.City;


public class HotelSearchFragment extends Fragment  implements DatePickerDialog.OnDateSetListener, ConexaoListener{

    private EditText inputArrivalDate;
    private EditText inputDepartureDate;

    private Date arrivalDate;
    private Date departureDate;

    private EditText selectedInputDateView;
    private AutoCompleteTextView citiesAutoComplete;
    private Button button;

    private ArrayList<String> cities;
    private ArrayAdapter<String> citiesAdapter;


    public HotelSearchFragment() {
        arrivalDate = new Date();
        departureDate = new Date();
    }

    public static HotelSearchFragment newInstance() {
        HotelSearchFragment fragment = new HotelSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_hotel_search, container, false);

        citiesAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.hotel_search_input_city);
        loadCityList();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        inputArrivalDate = (EditText) view.findViewById(R.id.hotel_search_input_arrival_date);
        inputArrivalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view, arrivalDate);
            }
        });
        inputArrivalDate.setText(df.format(arrivalDate));


        inputDepartureDate = (EditText) view.findViewById(R.id.hotel_search_input_departure_date);
        inputDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view, departureDate);
            }
        });
        inputDepartureDate.setText(df.format(departureDate));

        button = (Button) view.findViewById(R.id.send_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(button.getWindowToken(), 0);

                if(validateParameters())
                    loadResultsFragment();
            }
        });


        return view;
    }

    public void showDatePickerDialog(View v, Date date) {

        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        DialogFragment newFragment = new DatePickerFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putLong("currentDate", date.getTime());
        newFragment.setArguments(bundle);

        ((DatePickerFragmentDialog) newFragment).setOnDateSetListener(this);
        selectedInputDateView = (EditText) v;

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        final Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        if(selectedInputDateView.getId() == R.id.hotel_search_input_arrival_date){
            arrivalDate = c.getTime();
        }
        else {
            departureDate = c.getTime();
        }

        String dateString = day + "/" + (month+1) + "/" + year;

        selectedInputDateView.setText(dateString);
    }

    private void loadCityList (){
        ConexaoBuscaCidades cbc = new ConexaoBuscaCidades(this);
        cbc.iniciar();
    }

    @Override
    public void conexaoRetornouComSucesso(Conexao conexao) {

        if (conexao instanceof ConexaoBuscaCidades) {
            List<City> itens = (List<City>) conexao.getResultado();
            cities = new ArrayList<>();
            for(City c : itens  ){
                cities.add(c.getName());
            }
            citiesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, cities);
            citiesAutoComplete.setAdapter(citiesAdapter);
        }
    }

    @Override
    public void conexaoRetornouComErro(Conexao conexao) {
        Toast.makeText(getContext(), "Erro ao recuperar dados", Toast.LENGTH_SHORT).show();
    }

    private boolean validateParameters(){

        if("".equals(citiesAutoComplete.getText().toString())){
            Toast.makeText(getContext(), "Você deve indicar uma cidade", Toast.LENGTH_LONG).show();
            return false;
        }

        if(arrivalDate != null && departureDate != null){
            if(arrivalDate.after(departureDate)) {
                Toast.makeText(getContext(), "A data de chegada deve ser anterior a data de saída", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void loadResultsFragment() {
        HotelSearchResultFragment hotelSearchResultFragment = HotelSearchResultFragment.newInstance(citiesAutoComplete.getText().toString(), arrivalDate, departureDate);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.main_fragment_container, hotelSearchResultFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
