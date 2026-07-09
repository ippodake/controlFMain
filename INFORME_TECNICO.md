# Auditoría Técnica del Proyecto

## 1. Resumen

- Objetivo del proyecto: construir una plataforma de auditoría ciudadana para visualizar leyes, políticos y su coherencia legislativa, con un panel administrativo y un flujo de importación de datos desde la Asamblea Nacional. Esta intención es observable en los controladores, servicios y páginas del frontend.
- Estado actual y porcentaje aproximado de avance: el proyecto presenta una base funcional avanzada para un MVP. Tras las mejoras verificadas, el avance funcional se estima en torno al 85–90%. El backend compila y prueba correctamente, y el frontend compila correctamente tras corregir los errores de TypeScript.
- Cambios recientes del backend: se consolidaron cinco funcionalidades de negocio reutilizando el modelo existente: historial de actualizaciones para políticos, clasificación/cambio de categoría de leyes, actualización del estado real de una ley, registro de asistencia a votaciones y un resumen histórico de leyes y votos. La suite de regresión quedó en verde y se desactivó el poblado automático al arrancar la aplicación.
- Cambios recientes del frontend: se incorporó una UI mínima funcional para editar patrimonio/antecedentes del político, gestionar categoría/estado de una ley y actualizar asistencia de votos desde los endpoints reales. También se agregó un bloque de reporte histórico en el panel administrativo.
- Principales fortalezas:
  - Separación clara entre capas en el backend: controladores, servicios, repositorios y DTOs.
  - Modelo de dominio visible para leyes, políticos, promesas, votos, comentarios y coherencia.
  - Estructura de frontend con páginas y componentes por módulo, además de rutas definidas.
  - Integración con una API externa para importación de votaciones y de candidatos.
- Problemas y riesgos:
  - No existe autenticación ni autorización real.
  - La configuración de base de datos y secretos sigue en texto plano en el código.
  - El uso de IDs de usuario es todavía simplificado y no está ligado a identidad real.
  - Queda lógica de negocio y mapeo DTO mezclada en algunos servicios.
  - El flujo de seguridad aún es muy básico para un sistema con datos sensibles.

> Verificación realizada: el backend compila y ejecuta las pruebas con ./gradlew test, y el frontend compila con npm run build. Verificación reciente del bloque backend: ./gradlew test --tests com.controlf.backend.BackendWorkflowTests -> BUILD SUCCESSFUL. Verificación reciente del frontend: npm run build en controlf_fronted -> BUILD SUCCESSFUL.

## 2. Arquitectura

- Arquitectura implementada: arquitectura en capas inspirada en MVC/Layered Architecture, con Spring Boot en el backend y React/Vite en el frontend. No se observan patrones de Clean Architecture ni Hexagonal Architecture con puertos/adaptadores explícitos.
- Justificación:
  - El código separa claramente controladores, servicios y persistencia.
  - Los DTOs encapsulan la respuesta de la API.
  - El modelo de negocio se representa con entidades JPA y repositorios Spring Data.
- Organización de capas o módulos:
  - Presentación: frontend React + React Router.
  - API: controladores Spring en [controlF/src/main/java/com/controlf/controller](controlF/src/main/java/com/controlf/controller).
  - Aplicación: servicios en [controlF/src/main/java/com/controlf/service](controlF/src/main/java/com/controlf/service).
  - Persistencia: repositorios en [controlF/src/main/java/com/controlf/db/repository](controlF/src/main/java/com/controlf/db/repository).
  - Dominio: entidades en [controlF/src/main/java/com/controlf/db/schema](controlF/src/main/java/com/controlf/db/schema).
- Diagrama textual simple:

```text
Frontend React/Vite
  -> Rutas y páginas
  -> Fetch a /api/... y /admin/...
  -> Controladores Spring
  -> Servicios
  -> Repositorios JPA
  -> PostgreSQL
  -> API externa de la Asamblea Nacional
```

## 3. Tecnologías

- Backend:
  - Java 21.
  - Spring Boot 4.0.6.
  - Spring Web.
  - Spring Data JPA / Hibernate.
  - PostgreSQL.
  - Lombok.
  - JUnit 5.
  - Bean Validation via spring-boot-starter-validation.
- Frontend:
  - React 19.2.5.
  - TypeScript ~6.0.2.
  - Vite 8.0.10.
  - React Router DOM 7.14.2.
  - Tailwind CSS 4.3.1.
- Configuración y herramientas:
  - [controlF/build.gradle.kts](controlF/build.gradle.kts)
  - [controlf_fronted/package.json](controlf_fronted/package.json)
  - [controlF/src/main/resources/application.properties](controlF/src/main/resources/application.properties)

## 4. Estructura del Proyecto

- Backend:
  - [controlF/src/main/java/com/controlf/controller](controlF/src/main/java/com/controlf/controller): endpoints HTTP y rutas REST.
  - [controlF/src/main/java/com/controlf/service](controlF/src/main/java/com/controlf/service): lógica de negocio y operaciones de dominio.
  - [controlF/src/main/java/com/controlf/db](controlF/src/main/java/com/controlf/db): repositorios y entidades JPA.
  - [controlF/src/main/java/com/controlf/dto](controlF/src/main/java/com/controlf/dto): objetos de transferencia para requests y responses.
  - [controlF/src/main/resources](controlF/src/main/resources): configuración de la aplicación y recursos estáticos.
- Frontend:
  - [controlf_fronted/src/componentes](controlf_fronted/src/componentes): páginas y componentes por módulo.
  - [controlf_fronted/src/App.tsx](controlf_fronted/src/App.tsx): definición de rutas.
  - [controlf_fronted/src/main.tsx](controlf_fronted/src/main.tsx): inicialización de la aplicación.

## 5. Módulos y Funcionalidades

| Módulo | Propósito | Funcionalidades | Estado |
|---|---|---|---|
| Directorio de políticos | Explorar políticos | Listado, filtros, paginación y perfil | Parcial |
| Perfil político | Detalle del político | Información básica, coherencia, historial, comentarios y calificaciones | Parcial |
| Directorio de leyes | Explorar leyes | Listado, filtros, paginación y detalle | Parcial |
| Perfil de ley | Detalle de ley | Contenido, votación, auditoría y debate ciudadano | Parcial |
| Dashboard | Resumen general | Estadísticas y actividad reciente | Parcial |
| Panel administrativo | Operaciones de administración | Panel, motor de coherencia, mantenimiento, seed e importación | Parcial |
| Importación externa | Traer datos desde la Asamblea | Obtener asambleístas, votaciones y cargar datos por candidatos | Parcial |

Observación verificable: estos módulos existen y están conectados a endpoints reales del backend.

## 6. Cambios recientes del backend y frontend (julio 2026)

- Historial de patrimonio/antecedentes del político: se implementó el registro de cambios en un campo de texto estructurado en JSON, reutilizando el esquema existente de [controlF/src/main/java/com/controlf/db/schema/Politico.java](controlF/src/main/java/com/controlf/db/schema/Politico.java) y la actualización en [controlF/src/main/java/com/controlf/service/PoliticoService.java](controlF/src/main/java/com/controlf/service/PoliticoService.java).
- Clasificación de leyes por categoría: se reutilizó el campo existente de categoría en [controlF/src/main/java/com/controlf/db/schema/Ley.java](controlF/src/main/java/com/controlf/db/schema/Ley.java) y se expuso su actualización desde [controlF/src/main/java/com/controlf/controller/LeyController.java](controlF/src/main/java/com/controlf/controller/LeyController.java) y [controlF/src/main/java/com/controlf/service/LeyService.java](controlF/src/main/java/com/controlf/service/LeyService.java).
- Estado real de la ley: se incorporó la actualización del estado usando el enum existente [controlF/src/main/java/com/controlf/db/schema/enums/EstadoLey.java](controlF/src/main/java/com/controlf/db/schema/enums/EstadoLey.java) a través del mismo flujo de servicio/controlador.
- Asistencia a votaciones: se reutilizó el campo asistencia en [controlF/src/main/java/com/controlf/db/schema/Voto.java](controlF/src/main/java/com/controlf/db/schema/Voto.java) para permitir su actualización por ley y voto.
- Reporte histórico: se añadió la agregación de totales y conteos por estado/tipo de voto desde [controlF/src/main/java/com/controlf/service/AdminService.java](controlF/src/main/java/com/controlf/service/AdminService.java), expuesta en [controlF/src/main/java/com/controlf/controller/AdminController.java](controlF/src/main/java/com/controlf/controller/AdminController.java).
- Poblado inicial al arrancar la app: se desactivó el seed automático en [controlF/src/main/java/com/controlf/ControlFApplication.java](controlF/src/main/java/com/controlf/ControlFApplication.java) para evitar datos de ejemplo al iniciar el sistema. Para pruebas se añadió configuración específica en [controlF/build.gradle.kts](controlF/build.gradle.kts) y [controlF/src/test/resources/application.properties](controlF/src/test/resources/application.properties).
- Pruebas de regresión: se añadió y verificó [controlF/src/test/java/com/controlf/backend/BackendWorkflowTests.java](controlF/src/test/java/com/controlf/backend/BackendWorkflowTests.java), que cubre historial, categoría/estado y resumen histórico.
- UI mínima operativa: se implementó edición básica desde la interfaz para los siguientes flujos conectados a los endpoints reales:
  - [controlf_fronted/src/componentes/perfil_politico_screen/PerfilPoliticoPage.tsx](controlf_fronted/src/componentes/perfil_politico_screen/PerfilPoliticoPage.tsx): edición de patrimonio y antecedentes del político.
  - [controlf_fronted/src/componentes/perfil_ley/PerfilLeyPage.tsx](controlf_fronted/src/componentes/perfil_ley/PerfilLeyPage.tsx): edición de categoría, estado y asistencia a votaciones de una ley.
  - [controlf_fronted/src/componentes/panel_admin/AdminPage.tsx](controlf_fronted/src/componentes/panel_admin/AdminPage.tsx): visualización del reporte histórico agregado en tarjetas.

## 7. Frontend

- Páginas:
  - [controlf_fronted/src/componentes/DashboardPage.tsx](controlf_fronted/src/componentes/DashboardPage.tsx)
  - [controlf_fronted/src/componentes/directorio_politicos/DirectorioPoliticosPage.tsx](controlf_fronted/src/componentes/directorio_politicos/DirectorioPoliticosPage.tsx)
  - [controlf_fronted/src/componentes/directorio_leyes/DirectorioLeyesPage.tsx](controlf_fronted/src/componentes/directorio_leyes/DirectorioLeyesPage.tsx)
  - [controlf_fronted/src/componentes/perfil_politico_screen/PerfilPoliticoPage.tsx](controlf_fronted/src/componentes/perfil_politico_screen/PerfilPoliticoPage.tsx)
  - [controlf_fronted/src/componentes/perfil_ley/PerfilLeyPage.tsx](controlf_fronted/src/componentes/perfil_ley/PerfilLeyPage.tsx)
  - [controlf_fronted/src/componentes/panel_admin/AdminPage.tsx](controlf_fronted/src/componentes/panel_admin/AdminPage.tsx)
- Componentes:
  - El frontend está estructurado por dominio y cuenta con subcomponentes reutilizables para directorios, perfiles y panel administrativo.
- Hooks, Context y Store:
  - No se encontró un store global ni Context API. El estado se gestiona con useState y useEffect en las páginas y componentes.
- Manejo de estado:
  - El estado es local y component-level. No se observan mecanismos de sincronización global ni persistencia.
- Navegación y rutas:
  - Definidas en [controlf_fronted/src/App.tsx](controlf_fronted/src/App.tsx): /, /dashboard, /politico/:id, /leyes, /ley/:id y /admin.
- Consumo de APIs:
  - Se usa fetch directo en los componentes. Ejemplos: [controlf_fronted/src/componentes/directorio_politicos/DirectorioPoliticosPage.tsx](controlf_fronted/src/componentes/directorio_politicos/DirectorioPoliticosPage.tsx) y [controlf_fronted/src/componentes/perfil_politico_screen/PerfilPoliticoPage.tsx](controlf_fronted/src/componentes/perfil_politico_screen/PerfilPoliticoPage.tsx).

## 7. Backend

- Endpoints disponibles:
  - Leyes: [controlF/src/main/java/com/controlf/controller/LeyController.java](controlF/src/main/java/com/controlf/controller/LeyController.java)
  - Políticos: [controlF/src/main/java/com/controlf/controller/PoliticoController.java](controlF/src/main/java/com/controlf/controller/PoliticoController.java)
  - Dashboard: [controlF/src/main/java/com/controlf/controller/DashboardController.java](controlF/src/main/java/com/controlf/controller/DashboardController.java)
  - Admin: [controlF/src/main/java/com/controlf/controller/AdminController.java](controlF/src/main/java/com/controlf/controller/AdminController.java)
  - Importación: [controlF/src/main/java/com/controlf/controller/AssemblyImportController.java](controlF/src/main/java/com/controlf/controller/AssemblyImportController.java) y [controlF/src/main/java/com/controlf/controller/PoliticoImportController.java](controlF/src/main/java/com/controlf/controller/PoliticoImportController.java)
- Controladores:
  - Exponen endpoints REST con @RestController y rutas agrupadas con @RequestMapping.
- Servicios:
  - [controlF/src/main/java/com/controlf/service/LeyService.java](controlF/src/main/java/com/controlf/service/LeyService.java)
  - [controlF/src/main/java/com/controlf/service/PoliticoService.java](controlF/src/main/java/com/controlf/service/PoliticoService.java)
  - [controlF/src/main/java/com/controlf/service/AdminService.java](controlF/src/main/java/com/controlf/service/AdminService.java)
  - [controlF/src/main/java/com/controlf/service/AssemblyImportService.java](controlF/src/main/java/com/controlf/service/AssemblyImportService.java)
  - [controlF/src/main/java/com/controlf/service/PoliticoImportService.java](controlF/src/main/java/com/controlf/service/PoliticoImportService.java)
- Casos de uso implementados:
  - Consulta de leyes y políticos.
  - Creación de comentarios y calificaciones.
  - Cálculo de coherencia y métricas básicas.
  - Importación masiva de datos desde la Asamblea.
  - Gestión administrativa básica.
  - Actualización de categoría, estado y asistencia en leyes/votos.
  - Registro de historial de cambios para datos de políticos.
  - Resumen histórico y agregaciones administrativas.
- Repositorios:
  - Se usa Spring Data JPA a través de interfaces como [controlF/src/main/java/com/controlf/db/repository/LeyRepository.java](controlF/src/main/java/com/controlf/db/repository/LeyRepository.java) y [controlF/src/main/java/com/controlf/db/repository/PoliticoRepository.java](controlF/src/main/java/com/controlf/db/repository/PoliticoRepository.java).
- Entidades:
  - Se observan entidades/objetos de esquema para leyes, políticos, promesas, vínculos, usuarios, votos, comentarios y calificaciones en [controlF/src/main/java/com/controlf/db/schema](controlF/src/main/java/com/controlf/db/schema).
- DTOs:
  - Existe una colección amplia de DTOs en [controlF/src/main/java/com/controlf/dto](controlF/src/main/java/com/controlf/dto) para requests y responses.
- Middleware:
  - No se detecta un stack de middleware propio de seguridad; sí existe configuración de CORS en [controlF/src/main/java/com/controlf/WebConfig.java](controlF/src/main/java/com/controlf/WebConfig.java).
- Autenticación y autorización:
  - No se encontró implementación real de JWT, filtros, roles ni sesiones de usuario. La seguridad de Spring está desactivada explícitamente en [controlF/src/main/resources/application.properties](controlF/src/main/resources/application.properties).

## 8. Base de Datos y APIs

- Modelos o tablas observables a partir del código:
  - Leyes, políticos, promesas, votos, usuarios, comentarios, calificaciones, vínculos promesa-ley, configuraciones y registros de sistema.
- Relaciones observables:
  - Un político tiene promesas y votos.
  - Una promesa puede vincularse a varias leyes.
  - Una ley tiene votos, comentarios y calificaciones.
  - Un usuario participa en comentarios y calificaciones.
- ORM utilizado:
  - Hibernate/JPA mediante Spring Data JPA.
- Resumen de endpoints disponibles:
  - GET /api/leyes, GET /api/leyes/filtros, GET /api/leyes/{id}/perfil, POST /api/leyes/{id}/comentarios, POST /api/leyes/{id}/calificaciones
  - GET /api/politicos, GET /api/politicos/filtros, GET /api/politicos/{id}, POST /api/politicos/{id}/comentarios, POST /api/politicos/{id}/calificaciones
  - GET /api/dashboard/stats
  - GET /api/admin/..., POST /api/admin/...
  - GET /admin/assembly-members, POST /admin/import-leyes

## 9. Patrones y Calidad del Código

- Patrones observados:
  - Repository Pattern.
  - Service Layer.
  - DTOs para transporte.
  - Dependency Injection con Lombok + Spring.
- Patrones de diseño detectados:
  - No se observan Factory, Strategy o Singleton explícitos en el código base inspeccionado.
- Uso de SOLID:
  - Hay separación razonable entre controladores, servicios y persistencia, aunque todavía existe mezcla entre lógica de negocio y mapeo DTO en algunos servicios.
  - No se observa una separación estricta entre dominio, aplicación e infraestructura.
- Organización del código:
  - Adecuada para un MVP o prototipo, pero todavía poco madura para un sistema empresarial.
- Acoplamiento y cohesión:
  - La cohesión por módulo es razonable.
  - El acoplamiento entre frontend y backend es alto porque los componentes consumen directamente los endpoint y esperan estructuras concretas.

## 10. Seguridad y Configuración

- Variables de entorno:
  - No se detectan variables de entorno para base de datos ni secretos; la configuración actual está en [controlF/src/main/resources/application.properties](controlF/src/main/resources/application.properties).
- Gestión de secretos:
  - No se encontró un gestor de secretos ni placeholders seguros.
- JWT/Auth:
  - No se encontró implementación real de JWT ni autenticación de usuarios.
- Validaciones:
  - Se añadieron validaciones con Bean Validation en DTOs y controladores relevantes.
  - Aún no se observa un manejo global de excepciones ni un control transaccional robusto.
- Riesgos de seguridad detectados:
  - Seguridad de Spring desactivada explícitamente.
  - Credenciales de base de datos en texto plano.
  - CORS configurado desde una propiedad, pero aún no se ha implementado una política por ambiente ni autorización por rol.
  - IDs de usuario simplificados y no ligados a una identidad real.

## 11. Deuda Técnica

Prioridad alta:
- Implementar autenticación y autorización reales.
- Mover credenciales y secretos a variables de entorno o un gestor seguro.
- Sustituir los IDs de usuario simplificados por identidad real y control de acceso.

Prioridad media:
- Mejorar el manejo global de excepciones y los mensajes de error.
- Separar mejor el mapeo DTO de la lógica de negocio en módulos adicionales.
- Revisar el flujo de filtros y búsqueda en el frontend para evitar inconsistencias.

Prioridad baja:
- Limpiar interfaces y DTOs redundantes o poco usados.
- Revisar comentarios temporales y código de ejemplo.
- Mejorar la calidad de los mensajes y del feedback de la interfaz.

Elementos de deuda observados en el código:
- Comentarios simplificados y métricas aproximadas en [controlF/src/main/java/com/controlf/service/DashboardService.java](controlF/src/main/java/com/controlf/service/DashboardService.java).
- Código de importación con lógica muy directa en [controlF/src/main/java/com/controlf/service/AssemblyImportService.java](controlF/src/main/java/com/controlf/service/AssemblyImportService.java).
- Algunos endpoints todavía usan respuestas simples y no un manejo uniforme de errores.

## 12. Conclusión

| Área | Estado |
|---|---|
| Frontend | Parcialmente sólido |
| Backend | Parcialmente sólido |
| Base de datos | Parcial |
| APIs | Parcialmente funcionales |
| Seguridad | Incompleto |
| Arquitectura | Sólida para un MVP |
| Calidad del código | Media |

- ¿El proyecto está listo para producción? No.
- ¿Qué aspectos faltan para considerarlo completo? Autenticación real, seguridad robusta, configuración externa y segura, manejo global de errores, tests más amplios y mayor madurez de integración.
- ¿Cuáles son las prioridades técnicas inmediatas? 1) autenticación y autorización, 2) secret management y configuración externa, 3) manejo de errores y validaciones transaccionales, 4) refactorización incremental de servicios y frontend.
