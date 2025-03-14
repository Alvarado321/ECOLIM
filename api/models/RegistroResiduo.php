<?php
require_once __DIR__ . "/../config/database.php";

class RegistroResiduo {
    private $conn;
    private $table = "registro_residuos";

    public $idRegistro;
    public $idEmpleado;
    public $idResiduo;
    public $cantidad;
    public $fechaRegistro;
    public $observaciones;

    public function __construct() {
        $database = new Database();
        $this->conn = $database->getConnection();
    }

    public function getAll() {
        $query = "SELECT r.*, e.nombre as nombre_empleado, rs.nombre as nombre_residuo 
                FROM " . $this->table . " r
                LEFT JOIN empleados e ON r.idEmpleado = e.idEmpleado
                LEFT JOIN residuos rs ON r.idResiduo = rs.idResiduo
                ORDER BY r.fechaRegistro DESC";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    public function getById() {
        $query = "SELECT r.*, e.nombre as nombre_empleado, rs.nombre as nombre_residuo 
                FROM " . $this->table . " r
                LEFT JOIN empleados e ON r.idEmpleado = e.idEmpleado
                LEFT JOIN residuos rs ON r.idResiduo = rs.idResiduo
                WHERE r.idRegistro = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->idRegistro);
        $stmt->execute();
        return $stmt;
    }

    public function create() {
        $query = "INSERT INTO " . $this->table . " 
                SET idEmpleado=:idEmpleado, idResiduo=:idResiduo, 
                    cantidad=:cantidad, observaciones=:observaciones";
        $stmt = $this->conn->prepare($query);

        // Sanitize
        $this->idEmpleado = htmlspecialchars(strip_tags($this->idEmpleado));
        $this->idResiduo = htmlspecialchars(strip_tags($this->idResiduo));
        $this->cantidad = htmlspecialchars(strip_tags($this->cantidad));
        $this->observaciones = htmlspecialchars(strip_tags($this->observaciones));

        // Bind values
        $stmt->bindParam(":idEmpleado", $this->idEmpleado);
        $stmt->bindParam(":idResiduo", $this->idResiduo);
        $stmt->bindParam(":cantidad", $this->cantidad);
        $stmt->bindParam(":observaciones", $this->observaciones);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function update() {
        $query = "UPDATE " . $this->table . " 
                SET idEmpleado=:idEmpleado, idResiduo=:idResiduo, 
                    cantidad=:cantidad, observaciones=:observaciones 
                WHERE idRegistro=:idRegistro";

        $stmt = $this->conn->prepare($query);

        // Sanitize
        $this->idRegistro = htmlspecialchars(strip_tags($this->idRegistro));
        $this->idEmpleado = htmlspecialchars(strip_tags($this->idEmpleado));
        $this->idResiduo = htmlspecialchars(strip_tags($this->idResiduo));
        $this->cantidad = htmlspecialchars(strip_tags($this->cantidad));
        $this->observaciones = htmlspecialchars(strip_tags($this->observaciones));

        // Bind values
        $stmt->bindParam(":idRegistro", $this->idRegistro);
        $stmt->bindParam(":idEmpleado", $this->idEmpleado);
        $stmt->bindParam(":idResiduo", $this->idResiduo);
        $stmt->bindParam(":cantidad", $this->cantidad);
        $stmt->bindParam(":observaciones", $this->observaciones);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function delete() {
        $query = "DELETE FROM " . $this->table . " WHERE idRegistro = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->idRegistro);
        
        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function getByEmpleado($idEmpleado) {
        $query = "SELECT r.*, e.nombre as nombre_empleado, rs.nombre as nombre_residuo 
                FROM " . $this->table . " r
                LEFT JOIN empleados e ON r.idEmpleado = e.idEmpleado
                LEFT JOIN residuos rs ON r.idResiduo = rs.idResiduo
                WHERE r.idEmpleado = ?
                ORDER BY r.fechaRegistro DESC";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $idEmpleado);
        $stmt->execute();
        return $stmt;
    }

    public function getByResiduo($idResiduo) {
        $query = "SELECT r.*, e.nombre as nombre_empleado, rs.nombre as nombre_residuo 
                FROM " . $this->table . " r
                LEFT JOIN empleados e ON r.idEmpleado = e.idEmpleado
                LEFT JOIN residuos rs ON r.idResiduo = rs.idResiduo
                WHERE r.idResiduo = ?
                ORDER BY r.fechaRegistro DESC";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $idResiduo);
        $stmt->execute();
        return $stmt;
    }

    public function getByDateRange($startDate, $endDate) {
        $query = "SELECT r.*, e.nombre as nombre_empleado, rs.nombre as nombre_residuo 
                FROM " . $this->table . " r
                LEFT JOIN empleados e ON r.idEmpleado = e.idEmpleado
                LEFT JOIN residuos rs ON r.idResiduo = rs.idResiduo
                WHERE DATE(r.fechaRegistro) BETWEEN ? AND ?
                ORDER BY r.fechaRegistro DESC";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $startDate);
        $stmt->bindParam(2, $endDate);
        $stmt->execute();
        return $stmt;
    }

    public function getTotalsByResiduo() {
        $query = "SELECT rs.nombre as residuo, SUM(r.cantidad) as total
                FROM " . $this->table . " r
                LEFT JOIN residuos rs ON r.idResiduo = rs.idResiduo
                GROUP BY r.idResiduo, rs.nombre
                ORDER BY total DESC";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;
    }
}
