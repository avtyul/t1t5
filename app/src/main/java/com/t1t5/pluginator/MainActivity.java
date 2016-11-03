package com.t1t5.pluginator;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.t1t5.pluginator.control.PluginService;
import com.t1t5.pluginator.control.PluginServiceBinder;
import com.t1t5.pluginator.features.FeaturesStorage;
import com.t1t5.pluginator.network.ApiService;
import com.t1t5.pluginator.network.ApiServiceGenerator;
import com.t1t5.pluginator.network.models.Feature;
import com.t1t5.pluginator.plugins.Action;
import com.t1t5.pluginator.plugins.Event;
import com.t1t5.pluginator.plugins.Plugin;
import com.t1t5.pluginator.plugins.PluginContent;
import com.t1t5.pluginator.plugins.actions.ActivityAction;

import org.apache.http.protocol.HTTP;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends ActionBarActivity {
    Button checkServiceButton;
    Button addPluginButton;
    ServiceConnection serviceConnection;
    PluginServiceBinder pluginServiceBinder;
    Button featuresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!PluginService.isRunning()){
            Intent intent = new Intent(this, PluginService.class);
            startService(intent);
        }
        checkServiceButton = (Button)findViewById(R.id.checkServiceButoon);
        checkServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), PluginService.isRunning().toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });

        addPluginButton = (Button)findViewById(R.id.addPluginButton);
        addPluginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pluginServiceBinder != null){
                    String eventIntent = ((EditText)findViewById(R.id.eventIntent)).getText().toString();
                    String actionIntent = ((EditText)findViewById(R.id.actionIntent)).getText().toString();
                    Event event = new Event(eventIntent);
                    Action action = new ActivityAction(actionIntent);
                    Plugin plugin = new Plugin(getApplicationContext(), new PluginContent(event, action));
                    plugin.enable();
                    pluginServiceBinder.addPlugin(plugin);
                    drawPluginsList();
                }
            }
        });

        featuresButton = (Button)findViewById(R.id.downloadFeatures);
        featuresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService apiService = ApiServiceGenerator.createService(ApiService.class);
                Call<List<Feature>> featuresCall = apiService.getFeatures();
                featuresCall.enqueue(new Callback<List<Feature>>() {
                    @Override
                    public void onResponse(Call<List<Feature>> call, Response<List<Feature>> response) {
                        if (!response.isSuccessful()) {
                            Log.e("NETWORK ERROR", "Response code " + response.code());
                            return;
                        }
                        Log.d("NETWORK SUCCESS", "Features download success");
                        List<Feature> features = response.body();
                        Context applicationContext = getApplicationContext();
                        try{
                            FeaturesStorage.save(features, applicationContext, applicationContext.getString(R.string.FEATURES_STORAGE));
                        }
                        catch (Exception e){
                            Log.e("CANNOT SAVE FEATURES", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Feature>> call, Throwable t) {
                        Log.e("NETWORK ERROR", t.getMessage());
                    }
                });
            }
        });

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                pluginServiceBinder = (PluginServiceBinder)iBinder;
                MainActivity.this.drawPluginsList();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        Intent intent = new Intent(this, PluginService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void drawPluginsList(){
        List<Plugin> pluginList = pluginServiceBinder.getPlugins();
        LinearLayout pluginsListView = (LinearLayout)findViewById(R.id.pluginsList);
        pluginsListView.removeAllViews();
        for(final Plugin plugin: pluginList){
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView textView = new TextView(this);
            Event event = plugin.getPluginContent().getEvent();
            Action action = plugin.getPluginContent().getAction();
            String text = event.getIntentFilter() + ":" + action.getAction();
            textView.setText(text);
            linearLayout.addView(textView);

            Button button = new Button(this);
            button.setText(R.string.deleteButtonText);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pluginServiceBinder.deletePlugin(plugin);
                    drawPluginsList();
                }
            });
            linearLayout.addView(button);

            pluginsListView.addView(linearLayout);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
