package org.example.boxes

import org.example.Paciente

sealed class EstadoBox{
    object Libre: EstadoBox()

    data class EnAtencion (
        val paciente: Paciente
    ): EstadoBox()

    data class EnProceso(
        val motivo: String
    ): EstadoBox()

    data class FueraServicio (
        val motivo: String
    ): EstadoBox()
}