package com.earthday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static String host = ;
    public static int port = ;

    private boolean hasGroup;
    public synchronized void hasGroup(boolean hasGroup) {
        this.hasGroup = hasGroup;
        this.notify();
    }

    public void launchSetUserCodename(View view) {
        Intent gotosetusercodename = new Intent(view.getContext(), SetUserActivity.class);
        startActivity(gotosetusercodename);
    }

    public void launchCreateGroup(View view) {
        Intent gotocreategrp = new Intent(view.getContext(), CreateActivity.class);
        startActivity(gotocreategrp);
    }

    public void launchManageGroup(View view) {
        Intent gotomanagegrp = new Intent(view.getContext(), ManageActivity.class);
        startActivity(gotomanagegrp);
    }

    public void launchJoinGroup(View view) {
        Intent gotojoingrp = new Intent(view.getContext(), JoinActivity.class);
        startActivity(gotojoingrp);
    }

    public void launchGroupPortal(View view) {
        Intent gotogroup = new Intent(view.getContext(), GroupActivity.class);
        startActivity(gotogroup);
    }

    public void launchAbout(View view) {
        Intent gotoabout = new Intent(view.getContext(), AboutActivity.class);
        startActivity(gotoabout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);

        Button btn_gotosetusercodename = (Button) findViewById(R.id.btngotosetuser);
        Button btn_gotocreategrp = (Button) findViewById(R.id.btn_gotocreategrp);
        Button btn_gotojoingrp = (Button) findViewById(R.id.btn_gotojoingrp);

        System.out.println(data.getString("usercodename", null));

        Button btn_gotoabout = (Button) findViewById(R.id.btn_gotoabout);
        btn_gotoabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAbout(view);
            }
        });

        if(data.getString("usercodename", null) != null) {
            // either goto set user or display username
            btn_gotosetusercodename.setClickable(false);
            btn_gotosetusercodename.setOnClickListener(null);
            btn_gotosetusercodename.setText(data.getString("usercodename", null));

            String usertype = data.getString("usertype", null);
            if(usertype == null) {
                SharedPreferences.Editor editor = data.edit();
                editor.putString("usertype", "none");
                editor.commit();
            }

            if(data.getString("usertype", null).equals("leader")) {
                btn_gotocreategrp.setText(R.string.btn_managegrp);
                btn_gotocreategrp.setClickable(true);
                btn_gotocreategrp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchManageGroup(view);
                    }
                });
                btn_gotojoingrp.setText(R.string.btn_statusleader);
                btn_gotojoingrp.setClickable(false);
                btn_gotojoingrp.setOnClickListener(null);

            } else if(data.getString("usertype", null).equals("member")) {
                btn_gotocreategrp.setText(R.string.btn_statusmember);
                btn_gotocreategrp.setClickable(false);
                btn_gotocreategrp.setOnClickListener(null);

                btn_gotojoingrp.setText(R.string.btn_grpportal);
                btn_gotojoingrp.setClickable(true);
                btn_gotojoingrp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // get what the phone thinks the groupname was
                        SharedPreferences data = getApplicationContext().getSharedPreferences("preferences", 0);
                        String grpcodename = data.getString("groupcodename", null);

                        // test if the users group still exists
                        synchronized(MainActivity.this) {

                            // assume the group doesnt exist
                            MainActivity.this.hasGroup = false;

                            // test if the group still exists
                            NetHasGroup net0 = new NetHasGroup(MainActivity.this, grpcodename);
                            net0.start();

                            // wait for a response
                            try {
                                MainActivity.this.wait(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        if(MainActivity.this.hasGroup) {
                            launchGroupPortal(view);
                        } else {
                            // reset data, goto main menu
                            SharedPreferences.Editor editor = data.edit();
                            editor.putString("EarthDay", null);
                            editor.putString("groupcodename", null);
                            editor.putString("usertype", "none");
                            editor.commit();

                            Intent gotomain = new Intent(view.getContext(), MainActivity.class);
                            startActivity(gotomain);
                        }
                    }
                });

            } else if(data.getString("usertype", null).equals("none")) {
                btn_gotocreategrp.setText(R.string.btn_gotocreategrp);
                btn_gotocreategrp.setClickable(true);
                btn_gotocreategrp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchCreateGroup(view);
                    }
                });
                btn_gotojoingrp.setText(R.string.btn_gotojoingrp);
                btn_gotojoingrp.setClickable(true);
                btn_gotojoingrp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        launchJoinGroup(view);
                    }
                });
            }

        } else {
            btn_gotocreategrp.setText("");
            btn_gotocreategrp.setClickable(false);
            btn_gotocreategrp.setOnClickListener(null);

            btn_gotojoingrp.setText("");
            btn_gotojoingrp.setClickable(false);
            btn_gotojoingrp.setOnClickListener(null);

            btn_gotosetusercodename.setText(R.string.btn_gotosetusercodename);
            btn_gotosetusercodename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchSetUserCodename(view);
                }
            });
        }
    }
}
