package com.example.ecolim.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ecolim.Registro_R_Monitoreo;
import com.example.ecolim.models.RegistroResiduo;
import com.example.ecolim.models.ReporteResiduo;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> obtenerListaNombresEmpleados() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> empleados = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT nombre FROM " + DBHelper.TABLA_EMPLEADO, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                empleados.add(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
            }
            cursor.close();
        }

        return empleados;
    }

    public int obtenerIdResiduo(String nombreResiduo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int idResiduo = -1;

        Cursor cursor = db.rawQuery("SELECT idResiduo FROM residuos WHERE nombre = ?", new String[]{nombreResiduo});

        if (cursor.moveToFirst()) {
            idResiduo = cursor.getInt(cursor.getColumnIndexOrThrow("idResiduo"));
        }
        cursor.close();

        return idResiduo;
    }

    public int obtenerTotalRegistros() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DBHelper.TABLA_REGISTRO, null);
        cursor.moveToFirst();
        int total = cursor.getInt(0);
        cursor.close();
        return total;
    }

    public double obtenerCantidadTotal() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(cantidad) FROM " + DBHelper.TABLA_REGISTRO, null);
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    public String obtenerResiduoMasComun() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r.nombre FROM residuos r JOIN registro_residuos rg ON r.idResiduo = rg.idResiduo GROUP BY rg.idResiduo ORDER BY COUNT(rg.idResiduo) DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String tipo = cursor.getString(0);
            cursor.close();
            return tipo;
        }
        cursor.close();
        return "Ninguno";
    }

    public List<RegistroResiduo> obtenerTodosRegistros() {
        List<RegistroResiduo> registros = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT e.nombre AS nombreEmpleado, \n" +
                "r.nombre AS nombreResiduo, \n" +
                "rg.cantidad, \n" +
                "rg.fechaRegistro, \n" +
                "rg.observaciones\n" +
                "FROM registro_residuos rg\n" +
                "JOIN empleados e ON rg.idEmpleado = e.idEmpleado\n" +
                "JOIN residuos r ON rg.idResiduo = r.idResiduo\n" +
                "ORDER BY rg.fechaRegistro DESC";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                registros.add(new RegistroResiduo(
                        cursor.getString(cursor.getColumnIndexOrThrow("nombreEmpleado")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nombreResiduo")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("cantidad")),
                        cursor.getString(cursor.getColumnIndexOrThrow("fechaRegistro")),
                        cursor.getString(cursor.getColumnIndexOrThrow("observaciones")) == null ? "Sin observaciones" : cursor.getString(cursor.getColumnIndexOrThrow("observaciones"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return registros;
    }

    public List<ReporteResiduo> obtenerReportePorTipo() {
        List<ReporteResiduo> reportes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT r.nombre, SUM(rg.cantidad) as cantidadTotal " +
                "FROM registro_residuos rg " +
                "JOIN residuos r ON rg.idResiduo = r.idResiduo " +
                "WHERE rg.fechaRegistro >= ? AND rg.fechaRegistro <= ? " +
                "GROUP BY r.nombre";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                reportes.add(new ReporteResiduo(
                        cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("cantidadTotal"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reportes;
    }

    public List<ReporteResiduo> obtenerReportesPorFecha(String fechaInicio, String fechaFin) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<ReporteResiduo> reportes = new ArrayList<>();
        Log.e("FECHA REPORTE POR FECHA:", fechaInicio);
        Log.e("FECHA REPORTE POR FECHA:", fechaFin);

        String query = "SELECT r.nombre, SUM(rg.cantidad) as cantidadTotal " +
                "FROM registro_residuos rg " +
                "JOIN residuos r ON rg.idResiduo = r.idResiduo " +
                "WHERE DATETIME(rg.fechaRegistro) BETWEEN DATETIME(?) AND DATETIME(?) " +
                "GROUP BY rg.idResiduo";

        Log.e("FECHA CONSULTA QUERY:", query);

        Cursor cursor = db.rawQuery(query, new String[]{fechaInicio, fechaFin});

        if (cursor.moveToFirst()) {
            do {
                reportes.add(new ReporteResiduo(
                        cursor.getString(0),
                        cursor.getDouble(1)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return reportes;
    }
}
