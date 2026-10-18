# PersonCarDog — Adopción Many-to-Many (Custodia Compartida)

Implementación completa del **Part 2 — Many-to-Many Dog Adoption (Shared Custody)** sobre el proyecto base *PersonCarDog* (Java SE, pre-Spring Boot).

Esta rama contiene la versión final y definitiva del ejercicio, incluida la **persistencia de datos en JSON** (opciones 9 y 10 del menú).

---

## 1. Resumen

El proyecto partía de una adopción **uno-a-uno** (`adoptDog` de la Parte 1). El Part 2 lo evoluciona a una relación **many-to-many** con custodia compartida:

- Una `Person` puede tener **varios perros** (máx. 3 activos).
- Un `Dog` puede tener **varios dueños** (máx. 2 activos).
- `DogAdoption` es la **entidad de unión** explícita: una tupla por par persona-perro, con `fee`, `date` y `status`.

Además se añade **guardado y carga del repositorio en formato JSON** con Jackson.

---

## 2. Qué incluye esta rama

| Área | Contenido |
|---|---|
| **Modelo** | `Person` con `List<Dog> dogs`; `Dog` con `List<Person> owners`; `DogAdoption` con `status` (`ACTIVE` / `RETURNED`). Sin referencias únicas heredadas. |
| **Servicio** | `adoptDog` con las 7 validaciones, `returnDog`, `getDogsByOwner`, `getOwnersByDog`, `getDogAdoptionById`, y cascada en `deletePerson` / `deleteDog`. |
| **Estilo** | Todos los métodos de negocio devuelven `ResultOperation`, con el mismo patrón que `buyCar` / Parte 1. |
| **UI** | Menú de consola de 11 opciones; selección por id en todas las operaciones. |
| **Persistencia** | `JsonStore` con `save` / `load` sobre `repository.json` (Jackson `jackson-databind`). |
| **Datos** | `DataSeeder` con custodia compartida de ejemplo. |
| **Pruebas** | 76 pruebas JUnit 5, incluyendo round-trip de JSON. |

---

## 3. Estructura

```
src/main/java/org/example/
├── App.java
├── model/          Person, Dog, DogAdoption, Car, CarTransaction
├── repository/     Repository
├── service/        Service  (toda la lógica de negocio)
├── controller/     Controller
└── utils/          Utils, ResultOperation, DataSeeder, JsonStore

src/test/java/org/example/
├── service/        AdoptDogServiceTest, ServiceTest
├── repository/     RepositoryTest, DogAdoptionRepositoryTest
└── utils/          JsonStoreTest
```

Toda la lógica de negocio vive en `Service.java`; toda la interacción de usuario en `Controller.java` + `Utils.java`. Sin anotaciones JPA: Java SE puro.

---

## 4. Criterios de aceptación

- [x] `Person` tiene `List<Dog> dogs`; `Dog` tiene `List<Person> owners`
- [x] `DogAdoption` tiene campo `status` (`ACTIVE` / `RETURNED`)
- [x] `adoptDog` aplica las 7 reglas y mantiene ambos lados de la relación
- [x] `returnDog` elimina solo el par elegido y marca la adopción `RETURNED`
- [x] `getDogsByOwner` y `getOwnersByDog` devuelven las listas activas correctas
- [x] `deletePerson` y `deleteDog` limpian las listas opuestas y marcan `RETURNED`
- [x] No se permiten pares activos duplicados
- [x] Límites máximos (3 perros / 2 dueños) aplicados
- [x] El *Controller* permite adoptar y devolver seleccionando ids
- [x] `DataSeeder` crea al menos un perro multi-dueño y una persona multi-perro
- [x] `buyCar`, CRUD y flujos previos sin regresiones
- [x] Sin anotaciones JPA en el código Java SE
- [x] Guardado y carga del repositorio en JSON (opciones 9 y 10)

---

## 5. Verificación

- **76 pruebas, 0 fallos, 0 errores** (`DogAdoptionRepositoryTest` 5, `RepositoryTest` 8, `ServiceTest` 20, `AdoptDogServiceTest` 32, `JsonStoreTest` 10, `AppTest` 1).
- Comando: `mvnw.cmd clean test`.

---

## 6. Cómo ejecutar

Requiere **Java 21** (no hace falta tener Maven instalado, se usa el *wrapper* incluido). Si tu sistema no tiene la 21 activa por defecto, apunta `JAVA_HOME` a una instalación 21.

```bash
# Compilar y ejecutar las pruebas (76 pruebas)
mvnw.cmd clean test

# Compilar y arrancar la aplicación
mvnw.cmd compile exec:java "-Dexec.mainClass=org.example.App"
```

> **Nota:** `exec:java` ejecuta las clases ya compiladas, por eso se incluye `compile` en el mismo comando. Sin él, el arranque fallará si la carpeta `target/` está vacía.

---

## 7. Manual de uso

Aplicación de consola. Toda la interacción se hace escribiendo números o texto y pulsando **Enter**. Los identificadores (ID) son **UUID** únicos: para obtenerlos, usa primero las opciones *List all…* de cada menú y cópialos.

### 7.1 Arranque

```bash
mvnw.cmd compile exec:java "-Dexec.mainClass=org.example.App"
```

Al arrancar, la aplicación **carga datos de ejemplo automáticamente** (5 personas, 3 coches, 5 perros y 7 adopciones), de modo que se puede probar sin crear nada antes. El resumen de siembra indica explícitamente los ejemplos de custodia compartida.

### 7.2 Menú principal

```
===== MAIN MENU =====
1. Person operations
2. Dog operations
3. Car operations
4. Buy a car (person to person)
5. View car transactions (read only)
6. Adopt a dog (shared custody)
7. Return a dog (end one adoption)
8. View dog adoptions (read only)
9. Save repository to JSON file
10. Load repository from JSON file
11. Quit
```

- **1–3** abren los submenús CRUD de personas, perros y coches.
- **4** compra un coche entre dos personas.
- **5** consulta el historial de transacciones de coche (solo lectura).
- **6–8** son las operaciones del many-to-many.
- **9–10** guardan y cargan el repositorio en `repository.json`.
- **11** sale de la aplicación.

### 7.3 Operaciones básicas (Personas / Perros / Coches)

Los tres submenús comparten la misma estructura: **crear**, **listar**, **buscar por id**, **actualizar** y **borrar**. En las actualizaciones, dejar un campo en blanco (o `-1` en números) conserva el valor anterior.

Al **borrar una persona** o un **perro** que tenga adopciones, la aplicación cierra automáticamente sus vínculos y marca esas adopciones como devueltas. Los registros **no se borran**: quedan en el historial.

### 7.4 Adopción compartida (opción 6)

1. Introduce el **id de la persona** adoptante.
2. Introduce el **id del perro**.
3. Introduce la **cuota** (fee) de adopción.
4. La aplicación valida las reglas y confirma con un mensaje.

Reglas que se comprueban:

- La persona y el perro deben existir.
- La persona debe ser **adulta** (edad ≥ 18).
- La cuota debe ser **≥ 0**.
- No puede repetirse un **par activo** persona-perro.
- Una persona no puede tener **más de 3 perros** activos.
- Un perro no puede tener **más de 2 dueños** activos.

### 7.5 Devolución (opción 7)

1. Introduce el **id de la persona**.
2. Introduce el **id del perro**.

Cierra **únicamente** la adopción de ese par: el perro puede seguir teniendo otros dueños y la persona otros perros. El registro no se borra; se marca como `RETURNED` y se añade `" - RETURNED"` al contrato, quedando en el historial.

### 7.6 Consultas de custodia (opción 8)

Las opciones **6** (adoptar) y **7** (devolver) son acciones directas que piden los datos por pantalla. La opción **8** abre un submenú de solo lectura:

```
===== DOG ADOPTIONS / CUSTODY (READ ONLY) =====
1. List all adoptions
2. Find adoption by ID
3. List dogs of a person (by ID)
4. List owners of a dog (by ID)
5. Back
```

- **1** lista el historial completo de adopciones (activas y devueltas).
- **2** busca una adopción concreta por su id.
- **3** introduce el id de una persona → muestra sus perros.
- **4** introduce el id de un perro → muestra sus dueños.

### 7.7 Guardar y cargar datos (opciones 9 y 10)

- **9. Save repository to JSON file** — vuelca el estado completo del repositorio a un archivo `repository.json` en la carpeta desde la que se lanza la aplicación.
- **10. Load repository from JSON file** — lee ese archivo y **sustituye** el repositorio en memoria por su contenido.

Permite simular persistencia entre sesiones: guardar, salir, volver a arrancar y cargar.

La implementación es `JsonStore` (`save` / `load`), con cobertura de pruebas de ida y vuelta (*round-trip*) en `JsonStoreTest`.

### 7.8 Ejemplo de sesión

```
===== MAIN MENU =====
Select an option: 6
Enter adopter person ID: <id-persona>
Enter dog ID: <id-perro>
Enter adoption fee: 50
SUCCESS: Dog adopted for 50

Select an option: 7
Enter person ID: <id-persona>
Enter dog ID: <id-perro>
SUCCESS: Dog returned successfully

Select an option: 9
Repository saved to: <ruta>\repository.json
```

---

## 8. Estado en control de versiones

- Rama: **`full-many-to-many`**
- Contiene la implementación completa del ejercicio más la persistencia JSON, con el modelo definitivo (sin versiones provisionales) y las 76 pruebas en verde.
