package ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Adapter.Karyawan
import com.RangeAPI.RangeApiResponse
import com.RangeAPI.RetrofitClient
import com.example.dna.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BebanViewModel : ViewModel() {


    private val _karyawanList = MutableLiveData<List<Karyawan>?>()
    val karyawanList: LiveData<List<Karyawan>?> get() = _karyawanList


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    // parameter startDate dan endDate
    fun fetchDataFromApi(startDate: String, endDate: String) {

        if (_karyawanList.value != null) {
            return
        }



        // loading indicator
        _isLoading.value = true

        //  Retrofit
        val client = RetrofitClient.instance.getAssigneeTasks(startDate, endDate)


        // asynchronous
        client.enqueue(object : Callback<RangeApiResponse> {


            override fun onResponse(call: Call<RangeApiResponse>, response: Response<RangeApiResponse>) {
                //loading indicator
                _isLoading.value = false

                if (response.isSuccessful) {
                    // Ambil body dari response
                    val apiResponse = response.body()
                    if (apiResponse != null) {

                        // API ke List<Karyawan>
                        val mappedList = apiResponse.assignees?.map { assignee ->
                            val percentageValue = assignee.onTimePercentage ?: 0.0F
                            val onTimePercentageString = String.format("%.0f%%", percentageValue)
                            Karyawan(
                                id = assignee.clickupId ?: 0 ,
                                foto = R.drawable.profile,
                                nama = assignee.name ?: "Tanpa Nama",
                                keahlian = assignee.role ?: "Role tidak Ditemukan",
                                jamKerja = "${assignee.tasks?.sumOf { it.timeEstimateHours ?: 0 } ?: 0} Jam",
                                periode = "Desember 2025",
                                taskList = emptyList(),
                                totalTaskFromApi = assignee.totalTask ?: 0,
                                totalSpentHoursFromApi = assignee.totalSpentHours ?: 0,
                                totalActualHoursFormApi = assignee.actualWorkHours ?: 0,
                                performanceColor =if ((assignee.onTimePercentage ?: 100f) < 80f) com.example.dna.R.color.red3 else com.example.dna.R.color.hijau3,
                                peformanceTextColor = if ((assignee.onTimePercentage ?: 100f) < 80f) com.example.dna.R.color.red else com.example.dna.R.color.hijaugelap,
                                onTimepersentase = onTimePercentageString
                            )

                        }
                        _karyawanList.value = mappedList
                    } else {
                        _error.value = "Response body is null"
                    }
                } else {
                    _error.value = "Gagal memuat data. Kode: ${response.code()}"
                }
            }


            override fun onFailure(call: Call<RangeApiResponse>, t: Throwable) {
                // loading indicator hilang
                _isLoading.value = false
                //error
                _error.value = "Error Koneksi: ${t.message}"
                Log.e("BebanViewModel", "onFailure: ", t)
            }
        })
    }

    fun clearData() {
        _karyawanList.value = null
    }
}
