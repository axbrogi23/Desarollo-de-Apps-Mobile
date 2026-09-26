package org.example

import java.time.LocalDateTime


class Exotico (
    codigoAtencion: String,
    nombre: String,
    especie: String,
    fechaIngreso: LocalDateTime,
    tipoDueno: TipoDueno,

    val esSilvestre: Boolean
): Paciente(
    codigoAtencion,
    nombre,
    especie,
    fechaIngreso,
    tipoDueno
){
    override fun calcularCostoBase(minutos: Long): Double {
        require(minutos > 0) {
            "El tiempo de atencion debe ser mayor a cero"
        }

        val horas = minutos / 60.0
        val costo = horas * 20000.0

        return if (esSilvestre){
            costo * 1.30
        } else {
            costo
        }
    }
}