package org.example.consultas

import org.example.Canino
import org.example.Exotico
import org.example.Felino
import org.example.Paciente
import org.example.TipoDueno
import org.example.tickets.Ticket

class ConsultasTurno(
    private val tickets: List<Ticket>
) {

    fun recaudadoTotal(): Double {
        return tickets.sumOf { it.montoPagado }
    }

    fun cantidadAtendidos(): Int {
        return tickets.size
    }

    fun ingresoPromedio(): Double {
        return if (tickets.isEmpty()) {
            0.0
        } else {
            recaudadoTotal() / tickets.size
        }
    }

    fun pacientesConConvenio(): List<Paciente> {
        return tickets
            .filter { it.paciente.tipoDueno == TipoDueno.Convenio }
            .map { it.paciente }
    }

    fun codigosFinalizados(): List<String> {
        return tickets.map { it.paciente.codigoAtencion }
    }

    fun atencionMasLarga(): Ticket? {
        return tickets.maxByOrNull { it.minutosAtencion }
    }

    fun ingresosPorTipo(): Map<String, Double> {
        return mapOf(
            "Canino" to tickets
                .filter { it.paciente is Canino }
                .sumOf { it.montoPagado },

            "Felino" to tickets
                .filter { it.paciente is Felino }
                .sumOf { it.montoPagado },

            "Exotico" to tickets
                .filter { it.paciente is Exotico }
                .sumOf { it.montoPagado }
        )
    }

    fun tiposConMayorIngreso(): List<String> {
        val ingresos = ingresosPorTipo()
        val mayorIngreso = ingresos.values.maxOrNull() ?: return emptyList()

        if (mayorIngreso == 0.0) {
            return emptyList()
        }

        return ingresos
            .filterValues { it == mayorIngreso }
            .keys
            .toList()
    }
}