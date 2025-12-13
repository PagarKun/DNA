package com.fragment.dna

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.DetailTaskAdapter.detailAdapter
import com.RangeAPI.Assignee
import com.example.dna.R
import com.RangeAPI.Task

class DetailTaskFragment : Fragment() {

    private lateinit var taskAdapter: detailAdapter
    private lateinit var rvTasks: RecyclerView
    private var taskListFromBundle: ArrayList<Task>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            taskListFromBundle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableArrayList("EXTRA_TASK", Task::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelableArrayList("EXTRA_TASK")

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnKembali = view.findViewById<Button>(R.id.btn_kembali_employee)

        btnKembali.setOnClickListener {
           parentFragmentManager.popBackStack()
        }


        rvTasks = view.findViewById(R.id.rv_task_list)

        setupRecyclerView()
        loadTasksFromBundle()
    }

    private fun setupRecyclerView() {
        taskAdapter = detailAdapter(emptyList())
        rvTasks.layoutManager = LinearLayoutManager(requireContext())
        rvTasks.adapter = taskAdapter
    }

    private fun loadTasksFromBundle() {

        taskListFromBundle?.let { tasks ->
            taskAdapter.updateTasks(tasks)
        }
    }
}
