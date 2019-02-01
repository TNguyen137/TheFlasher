package com.android.theflasherapp.restservice;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.android.theflasherapp.UserInfo;
import com.android.theflasherapp.interfaces.IRestTask;

import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestTask extends AsyncTask<Pair, Void, UserInfo> {
    public IRestTask restTask;

    public RestTask (IRestTask restTask) {
        this.restTask = restTask;
    }

    @Override
    protected UserInfo doInBackground(Pair... params) {
        String userId = params[0].first.toString();
        HttpMethod httpMethod = (HttpMethod) params[0].second;
        try {
            if (httpMethod == HttpMethod.GET) {
                final String url = "https://hj07rv3r9k.execute-api.us-east-2.amazonaws.com/test/user/" + userId; //url of where to fetch data
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                UserInfo userInfo;
                userInfo = restTemplate.getForObject(url, UserInfo.class);
                return userInfo;
            } else if (httpMethod == HttpMethod.POST){

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
