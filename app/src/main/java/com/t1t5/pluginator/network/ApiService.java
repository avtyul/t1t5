package com.t1t5.pluginator.network;

import com.t1t5.pluginator.network.models.Feature;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/*
 * Created by kvukolov on 03.11.16.
 */
public interface ApiService {
    @GET("features/")
    Call<List<Feature>> getFeatures();
}
