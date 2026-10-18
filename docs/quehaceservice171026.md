👤 Sección 1: Gestión de Personas (CRUD)

Esta parte del código es la ventanilla donde se maneja el registro de los clientes o personas.

-   **Crear Persona (`**createPerson**`):**
-   1.  Da un saludo en la pantalla interna: *"Bienvenido a Crear Persona"*.
    2.  Abre un formulario en blanco para el resultado.
    3.  **Paso de control 1:** Revisa el nombre. Si está vacío o no se puso nada, se detiene, cancela el proceso y devuelve un recibo diciendo: *"Error: El nombre no puede estar vacío"*.
    4.  **Paso de control 2:** Revisa la edad. Si pusiste un número negativo (como -5), se detiene y dice: *"Error: La edad no puede ser negativa"*.
    5.  Si los datos son válidos, crea la ficha de la persona con su nombre y edad, la guarda en el archivador central (`repo`) y te entrega el recibo de éxito: *"Persona creada con éxito"*.
-   **Buscar Persona por ID (`**getPersonById**`):**
-   1.  Revisa que hayas anotado el número de identificación (ID). Si no lo pusiste, da error.
    2.  Va al archivador central a buscar ese ID. Si no encuentra a nadie con ese número, te devuelve un recibo de: *"Persona no encontrada"*.
    3.  Si la encuentra, te la muestra y te da el recibo de éxito.
-   **Listar todas las Personas (`**getAllPeople**`):**
-   1.  Va directo al archivador, saca la lista completa de todas las personas registradas y te entrega un recibo que dice cuántas personas hay en total.
-   **Modificar Persona (`**updatePerson**`):**
-   1.  Primero te pide el ID de la persona que quieres cambiar. Si no existe la persona, cancela el trámite.
    2.  Si pusiste un nombre nuevo válido, borra el anterior y escribe el nuevo.
    3.  Si pusiste una edad válida (mayor o igual a cero), actualiza la edad.
    4.  Guarda los cambios y te devuelve la ficha actualizada.
-   **Borrar Persona (`**deletePerson**`):**
-   1.  Busca a la persona por su ID. Si no existe, detiene el proceso.
    2.  **Efecto dominó (Limpieza):** Antes de borrarla, el sistema es inteligente y limpia sus papeles: si tenía un coche a su nombre, lo deja sin dueño. Si tenía perros adoptados, va a la ficha de cada perro, borra a esta persona como dueño y cambia el estado de sus contratos de adopción a *"DEVUELTO"*.
    3.  Finalmente, la elimina por completo del archivador central.

🚗 Sección 2: Gestión de Coches y Perros (CRUD)

Las secciones de **Coches (`**Car**`)** y **Perros (`**Dog**`)** hacen exactamente los mismos pasos lógicos que la sección de personas (Crear, Buscar, Listar, Modificar y Borrar), pero con sus propios datos de control:

-   Para los **Coches**, el sistema comprueba que el año de fabricación no sea menor a **1886** (el año en que se inventó el primer coche). Al borrar un coche, se asegura de que ninguna persona se quede con un coche fantasma asignado.
-   Para los **Perros**, comprueba nombre, raza y edad. Al borrar un perro, busca a sus dueños, les quita ese perro de sus listas y cancela los contratos de adopción activos pasando su estado a *"DEVUELTO"*.

💰 Sección 3: Transacción de Compra de Coche (`buyCar`)

Esta es la ventanilla donde **una Persona le compra un coche a otra**. Sigue un orden muy estricto:

1.  **Verificación del Comprador:** ¿Existe este comprador en nuestro archivador? Si no, se cancela la venta.
2.  **Verificación del Vendedor:** ¿Existe este vendedor? Si no, se cancela.
3.  **Verificación de Identidad:** Comprueba que el comprador y el vendedor no sean la misma persona. ¡No puedes comprarte un coche a ti mismo!
4.  **Verificación del Coche del Vendedor:** Mira si el vendedor realmente tiene un coche para vender. Si no tiene coche, la venta se cancela.
5.  **Verificación del Comprador:** Mira si el comprador ya tiene un coche. En este sistema, las personas solo pueden tener **un coche a la vez**. Si ya tiene uno, se cancela.
6.  **El Intercambio:** Si todo lo anterior es correcto, el sistema hace el traspaso: le asigna el coche al comprador y le quita el coche al vendedor (se lo deja en nada/vacío).
7.  **El Registro:** Crea un recibo oficial de venta (un contrato con la fecha, el precio y quiénes participaron), lo guarda en el histórico y te avisa que la compra fue un éxito.

🐕 Sección 4: Adopción y Devolución de Perros

Aquí se gestiona la "custodia compartida" de los perritos. El sistema permite que varias personas cuiden de un perro.

-   **Adoptar un Perro (`**adoptDog**`):**
-   1.  Comprueba que la persona y el perro existan en los registros.
    2.  **Control de edad:** La persona debe tener **18 años o más** para poder adoptar.
    3.  **Control de pago:** La tasa de adopción no puede ser un dinero negativo.
    4.  **Control de duplicados:** Revisa los contratos antiguos para asegurarse de que esta persona no tenga ya una adopción *activa* con este mismo perro.
    5.  **Límite por Persona:** Cuenta cuántos perros tiene la persona. Si ya tiene **3 perros activos**, el sistema le prohíbe adoptar más.
    6.  **Límite por Perro:** Cuenta cuántos dueños tiene el perro. Si el perro ya tiene **2 dueños activos**, el sistema no permite que lo adopte nadie más (máximo 2 cuidadores).
    7.  **Éxito:** Si pasa todos los filtros, añade el perro a la lista de la persona, añade la persona a la lista de dueños del perro, genera el contrato oficial como *"ACTIVO"* y lo guarda.
-   **Devolver un Perro (`**returnDog**`):**
-   1.  Comprueba que la persona y el perro existan en el sistema.
    2.  Busca en el archivo histórico el contrato exacto que esté **"ACTIVO"** entre esa persona y ese perro. Si no encuentra ninguno, dice que no hay nada que devolver.
    3.  Si lo encuentra, rompe el vínculo: quita al perro de la lista de la persona y quita a la persona de la lista de dueños del perro (pero si el perro tenía otro dueño, ese se queda).
    4.  El contrato de adopción no se borra (para no perder el historial), sino que se actualiza escribiendo que ahora está **"RETURNED" (Devuelto)**.

📊 Sección 5: Consultas Finales

Al final del código hay ventanillas rápidas de consulta:

-   `getDogsByOwner`: Le das una persona y te saca la lista de todos los perros que cuida actualmente.
-   `getOwnersByDog`: Le das un perro y te dice quién o quiénes son sus dueños actuales.
-   `getDogAdoptionById` y `getAllDogAdoptions`: Permiten buscar un contrato de adopción específico por su número o listar todos los contratos de adopción que se han hecho en la historia de la oficina.