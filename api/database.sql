-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS ecolim;

-- Usar la base de datos
USE ecolim;

-- Limpiar tablas existentes si existen
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS registro_residuos;
DROP TABLE IF EXISTS empleados;
DROP TABLE IF EXISTS residuos;
DROP TABLE IF EXISTS usuarios;
SET FOREIGN_KEY_CHECKS = 1;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL DEFAULT 'Usuario'
);

-- Tabla de empleados
CREATE TABLE IF NOT EXISTS empleados (
    idEmpleado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    cargo VARCHAR(100),
    departamento VARCHAR(100),
    activo BOOLEAN DEFAULT true
);

-- Tabla de residuos
CREATE TABLE IF NOT EXISTS residuos (
    idResiduo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
);

-- Tabla de registro de residuos
CREATE TABLE IF NOT EXISTS registro_residuos (
    idRegistro INT AUTO_INCREMENT PRIMARY KEY,
    idEmpleado INT NOT NULL,
    idResiduo INT NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,
    fechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP,
    observaciones TEXT,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado),
    FOREIGN KEY (idResiduo) REFERENCES residuos(idResiduo)
);

-- Insertar usuarios
INSERT INTO usuarios (nombre, email, password, rol) VALUES
('Administrador', 'admin@ecolim.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Administrador'),
('Juan Pérez', 'juan@ecolim.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuario'),
('María García', 'maria@ecolim.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuario'),
('Carlos López', 'carlos@ecolim.com', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Usuario');
-- Nota: La contraseña para todos los usuarios es 'password'

-- Insertar empleados
INSERT INTO empleados (nombre, email, cargo, departamento, activo) VALUES
('Roberto Martínez', 'roberto@ecolim.com', 'Operador', 'Producción', 1),
('Ana Sánchez', 'ana@ecolim.com', 'Supervisor', 'Calidad', 1),
('Pedro Ramírez', 'pedro@ecolim.com', 'Técnico', 'Mantenimiento', 1),
('Laura Torres', 'laura@ecolim.com', 'Operador', 'Producción', 1),
('Miguel Ángel', 'miguel@ecolim.com', 'Supervisor', 'Producción', 1),
('Isabel Castro', 'isabel@ecolim.com', 'Técnico', 'Calidad', 0);

-- Insertar residuos
INSERT INTO residuos (nombre, descripcion) VALUES
('Plástico PET', 'Botellas y envases de plástico PET reciclables'),
('Vidrio Transparente', 'Botellas y envases de vidrio transparente'),
('Aluminio', 'Latas y envases de aluminio'),
('Papel Blanco', 'Papel de oficina y documentos'),
('Cartón', 'Cajas y empaques de cartón'),
('Residuos Orgánicos', 'Residuos de alimentos y jardín'),
('Electrónicos', 'Equipos y componentes electrónicos'),
('Plástico HDPE', 'Envases de plástico duro'),
('Metal Ferroso', 'Materiales ferrosos y acero'),
('Madera', 'Residuos de madera y derivados');

-- Insertar registros de residuos (últimos 30 días)
INSERT INTO registro_residuos (idEmpleado, idResiduo, cantidad, fechaRegistro, observaciones) VALUES
(1, 1, 25.5, DATE_SUB(NOW(), INTERVAL 1 DAY), 'Recolección diaria área producción'),
(2, 2, 15.2, DATE_SUB(NOW(), INTERVAL 2 DAY), 'Limpieza área comedor'),
(3, 3, 10.0, DATE_SUB(NOW(), INTERVAL 3 DAY), 'Mantenimiento maquinaria'),
(4, 4, 5.5, DATE_SUB(NOW(), INTERVAL 4 DAY), 'Oficinas administrativas'),
(5, 5, 30.0, DATE_SUB(NOW(), INTERVAL 5 DAY), 'Recepción mercancía'),
(1, 6, 12.3, DATE_SUB(NOW(), INTERVAL 6 DAY), 'Residuos cafetería'),
(2, 7, 8.7, DATE_SUB(NOW(), INTERVAL 7 DAY), 'Actualización equipos'),
(3, 8, 18.4, DATE_SUB(NOW(), INTERVAL 8 DAY), 'Área de empaque'),
(4, 9, 22.1, DATE_SUB(NOW(), INTERVAL 9 DAY), 'Renovación estructuras'),
(5, 10, 14.6, DATE_SUB(NOW(), INTERVAL 10 DAY), 'Remodelación oficinas'),
(1, 1, 28.3, DATE_SUB(NOW(), INTERVAL 11 DAY), 'Recolección diaria'),
(2, 2, 16.8, DATE_SUB(NOW(), INTERVAL 12 DAY), 'Limpieza general'),
(3, 3, 11.2, DATE_SUB(NOW(), INTERVAL 13 DAY), 'Mantenimiento preventivo'),
(4, 4, 6.7, DATE_SUB(NOW(), INTERVAL 14 DAY), 'Archivo muerto'),
(5, 5, 32.5, DATE_SUB(NOW(), INTERVAL 15 DAY), 'Descarga productos'),
(1, 6, 13.9, DATE_SUB(NOW(), INTERVAL 16 DAY), 'Residuos orgánicos'),
(2, 7, 9.4, DATE_SUB(NOW(), INTERVAL 17 DAY), 'Reemplazo equipos'),
(3, 8, 19.8, DATE_SUB(NOW(), INTERVAL 18 DAY), 'Producción diaria'),
(4, 9, 23.6, DATE_SUB(NOW(), INTERVAL 19 DAY), 'Área metalurgia'),
(5, 10, 15.3, DATE_SUB(NOW(), INTERVAL 20 DAY), 'Renovación mobiliario');

-- Registros más recientes (última semana)
INSERT INTO registro_residuos (idEmpleado, idResiduo, cantidad, fechaRegistro, observaciones) VALUES
(1, 1, 27.8, DATE_SUB(NOW(), INTERVAL 1 HOUR), 'Turno mañana'),
(2, 2, 17.5, DATE_SUB(NOW(), INTERVAL 2 HOUR), 'Turno tarde'),
(3, 3, 12.1, DATE_SUB(NOW(), INTERVAL 3 HOUR), 'Turno noche'),
(4, 4, 7.3, DATE_SUB(NOW(), INTERVAL 4 HOUR), 'Área administrativa'),
(5, 5, 33.9, DATE_SUB(NOW(), INTERVAL 5 HOUR), 'Almacén central'),
(1, 6, 14.7, DATE_SUB(NOW(), INTERVAL 6 HOUR), 'Comedor empleados'),
(2, 7, 10.2, DATE_SUB(NOW(), INTERVAL 7 HOUR), 'Sala de servidores'),
(3, 8, 20.6, DATE_SUB(NOW(), INTERVAL 8 HOUR), 'Línea producción 1'),
(4, 9, 24.4, DATE_SUB(NOW(), INTERVAL 9 HOUR), 'Taller mecánico'),
(5, 10, 16.1, DATE_SUB(NOW(), INTERVAL 10 HOUR), 'Área descanso');
