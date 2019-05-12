package com.earthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SetUserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercodename);
    }

    public void functionSetUserCodename(View v) {
        SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = data.edit();

        TextView text_usercodename = (TextView) findViewById(R.id.text_setusercodename);
        String usercodename = text_usercodename.getText().toString();

        boolean pass = true;
        for(char ch: usercodename.toCharArray()) {
            if(String.valueOf(ch).matches("[^a-zA-Z0-9]")) {
                pass = false;
            }
        }

        if(pass) {
            editor.putString("usercodename", usercodename);
            editor.putString("usertype", "none");
            editor.commit();

            Intent gotomain = new Intent(v.getContext(), MainActivity.class);
            startActivity(gotomain);
        } else {
            DialogFragment newFragment = InputError.newInstance(
                    R.string.dialog_inputmessage);
            newFragment.show(getSupportFragmentManager(), "input error");
            pass = true;
        }

    }
}
