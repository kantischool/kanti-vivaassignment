@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.vivatechassignment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
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

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VivaTechAssignmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignInPage()
                }
            }
        }
    }
}

@Composable
fun SignInPage() {

    val signEmail = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val signPassword = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val signConfPassword = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    Column(modifier = Modifier.padding(10.dp)) {

        Text(text = "Sign In",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),)
        TextField(
            value = signEmail.value,
            onValueChange = {
                signEmail.value = it
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            label = {
                Text(text = "email")
            }
        )

        TextField(
            value = signPassword.value,
            onValueChange = {
                signPassword.value = it
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            label = {
                Text(text = "password")
            }
        )

        TextField(
            value = signConfPassword.value,
            onValueChange = {
                signConfPassword.value = it
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            label = {
                Text(text = " confirm password")
            }
        )

        val ctx = LocalContext.current
        val userEmail = signEmail.value.text.trim().toLowerCase(Locale.current)
        val userPassword = signPassword.value.text
        Button(
            onClick = {
                if (userEmail.isEmpty() || !(Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
                    || userPassword.isEmpty() || userPassword.length < 8){
                    Toast.makeText(ctx, "check the email format or password length", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (userPassword != signConfPassword.value.text){
                    Toast.makeText(ctx, "password is not matching", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                createUser(ctx, userEmail, userPassword)
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
        ) {

            Text(text = "Sign In")

        }

        Text(text = "Already have an account, Click Here",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    ctx.startActivity(Intent(ctx, AuthActivity::class.java))
                }
                .align(Alignment.CenterHorizontally),)
    }
}

fun createUser(ctx: Context, email: String, password: String) {

    val auth = Firebase.auth
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                ctx.startActivity(Intent(ctx, MainActivity::class.java))
                ( ctx as SignInActivity).finish()
            } else {

                Toast.makeText(
                    ctx,
                    task.exception?.message ?: "auth failed",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    VivaTechAssignmentTheme {

    }
}