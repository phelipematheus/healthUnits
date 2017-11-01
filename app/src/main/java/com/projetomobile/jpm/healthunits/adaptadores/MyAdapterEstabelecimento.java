package com.projetomobile.jpm.healthunits.adaptadores;

/**
 * Created by JV on 06/09/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.projetomobile.jpm.healthunits.R;
import com.projetomobile.jpm.healthunits.telas.TelaMaps;
import com.projetomobile.jpm.healthunits.valueobject.Estabelecimento;

import java.util.List;


public class MyAdapterEstabelecimento extends RecyclerView.Adapter<MyAdapterEstabelecimento.ViewHolder> {
    private List<Estabelecimento> values;
    public Context contextListaEstabelecimento;

    public static Estabelecimento estabeleci;
    public static LatLng tracaOrigem;
    public static boolean verificaSePodeFinalizarActivity = false;
    private LocationManager locationManager;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public Button btnGo;
        public View layout;


        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
            btnGo = (Button) v.findViewById(R.id.btn_go);
        }
    }

    public void add(int position, Estabelecimento item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterEstabelecimento(Context contextListaEstabelecimento, List<Estabelecimento> myDataset, LatLng origem) {
        tracaOrigem = origem;
        this.contextListaEstabelecimento = contextListaEstabelecimento;
        values = myDataset;
    }
    public MyAdapterEstabelecimento(List<Estabelecimento> myDataset) {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterEstabelecimento.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Estabelecimento estabelecimento = values.get(position);
        holder.txtHeader.setText(estabelecimento.getNomeFantasia());
        holder.btnGo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abreMapaComRota = new Intent(contextListaEstabelecimento, TelaMaps.class);
                estabeleci = values.get(position);
                verificaSePodeFinalizarActivity = true;
                contextListaEstabelecimento.startActivity(abreMapaComRota);

            }

        });

        holder.txtFooter.setText("Footer: " + estabelecimento.getDescricaoCompleta());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }


}
