package com.example.ecolim.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ResiduoDAO {

    private DBHelper dbHelper;

    public ResiduoDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    public boolean insertarRegistroResiduo(String empleado, String tipo, double cantidad, String observaciones, String fechaHora) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int idResiduo = obtenerIdResiduo(tipo);
        int idEmpleado = obtenerIdEmpleado(empleado);

        if(idEmpleado == -1){
            return false;
        }

        ContentValues valores = new ContentValues();
        valores.put("idEmpleado", idEmpleado);
        valores.put("idResiduo", idResiduo);
        valores.put("cantidad", cantidad);
        valores.put("observaciones", observaciones);
        valores.put("fechaRegistro", fechaHora);

        long resultado = db.insert(DBHelper.TABLA_REGISTRO, null, valores);
        return resultado != -1;
    }

    public int obtenerIdEmpleado(String nombreEmpleado) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int idEmpleado = -1;

        Cursor cursor = db.rawQuery("SELECT idEmpleado FROM " + DBHelper.TABLA_EMPLEADO + " WHERE nombre = ?",
                new String[]{nombreEmpleado});

        if (cursor != null && cursor.moveToFirst()) {
            idEmpleado = cursor.getInt(cursor.getColumnIndexOrThrow("idEmpleado"));
            cursor.close();
        }

        return idEmpleado;
    }

    public int obtenerIdResiduo(String tipoResiduo){
        switch (tipoResiduo){
            case "Plástico":
                return 1;
            case "Vidrio":
                return 2;
            case "Metal":
                return 3;
            case "Orgánico":
                return 4;
            case "Papel":
                return 5;
            case "Electrónico":
                return 6;
            default:
                return 0;
        }
    }
}
