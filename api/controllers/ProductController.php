<?php
require_once __DIR__ . "/../models/Product.php";
require_once __DIR__ . "/../utils/Response.php";

class ProductController {
    private $product;

    public function __construct() {
        $this->product = new Product();
    }

    public function getAll() {
        $stmt = $this->product->getAll();
        $products = $stmt->fetchAll(PDO::FETCH_ASSOC);
        
        if($products) {
            Response::success($products);
        } else {
            Response::error("No se encontraron productos", 404);
        }
    }

    public function getById($id) {
        $this->product->id = $id;
        $stmt = $this->product->getById();
        $product = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if($product) {
            Response::success($product);
        } else {
            Response::error("Producto no encontrado", 404);
        }
    }

    public function create() {
        $data = json_decode(file_get_contents("php://input"));
        
        if(!empty($data->name) && !empty($data->price)) {
            $this->product->name = $data->name;
            $this->product->description = $data->description ?? "";
            $this->product->price = $data->price;
            $this->product->category_id = $data->category_id ?? null;
            
            if($this->product->create()) {
                Response::success([], "Producto creado exitosamente");
            } else {
                Response::error("No se pudo crear el producto");
            }
        } else {
            Response::error("Datos incompletos");
        }
    }

    public function update($id) {
        $data = json_decode(file_get_contents("php://input"));
        
        if(!empty($data->name) && !empty($data->price)) {
            $this->product->id = $id;
            $this->product->name = $data->name;
            $this->product->description = $data->description ?? "";
            $this->product->price = $data->price;
            $this->product->category_id = $data->category_id ?? null;
            
            if($this->product->update()) {
                Response::success([], "Producto actualizado exitosamente");
            } else {
                Response::error("No se pudo actualizar el producto");
            }
        } else {
            Response::error("Datos incompletos");
        }
    }

    public function delete($id) {
        $this->product->id = $id;
        
        if($this->product->delete()) {
            Response::success([], "Producto eliminado exitosamente");
        } else {
            Response::error("No se pudo eliminar el producto");
        }
    }
}
