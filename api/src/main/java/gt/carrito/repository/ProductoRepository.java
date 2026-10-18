package gt.carrito.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gt.carrito.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
