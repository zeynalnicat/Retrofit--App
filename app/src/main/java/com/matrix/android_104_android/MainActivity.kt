package com.matrix.android_104_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.matrix.android_104_android.adapter.ProductAdapter
import com.matrix.android_104_android.databinding.ActivityMainBinding
import com.matrix.android_104_android.model.Products
import com.matrix.android_104_android.retrofit.ProductApi
import com.matrix.android_104_android.retrofit.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var retrofit: ProductApi? = null
    private var adapter: ProductAdapter? = null
    private var selectedCategory: String = "All categories"
    private var listCategory: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofit = RetrofitInstance.productApi
        setSpinner()
        setAdapter()


    }

    private fun setSpinner() {
        val spinner = binding.spinner
        CoroutineScope(Dispatchers.IO).launch {
            val categoriesResponse = retrofit?.getCategories()
            val categories = categoriesResponse

            withContext(Dispatchers.Main) {
                listCategory.add("All categories")
                categories?.let {
                    it.body()?.let { it1 -> listCategory.addAll(it1) }
                }

                val arrayAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_dropdown_item, listCategory
                )
                spinner.adapter = arrayAdapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        selectedCategory = listCategory[p2]
                        binding.spinner.setSelection(p2)
                        setAdapter()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
        }
    }


    private fun setAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            var productsResponse: Response<Products>?

            if (selectedCategory == "All categories") {
                productsResponse = retrofit?.getProducts()
            } else {
                productsResponse = retrofit?.getSpecific(selectedCategory)
            }


            Log.i("products", productsResponse.toString())
            val products = productsResponse?.body()

            if (products?.size == 0) {
                binding.progressBar.visibility = View.VISIBLE
            }
            withContext(Dispatchers.Main) {
                products?.let {
                    binding.progressBar.visibility = View.GONE
                    adapter = ProductAdapter(it)
                    binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                    binding.recyclerView.adapter = adapter
                }


            }
        }
    }
}

