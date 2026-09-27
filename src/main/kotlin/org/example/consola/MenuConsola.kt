package org.example.consola

import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.Canino
import org.example.Exotico
import org.example.Felino
import org.example.Paciente
import org.example.TipoDueno
import org.example.boxes.GestorBoxes
import org.example.consultas.ConsultasTurno
import org.example.reportes.TurnoReporte

object MenuConsola {

    suspend fun ejecutar() = coroutineScope {
        val gestor = GestorBoxes()
        val operaciones = mutableListOf<Job>()
        var continuar = true

        println("Bienvenido a PetCare")

        while (continuar) {
            println("--- Menu Principal ---")
            println("1. Registrar entrada")
            println("2. Registrar salida")
            println("2. Registrar salida")
            println("3. Mostrar boxes")
            println("4. Consultar turno")
            println("0. Cerrar turno")

            try {
                when (leerTexto("Selecciona una opcion:")) {
                    "1" -> {
                        val paciente = solicitarPaciente()

                        // La entrada avanza mientras el menú sigue disponible.
                        val operacion = launch {
                            try {
                                gestor.registrarEntrada(paciente)
                            } catch (error: IllegalArgumentException) {
                                println("Error de entrada: ${error.message}")
                            }
                        }

                        operaciones.add(operacion)
                        println("Entrada solicitada.")
                    }

                    "2" -> {
                        val codigo = leerTexto("Código de atencion:")
                            .uppercase()

                        val minutos = leerTexto(
                            "Minutos de atencion (simulados para esta prueba):"
                        ).toLongOrNull()

                        require(minutos != null && minutos >= 0) {
                            "Ingresa una cantidad entera de minutos, no negativa."
                        }

                        val operacion = launch {
                            try {
                                gestor.registrarSalida(codigo, minutos)
                            } catch (error: IllegalArgumentException) {
                                println("Error de salida: ${error.message}")
                            }
                        }

                        operaciones.add(operacion)
                        println("Salida solicitada.")
                    }

                    "3" -> {
                        gestor.mostrarBoxes()
                        println(
                            "Boxes disponibles: ${gestor.disponibilidades()}"
                        )
                    }

                    "4" -> mostrarConsultas(gestor)

                    "0" -> continuar = false

                    else -> println("la Opcion ingresada es invalida porfavor, intentalo nuevamente")
                }

            } catch (error: IllegalArgumentException) {
                // El error afecta a esta operación, no termina el menú.
                println("Error de datos: ${error.message}")

            } catch (error: FinDeEntrada) {
                println("Se cerro la entrada de consola.")
                continuar = false
            }
        }

        println("Esperando que terminen las operaciones pendientes...")

        // No generamos el cierre mientras los sensores siguen procesando.
        operaciones.joinAll()

        TurnoReporte.mostrar(gestor)
        println("Turno finalizado.")
    }

    private suspend fun solicitarPaciente(): Paciente {
        println("Tipo de paciente:")
        println("1. Canino")
        println("2. Felino")
        println("3. Exotico")

        val tipo = leerTexto("Selecciona el tipo:")

        require(tipo in listOf("1", "2", "3")) {
            "El tipo de paciente no es valido."
        }

        val codigo = leerTexto("Codigo de atencion:").uppercase()
        val nombre = leerTexto("Nombre de la mascota:")
        val especie = leerTexto("Especie o raza:")
        val tipoDueno = solicitarTipoDueno()

        // Registramos la fecha y hora al crear al paciente.
        return when (tipo) {
            "1" -> Canino(
                codigoAtencion = codigo,
                nombre = nombre,
                especie = especie,
                fechaIngreso = LocalDateTime.now(),
                tipoDueno = tipoDueno
            )

            "2" -> Felino(
                codigoAtencion = codigo,
                nombre = nombre,
                especie = especie,
                fechaIngreso = LocalDateTime.now(),
                tipoDueno = tipoDueno
            )

            else -> {
                val respuesta = leerTexto(
                    "Es silvestre?, Escribe s para sí o n para no:"
                ).lowercase()

                require(respuesta == "s" || respuesta == "n") {
                    "Debe de responder s o n."
                }

                Exotico(
                    codigoAtencion = codigo,
                    nombre = nombre,
                    especie = especie,
                    fechaIngreso = LocalDateTime.now(),
                    tipoDueno = tipoDueno,
                    esSilvestre = respuesta == "s"
                )
            }
        }
    }

    private suspend fun solicitarTipoDueno(): TipoDueno {
        println("Tipo de dueño:")
        println("1. Particular")
        println("2. Convenio")
        println("3. Municipal")

        val nombreTipo = when (leerTexto("Selecciona el tipo de dueño:")) {
            "1" -> "Particular"
            "2" -> "Convenio"
            "3" -> "Municipal"
            else -> throw IllegalArgumentException(
                "El tipo de dueño no es valido."
            )
        }

        // Acepta nombres del enum como Convenio o CONVENIO.
        return TipoDueno.values().firstOrNull {
            it.name.equals(nombreTipo, ignoreCase = true)
        } ?: throw IllegalArgumentException(
            "No se encontró el tipo $nombreTipo en TipoDueno."
        )
    }

    private fun mostrarConsultas(gestor: GestorBoxes) {
        val consultas = ConsultasTurno(gestor.obtenerHistorial())

        println("--- CONSULTAS DEL TURNO ---")
        println("Boxes disponibles: ${gestor.disponibilidades()}")
        println("Atenciones finalizadas: ${consultas.cantidadAtendidos()}")
        println("Total recaudado: ${consultas.recaudadoTotal()}")
        println("Ingreso promedio: ${consultas.ingresoPromedio()}")

        println("Ingresos por tipo:")
        consultas.ingresosPorTipo().forEach { (tipo, monto) ->
            println("$tipo: $monto")
        }

        val convenio = consultas.pacientesConConvenio()

        println("Pacientes finalizados con convenio:")
        if (convenio.isEmpty()) {
            println("Ninguno.")
        } else {
            convenio.forEach {
                println("${it.nombre} (${it.codigoAtencion})")
            }
        }

        val codigos = consultas.codigosFinalizados()
        println(
            "codigos finalizados: " +
                    if (codigos.isEmpty()) "ninguno." else codigos.joinToString()
        )

        val mayor = consultas.atencionMasLarga()
        if (mayor == null) {
            println("Atencion mas larga: sin datos.")
        } else {
            println(
                "Atencion mas larga: ${mayor.paciente.nombre} " +
                        "(${mayor.paciente.codigoAtencion}), " +
                        "${mayor.minutosAtencion} minutos"
            )
        }
    }

    private suspend fun leerTexto(mensaje: String): String {
        println(mensaje)

        // Leer el teclado puede bloquear: lo hacemos en un hilo de entrada/salida.
        // Así las corrutinas de los sensores pueden seguir avanzando.
        return withContext(Dispatchers.IO) {
            readlnOrNull()?.trim() ?: throw FinDeEntrada()
        }
    }

    private class FinDeEntrada : Exception()
}