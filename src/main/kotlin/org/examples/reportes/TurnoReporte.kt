package org.example.reportes

import org.example.boxes.GestorBoxes
import org.example.consultas.ConsultasTurno

object TurnoReporte {

    fun mostrar(gestor: GestorBoxes) {
        val tickets = gestor.obtenerHistorial()
        val consultas = ConsultasTurno(tickets)

        println("Reporte de cierre PetCare")

        if (tickets.isEmpty()){
            println("No hay atenciones finalizadas")
        } else {
            tickets.forEach { ticket ->
                ticket.mostrarDetalle()
            }
        }

        println("Resumen del turno")
        println("El total recaudado: ${consultas.recaudadoTotal()}")
        println("Total de pacientes antendidos: ${consultas.cantidadAtendidos()}")
        println("Ingreso promedio: ${consultas.ingresoPromedio()}")

        println("Ingresos por tipo:")
        consultas.ingresosPorTipo().forEach {(tipo, monto) ->
            println("$tipo: $monto")
        }

        val tiposMayores = consultas.tiposConMayorIngreso()

        if (tiposMayores.isEmpty()){
            println("Tipo con mas ingresos: no hay ingresos positivos")
        } else {
            println("Tipo con mas ingresos: ${tiposMayores.joinToString()}"
            )
        }

        println("Boxes disponibles al cierre: ${gestor.disponibilidades()}")

        println("Pacientes atendidos con convenio")
        val pacientesConvenio = consultas.pacientesConConvenio()

        if (pacientesConvenio.isEmpty()){
            println("No hay pacientes con convenio")
        } else {
            pacientesConvenio.forEach { paciente ->
                println("${paciente.nombre} (${paciente.codigoAtencion}")
            }
        }

        val codigos = consultas.codigosFinalizados()
        println("codigos finalizados: " +
        if (codigos.isEmpty())"Ninguno" else codigos.joinToString()
        )

        val mayorAtencion = consultas.atencionMasLarga()

        if (mayorAtencion == null) {
            println("Atencion mas larga: no se encuentran datos")
        }else {
            println("Atencion mas larga: ${mayorAtencion.paciente.nombre} " +
            "(${mayorAtencion.paciente.codigoAtencion}), " +
            "${mayorAtencion.minutosAtencion} minutos")
        }
    }
}