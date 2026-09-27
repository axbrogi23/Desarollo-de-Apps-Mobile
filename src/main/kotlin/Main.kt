package org.example

import java.time.LocalDateTime

import org.example.boxes.Box
import org.example.boxes.EstadoBox
import org.example.boxes.GestorBoxes
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import org.example.reportes.TurnoReporte

fun main() = runBlocking {
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

        //probando los cuatro estados.
        println("---Prueba de estados del box---")

        val PruebaBox = Box(numero = 1)

        //1. Libre es el estado inicial
        println(PruebaBox.obtenerDetalle())

        //2 en proceso
        PruebaBox.estado = EstadoBox.EnProceso(
            motivo = "Resgistrando entrada"
        )
        println(PruebaBox.obtenerDetalle())

        //3 en atencion
        PruebaBox.estado = EstadoBox.EnAtencion(
            paciente = paciente
        )
        println(PruebaBox.obtenerDetalle())

        //simular el termino de atencion para quedar libre
        PruebaBox.estado = EstadoBox.Libre
        println(PruebaBox.obtenerDetalle())

        //4. obtener si esta fuera de servicio
        PruebaBox.estado = EstadoBox.FueraServicio(
            motivo = "Mantenimiento del sistema"
        )
        println(PruebaBox.obtenerDetalle())

        println("---Gestion de los 10 boxes---")

        val gestor = GestorBoxes()

        gestor.mostrarBoxes()
        println("Boxes disponibles: ${gestor.disponibilidades()}")

        val disponible = gestor.buscarPrimerLibre()

        if (disponible != null) {
            println("Primero box libre: ${disponible.numero}")
        } else {
            println("no hay ningun box disponible")
        }

        println("Registro para entrada")

        val entrada = launch {
            gestor.registrarEntrada(paciente)
        }

        println("el registro ha sido solicitado ya puede continuar")

        entrada.join()

        gestor.mostrarBoxes()
        println("Boxes disponibles en este momento: ${gestor.disponibilidades()}")

        println("Registro de salida")

        val salida = launch {
            gestor.registrarSalida(
                codigoAtencion = paciente.codigoAtencion,
                minutosAtencion = 75
            )
        }

        salida.join()

        gestor.mostrarBoxes()
        println("Los boxes disponibles al finalizar: ${gestor.disponibilidades()}")

        TurnoReporte.mostrar(gestor)

    }   catch ( error: IllegalArgumentException){
        println("Error de los datos: ${error.message}")
    }
}
