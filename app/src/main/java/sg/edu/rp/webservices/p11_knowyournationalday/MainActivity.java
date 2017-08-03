package sg.edu.rp.webservices.p11_knowyournationalday;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> listAdapter;
    String[] itemList = {"Singapore Nation Day is on 9 Aug", "Singapore is 52 years old", "Theme is '#OneNationTogether'"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(listAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Tally against the respective action item clicked
        //  and implement the appropriate action
        if (item.getItemId() == R.id.action_quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    // Set text for the negative button and the corresponding
                    //  OnClickListener when it is clicked
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.action_send) {
            String[] list = new String[]{"Email", "SMS"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            String message = "";
                            for (int i = 0; i < itemList.length; i++) {
                                message += (i+1) + ". " + itemList[i] + " \n";
                                if (which == 0) {
                                    Intent email = new Intent(Intent.ACTION_SEND);
                                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"15017359@myrp.edu.sg"});
                                    email.putExtra(Intent.EXTRA_SUBJECT, "More Info For You");
                                    email.putExtra(Intent.EXTRA_TEXT, message);
                                    email.setType("message/rfc822");
                                    startActivity(Intent.createChooser(email, "Choose Email Client:"));

                                } else if (which == 1) {
                                    try{
                                        int permissionCheck = PermissionChecker.checkSelfPermission
                                                (MainActivity.this, Manifest.permission.SEND_SMS);

                                        if (permissionCheck != PermissionChecker.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(MainActivity.this,
                                                    new String[]{Manifest.permission.SEND_SMS}, 0);
                                            // stops the action from proceeding further as permission not
                                            //  granted yet
                                            return;
                                        }

                                        SmsManager.getDefault().sendTextMessage("+6593297113",null,message,null,null);
                                        Intent sendingIntent = new Intent(Intent.ACTION_VIEW);
                                        sendingIntent.setData(Uri.parse("sms:+6593297113"));
                                        sendingIntent.putExtra("sms_body", "");
                                        startActivity(sendingIntent);
                                        Toast.makeText(MainActivity.this,"SMS Send",Toast.LENGTH_LONG).show();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this,"SMS Not Sent",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
