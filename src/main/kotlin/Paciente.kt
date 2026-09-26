package org.example

import java.time.LocalDateTime

abstract class Paciente(
    val codigoAtencion: String,
    val nombre: String,
    val especie: String,
    val fechaIngreso: LocalDateTime,
    val tipoDueno: TipoDueno
) {
    init {
        require(Regex("^[A-Z]{2}[0-9]{2}[A-Z]{2}$")
            .matches(codigoAtencion)) {
            "El código debe tener dos letras, dos dígitos y dos letras."
        }

        require(nombre.isNotBlank()) {
            "El nombre de la mascota no puede estar vacío."
        }

        require(especie.isNotBlank()) {
            "La especie no puede estar vacía."
        }
    }

    abstract fun calcularCostoBase(minutos: Long): Double
}