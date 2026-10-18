package gt.carrito.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "shopping_cart")
@Getter 
@Setter 
@ToString
@NoArgsConstructor
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idShoppingCart;

    // Muchas líneas del carrito pueden tener el mismo producto; la columna idProducto es la FK
    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    private String codUsuario;
    private int cantidad;

    public void setCantidad(int cantidad){
        if(cantidad < 1){
            throw new IllegalArgumentException("La cantidad debe ser al menos 1");
        }
        this.cantidad = cantidad;
    }

    // No es una columna: se calcula (precio x cantidad) y sale en el JSON como "subTotal"
    public BigDecimal getSubTotal(){
        return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }
}
