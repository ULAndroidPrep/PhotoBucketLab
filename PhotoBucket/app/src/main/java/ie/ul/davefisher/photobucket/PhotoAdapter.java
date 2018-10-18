package ie.ul.davefisher.photobucket;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

  private List<DocumentSnapshot> mPhotoSnapshots = new ArrayList<>();

  public PhotoAdapter() {
    CollectionReference photosRef = FirebaseFirestore.getInstance().collection("photos");

    photosRef.orderBy("created", Query.Direction.DESCENDING).limit(50)
        .addSnapshotListener(new EventListener<QuerySnapshot>() {
          @Override
          public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e != null) {
              Log.w(MainActivity.TAG, "Listen failed.", e);
              return;
            }
            mPhotoSnapshots = documentSnapshots.getDocuments();
            notifyDataSetChanged();
          }
        });
  }

  @NonNull
  @Override
  public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.caption_only_view, parent, false);
    return new PhotoViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
    DocumentSnapshot photo = mPhotoSnapshots.get(position);
    holder.mCaptionTextView.setText((String)photo.get("caption"));
  }

  @Override
  public int getItemCount() {
    return mPhotoSnapshots.size();
  }

  public class PhotoViewHolder extends RecyclerView.ViewHolder {

    private TextView mCaptionTextView;
    public PhotoViewHolder(View itemView) {
      super(itemView);
      mCaptionTextView = itemView.findViewById(R.id.itemview_caption);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          DocumentSnapshot photo = mPhotoSnapshots.get(getAdapterPosition());

          Context context = v.getContext();
          Intent intent = new Intent(context, PhotoDetailActivity.class);
          intent.putExtra("document_id", photo.getId());
          context.startActivity(intent);
        }
      });
    }
  }
}
