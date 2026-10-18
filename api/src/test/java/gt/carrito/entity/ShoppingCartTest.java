package gt.carrito.entity;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

public class ShoppingCartTest {

    private ShoppingCart shoppingCart;

    @Before 
    public void setUp(){
        Producto producto = new Producto();
        producto.setPrecio(new BigDecimal("100.50"));
        shoppingCart = new ShoppingCart();
        shoppingCart.setProducto(producto);
    }

    @Test 
    public void colocarCantidadTest(){
        shoppingCart.setCantidad(3);
        assertEquals(3, shoppingCart.getCantidad());
    }

    @Test (expected = IllegalArgumentException.class)
    public void cantidadInvalidaTest(){
        shoppingCart.setCantidad(0);
    }

    @Test 
    public void subTotalTest(){
        shoppingCart.setCantidad(3);
        assertEquals(0, shoppingCart.getSubTotal().compareTo(new BigDecimal("301.50")));
    }

}
