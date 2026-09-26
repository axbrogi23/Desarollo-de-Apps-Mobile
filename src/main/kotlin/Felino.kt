package org.example

import java.time.LocalDateTime



class Felino(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    fechaIngreso: LocalDateTime,
    tipoDueno: TipoDueno
) : Paciente(
    codigoAtencion,
    nombre,
    especie,
    fechaIngreso,
    tipoDueno
) {
    override fun calcularCostoBase(minutos: Long): Double {
        require(minutos >= 0) {
            "El tiempo de atención no puede ser negativo."
        }

        // no hay cobro en atenciones menores a 20 min
        if (minutos < 20) {
            return 0.0
        }

        val horas = minutos / 60.0
        return horas * 9000.0
    }
}