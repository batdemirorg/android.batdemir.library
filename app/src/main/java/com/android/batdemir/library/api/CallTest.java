package com.android.batdemir.library.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CallTest {
    @GET("/orgs/octokit/repos")
    Call<Object> callTest();
}
