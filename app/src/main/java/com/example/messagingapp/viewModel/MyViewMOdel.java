package com.example.messagingapp.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.messagingapp.model.ChatGroup;
import com.example.messagingapp.Repository.Repository;
import com.example.messagingapp.model.ChatMessage;

import java.util.List;

public class MyViewMOdel extends AndroidViewModel {

    Repository mRepository;


    public MyViewMOdel(@NonNull Application application) {
        super(application);
        mRepository=new Repository();
    }

    public void signUpAnonymousUser(){
        Context c=this.getApplication();
        mRepository.firebaseAnonymusAuth(c);
    }

    public String getCurrentUserId(){
        return mRepository.getCurrentUserId();
    }

    public void signOut(){
        mRepository.SignOut();
    }


    public MutableLiveData<List<ChatGroup>> getGroupList(){
        return  mRepository.getChatGroupMutableLiveData();
    }


    public void createNewGroup(String groupName){
        mRepository.createNewChatGroup(groupName);
    }


    //Messages
    public MutableLiveData<List<ChatMessage>> getMessagesLiveData(String groupName){
        return mRepository.getMessagesLiveData(groupName);
    }


    //send messages

    public void sendMessage(String msg,String chatGroup){
         mRepository.sendMessage(msg,chatGroup);
    }
}
