# PersonCarDog — Adopción Many-to-Many (Custodia Compartida)

Trabajo correspondiente al **Part 2 — Many-to-Many Dog Adoption (Shared Custody)** del proyecto *PersonCarDog* (Java SE, pre-Spring Boot).

Este documento reúne la contribución de cada miembro del equipo y el grado de cumplimiento de los **criterios de aceptación** del enunciado.

---

## 1. Resumen

El proyecto partía de una adopción **uno-a-uno** (`adoptDog` de la Parte 1). El objetivo del Part 2 es evolucionarla a una relación **many-to-many** con custodia compartida:

- Una `Person` puede tener **varios perros** (máx. 3 activos).
- Un `Dog` puede tener **varios dueños** (máx. 2 activos).
- `DogAdoption` pasa a ser la **entidad de unión** explícita (una tupla por par persona-perro, con `fee`, `date` y `status`).

Toda la lógica de negocio vive en `Service.java`; toda la interacción de usuario en `Controller.java` + `Utils.java`. Sin anotaciones JPA (Java SE puro).

---

## 2. Equipo y área de trabajo

| Miembro | Área principal |
|---|---|
| **Laura** (`Mi-Sharona`) | Modelo — refactor a listas + estado de adopción |
| **Francesca** (`Francesca Romano`) | Devolución, historial y UI de adopción |
| **Jose** (`josejavacifovioleta`) | Servicio (`adoptDog`), `ResultOperation`, Repository, pruebas, datos de ejemplo e integración |

---

## 3. Contribución por miembro

### 3.1 Laura — Modelo: listas + estado (Requisito 1)

Refactorizó las tres clases del modelo a la nueva relación many-to-many:

- `Person`: sustituyó la referencia única `private Dog dog;` por `private List<Dog> dogs` con `getDogs()`.
- `Dog`: sustituyó `private Person owner;` por `private List<Person> owners` con `getOwners()`.
- `DogAdoption`: añadió el campo `status` con los literales `"ACTIVE"` / `"RETURNED"`.

**Checklist frente al enunciado:**

- [x] `Person` tiene `List<Dog> dogs`
- [x] `Dog` tiene `List<Person> owners`
- [x] `DogAdoption` tiene campo `status` (`ACTIVE` / `RETURNED`)
- [x] `toString()` sin recursión infinita (imprime nombres/ids)

### 3.2 Francesca — Devolución, historial y UI (Requisitos 3, 4 y 5)

Implementó el cierre unilateral de la adopción y la gestión del historial:

- `Service.returnDog(Person, Dog, Repository)`: localiza el registro `ACTIVE` del par exacto, retira el vínculo de ambas colecciones y marca el registro como `RETURNED` añadiendo `" - RETURNED"` al contrato. El registro permanece en el historial.
- `getDogsByOwner` / `getOwnersByDog`: lectura de las listas activas.
- Cascada en `deletePerson` / `deleteDog`: retiran los vínculos del lado opuesto y marcan como `RETURNED` las adopciones relacionadas, **sin** borrar los registros (traza de auditoría).
- Ampliación de `Controller` y `Utils` con las opciones de menú para devolver un perro y consultar dueños/perros.

**Checklist frente al enunciado:**

- [x] `returnDog` elimina solo el par seleccionado y marca la adopción `RETURNED`
- [x] `getDogsByOwner` / `getOwnersByDog` devuelven las listas activas correctas
- [x] `deletePerson` / `deleteDog` limpian las listas opuestas y marcan las adopciones `RETURNED`
- [x] El historial de `DogAdoption` se conserva (no se borran registros)
- [x] Opciones de menú para devolver perro y consultar dueños/perros

### 3.3 Jose — Servicio, estilo, pruebas e integración (Requisitos 2, 3, 5)

Implementó el caso de uso central y la infraestructura de soporte:

- `Service.adoptDog(adopter, dog, fee, repo)` con la **cadena completa de 7 validaciones** y el mantenimiento de ambos lados de la relación.
- `ResultOperation` como objeto de retorno estandarizado, con el mismo estilo que `buyCar` / Parte 1.
- Helpers de `Repository` para las adopciones.
- Batería de **66 pruebas** (incluye las 7 validaciones, los límites 3/2, devolución parcial, re-adopción y la cascada de borrado).
- `DataSeeder` con datos de custodia compartida (un perro con varios dueños y una persona con varios perros).
- Menú de adopción en `Controller` / `Utils` (selección por id).
- Integración de las tres aportaciones en una única rama `shared-custody-team`.

**Checklist frente al enunciado:**

- [x] `adoptDog` aplica las 7 validaciones (existencia, edad ≥ 18, fee ≥ 0, par duplicado, máx. 3 perros, máx. 2 dueños)
- [x] `adoptDog` mantiene los dos lados de la relación y crea el registro `ACTIVE`
- [x] No se permiten pares activos duplicados
- [x] Límites máximos (3 perros / 2 dueños) aplicados
- [x] `Controller` permite adoptar y devolver seleccionando ids
- [x] `DataSeeder` crea al menos un perro con varios dueños y una persona con varios perros
- [x] `buyCar`, CRUD y flujos previos sin regresiones (cubierto por pruebas)
- [x] Sin anotaciones JPA en el código Java SE

---

## 4. Criterios de aceptación — verificación conjunta

| Criterio de aceptación | Responsable | Estado |
|---|---|---|
| `Person` con `List<Dog>`; `Dog` con `List<Person>`; `DogAdoption` con `status` | Laura | ✅ |
| `adoptDog` aplica las 7 reglas y mantiene ambos lados | Jose | ✅ |
| `returnDog` elimina solo el par elegido y marca `RETURNED` | Francesca | ✅ |
| `getDogsByOwner` / `getOwnersByDog` devuelven listas activas correctas | Francesca | ✅ |
| `deletePerson` / `deleteDog` limpian listas opuestas y marcan `RETURNED` | Francesca | ✅ |
| Sin pares activos duplicados | Jose | ✅ |
| Límites máximos (3 perros / 2 dueños) aplicados | Jose | ✅ |
| `Controller` permite adoptar y devolver por id | Jose + Francesca | ✅ |
| `DataSeeder` con un perro multi-dueño y una persona multi-perro | Jose | ✅ |
| Sin regresión en `buyCar`, CRUD y flujos 1:1 | Equipo | ✅ |
| Sin anotaciones JPA | Equipo | ✅ |

---

## 5. Verificación

- **66 pruebas, 0 fallos, 0 errores.**
- Comando: `mvnw.cmd test` (requiere Java 21; no requiere Maven instalado).
- Datos cargados por `DataSeeder` permiten demostrar la custodia compartida desde la consola.

---

## 6. Cómo ejecutar

```bash
# Compilar y ejecutar las pruebas
mvnw.cmd clean test

# Arrancar la aplicación
mvnw.cmd exec:java "-Dexec.mainClass=org.example.App"
```

---

## 7. Manual de uso

Aplicación de consola. Toda la interacción se hace escribiendo números o texto y pulsando **Enter**. Los identificadores (ID) son cadenas únicas; para obtenerlos, usa las opciones *List all…* antes de operar.

### 7.1 Arranque

```bash
mvnw.cmd exec:java "-Dexec.mainClass=org.example.App"
```

Al arrancar, la aplicación **carga datos de ejemplo automáticamente** (varias personas, coches, perros y algunas adopciones de custodia compartida), de modo que se puede probar sin crear nada antes.

### 7.2 Menú principal

```
===== MAIN MENU =====
1. Person operations
2. Dog operations
3. Car operations
4. Buy a car (person to person)
5. View car transactions (read only)
6. Dog adoptions
7. Quit
```

- **1–3** abren los submenús CRUD de personas, perros y coches.
- **4** compra un coche entre dos personas.
- **5** consulta el historial de transacciones de coche (solo lectura).
- **6** abre el submenú de adopciones (núcleo del Part 2).
- **7** sale de la aplicación.

Los identificadores (id) son cadenas únicas. Para trabajar con uno, primero usa las opciones **List all…** de cada menú para verlo y copiarlo.

### 7.3 Operaciones básicas (Personas / Perros / Coches)

Los tres submenús comparten la misma estructura: **crear**, **listar**, **buscar por id**, **actualizar** y **borrar**. En las actualizaciones, dejar un campo en blanco (o `-1` en números) conserva el valor anterior. Al **borrar una persona** o un **perro** con adopciones, la aplicación cierra suavemente los vínculos (no borra el historial).

### 7.4 Adopción compartida (Many-to-Many)

Desde el menú principal, opción **6 → Dog adoptions**:

```
===== DOG ADOPTIONS =====
1. List all adoptions
2. Find adoption by ID
3. Adopt a dog
4. View dogs by owner
5. View owners of a dog
6. Return a dog
7. Back
```

**Adoptar un perro (3)**

1. Introduce el **id de la persona** adoptante.
2. Introduce el **id del perro**.
3. Introduce la **cuota** (fee) de adopción.
4. La aplicación valida las reglas y confirma con un mensaje.

Reglas que la aplicación comprueba (según el enunciado):

- La persona y el perro deben existir.
- La persona debe ser **adulta** (edad ≥ 18).
- La cuota debe ser **≥ 0**.
- No puede repetirse un **par activo** persona-perro.
- Una persona no puede tener **más de 3 perros** activos.
- Un perro no puede tener **más de 2 dueños** activos.

**Devolver un perro (6)**

1. Introduce el **id de la persona**.
2. Introduce el **id del perro**.

Cierra **únicamente** la adopción de ese par: el perro puede seguir teniendo otros dueños y la persona otros perros. El registro de adopción no se borra; se marca como devuelto y queda en el historial.

**Consultar la custodia**

- **4. View dogs by owner**: introduce el id de una persona → muestra sus perros activos.
- **5. View owners of a dog**: introduce el id de un perro → muestra sus dueños activos.
- **1. List all adoptions** y **2. Find adoption by ID**: consultan el historial completo.

### 7.5 Ejemplo de sesión (adopción y devolución)

```
===== MAIN MENU =====
Select an option: 6

===== DOG ADOPTIONS =====
Select an option: 3
Enter adopter person ID: <id-persona>
Enter dog ID: <id-perro>
Enter adoption fee: 50
SUCCESS: Dog adopted for 50

...
Select an option: 6
Enter person ID: <id-persona>
Enter dog ID: <id-perro>
SUCCESS: Dog returned successfully
```

### 7.6 Datos cargados automáticamente

Al arrancar, `DataSeeder` crea:

- 3 coches y 3 personas (con coches asignados y 2 transacciones de ejemplo).
- 4 perros.
- Cuatro adopciones que demuestran la custodia compartida: **un perro con dos dueños** y **una persona con dos perros**.

Esto permite probar los límites y los listados sin crear nada a mano.

---

## 8. Estado en control de versiones

- Rama de entrega: **`shared-custody-team`**.
- Integra en un único historial las aportaciones de Laura (`dog-adoption-francesca`), Francesca (`shared-custody-fra`) y Jose.
