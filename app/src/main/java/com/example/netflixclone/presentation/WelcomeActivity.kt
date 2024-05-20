package com.example.netflixclone.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.example.netflixclone.presentation.movielist.screen.welcome.WelcomeScreen

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val context = LocalContext.current
//            val startAnotherActivity = rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.StartActivityForResult()
//            ) { result ->
//                // handle the result of the launched activity if needed
//            }
//
//            val intent = Intent(context, MainActivity::class.java)
//            startAnotherActivity.launch(intent)
//
            WelcomeScreen()
        }
    }
}