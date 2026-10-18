import { useState } from "react";
import { dinero } from "../dinero";

export default function Carrito({ lineas, total, onEliminar, onVaciar }) {
  const [confirmar, setConfirmar] = useState(false);

  if (lineas.length === 0) {
    return (
      <section className="tarjeta vacio">
        <b>Tu carrito de compra está vacío</b>
        <span>Agrega productos desde la pestaña «Productos».</span>
      </section>
    );
  }

  return (
    <section className="carrito">
      <div className="tarjeta tabla">
        <table>
          <thead>
            <tr><th></th><th>Producto</th><th>Código</th><th className="num">Cantidad</th>
                <th className="num">Precio</th><th className="num">Subtotal</th><th></th></tr>
          </thead>
          <tbody>
            {lineas.map((l) => (
              <tr key={l.idShoppingCart}>
                <td><img src={"/" + l.producto.imagen} alt="" /></td>
                <td>{l.producto.nombre}</td>
                <td><span className="tag">{l.producto.codigo}</span></td>
                <td className="num">{l.cantidad}</td>
                <td className="num">{dinero(l.producto.precio)}</td>
                <td className="num"><b>{dinero(l.subTotal)}</b></td>
                <td><button className="quitar" onClick={() => onEliminar(l)}>Eliminar</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {confirmar && (
        <div className="confirma">
          <span>¿Vaciar el carrito? Se quitan todos los productos.</span>
          <div className="acc">
            <button className="si" onClick={() => { setConfirmar(false); onVaciar(); }}>Sí, vaciarlo</button>
            <button onClick={() => setConfirmar(false)}>Cancelar</button>
          </div>
        </div>
      )}

      <footer className="pie">
        <button onClick={() => setConfirmar(true)}>Vaciar carrito</button>
        <div className="total"><span>Total</span><b>{dinero(total)}</b></div>
      </footer>
    </section>
  );
}
