package com.example.amour.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.amour.R;
import com.bumptech.glide.Glide;
import com.example.amour.match.DetailedProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PhotoAdapter extends ArrayAdapter<User> {
    Context mContext;
    private StorageReference mStorageRef;
    final private String TAG = "PhotoAdapter";

    public PhotoAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final User card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        ImageButton btnInfo = (ImageButton) convertView.findViewById(R.id.checkInfoBeforeMatched);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailedProfile.class);
                intent.putExtra("user", card_item);
                mContext.startActivity(intent);
            }
        });

        name.setText(card_item.getUsername() + ", " + card_item.getAge());
        getImage(card_item.getImage_link(), image);

        return convertView;
    }

    public void getImage(String imageURL, final ImageView image) {
        StorageReference ref = mStorageRef.child(imageURL);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(image);
            }
        });
    }
}
