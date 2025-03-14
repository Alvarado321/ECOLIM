<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET,POST,PUT,DELETE");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

require_once __DIR__ . "/controllers/EmpleadoController.php";
require_once __DIR__ . "/controllers/ResiduoController.php";
require_once __DIR__ . "/controllers/RegistroResiduoController.php";
require_once __DIR__ . "/utils/Response.php";

// Get the request method and URI
$method = $_SERVER['REQUEST_METHOD'];
$request_uri = $_SERVER['REQUEST_URI'];

// Remove base path from URI to get the route
$base_path = '/ecolim/api';
$route = str_replace($base_path, '', $request_uri);
$route = strtok($route, '?'); // Remove query parameters

// Parse the route into segments
$segments = explode('/', trim($route, '/'));
$resource = $segments[0] ?? '';
$id = $segments[1] ?? null;
$action = $segments[2] ?? null;

// Initialize controllers
$empleadoController = new EmpleadoController();
$residuoController = new ResiduoController();
$registroController = new RegistroResiduoController();

// Route the request
switch($resource) {
    case 'empleados':
        switch($method) {
            case 'GET':
                if($id) {
                    $empleadoController->getById($id);
                } elseif(isset($_GET['search'])) {
                    $empleadoController->search($_GET['search']);
                } else {
                    $empleadoController->getAll();
                }
                break;
            case 'POST':
                if($action === 'login') {
                    $empleadoController->login();
                } else {
                    $empleadoController->create();
                }
                break;
            case 'PUT':
                if($id) {
                    $empleadoController->update($id);
                } else {
                    Response::error("ID no proporcionado", 400);
                }
                break;
            case 'DELETE':
                if($id) {
                    $empleadoController->delete($id);
                } else {
                    Response::error("ID no proporcionado", 400);
                }
                break;
            default:
                Response::error("Método no permitido", 405);
        }
        break;

    case 'residuos':
        switch($method) {
            case 'GET':
                if($id) {
                    $residuoController->getById($id);
                } elseif(isset($_GET['search'])) {
                    $residuoController->search($_GET['search']);
                } else {
                    $residuoController->getAll();
                }
                break;
            case 'POST':
                if($action === 'initialize') {
                    $residuoController->initializeData();
                } else {
                    $residuoController->create();
                }
                break;
            case 'PUT':
                if($id) {
                    $residuoController->update($id);
                } else {
                    Response::error("ID no proporcionado", 400);
                }
                break;
            case 'DELETE':
                if($id) {
                    $residuoController->delete($id);
                } else {
                    Response::error("ID no proporcionado", 400);
                }
                break;
            default:
                Response::error("Método no permitido", 405);
        }
        break;

    case 'registros':
        switch($method) {
            case 'GET':
                if($id) {
                    $registroController->getById($id);
                } elseif($action === 'totales') {
                    $registroController->getTotalsByResiduo();
                } elseif(isset($_GET['empleado'])) {
                    $registroController->getByEmpleado($_GET['empleado']);
                } elseif(isset($_GET['residuo'])) {
                    $registroController->getByResiduo($_GET['residuo']);
                } else {
                    $registroController->getAll();
                }
                break;
            case 'POST':
                if($action === 'fecha') {
                    $registroController->getByDateRange();
                } else {
                    $registroController->create();
                }
                break;
            case 'PUT':
                if($id) {
                    $registroController->update($id);
                } else {
                    Response::error("ID no proporcionado", 400);
                }
                break;
            case 'DELETE':
                if($id) {
                    $registroController->delete($id);
                } else {
                    Response::error("ID no proporcionado", 400);
                }
                break;
            default:
                Response::error("Método no permitido", 405);
        }
        break;
    
    default:
        Response::error("Recurso no encontrado", 404);
}
