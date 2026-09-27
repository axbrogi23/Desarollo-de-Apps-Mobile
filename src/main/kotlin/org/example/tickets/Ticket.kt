package org.example.tickets

import org.example.Paciente
import org.example.boxes.EstadoBox

data class Ticket (
    val numero: Int,
    val paciente: Paciente,
    val minutosAtencion: Long,
    val montoPagado: Double
) {
    fun mostrarDetalle(){
        println("Ticket: $numero")
        println("Tipo: ${paciente.javaClass.simpleName}")
        println("Paciente: ${paciente.nombre}")
        println("Codigo: ${paciente.codigoAtencion}")
        println("Tiempo de la atencion: $minutosAtencion Minutos")
        println("Monto pagado: $montoPagado")
    }
}