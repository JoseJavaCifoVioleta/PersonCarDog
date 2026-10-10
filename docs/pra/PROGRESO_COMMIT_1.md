# 📔 Bitácora de Aprendizaje - Commit 1: Herramientas Base (Utils)

## 🎯 ¿Qué hemos hecho en este paso?
Hemos preparado la interfaz visual y las herramientas de lectura del teclado en `Utils.java`. El programa ahora es capaz de pintar los submenús de opciones para Personas, Coches, Perros y Transacciones, y tiene funciones seguras para pedir textos y números.

## 🎓 Conexión con el MOOC de Helsinki (Capítulos 1 y 2)
* **Lectura de datos (Scanner):** Aprendimos que para recibir información del usuario usamos la clase `Scanner` y el método `nextLine()`.
* **Conversión de Tipos (Parsing):** En el método `askInt`, aplicamos `Integer.parseInt()` para transformar el texto del teclado en un número entero computable.
* **Estructuras de Control (While y Try-Catch):** Usamos un bucle `while(true)` infinito en `askInt` combinado con un bloque de seguridad `try-catch`. Si el usuario introduce letras en vez de un número, el programa no explota; captura el error y vuelve a preguntar de forma segura.

## 📋 Checklist de Verificación Temporal
- [x] Submenús de texto diseñados y listos.
- [x] Captura de texto (`askString`) implementada de forma estática.
- [x] Captura de números (`askInt`) protegida contra fallos de escritura.