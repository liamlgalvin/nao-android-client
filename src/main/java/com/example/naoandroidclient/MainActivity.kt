package com.example.naoandroidclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.naoandroidclient.domain.ActivityNotification
import com.example.naoandroidclient.domain.ConnectionStatus
import com.example.naoandroidclient.ui.DetailViewModel
import com.example.naoandroidclient.ui.main.MainScreen
import com.example.naoandroidclient.ui.main.NaoApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity()  {

    private val viewModel : DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            // get ip if it has been set on closing
            savedInstanceState.getString("ip")?.let { viewModel.setIp(it) }
        }

        val activityNotificationObserver = createActivityNotificationObserver()
        viewModel.activityNotification.observe(this, activityNotificationObserver)

        setContent {
            NaoApp {
                MainScreen(viewModel)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (viewModel.connectionStatus.value == ConnectionStatus.CONNECTED) {
            viewModel.sendMessage("destroy_connection", "connection destroyed")
        }
        viewModel.destroyWebSocket()
    }

    private fun createActivityNotificationObserver(): Observer<ActivityNotification> {
        return Observer<ActivityNotification> {
            when(it){
                ActivityNotification.RESTART -> {
                    val intent = intent
                    val bundle = Bundle()
                    bundle.putString("ip", viewModel.ip.value)
                    intent.putExtras(bundle)
                    finish()
                    startActivity(intent)
                }
                ActivityNotification.OBSERVE -> {
                    viewModel.observeConnection()
                }
            }

        }
    }
}
