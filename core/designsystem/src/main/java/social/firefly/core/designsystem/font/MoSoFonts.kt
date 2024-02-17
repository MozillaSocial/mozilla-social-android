package social.firefly.core.designsystem.font

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import social.firefly.core.designsystem.R

object MoSoFonts {
    val zillaSlab =
        FontFamily(
            Font(R.font.zillaslab_bold, FontWeight.Bold),
            Font(R.font.zillaslab_bold_italic, FontWeight.Bold, FontStyle.Italic),
            Font(R.font.zillaslab_italic, style = FontStyle.Italic),
            Font(R.font.zillaslab_light_italic, FontWeight.Light, FontStyle.Italic),
            Font(R.font.zillaslab_medium, FontWeight.Medium),
            Font(R.font.zillaslab_medium_italic, FontWeight.Medium, FontStyle.Italic),
            Font(R.font.zillaslab_regular, FontWeight.Normal),
            Font(R.font.zillaslab_semi_bold, FontWeight.SemiBold),
            Font(R.font.zillaslab_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
        )

    val petiteFormalScript =
        FontFamily(
            Font(R.font.petit_formal_script_regular, FontWeight.Bold),
        )
}
