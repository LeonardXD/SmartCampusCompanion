import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.CoroutineExceptionHandler

// ErrorFallback UI Component
@Composable
fun ErrorFallback(error: Throwable, resetError: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "An error occurred: ${error.localizedMessage}")
        Button(
            onClick = resetError,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Retry")
        }
    }
}

// ErrorBoundary Composable
@Composable
fun ErrorBoundary(
    onError: (Throwable) -> Unit = {},
    content: @Composable () -> Unit
) {
    var error by remember { mutableStateOf<Throwable?>(null) }

    val resetError = {
        error = null
    }

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error = throwable
        onError(throwable)
    }

    // Wrap the content with the error handler
    try {
        content()
    } catch (e: Throwable) {
        error = e
        onError(e)
    }

    // Show error fallback if error exists
    if (error != null) {
        ErrorFallback(error = error!!, resetError = resetError)
    }
}

@Preview
@Composable
fun PreviewErrorBoundary() {
    MyApplicationTheme {
        ErrorBoundary(
            onError = { throwable ->
                println("Error caught: ${throwable.localizedMessage}")
            }
        ) {
            // Content that may throw an error
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text("This is some content inside ErrorBoundary.")
                Button(onClick = { throw RuntimeException("Test error!") }) {
                    Text("Cause Error")
                }
            }
        }
    }
}
