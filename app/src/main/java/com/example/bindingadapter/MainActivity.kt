package com.example.bindingadapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bindingadapter.databinding.ActivityMainBinding
import com.example.bindingadapter.pojo.EtuItem
import com.example.bindingadapter.retrofit.API_Retrofit
import com.example.bindingadapter.view_model.EtuViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: EtuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(EtuViewModel::class.java)
        binding.lifecycleOwner = this@MainActivity
        binding.liveData=viewModel
        viewModel.initData()
        binding.buttonEtudiant.setOnClickListener{getEtudiant()}
        binding.buttonAllEtudiants.setOnClickListener{getAllEtudiants()}

    }

    private fun getAllEtudiants() {
        API_Retrofit.api.getAllEtudiants().enqueue(object : Callback<ArrayList<EtuItem>>{
            override fun onResponse(call: Call<ArrayList<EtuItem>?>, response: Response<ArrayList<EtuItem>?>) {
                if (response.isSuccessful) {
                    Log.d("response", response.body().toString())
                    viewModel.initData()
                    response.body()?.let {
                        viewModel.updateData(it)
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<EtuItem>?>, t: Throwable) {
                Log.e( "onFailure", "onFailure: ${t.toString()}", )
            }
        })

    }

    private fun getEtudiant() {
        API_Retrofit.api.getEtudiant().enqueue(object : Callback<EtuItem> {
            override fun onResponse(call: Call<EtuItem?>, response: Response<EtuItem?>) {
                if (response.isSuccessful) {
                    Log.d("response", response.body().toString())
                    viewModel.initData()
                    response.body()?.let {
                        viewModel.updateData(it as EtuItem)
                    }
                }
            }

            override fun onFailure(call: Call<EtuItem?>, t: Throwable) {
            }
        })

    }
}