package com.example.vivatechassignment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vivatechassignment.ui.theme.VivaTechAssignmentTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VivaTechAssignmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginDesign( )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
       val user =  Firebase.auth.currentUser
        if (user != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginDesign() {

    val loginEmail = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val loginPassword = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    Column(modifier = Modifier.padding(10.dp)) {

        Text(text = "Login",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
            .padding(8.dp)
            .align(Alignment.CenterHorizontally),)
        TextField(
            value = loginEmail.value,
            onValueChange = {
            loginEmail.value = it
        },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            label = {
                Text(text = "email")
            }
        )

        TextField(
            value = loginPassword.value,
            onValueChange = {
                loginPassword.value = it
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            label = {
                Text(text = "password")
            }
        )

        val ctx = LocalContext.current
        val userEmail = loginEmail.value.text.trim().toLowerCase(Locale.current)

        Button(
            onClick = {
                if (userEmail.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(loginEmail.value.text).matches()
                    && loginPassword.value.text.isNotEmpty() && loginPassword.value.text.length >= 8){
                    loginUser(ctx, userEmail, loginPassword.value.text)
                }
                else{
                    Toast.makeText(ctx, "check the email format or password length", Toast.LENGTH_SHORT).show()
                }

        },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
        ) {

            Text(text = "Login")

        }

        Text(text = "Don't have an account, Click Here",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    ctx.startActivity(Intent(ctx, SignInActivity::class.java))
                }
                .align(Alignment.CenterHorizontally)
        )
    }
}

fun loginUser(ctx: Context, email: String, password: String) {
val auth = Firebase.auth
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
        if (it.isSuccessful){
            val intent = Intent(ctx, MainActivity::class.java)
            ctx.startActivity(intent)
           ( ctx as AuthActivity).finish()
        }
        else{
            Toast.makeText(ctx, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    VivaTechAssignmentTheme {

    }
}