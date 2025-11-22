# 🏋️‍♂️ Gestor del Club Deportivo — DAMA Sports
Aplicación de escritorio en **Java + JavaFX** para la gestión integral de *socios*, *pistas* y *reservas*, con persistencia en **MariaDB/MySQL** mediante **JDBC**.

Desarrollado como parte de la **Actividad de Entregar — UT2 (Acceso a Datos)**.

---

## 👥 Equipo de Desarrollo
| Nombre | Rol |
|--------|------|
| **Luis** | Responsable de Persistencia (JDBC + DAOs + conexión a la BBDD) |
| **Javi** | Responsable de Lógica de Negocio (Clase ClubDeportivo + validaciones + reglas) |

Las vistas JavaFX fueron proporcionadas por la profesora y se han adaptado para integrarse con la lógica del proyecto.

---

## 📦 Estructura del Proyecto

src/
└── main/java/
└── (paquete base)/
├── modelo/ ← Clases Socio, Pista, Reserva
├── dao/ ← DAOs definitivos con JDBC
├── servicio/ ← Clase ClubDeportivo (reglas de negocio)
├── util/ ← DBConnection.java, TestDB.java
└── vista/ ← Interfaz JavaFX proporcionada
data/
└── script.sql ← Tablas + función + procedimiento almacenado
docs/
└── presentacion.pdf ← Presentación final del proyecto
README.md

---

## 🗄️ Base de Datos: MariaDB / MySQL
Todos los scripts necesarios se encuentran en:
/data/script.sql

Incluye:

- Creación de tablas:
    - `socios`
    - `pistas`
    - `reservas`
- Procedimiento almacenado `sp_crear_reserva`
- Función `calcular_precio`
- Inserts opcionales

### 🔌 Conexión JDBC
La conexión se gestiona mediante `DBConnection.java`, usando:
com.mysql.cj.jdbc.Driver

Ajustar los valores de URL, usuario y contraseña según vuestra configuración local.

---

## 🧠 Lógica de Negocio (Clase ClubDeportivo)

La lógica está completamente separada de la interfaz siguiendo buenas prácticas:

- Alta, baja y modificación de socios
- Gestión de disponibilidad de pistas
- Validación de reservas:
    - no solapes
    - fechas/hora válidas
    - socio existente
    - pista disponible
    - duración mínima
- Cancelación de reservas
- Uso del procedimiento almacenado y función para el cálculo automático del precio
- Control de que *un socio con reservas futuras no puede darse de baja*

---

## 🖥️ Vista (JavaFX)
La interfaz incluye:

- Menú principal (Dashboard)
- Gestión de socios
- Gestión de pistas
- Formularios de reserva
- Cancelación de reservas

Totalmente conectada con la lógica del servicio.

---

## ▶️ Cómo Ejecutar el Proyecto

1. Crear la base de datos en MariaDB/MySQL.
2. Importar el script:
   /data/script.sql
3. Configurar tu usuario/contraseña en `DBConnection.java`
4. Ejecutar la aplicación desde:
   vista/MainApp.java

---

## 🔀 Flujo de Trabajo (Git)

El equipo ha seguido un flujo basado en ramas:

- `luis-dev` → desarrollo de persistencia
- `javi-dev` → desarrollo de lógica de negocio
- `main` → rama principal estable

Los cambios se integraron mediante *pull requests*, con commits descriptivos siguiendo la convención:
- feat: añadir alta de socio 
- fix: corregir validación de reserva 
- refactor: limpiar lógica de ClubDeportivo 
- docs: añadir README y presentación

---

## 🧪 Pruebas

Se realizaron pruebas manuales desde la interfaz:

### ✔ Caso exitoso
- Alta de socio
- Reserva válida (sin solape)
- Cálculo correcto del precio vía función SQL

### ❌ Caso con error
- Reserva que se solapa con otra existente → rechazo
- Baja de socio con reservas futuras → bloqueo
- Hora/duración incorrectas → mensaje de error

---

## 📄 Presentación

Disponible en:
/docs/presentacion.pdf

Incluye:

- Equipo y roles
- Modelo UML
- Arquitectura
- Lógica aplicada
- Persistencia (DAOs + SQL)
- Git Flow
- Demo final

---

## 📜 Licencia
Proyecto académico — I.E.S. Vicente Medina.







