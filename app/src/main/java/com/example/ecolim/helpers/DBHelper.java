package com.example.ecolim.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "ecolim_db.sqlite";

    public static final String TABLA_EMPLEADO = "empleados";
    public static final String TABLA_RESIDUO = "residuos";
    public static final String TABLA_REGISTRO = "registro_residuos";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_EMPLEADO + "(" +
                "idEmpleado INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLA_RESIDUO + "(" +
                "idResiduo INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "descripcion TEXT)");

        db.execSQL("CREATE TABLE " + TABLA_REGISTRO + "(" +
                "idRegistro INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idEmpleado INTEGER NOT NULL," +
                "idResiduo INTEGER NOT NULL," +
                "cantidad REAL NOT NULL," +
                "fechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "observaciones TEXT," +
                "FOREIGN KEY(idEmpleado) REFERENCES empleados(idEmpleado)," +
                "FOREIGN KEY(idResiduo) REFERENCES residuos(idResiduo))");

        insertarDatosIniciales(db);
    }

    void insertarDatosIniciales(SQLiteDatabase db) {
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Plástico', 'Residuos plásticos reciclables')");
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Vidrio', 'Residuos de vidrio reciclables')");
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Metal', 'Residuos metálicos reciclables')");
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Orgánico', 'Residuos orgánicos biodegradables')");
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Papel', 'Residuos de papel reciclable')");
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Electrónico', 'Residuos electrónicos reciclables')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_REGISTRO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_EMPLEADO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_RESIDUO);
        onCreate(db);
    }
}