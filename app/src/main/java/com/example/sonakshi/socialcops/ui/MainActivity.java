package com.example.sonakshi.socialcops.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.sonakshi.socialcops.R;
import com.example.sonakshi.socialcops.databinding.ActivityMainBinding;
import com.example.sonakshi.socialcops.notifs.AlarmNotificationReceiver;
import com.example.sonakshi.socialcops.ui.headlines.HeadlinesFragment;
import com.example.sonakshi.socialcops.ui.news.NewsFragment;
import com.example.sonakshi.socialcops.ui.news.OptionsBottomSheet;
import com.example.sonakshi.socialcops.ui.search.SearchFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OptionsBottomSheet.OptionsBottomSheetListener {
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private ActivityMainBinding binding;
    private HeadlinesFragment headlinesFragment;
    private SearchFragment searchFragment;
    private NewsFragment newsFragment;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case R.id.navigation_headlines:
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, headlinesFragment)
                            .commit();
                    return true;
                case R.id.navigation_saved:
                    if (newsFragment == null) {
                        newsFragment = NewsFragment.newInstance(null);
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, newsFragment)
                            .commit();
                    return true;
                case R.id.navigation_search:
                    if (searchFragment == null) {
                        searchFragment = SearchFragment.newInstance();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, searchFragment)
                            .commit();
                    return true;
            }
            return false;
        }
    };
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Bind data using DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            // Add a default fragment
            headlinesFragment = HeadlinesFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, headlinesFragment)
                    .commit();
        }
        startAlarm();
        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            binding.toolbar.setContentInsetsAbsolute(10, 10);
        }
    }

    @Override
    public void onSaveToggle(String text) {
        if (snackbar == null) {
            snackbar = Snackbar.make(binding.coordinator, "Hello", Snackbar.LENGTH_SHORT);
            final CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackbar.getView().getLayoutParams();
            params.setMargins(
                    (int) getResources().getDimension(R.dimen.snackbar_margin_vertical),
                    0,
                    (int) getResources().getDimension(R.dimen.snackbar_margin_vertical),
                    (int) getResources().getDimension(R.dimen.snackbar_margin_horizontal)
            );
            snackbar.getView().setLayoutParams(params);
            snackbar.getView().setPadding(
                    (int) getResources().getDimension(R.dimen.snackbar_padding),
                    (int) getResources().getDimension(R.dimen.snackbar_padding),
                    (int) getResources().getDimension(R.dimen.snackbar_padding),
                    (int) getResources().getDimension(R.dimen.snackbar_padding)
            );
        }
        if (snackbar.isShown()) {
            snackbar.dismiss();
        }
        snackbar.setText(text);
        snackbar.show();
    }

    private void startAlarm() {
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,15);


        myIntent = new Intent(MainActivity.this,AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0);


        manager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()+3000,pendingIntent);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
    }
}
