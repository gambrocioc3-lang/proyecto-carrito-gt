package gt.carrito.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import gt.carrito.entity.Producto;
import gt.carrito.entity.ShoppingCart;
import gt.carrito.exception.RecursoNoEncontradoException;
import gt.carrito.repository.ProductoRepository;
import gt.carrito.repository.ShoppingCartRepository;

// Los repositorios son falsos (Mockito): las pruebas no usan MySQL
@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ProductoRepository productoRepository;

    private ShoppingCartService shoppingCartService;
    private Producto reloj, camara;

    @Before
    public void setUp(){
        shoppingCartService = new ShoppingCartService(shoppingCartRepository, productoRepository);
        reloj = producto(3L, "2121.86");
        camara = producto(1L, "671.21");
    }

    private Producto producto(Long id, String precio){
        Producto p = new Producto();
        p.setIdProducto(id);
        p.setPrecio(new BigDecimal(precio));
        return p;
    }

    private ShoppingCart linea(Long id, Producto producto, String codUsuario, int cantidad){
        ShoppingCart sc = new ShoppingCart();
        sc.setIdShoppingCart(id);
        sc.setProducto(producto);
        sc.setCodUsuario(codUsuario);
        sc.setCantidad(cantidad);
        return sc;
    }

    @Test
    public void agregarProductoNuevoTest(){
        when(productoRepository.findById(3L)).thenReturn(Optional.of(reloj));
        when(shoppingCartRepository.findByCodUsuarioAndProductoIdProducto("ana", 3L)).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenAnswer(i -> i.getArgument(0));

        ShoppingCart resultado = shoppingCartService.agregar("ana", 3L, 2);

        assertEquals(2, resultado.getCantidad());
        assertEquals("ana", resultado.getCodUsuario());
        assertSame(reloj, resultado.getProducto());
    }

    @Test
    public void agregarProductoQueYaEstaSumaTest(){
        when(productoRepository.findById(3L)).thenReturn(Optional.of(reloj));
        when(shoppingCartRepository.findByCodUsuarioAndProductoIdProducto("ana", 3L))
                .thenReturn(Optional.of(linea(7L, reloj, "ana", 2)));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenAnswer(i -> i.getArgument(0));

        ShoppingCart resultado = shoppingCartService.agregar("ana", 3L, 3);

        assertEquals(5, resultado.getCantidad());
        assertEquals(Long.valueOf(7L), resultado.getIdShoppingCart());
    }

    @Test (expected = RecursoNoEncontradoException.class)
    public void agregarProductoInexistenteTest(){
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        shoppingCartService.agregar("ana", 99L, 1);
    }

    @Test
    public void eliminarTest(){
        ShoppingCart lineaDeAna = linea(7L, reloj, "ana", 2);
        when(shoppingCartRepository.findByIdShoppingCartAndCodUsuario(7L, "ana")).thenReturn(Optional.of(lineaDeAna));

        shoppingCartService.eliminar("ana", 7L);

        verify(shoppingCartRepository).delete(lineaDeAna);
    }

    @Test
    public void eliminarLineaDeOtroUsuarioTest(){
        when(shoppingCartRepository.findByIdShoppingCartAndCodUsuario(7L, "luis")).thenReturn(Optional.empty());

        try {
            shoppingCartService.eliminar("luis", 7L);
            fail("Debió lanzar RecursoNoEncontradoException");
        } catch (RecursoNoEncontradoException e) {
            verify(shoppingCartRepository, never()).delete(any(ShoppingCart.class));
        }
    }

    @Test
    public void calcularTotalTest(){
        when(shoppingCartRepository.findByCodUsuario("ana"))
                .thenReturn(List.of(linea(7L, reloj, "ana", 2), linea(8L, camara, "ana", 1)));

        BigDecimal total = shoppingCartService.calcularTotal("ana");

        assertEquals(0, total.compareTo(new BigDecimal("4914.93")));
    }

}
