package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.Helper
import com.dicoding.todoapp.utils.TASK_ID

class DetailTaskActivity : AppCompatActivity() {
    private lateinit var detailTaskViewModel: DetailTaskViewModel
    private lateinit var titleTextInput: EditText
    private lateinit var detailTextInput: EditText
    private lateinit var dueDateTextInput: EditText
    private lateinit var deleteButton: Button
    private lateinit var factory: ViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        val id = intent.getIntExtra(TASK_ID,0)

        factory = ViewModelFactory.getInstance(this)
        detailTaskViewModel = ViewModelProvider(this,factory)[DetailTaskViewModel::class.java]
        titleTextInput = findViewById(R.id.detail_ed_title)
        detailTextInput = findViewById(R.id.detail_ed_description)
        dueDateTextInput = findViewById(R.id.detail_ed_due_date)
        deleteButton = findViewById(R.id.btn_delete_task)
        //TODO 11 : Show detail task and implement delete action
        detailTaskViewModel.setTaskId(id)

        deleteButton.setOnClickListener {
            finish()
            Helper.showToast(this,"Task berhasil dihapus")
            detailTaskViewModel.deleteTask()
        }
        detailTaskViewModel.task.observe(this) { task ->
            if(task != null) {
                titleTextInput.setText(task.title)
                detailTextInput.setText(task.description)
                dueDateTextInput.setText(task.dueDateMillis.toString())
            } else {
                finish()
            }
        }
    }
}