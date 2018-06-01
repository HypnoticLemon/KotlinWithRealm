package com.example.vikrant.demoone.Interface;

import com.example.vikrant.demoone.Model.DataModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Vikrant on 12-01-2018.
 */

public interface ApiInterface {

    @GET("posts")
    Call<List<DataModel>> getModelData();
}
