package org.example

import java.time.LocalDateTime

class Canino(
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
        require(minutos > 0){
            "El tiempo de atencion debe ser mayor que cero"
        }

        //dividir por 60.0 conserva la fraccion de hora
        val horas = minutos / 60.0
        val costo = horas * 12_000.0

        return if (tipoDueno == TipoDueno.Convenio){
            costo * 0.80
        } else {
            costo
        }
    }
}
