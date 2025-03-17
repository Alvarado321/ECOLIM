<?php

class Usuario {
    private $conn;
    private $table = 'usuarios';

    public $idUsuario;
    public $nombre;
    public $email;
    public $password;
    public $rol;

    public function __construct($db) {
        $this->conn = $db;
    }

    public function create() {
        $query = "INSERT INTO " . $this->table . " SET nombre=:nombre, email=:email, password=:password, rol=:rol";
        $stmt = $this->conn->prepare($query);

        // Sanitize input
        $this->nombre = htmlspecialchars(strip_tags($this->nombre));
        $this->email = htmlspecialchars(strip_tags($this->email));
        $this->password = htmlspecialchars(strip_tags($this->password));
        $this->rol = htmlspecialchars(strip_tags($this->rol));

        // Hash password
        $this->password = password_hash($this->password, PASSWORD_DEFAULT);

        // Bind values
        $stmt->bindParam(":nombre", $this->nombre);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":password", $this->password);
        $stmt->bindParam(":rol", $this->rol);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function update() {
        $query = "UPDATE " . $this->table . " 
                SET nombre=:nombre, email=:email";
        
        if(!empty($this->password)) {
            $query .= ", password=:password";
        }
        
        $query .= ", rol=:rol WHERE idUsuario=:idUsuario";
        
        $stmt = $this->conn->prepare($query);

        // Sanitize input
        $this->nombre = htmlspecialchars(strip_tags($this->nombre));
        $this->email = htmlspecialchars(strip_tags($this->email));
        $this->rol = htmlspecialchars(strip_tags($this->rol));
        $this->idUsuario = htmlspecialchars(strip_tags($this->idUsuario));

        // Bind values
        $stmt->bindParam(":nombre", $this->nombre);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":rol", $this->rol);
        $stmt->bindParam(":idUsuario", $this->idUsuario);

        if(!empty($this->password)) {
            $this->password = htmlspecialchars(strip_tags($this->password));
            $this->password = password_hash($this->password, PASSWORD_DEFAULT);
            $stmt->bindParam(":password", $this->password);
        }

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function delete() {
        $query = "DELETE FROM " . $this->table . " WHERE idUsuario = ?";
        $stmt = $this->conn->prepare($query);
        
        $this->idUsuario = htmlspecialchars(strip_tags($this->idUsuario));
        $stmt->bindParam(1, $this->idUsuario);

        if($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function read() {
        $query = "SELECT * FROM " . $this->table;
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    public function readOne() {
        $query = "SELECT * FROM " . $this->table . " WHERE idUsuario = ?";
        $stmt = $this->conn->prepare($query);
        
        $stmt->bindParam(1, $this->idUsuario);
        $stmt->execute();

        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if($row) {
            $this->nombre = $row['nombre'];
            $this->email = $row['email'];
            $this->rol = $row['rol'];
            return true;
        }
        return false;
    }

    public function authenticate() {
        $query = "SELECT * FROM " . $this->table . " WHERE email = ?";
        $stmt = $this->conn->prepare($query);
        
        $stmt->bindParam(1, $this->email);
        $stmt->execute();

        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if($row && password_verify($this->password, $row['password'])) {
            $this->idUsuario = $row['idUsuario'];
            $this->nombre = $row['nombre'];
            $this->rol = $row['rol'];
            return true;
        }
        return false;
    }

    public function search($keyword) {
        $query = "SELECT * FROM " . $this->table . " WHERE nombre LIKE ? OR email LIKE ?";
        $stmt = $this->conn->prepare($query);
        
        $keyword = "%{$keyword}%";
        $stmt->bindParam(1, $keyword);
        $stmt->bindParam(2, $keyword);
        
        $stmt->execute();
        return $stmt;
    }
}
