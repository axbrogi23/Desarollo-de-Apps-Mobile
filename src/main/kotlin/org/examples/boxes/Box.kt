package org.example.boxes

class Box (
    val numero: Int
) {
    var estado: EstadoBox = EstadoBox.Libre

    init {
        require(numero > 0) {
            "El numero del box tiene que ser mayor de cero"
        }
    }

    fun obtenerDetalle(): String{

        val estadoActual = estado

        val descripcion = when (estadoActual) {
            EstadoBox.Libre -> "Libre"

            is EstadoBox.EnAtencion -> "En atencion ${estadoActual.paciente.nombre}"+
                    "${estadoActual.paciente.codigoAtencion})"

            is EstadoBox.EnProceso -> "En proceso: ${estadoActual.motivo}"

            is EstadoBox.FueraServicio -> "Fuera de servicio: ${estadoActual.motivo}"
        }
        return "box $numero: $descripcion"
    }

}