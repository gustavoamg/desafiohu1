package br.com.hotelurbano.desafio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import br.com.hotelurbano.desafio.adapter.OnListFragmentInteractionListener;
import br.com.hotelurbano.desafio.desafiohotelurbano.R;
import br.com.hotelurbano.desafio.domain.Availability;
import br.com.hotelurbano.desafio.fragment.ConfigureSearchFragment;
import br.com.hotelurbano.desafio.fragment.HotelSearchFragment;

public class MainActivity extends AppCompatActivity implements ConfigureSearchFragment.OnConfigureSearchListener, OnListFragmentInteractionListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DesafioHotelUrbanoApplication application = (DesafioHotelUrbanoApplication) getApplication();


        if(application.getUrl() != null)
            loadHotelSearchFragment();
        else
            loadInputUrlFragment();

    }

    private void loadInputUrlFragment(){

        Fragment configureSearchFragment = ConfigureSearchFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.main_fragment_container, configureSearchFragment);
        fragmentTransaction.commit();
    }

    private void loadHotelSearchFragment() {

        Fragment hotelSearchFragment = HotelSearchFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.main_fragment_container, hotelSearchFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onConfigureSearch() {
        loadHotelSearchFragment();
    }

    @Override
    public void onListFragmentInteraction(Availability mItem) {

    }

    @Override
    public void onAdapterRequestMoreData() {

    }
}
