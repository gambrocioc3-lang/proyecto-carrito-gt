// Quién es el cliente: un código al azar guardado en el navegador
// (en el PHP era el GUID que Auth::getUsuario guardaba en la sesión)
export function codigoUsuario() {
  let cod = localStorage.getItem("codUsuario");
  if (!cod) {
    cod = crypto.randomUUID();
    localStorage.setItem("codUsuario", cod);
  }
  return cod;
}
