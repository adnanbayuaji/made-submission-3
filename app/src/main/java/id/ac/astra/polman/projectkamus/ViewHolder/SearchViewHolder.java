package id.ac.astra.polman.projectkamus.ViewHolder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import id.ac.astra.polman.projectkamus.DetailKamusActivity;
import id.ac.astra.polman.projectkamus.Model.KamusModel;
import id.ac.astra.polman.projectkamus.R;

/**
 * Created by Nursing Bank IT Dept on 3/29/2018.
 */

public class SearchViewHolder extends RecyclerView.ViewHolder {

    TextView tvKosakata, tvArti, tvCategory;

    public SearchViewHolder(View itemView) {
        super(itemView);

        tvKosakata  = (TextView)itemView.findViewById(R.id.tvKosakata);
        tvArti      = (TextView)itemView.findViewById(R.id.tvArti);
//        tvCategory  = (TextView)itemView.findViewById(R.id.tvCategory);
    }

    public void bind(final KamusModel kamusModel) {
        tvKosakata.setText(kamusModel.getKata());
        tvArti.setText(kamusModel.getDeskripsi());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), DetailKamusActivity.class);
                intent.putExtra(DetailKamusActivity.ITEM_KOSAKATA, kamusModel.getKata());
                intent.putExtra(DetailKamusActivity.ITEM_ARTI, kamusModel.getDeskripsi());
                intent.putExtra(DetailKamusActivity.ITEM_CATEGORY, kamusModel.getCategory());
                itemView.getContext().startActivity(intent);
            }
        });
    }
}
