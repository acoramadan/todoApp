package com.dicoding.todoapp.setting

import android.Manifest
import android.app.Notification
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.dicoding.todoapp.R
import com.dicoding.todoapp.notification.NotificationWorker
import com.dicoding.todoapp.utils.Helper
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Helper.showToast(this, "Notifications permission granted")
            } else {
                Helper.showToast(this, "Notifications will not show without permission")
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT > 32) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification =
                findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { preference, newValue ->
                val channelName = getString(R.string.notify_channel_name)
                //TODO 13 : Schedule and cancel daily reminder using WorkManager with data channelName
                if(newValue == true) {
                    val inputData = workDataOf(
                        "channelName" to channelName
                    )
                    Log.d("Settings Activity","preference = $preference, newValue = $newValue")

                    val notificationReq =
                        PeriodicWorkRequestBuilder<NotificationWorker>(1, TimeUnit.DAYS)
                            .setInputData(inputData)
                            .setInitialDelay(1, TimeUnit.MINUTES)
                            .build()

                    WorkManager.getInstance(requireContext())
                        .enqueueUniquePeriodicWork(
                            "dailyReminder",
                            ExistingPeriodicWorkPolicy.REPLACE,
                            notificationReq
                        )
                } else {
                    WorkManager.getInstance(requireContext())
                        .cancelUniqueWork("dailyReminder")
                }
                true
            }
        }
    }
}