package com.example.vivatechassignment

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.vivatechassignment.ui.theme.VivaTechAssignmentTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            askNotificationPermission()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel("testingId", "testingChannel", NotificationManager.IMPORTANCE_DEFAULT)
                val manager = getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(channel)
            }
            Firebase.messaging.subscribeToTopic("kanti")
                .addOnCompleteListener { task ->
                    var msg = "Subscribed"
                    if (!task.isSuccessful) {
                        msg = "Subscribe failed"
                    }
                    Log.d("Kanti-Notification", msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                }
            VivaTechAssignmentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "YOu will receive notification", Toast.LENGTH_SHORT).show()
            // FCM SDK (and your app) can post notifications.
        } else {
            Toast.makeText(this, "give permission", Toast.LENGTH_SHORT).show()
            // TODO: Inform user that that your app will not show notifications.
        }
    }
    private fun askNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }

//        // This is only necessary for API level >= 33 (TIRAMISU)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION) ==
//                PackageManager.PERMISSION_GRANTED
//            ) {
//                // FCM SDK (and your app) can post notifications.
//            } else if (shouldShowRequestPermissionRationale(Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION)) {
//                // TODO: display an educational UI explaining to the user the features that will be enabled
//                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
//                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
//                //       If the user selects "No thanks," allow the user to continue without notifications.
//            } else {
//                // Directly ask for the permission
//                requestPermissionLauncher.launch(Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION)
//            }
//        }
    }


}

@Composable
fun Greeting() {

    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            text = "Hello, you are logged in that means you subscribe to a topic, and when owner send notification from firebase you will receive notification.",
            modifier = Modifier
                .wrapContentWidth()
                .padding(8.dp)
        )

        val ctx = LocalContext.current
        Button(onClick = {
            Firebase.auth.signOut()
            ctx.startActivity(Intent(ctx, AuthActivity::class.java))
            (ctx as MainActivity).finish()
        },
        modifier = Modifier.align(Alignment.CenterHorizontally).padding(10.dp)) {
            Text(text = "Logout")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VivaTechAssignmentTheme {

    }
}