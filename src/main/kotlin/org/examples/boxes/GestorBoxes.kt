package org.example.boxes

import org.example.Paciente
import kotlinx.coroutines.delay

import org.example.CalcularTarifa
import org.example.tickets.Ticket

class GestorBoxes {

        val boxes: List<Box> = List(10) { indice ->
        Box(numero = indice + 1)
    }

        val codigosRegistrados = mutableSetOf<String>()
        val bloqueo = Any()
        val historial = mutableListOf<Ticket>()

    fun mostrarBoxes() {
        synchronized(bloqueo) {
            boxes.forEach { box ->
                println(box.obtenerDetalle())
            }
        }
    }

    fun disponibilidades(): Int {
        return synchronized(bloqueo) {
            boxes.count { box ->
                box.estado == EstadoBox.Libre
            }
        }
    }

    fun buscarPrimerLibre(): Box? {
        return synchronized(bloqueo) {
            boxes.firstOrNull { box ->
                box.estado == EstadoBox.Libre
            }
        }
    }

    suspend fun registrarEntrada(paciente: Paciente) {
        val boxAsignado = synchronized(bloqueo) {
            if (paciente.codigoAtencion in codigosRegistrados) {
                println("Error: el código de atención ya fue registrado.")
                return
            }

            val box = boxes.firstOrNull {
                it.estado == EstadoBox.Libre
            }

            if (box == null) {
                println("No hay boxes libres disponibles.")
                return
            }

            // funcion para la reserva de box
            box.estado = EstadoBox.EnProceso("Registrando entrada")
            codigosRegistrados.add(paciente.codigoAtencion)

            println(box.obtenerDetalle())
            box
        }

        var entradaConfirmada = false

        try {
            // el tiempo de espera ocurre fuera del bloqueo
            delay(3000)

            synchronized(bloqueo) {
                boxAsignado.estado = EstadoBox.EnAtencion(paciente)
                entradaConfirmada = true

                println("Entrada confirmada: ${boxAsignado.obtenerDetalle()}")
            }

        } finally {
            // Si la entrada no se completa, se libera
            if (!entradaConfirmada) {
                synchronized(bloqueo) {
                    boxAsignado.estado = EstadoBox.Libre
                    codigosRegistrados.remove(paciente.codigoAtencion)
                }
            }
        }
    }

    suspend fun registrarSalida(
        codigoAtencion: String,
        minutosAtencion: Long
    ): Ticket? {
        val datosSalida = synchronized(bloqueo){
            val box = boxes.firstOrNull{box ->
                when (val estado = box.estado) {
                    is EstadoBox.EnAtencion -> estado.paciente.codigoAtencion == codigoAtencion

                    EstadoBox.Libre -> false
                    is EstadoBox.EnProceso -> false
                    is EstadoBox.FueraServicio -> false
                }
            }

            if (box == null){
                println("no hay un paciente en atencion disponible " +
                "para salir con codigo $codigoAtencion"
                )
                return null
            }

            val estadoAnterior = box.estado as EstadoBox.EnAtencion

            box.estado = EstadoBox.EnProceso("Calculando la tarifa")
            println(box.obtenerDetalle())

            Pair(box,estadoAnterior)
        }

        val (boxAsignado, estadoAnterior) = datosSalida
        var salidaConfirmada = false

        try {
            delay(6500)

            val monto = CalcularTarifa.calcularTotal(
                estadoAnterior.paciente,
                minutosAtencion
            )

            val ticket = synchronized(bloqueo){
                val nuevoTicket = Ticket(
                    numero = historial.size + 1,
                    paciente = estadoAnterior.paciente,
                    minutosAtencion = minutosAtencion,
                    montoPagado = monto
                )
                //funcion para registrar la atencion y liberar box
                historial.add(nuevoTicket)
                boxAsignado.estado = EstadoBox.Libre
                salidaConfirmada = true

                nuevoTicket
            }
            ticket.mostrarDetalle()
            println("Salida confirmada, el box ${boxAsignado.numero} esta libre")
            return ticket

        } catch (error: IllegalArgumentException) {
            println("Error al querer registrar la salida: ${error.message}")
            return null

        } finally {
            if(!salidaConfirmada) {
                synchronized(bloqueo){
                    boxAsignado.estado = estadoAnterior
                }
            }
        }
    }

    fun obtenerHistorial(): List<Ticket> {
        return synchronized(bloqueo) {
            historial.toList()
        }
    }

}