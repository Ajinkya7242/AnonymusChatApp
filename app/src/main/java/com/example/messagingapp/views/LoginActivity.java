package com.example.messagingapp.views;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.messagingapp.R;
import com.example.messagingapp.databinding.ActivityLoginBinding;
import com.example.messagingapp.viewModel.MyViewMOdel;

public class LoginActivity extends AppCompatActivity {
    MyViewMOdel myViewMOdel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myViewMOdel=new ViewModelProvider(this).get(MyViewMOdel.class);

        ActivityLoginBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        binding.setVModel(myViewMOdel);
    }
}