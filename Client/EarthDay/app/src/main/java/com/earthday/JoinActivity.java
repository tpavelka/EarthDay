package com.earthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class JoinActivity extends AppCompatActivity {
    private boolean hasGroup;
    public synchronized void hasGroup(boolean hasGroup) {
        this.hasGroup = hasGroup;
        this.notify();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
    }

    public void functionJoinGroup(View v) {
        EditText text_enterjoingrp = (EditText) findViewById(R.id.text_enterjoingrp);
        String groupcodename = text_enterjoingrp.getText().toString();
        System.out.println(groupcodename);

        boolean pass = true;
        for(char ch: groupcodename.toCharArray()) {
            if(String.valueOf(ch).matches("[^a-zA-Z0-9]")) {
                pass = false;
            }
        }

        if(pass) {
            SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);
            String usercodename = data.getString("usercodename", null);

            synchronized(this) {

                NetJoinGroup net = new NetJoinGroup(this, groupcodename, usercodename);
                net.start();

                try {
                    this.wait(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(hasGroup) {
                    SharedPreferences.Editor editor = data.edit();
                    editor.putString("groupcodename", groupcodename);
                    editor.putString("usertype", "member");
                    editor.commit();

                    Intent gotomain = new Intent(v.getContext(), MainActivity.class);
                    startActivity(gotomain);
                } else {
                    // group does not exist dialog
                    DialogFragment dnedialog = DoesNotExistDialog.newInstance(
                            R.string.dialog_titledoesnotexist);
                    dnedialog.show(getSupportFragmentManager(), "does not exist");
                }
            }

        } else {
            DialogFragment newFragment = InputError.newInstance(
                    R.string.dialog_inputmessage);
            newFragment.show(getSupportFragmentManager(), "input error");
            pass = true;
        }
    }
}
