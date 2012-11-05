package com.ermes.controlGasto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

public class Apunte {

    private static class BDHelper extends SQLiteOpenHelper {

        public BDHelper(Context context) {
            super(context, N_BBDD, null, VERSION_BBDD);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + N_TABLA + " (" + ID_FILA + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ID_CANTIDAD + " INTEGER, " + ID_FECHA
                    + " INTEGER, " + ID_CONCEPTO + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + N_TABLA);
        }
    }

    public int CANTIDAD = 0;
    public static final String ID_FILA = "_id";
    public static final String ID_FECHA = "_fecha";
    public static final String ID_CANTIDAD = "_cantidad";
    public static final String ID_CONCEPTO = "_concepto";
    private static final String N_BBDD = "Gasto";
    private static final String N_TABLA = "tGasto";
    private static final int VERSION_BBDD = 1;
    private BDHelper nHelper;
    private final Context nContext;

    private SQLiteDatabase nBBDD;

    public Apunte(Context c) {
        nContext = c;
    }

    public Apunte abrir() {
        nHelper = new BDHelper(nContext);
        nBBDD = nHelper.getWritableDatabase();
        return this;
    }

    public int borrarApunte(String id) {
        return nBBDD.delete(N_TABLA, ID_FILA + "=" + id, null);
    }

    public void cerrar() {
        nHelper.close();
    }

    public long crearApunte(String cantidad, Long fecha, String concepto) {
        // Date d = new Date(cursor.getLong(DATE_FIELD_INDEX));
        ContentValues cv = new ContentValues();

        cv.put(ID_CANTIDAD, cantidad);
        cv.put(ID_FECHA, fecha);
        cv.put(ID_CONCEPTO, concepto);
        return nBBDD.insert(N_TABLA, null, cv);
    }

    public String listarApuntes() {
        String[] columnas = new String[] { ID_FILA, ID_CANTIDAD, ID_FECHA, ID_CONCEPTO };
        Cursor c = nBBDD.query(N_TABLA, columnas, null, null, null, null, ID_FECHA + " desc", null);
        String resultado = "";

        int iFila = c.getColumnIndex(ID_FILA);
        int iCantidad = c.getColumnIndex(ID_CANTIDAD);
        int iFecha = c.getColumnIndex(ID_FECHA);
        int iConcepto = c.getColumnIndex(ID_CONCEPTO);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            resultado = resultado + c.getString(iFila) + " " + DateFormat.format("dd-MMM", c.getLong(iFecha)) + " " + c.getString(iConcepto) + " "
                    + c.getString(iCantidad) + "\n";
            CANTIDAD += c.getInt(iCantidad);
        }

        return resultado;
    }

}
