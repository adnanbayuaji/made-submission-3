package id.ac.astra.polman.projectkamus;

import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;

import id.ac.astra.polman.projectkamus.Adapter.SearchAdapter;
import id.ac.astra.polman.projectkamus.Helper.KamusHelper;
import id.ac.astra.polman.projectkamus.Model.KamusModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private KamusHelper kamusHelper;
    private SearchAdapter searchAdapter;

    private ArrayList<KamusModel> list = new ArrayList<>();

    RecyclerView recyclerView;
    SearchView searchView;
    String lang_selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        searchView = (SearchView)findViewById(R.id.search_bar);
        searchView.onActionViewExpanded(); //supaya always expanded
        searchView.setOnQueryTextListener(this);

        kamusHelper = new KamusHelper(this);
        searchAdapter = new SearchAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searchAdapter);

        //get Default English - Indonesia
        lang_selection = "Eng";
        getData(lang_selection, "");
    }

    private void getData(String selection, String search) {
        try {
            kamusHelper.open();
            if (search.isEmpty()) {
                list = kamusHelper.getAllData(selection);
            } else {
                list = kamusHelper.getDataByName(search, selection);
            }

            String title = null;
            String hint = null;
            if (selection == "Eng") {
                title   = getResources().getString(R.string.eng_to_ind);
                hint    = getResources().getString(R.string.search_keyword);
            } else {
                title = getResources().getString(R.string.ind_to_eng);
                hint    = getResources().getString(R.string.cari_kosakata);
            }
            getSupportActionBar().setSubtitle(title);
            searchView.setQueryHint(hint);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            kamusHelper.close();
        }
        searchAdapter.replaceAll(list);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_english_to_indo) {
            lang_selection = "Eng";
            getData(lang_selection, "");
        } else if (id == R.id.nav_indo_to_english) {
            lang_selection = "Ind";
            getData(lang_selection, "");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String keyword) {
        getData(lang_selection, keyword);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String keyword) {
        getData(lang_selection, keyword);
        return false;
    }
}
