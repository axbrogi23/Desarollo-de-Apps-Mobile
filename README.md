# VetPetCare

Proyecto de consola desarrollado en Kotlin para gestionar
10 boxes de atención veterinaria.

## Funcionalidades:

- Registro de pacientes caninos, felinos y exóticos.
- Entradas y salidas con corrutinas.
- Control de estados y disponibilidad de boxes.
- Cálculo de tarifas, descuentos, recargo silvestre e IVA.
- Emisión de tickets e historial de atenciones.
- Consultas de recaudación y reporte de cierre.
- Validación de datos y manejo de errores.

## Requisitos:

- IntelliJ IDEA.
- JDK 25, según la configuración del proyecto.

## Ejecución:

1. Clonar o descargar el repositorio.
2. Abrir el proyecto en IntelliJ IDEA.
3. Esperar la sincronización de Gradle.
4. Ejecutar la función main de Main.kt.
5. Utilizar las opciones del menú en la consola.

## Consideraciones:

- Los códigos de atención tienen dos letras, dos números
  y dos letras. Ejemplo: CA12CD.
- La entrada simula una espera de 3 segundos.
- La salida simula una espera de 6,5 segundos.
- Los minutos de atención se ingresan manualmente para
  simular los casos de prueba.
- El historial se conserva únicamente durante la ejecución.
- El cierre espera las operaciones pendientes y muestra
  las atenciones finalizadas.

## Casos de prueba:

| Paciente | Dueño | Minutos | Total esperado |
|---|---|---:|---:|
| Canino | Convenio | 75 | $14.280 |
| Felino | Particular | 18 | $0 |
| Exótico silvestre | Municipal | 120 | $30.940 |


## Asignatura

DSY1105 — Desarrollo de Aplicaciones Móviles.
