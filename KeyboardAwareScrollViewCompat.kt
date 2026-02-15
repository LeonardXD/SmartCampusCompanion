import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

@Composable
fun KeyboardAwareScrollViewCompat(
    modifier: Modifier = Modifier,
    children: @Composable () -> Unit,
    keyboardShouldPersistTaps: Boolean = true
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current.density

    // Handle scroll and keyboard visibility
    Column(modifier = modifier.fillMaxSize()) {
        // Placeholder for children
        children()
    }

    if (keyboardShouldPersistTaps) {
        // Ensure taps on the screen that are not on text fields dismiss the keyboard
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)  // Space for the keyboard
                .clickable {
                    keyboardController?.hide()  // Hide the keyboard when clicked outside
                }
        )
    }
}

@Composable
fun TextInputWithKeyboardSupport() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Type here") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()  // Dismiss the keyboard on done action
            }
        ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewKeyboardAwareScrollViewCompat() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Below is the keyboard-aware scroll view:")
        KeyboardAwareScrollViewCompat(keyboardShouldPersistTaps = true) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text("This is some content inside the scroll view.")
                TextInputWithKeyboardSupport()
            }
        }
    }
}

