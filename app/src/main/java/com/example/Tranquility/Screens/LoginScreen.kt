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

@Composable
fun LoginScreen(
    onRegisterClick : () -> Unit,
    onLogin : () -> Unit

) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(32.dp))

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Enter your email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage.isNotEmpty(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            placeholder = { Text("Enter your password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage.isNotEmpty(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {  }
            ),
            visualTransformation = PasswordVisualTransformation()
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (successMessage.isNotEmpty()) {
            Text(
                text = successMessage,
                color = Color.Green,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    // Authenticate user with Firebase
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                successMessage = "Login successful!"
                                errorMessage = ""
                                onLogin()
                            } else {
                                errorMessage = task.exception?.message ?: "Login failed"
                                successMessage = ""
                            }
                        }
                } else {
                    errorMessage = "Please fill in both fields"
                    successMessage = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onRegisterClick()}) {
            Text("Don't have an account? Register", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen({}, {})
}
