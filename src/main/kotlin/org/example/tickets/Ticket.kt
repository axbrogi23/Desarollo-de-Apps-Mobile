package org.example.tickets

import org.example.Paciente
import org.example.Exotico

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

        val pacienteActual = paciente

        if (pacienteActual is Exotico){
            println("Silvestre: ${if (pacienteActual.esSilvestre) "Si" else "No"}"
            )
        }

        println("Codigo: ${paciente.codigoAtencion}")
        println("Tiempo de la atencion: $minutosAtencion Minutos")
        println("Monto pagado: $montoPagado")
    }
}