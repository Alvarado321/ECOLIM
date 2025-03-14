<?php
require_once __DIR__ . "/../models/Residuo.php";
require_once __DIR__ . "/../utils/Response.php";

class ResiduoController {
    private $residuo;

    public function __construct() {
        $this->residuo = new Residuo();
    }

    public function getAll() {
        $stmt = $this->residuo->getAll();
        $residuos = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        if($residuos) {
            Response::success($residuos);
        } else {
            Response::error("No se encontraron residuos", 404);
        }
    }

    public function getById($id) {
        $this->residuo->idResiduo = $id;
        $stmt = $this->residuo->getById();
        $residuo = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if($residuo) {
            Response::success($residuo);
        } else {
            Response::error("Residuo no encontrado", 404);
        }
    }

    public function create() {
        $data = json_decode(file_get_contents("php://input"));
        
        if(!empty($data->nombre)) {
            $this->residuo->nombre = $data->nombre;
            $this->residuo->descripcion = $data->descripcion ?? "";
            
            if($this->residuo->create()) {
                Response::success([], "Residuo creado exitosamente");
            } else {
                Response::error("No se pudo crear el residuo");
            }
        } else {
            Response::error("Datos incompletos");
        }
    }

    public function update($id) {
        $data = json_decode(file_get_contents("php://input"));
        
        if(!empty($data->nombre)) {
            $this->residuo->idResiduo = $id;
            $this->residuo->nombre = $data->nombre;
            $this->residuo->descripcion = $data->descripcion ?? "";
            
            if($this->residuo->update()) {
                Response::success([], "Residuo actualizado exitosamente");
            } else {
                Response::error("No se pudo actualizar el residuo");
            }
        } else {
            Response::error("Datos incompletos");
        }
    }

    public function delete($id) {
        $this->residuo->idResiduo = $id;
        
        if($this->residuo->delete()) {
            Response::success([], "Residuo eliminado exitosamente");
        } else {
            Response::error("No se pudo eliminar el residuo");
        }
    }

    public function search($keyword) {
        if(!empty($keyword)) {
            $stmt = $this->residuo->search($keyword);
            $residuos = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            if($residuos) {
                Response::success($residuos);
            } else {
                Response::error("No se encontraron residuos", 404);
            }
        } else {
            Response::error("Término de búsqueda requerido");
        }
    }

    public function initializeData() {
        if($this->residuo->insertInitialData()) {
            Response::success([], "Datos iniciales insertados exitosamente");
        } else {
            Response::error("No se pudieron insertar los datos iniciales");
        }
    }
}
