package com.pashteut.todoapp.ui_kit

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES, widthDp = 600)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO, widthDp = 600)
@Composable
fun ThemePreview() {
    ToDoAppTheme {
        val colors: ColorScheme = MaterialTheme.colorScheme
        val fields = colors::class.java.declaredFields

        val colorFields = fields.filter { field ->
            field.type == Long::class.java
        }

        val colorList = colorFields.mapNotNull { field ->
            field.isAccessible = true
            val colorName = field.name
            val colorValue = Color((field.get(colors) as Long).toULong())
            colorName to colorValue
        }

        val textTypography = MaterialTheme.typography
        val typographyFields = textTypography::class.java.declaredFields

        val textStyles = typographyFields.filter { field ->
            field.isAccessible = true
            field.type == TextStyle::class.java
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                colorList.forEach { (colorName, color) ->
                    ColorBox(color = color, colorName = colorName)
                }
            }
            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .background(Color.White)
                    .wrapContentHeight()
            ) {
                textStyles.forEach {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${it.name}: ",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(text = "Sample Text", style = it.get(textTypography) as TextStyle)
                    }
                }
            }
        }
    }
}


@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewAdditionalColors(){
    ToDoAppTheme {
        val colors: Colors = MaterialTheme.colorScheme.additionalColors
        val fields = colors::class.java.declaredFields

        val colorFields = fields.filter { field ->
            field.type == Long::class.java
        }

        val colorList = colorFields.mapNotNull { field ->
            field.isAccessible = true
            val colorName = field.name
            val colorValue = Color((field.get(colors) as Long).toULong())
            colorName to colorValue
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            colorList.forEach { (colorName, color) ->
                ColorBox(color = color, colorName = colorName)
            }
        }
    }
}

@Composable
fun ColorBox(color: Color, colorName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = color)
            .padding(10.dp),
        contentAlignment = Alignment.Center,
    )
    {
        Text(
            text = colorName,
            color = Color.White,
            modifier = Modifier.background(Color.Black),
        )
    }
}
