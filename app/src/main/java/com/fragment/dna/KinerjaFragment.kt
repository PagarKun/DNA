package com.fragment.dna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Nyoba.Employee
import com.Nyoba.EmployeeAdapter
import com.Nyoba.Task
import com.example.dna.R

class KinerjaFragment : Fragment() {

    private lateinit var rvEmployees: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_kinerja, container, false)

        // Initialize RecyclerView dari view yang sudah di-inflate
        rvEmployees = view.findViewById(R.id.rvEmployees)

        // Dummy data
        val employees = getDummyEmployees()

        // Setup RecyclerView
        rvEmployees.layoutManager = LinearLayoutManager(requireContext())
        rvEmployees.adapter = EmployeeAdapter(employees)

        return view
    }

    private fun getDummyEmployees(): List<Employee> {
        return listOf(
            Employee(
                id = 1,
                name = "Budi Santoso",
                position = "Senior Developer",
                tasks = listOf(
                    Task(1, "Develop Login Feature", "In Progress", "2024-12-01"),
                    Task(2, "Fix Bug Dashboard", "Completed", "2024-11-25"),
                    Task(3, "Code Review PR #123", "Pending", "2024-11-30")
                )
            ),
            Employee(
                id = 2,
                name = "Siti Nurhaliza",
                position = "UI/UX Designer",
                tasks = listOf(
                    Task(4, "Design Landing Page", "In Progress", "2024-12-05"),
                    Task(5, "User Research", "Completed", "2024-11-20")
                )
            ),
            Employee(
                id = 3,
                name = "Andi Wijaya",
                position = "Project Manager",
                tasks = listOf(
                    Task(6, "Sprint Planning", "Completed", "2024-11-26"),
                    Task(7, "Client Meeting", "Upcoming", "2024-11-28"),
                    Task(8, "Review Deliverables", "In Progress", "2024-12-02")
                )
            )
        )
    }
}