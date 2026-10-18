// Todas las peticiones a la API de Spring Boot, en un solo lugar
const API = "http://localhost:8080/api";

// Hace la petición y devuelve el JSON; si el código no es 2xx, lanza un error con el mensaje de Spring
async function pedir(url, opciones) {
  let res;
  try {
    res = await fetch(API + url, opciones);
  } catch {
    throw new Error("No se pudo conectar con la API. ¿Está encendida en el puerto 8080?");
  }
  if (!res.ok) {
    const cuerpo = await res.json().catch(() => ({}));
    throw new Error(mensajeDeError(res.status, cuerpo.message));
  }
  if (res.status === 204) return null;      // DELETE: no trae cuerpo
  return res.json();                        // aquí el texto JSON se vuelve objetos de JavaScript
}

function mensajeDeError(status, mensaje) {
  if (status === 400) return "La cantidad debe ser al menos 1.";
  if (status === 404) return mensaje || "No se encontró lo que pediste.";
  return mensaje || `Error ${status} en la API.`;
}

export const listarProductos = () => pedir("/productos");
export const listarCarrito = (cod) => pedir(`/carrito/${cod}`);
export const totalCarrito = (cod) => pedir(`/carrito/${cod}/total`);
export const agregar = (cod, idProducto, cantidad) =>
  pedir(`/carrito/${cod}/items?idProducto=${idProducto}&cantidad=${cantidad}`, { method: "POST" });
export const eliminar = (cod, idShoppingCart) => pedir(`/carrito/${cod}/items/${idShoppingCart}`, { method: "DELETE" });
export const vaciar = (cod) => pedir(`/carrito/${cod}`, { method: "DELETE" });
