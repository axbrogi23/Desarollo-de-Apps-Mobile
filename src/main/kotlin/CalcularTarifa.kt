package org.example


object CalcularTarifa {
    fun calcularTotal(paciente: Paciente, minutos: Long): Double {
        require(minutos >= 0){
            "El tiempo de la atencion no puede ser un numero negativc"
        }

        val costoBase = paciente.calcularCostoBase(minutos)

        val CostoCero = paciente is Felino && minutos < 20

        require(
            costoBase.isFinite()&&(costoBase > 0 || (costoBase == 0.0 && CostoCero))
        ) {
            "El calculo ha producido una tarifa o costo invalido"
        }

        val CostoIva = costoBase * 1.19

        return if (paciente.tipoDueno == TipoDueno.Municipal) {
            CostoIva * 0.50
        } else {
            CostoIva
        }
    }
}