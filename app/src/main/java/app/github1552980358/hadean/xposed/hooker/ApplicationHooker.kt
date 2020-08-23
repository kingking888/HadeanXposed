package app.github1552980358.hadean.xposed.hooker

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import app.github1552980358.hadean.receiver.ExternalBroadcastReceiver.Companion.ACTION_LOCK_APPLICATION
import app.github1552980358.hadean.xposed.base.BaseHooker
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.util.Timer
import kotlin.concurrent.timerTask

/**
 * [ApplicationHooker]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/20
 * @time    : 14:08
 **/

interface ApplicationHooker: BaseHooker {
    
    companion object {
        
        const val KILL_INSTANT = 0
        const val KILL_DELAY_5_MIN = 1
        const val KILL_DELAY_10_MIN = 2
        
        private const val DELAY_5_MIN = 300000L
        private const val DELAY_10_MIN = 600000L
    
    }
    
    fun killInstant(loadPackageParam: XC_LoadPackage.LoadPackageParam) = listenToScreen(loadPackageParam, KILL_INSTANT)
    fun killDelay5Min(loadPackageParam: XC_LoadPackage.LoadPackageParam) = listenToScreen(loadPackageParam, KILL_DELAY_5_MIN)
    fun killDelay10Min(loadPackageParam: XC_LoadPackage.LoadPackageParam) = listenToScreen(loadPackageParam, KILL_DELAY_10_MIN)
    
    private fun listenToScreen(loadPackageParam: XC_LoadPackage.LoadPackageParam, policy: Int) {
        XposedHelpers.findAndHookMethod(
            "android.app.Application",
            loadPackageParam.classLoader,
            "onCreate",
            object : XC_MethodHook() {
                
                override fun afterHookedMethod(param: MethodHookParam?) {
                    (param?.thisObject as Application?)?.registerReceiver(
                        object : BroadcastReceiver() {
                            private var timer: Timer? = null
                            override fun onReceive(context: Context?, intent: Intent?) {
                                // Filtering
                                /*
                                 *  if (intent?.action != Intent.ACTION_SCREEN_OFF) {
                                 *      return
                                 * }
                                 **/
                                when (intent?.action) {
                                    Intent.ACTION_SCREEN_OFF -> {
                                        when (policy) {
                                            KILL_INSTANT -> killApplication(context, this)
                                            // Delays
                                            KILL_DELAY_5_MIN -> {
                                                setTimer(context, this, DELAY_5_MIN)
                                                return
                                            }
                                            KILL_DELAY_10_MIN -> {
                                                setTimer(context, this, DELAY_10_MIN)
                                                return
                                            }
                                            else -> return
                                        }
                                        timerTask {  }
                                    }
                                    Intent.ACTION_SCREEN_ON -> {
                                        // Update screen status
                                        //screenOff = //try {
                                        //    !((context?.getSystemService(Service.POWER_SERVICE) as PowerManager?)?.isInteractive ?: false)
                                        timer?.cancel()
                                        //} catch (e: Exception) { false }
                                        return
                                    }
                                    else -> return
                                }
                            }
                            
                            private fun setTimer(context: Context?, broadcastReceiver: BroadcastReceiver, delay: Long) {
                                timer?.cancel()
                                timer = Timer().apply {
                                    schedule(
                                        timerTask { killApplication(context, broadcastReceiver) },
                                        delay
                                    )
                                }
                            }
                        },
                        IntentFilter(Intent.ACTION_SCREEN_ON , Intent.ACTION_SCREEN_OFF)
                    )
                }
            }
        )
    }
    
    private fun killApplication(context: Context?, broadcastReceiver: BroadcastReceiver) {
        // Call to lock and kill
        context?.sendBroadcast(Intent(ACTION_LOCK_APPLICATION))
        // Remove this receiver
        context?.unregisterReceiver(broadcastReceiver)
        // Kill app and clear VM stack
        throw Exception()
    }
    
}