package br.com.hotelurbano.desafio.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.hotelurbano.desafio.desafiohotelurbano.R;
import br.com.hotelurbano.desafio.domain.Availability;

public class HotelSearchResultAdapter extends RecyclerView.Adapter<HotelSearchResultAdapter.ViewHolder> {

    private final List<Availability> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final SimpleDateFormat df;

    public HotelSearchResultAdapter(List<Availability> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        df = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.hotelTextView.setText(mValues.get(position).getHotel().getName());
        holder.minimumNighnts.setText(String.valueOf(mValues.get(position).getMinimumNights()));
        holder.checkinTextView.setText(df.format(mValues.get(position).getDate()));
        holder.cityTextView.setText(mValues.get(position).getHotel().getCity().getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        if(position == mValues.size()-1){
            if(mListener != null)
                mListener.onAdapterRequestMoreData();
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView hotelTextView;
        public final TextView minimumNighnts;
        public final TextView cityTextView;
        public final TextView checkinTextView;
        public Availability mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            hotelTextView = (TextView) view.findViewById(R.id.hotel);
            minimumNighnts = (TextView) view.findViewById(R.id.available_nights);
            cityTextView = (TextView) view.findViewById(R.id.cidade);
            checkinTextView = (TextView) view.findViewById(R.id.checkin);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + minimumNighnts.getText() + "'";
        }
    }
}
