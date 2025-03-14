<?php
require_once __DIR__ . "/../models/Empleado.php";
require_once __DIR__ . "/../utils/Response.php";

class EmpleadoController {
    private $empleado;

    public function __construct() {
        $this->empleado = new Empleado();
    }

    public function getAll() {
        $stmt = $this->empleado->getAll();
        $empleados = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        if($empleados) {
            Response::success($empleados);
        } else {
            Response::error("No se encontraron empleados", 404);
        }
    }

    public function getById($id) {
        $this->empleado->idEmpleado = $id;
        $stmt = $this->empleado->getById();
        $empleado = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if($empleado) {
            Response::success($empleado);
        } else {
            Response::error("Empleado no encontrado", 404);
        }
    }

    public function create() {
        $data = json_decode(file_get_contents("php://input"));
        
        if(!empty($data->nombre) && !empty($data->email) && !empty($data->password)) {
            $this->empleado->nombre = $data->nombre;
            $this->empleado->email = $data->email;
            $this->empleado->password = $data->password;
            
            if($this->empleado->create()) {
                Response::success([], "Empleado creado exitosamente");
            } else {
                Response::error("No se pudo crear el empleado");
            }
        } else {
            Response::error("Datos incompletos");
        }
    }

    public function update($id) {
        $data = json_decode(file_get_contents("php://input"));
        
        if(!empty($data->nombre) && !empty($data->email)) {
            $this->empleado->idEmpleado = $id;
            $this->empleado->nombre = $data->nombre;
            $this->empleado->email = $data->email;
            if(isset($data->password) && !empty($data->password)) {
                $this->empleado->password = $data->password;
            }
            
            if($this->empleado->update()) {
                Response::success([], "Empleado actualizado exitosamente");
            } else {
                Response::error("No se pudo actualizar el empleado");
            }
        } else {
            Response::error("Datos incompletos");
        }
    }

    public function delete($id) {
        $this->empleado->idEmpleado = $id;
        
        if($this->empleado->delete()) {
            Response::success([], "Empleado eliminado exitosamente");
        } else {
            Response::error("No se pudo eliminar el empleado");
        }
    }

    public function login() {
        $data = json_decode(file_get_contents("php://input"));
        
        if(!empty($data->email) && !empty($data->password)) {
            $this->empleado->email = $data->email;
            $stmt = $this->empleado->login();
            $empleado = $stmt->fetch(PDO::FETCH_ASSOC);
            
            if($empleado && password_verify($data->password, $empleado['password'])) {
                unset($empleado['password']); // Remove password from response
                Response::success($empleado, "Login exitoso");
            } else {
                Response::error("Credenciales inválidas", 401);
            }
        } else {
            Response::error("Datos incompletos");
        }
    }

    public function search($keyword) {
        if(!empty($keyword)) {
            $stmt = $this->empleado->search($keyword);
            $empleados = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            if($empleados) {
                Response::success($empleados);
            } else {
                Response::error("No se encontraron empleados", 404);
            }
        } else {
            Response::error("Término de búsqueda requerido");
        }
    }
}
