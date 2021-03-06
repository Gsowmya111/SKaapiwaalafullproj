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
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
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
import java.util.UUID;

import static com.example.sowmyaram.tablelayoutsample.Halfcup_page.half_back;
import static com.example.sowmyaram.tablelayoutsample.Halfcup_page.half_full_cmd;
import static com.example.sowmyaram.tablelayoutsample.Halfcup_page.set_cmd_full_half;
import static com.example.sowmyaram.tablelayoutsample.Spinner.arraySpinner;
import static com.example.sowmyaram.tablelayoutsample.Spinner.arraySpinner_alernate_values;
import static com.example.sowmyaram.tablelayoutsample.Spinner.et_coff_ml;
import static com.example.sowmyaram.tablelayoutsample.Spinner.et_milk_ml;
import static com.example.sowmyaram.tablelayoutsample.Spinner.s;
import static com.example.sowmyaram.tablelayoutsample.Spinner.spinertextval;
import static com.example.sowmyaram.tablelayoutsample.Spinner.val;


public class Blacktea extends Activity {


    byte[] bytesToSend, theByteArray, a2, a1, out, theByteArray3, bytesToSend2,bytesToSend3,theByteArray2;
    Button button;
    BufferedReader br;
    String incoming_data;
    String bytesToSend4;
    ByteArrayOutputStream output;
    Button bset, bback, bread;
    String strReceived = null;
    private static final int REQUEST_ENABLE_BT = 1;
    InputStream in = null;
    BluetoothAdapter bluetoothAdapter;
    EditText etsugarv,etteaval,etwaterspeed;
    String digits;
    LinearLayout lcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_tea);
        Btconnection.handler=handler;


        //initialising ID's
        TextView blacktea= (TextView) findViewById(R.id.tvblacktea);
        Button value= (Button) findViewById(R.id.tvvalue);
        Button tvsugarv= (Button) findViewById(R.id.tvsugarv);
        Button tvteav= (Button) findViewById(R.id.tvteav);
        Button btnwaterspeed= (Button) findViewById(R.id.btnwaterspeed);
        etsugarv= (EditText) findViewById(R.id.etsugarval);
        etteaval= (EditText) findViewById(R.id.etteaval);
        etwaterspeed= (EditText) findViewById(R.id.etwaterspeed);
        bread = (Button) findViewById(R.id.buttonread);
        bset = (Button) findViewById(R.id.buttonset);
        bback = (Button) findViewById(R.id.buttonback);
        lcon = (LinearLayout) findViewById(R.id.lcon);


        //To send *RD# cmd while opening blacktea page
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
          //  Toast.makeText(Blacktea.this, "Connection Problem", Toast.LENGTH_SHORT).show();
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
        etteaval.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 2, afterDecimal = 1;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = etteaval.getText() + source.toString();

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

        //making edittext to take data in 0.0 format
        etwaterspeed.setFilters(new InputFilter[] {
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 2, afterDecimal = 1;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = etwaterspeed.getText() + source.toString();

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
                if ( etsugarv.length()!=0  && etteaval.length()!=0  &&etwaterspeed.length()!=0) {

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


                        bytesToSend = et_milk_ml.getBytes();
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


                        bytesToSend = et_coff_ml.getBytes();
                        zero();
                        bytesToSend4 = "#\n";
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


                    }
                }
                else
                {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Blacktea.this);
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
                   // Toast.makeText(Blacktea.this, "Connection Problem", Toast.LENGTH_SHORT).show();
                }

            }


        });

        //READ button click event
        //To send *RD# cmd to the machine
        bread.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                String bytesToSend1 = half_full_cmd;
                byte[] theByteArray = bytesToSend1.getBytes();

                String data = new String(theByteArray);
                Btconnection.sendbt(data);
                } catch (Exception e) {
                    e.printStackTrace();
                   // Toast.makeText(Blacktea.this, "Connection Problem", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //BACK button click event
        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(half_back.equals("halfcup_page")){
                    Intent  i=new Intent(Blacktea.this, Halfcup_page.class);
                    startActivity(i);
                    finish();
                }else if(half_back.equals("fullcup_page")) {

                    Intent i = new Intent(Blacktea.this, FullCup_page.class);
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
                    //Toast.makeText(Blacktea.this, "Connection Problem", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etteaval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etteaval.setText("");
                spinertextval="etteaval";
                spinnermethod();


            }
        });

        etwaterspeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etwaterspeed.setText("");
                spinertextval="etwaterspeed";
                spinnermethod();
            }
        });


    }

    public  void spinnermethod()
    {
        View alertLayout;
        LayoutInflater inflater1 = getLayoutInflater();
        alertLayout = inflater1.inflate(R.layout.popup, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(Blacktea.this,R.style.MyDialogTheme1);

        alert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        int val1 = s.getSelectedItemPosition();
                        val = s.getSelectedItem().toString();

                        if(spinertextval.equals("etteaval")){
                            etteaval.setText(val);
                            et_milk_ml=(arraySpinner_alernate_values[val1]);
                        } if(spinertextval.equals("etwaterspeed")){
                            et_coff_ml=(arraySpinner_alernate_values[val1]);
                            etwaterspeed.setText(val);
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
            Intent  i=new Intent(Blacktea.this, Halfcup_page.class);
            startActivity(i);
            finish();
        }else if(half_back.equals("fullcup_page")) {

            Intent i = new Intent(Blacktea.this, FullCup_page.class);
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
                        if ((message.startsWith("*")) && (message.endsWith("#"))  && message.length() <= 5) {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    if (message.equals("*CON#")) {
                                       // Toast.makeText(Blacktea.this, "Device Connected", Toast.LENGTH_SHORT).show();
                                        lcon.setBackgroundResource(R.drawable.kapiconect);
                                    } else if (message.equals("*NOC#")) {
                                       // Toast.makeText(Blacktea.this, "Device Busy,not connected", Toast.LENGTH_SHORT).show();
                                        lcon.setBackgroundResource(R.drawable.kaapiwala_tab);
                                    } else if (message.equals("*DCN#")) {
                                        //Toast.makeText(Blacktea.this, "Device disconnected", Toast.LENGTH_SHORT).show();
                                        lcon.setBackgroundResource(R.drawable.kaapiwala_tab);
                                    }

                                    else if(message.equals("*O4#")) {
                                        Toast.makeText(Blacktea.this, "Data Received", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                            break;
                        }
                        else  if (message != null ) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    String bhalf = message.substring(3, 7);
                                    etsugarv.setText(bhalf.replaceAll("[^0-9.]", ""));

                                    //setting tea and water value from spinner
                                    String bfull = message.substring(8, 12);
                                    String shalf = message.substring(13);
                                    String z11 = shalf.replaceAll("[^0-9.]", "");
                                    String bfull1 = bfull.replaceAll("[^0-9.]", "");

                                    for (int j = 0; j <= arraySpinner_alernate_values.length; j++) {
                                        if (arraySpinner_alernate_values[j].equals(bfull1)) {
                                            etteaval.setText(arraySpinner[j]);

                                        }  if (arraySpinner_alernate_values[j].equals(z11)) {
                                            etwaterspeed.setText(arraySpinner[j]);
                                        }
                                    }



                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                       // Toast.makeText(Blacktea.this, "Connection Problem", Toast.LENGTH_SHORT).show();
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
}



