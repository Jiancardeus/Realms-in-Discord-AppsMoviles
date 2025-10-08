package com.example.actividad2.viewmodel
import androidx.lifecycle.ViewModel
import com.example.actividad2.model.UsuarioErrores
import com.example.actividad2.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {

    //Estado interno mutable

    private val _estado = MutableStateFlow(value = UsuarioUiState())

    //Estado expuesto para la ui

    val estado: StateFlow<UsuarioUiState> = _estado

    //Actualiza el campo nombre y limpia su error

    fun onNombreChange (valor : String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }

    }

    //Actualiza el campo correo

    fun onCorreoChange (valor : String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }

    }

    //Actualiza el campo clave

    fun onClaveChange (valor : String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }

    }

    //Actualiza el campo direccion

    fun onDireccionChange (valor : String) {
        _estado.update { it.copy(direcccion = valor, errores = it.errores.copy(direcccion = null)) }

    }
    //Actualiza checkbox de aceptacion

    fun onAceptarTerminosChange (valor : Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }

    }
    //Validacion global del formulario
    fun validarFormulario() : Boolean {
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank())"Campo obligatorio" else null,
            correo = if (estadoActual.correo.isBlank())"Campo obligatorio" else null,
            clave = if (estadoActual.clave.isBlank())"Campo obligatorio" else null,
            direcccion = if (estadoActual.direcccion.isBlank())"Campo obligatorio" else null,
        )

        val hayErrores = listOfNotNull(
            elements = errores.nombre,
            errores.correo,
            errores.clave,
            errores.direcccion
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return hayErrores


    }
}