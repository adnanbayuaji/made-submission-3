package id.ac.astra.polman.projectkamus;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import id.ac.astra.polman.projectkamus.Helper.KamusHelper;
import id.ac.astra.polman.projectkamus.Model.KamusModel;
import id.ac.astra.polman.projectkamus.Preference.KamusPreference;

public class PreLoadActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_load);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        new LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void> {

        final String TAG = LoadData.class.getSimpleName();

        KamusHelper kamusHelper;
        KamusPreference appPreference;

        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            kamusHelper  = new KamusHelper(PreLoadActivity.this);
            appPreference   = new KamusPreference(PreLoadActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            Boolean firstRun = appPreference.getFirstRun();
            if (firstRun) {
                ArrayList<KamusModel> kamusModelsEngToInd = preLoadRaw("Eng");
                ArrayList<KamusModel> kamusModelsIndToEng = preLoadRaw("Ind");

                kamusHelper.open();
//                progress = 30;
//                publishProgress((int) progress);
                Double progressMaxInsert = 100.0;
                int total_size = kamusModelsEngToInd.size() + kamusModelsIndToEng.size();
                Double progressDiff = (progressMaxInsert - progress) / total_size;

                //Eng to Indo
                kamusHelper.beginTransaction();
                try {
                    for (KamusModel model : kamusModelsEngToInd) {
                        kamusHelper.insertTransaction(model, "Eng");
                    }
                    kamusHelper.setTransactionSuccess();
                } catch (Exception e) {
                    Log.e(TAG, "doInBackground: Exception");
                }
                kamusHelper.endTransaction();
                progress += progressDiff;
                publishProgress((int) progress);

                //Ind to Eng
                kamusHelper.beginTransaction();
                try {
                    for (KamusModel model : kamusModelsIndToEng) {
                        kamusHelper.insertTransaction(model, "Ind");
                    }
                    kamusHelper.setTransactionSuccess();
                } catch (Exception e) {
                    Log.e(TAG, "doInBackground: Exception");
                }
                kamusHelper.endTransaction();
                progress += progressDiff;
                publishProgress((int) progress);

                //close untuk semua insert selesai
                kamusHelper.close();

                appPreference.setFirstRun(false);
                publishProgress((int) maxprogress);

            } else {
                try {
                    synchronized (this) {
                        this.wait(2000);

                        publishProgress(50);

                        this.wait(2000);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        //Update prosesnya
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        /*
        Setelah proses selesai
        Berjalan di Main Thread
        */
        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(PreLoadActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    /**
     * Parsing raw data text berupa data menjadi array mahasiswa
     *
     * @return array model dari semua mahasiswa
     */
    public ArrayList<KamusModel> preLoadRaw(String selection) {
        int raw_data;
        if(selection == "Eng"){
            raw_data = R.raw.english_indonesia;
        }else{
            raw_data = R.raw.indonesia_english;
        }
        ArrayList<KamusModel> kamusModels = new ArrayList<>();
        String line = null;
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(raw_data);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            int count = 0;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");

                KamusModel kamusModel;

                kamusModel = new KamusModel(splitstr[0], splitstr[1]);
                kamusModels.add(kamusModel);
                count++;
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kamusModels;
    }
}
