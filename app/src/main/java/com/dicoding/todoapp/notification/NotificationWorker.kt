    package com.dicoding.todoapp.notification

    import android.Manifest
    import android.app.NotificationChannel
    import android.app.NotificationManager
    import android.app.PendingIntent
    import android.content.Context
    import android.content.Intent
    import android.content.pm.PackageManager
    import android.os.Build
    import android.util.Log
    import androidx.core.app.ActivityCompat
    import androidx.core.app.NotificationCompat
    import androidx.core.app.NotificationManagerCompat
    import androidx.core.app.TaskStackBuilder
    import androidx.preference.PreferenceManager
    import androidx.work.Worker
    import androidx.work.WorkerParameters
    import com.dicoding.todoapp.R
    import com.dicoding.todoapp.data.Task
    import com.dicoding.todoapp.data.TaskRepository
    import com.dicoding.todoapp.ui.detail.DetailTaskActivity
    import com.dicoding.todoapp.utils.DateConverter
    import com.dicoding.todoapp.utils.TASK_ID

    class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

        private val channelName = inputData.getString("channelName")
        private val repo = TaskRepository.getInstance(ctx)
        private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)
        private val isNotificationEnabled = sharedPreferences.getBoolean(ctx.getString(R.string.pref_key_notify),true)

        private fun getPendingIntent(task: Task): PendingIntent? {
            val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
                putExtra(TASK_ID, task.id)
            }
            return TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(intent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getPendingIntent(task.id, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                } else {
                    getPendingIntent(task.id, PendingIntent.FLAG_UPDATE_CURRENT)
                }
            }
        }

        override fun doWork(): Result {
            //TODO 14 : If notification preference on, get nearest active task from repository and show notification with pending intent
            Log.d("preferences","pref : $isNotificationEnabled")
           if(!isNotificationEnabled) {
               return Result.success()
           }
            return try {
                fetchAndNotify(applicationContext)
                Log.d("Notification Worker","Worker bekerja")
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }

        private fun fetchAndNotify(context: Context) {
            try {
                val task = repo.getNearestActiveTask()
                Log.d("Notification Worker", "Task $task" )
                showNotification(context,task)
            } catch (e: Exception) {
                Log.e("Notification Worker","Masalah saat mengambil data ${e.message}")
            }
        }
        private fun showNotification(context: Context, task: Task) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel (
                    channelName,
                    "To do Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.description = task.description
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            val notificationBuilder = NotificationCompat.Builder(context, channelName!!)
                .setContentTitle(task.title)
                .setContentText("Due date: ${DateConverter.convertMillisToString(task.dueDateMillis)}")
                .setContentIntent(getPendingIntent(task))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notifications)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED) {
                    return@with
                }
                notify(1,notificationBuilder.build())
            }
        }

    }
