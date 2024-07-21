package com.example.messagingapp.Repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.messagingapp.model.ChatGroup;
import com.example.messagingapp.model.ChatMessage;
import com.example.messagingapp.views.GroupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repository {


    MutableLiveData<List<ChatGroup>> chatGroupMutableLiveData;
    FirebaseDatabase database;
    DatabaseReference reference;

    DatabaseReference groupRef;

    MutableLiveData<List<ChatMessage>> messagesLiveData;

    public Repository() {

        this.chatGroupMutableLiveData=new MutableLiveData<>();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        messagesLiveData=new MutableLiveData<>();
    }

    public void firebaseAnonymusAuth(Context context){
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i= new Intent(context, GroupActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    }
                });

    }

    public String getCurrentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public void SignOut(){
        FirebaseAuth.getInstance().signOut();
    }


    //Getting chat groups avilable on  the firebase

    public MutableLiveData<List<ChatGroup>> getChatGroupMutableLiveData() {
        List<ChatGroup> groupList=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatGroup group=new ChatGroup(dataSnapshot.getKey());
                    groupList.add(group);
                }

                chatGroupMutableLiveData.postValue(groupList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return chatGroupMutableLiveData;
    }


    //createNewChatGroupMutableLiveData

    public void createNewChatGroup(String groupName){

        reference.child(groupName).setValue(groupName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("FIREBASE_REALTIME","Group Name Added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FIREBASE_REALTIME",e.getLocalizedMessage()+" "+e.getMessage());

            }
        });

    }


    //get Messages Live Data
    public MutableLiveData<List<ChatMessage>> getMessagesLiveData(String groupName) {
        groupRef=database.getReference().child(groupName);
        List<ChatMessage> messageList=new ArrayList<>();
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatMessage message=dataSnapshot.getValue(ChatMessage.class);
                    messageList.add(message);
                }

                messagesLiveData.postValue(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return messagesLiveData;
    }

    public void sendMessage(String message,String chatGroup){
        DatabaseReference ref=database.getReference(chatGroup);

        if(!message.trim().equals("")){
            ChatMessage msg=new ChatMessage(
                    FirebaseAuth.getInstance().getUid(),
                    message,
                    System.currentTimeMillis()
            );
            String randomKey= ref.push().getKey();
            ref.child(randomKey).setValue(msg);
        }
    }
}
