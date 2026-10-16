package gt.carrito.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor
public class Producto {
    private Long idProducto;
    private String nombre, codigo, imagen;
    private BigDecimal precio;

    public void setPrecio(BigDecimal precio){
        if(precio.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0");
        }
        this.precio = precio;
    }
}
