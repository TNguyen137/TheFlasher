package com.android.theflasherapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.theflasherapp.interfaces.IRestTask;
import com.android.theflasherapp.restservice.RestTask;

import org.springframework.http.HttpMethod;

import java.util.Random;

public class UserInfoActivity extends AppCompatActivity {

    RestTask restTask = new RestTask(new IRestTask() {
        @Override
        public void processFinish(UserInfo userInfo) {
            SetTextViewInfo(userInfo);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Pair <Integer, HttpMethod> pair = new Pair<> (1, HttpMethod.GET);
        restTask.execute(pair);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add items to action bar if it's there
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Pair <Integer, HttpMethod> pair = new Pair<> (2, HttpMethod.GET);
            new RestTask(new IRestTask() {
                @Override
                public void processFinish(UserInfo userInfo) {
                    SetTextViewInfo(userInfo);
                }
            }).execute(pair);
            return true;
        } else if (id == R.id.action_add) {
            UserInfo userInfo = CreateNewRandomUser();
            Pair <UserInfo, HttpMethod> pair = new Pair<> (userInfo, HttpMethod.POST);
            new RestTask(new IRestTask() {
                @Override
                public void processFinish(UserInfo userInfo) {
                    SetTextViewInfo(userInfo);
                }
            }).execute(pair);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_rest, container, false);
            return rootView;
        }
    }

    public void SetTextViewInfo (UserInfo userInfo) {
        TextView userInfoIdText = findViewById(R.id.id_value);
        TextView userInfoFirstNameText = findViewById(R.id.firstName_value);
        TextView userInfoLastNameText = findViewById(R.id.lastName_value);
        TextView userInfoLocationText = findViewById(R.id.location_value);
        TextView userInfoNumText = findViewById(R.id.num_value);
        TextView userInfoEmailText = findViewById(R.id.email_value);

        userInfoIdText.setText(String.valueOf(userInfo.getId()));
        userInfoFirstNameText.setText(userInfo.getFirstName());
        userInfoLastNameText.setText(userInfo.getLastName());
        userInfoLocationText.setText(userInfo.getLocation());
        userInfoNumText.setText(userInfo.getNum());
        userInfoEmailText.setText(userInfo.getEmail());
    }

    public UserInfo CreateNewRandomUser() {
        Random random = new Random();
        int num  = random.nextInt(100);
        int userId = num;
        UserInfo user = new UserInfo(userId, "Atl" + userId, "T" + userId,
                "N" + userId, "4354354" + userId, "TK@TK.TK" + userId);
        return user;
    }
}
