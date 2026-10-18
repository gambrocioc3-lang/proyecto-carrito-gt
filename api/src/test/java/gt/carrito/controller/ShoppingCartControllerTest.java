package gt.carrito.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

import gt.carrito.entity.Producto;
import gt.carrito.entity.ShoppingCart;
import gt.carrito.exception.RecursoNoEncontradoException;
import gt.carrito.service.ShoppingCartService;

@RunWith(SpringRunner.class)
@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShoppingCartService shoppingCartService;

    private ShoppingCart lineaDeAna;

    @Before
    public void setUp(){
        Producto reloj = new Producto();
        reloj.setIdProducto(3L);
        reloj.setPrecio(new BigDecimal("2121.86"));
        lineaDeAna = new ShoppingCart();
        lineaDeAna.setIdShoppingCart(7L);
        lineaDeAna.setProducto(reloj);
        lineaDeAna.setCodUsuario("ana");
        lineaDeAna.setCantidad(2);
    }

    @Test
    public void listarTest() throws Exception {
        when(shoppingCartService.listar("ana")).thenReturn(List.of(lineaDeAna));

        mockMvc.perform(get("/api/carrito/ana"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cantidad").value(2))
                .andExpect(jsonPath("$[0].subTotal").value(4243.72));
    }

    @Test
    public void agregarTest() throws Exception {
        when(shoppingCartService.agregar("ana", 3L, 2)).thenReturn(lineaDeAna);

        mockMvc.perform(post("/api/carrito/ana/items").param("idProducto", "3").param("cantidad", "2"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idShoppingCart").value(7));
    }

    @Test
    public void agregarCantidadCeroTest() throws Exception {
        mockMvc.perform(post("/api/carrito/ana/items").param("idProducto", "3").param("cantidad", "0"))
                .andExpect(status().isBadRequest());

        verify(shoppingCartService, never()).agregar(anyString(), anyLong(), anyInt());
    }

    @Test
    public void agregarProductoInexistenteTest() throws Exception {
        when(shoppingCartService.agregar("ana", 99L, 1))
                .thenThrow(new RecursoNoEncontradoException("No existe el producto 99"));

        mockMvc.perform(post("/api/carrito/ana/items").param("idProducto", "99").param("cantidad", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void eliminarTest() throws Exception {
        mockMvc.perform(delete("/api/carrito/ana/items/7"))
                .andExpect(status().isNoContent());

        verify(shoppingCartService).eliminar("ana", 7L);
    }

    @Test
    public void vaciarTest() throws Exception {
        mockMvc.perform(delete("/api/carrito/ana"))
                .andExpect(status().isNoContent());

        verify(shoppingCartService).vaciar("ana");
    }

    @Test
    public void calcularTotalTest() throws Exception {
        when(shoppingCartService.calcularTotal("ana")).thenReturn(new BigDecimal("4914.93"));

        mockMvc.perform(get("/api/carrito/ana/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("4914.93"));
    }

}
