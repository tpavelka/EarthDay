package com.earthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {
    private boolean earthday;
    public void setEarthDay(boolean earthday) {
        this.earthday = earthday;
    }
    private ArrayList<String> members;
    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
    private String action;
    public void action(String action) {
        this.action = action;
    }
    private boolean hasGroup;
    public void hasGroup(boolean hasGroup) { this.hasGroup = hasGroup; }

    private int defcon = 0;
    public synchronized void increment() {
        this.defcon++;
        if(defcon >= 3) {
            this.notify();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);
        CharSequence grpcodename = data.getString("groupcodename", null);

        synchronized(this) {

            // assume the group doesnt exist
            this.hasGroup = false;

            // test if the group still exists
            NetHasGroup net0 = new NetHasGroup(this, grpcodename.toString());
            net0.start();

            // wait for a response
            try {
                this.wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(hasGroup) {
            // continue as normal

            // leave group stuff
            Button btn_leavegrp = (Button) findViewById(R.id.btn_leavegrp);
            btn_leavegrp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText text_leavegrp = (EditText) findViewById(R.id.text_leavegrp);
                    String text = text_leavegrp.getText().toString();

                    SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);
                    SharedPreferences.Editor editor = data.edit();
                    String grpcodename = data.getString("groupcodename", null);

                    if (text.equals(grpcodename)) {
                        NetLeaveGroup net = new NetLeaveGroup(grpcodename, data.getString("usercodename", null));
                        net.start();

                        editor.putString("groupcodename", null);
                        editor.putString("usertype", "none");
                        editor.commit();

                        Intent gotomain = new Intent(view.getContext(), MainActivity.class);
                        startActivity(gotomain);
                    }
                }
            });

            synchronized (this) {

                NetGetMembers net1 = new NetGetMembers(this, grpcodename.toString());
                net1.start();
                NetGetEarthDay net2 = new NetGetEarthDay(this, ((String) grpcodename).toString());
                net2.start();
                NetGetAction net3 = new NetGetAction(this, grpcodename.toString());
                net3.start();

                // assume earthday is false
                TextView text_earthheader = (TextView) findViewById(R.id.text_earthheader);
                text_earthheader.setText(R.string.text_notearthday);
                text_earthheader.setVisibility(View.VISIBLE);
                ImageView img_earthday2 = (ImageView) findViewById(R.id.img_earthday2);
                img_earthday2.setVisibility(View.GONE);

                // assume no members
                TextView text_memheader = (TextView) findViewById(R.id.text_memheader);
                text_memheader.setVisibility(View.GONE);

                // display group name
                TextView text_grpcodename2 = (TextView) findViewById(R.id.text_grpcodename2);
                text_grpcodename2.setText(grpcodename);

                // wait until both threads return
                try {
                    this.wait(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            earthDay();
            members();
            TextView text_action = (TextView) findViewById(R.id.text_grpaction2);
            if(action.equals("default")) {
                text_action.setText(R.string.text_setgrpaction);
            } else {
                text_action.setText(action);
            }

            defcon = 0;


        } else {
            // reset data, goto main menu
            SharedPreferences.Editor editor = data.edit();
            editor.putString("EarthDay", null);
            editor.putString("groupcodename", null);
            editor.putString("usertype", "none");
            editor.commit();

            // show message to user the group was deleted
            DialogFragment existsdialog = GroupDeletedDialog.newInstance(
                    R.string.dialog_titlegroupdeleted);
            existsdialog.show(getSupportFragmentManager(), "group deleted");

            // goto main
            Intent gotomain = new Intent(GroupActivity.this, MainActivity.class);
            startActivity(gotomain);

        }
    }

    public void members() {
        if(members.size() > 0) {
            TextView text_memheader = (TextView) findViewById(R.id.text_memheader);
            text_memheader.setVisibility(View.VISIBLE);


            LinearLayout lin = (LinearLayout) findViewById(R.id.lin_lay);
            int counter = 0;
            for(CharSequence member: members) {
                TextView text_member = new TextView(GroupActivity.this);
                text_member.setText(member);
                text_member.setBackgroundColor(Color.parseColor("#4F3900"));
                text_member.setId(counter + 1);

                lin.addView(text_member);
            }
        }
    }

    public void earthDay() {
        if(earthday) {
            TextView text_earthheader = (TextView) findViewById(R.id.text_earthheader);
            text_earthheader.setText(R.string.text_isearthday);
            text_earthheader.setVisibility(View.VISIBLE);
            ImageView img_earthday2 = (ImageView) findViewById(R.id.img_earthday2);
            img_earthday2.setVisibility(View.VISIBLE);
        }
    }
}
