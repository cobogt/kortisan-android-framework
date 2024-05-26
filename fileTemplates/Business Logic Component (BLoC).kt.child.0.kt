#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}.presentation.components#end

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

#parse("File Header.java")
@Composable
fun Custom${NAME}Component( value: String ) {
    Text("Hello, I'am Custom${NAME}Component")
    Text(value)
}