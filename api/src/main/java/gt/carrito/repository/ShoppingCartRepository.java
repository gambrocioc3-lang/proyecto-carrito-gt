package gt.carrito.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import gt.carrito.entity.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    // Spring arma el SQL a partir del nombre del método
    List<ShoppingCart> findByCodUsuario(String codUsuario);

    Optional<ShoppingCart> findByCodUsuarioAndProductoIdProducto(String codUsuario, Long idProducto);

    Optional<ShoppingCart> findByIdShoppingCartAndCodUsuario(Long idShoppingCart, String codUsuario);

    @Transactional
    void deleteByCodUsuario(String codUsuario);
}
