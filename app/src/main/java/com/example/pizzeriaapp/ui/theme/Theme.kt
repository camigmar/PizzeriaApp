package com.example.pizzeriaapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Esquema de colores completo; cada color "on..." es el del texto que va encima
// y tiene contraste suficiente para leerse bien
private val EsquemaPizzeria = lightColorScheme(
    primary = RojoTomate,
    onPrimary = BlancoCalido,             // blanco sobre rojo tomate: contraste 5,5:1
    primaryContainer = RojoSuave,
    onPrimaryContainer = RojoProfundo,

    secondary = NaranjaQuemado,
    onSecondary = CafeTexto,              // el blanco no alcanza contraste sobre este naranja
    secondaryContainer = NaranjaSuave,
    onSecondaryContainer = NaranjaProfundo,

    tertiary = CafeOscuro,
    onTertiary = Crema,
    tertiaryContainer = CafeOscuro,
    onTertiaryContainer = Crema,

    background = Crema,
    onBackground = CafeTexto,

    surface = BlancoCalido,
    onSurface = CafeTexto,
    surfaceVariant = CremaOscura,
    onSurfaceVariant = CafeMedio,
    surfaceContainerLowest = BlancoCalido,
    surfaceContainerLow = BlancoCalido,
    surfaceContainer = Crema,
    surfaceContainerHigh = CremaOscura,
    surfaceContainerHighest = CremaOscura,

    outline = CafeBorde,
    outlineVariant = BordeCalido,

    error = RojoError,
    onError = BlancoCalido
)

// Tema de la app: siempre claro y sin colores dinámicos,
// para que la pizzería se vea igual en todos los teléfonos
@Composable
fun PizzeriaAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaPizzeria,
        typography = Typography,
        shapes = Formas,
        content = content
    )
}
