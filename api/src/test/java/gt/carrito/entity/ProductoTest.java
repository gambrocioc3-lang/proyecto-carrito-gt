package gt.carrito.entity;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

public class ProductoTest {

    private Producto producto;
    
    @Before 
    public void setUp(){
        producto = new Producto();
    }

    @Test 
    public void colocarPrecioTest(){
        producto.setPrecio(new BigDecimal("100.34"));
        assertEquals(0, producto.getPrecio().compareTo(new BigDecimal("100.34")));
    }

    @Test (expected = IllegalArgumentException.class)
    public void precioInvalidoTest(){
        producto.setPrecio(new BigDecimal("-87"));
    }

}
