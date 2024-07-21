package com.example.messagingapp.views;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagingapp.model.ChatGroup;
import com.example.messagingapp.R;
import com.example.messagingapp.databinding.ActivityGroupBinding;
import com.example.messagingapp.viewModel.MyViewMOdel;
import com.example.messagingapp.views.adapter.GroupAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    private ArrayList<ChatGroup> chatGroupArrayList;
    private RecyclerView recyclerViee;
    private GroupAdapter adapter;
    private ActivityGroupBinding binding;
    private MyViewMOdel myViewMOdel;
    private Dialog chatGroupDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding= DataBindingUtil.setContentView(this,R.layout.activity_group);
        myViewMOdel=new ViewModelProvider(this).get(MyViewMOdel.class);

        recyclerViee=binding.recyclerview;
        recyclerViee.setLayoutManager(new LinearLayoutManager(this));
        myViewMOdel.getGroupList().observe(this, new Observer<List<ChatGroup>>() {
            @Override
            public void onChanged(List<ChatGroup> chatGroups) {
                chatGroupArrayList=new ArrayList<ChatGroup>();
                chatGroupArrayList.addAll(chatGroups);

                adapter=new GroupAdapter(chatGroupArrayList);
                recyclerViee.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog(){
        chatGroupDialog=new Dialog(this);
        chatGroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_layout,null);
        chatGroupDialog.setContentView(view);

        WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
        layoutParams.copyFrom(chatGroupDialog.getWindow().getAttributes());
        layoutParams.width=WindowManager.LayoutParams.MATCH_PARENT;
        chatGroupDialog.getWindow().setAttributes(layoutParams);

        Button submit=view.findViewById(R.id.btnCreateBtn);
        EditText edt=view.findViewById(R.id.edtGroupName);

        chatGroupDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName=edt.getText().toString();
                if(!groupName.isEmpty()){
                    myViewMOdel.createNewGroup(groupName);
                    chatGroupDialog.dismiss();
                }
                else{
                    Toast.makeText(GroupActivity.this,"Please Enter Group Name", Toast.LENGTH_SHORT).show();

                }
                Toast.makeText(GroupActivity.this,"Your Chat Group Name is : -"+groupName, Toast.LENGTH_SHORT).show();
            }
        });

    }
}