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

// Fonts
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


val Anek = FontFamily(
    Font(
        R.font.anek_devanagari,

    )
)


val SplineSansMono = FontFamily(
    Font(
        R.font.spline_sans_mono,

    )
)

val Wellfleet = FontFamily(
    Font(R.font.wellfleet_regular, FontWeight.Normal, FontStyle.Normal)
)

val BlackOpsOne = FontFamily(
    Font(R.font.black_ops_one, FontWeight.Normal, FontStyle.Normal)
)

val Caveat = FontFamily(
    Font(R.font.caveat_regular, FontWeight.Normal, FontStyle.Normal)
)

val GermaniaOne = FontFamily(
    Font(R.font.germania_one, FontWeight.Normal, FontStyle.Normal)
)

val MonotonRegular = FontFamily(
    Font(R.font.monoton_regular, FontWeight.Normal, FontStyle.Normal)
)

val PixelifySans = FontFamily(
    Font(R.font.pixelify_sans_regular_fixed5, FontWeight.Normal, FontStyle.Normal)
)

val PoiretOne = FontFamily(
    Font(R.font.poiret_one, FontWeight.Normal, FontStyle.Normal)
)

val SairaVariable = FontFamily(
    Font(R.font.saira_variable, FontWeight.Normal, FontStyle.Normal)
)

val TacOne = FontFamily(
    Font(R.font.tac_one_regular, FontWeight.Normal, FontStyle.Normal)
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
        letterSpacing = 1.sp,
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

)