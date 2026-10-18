package gt.carrito.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "producto")
@Getter 
@Setter 
@ToString
@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
