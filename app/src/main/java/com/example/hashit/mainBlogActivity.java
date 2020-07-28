package com.example.hashit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class mainBlogActivity extends AppCompatActivity {
    private ImageView ImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_blog);
        ImageButton=findViewById(R.id.addbutton);
        ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mainBlogActivity.this,AddnewPost.class);
                startActivity(intent);
            }
        });

    }
}
