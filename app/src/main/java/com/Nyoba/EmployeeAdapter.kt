package com.Nyoba

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dna.R

class EmployeeAdapter(private val employees: List<Employee>) :
    RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {

    inner class EmployeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEmployeeName: TextView = view.findViewById(R.id.tvEmployeeName)
        val tvEmployeePosition: TextView = view.findViewById(R.id.tvEmployeePosition)
        val ivExpandArrow: ImageView = view.findViewById(R.id.ivExpandArrow)
        val rvTasks: RecyclerView = view.findViewById(R.id.rvTasks)
        val layoutExpandable: LinearLayout = view.findViewById(R.id.layoutExpandable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_employee, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employees[position]

        holder.tvEmployeeName.text = employee.name
        holder.tvEmployeePosition.text = employee.position

        // Setup child RecyclerView
        holder.rvTasks.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvTasks.adapter = TaskAdapter(employee.tasks)

        // Show/hide expandable section
        holder.layoutExpandable.visibility = if (employee.isExpanded) View.VISIBLE else View.GONE

        // Rotate arrow
        holder.ivExpandArrow.rotation = if (employee.isExpanded) 180f else 0f

        // Click listener for expand/collapse
        holder.itemView.setOnClickListener {
            employee.isExpanded = !employee.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = employees.size
}
