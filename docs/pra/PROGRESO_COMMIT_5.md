# 📔 Bitácora de Aprendizaje - Commit 5: Historial de Transacciones (Read-Only)

## 🧱 Roles y Flujo de Trabajo en este paso
* **Model (`CarTransaction.java`):** Actúa como un recibo físico sellado. Contiene información del comprador, el vendedor, la fecha, el coche y el precio.
* **Repository (`Repository.java`):** Guarda la lista inalterable de transacciones generadas por el sistema.
* **Service (`Service.java`):** Ofrece herramientas exclusivas de consulta (`getCarTransactionById` y `getAllCarTransactions`). No se incluyen opciones de creación o borrado para garantizar la seguridad de la auditoría.
* **Controller (`Controller.java`):** Muestra el submenú restringido de solo dos opciones operativas (Listar todo y Buscar por ID).

## 🎓 Conexión con el MOOC de Helsinki (Capítulos 4 y 5)
* **Concepto de "Efecto Secundario" (Side Effect):** Las transacciones no se pueden "crear" desde su propio menú. La única forma de que nazca una transacción es ejecutando la opción 4 del menú principal ("Buy a car"). El método invoca de forma automática la creación interna del registro.
* **Seguridad en la encapsulación:** Al omitir los métodos modificadores, el código impide que los usuarios puedan "alterar las cuentas" o borrar registros del historial del negocio.

## 📋 Checklist de Verificación en Consola
- [x] Opción 5 del menú principal abre el menú de transacciones de solo lectura.
- [x] Al listar, verás las 2 transacciones ficticias que el `DataSeeder` inyectó para pruebas.
- [x] Si vuelves al menú principal, ejecutas la opción 4 ("Buy a car") para hacer una compra real, y regresas aquí, verás que ha aparecido de forma automática un tercer recibo con los detalles de tu compra.
