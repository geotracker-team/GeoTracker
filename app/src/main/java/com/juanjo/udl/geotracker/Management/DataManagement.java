package com.juanjo.udl.geotracker.Management;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalAppCompatActivity;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class DataManagement {
    private GlobalAppCompatActivity context;

    public DataManagement(GlobalAppCompatActivity context){
        this.context = context;
    }//constructor

    public void login (String user, String pass, Handler h) throws ExecutionException, InterruptedException {
        loginApi(user, pass, h);
    }//login

    public void getProjectsOfUser(String user, String pass, Handler h) {
        getProjectsOfUserApi(user, pass, h);
    }//getProjects

    public void getRecordsOfProject(String user, String pass, int idProject, Handler h){
        getRecordsOfProjectApi(user,pass,idProject,h);
    }//getRecordsOfProject

    public void addRecord(String user, String pass, JSONRecord record, Handler h) throws IOException, JSONException {
        addRecordApi(user, pass, record, h);
    }//addRecord

    public boolean editRecord(String user, String pass, int idRecrod, JSONRecord record){
        boolean ret = true;
        return context.isConnectionAllowed() && ret;
    }//addRecord

//region API connection
    public final class ConstantesRestApi {
        public static final String ROOT_URL = "http://89.128.4.157:8081/GeoTrackerWeb/";
        public static final String REST_API = "rest/";

        public static final String KEY_LOGIN = "login/{user}/{pass}";
        public static final String KEY_GET_PROJECTS = "projects/{name}/{pass}";
        public static final String KEY_GET_RECORDS = "records/{name}/{pass}/{idProject}";
        public static final String KEY_ADD_RECORD = "addRecord/{name}/{pass}";
        public static final String KEY_EDIT_RECORD = "editRecord/{name}/{pass}/{idRecord}";

        public static final String URL_LOGIN = ROOT_URL + REST_API + KEY_LOGIN;
        public static final String URL_GET_PROJECTS = ROOT_URL + REST_API + KEY_GET_PROJECTS;
        public static final String URL_GET_RECORDS = ROOT_URL + REST_API + KEY_GET_RECORDS;
        public static final String URL_ADD_RECORD = ROOT_URL + REST_API + KEY_ADD_RECORD;
        public static final String URL_EDIT_RECORD = ROOT_URL + REST_API + KEY_EDIT_RECORD;
    }//ConstantesRestApi

    public interface EndpointsApi {
        @GET(ConstantesRestApi.URL_LOGIN)
        Call<ApiResponse> login(@Path("user") String user, @Path("pass") String pass);
        @GET(ConstantesRestApi.URL_GET_PROJECTS)
        Call<ApiResponse> getProjects(@Path("name") String user, @Path("pass") String pass);
        @GET(ConstantesRestApi.URL_GET_RECORDS)
        Call<ApiResponse> getRecords(@Path("name") String user, @Path("pass") String pass, @Path("idProject") int idProject);
        @POST(ConstantesRestApi.URL_ADD_RECORD)
        Call<ApiResponse> addRecord(@Path("name") String user, @Path("pass") String pass, @Body JsonObject record);
        @GET(ConstantesRestApi.URL_EDIT_RECORD)
        Call<ApiResponse> editRecord(@Path("name") String user, @Path("pass") String pass, @Path("idRecord") int idRecord);
    }//EndpointsApi

    public class ApiResponse {
        private boolean isOk;
        private Object extra;

        public boolean isOk(){ return isOk; }
        public Object getExtra() { return extra; }
        public void setOk (boolean isOk) { this.isOk = isOk; }
        public void setExtra(Object extra) { this.extra = extra; }
    }//ApiResponse

    public class RestApiAdapter {
        public EndpointsApi establecerConexionRestApi(Gson gson) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstantesRestApi.ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            return retrofit.create(EndpointsApi.class);
        }

        public Gson convierteGsonDesearilizadorNotificaciones() {
            GsonBuilder gsonBuldier = new GsonBuilder();
            gsonBuldier.registerTypeAdapter(ApiResponse.class, new ApiDesearilizador());

            return gsonBuldier.create();
        }
    }//RestApiAdapter

    public class ApiDesearilizador implements JsonDeserializer<ApiResponse> {
        @Override
        public ApiResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            JsonObject apiResponseData = json.getAsJsonObject();
            ApiResponse ApiResponse;
            try {
                ApiResponse = deserializeResponse(apiResponseData);
            } catch (JSONException e) {
                ApiResponse = new ApiResponse();
                ApiResponse.setOk(false);
                e.printStackTrace();
            }
            return ApiResponse;
        }

        private ApiResponse deserializeResponse(JsonObject notificacionesResponseData) throws JSONException {
            ApiResponse ret = new ApiResponse();
            JSONObject tmp = new JSONObject(notificacionesResponseData.toString());
            ret.setOk(tmp.has("ok") && tmp.getBoolean("ok"));
            if(tmp.has("extra")) ret.setExtra(tmp.get("extra"));
            return ret;
        }
    }//ApiDesearilizador

    public void genericApiCall(Call<ApiResponse> responseCall, final Handler h){
        responseCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse notificationResponse = response.body();
                if (notificationResponse != null)  {
                    Message msg = new Message();
                    msg.what = (notificationResponse.isOk() ? 0 : -1);
                    msg.obj = notificationResponse.getExtra();
                    h.sendMessage(msg);
                }
                else {
                    h.sendEmptyMessage(-1);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Message msg = new Message();
                msg.what = -1;
                msg.obj = t.toString();
                h.sendMessage(msg);
            }
        });
    }//genericApiCall

    public void loginApi(String user, String pass, final Handler h) {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Gson gson = restApiAdapter.convierteGsonDesearilizadorNotificaciones();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi(gson);

        Call<ApiResponse> responseCall = endpointsApi.login(user, pass);

        genericApiCall(responseCall, h);
    }//loginApi

    public void getProjectsOfUserApi(String user, String pass, final Handler h){
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Gson gson = restApiAdapter.convierteGsonDesearilizadorNotificaciones();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi(gson);

        Call<ApiResponse> responseCall = endpointsApi.getProjects(user, pass);

        genericApiCall(responseCall, h);
    }//getProjectsOfUser

    public void getRecordsOfProjectApi(String user, String pass, int idProject, final Handler h){
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Gson gson = restApiAdapter.convierteGsonDesearilizadorNotificaciones();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi(gson);

        Call<ApiResponse> responseCall = endpointsApi.getRecords(user, pass, idProject);

        genericApiCall(responseCall, h);
    }//getProjectsOfUser

    public void addRecordApi(String user, String pass, JSONRecord record, final Handler h) throws JSONException, IOException {
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Gson gson = restApiAdapter.convierteGsonDesearilizadorNotificaciones();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi(gson);

        record.put("otherFields", Constants.AuxiliarFunctions.APPExtraToAPIExtra(record.getOtherFields())); //Adapt the otherFields to make server accept it
        record.remove("userName");
        record.remove("projectName");
        Call<ApiResponse> responseCall = endpointsApi.addRecord(user, pass, gson.fromJson(record.toString(), JsonObject.class));

        genericApiCall(responseCall, h);
    }//getProjectsOfUser
// endregion
}
