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
* 📁 `Controllers/` ➡️ `ProductController.java`
* 📁 `Services/` ➡️ `ProductService.java`
* 📁 `Repositories/` ➡️ `ProductRepository.java`
* 📁 `Models/` ➡️ `Product.java`

### 📦 Enfoque Vertical CRUD (Vertical Slice)
Todo lo que el CRUD de productos necesita para funcionar está en un solo lugar:
* 📁 `features/products/`
  * 📄 `CreateProduct.java` (Contiene su propio endpoint/manejador, lógica de negocio y entrada de datos)
  * 📄 `GetProduct.java` (Contiene la consulta de lectura y el registro de respuesta)
  * 📄 `UpdateProduct.java`
  * 📄 `DeleteProduct.java`
  * 📄 `Product.java` (El registro o entidad interna de dominio)

---

## 👍 Ventajas del Vertical CRUD
* **Alta cohesión:** Todo el código que cambia junto, vive junto. Si necesitas arreglar el "Borrar Producto", solo abres ese archivo o carpeta.
* **Menos fricción al desarrollar:** No pierdes tiempo navegando por árboles de carpetas gigantescos para una sola operación.
* **Escalabilidad:** Es mucho más fácil extraer una característica e independiente en un microservicio en el futuro porque ya está aislada verticalmente.
* **Optimización individual:** Cada operación CRUD puede optimizarse de forma única. Por ejemplo, la operación de **Leer** puede usar SQL directo (con JDBC plano) para ser ultra rápida, mientras que la de **Crear** puede usar un framework pesado para asegurar las reglas de negocio.

---

## ☕ Ejemplo Breve en Java SE (Vertical Slice)

En **Java SE**, una forma limpia de implementar el CRUD Vertical dentro del paquete de la característica (`features.products`) consiste en encapsular cada operación en su propia clase, exponiendo un único punto de entrada (por ejemplo, un método `execute`). 

Utilizando características modernas de Java como **Records** (Java 16+) para los DTOs y el manejo de datos, la operación para **Crear un Producto** se estructuraría en un único archivo de la siguiente manera:

```java
package com.example.features.products;

import java.util.UUID;

// Todo lo necesario para la operación "Crear Producto" vive en este archivo
public class CreateProduct {

    // 1. Datos de Entrada (Request DTO)
    public record Request(String name, double price) {}

    // 2. Datos de Salida (Response DTO)
    public record Response(String id, String name, double price) {}

    // 3. Lógica de negocio y persistencia agrupada
    public class Handler {
        
        public Response execute(Request request) {
            // Validación básica de negocio
            if (request.name() == null || request.name().isBlank()) {
                throw new IllegalArgumentException("El nombre del producto es obligatorio.");
            }
            if (request.price() <= 0) {
                throw new IllegalArgumentException("El precio debe ser mayor que cero.");
            }

            // Generar ID e instanciar entidad de dominio local
            String productId = UUID.randomUUID().toString();
            Product product = new Product(productId, request.name(), request.price());

            // Aquí se ejecutaría la persistencia directa (ej. conexión JDBC local)
            System.out.println("Guardando en base de datos de manera aislada: " + product.name());

            // Retornar la respuesta esperada
            return new Response(product.id(), product.name(), product.price());
        }
    }
}
```

Y la entidad base compartida dentro del mismo paquete de la característica sería simplemente:

```java
package com.example.features.products;

// Modelo de dominio local, exclusivo para el módulo de productos
public record Product(String id, String name, double price) {}
```
