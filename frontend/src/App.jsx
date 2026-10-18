import { useEffect, useState } from "react";
import * as api from "./api";
import { codigoUsuario } from "./usuario";
import ListaProductos from "./components/ListaProductos";
import Carrito from "./components/Carrito";

const cod = codigoUsuario();

export default function App() {
  const [pestana, setPestana] = useState("productos");
  const [carrito, setCarrito] = useState([]);
  const [total, setTotal] = useState(0);
  const [aviso, setAviso] = useState(null);        // { tipo: "error" | "ok", texto }

  // Pide el carrito y el total a la API y los guarda en el estado (la pantalla se redibuja sola)
  async function recargarCarrito() {
    try {
      setCarrito(await api.listarCarrito(cod));
      setTotal(await api.totalCarrito(cod));
    } catch (e) {
      setAviso({ tipo: "error", texto: e.message });
    }
  }

  useEffect(() => { recargarCarrito(); }, []);

  async function agregar(producto, cantidad) {
    try {
      const linea = await api.agregar(cod, producto.idProducto, cantidad);
      setAviso({ tipo: "ok", texto: `${producto.nombre}: ahora tienes ${linea.cantidad} en el carrito.` });
      await recargarCarrito();
    } catch (e) {
      setAviso({ tipo: "error", texto: e.message });
    }
  }

  async function eliminar(linea) {
    try {
      await api.eliminar(cod, linea.idShoppingCart);
      await recargarCarrito();
    } catch (e) {
      setAviso({ tipo: "error", texto: e.message });
    }
  }

  async function vaciar() {
    try {
      await api.vaciar(cod);
      await recargarCarrito();
    } catch (e) {
      setAviso({ tipo: "error", texto: e.message });
    }
  }

  const unidades = carrito.reduce((suma, l) => suma + l.cantidad, 0);

  return (
    <div className="app">
      <header className="cabecera">
        <div className="marca">
          <span className="logo">CG</span>
          <div>
            <b>CarritoGT</b>
            <span className="sub">Carrito de compra · API REST con Spring Boot</span>
          </div>
        </div>
        <span className="ctx" title="Tu código de usuario (se guarda en este navegador)">
          <i /> {cod.slice(0, 8)}
        </span>
      </header>

      <nav className="tabs">
        <button className={"tab" + (pestana === "productos" ? " activa" : "")} onClick={() => setPestana("productos")}>
          Productos
        </button>
        <button className={"tab" + (pestana === "carrito" ? " activa" : "")} onClick={() => setPestana("carrito")}>
          Carrito {unidades > 0 && <span className="pill">{unidades}</span>}
        </button>
      </nav>

      {aviso && (
        <div className={"aviso " + aviso.tipo} role="alert">
          <span>{aviso.texto}</span>
          <button className="cerrar" onClick={() => setAviso(null)} aria-label="Cerrar">×</button>
        </div>
      )}

      <main className="vista">
        {pestana === "productos"
          ? <ListaProductos onAgregar={agregar} onError={(texto) => setAviso({ tipo: "error", texto })} />
          : <Carrito lineas={carrito} total={total} onEliminar={eliminar} onVaciar={vaciar} />}
      </main>
    </div>
  );
}
