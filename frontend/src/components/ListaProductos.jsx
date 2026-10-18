import { useEffect, useState } from "react";
import * as api from "../api";
import { dinero } from "../dinero";

export default function ListaProductos({ onAgregar, onError }) {
  const [productos, setProductos] = useState([]);
  const [cantidades, setCantidades] = useState({});     // { idProducto: cantidad escrita }

  // Al aparecer: GET /api/productos → guarda la lista en el estado
  useEffect(() => {
    api.listarProductos().then(setProductos).catch((e) => onError(e.message));
  }, []);

  return (
    <section className="cuadricula">
      {productos.map((p) => {
        const cantidad = cantidades[p.idProducto] ?? 1;
        return (
          <article className="tarjeta producto" key={p.idProducto}>
            <div className="foto"><img src={"/" + p.imagen} alt={p.nombre} /></div>
            <div className="ceja"><span className="tag">{p.codigo}</span></div>
            <h2>{p.nombre}</h2>
            <div className="precio">{dinero(p.precio)}</div>
            <div className="acciones">
              <input
                type="number" min="1" value={cantidad} aria-label="Cantidad"
                onChange={(e) => setCantidades({ ...cantidades, [p.idProducto]: e.target.value })}
              />
              <button className="prim" onClick={() => onAgregar(p, cantidad)}>Agregar</button>
            </div>
          </article>
        );
      })}
    </section>
  );
}
