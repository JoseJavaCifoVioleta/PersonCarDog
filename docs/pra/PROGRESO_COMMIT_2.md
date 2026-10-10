# 📔 Bitácora de Aprendizaje - Commit 2: Ciclo de Personas

## 🎯 ¿Qué hemos hecho en este paso?
Hemos completado el flujo operativo para las Personas. Ahora el usuario puede interactuar desde el menú de la consola (Controller) para registrar, listar, buscar, modificar y borrar personas reales en la memoria del sistema.

## 🎓 Conexión con el MOOC de Helsinki (Capítulos 3, 4 y 5)
* **Estructuras de Decisión Anidadas (Switch-Case):** Aprendimos cómo un menú principal puede llamar a un submenú y gestionar las opciones de forma aislada sin mezclar flujos de datos.
* **Listas de Objetos (ArrayList y Bucles For-Each):** Para listar los elementos, llamamos al método del repositorio y los recorremos utilizando estructuras de repetición (`for (Person p : lista)`).
* **Manipulación de Objetos en Memoria:** Al actualizar o borrar, usamos condicionales `if (objeto != null)` para asegurarnos de que la referencia existe antes de invocar a sus métodos internos.

## 📋 Checklist de Validación Funcional
- [x] Opción 1 del menú principal abre el submenú de personas.
- [x] Crear persona funciona y valida datos vacíos.
- [x] Listar personas muestra los datos inyectados por el Seeder y los nuevos.
- [x] Buscar por ID localiza correctamente la instancia.
- [x] Modificar campos altera el objeto real en el almacén.
- [x] Eliminar persona limpia su registro de forma segura.

## 🧱 Arquitectura de Capas (Explicación del Repositorio y Modelos)
Para que el código sea limpio, dividimos el trabajo en 4 roles:
1. **Model (`Person`):** Son las plantillas que dan forma a los datos (un contenedor con nombre y edad).
2. **Repository (`Repository`):** Es nuestra despensa o almacén en memoria. Guarda los objetos en listas (`ArrayList`) para que no se borren al navegar por los menús.
3. **Service (`Service`):** Es el cerebro o el "Chef". Comprueba que los datos no sean erróneos y gestiona las reglas del juego.
4. **Controller y Utils:** Son la cara al público (el "Camarero"). Pintan los textos en consola y capturan el teclado.
