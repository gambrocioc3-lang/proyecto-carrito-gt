package gt.carrito.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gt.carrito.entity.ShoppingCart;
import gt.carrito.service.ShoppingCartService;
import jakarta.validation.constraints.Min;
import lombok.ToString;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:5173")
@ToString
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService){
        this.shoppingCartService = shoppingCartService;
    }

    // GET /api/carrito/ana → 200 con las líneas del carrito (cada una con su subTotal)
    @GetMapping("/{codUsuario}")
    public List<ShoppingCart> listar(@PathVariable String codUsuario){
        return shoppingCartService.listar(codUsuario);
    }

    // POST /api/carrito/ana/items?idProducto=3&cantidad=2 → 201 con la línea creada o sumada
    // cantidad < 1 → 400 (lo revisa @Min antes de llegar al service)
    @PostMapping("/{codUsuario}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCart agregar(@PathVariable String codUsuario,
                                @RequestParam Long idProducto,
                                @RequestParam @Min(1) int cantidad){
        return shoppingCartService.agregar(codUsuario, idProducto, cantidad);
    }

    // DELETE /api/carrito/ana/items/7 → 204 (404 si la línea no existe o no es de ana)
    @DeleteMapping("/{codUsuario}/items/{idShoppingCart}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String codUsuario, @PathVariable Long idShoppingCart){
        shoppingCartService.eliminar(codUsuario, idShoppingCart);
    }

    // DELETE /api/carrito/ana → 204
    @DeleteMapping("/{codUsuario}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vaciar(@PathVariable String codUsuario){
        shoppingCartService.vaciar(codUsuario);
    }

    // GET /api/carrito/ana/total → 200 con el total (un número en JSON)
    @GetMapping("/{codUsuario}/total")
    public BigDecimal calcularTotal(@PathVariable String codUsuario){
        return shoppingCartService.calcularTotal(codUsuario);
    }
}
