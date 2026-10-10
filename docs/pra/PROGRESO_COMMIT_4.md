# 📔 Bitácora de Aprendizaje - Commit 4: Ciclo Vertical de Perros (Dog)

## 🧱 Roles y Flujo de Trabajo en este paso
* **Model (`Dog.java`):** Es nuestra plantilla pura para animales. Define que cada perro en el sistema cuenta con un identificador único automático, un nombre, una raza y una edad.
* **Repository (`Repository.java`):** Actúa como el almacén central en memoria, proporcionando acceso a las listas compartidas (`addDog()`, `getAllDogs()`).
* **Service (`Service.java`):** Implementa las reglas de control de los datos de los perros, asegurando que ninguna entrada inválida contamine el almacén.
* **Controller (`Controller.java`):** Sirve de puente interactivo, capturando los datos mediante la consola para invocar las operaciones del Service.

## 🎓 Conexión con el MOOC de Helsinki (Capítulos 4 y 5)
* **Reutilización de Patrones Lógicos:** En este commit el equipo puede observar que programar la lógica de los Perros es estructuralmente idéntico a lo que hicimos con las Personas y los Coches. Este diseño repetitivo asienta la confianza en la programación orientada a objetos (POO).
* **Manejo de Colecciones Dinámicas:** Usamos el método `.remove()` de la clase `ArrayList` de Java para buscar y extirpar la referencia exacta del objeto seleccionado de las listas de nuestra memoria.

## 📋 Checklist de Verificación en Consola
- [x] Opción 2 del menú principal abre con éxito el submenú de perros.
- [x] Crear un perro almacena correctamente la información.
- [x] Listar perros muestra tanto los animales pre-cargados por el Seeder automatizado como los añadidos manualmente.
