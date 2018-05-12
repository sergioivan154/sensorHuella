package sagarpa.planetmedia.com.sagarpapp.Utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

import sagarpa.planetmedia.com.sagarpapp.Model.Adapter.Huella;
import sagarpa.planetmedia.com.sagarpapp.Model.Entity.User;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Utilities.CreateUserTab);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int OldVersion, int NewVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
        onCreate(sqLiteDatabase);
    }

    public List<User> selectUser(String email){
        List<User> lUsuarios = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + Utilities.CampName  + "," + Utilities.CampPassword  +  " FROM " + Utilities.TABLEuser +" WHERE "+ Utilities.CampEmail  + " = "+ email, null);
        if (c.moveToFirst()){
            do {
                // Passing values
                String column1 = c.getString(0);
                String column2 = c.getString(1);

                User user = new User(column1, email ,column2);

                lUsuarios.add(user);

                // Do something Here with values
            } while(c.moveToNext());
        }
        c.close();
        db.close();

        return lUsuarios;
    }

    public List<Huella> selectHuellas(){
        List<Huella> lHuellas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + Utilities.CampName  + "," + Utilities.CampEmail   + ", " + Utilities.CampHuella  + " FROM " + Utilities.TABLEuser +";", null);
        if (c.moveToFirst()){
            do {
                // Passing values
                String column1 = c.getString(0);
                String column2 = c.getString(1);
                String column3 = null;
                try {
                    column3 = new AppUtilidadesEncript().Desencriptar(c.getString(2));
                } catch (Exception e) {
                    column3 = "";
                }

                byte[] decodedString = Base64.decode(column3, Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                Huella huella = new Huella(column1, column2 ,bm);

                lHuellas.add(huella);

                // Do something Here with values
            } while(c.moveToNext());
        }
        c.close();
        db.close();

        return lHuellas;
    }




}
