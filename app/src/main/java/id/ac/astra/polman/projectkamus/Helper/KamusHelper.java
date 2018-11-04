package id.ac.astra.polman.projectkamus.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import id.ac.astra.polman.projectkamus.Database.DatabaseHelper;
import id.ac.astra.polman.projectkamus.Model.KamusModel;
import id.ac.astra.polman.projectkamus.R;

import static android.provider.BaseColumns._ID;
import static id.ac.astra.polman.projectkamus.Database.DatabaseContract.KamusColumns.FIELD_ARTI;
import static id.ac.astra.polman.projectkamus.Database.DatabaseContract.KamusColumns.FIELD_KATA;
import static id.ac.astra.polman.projectkamus.Database.DatabaseContract.TABLE_ENG_TO_IND;
import static id.ac.astra.polman.projectkamus.Database.DatabaseContract.TABLE_IND_TO_ENG;

/**
 * Created by Jihad044 on 30/06/2018.
 */

public class KamusHelper {

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public KamusHelper(Context context){
        this.context = context;
    }

    public KamusHelper open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public ArrayList<KamusModel> getDataByName(String cari, String selection){

        String category = null;
        Cursor cursor;
        if(selection == "Eng"){
            // method query
            cursor = database.query(TABLE_ENG_TO_IND,null,FIELD_KATA+" LIKE ?",new String[]{cari.trim()+"%"},null,null,_ID + " ASC",null);
            category = context.getResources().getString(R.string.eng_to_ind);
        }else{
            // method rawQuery
            cursor = database.rawQuery("SELECT * FROM " + TABLE_IND_TO_ENG +
                    " WHERE " + FIELD_KATA + " LIKE " + cari.trim() + "%'", null);
            category = context.getResources().getString(R.string.ind_to_eng);
        }
        cursor.moveToFirst();

        ArrayList<KamusModel> arrayList = new ArrayList<>();
        KamusModel kamusModel;
        if (cursor.getCount()>0) {
            do {
                kamusModel = new KamusModel();
                kamusModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                kamusModel.setKata(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_KATA)));
                kamusModel.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_ARTI)));
                kamusModel.setCategory(category);

                arrayList.add(kamusModel);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public ArrayList<KamusModel> getAllData(String selection){
        Cursor cursor;
        String category = null;
        if(selection == "Eng"){
            cursor = database.query(TABLE_ENG_TO_IND,null,null,null,null,null,_ID+ " ASC",null);
            category = context.getResources().getString(R.string.eng_to_ind);
        }else{
            cursor = database.query(TABLE_IND_TO_ENG,null,null,null,null,null,_ID+ " ASC",null);
            category = context.getResources().getString(R.string.ind_to_eng);
        }
        cursor.moveToFirst();

        ArrayList<KamusModel> arrayList = new ArrayList<>();
        KamusModel kamusModel;
        if (cursor.getCount()>0) {
            do {
                kamusModel = new KamusModel();
                kamusModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                kamusModel.setKata(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_KATA)));
                kamusModel.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(FIELD_ARTI)));
                kamusModel.setCategory(category);

                arrayList.add(kamusModel);
                cursor.moveToNext();

            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(KamusModel kamusModel, String selection){
        String table = null;
        if(selection == "Eng"){
            table = TABLE_ENG_TO_IND;
        }else{
            table = TABLE_IND_TO_ENG;
        }

        ContentValues initialValues =  new ContentValues();
        initialValues.put(FIELD_KATA, kamusModel.getKata());
        initialValues.put(FIELD_ARTI, kamusModel.getDeskripsi());
        return database.insert(table, null, initialValues);
    }

    public void beginTransaction(){
        database.beginTransaction();
    }

    public void setTransactionSuccess(){
        database.setTransactionSuccessful();
    }

    public void endTransaction(){
        database.endTransaction();
    }

    public void insertTransaction(KamusModel kamusModel, String selection){
        String table = null;
        if(selection == "Eng"){
            table = TABLE_ENG_TO_IND;
        }else{
            table = TABLE_IND_TO_ENG;
        }

        String sql = "INSERT INTO "+table+" ("+FIELD_KATA+", "+FIELD_ARTI
                +") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, kamusModel.getKata());
        stmt.bindString(2, kamusModel.getDeskripsi());
        stmt.execute();
        stmt.clearBindings();
    }

    public int update(KamusModel kamusModel, String selection){
        String table = null;
        if(selection == "Eng"){
            table = TABLE_ENG_TO_IND;
        }else{
            table = TABLE_IND_TO_ENG;
        }

        ContentValues args = new ContentValues();
        args.put(FIELD_KATA, kamusModel.getKata());
        args.put(FIELD_ARTI, kamusModel.getDeskripsi());
        return database.update(table, args, _ID + "= '" + kamusModel.getId() + "'", null);
    }

    public int delete(int id, String selection){
        String table = null;
        if(selection == "Eng"){
            table = TABLE_ENG_TO_IND;
        }else{
            table = TABLE_IND_TO_ENG;
        }

        return database.delete(table, _ID + " = '"+id+"'", null);
    }
}
