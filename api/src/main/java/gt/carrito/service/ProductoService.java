package gt.carrito.service;

import java.util.List;

import org.springframework.stereotype.Service;

import gt.carrito.entity.Producto;
import gt.carrito.repository.ProductoRepository;
import lombok.ToString;

@Service
@ToString
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Spring le pasa el repositorio al crear el service (inyección por constructor)
    public ProductoService(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    public List<Producto> listar(){
        return productoRepository.findAll();
    }
}
