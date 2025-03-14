<?php
require_once __DIR__ . "/../models/RegistroResiduo.php";
require_once __DIR__ . "/../utils/Response.php";

class RegistroResiduoController {
    private $registro;

    public function __construct() {
        $this->registro = new RegistroResiduo();
    }

    public function getAll() {
        $stmt = $this->registro->getAll();
        $registros = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        if($registros) {
            Response::success($registros);
        } else {
            Response::error("No se encontraron registros", 404);
        }
    }

    public function getById($id) {
        $this->registro->idRegistro = $id;
        $stmt = $this->registro->getById();
        $registro = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if($registro) {
            Response::success($registro);
        } else {
            Response::error("Registro no encontrado", 404);
        }
    }

    public function create() {
        $data = json_decode(file_get_contents("php://input"));
        
        if(!empty($data->idEmpleado) && !empty($data->idResiduo) && isset($data->cantidad)) {
            $this->registro->idEmpleado = $data->idEmpleado;
            $this->registro->idResiduo = $data->idResiduo;
            $this->registro->cantidad = $data->cantidad;
            $this->registro->observaciones = $data->observaciones ?? "";
            
            if($this->registro->create()) {
                Response::success([], "Registro creado exitosamente");
            } else {
                Response::error("No se pudo crear el registro");
            }
        } else {
            Response::error("Datos incompletos");
        }
    }

    public function update($id) {
        $data = json_decode(file_get_contents("php://input"));
        
        if(!empty($data->idEmpleado) && !empty($data->idResiduo) && isset($data->cantidad)) {
            $this->registro->idRegistro = $id;
            $this->registro->idEmpleado = $data->idEmpleado;
            $this->registro->idResiduo = $data->idResiduo;
            $this->registro->cantidad = $data->cantidad;
            $this->registro->observaciones = $data->observaciones ?? "";
            
            if($this->registro->update()) {
                Response::success([], "Registro actualizado exitosamente");
            } else {
                Response::error("No se pudo actualizar el registro");
            }
        } else {
            Response::error("Datos incompletos");
        }
    }

    public function delete($id) {
        $this->registro->idRegistro = $id;
        
        if($this->registro->delete()) {
            Response::success([], "Registro eliminado exitosamente");
        } else {
            Response::error("No se pudo eliminar el registro");
        }
    }

    public function getByEmpleado($idEmpleado) {
        $stmt = $this->registro->getByEmpleado($idEmpleado);
        $registros = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        if($registros) {
            Response::success($registros);
        } else {
            Response::error("No se encontraron registros para este empleado", 404);
        }
    }

    public function getByResiduo($idResiduo) {
        $stmt = $this->registro->getByResiduo($idResiduo);
        $registros = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        if($registros) {
            Response::success($registros);
        } else {
            Response::error("No se encontraron registros para este residuo", 404);
        }
    }

    public function getByDateRange() {
        $data = json_decode(file_get_contents("php://input"));
        
        if(!empty($data->startDate) && !empty($data->endDate)) {
            $stmt = $this->registro->getByDateRange($data->startDate, $data->endDate);
            $registros = $stmt->fetchAll(PDO::FETCH_ASSOC);
            
            if($registros) {
                Response::success($registros);
            } else {
                Response::error("No se encontraron registros en este rango de fechas", 404);
            }
        } else {
            Response::error("Rango de fechas requerido");
        }
    }

    public function getTotalsByResiduo() {
        $stmt = $this->registro->getTotalsByResiduo();
        $totales = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        if($totales) {
            Response::success($totales);
        } else {
            Response::error("No se encontraron totales", 404);
        }
    }
}
