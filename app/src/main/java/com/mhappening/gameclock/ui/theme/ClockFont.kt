package com.mhappening.gameclock.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

enum class ClockFont(
    val fontName: String,
    val textStyle: TextStyle,
    val fontScale: Float,
    val textBoxHeight: Float,
    val textBoxWidthHeightRatio: Float,
    val fontYOffsetPercentage: Float,
    val fontTotalHeightPercentage: Float = 1.0f
) {


    /**
     * Roboto done
     */
    ROBOTO(
        fontName = "Roboto",
        textStyle = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.15f,
        textBoxHeight = 1.1f,
        textBoxWidthHeightRatio = 0.6f,
        fontYOffsetPercentage = 0.1f,
        fontTotalHeightPercentage = 1.46f
    ),

    /**
     * Doppio done-io
     */
    DOPPIO(
        fontName = "Doppio",
        textStyle = TextStyle(
            fontFamily = Doppio,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.12f,
        textBoxHeight = 1.05f,
        textBoxWidthHeightRatio = 0.6f,
        fontYOffsetPercentage = 0.15f,
        fontTotalHeightPercentage = 1.47f
    ),
    /**
     * Teko, more like tek done
     */

    TEKO(
        fontName = "Teko",
        textStyle = TextStyle(
            fontFamily = Teko,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.35f,
        textBoxHeight = 1f,
        textBoxWidthHeightRatio = 0.4f,
        fontYOffsetPercentage = 0.15f,
        fontTotalHeightPercentage = 1.43f
    ),

    /**
     * Anek done
     */
    ANEK(
        fontName = "Anek Devanagari",
        textStyle = TextStyle(
            fontFamily = Anek,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.12f,
        textBoxHeight = 1.04f,
        textBoxWidthHeightRatio = 0.58f,
        fontYOffsetPercentage = 0.2f,
        fontTotalHeightPercentage = 1.5f
    ),

    /**
     * Well-done-fleet
     */
    WELLFLEET(
        fontName = "Wellfleet",
        textStyle = TextStyle(
            fontFamily = Wellfleet,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.1f,
        textBoxHeight = 1.1f,
        textBoxWidthHeightRatio = 0.6f,
        fontYOffsetPercentage = 0.04f,
        fontTotalHeightPercentage = 1.5f
    ),

    /**
     * Spline Sans Done
     */
    SPLINE_SANS_MONO(
        fontName = "Spline Sans Mono",
        textStyle = TextStyle(
            fontFamily = SplineSansMono,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.2f,
        textBoxHeight = 1.0f,
        textBoxWidthHeightRatio = 0.57f,
        fontYOffsetPercentage = 0.1f,
        fontTotalHeightPercentage = 1.47f
    ),

    /**
     * preview doesn't seem to like this one but it works in emu
     */
    BLACK_OPS_ONE(
        fontName = "Black Ops One",
        textStyle = TextStyle(
            fontFamily = BlackOpsOne,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.0f,
        textBoxHeight = 1.0f,
        textBoxWidthHeightRatio = 0.67f,
        fontYOffsetPercentage = 0.1f,
        fontTotalHeightPercentage = 1.4f
    ),

//    CAVEAT(
//        fontName = "Caveat",
//        textStyle = TextStyle(
//            fontFamily = Caveat,
//            fontWeight = FontWeight.Normal,
//        ),
//        fontScale = 1.0f,
//        textBoxHeight = 1.0f,
//        textBoxWidthHeightRatio = 0.7f,
//        fontYOffsetPercentage = 0.1f,
//        fontTotalHeightPercentage = 1.4f
//    ),

    GERMANIA_ONE(
        fontName = "Germania One",
        textStyle = TextStyle(
            fontFamily = GermaniaOne,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.2f,
        textBoxHeight = 1.0f,
        textBoxWidthHeightRatio = 0.57f,
        fontYOffsetPercentage = 0.1f,
        fontTotalHeightPercentage = 1.48f
    ),

    MONOTON_REGULAR(
        fontName = "Monoton Regular",
        textStyle = TextStyle(
            fontFamily = MonotonRegular,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 0.83f,
        textBoxHeight = 1.2f,
        textBoxWidthHeightRatio = 0.82f,
        fontYOffsetPercentage = 0.1f,
        fontTotalHeightPercentage = 1.4f
    ),

    PIXELIFY_SANS(
        fontName = "Pixelify Sans",
        textStyle = TextStyle(
            fontFamily = PixelifySans,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.0f,
        textBoxHeight = 1.0f,
        textBoxWidthHeightRatio = 0.57f,
        fontYOffsetPercentage = 0.1f,
        fontTotalHeightPercentage = 1.4f
    ),

    POIRET_ONE(
        fontName = "Poiret One",
        textStyle = TextStyle(
            fontFamily = PoiretOne,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.0f,
        textBoxHeight = 1.0f,
        textBoxWidthHeightRatio = 0.778f,
        fontYOffsetPercentage = 0.1f,
        fontTotalHeightPercentage = 1.4f
    ),

    SAIRA_VARIABLE(
        fontName = "Saira Variable",
        textStyle = TextStyle(
            fontFamily = SairaVariable,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.15f,
        textBoxHeight = 1.2f,
        textBoxWidthHeightRatio = 0.57f,
        fontYOffsetPercentage = 0.15f,
        fontTotalHeightPercentage = 1.43f
    ),

    TAC_ONE(
        fontName = "Tac One",
        textStyle = TextStyle(
            fontFamily = TacOne,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.2f,
        textBoxHeight = 1.0f,
        textBoxWidthHeightRatio = 0.53f,
        fontYOffsetPercentage = 0.1f,
        fontTotalHeightPercentage = 1.4f
    ),


/*    HONK(
        fontName = "Honk",
        textStyle = TextStyle(
            fontFamily = Honk,
            fontWeight = FontWeight.Normal,
        ),
        fontScale = 1.4f,
        textBoxHeight = 1f,
        textBoxWidthHeightRatio = 0.51f,
        fontYOffsetPercentage = 0.1f
    ),*/


}