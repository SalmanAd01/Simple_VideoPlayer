package com.example.video_player;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        ArrayList<File> videos = fetchvideos(Environment.getExternalStorageDirectory());
                        String [] items = new String[videos.size()];
                        for(int i=0;i<videos.size();i++){
                            items[i]=videos.get(i).getName().replace(".mp4","");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(MainActivity.this,Video.class);
                                String currentvideo = listView.getItemAtPosition(i).toString();
                                intent.putExtra("currentvideo",currentvideo);
                                intent.putExtra("videolist",videos);
                                intent.putExtra("position",i);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
    public ArrayList<File> fetchvideos(File file){
        ArrayList arrayList = new ArrayList();
        File [] videos = file.listFiles();
        if(videos != null)
        {
            for(File myfile: videos)
            {
                if(!myfile.isHidden() && myfile.isDirectory()){
                    arrayList.addAll(fetchvideos(myfile));
                }
                else{
                    if(myfile.getName().endsWith(".mp4") && !myfile.getName().startsWith(".")){
                        arrayList.add(myfile);
                    }
                }
            }
        }
        return  arrayList;
    }
}