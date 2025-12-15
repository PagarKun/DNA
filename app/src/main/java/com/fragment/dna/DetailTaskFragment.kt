package com.fragment.dna

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.DetailTaskAdapter.detailAdapter
import com.example.dna.R
import com.bumptech.glide.Glide
import com.RangeAPI.Assignee
import com.RangeAPI.RangeApiResponse
import com.RangeAPI.Task
import com.RangeAPI.RetrofitClient as RangeRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailTaskFragment : Fragment() {

    private lateinit var taskAdapter: detailAdapter
    private lateinit var rvTasks: RecyclerView

    // Data karyawan
    private lateinit var tvNamaKaryawan : TextView
    private lateinit var tvRoleKaryawan: TextView
    private lateinit var ivEmployeePhoto: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Data Karyawan
        tvNamaKaryawan = view.findViewById(R.id.tv_employee_name)
        tvRoleKaryawan = view.findViewById(R.id.tv_employee_role)
        ivEmployeePhoto = view.findViewById(R.id.iv_employee_photo)

        val employeeId = arguments?.getInt("EMPLOYEE_ID", -1)
        if (employeeId != null && employeeId != -1) {
            fetchKaryawanData(employeeId)
        } else {
            Toast.makeText(requireContext(), "ID Karyawan tidak valid.", Toast.LENGTH_SHORT).show()
        }
        // ---------------------------------

        val btnKembali = view.findViewById<Button>(R.id.btn_kembali_employee)
        btnKembali.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        rvTasks = view.findViewById(R.id.rv_task_list)
        setupRecyclerView()
    }

    private fun fetchKaryawanData(employeeId: Int) {

        val startDate = arguments?.getString("START_DATE")
        val endDate = arguments?.getString("END_DATE")

        if (startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Rentang tanggal tidak valid.", Toast.LENGTH_SHORT).show()
            return
        }



        val client = RangeRetrofitClient.instance.getAssigneeTasks(startDate, endDate)

        //menerima respons  RangeAPI
        client.enqueue(object : Callback<RangeApiResponse> {
            override fun onResponse(call: Call<RangeApiResponse>, response: Response<RangeApiResponse>) {
               if (!isAdded) {
                   return
               }

                if (response.isSuccessful) {
                    val allAssignees = response.body()?.assignees
                    val targetKaryawan = allAssignees?.find { it.clickupId == employeeId }

                    if (targetKaryawan != null) {
                        populateUi(targetKaryawan)
                        taskAdapter.updateTasks(targetKaryawan.tasks ?: emptyList())
                    } else {
                        Toast.makeText(requireContext(), "Karyawan Tidak Ditemukan di RangeAPI", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal memuat data dari RangeAPI. Kode: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RangeApiResponse>, t: Throwable) {
                if (!isAdded) {
                    return
                }
                Toast.makeText(requireContext(), "Error Koneksi RangeAPI: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateUi(dataKaryawan: Assignee) {
        tvNamaKaryawan.text = dataKaryawan.name
        tvRoleKaryawan.text = dataKaryawan.role ?: "Role Tidak Ditemukan"

        Glide.with(this)
            .load(R.drawable.person)
            .placeholder(R.drawable.person)
            .error(R.drawable.apasih)
            .into(ivEmployeePhoto)
    }

    private fun setupRecyclerView() {
        taskAdapter = detailAdapter(emptyList())
        rvTasks.layoutManager = LinearLayoutManager(requireContext())
        rvTasks.adapter = taskAdapter
    }
}
