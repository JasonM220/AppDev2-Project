import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

// Composable function for the login screen
@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit, // Callback for navigating to the registration screen
    onLogin: () -> Unit // Callback for successful login
) {
    // State variables for user input and messages
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    // Firebase Authentication instance
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Layout of the login screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Padding around the entire column
        horizontalAlignment = Alignment.CenterHorizontally, // Center items horizontally
        verticalArrangement = Arrangement.Center // Center items vertically
    ) {
        // Welcome message
        Text("Welcome", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(32.dp)) // Spacing between elements

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it }, // Update email state
            label = { Text("Email") },
            placeholder = { Text("Enter your email") },
            modifier = Modifier.fillMaxWidth(), // TextField takes full width
            singleLine = true, // Input is restricted to a single line
            isError = errorMessage.isNotEmpty(), // Show error styling if there's an error
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next // "Next" action for the keyboard
            ),
            keyboardActions = KeyboardActions(
                onNext = { /* Additional action on "Next" (optional) */ }
            )
        )

        Spacer(modifier = Modifier.height(16.dp)) // Spacing between fields

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it }, // Update password state
            label = { Text("Password") },
            placeholder = { Text("Enter your password") },
            modifier = Modifier.fillMaxWidth(), // TextField takes full width
            singleLine = true, // Input is restricted to a single line
            isError = errorMessage.isNotEmpty(), // Show error styling if there's an error
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done // "Done" action for the keyboard
            ),
            visualTransformation = PasswordVisualTransformation() // Mask password input
        )

        // Display error message, if any
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp) // Padding above the message
            )
        }

        // Display success message, if any
        if (successMessage.isNotEmpty()) {
            Text(
                text = successMessage,
                color = Color.Green,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp) // Padding above the message
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // Spacing before the button

        // Sign-In Button
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    // Attempt Firebase authentication
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                successMessage = "Login successful!" // Show success message
                                errorMessage = "" // Clear error message
                                onLogin() // Trigger login callback
                            } else {
                                errorMessage = "Login failed" // Show error
                                successMessage = "" // Clear success message
                            }
                        }
                } else {
                    // Show an error if fields are empty
                    errorMessage = "Please fill in both fields"
                    successMessage = ""
                }
            },
            modifier = Modifier.fillMaxWidth() // Button takes full width
        ) {
            Text("Sign In") // Button text
        }

        Spacer(modifier = Modifier.height(16.dp)) // Spacing between buttons

        // Register Button
        TextButton(onClick = { onRegisterClick() }) { // Navigate to registration screen
            Text(
                "Don't have an account? Register",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

// Preview for the Login Screen
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen({}, {}) // Pass empty lambdas for the callbacks
}
