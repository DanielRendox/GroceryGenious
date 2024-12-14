package com.rendox.grocerygenius.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.rendox.grocerygenius.model.DEFAULT_USER_PREFERENCES
import com.rendox.grocerygenius.model.GroceryGeniusColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.BeigeDarkColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.BeigeLightColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.CyanDarkColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.CyanLightColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.GreenDarkColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.GreenLightColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.PinkDarkColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.PinkLightColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.PurpleDarkColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.PurpleLightColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.YellowDarkColorScheme
import com.rendox.grocerygenius.ui.theme.colorschemes.YellowLightColorScheme

@Composable
fun GroceryGeniusTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicColor: Boolean = true,
    requestedColorScheme: GroceryGeniusColorScheme? = null,
    content: @Composable () -> Unit
) {
    val dynamicColor = dynamicColorIsSupported && !disableDynamicColor
    val extendedColors =
        if (useDarkTheme) DarkExtendedColors else LightExtendedColors
    val dynamicColorScheme = when {
        dynamicColor && useDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !useDarkTheme ->
            dynamicLightColorScheme(LocalContext.current)

        else -> null
    }
    val staticColorScheme = requestedColorScheme ?: DEFAULT_USER_PREFERENCES.selectedTheme
    val resultingColorScheme =
        dynamicColorScheme ?: staticColorScheme.deriveColorScheme(useDarkTheme)

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = resultingColorScheme,
            content = content
        )
    }
}

@Composable
fun GroceryGeniusColorScheme.deriveColorScheme(useDarkTheme: Boolean): ColorScheme = when (this) {
    GroceryGeniusColorScheme.BeigeColorScheme -> {
        if (useDarkTheme) {
            BeigeDarkColorScheme
        } else {
            BeigeLightColorScheme
        }
    }

    GroceryGeniusColorScheme.CyanColorScheme -> {
        if (useDarkTheme) {
            CyanDarkColorScheme
        } else {
            CyanLightColorScheme
        }
    }

    GroceryGeniusColorScheme.GreenColorScheme -> {
        if (useDarkTheme) {
            GreenDarkColorScheme
        } else {
            GreenLightColorScheme
        }
    }

    GroceryGeniusColorScheme.PinkColorScheme -> {
        if (useDarkTheme) {
            PinkDarkColorScheme
        } else {
            PinkLightColorScheme
        }
    }

    GroceryGeniusColorScheme.PurpleColorScheme -> {
        if (useDarkTheme) {
            PurpleDarkColorScheme
        } else {
            PurpleLightColorScheme
        }
    }

    GroceryGeniusColorScheme.YellowColorScheme -> {
        if (useDarkTheme) {
            YellowDarkColorScheme
        } else {
            YellowLightColorScheme
        }
    }
}

val dynamicColorIsSupported: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S