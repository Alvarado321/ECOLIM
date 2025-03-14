<?php
require_once 'models/Usuario.php';
require_once 'utils/Response.php';

class UsuarioController {
    private $db;
    private $usuario;
    private $response;

    public function __construct($db) {
        $this->db = $db;
        $this->usuario = new Usuario($db);
        $this->response = new Response();
    }

    public function create($data) {
        if(!isset($data->nombre) || !isset($data->email) || !isset($data->password)) {
            return $this->response->error("Datos incompletos");
        }

        $this->usuario->nombre = $data->nombre;
        $this->usuario->email = $data->email;
        $this->usuario->password = $data->password;
        $this->usuario->rol = isset($data->rol) ? $data->rol : "Usuario";

        if($this->usuario->create()) {
            return $this->response->success("Usuario creado exitosamente");
        }
        return $this->response->error("No se pudo crear el usuario");
    }

    public function update($id, $data) {
        $this->usuario->idUsuario = $id;
        
        if(!$this->usuario->readOne()) {
            return $this->response->error("Usuario no encontrado");
        }

        $this->usuario->nombre = isset($data->nombre) ? $data->nombre : $this->usuario->nombre;
        $this->usuario->email = isset($data->email) ? $data->email : $this->usuario->email;
        $this->usuario->password = isset($data->password) ? $data->password : "";
        $this->usuario->rol = isset($data->rol) ? $data->rol : $this->usuario->rol;

        if($this->usuario->update()) {
            return $this->response->success("Usuario actualizado exitosamente");
        }
        return $this->response->error("No se pudo actualizar el usuario");
    }

    public function delete($id) {
        $this->usuario->idUsuario = $id;
        
        if($this->usuario->delete()) {
            return $this->response->success("Usuario eliminado exitosamente");
        }
        return $this->response->error("No se pudo eliminar el usuario");
    }

    public function getAll() {
        $stmt = $this->usuario->read();
        $usuarios = [];

        while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $usuario = array(
                "idUsuario" => $row['idUsuario'],
                "nombre" => $row['nombre'],
                "email" => $row['email'],
                "rol" => $row['rol']
            );
            array_push($usuarios, $usuario);
        }

        if(count($usuarios) > 0) {
            return $this->response->success("Usuarios encontrados", $usuarios);
        }
        return $this->response->error("No se encontraron usuarios");
    }

    public function getOne($id) {
        $this->usuario->idUsuario = $id;
        
        if($this->usuario->readOne()) {
            $usuario = array(
                "idUsuario" => $this->usuario->idUsuario,
                "nombre" => $this->usuario->nombre,
                "email" => $this->usuario->email,
                "rol" => $this->usuario->rol
            );
            return $this->response->success("Usuario encontrado", $usuario);
        }
        return $this->response->error("Usuario no encontrado");
    }

    public function login($data) {
        if(!isset($data->email) || !isset($data->password)) {
            return $this->response->error("Email y contraseña son requeridos");
        }

        $this->usuario->email = $data->email;
        $this->usuario->password = $data->password;

        if($this->usuario->authenticate()) {
            $usuario = array(
                "idUsuario" => $this->usuario->idUsuario,
                "nombre" => $this->usuario->nombre,
                "email" => $this->usuario->email,
                "rol" => $this->usuario->rol
            );
            return $this->response->success("Login exitoso", $usuario);
        }
        return $this->response->error("Credenciales inválidas");
    }
}
