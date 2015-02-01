package com.example.ajklen.lostandfound;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnTaskCompleted {


    private static final int ACTIVITY_GOOGLE_PLAY = 1;
    private final String LINK = "http://138.51.236.172/project-118/";

    public static final String MAP_NAME = "Res";
    public static final String MAP_LAT = "Lat";
    public static final String MAP_LONG = "Long";

    private GoogleApiClient googleClient;
    private TextView textView;

    private int mChars = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        textView = (TextView)findViewById(R.id.textView);

        googleClient =  new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //new DownloadTask(this).execute("http://www.google.com");
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleClient.connect();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACTIVITY_GOOGLE_PLAY) {
            if(resultCode == RESULT_OK){
                //String result=data.getStringExtra("result");
            }

            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Please update Google Play APK.", Toast.LENGTH_SHORT).show();
                this.onDestroy();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int returnCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
        if (returnCode != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(returnCode, MainActivity.this, ACTIVITY_GOOGLE_PLAY);
            if (dialog != null) dialog.show();
            //startActivityForResult(intent, ACTIVITY_GOOGLE_PLAY);

        }
    }

    public void onSubmit(View v){
        String request = LINK+"/login.php?username="+((EditText)findViewById(R.id.nameField)).getText()+"&password="+((EditText)findViewById(R.id.password)).getText();
        new DownloadTask(this).execute(request);
    }

    public void getCoord(View v){
        Location lastLocation = getLocation();
        if (lastLocation!=null) {
            textView.setText(String.valueOf(lastLocation.getLatitude())+"\n"+String.valueOf(lastLocation.getLongitude()));
        }
    }

    private Location getLocation(){
        return LocationServices.FusedLocationApi.getLastLocation(googleClient);
    }

    public void createButton(View v){
        Location lastLocation = getLocation();
        Intent intent = new Intent(this, CreateActivity.class);
        if (lastLocation!=null){
            intent.putExtra(CreateActivity.LAT, lastLocation.getLatitude());
            intent.putExtra(CreateActivity.LON, lastLocation.getLongitude());
        }else {
            Log.e("getMap", "Location not found.");
        }

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void callback(String result) {
        Log.d("callback", result);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }
    }
}