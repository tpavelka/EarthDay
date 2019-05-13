package com.earthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.regex.Pattern;

public class ManageActivity extends AppCompatActivity {
    private int layout;

    private String action;
    public synchronized void action(String action) {
        this.action = action;
        this.notify();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);
        if(data.getString("EarthDay", null) != null) {
            if(data.getString("EarthDay", null).equals("false")) {
                layout = 1;
            } else {
                layout = 2;
            }
        } else {
            layout = 1;
        }

        if(layout == 1) {
            displayLayout1();
        } else if(layout == 2) {
            displayLayout2();
        }

        // get the group name to display
        final SharedPreferences.Editor editor = data.edit();
        final CharSequence grpcodename = data.getString("groupcodename", null);

        // display group name for layout 1
        Button btn_grpcodename1 = (Button) findViewById(R.id.btn_grpcodename);
        btn_grpcodename1.setText(grpcodename);

        // display group name for layout 2
        Button btn_grpcodename2 = (Button) findViewById(R.id.btn_grpcodename2);
        btn_grpcodename2.setText(grpcodename);

        // display the earth day activity
        synchronized(this) {
            EditText text_setaction = (EditText) findViewById(R.id.text_grpaction);
            NetGetAction net1 = new NetGetAction(this, grpcodename.toString());
            net1.start();
            try {
                this.wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(action.equals("default")) {
                text_setaction.setText(R.string.text_setgrpaction);
            } else {
                text_setaction.setText(action);
            }
        }

        // for setting the earth day activity
        Button btn_setaction = (Button) findViewById(R.id.btn_grpaction);
        btn_setaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text_setaction = (EditText) findViewById(R.id.text_grpaction);
                CharSequence test = text_setaction.getText();
                Pattern regex = Pattern.compile("^[a-zA-Z0-9 .!$%^*(){}+;,=_~\t-]+$");
                if(regex.matcher(test).find()) {
                    action = text_setaction.getText().toString();
                    NetSetAction net = new NetSetAction(grpcodename.toString(), action);
                    net.start();

                    // error with activity message dialog
                    DialogFragment showcommit = NotifyDialg.newInstance(R.string.dialog_titlenotify);
                    showcommit.show(getSupportFragmentManager(), "validation");


                } else {
                    // error with activity message dialog
                    DialogFragment actdialog = ActivityError.newInstance(
                            R.string.dialog_titleactivity);
                    actdialog.show(getSupportFragmentManager(), "input error");

                }
            }
        });

        // if earthday doesnt exist make it false
        if(data.getString("EarthDay", null) == null) {
            editor.putString("EarthDay", "false");
            editor.commit();
        }

        // for switching to the second layout with the earthday image
        Button btn_turnonearthday = (Button) findViewById(R.id.btn_turnonearthday);
        btn_turnonearthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // assume earthday is false

                SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);
                String grpcodename = data.getString("groupcodename", null);

                // contact server set earthday true
                NetSetEarthDay net = new NetSetEarthDay(grpcodename, "true");
                net.start();
                editor.putString("EarthDay", "true");
                editor.commit();

                displayLayout2();
            }
        });

        // for switching to the first layout with the delete stuff
        Button btn_turnoffearthday = (Button) findViewById(R.id.btn_turnoffearthday);
        btn_turnoffearthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // assume earthday is true

                SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);
                String grpcodename = data.getString("groupcodename", null);

                // contact server set earthday false
                NetSetEarthDay net = new NetSetEarthDay(grpcodename, "false");
                net.start();
                editor.putString("EarthDay", "false");
                editor.commit();

                displayLayout1();
            }
        });

        Button btn_deletegrp = (Button) findViewById(R.id.btn_deletegrp);
        btn_deletegrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text_deletegrp = (EditText) findViewById(R.id.text_deletegrp);
                String presstext = text_deletegrp.getText().toString();

                SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);
                if(presstext.equals(data.getString("groupcodename", null))) {
                    String grpcodename = data.getString("groupcodename", null);
                    NetDeleteGroup net = new NetDeleteGroup(grpcodename);
                    net.start();

                    text_deletegrp.setText("");

                    editor.putString("usertype", "none");
                    editor.putString("groupcodename", null);
                    editor.commit();

                    Intent gotomain = new Intent(view.getContext(), MainActivity.class);
                    startActivity(gotomain);
                }
            }
        });
    }

    public void displayLayout1() {
        // display earth image layout no. 1
        LinearLayout lay1 = (LinearLayout) findViewById(R.id.managelayout1);
        lay1.setVisibility(View.VISIBLE);
        LinearLayout lay2 = (LinearLayout) findViewById(R.id.managelayout2);
        lay2.setVisibility(View.GONE);

        // set layout to no. 1
        layout = 1;
    }

    public void displayLayout2() {
        // display earth image layout no. 2
        LinearLayout lay1 = (LinearLayout) findViewById(R.id.managelayout1);
        lay1.setVisibility(View.GONE);
        LinearLayout lay2 = (LinearLayout) findViewById(R.id.managelayout2);
        lay2.setVisibility(View.VISIBLE);

        // set layout to no. 2
        layout = 2;
    }
}
