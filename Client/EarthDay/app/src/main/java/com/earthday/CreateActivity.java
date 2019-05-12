package com.earthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class CreateActivity extends AppCompatActivity {
    private boolean hasGroup;
    public synchronized void hasGroup(boolean hasGroup) {
        this.hasGroup = hasGroup;
        System.out.println(hasGroup);
        this.notify();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    public void functionStartGroup(View view) {
        TextView text_grpcodename = (TextView) findViewById(R.id.textgrpcodename);
        String grpcodename = text_grpcodename.getText().toString();

        boolean pass = true;
        for(char ch: grpcodename.toCharArray()) {
            if(String.valueOf(ch).matches("[^a-zA-Z0-9]")) {
                pass = false;
            }
        }

        if(pass) {
            SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);
            String usercodename = data.getString("usercodename", null);

            // wait until net thread notifies if the group is createable
            synchronized(this) {

                // try to create the group
                hasGroup = true;
                NetCreateGroup net = new NetCreateGroup(this, grpcodename, usercodename);
                net.start();

                try {
                    this.wait(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(!hasGroup) {
                SharedPreferences.Editor editor = data.edit();
                editor.putString("EarthDay", "false");
                editor.putString("groupcodename", grpcodename);
                editor.putString("usertype", "leader");
                editor.commit();

                Intent gotomain = new Intent(view.getContext(), MainActivity.class);
                startActivity(gotomain);
            } else {
                // group already exists dialog
                DialogFragment existsdialog = GroupAreadyExistsDialog.newInstance(
                        R.string.dialog_alreadyexists);
                existsdialog.show(getSupportFragmentManager(), "already exists");
            }

        } else {
            // input error dialog
            DialogFragment inputdialog = InputError.newInstance(
                    R.string.dialog_inputmessage);
            inputdialog.show(getSupportFragmentManager(), "input error");
            pass = true;
        }

    }
}
