package custom

import org.jetbrains.compose.web.css.*

class CustomStyle {
    companion object {
        fun StyleScope.borderTop(width: CSSNumeric, lineStyle: LineStyle, color: CSSColorValue) {
            property("border-top", "$width $lineStyle $color")
        }

        fun StyleScope.borderRight(width: CSSNumeric, lineStyle: LineStyle, color: CSSColorValue) {
            property("border-right", "$width $lineStyle $color")
        }

        fun StyleScope.borderBottom(width: CSSNumeric, lineStyle: LineStyle, color: CSSColorValue) {
            property("border-bottom", "$width $lineStyle $color")
        }

        fun StyleScope.borderLeft(width: CSSNumeric, lineStyle: LineStyle, color: CSSColorValue) {
            property("border-left", "$width $lineStyle $color")
        }

        fun StyleScope.borderCollapse(){
            property("border-collapse","collapse")
        }
    }
}