package org.example

import java.time.LocalDateTime

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    try {
        val paciente = Canino(
            codigoAtencion = "CA12CD", //codigo de la atencion
            nombre = "Max",
            especie = "Golden Retriever",
            fechaIngreso = LocalDateTime.now(),
            tipoDueno = TipoDueno.Convenio
        )

        val costoBase = paciente.calcularCostoBase(75)

        println("Bienvenido al sistema de Pet Care")
        println("Paciente: ${paciente.nombre}")
        println("Codigo: ${paciente.codigoAtencion}")
        println("Costo antes de IVA: $costoBase")

        val felino = Felino(
            codigoAtencion = "FA28ZT",
            nombre = "Shakira",
            especie = "Siames",
            fechaIngreso = LocalDateTime.now(),
            tipoDueno = paciente.tipoDueno
        )

        println("--Felino--")
        println("Paciente: ${felino.nombre}")
        println("costo total por los 18 minutos: ${felino.calcularCostoBase(18)}")
        println("costo total por los 20 minutos: ${felino.calcularCostoBase(20)}")

        val exotico = Exotico(
            codigoAtencion = "EX01TC",
            nombre = "Pato mandarin",
            especie = "Ave anseriforme",
            fechaIngreso = LocalDateTime.now(),
            tipoDueno = TipoDueno.Municipal,
            esSilvestre = true
        )

        println("--Exotico--")
        println("Paciente: ${exotico.nombre}")
        println("Silvestre: ${if (exotico.esSilvestre) "Si" else "No"}")
        println("costo por una atencion de 120 minutos es: ${exotico.calcularCostoBase(120)}")

        val totalCanino = CalcularTarifa.calcularTotal(paciente, 75)
        val totalFelino1 = CalcularTarifa.calcularTotal(felino, 18)
        val totalFelino2 = CalcularTarifa.calcularTotal(felino, 20)
        val totalExotico = CalcularTarifa.calcularTotal(exotico, 120)


        println("Max Tardo 75 minutos: $totalCanino")
        println("Shakira Tardo 18 minutos: $totalFelino1")
        println("Shakira Tardo 20 minutos: $totalFelino2")
        println("Pato mandarin Tardo 120 minutos: $totalExotico")


    }   catch ( error: IllegalArgumentException){
        println("Error de los datos: ${error.message}")
    }
}
