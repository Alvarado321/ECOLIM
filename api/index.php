<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST, GET, PUT, DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

require_once 'config/database.php';
require_once 'controllers/EmpleadoController.php';
require_once 'controllers/ResiduoController.php';
require_once 'controllers/RegistroResiduoController.php';
require_once 'controllers/UsuarioController.php';
require_once 'utils/Response.php';

$database = new Database();
$db = $database->getConnection();
$response = new Response();

$empleadoController = new EmpleadoController($db);
$residuoController = new ResiduoController($db);
$registroController = new RegistroResiduoController($db);
$usuarioController = new UsuarioController($db);

$request_method = $_SERVER["REQUEST_METHOD"];
$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$path_parts = explode('/', trim($path, '/'));
$resource = isset($path_parts[2]) ? $path_parts[2] : '';
$id = isset($path_parts[3]) ? $path_parts[3] : null;

$data = json_decode(file_get_contents("php://input"));

try {
    switch($resource) {
        case 'usuarios':
            switch($request_method) {
                case 'GET':
                    if(isset($path_parts[3]) && $path_parts[3] === 'search') {
                        $keyword = isset($_GET['query']) ? $_GET['query'] : '';
                        echo $usuarioController->search($keyword);
                    } else {
                        echo $id ? $usuarioController->getOne($id) : $usuarioController->getAll();
                    }
                    break;
                case 'POST':
                    if(isset($path_parts[3]) && $path_parts[3] === 'login') {
                        echo $usuarioController->login($data);
                    } else {
                        echo $usuarioController->create($data);
                    }
                    break;
                case 'PUT':
                    echo $usuarioController->update($id, $data);
                    break;
                case 'DELETE':
                    echo $usuarioController->delete($id);
                    break;
                default:
                    echo $response->error("Método no permitido");
            }
            break;

        case 'empleados':
            switch($request_method) {
                case 'GET':
                    echo $id ? $empleadoController->getOne($id) : $empleadoController->getAll();
                    break;
                case 'POST':
                    echo $empleadoController->create($data);
                    break;
                case 'PUT':
                    echo $empleadoController->update($id, $data);
                    break;
                case 'DELETE':
                    echo $empleadoController->delete($id);
                    break;
                default:
                    echo $response->error("Método no permitido");
            }
            break;

        case 'residuos':
            switch($request_method) {
                case 'GET':
                    echo $id ? $residuoController->getOne($id) : $residuoController->getAll();
                    break;
                case 'POST':
                    echo $residuoController->create($data);
                    break;
                case 'PUT':
                    echo $residuoController->update($id, $data);
                    break;
                case 'DELETE':
                    echo $residuoController->delete($id);
                    break;
                default:
                    echo $response->error("Método no permitido");
            }
            break;

        case 'registros':
            switch($request_method) {
                case 'GET':
                    if(isset($path_parts[3]) && $path_parts[3] === 'empleado') {
                        $empleadoId = isset($path_parts[4]) ? $path_parts[4] : null;
                        echo $registroController->getByEmpleado($empleadoId);
                    } else {
                        echo $id ? $registroController->getOne($id) : $registroController->getAll();
                    }
                    break;
                case 'POST':
                    echo $registroController->create($data);
                    break;
                case 'PUT':
                    echo $registroController->update($id, $data);
                    break;
                case 'DELETE':
                    echo $registroController->delete($id);
                    break;
                default:
                    echo $response->error("Método no permitido");
            }
            break;

        default:
            echo $response->error("Recurso no encontrado");
    }
} catch(Exception $e) {
    echo $response->error($e->getMessage());
}
