package com.konel.kryptapps.onboard.Home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.konel.kryptapps.onboard.R;
import com.konel.kryptapps.onboard.model.User;
import com.konel.kryptapps.onboard.utils.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    // All views
    public ProfileFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.user_name)
    TextView user_name;

    @BindView(R.id.user_email)
    TextView user_email;

    @BindView(R.id.user_phone_number)
    TextView user_phone_number;


    public static ProfileFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);

        FirebaseDatabase.getInstance().getReference("users")
                .child(PreferenceUtil.getString(PreferenceUtil.USER_ID))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        populateUserProfile(user);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return rootView;
    }

    private void populateUserProfile(User user) {
        if (user == null)
            return;

        user_name.setText(user.displayName);
        user_email.setText(user.email);
        user_phone_number.setText(user.phoneNumber);
    }

}
