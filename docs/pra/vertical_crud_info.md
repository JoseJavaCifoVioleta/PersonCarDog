# ¿Qué es Vertical CRUD?

El **"CRUD vertical" (Vertical CRUD)** es un enfoque arquitectónico y de desarrollo de software donde **las operaciones CRUD (Crear, Leer, Actualizar, Borrar) se organizan en torno a características o verticales de negocio**, en lugar de dividirse en las capas técnicas tradicionales (como la capa de presentación, la capa de negocio y la capa de datos).

Este concepto está estrechamente relacionado con la arquitectura de **Vertical Slice Architecture** (Arquitectura de Rebanadas Verticales).

---

## 🧱 Comparación: Enfoque Horizontal vs. Enfoque Vertical

| Característica | Enfoque Tradicional (Horizontal / Por Capas) | Enfoque Vertical (Vertical CRUD) |
| :--- | :--- | :--- |
| **Organización** | El código se agrupa por **tipo de componente técnico** (todos los Controladores juntos, todos los Servicios juntos, todos los Repositorios juntos). | El código se agrupa por **característica o funcionalidad de negocio** (todo lo relacionado con "Usuarios" o "Pedidos" junto). |
| **Flujo de desarrollo** | Para crear un simple CRUD, tienes que modificar múltiples carpetas y archivos a lo largo de toda la aplicación. | Para crear o modificar un CRUD, trabajas dentro de un solo módulo o "rebanada" independiente. |
| **Acoplamiento** | Alto acoplamiento técnico. Un cambio en la base de datos a menudo rompe capas superiores. | Alto acoplamiento *interno* de la característica, pero bajo acoplamiento con el resto del sistema. |

---

## 📂 Ejemplo Práctico de Estructura de Carpetas

Si estuvieras construyendo el CRUD de un sistema de "Productos", las estructuras se verían así:

### ❌ Enfoque Horizontal Tradicional (Clean Architecture / Onion)
Modificar el CRUD implica saltar entre todas estas carpetas:
* 📁 `Controllers/` ➡️ `ProductController.cs`
* 📁 `Services/` ➡️ `ProductService.cs`
* 📁 `Repositories/` ➡️ `ProductRepository.cs`
* 📁 `Models/` ➡️ `Product.cs`

###  Enfoque Vertical CRUD (Vertical Slice)
Todo lo que el CRUD de productos necesita para funcionar está en un solo lugar:
* 📁 `Features/Products/`
  * 📄 `CreateProduct.cs` (Contiene su propio endpoint, lógica de validación y comando)
  * 📄 `GetProduct.cs` (Contiene la consulta de lectura y el mapa de respuesta)
  * 📄 `UpdateProduct.cs`
  * 📄 `DeleteProduct.cs`
  * 📄 `ProductDomainModel.cs`

---

## 👍 Ventajas del Vertical CRUD
* **Alta cohesión:** Todo el código que cambia junto, vive junto. Si necesitas arreglar el "Borrar Producto", solo abres ese archivo o carpeta.
* **Menos fricción al desarrollar:** No pierdes tiempo navegando por árboles de carpetas gigantescos para una sola operación.
* **Escalabilidad:** Es mucho más fácil extraer una característica e independiente en un microservicio en el futuro porque ya está aislada verticalmente.
* **Optimización individual:** Cada operación CRUD puede optimizarse de forma única. Por ejemplo, la operación de **Leer** puede usar SQL directo (Dapper) para ser ultra rápida, mientras que la de **Crear** puede usar un ORM pesado (Entity Framework) para asegurar las reglas de negocio.

Suele combinarse mucho con patrones como **CQRS** (Separación de Consultas y Comandos) y librerías de mediación como **MediatR** (en el mundo .NET).
