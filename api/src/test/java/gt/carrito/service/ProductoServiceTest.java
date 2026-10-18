package gt.carrito.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import gt.carrito.entity.Producto;
import gt.carrito.repository.ProductoRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    private ProductoService productoService;

    @Before
    public void setUp(){
        productoService = new ProductoService(productoRepository);
    }

    @Test
    public void listarTest(){
        List<Producto> productos = List.of(new Producto(), new Producto());
        when(productoRepository.findAll()).thenReturn(productos);

        assertEquals(2, productoService.listar().size());
    }

}
