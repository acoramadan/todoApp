package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory

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
        factory = ViewModelFactory.getInstance(this)
        detailTaskViewModel = ViewModelProvider(this,factory)[DetailTaskViewModel::class.java]
        titleTextInput = findViewById(R.id.detail_ed_title)
        detailTextInput = findViewById(R.id.detail_ed_description)
        dueDateTextInput = findViewById(R.id.detail_ed_due_date)
        deleteButton = findViewById(R.id.btn_delete_task)

        //TODO 11 : Show detail task and implement delete action
        detailTaskViewModel.task.observe(this) { task ->
            titleTextInput.setText(task.title)
            detailTextInput.setText(task.description)
            dueDateTextInput.setText(task.dueDateMillis.toString())

            deleteButton.setOnClickListener {
                detailTaskViewModel.deleteTask()
            }
        }
    }
}