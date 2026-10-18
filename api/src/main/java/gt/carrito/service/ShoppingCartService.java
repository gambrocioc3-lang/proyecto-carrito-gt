package gt.carrito.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import gt.carrito.entity.Producto;
import gt.carrito.entity.ShoppingCart;
import gt.carrito.exception.RecursoNoEncontradoException;
import gt.carrito.repository.ProductoRepository;
import gt.carrito.repository.ShoppingCartRepository;
import lombok.ToString;

@Service
@ToString
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductoRepository productoRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductoRepository productoRepository){
        this.shoppingCartRepository = shoppingCartRepository;
        this.productoRepository = productoRepository;
    }

    public List<ShoppingCart> listar(String codUsuario){
        return shoppingCartRepository.findByCodUsuario(codUsuario);
    }

    // Si el usuario ya tiene ese producto, le suma la cantidad; si no, crea una línea nueva
    public ShoppingCart agregar(String codUsuario, Long idProducto, int cantidad){
        Optional<Producto> producto = productoRepository.findById(idProducto);
        if(producto.isEmpty()){
            throw new RecursoNoEncontradoException("No existe el producto " + idProducto);
        }

        Optional<ShoppingCart> existente =
                shoppingCartRepository.findByCodUsuarioAndProductoIdProducto(codUsuario, idProducto);

        ShoppingCart item;
        if(existente.isPresent()){
            item = existente.get();
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            item = new ShoppingCart();
            item.setProducto(producto.get());
            item.setCodUsuario(codUsuario);
            item.setCantidad(cantidad);
        }
        // save: si la línea ya tiene id hace UPDATE; si no, INSERT
        return shoppingCartRepository.save(item);
    }

    // Solo borra la línea si es de ese usuario
    public void eliminar(String codUsuario, Long idShoppingCart){
        Optional<ShoppingCart> item =
                shoppingCartRepository.findByIdShoppingCartAndCodUsuario(idShoppingCart, codUsuario);
        if(item.isEmpty()){
            throw new RecursoNoEncontradoException("No existe la línea " + idShoppingCart + " en tu carrito");
        }
        shoppingCartRepository.delete(item.get());
    }

    public void vaciar(String codUsuario){
        shoppingCartRepository.deleteByCodUsuario(codUsuario);
    }

    public BigDecimal calcularTotal(String codUsuario){
        BigDecimal total = BigDecimal.ZERO;
        for(ShoppingCart item : listar(codUsuario)){
            total = total.add(item.getSubTotal());
        }
        return total;
    }
}
