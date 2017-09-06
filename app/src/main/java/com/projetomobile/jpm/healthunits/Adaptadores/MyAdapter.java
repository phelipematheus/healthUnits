package com.projetomobile.jpm.healthunits.Adaptadores;

/**
 * Created by muril on 05/09/2017.
 */



        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import com.projetomobile.jpm.healthunits.R;
        import com.projetomobile.jpm.healthunits.ValueObject.Estabelecimento;

        import java.util.List;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Estabelecimento> values;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public MyAdapter(List<Estabelecimento> myDataset) {
        values = myDataset;
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from( parent.getContext() );
        View v = inflater.inflate(R.layout.row_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final Estabelecimento estabelecimento = values.get(position);
        holder.txtHeader.setText(estabelecimento.getNomeFantasia());
        holder.txtFooter.setText(estabelecimento.getDescricaoCompleta());
        Log.e(null,"FOOOI");
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

}