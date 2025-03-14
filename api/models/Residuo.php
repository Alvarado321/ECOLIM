<?php
require_once __DIR__ . "/../config/database.php";

class Residuo {
    private $conn;
    private $table = "residuos";

    public $idResiduo;
    public $nombre;
    public $descripcion;

    public function __construct() {
        $database = new Database();
        $this->conn = $database->getConnection();
    }

    public function getAll() {
        $query = "SELECT * FROM " . $this->table;
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    public function getById() {
        $query = "SELECT * FROM " . $this->table . " WHERE idResiduo = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->idResiduo);
        $stmt->execute();
        return $stmt;
    }

    public function create() {
        $query = "INSERT INTO " . $this->table . " SET nombre=:nombre, descripcion=:descripcion";
        $stmt = $this->conn->prepare($query);

        // Sanitize
        $this->nombre = htmlspecialchars(strip_tags($this->nombre));
        $this->descripcion = htmlspecialchars(strip_tags($this->descripcion));

        // Bind values
        $stmt->bindParam(":nombre", $this->nombre);
        $stmt->bindParam(":descripcion", $this->descripcion);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function update() {
        $query = "UPDATE " . $this->table . " 
                SET nombre=:nombre, descripcion=:descripcion 
                WHERE idResiduo=:idResiduo";

        $stmt = $this->conn->prepare($query);

        // Sanitize
        $this->nombre = htmlspecialchars(strip_tags($this->nombre));
        $this->descripcion = htmlspecialchars(strip_tags($this->descripcion));
        $this->idResiduo = htmlspecialchars(strip_tags($this->idResiduo));

        // Bind values
        $stmt->bindParam(":nombre", $this->nombre);
        $stmt->bindParam(":descripcion", $this->descripcion);
        $stmt->bindParam(":idResiduo", $this->idResiduo);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function delete() {
        $query = "DELETE FROM " . $this->table . " WHERE idResiduo = ?";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->idResiduo);
        
        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function search($keyword) {
        $query = "SELECT * FROM " . $this->table . 
                " WHERE nombre LIKE ? OR descripcion LIKE ?";
        $keyword = "%{$keyword}%";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $keyword);
        $stmt->bindParam(2, $keyword);
        $stmt->execute();
        return $stmt;
    }

    public function insertInitialData() {
        $initialData = [
            ['Plástico', 'Residuos plásticos reciclables'],
            ['Vidrio', 'Residuos de vidrio reciclables'],
            ['Metal', 'Residuos metálicos reciclables'],
            ['Orgánico', 'Residuos orgánicos biodegradables'],
            ['Papel', 'Residuos de papel reciclable'],
            ['Electrónico', 'Residuos electrónicos reciclables']
        ];

        $query = "INSERT INTO " . $this->table . " (nombre, descripcion) VALUES (:nombre, :descripcion)";
        $stmt = $this->conn->prepare($query);

        foreach($initialData as $data) {
            $stmt->bindParam(":nombre", $data[0]);
            $stmt->bindParam(":descripcion", $data[1]);
            $stmt->execute();
        }

        return true;
    }
}
