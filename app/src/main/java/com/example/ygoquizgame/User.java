package com.example.ygoquizgame;

import java.util.LinkedList;
import java.util.List;

public class User {
    private String duelistName, email, password, bio, profilePic;
    private int numOfAnswers, numOfCorrectAnswers;
    private double correctnessRate;
    private List<String> friendsList;

    public User(){
        duelistName="";
        email="";
        password="";
        bio="";
        profilePic="";
        numOfAnswers=0;
        numOfCorrectAnswers=0;
        correctnessRate=0;
        friendsList=new LinkedList<String>();
    }
    public User(String duelistName, String email, String password){
        this.duelistName=duelistName;
        this.email=email;
        this.password=password;
        bio="";
        profilePic="";
        numOfAnswers=0;
        numOfCorrectAnswers=0;
        correctnessRate=0;
        friendsList=new LinkedList<String>();
    }

    public String getDuelistName(){ return duelistName;}
    public String getEmail(){ return  email;}
    public String getPassword(){ return password;}
    public String getBio() { return bio; }
    public String getProfilePic(){ return profilePic;}
    public int getNumOfAnswers(){ return numOfAnswers;}
    public int getNumOfCorrectAnswers(){ return numOfCorrectAnswers;}
    public double getCorrectnessRate(){
        calculateCorrectnessRate();
        return correctnessRate;
    }
    public List<String> getFriendsList(){ return friendsList;}

    public void setDuelistName(String duelistName){ this.duelistName=duelistName;}
    public void setEmail(String email){ this.email=email;}
    public void setPassword(String password){ this.password=password;}
    public void setBio(String bio){ this.bio=bio;}
    public void setProfilePic(String profilePic){ this.profilePic=profilePic;}
    public void setNumOfAnswers(int numOfAnswers){ this.numOfAnswers=numOfAnswers;}
    public void setNumOfCorrectAnswers(int numOfCorrectAnswers){ this.numOfCorrectAnswers=numOfCorrectAnswers;}
    public void setCorrectnessRate(double correctnessRate){ this.correctnessRate=correctnessRate;}
    public void setFriendsList(LinkedList<String> friendsList){ this.friendsList=friendsList;}

    public void incrementNumOfAnswers(){
        numOfAnswers += 1;
    }
    public void incrementNumOfCorrectAnswers(){
        numOfCorrectAnswers += 1;
    }
    public void calculateCorrectnessRate(){
        if(numOfAnswers!=0)
            correctnessRate=(numOfCorrectAnswers/numOfAnswers)*100;
    }
    public void addFriend(String friendEmail){
        friendsList.add(friendEmail);
    }
}
