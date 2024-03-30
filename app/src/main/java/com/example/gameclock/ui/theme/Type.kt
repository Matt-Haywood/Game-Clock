package com.example.gameclock.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.gameclock.R

fun counterWidth(counterWidth: Int): FontVariation.Setting {
    require(counterWidth in 323..603) { "'Counter width' must be in 323..603" }
    return FontVariation.Setting("XTRA", counterWidth.toFloat())
}

val Doppio = FontFamily(
    Font(R.font.doppio_one, FontWeight.Normal, FontStyle.Normal)
)

val Roboto = FontFamily(
    Font(R.font.roboto_mono, FontWeight.Normal, FontStyle.Normal)
)

val Teko = FontFamily(
    Font(R.font.teko, FontWeight.Normal, FontStyle.Normal)
)

@OptIn(ExperimentalTextApi::class)
val Honk = FontFamily(
    Font(
        R.font.honk, FontWeight.Normal, FontStyle.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.Setting("MORF", 10f),
            FontVariation.Setting("SHLN", 50f)
        )
    )
)


//@OptIn(ExperimentalTextApi::class)
//val Anek = FontFamily(
//    Font(
//        R.font.anek_devanagari,
//        variationSettings = FontVariation.Settings(
//            FontVariation.width(100f),
//            counterWidth(324)
//
//        )
//    )
//)

// (10.sp, 7.dp, 11.dp) (20.sp, 12.dp, 24.dp)
@OptIn(ExperimentalTextApi::class)
val SplineSansMono = FontFamily(
    Font(
        R.font.spline_sans_mono,
        variationSettings = FontVariation.Settings(
            FontVariation.width(100f),
            counterWidth(324)

        )
    )
)

val Wellfleet = FontFamily(
    Font(R.font.wellfleet_regular, FontWeight.Normal, FontStyle.Normal)
)

@OptIn(ExperimentalTextApi::class)
val RobotoFlex = FontFamily(
    Font(
        R.font.roboto_flex_variablefont,
        variationSettings = FontVariation.Settings(
            FontVariation.width(100f),
            counterWidth(324)
        ),

        )
)

// default typography for the app
val defaultTypography = Typography(
    //App Title
    titleLarge = TextStyle(
        fontFamily = Honk,
        fontWeight = FontWeight.Normal,
        fontSize = 80.sp,
        lineHeight = 80.sp,

        shadow = Shadow(offset = Offset(10.0f, 10.0f), blurRadius = 10f)
    ),
    // timer picker numbers
    displayLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 52.sp,
        letterSpacing = 0.sp
    ),
    //Clock Face
    headlineLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 52.sp,
        letterSpacing = 2.sp,

    ),
    //Settings Title
    titleMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.15.sp
    ),
    //Settings subheadings
    bodyMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 28.sp,

    ),
    //Settings on/off
    labelMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)