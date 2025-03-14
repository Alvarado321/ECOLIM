package com.example.ecolim.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "ecolim_db.sqlite";

    public static final String TABLA_USUARIO = "usuarios";
    public static final String TABLA_EMPLEADO = "empleados";
    public static final String TABLA_RESIDUO = "residuos";
    public static final String TABLA_REGISTRO = "registro_residuos";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabla de usuarios (para autenticación y control de acceso)
        db.execSQL("CREATE TABLE " + TABLA_USUARIO + "(" +
                "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "rol TEXT NOT NULL)");

        // Tabla de empleados (para registro de residuos)
        db.execSQL("CREATE TABLE " + TABLA_EMPLEADO + "(" +
                "idEmpleado INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "cargo TEXT," +
                "departamento TEXT," +
                "activo INTEGER DEFAULT 1)");

        // Tabla de residuos (para registro de residuos)
        db.execSQL("CREATE TABLE " + TABLA_RESIDUO + "(" +
                "idResiduo INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "descripcion TEXT)");

        // Tabla de registro de residuos
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
        // Usuario administrador por defecto
        db.execSQL("INSERT INTO usuarios (nombre, email, password, rol) VALUES ('Admin', 'admin@ecolim.com', 'admin123', 'Administrador')");

        // Tipos de residuos
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Plástico', 'Residuos plásticos reciclables')");
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Vidrio', 'Residuos de vidrio reciclables')");
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Metal', 'Residuos metálicos reciclables')");
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Orgánico', 'Residuos orgánicos biodegradables')");
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Papel', 'Residuos de papel reciclable')");
        db.execSQL("INSERT INTO residuos (nombre, descripcion) VALUES ('Electrónico', 'Residuos electrónicos reciclables')");

        // Empleado de ejemplo
        db.execSQL("INSERT INTO empleados (nombre, email, cargo, departamento) VALUES ('Juan Pérez', 'juan@ecolim.com', 'Operador', 'Producción')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Respaldo de datos importantes
        if (oldVersion == 1 && newVersion == 2) {
            // Respaldar empleados existentes
            db.execSQL("CREATE TABLE empleados_backup AS SELECT * FROM " + TABLA_EMPLEADO);
            
            // Elimina tablas
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_REGISTRO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_EMPLEADO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_RESIDUO);
            
            // Recrea la estructura
            onCreate(db);
            
            // Migra empleados antiguos a la nueva estructura
            db.execSQL("INSERT INTO empleados (nombre, email, cargo, departamento) " +
                    "SELECT nombre, email, 'No especificado', 'No especificado' FROM empleados_backup");
            
            // Elimina tabla de respaldo
            db.execSQL("DROP TABLE IF EXISTS empleados_backup");
        } else {
            // Si no es una actualización específica, recrear todo
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_REGISTRO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_EMPLEADO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_RESIDUO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
            onCreate(db);
        }
    }
}