-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS ecolim;
USE ecolim;

-- Tabla de empleados
CREATE TABLE IF NOT EXISTS empleados (
    idEmpleado INTEGER PRIMARY KEY AUTO_INCREMENT,
    nombre TEXT NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL
);

-- Tabla de residuos
CREATE TABLE IF NOT EXISTS residuos (
    idResiduo INTEGER PRIMARY KEY AUTO_INCREMENT,
    nombre TEXT NOT NULL,
    descripcion TEXT
);

-- Tabla de registro de residuos
CREATE TABLE IF NOT EXISTS registro_residuos (
    idRegistro INTEGER PRIMARY KEY AUTO_INCREMENT,
    idEmpleado INTEGER NOT NULL,
    idResiduo INTEGER NOT NULL,
    cantidad REAL NOT NULL,
    fechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP,
    observaciones TEXT,
    FOREIGN KEY(idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY(idResiduo) REFERENCES residuos(idResiduo)
);
