# 📔 Bitácora de Aprendizaje - Commit 3: Ciclo Vertical de Coches (Car)

## 🧱 Roles y Flujo de Trabajo en este paso
* **Model (`Car.java`):** Es la plantilla de datos pura. Define que cada coche tiene una marca, un modelo y un año.
* **Repository (`Repository.java`):** Proporciona la despensa central (`addCar()`, `getAllCars()`) para almacenar los coches en un `ArrayList` y que persistan en memoria.
* **Service (`Service.java`):** Valida las reglas de negocio (año correcto, campos no vacíos) y gestiona la integridad del sistema al borrar un coche.
* **Controller (`Controller.java`):** Captura las elecciones de la consola mediante herramientas de `Utils` y delega la responsabilidad al Service.

## 🎓 Conexión con el MOOC de Helsinki (Capítulos 4 y 5)
* **Encapsulamiento de Objetos (Capítulo 5):** Usamos los métodos `setMake()`, `setModel()` y `setYear()` para alterar de forma controlada propiedades privadas de los objetos guardados en el Repositorio.
* **Comparación de Referencias y Objetos:** Al borrar un coche, recorremos la lista de personas y comparamos si el coche de la persona (`p.getCar()`) es el mismo coche que queremos borrar mediante `.equals()`, rompiendo la relación si coincide.

## 📋 Checklist de Verificación en Consola (¡Hora de Probarlo!)
- [x] Opción 3 del menú principal abre el submenú de vehículos.
- [x] Crear un coche añade la instancia correctamente en memoria.
- [x] Si intentas borrar un coche asignado a una persona (las que inyecta el Seeder), verás en consola el mensaje de aviso limpiando la referencia del dueño de forma automática.
