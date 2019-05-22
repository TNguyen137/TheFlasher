package com.android.theflasherapp.restservice;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;

import com.android.theflasherapp.R;
import com.android.theflasherapp.UserInfo;
import com.android.theflasherapp.interfaces.IRestTask;
import com.google.android.gms.common.api.Response;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class RestTask extends AsyncTask<Pair, Void, UserInfo> {
    public IRestTask restTask;

    public RestTask (IRestTask restTask) {
        this.restTask = restTask;
    }

    @Override
    protected UserInfo doInBackground(Pair... params) {
        //String userId = params[0].first.toString();
        HttpMethod httpMethod = (HttpMethod) params[0].second;
        try {
            if (httpMethod == HttpMethod.GET) {
                String userId = params[0].first.toString();
                //String uri = Resources.getSystem().getString(R.string.base_uri);
                final String url = "https://hj07rv3r9k.execute-api.us-east-2.amazonaws.com/test/user/" + userId; //url of where to fetch data
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                UserInfo userInfo;
                userInfo = restTemplate.getForObject(url, UserInfo.class);
                return userInfo;
            } else if (httpMethod == HttpMethod.POST){
                UserInfo userInfo = (UserInfo) params[0].first;
                int userId = userInfo.getId();

                final String url = "https://hj07rv3r9k.execute-api.us-east-2.amazonaws.com/test/user/" + userId; //url of where to fetch data
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                JSONObject postData = new JSONObject();
                postData.put("FirstName", userInfo.getFirstName());
                postData.put("LastName", userInfo.getLastName());
                postData.put("Email", userInfo.getEmail());
                postData.put("Num", userInfo.getNum());
                postData.put("Latlng", userInfo.getLocation());

                HttpEntity<String> requestEntity = new
                        HttpEntity<>(postData.toString(), httpHeaders);

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<UserInfo> responseEntity = restTemplate.exchange(url, HttpMethod.POST,requestEntity, UserInfo.class);
                HttpStatus httpStatus = responseEntity.getStatusCode();
                if (httpStatus == HttpStatus.OK) {
                    return userInfo;
                } else {
                    return userInfo;
                }
            }
        } catch (Exception e) {
            Log.e("UserInfoActivity", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(UserInfo userInfo) {
        restTask.processFinish(userInfo);
    }
}
