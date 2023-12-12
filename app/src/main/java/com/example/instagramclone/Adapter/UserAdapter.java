package com.example.instagramclone.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean isFragment;
    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<User> mUsers, boolean isFragment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user =mUsers.get(position);
        holder.btnFollow.setVisibility(View.VISIBLE);

        holder.pseudo.setText(user.getPseudo());
        holder.prenom.setText(user.getPrenom());
        holder.nom.setText(user.getNom());

        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);

        isFollowed(user.getId(), holder.btnFollow);

        if(user.getId().equals(firebaseUser.getUid())) {
            holder.btnFollow.setVisibility(View.GONE);
        }
        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String currentStatus = holder.btnFollow.getText().toString();
                Log.d("FollowDebug", "Button clicked. Current status: " + currentStatus);

                if(currentStatus.equals("FOLLOW")) {
                    // Mise à jour immédiate du texte du bouton
                    holder.btnFollow.setText("FOLLOWING");
                    Log.d("FollowDebug", "Changed button text to FOLLOWING.");

                    // Ajout de l'utilisateur à la liste de suivi
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(firebaseUser.getUid()).child("following").child(user.getId())
                            .setValue(true)
                            // Autres méthodes on success et on failure
                            .addOnSuccessListener(aVoid -> Log.d("Follow", "Successfully followed: " + user.getId()))
                            .addOnFailureListener(e -> Log.e("Follow", "Failed to follow: " + user.getId(), e));

                    // Ajout de l'utilisateur actuel aux followers de l'utilisateur cible
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(user.getId()).child("followers").child(firebaseUser.getUid())
                            .setValue(true)
                            // Autres méthodes on success et on failure
                            .addOnSuccessListener(aVoid -> Log.d("Follow", "Added to followers: " + firebaseUser.getUid()))
                            .addOnFailureListener(e -> Log.e("Follow", "Failed to add to followers: " + firebaseUser.getUid(), e));

                } else {
                    // Mise à jour immédiate du texte du bouton
                    holder.btnFollow.setText("FOLLOW");
                    Log.d("FollowDebug", "Changed button text to FOLLOW.");

                    // Retrait de l'utilisateur de la liste de suivi
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(firebaseUser.getUid()).child("following").child(user.getId())
                            .removeValue()
                            // Autres méthodes on success et on failure
                            .addOnSuccessListener(aVoid -> Log.d("Follow", "Successfully unfollowed: " + user.getId()))
                            .addOnFailureListener(e -> Log.e("Follow", "Failed to unfollow: " + user.getId(), e));

                    // Retrait de l'utilisateur actuel des followers de l'utilisateur cible
                    FirebaseDatabase.getInstance().getReference().child("Follow")
                            .child(user.getId()).child("followers").child(firebaseUser.getUid())
                            .removeValue()
                            // Autres méthodes on success et on failure
                            .addOnSuccessListener(aVoid -> Log.d("Follow", "Removed from followers: " + firebaseUser.getUid()))
                            .addOnFailureListener(e -> Log.e("Follow", "Failed to remove from followers: " + firebaseUser.getUid(), e));
                }
            }
        });
    }

    private void isFollowed(final String id, final Button btnFollow) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists()){
                    btnFollow.setText("FOLLOWING");
                    Log.d("FollowDebug", "User " + id + " is followed. Button text set to FOLLOWING.");
                } else {
                    btnFollow.setText("FOLLOW");
                    Log.d("FollowDebug", "User " + id + " is not followed. Button text set to FOLLOW.");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FollowDebug", "Firebase error: " + error.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageProfile;
        public TextView nom;
        public TextView prenom;
        public TextView pseudo;
        public Button btnFollow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            prenom = itemView.findViewById(R.id.prenom);
            nom = itemView.findViewById(R.id.nom);
            pseudo = itemView.findViewById(R.id.username);
            btnFollow = itemView.findViewById(R.id.button_follow);

        }
    }
}
