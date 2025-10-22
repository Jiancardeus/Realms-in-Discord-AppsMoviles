// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Plugins de Android y Kotlin (sin aplicar la versión antigua de Hilt aquí)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Definición del plugin de Hilt (para que esté disponible en el módulo 'app')
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}