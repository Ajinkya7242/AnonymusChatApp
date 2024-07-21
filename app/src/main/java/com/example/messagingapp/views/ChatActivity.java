package com.example.messagingapp.views;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.messagingapp.R;
import com.example.messagingapp.databinding.ActivityChatBinding;
import com.example.messagingapp.model.ChatGroup;
import com.example.messagingapp.model.ChatMessage;
import com.example.messagingapp.viewModel.MyViewMOdel;
import com.example.messagingapp.views.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private MyViewMOdel myViewMOdel;
    private ChatAdapter myAdapter;

    private  List<ChatMessage> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding= DataBindingUtil.setContentView(this,R.layout.activity_chat);
        myViewMOdel=new ViewModelProvider(this).get(MyViewMOdel.class);
        binding.recyclerviewMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewMessages.hasFixedSize();
        String groupName=getIntent().getStringExtra("GROUP_NAME");
        myViewMOdel.getMessagesLiveData(groupName).observe(this, new Observer<List<ChatMessage>>() {
            @Override
            public void onChanged(List<ChatMessage> chatMessages) {
                messageList=new ArrayList<>();
                messageList.addAll(chatMessages);
                myAdapter=new ChatAdapter(messageList,getApplicationContext());
                binding.recyclerviewMessages.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
                int latastPostion=myAdapter.getItemCount()-1;
                if(latastPostion>0){
                    binding.recyclerviewMessages.smoothScrollToPosition(latastPostion);

                }
            }
        });


        binding.setVModel(myViewMOdel);

        binding.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message=binding.edtSendMessage.getText().toString();
                if(message.isEmpty()) {
                }
                else{
                    myViewMOdel.sendMessage(message,groupName);
                    binding.edtSendMessage.setText("");
                }
            }
        });


        binding.main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                binding.main.getWindowVisibleDisplayFrame(r);
                int screenHeight = binding.main.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(binding.main);

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is roughly the size of the keyboard
                    // keyboard is opened
                    constraintSet.connect(binding.edtSendMessage.getId(), ConstraintSet.BOTTOM, binding.main.getId(), ConstraintSet.BOTTOM, keypadHeight);
                } else {
                    // keyboard is closed
                    constraintSet.connect(binding.edtSendMessage.getId(), ConstraintSet.BOTTOM, binding.main.getId(), ConstraintSet.BOTTOM, 0);
                }

                constraintSet.applyTo(binding.main);
            }
        });
    }
}