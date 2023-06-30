package com.company0ne.demoretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import com.company0ne.demoretrofit.Api.ApiClient
import com.company0ne.demoretrofit.Api.ApiInterface
import com.google.gson.JsonArray
import com.squareup.picasso.Picasso
import org.json.JSONArray
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var arrayList: ArrayList<HomeViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrayList = ArrayList()

        retroFitArray()
    }

    //this is for Array
    private fun retroFitArray() {
        val apiInterface = ApiClient.client.create(ApiInterface::class.java)
        val call: Call<JsonArray> = apiInterface.getData()
        call.enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    val array = JSONArray(response.body().toString())
                    Log.d("jsonArrayData", array.toString())

                    //when the data in the form of Array we always use this for loop
                    for (i in 0 until array.length()) {
                        val model = HomeViewModel()
                        val objects = array.getJSONObject(i)

                        //find and set the data
                        model.albumId = objects.getString("albumId")
                        model.id = objects.getString("id")
                        model.title = objects.getString("title")
                        model.url = objects.getString("url")
                        arrayList.add(model)
                    }
                    buildRecycler()
                }
            }
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }
    private fun buildRecycler() {
        val recycler: RecyclerView = findViewById(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = DataAdapter(arrayList)
    }

    private class DataAdapter(var list: ArrayList<HomeViewModel>) :
        RecyclerView.Adapter<DataAdapter.DataViewHolder>() {
        class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            //for imageView
            val imageView: ImageView = itemView.findViewById(R.id.image)

            val textViewAlbumId: TextView = itemView.findViewById(R.id.albumId)
            val textViewId: TextView = itemView.findViewById(R.id.id)
            val textViewTitle: TextView = itemView.findViewById(R.id.app_title)
            val textViewUrl: TextView = itemView.findViewById(R.id.app_Url)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.data_item, parent, false)
            return DataViewHolder(view)

        }

        override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
            val model = list[position]

            // Load image using Picasso
            Picasso.get().load(model.url).into(holder.imageView)

            // Set other data
            holder.textViewAlbumId.text = model.albumId
            holder.textViewId.text = model.id
            holder.textViewTitle.text = model.title
            holder.textViewUrl.text = model.url

        }

        override fun getItemCount(): Int {
            return list.size
        }
    }
}