package com.example.vikrant.demoone.Activity

import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.vikrant.demoone.Adapter.DataAdapter
import com.example.vikrant.demoone.Utils.ApiClient
import com.example.vikrant.demoone.Interface.ApiInterface
import com.example.vikrant.demoone.Interface.RecyclerListListener
import com.example.vikrant.demoone.Model.DataModel
import com.example.vikrant.demoone.R
import com.example.vikrant.demoone.Utils.RealmController
import com.example.vikrant.demoone.Utils.Utility
import com.google.gson.Gson
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Debug.getNativeHeapAllocatedSize
import com.example.vikrant.demoone.Model.RealmDataModel
import java.util.*


open class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    private var context: Context = this
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)

        this.realm = RealmController.with(this).realm

        progressBar.visibility = View.GONE

        if (isNetworkConnected(this)) {
            getDataFromAPI()
        } else {
            displayData("Display Data from Database")
        }

    }

    private fun getDataFromAPI() {
        Log.e(TAG, "onResponse")
        progressBar.visibility = View.VISIBLE

        realm?.beginTransaction()
        realm?.deleteAll()
        realm?.commitTransaction()

        val apiService = ApiClient.getClient().create(ApiInterface::class.java)
        val call = apiService.modelData
        call.enqueue(object : Callback<List<DataModel>> {
            override fun onResponse(call: Call<List<DataModel>>, response: Response<List<DataModel>>) {
                Log.e(TAG, "onResponse: $response")
                //Log.e(TAG, "onResponse:DataCount =  " + response.body()!!.data.size)
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {

                    val rs = response.body()
                    val dataList: ArrayList<RealmDataModel> = ArrayList()

                    for (i in 0 until rs!!.size) {

                        var dataModel = RealmDataModel()
                        dataModel.id = rs[i].id
                        dataModel.body = rs[i].body
                        dataModel.title = rs[i].title

                        dataList.add(dataModel)
                    }


                    /*
                    //Without Database

                    val realmAdapter = DataAdapter(context, dataList, listener)
                    // Set the data and tell the RecyclerView to draw
                    recyclerView.adapter =realmAdapter
                    realmAdapter.notifyDataSetChanged()*/


                    try {
                        for (data in dataList) {
                            // Persist your data easily
                            realm?.beginTransaction()
                            realm?.insertOrUpdate(data)
                            realm?.commitTransaction()
                        }

                        displayData("Display Data from API")

                    } catch (e: Exception) {
                        Log.e(TAG, "" + e)
                    }

                }
            }

            override fun onFailure(call: Call<List<DataModel>>, t: Throwable) {
                progressBar.visibility = View.GONE
                t.printStackTrace()
            }
        })
    }

    private fun displayData(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
        RealmController.with(this).refresh()

        val realmAdapter = DataAdapter(this, RealmController.with(this).allDatas, listener)
        // Set the data and tell the RecyclerView to draw
        recyclerView.adapter = realmAdapter
        realmAdapter.notifyDataSetChanged()
    }


    private val listener = RecyclerListListener { _, data, view, id ->
        Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    protected fun displayMemoryUsage(message: String) {
        val usedKBytes = (Debug.getNativeHeapAllocatedSize() / 1024L) as Long
        val usedMegsString = String.format("%s - usedMemory = Memory Used: %d KB", message, usedKBytes)
        Log.e(TAG, usedMegsString)
    }


}
