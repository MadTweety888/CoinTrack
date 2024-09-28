package com.example.cointrack.ui.theme.customshapes

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class RoundedTopCornersWithCurvedEdgeRectangle(
    private val curveHeight: Float = 20f,
    private val cornerRadius: Float = 100f
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        return Outline.Generic(

            Path().apply {

                addRoundRect(
                    RoundRect(
                        rect = Rect(
                            offset = Offset(0f, curveHeight),
                            size = Size(
                                width = size.width,
                                height = size.height
                            )
                        ),
                        topLeft = CornerRadius(cornerRadius, cornerRadius),
                        topRight = CornerRadius(cornerRadius, cornerRadius)
                    )
                )

                arcTo(
                    rect = Rect(cornerRadius, 0f, size.width - cornerRadius, 2 * curveHeight),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = -180f,
                    forceMoveTo = false
                )
            }
        )
    }
}