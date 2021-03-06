package com.example.sowmyaram.tablelayoutsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.example.sowmyaram.tablelayoutsample.Halfcup_page.half_back;
import static com.example.sowmyaram.tablelayoutsample.Halfcup_page.half_full_cmd;
import static com.example.sowmyaram.tablelayoutsample.Halfcup_page.set_cmd_full_half;
import static com.example.sowmyaram.tablelayoutsample.R.id.spiner;
import static com.example.sowmyaram.tablelayoutsample.Spinner.arraySpinner;
import static com.example.sowmyaram.tablelayoutsample.Spinner.arraySpinner_alernate_values;
import static com.example.sowmyaram.tablelayoutsample.Spinner.et_coff_ml;
import static com.example.sowmyaram.tablelayoutsample.Spinner.et_milk_ml;
import static com.example.sowmyaram.tablelayoutsample.Spinner.s;
import static com.example.sowmyaram.tablelayoutsample.Spinner.spinertextval;
import static com.example.sowmyaram.tablelayoutsample.Spinner.val;
import static com.example.sowmyaram.tablelayoutsample.Spinner.val1;


public class Tea extends Activity {


    byte[] bytesToSend, theByteArray, a2, a1, out, theByteArray3, bytesToSend2,bytesToSend3,theByteArray2;
    Button button;
    BufferedReader br;
    String incoming_data;
    Button bset, bback, bread;
    String strReceived = null;
    private static final int REQUEST_ENABLE_BT = 1;
    InputStream in = null;
    BluetoothAdapter bluetoothAdapter;
    EditText etsugarv,etmilkval,etmilkspeed,etcoffeev,etmilkdelay;
    String bytesToSend4;
    ByteArrayOutputStream output;
    String digits;
    LinearLayout lcon;
    Spinner spinerr;


   /* String[] arraySpinner = new String[] {"2","4", "6", "8", "10","12","14","16","18","20","22","24",
            "26","28","30","32","34","36","40","42","44","46","48","50"};*/
   /* String[] arraySpinner_alernate_values = new String[] {"1.2","1.4", "1.6", "1.8", "2.0","2.2","2.3","2.5","2.6","2.8","2.7","2.9",
            "3.0","3.1","3.2","3.3","3.4","3.5","3.6","3.7","3.8","3.9","4.1","4.2"};*/




   /* private static final Set<String> arraySpinner = new HashSet<String>(Arrays.asList(
            new String[] {"2","4", "6", "8", "10","12","14","16","18","20","22","24",
                    "26","28","30","32","34","36","40","42","44","46","48","50"}));*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tea);
        Btconnection.handler=handler;

        //initialising ID's
        TextView sc= (TextView) findViewById(R.id.tvstrongcfe);
        Button value= (Button) findViewById(R.id.tvvalue);
        Button tvsugarv= (Button) findViewById(R.id.tvsugarv);
        Button tvcoffeev= (Button) findViewById(R.id.tvcoffeev);
        Button tvmilkd= (Button) findViewById(R.id.tvmilkd);
        Button tvmilkv= (Button) findViewById(R.id.tvmilkv);
        Button tvmilks= (Button) findViewById(R.id.tvmilks);
        etsugarv= (EditText) findViewById(R.id.etsugarval);
        etcoffeev= (EditText) findViewById(R.id.etteaval);
        etmilkdelay= (EditText) findViewById(R.id.etmilkdelay);
        etmilkval= (EditText) findViewById(R.id.etmilkval);
       // etmilkspeed= (EditText) findViewById(R.id.etmilkspeed);
        bread = (Button) findViewById(R.id.buttonread);
        bset = (Button) findViewById(R.id.buttonset);
        bback = (Button) findViewById(R.id.buttonback);
        lcon = (LinearLayout) findViewById(R.id.lcon);
        etcoffeev.clearFocus();

      //To send *RF# cmd while opening blacktea page and if bluetooth is connected then kaapiwaala tab will turn to green
        try{
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(Btconnection.btconnected=true&& Btconnection.bs.isConnected()) {
                    lcon.setBackgroundResource(R.drawable.kapiconect);
                }
                else
                {
                    lcon.setBackgroundResource(R.drawable.kaapiwala_tab);
                }
                String bytesToSend1 = half_full_cmd;
                byte[] theByteArray = bytesToSend1.getBytes();
                String data = new String(theByteArray);
                Btconnection.sendbt(data);

            }
        });
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(Tea.this, "Connection Problem", Toast.LENGTH_SHORT).show();
        }

        //making edittext to take data in 0.0 format
        etsugarv.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 2, afterDecimal = 1;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = etsugarv.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.0";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        }  else if (temp.length() > 5) {
                            temp = null;
                            temp = (String) source;
                        }else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        //making edittext to take data in 0.0 format
        etcoffeev.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 2, afterDecimal = 1;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = etcoffeev.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.0";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        }  else if (temp.length() > 5) {
                            temp = null;
                            temp = (String) source;
                        }else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        //making edittext to take data in 0.0 format
        etmilkdelay.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 2, afterDecimal = 1;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = etmilkdelay.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.0";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        }  else if (temp.length() > 5) {
                            temp = null;
                            temp = (String) source;
                        }else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });
        //making edittext to take data in 0.0 format
        etmilkval.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 2, afterDecimal = 1;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = etmilkval.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.0";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else if (temp.length() > 5) {
                            temp = null;
                            temp = (String) source;
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }
                }
        });


        //SET button click event
        //Sending data to the machine
        bset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                if ( etsugarv.length()!=0 && etcoffeev.length()!=0 &&etmilkval.length()!=0 && etmilkdelay.length()!=0  ) {

                    if (etsugarv.getText().toString().contains(".")) {


                        String bytesToSend1 = set_cmd_full_half;
                        theByteArray = bytesToSend1.getBytes();

                        bytesToSend = etsugarv.getText().toString().getBytes();
                        zero();
                        bytesToSend4 = ",";
                        theByteArray3 = bytesToSend4.getBytes();
                        output = new ByteArrayOutputStream();
                        //*G,
                        try {
                            output.write(theByteArray);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //00
                        try {
                            output.write(a1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //,
                        try {
                            output.write(theByteArray3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        byte[] out = output.toByteArray();
                        String data = new String(out);
                        Btconnection.sendbt(data);


                        /////////////////////////////////////////////////

                        bytesToSend = et_coff_ml.getBytes();
                    //    bytesToSend = etcoffeev.getText().toString().getBytes();
                        zero();


                        bytesToSend4 = ",";
                        theByteArray3 = bytesToSend4.getBytes();

                        output = new ByteArrayOutputStream();

                        //00
                        try {
                            output.write(a1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //,
                        try {
                            output.write(theByteArray3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        out = output.toByteArray();
                        String data1 = new String(out);
                        Btconnection.sendbt(data1);

/////////////////////////////////////////////////////////////////////*B,00.0,00.0,00.0,00.0,000#


                        bytesToSend = etmilkdelay.getText().toString().getBytes();
                        zero();


                        bytesToSend4 = ",";
                        theByteArray3 = bytesToSend4.getBytes();
                        output = new ByteArrayOutputStream();

                        //00
                        try {
                            output.write(a1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //,
                        try {
                            output.write(theByteArray3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        out = output.toByteArray();
                        String data2 = new String(out);
                        Btconnection.sendbt(data2);

/////////////////////////////////////////////////////////////////
                        bytesToSend = et_milk_ml.getBytes();
                       // bytesToSend = etmilkval.getText().toString().getBytes();
                        zero();


                        bytesToSend4 = "#\n";
                        theByteArray3 = bytesToSend4.getBytes();
                        output = new ByteArrayOutputStream();

                        //00.0
                        try {
                            output.write(a1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //,
                        try {
                            output.write(theByteArray3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        out = output.toByteArray();
                        String data3 = new String(out);
                        Btconnection.sendbt(data3);
/////////////////////////////////////////////////////////////////

                    }

                   /* bytesToSend = etmilkspeed.getText().toString().getBytes();
                    zero2();


                    bytesToSend4 = "#\n";
                    theByteArray3 = bytesToSend4.getBytes();
                    output = new ByteArrayOutputStream();

                    //00.0
                    try {
                        output.write(a1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //,
                    try {
                        output.write(theByteArray3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    out = output.toByteArray();
                    String data4 = new String(out);
                    Btconnection.sendbt(data4);

                */
                }
                else
                {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Tea.this);
                    alertBuilder.setTitle("Invalid Data");
                    alertBuilder.setMessage("Please, Enter data in 00.0 format");
                    alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    alertBuilder.create().show();
                }


                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(Tea.this, "Connection Problem", Toast.LENGTH_SHORT).show();
                }
            }


        });

        //READ button click event
        //To send *RF# cmd to the machine
        bread.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                String bytesToSend1 = half_full_cmd;
                byte[] theByteArray = bytesToSend1.getBytes();

                String data= new String(theByteArray);
                Btconnection.sendbt(data);
                } catch (Exception e) {
                    e.printStackTrace();
                   // Toast.makeText(Tea.this, "Connection Problem", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //BACK button click event
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(half_back.equals("halfcup_page")){
                    Intent  i=new Intent(Tea.this, Halfcup_page.class);
                    startActivity(i);
                    finish();
                }else if(half_back.equals("fullcup_page")) {

                    Intent i = new Intent(Tea.this, FullCup_page.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        // send CON cmd if click on Kaapiwaala tab
        lcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                String bytesToSend1 = "*CON#";
                byte[] theByteArray = bytesToSend1.getBytes();
                String data = new String(theByteArray);
                Btconnection.sendbt(data);
                } catch (Exception e) {
                    e.printStackTrace();
                   // Toast.makeText(Tea.this, "Connection Problem", Toast.LENGTH_SHORT).show();
                }
            }
        });




        etcoffeev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etcoffeev.setText("");
                spinertextval = "etcoffeev";
                spinnermethod();

            }
        });
        etmilkval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etmilkval.setText("");
                spinertextval="etmilkval";
                spinnermethod();
            }
        });


    }

    public  void spinnermethod()
    {
        View alertLayout;
        LayoutInflater inflater1 = getLayoutInflater();
        alertLayout = inflater1.inflate(R.layout.popup, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(Tea.this,R.style.MyDialogTheme1);

        alert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                       val1 = s.getSelectedItemPosition();
                        val = s.getSelectedItem().toString();

                        if(spinertextval.equals("etmilkval")){
                            etmilkval.setText(val);
                          et_milk_ml=(arraySpinner_alernate_values[val1]);
                        } if(spinertextval.equals("etcoffeev")){
                            et_coff_ml=(arraySpinner_alernate_values[val1]);
                            etcoffeev.setText(val);
                        }

                    }
                });




        alert.setView(alertLayout);
        alert.setCancelable(true);
        final AlertDialog dialog1 = alert.create();
        dialog1.show();

       s = (android.widget.Spinner) alertLayout.findViewById(R.id.spiner);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.65);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(half_back.equals("halfcup_page")){
            Intent  i=new Intent(Tea.this, Halfcup_page.class);
            startActivity(i);
            finish();
        }else if(half_back.equals("fullcup_page")) {

            Intent i = new Intent(Tea.this, FullCup_page.class);
            startActivity(i);
            finish();
        }
    }

    //To receive data from machine
    private Handler handler=new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    try {
                        final String message = (String) msg.obj;
                        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if ((message.startsWith("*")) && (message.endsWith("#"))  && message.length() <= 5) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    // Toast.makeText(Logins.this,"incom1 "+message,Toast.LENGTH_LONG).show();

                                    if (message.equals("*CON#")) {
                                       // Toast.makeText(Tea.this, "Device Connected", Toast.LENGTH_SHORT).show();
                                        lcon.setBackgroundResource(R.drawable.kapiconect);
                                    } else if (message.equals("*NOC#")) {
                                       // Toast.makeText(Tea.this, "Device Busy,not connected", Toast.LENGTH_SHORT).show();
                                        lcon.setBackgroundResource(R.drawable.kaapiwala_tab);
                                    } else if (message.equals("*DCN#")) {
                                       // Toast.makeText(Tea.this, "Device disconnected", Toast.LENGTH_SHORT).show();
                                        lcon.setBackgroundResource(R.drawable.kaapiwala_tab);
                                    }

                                    else if(message.equals("*O6#")) {
                                        Toast.makeText(Tea.this, "Data Received", Toast.LENGTH_SHORT).show();

                                    }



                                }
                            });
                            break;
                        }
                        else if (message != null ) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    String bhalf = message.substring(3, 7);
                                    etsugarv.setText(bhalf.replaceAll("[^0-9.]", ""));
                                    String shalf = message.substring(13,17);
                                    etmilkdelay.setText(shalf.replaceAll("[^0-9.]", ""));


                                    String bfull = message.substring(8, 12);
                                    String z1 = message.substring(18);

                                    String z11 = z1.replaceAll("[^0-9.]", "");
                                    String bfull1 = bfull.replaceAll("[^0-9.]", "");
                                    for (int j = 0; j <= arraySpinner_alernate_values.length; j++) {
                                        if (arraySpinner_alernate_values[j].equals(bfull1)) {
                                            etcoffeev.setText(arraySpinner[j]);

                                        }  if (arraySpinner_alernate_values[j].equals(z11)) {
                                            etmilkval.setText(arraySpinner[j]);
                                        }
                                    }


                                }
                            });
                        }




                    } catch (Exception e) {
                        e.printStackTrace();
                       // Toast.makeText(Tea.this, "Connection Problem", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    };


    //Adding zero if input datalength less than 4
    void zero() {
        String a = null;
        a1 = new byte[0];
        if (bytesToSend.length==0) {
            String value = new String(bytesToSend);
            a ="";
            a1 = a.getBytes();
        }
        else if (bytesToSend.length==1) {
            String value = new String(bytesToSend);
            a = "0" + value;
            a1 = a.getBytes();
        }
        else if (bytesToSend.length==2) {
            String value = new String(bytesToSend);
            a = "0" + value;
            a1 = a.getBytes();
        }
        else if (bytesToSend.length==3) {
            String value = new String(bytesToSend);
            a = "0" + value;
            a1 = a.getBytes();
        }
        else if (bytesToSend.length==4) {
            String value = new String(bytesToSend);
            a =  value;
            a1 = a.getBytes();
        }

    }
    void zero2() {
        String a = null;
        a1 = new byte[0];
        if (bytesToSend.length==3) {
            String value = new String(bytesToSend);
            a =  value;
            a1 = a.getBytes();
        }
        else if (bytesToSend.length==2) {
            String value = new String(bytesToSend);
            a = "0"+ value;
            a1 = a.getBytes();
        }
        else if (bytesToSend.length==1) {
            String value = new String(bytesToSend);
            a = "00"+ value;
            a1 = a.getBytes();
        }

    }





}





