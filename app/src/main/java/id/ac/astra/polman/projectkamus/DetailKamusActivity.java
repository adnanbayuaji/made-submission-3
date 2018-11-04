package id.ac.astra.polman.projectkamus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailKamusActivity extends AppCompatActivity {

    public static final String ITEM_KOSAKATA    = "kosakata";
    public static final String ITEM_ARTI        = "arti";
    public static final String ITEM_CATEGORY    = "category";

    TextView tvKosakata, tvArti, tvCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kamus);

        getSupportActionBar().setTitle(getIntent().getStringExtra(ITEM_KOSAKATA));
        getSupportActionBar().setSubtitle(getIntent().getStringExtra(ITEM_CATEGORY));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvKosakata  = (TextView)findViewById(R.id.tvDetailKosakata);
        tvArti      = (TextView)findViewById(R.id.tvDetailArti);
//        tvCategory  = (TextView)findViewById(R.id.tvCategory);

        tvKosakata.setText(getIntent().getStringExtra(ITEM_KOSAKATA));
        tvArti.setText(getIntent().getStringExtra(ITEM_ARTI));
//        tvCategory.setText(getIntent().getStringExtra(ITEM_CATEGORY));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
