package com.konel.kryptapps.onboard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.konel.kryptapps.onboard.Home.Event;
import com.konel.kryptapps.onboard.utils.CodeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_INVITE = 1;
    private static final String TAG = CreateEventActivity.class.getSimpleName();

    private EditText mEventName, mEventDate, mEventDescription, mEventVenue;
    private Button mSave;
    private Toolbar mToolbar;
    private Calendar mCalendar = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener mDatePickerListner = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDate();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        inflateUI();
        populateUI();
        customizeToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void inflateUI() {
        mEventName = (EditText) findViewById(R.id.event_name_edit_text);
        mEventDate = (EditText) findViewById(R.id.event_date_text);
        mEventDescription = (EditText) findViewById(R.id.event_description_edit_text);
        mEventVenue = (EditText) findViewById(R.id.event_venue_text);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSave = (Button) findViewById(R.id.save);
    }

    private void populateUI() {
        mSave.setOnClickListener(this);
        mEventDate.setOnClickListener(this);
    }

    private void customizeToolbar() {
        mToolbar.setTitle("Create Event");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                Event e = createEventObject();
                startInvite();
               /* if (e != null) {
                    //TODO anupam save it to Db
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference userCollection = database.getReference("events");
                    DatabaseReference userObject = userCollection.child(e.getId());
                    userObject.setValue(e);

                } else {
                    showErrorToast();
                }*/
                break;
            case R.id.event_date_text:
                new DatePickerDialog(this, mDatePickerListner, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    private void showErrorToast() {
        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
    }


    private void updateDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        mEventDate.setText(sdf.format(mCalendar.getTime()));
    }

    private Event createEventObject() {
        if (TextUtils.isEmpty(mEventName.getText().toString())
                || TextUtils.isEmpty(mEventDescription.getText().toString())
                || TextUtils.isEmpty(mEventVenue.getText().toString())
                || TextUtils.isEmpty(mEventDate.getText().toString())) {
            return null;
        }
        Event e = new Event();
        e.setId(CodeUtil.getHashFromEventTitle(mEventName.getText().toString()));
        e.setEventName(mEventName.getText().toString());
        e.setEventDate(mEventDate.getText().toString());
        e.setEventDescription(mEventDescription.getText().toString());
        e.setEventVenue(mEventVenue.getText().toString());
        return e;
    }

    private void startInvite() {
        Intent intent = new AppInviteInvitation.IntentBuilder("Invite title")
                .setMessage("hey testing one two three")
                .setDeepLink(getDeeplink("hash#no"))
                .setCallToActionText("Invite")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    private Uri getDeeplink(String hash) {
        String deeplinkUrl = "https://onb.com/" + hash + "/";
        String domain = "h22u9" + ".app.goo.gl";
        DynamicLink.Builder builder = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setDynamicLinkDomain(domain)
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
                        .build())
                .setLink(Uri.parse(deeplinkUrl));

        // Build the dynamic link
        DynamicLink link = builder.buildDynamicLink();
        // [END build_dynamic_link]

        // Return the dynamic link as a URI
        return link.getUri();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                Log.d(TAG, "failed");

            }
        }


    }
}
