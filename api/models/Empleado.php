<?php
require_once __DIR__ . "/../config/database.php";

class Empleado {
    private $conn;
    private $table = "empleados";

    public $idEmpleado;
    public $nombre;
    public $email;
    public $password;

    public function __construct() {
        $database = new Database();
        $this->conn = $database->getConnection();
    }

    public function getAll() {
        $query = "SELECT idEmpleado, nombre, email FROM " . $this->table;
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    public function getById() {
        $query = "SELECT idEmpleado, nombre, email FROM " . $this->table . " WHERE idEmpleado = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->idEmpleado);
        $stmt->execute();
        return $stmt;
    }

    public function create() {
        $query = "INSERT INTO " . $this->table . " SET nombre=:nombre, email=:email, password=:password";
        $stmt = $this->conn->prepare($query);

        // Sanitize
        $this->nombre = htmlspecialchars(strip_tags($this->nombre));
        $this->email = htmlspecialchars(strip_tags($this->email));
        $this->password = password_hash($this->password, PASSWORD_DEFAULT); // Hash the password

        // Bind values
        $stmt->bindParam(":nombre", $this->nombre);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":password", $this->password);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function update() {
        $query = "UPDATE " . $this->table . " 
                SET nombre=:nombre, email=:email" .
                ($this->password ? ", password=:password" : "") .
                " WHERE idEmpleado=:idEmpleado";

        $stmt = $this->conn->prepare($query);

        // Sanitize
        $this->nombre = htmlspecialchars(strip_tags($this->nombre));
        $this->email = htmlspecialchars(strip_tags($this->email));
        $this->idEmpleado = htmlspecialchars(strip_tags($this->idEmpleado));

        // Bind values
        $stmt->bindParam(":nombre", $this->nombre);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":idEmpleado", $this->idEmpleado);

        // Only bind password if it's being updated
        if($this->password) {
            $this->password = password_hash($this->password, PASSWORD_DEFAULT);
            $stmt->bindParam(":password", $this->password);
        }

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function delete() {
        $query = "DELETE FROM " . $this->table . " WHERE idEmpleado = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->idEmpleado);
        
        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function login() {
        $query = "SELECT idEmpleado, nombre, email, password FROM " . $this->table . " WHERE email = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->email);
        $stmt->execute();
        return $stmt;
    }

    public function search($keyword) {
        $query = "SELECT idEmpleado, nombre, email FROM " . $this->table . 
                " WHERE nombre LIKE ? OR email LIKE ?";
        $keyword = "%{$keyword}%";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $keyword);
        $stmt->bindParam(2, $keyword);
        $stmt->execute();
        return $stmt;
    }
}
