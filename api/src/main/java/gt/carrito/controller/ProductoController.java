package gt.carrito.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gt.carrito.entity.Producto;
import gt.carrito.service.ProductoService;
import lombok.ToString;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:5173")    // CORS: solo el frontend de React puede leer las respuestas
@ToString
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }

    // GET /api/productos → 200 con la lista en JSON
    @GetMapping
    public List<Producto> listar(){
        return productoService.listar();
    }
}
