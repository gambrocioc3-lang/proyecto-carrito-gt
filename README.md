# CarritoGT

Migración del carrito de compra básico en PHP ([mvelasquezo/carrito-compra-basico](https://github.com/mvelasquezo/carrito-compra-basico)) a una **API REST con Spring Boot**, organizada por capas (Controller, Service, Repository, Entity), con un **frontend en React** que la consume con `fetch`.

Permite listar productos, agregar productos al carrito (si el producto ya está, suma la cantidad), ver el carrito con subtotales y total, eliminar un producto y vaciar el carrito.

## Tecnologías

| Parte | Tecnología | Versión |
|---|---|---|
| Lenguaje | Java (JDK) | 21 |
| Backend | Spring Boot (Web MVC, Data JPA, Validation) | 4.1.1 |
| Construcción | Maven Wrapper (`mvnw`) | incluido en el repo |
| Base de datos | MySQL | 8.x |
| Utilidades | Lombok (getters, setters y `toString`) | la que fija Spring Boot |
| Pruebas | JUnit 4 + Mockito + MockMvc | JUnit 4.13.2 |
| Frontend | React + Vite | React 19, Vite 8 |
| Entorno del frontend | Node.js y npm | Node 20.19 o superior |

## Requisitos

- JDK 21 (`java -version` debe decir 21).
- MySQL 8 con la base `cc` creada con el script `cc.sql` (2 tablas: `producto` y `shopping_cart`, 4 productos).
- Node.js 20.19 o superior, con npm (solo para el frontend).
- No hace falta instalar Maven: el proyecto trae el Maven Wrapper.

## Instalación y configuración

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/gambrocioc3-lang/proyecto-carrito-gt.git
   cd proyecto-carrito-gt
   ```
2. Crear la base de datos con el script `script/cc.sql` del [repositorio original](https://github.com/mvelasquezo/carrito-compra-basico/blob/master/script/cc.sql):
   ```bash
   mysql -u root -p < cc.sql
   ```
3. Crear el archivo de configuración `.env` en la raíz del repo, a partir del ejemplo:
   ```bash
   cp .env.example .env
   ```
   y poner los datos de MySQL (ver *Configuración*).
4. En Linux, dar permiso de ejecución al wrapper (una sola vez):
   ```bash
   chmod +x api/mvnw
   ```
5. Instalar las dependencias del frontend (una sola vez):
   ```bash
   cd frontend
   npm install
   ```

## Configuración

El archivo `.env` (no se sube al repositorio, está en `.gitignore`) tiene las credenciales de la base de datos:

| Variable | Ejemplo | Para qué |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/cc` | servidor, puerto y base de datos |
| `DB_USER` | `carrito` | usuario de MySQL |
| `DB_PASSWORD` | `****` | contraseña de ese usuario |

`api/src/main/resources/application.properties` lee esas variables (`spring.config.import`). Además fija:
- `ddl-auto=none`: JPA no crea ni modifica las tablas del script;
- la estrategia de nombres `PhysicalNamingStrategyStandardImpl`: las columnas de `cc` están en camelCase (`idProducto`) y se usan tal cual;
- `spring.web.error.include-message=always`: los errores devuelven su mensaje.

Puertos: la API usa el **8080** y el frontend el **5173**. CORS solo permite peticiones desde `http://localhost:5173`.

## Cómo iniciar la aplicación

Con MySQL encendido, en dos terminales:

```bash
# Terminal 1: la API (en Windows: .\mvnw spring-boot:run)
cd api
./mvnw spring-boot:run
```

```bash
# Terminal 2: el frontend
cd frontend
npm run dev
```

Abrir **http://localhost:5173** en el navegador.

Para correr las pruebas (no necesitan MySQL): `cd api` y `./mvnw test` (20 pruebas: entidades, services con Mockito y controllers con MockMvc).

## Endpoints de la API

Base: `http://localhost:8080/api`. Todas las respuestas son JSON.

| Método | URL | Parámetros | Respuesta |
|---|---|---|---|
| GET | `/productos` | — | **200**, lista de productos |
| GET | `/carrito/{codUsuario}` | — | **200**, líneas del carrito con `subTotal` |
| GET | `/carrito/{codUsuario}/total` | — | **200**, total del carrito (número) |
| POST | `/carrito/{codUsuario}/items` | `idProducto`, `cantidad` (≥ 1) | **201**, la línea creada o sumada · **400** cantidad inválida o faltante · **404** producto inexistente |
| DELETE | `/carrito/{codUsuario}/items/{idShoppingCart}` | — | **204** · **404** si la línea no existe o no es de ese usuario |
| DELETE | `/carrito/{codUsuario}` | — | **204**, vacía el carrito |

Ejemplo, `POST /api/carrito/ana/items?idProducto=3&cantidad=2` → `201`:
```json
{
  "idShoppingCart": 7,
  "producto": { "idProducto": 3, "nombre": "Luxury Ultra thin Wrist Watch", "codigo": "wristWear03",
                "imagen": "images/product/watch.jpg", "precio": 2121.86 },
  "codUsuario": "ana",
  "cantidad": 2,
  "subTotal": 4243.72
}
```

Ejemplo de error, producto inexistente → `404`:
```json
{ "status": 404, "error": "Not Found", "message": "No existe el producto 99", "path": "/api/carrito/ana/items" }
```

## Estructura

```
api/                    Backend Spring Boot (paquete gt.carrito)
  controller/           Peticiones HTTP: URLs, parámetros y códigos de respuesta
  service/              Reglas del carrito (sumar si ya está, validar dueño, total)
  repository/           Acceso a MySQL con Spring Data JPA
  entity/               Producto y ShoppingCart (las 2 tablas)
  exception/            RecursoNoEncontradoException (404)
frontend/               React + Vite: api.js (fetch), componentes y estilos
docs/                   Diagrama de clases y diagrama de casos de uso (Dia y PNG)
```

## Mejoras respecto a la versión PHP

- Consultas con parámetros (Spring Data JPA): sin inyección SQL.
- Eliminar un producto revisa que la línea sea del usuario; antes se podía borrar la de otro.
- Eliminar y vaciar usan `DELETE`, no enlaces `GET`.
- Credenciales en `.env`, fuera del código.
- Dinero con `BigDecimal`, sin errores de redondeo de `double`.
- Errores con códigos HTTP coherentes (400, 404) y mensajes que el frontend muestra.

## Problemas conocidos y limitaciones

- No hay inicio de sesión: el «usuario» es un código aleatorio guardado en el navegador (`localStorage`), como el GUID de sesión del PHP. Otro navegador = otro carrito. La autenticación está fuera del alcance del proyecto.
- La columna `precio` sigue siendo `double(10,2)` en la base de datos (no se modificó el script); Java la lee como `BigDecimal`.
- Con `cantidad=0`, el mensaje del 400 que devuelve la API es técnico; el frontend muestra su propio texto.
- Las pruebas de los controllers usan `SpringRunner`, marcado como obsoleto en Spring 7 (recomienda JUnit 5); se mantiene porque el curso usa JUnit 4.
- En modo desarrollo (`npm run dev`), React ejecuta dos veces las peticiones iniciales (`StrictMode`); no pasa en `npm run build`.
- El frontend y la API corren por separado; para usarlo hay que levantar los dos.
