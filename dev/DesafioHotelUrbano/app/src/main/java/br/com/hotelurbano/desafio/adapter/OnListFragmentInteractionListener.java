package br.com.hotelurbano.desafio.adapter;

import br.com.hotelurbano.desafio.domain.Availability;

/**
 * Created by gustavoamg on 13/02/17.
 */
public interface OnListFragmentInteractionListener {
    void onListFragmentInteraction(Availability mItem);
    void onAdapterRequestMoreData();
}
