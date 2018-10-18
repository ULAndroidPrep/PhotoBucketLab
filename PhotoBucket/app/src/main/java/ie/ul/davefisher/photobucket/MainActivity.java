package ie.ul.davefisher.photobucket;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

  public static final String TAG = "PB";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    
    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setHasFixedSize(true);

    final PhotoAdapter photoAdapter = new PhotoAdapter();
    recyclerView.setAdapter(photoAdapter);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        showAddDialog();
      }
    });
  }


  private void showAddDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Add a new Photo");
    View view = getLayoutInflater().inflate(R.layout.photo_dialog, null, false);
    builder.setView(view);
    final EditText captionEditText = view.findViewById(R.id.dialog_caption_edittext);
    final EditText imageUrlEditText = view.findViewById(R.id.dialog_image_url_edittext);

    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Map<String, Object> photo = new HashMap<>();
        photo.put("caption", captionEditText.getText().toString());
        String imageUrl = imageUrlEditText.getText().toString();
        if (imageUrl.isEmpty()) {
          imageUrl = "https://www.irishcentral.com/images/FT5%20s-ivory-coast-flag%20copy.jpg";
        }
        photo.put("imageUrl", imageUrl);
        photo.put("created", new Date());
        FirebaseFirestore.getInstance().collection("photos").add(photo);
      }
    });
    builder.setNegativeButton(android.R.string.cancel, null);

    builder.create().show();
  }
}
