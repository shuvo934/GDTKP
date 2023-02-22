package com.shuvo.ttit.trkabikha.connection.retrofit;

import com.shuvo.ttit.trkabikha.projectCreation.ProjectCreationRequest;
import com.shuvo.ttit.trkabikha.projectCreation.ProjectCreationResponse;
import com.shuvo.ttit.trkabikha.projectPicture.PictureResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProjectService {

    @POST("/terrain/tr_kabikha/update_project/project_edit")
    Call<ProjectResponse> updateProject(@Body ProjectRequest projectRequest);

    @POST("/terrain/tr_kabikha/project_creation/insert_project_information")
    Call<ProjectCreationResponse> createProject(@Body ProjectCreationRequest projectCreationRequest);

    @GET("/terrain/tr_kabikha/images/getImages")
    Call<PictureResponse> getPictures(@Query("pcm_id") String pcm);

}

