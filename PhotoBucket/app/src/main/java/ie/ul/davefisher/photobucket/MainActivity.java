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
          imageUrl = randomImageUrl();
        }
        photo.put("imageUrl", imageUrl);
        photo.put("created", new Date());
        FirebaseFirestore.getInstance().collection("photos").add(photo);
      }
    });
    builder.setNegativeButton(android.R.string.cancel, null);

    builder.create().show();
  }

  public String randomImageUrl() {
    String[] urls = new String[]{
        "https://www.irishcentral.com/images/FT5%20s-ivory-coast-flag%20copy.jpg",
        "https://d5qsyj6vaeh11.cloudfront.net/hugoandcat/images_tidy_2014/destinations/counties/clare/cliffs-of-moher-hero.jpg",
        "https://www.laoistoday.ie/wp-content/uploads/2018/08/Limerick-win-All-Ireland-Hurling-FInal-e1534699452532-640x424.jpg",
        "https://www.ilovelimerick.ie/wp-content/uploads/2018/08/rsz_rsz_img_2408-620x400.jpg",
        "https://i2.wp.com/www.thesun.ie/wp-content/uploads/sites/3/2018/08/1559565-e1534691508773.jpg?crop=0px%2C794px%2C4565px%2C2162px&resize=940%2C400&ssl=1",
        "https://cdn.extra.ie/wp-content/uploads/2018/08/19193944/li8-696x406.jpg",
        "https://lynceans.org/wp-content/uploads/2017/02/David-Fisher.jpg",
        "https://www.irishtimes.com/polopoly_fs/1.3072115.1493933893!/image/image.jpg_gen/derivatives/box_620_330/image.jpg",
        "https://img.maximummedia.ie/her_ie/eyJkYXRhIjoie1widXJsXCI6XCJodHRwOlxcXC9cXFwvbWVkaWEtaGVyLm1heGltdW1tZWRpYS5pZS5zMy5hbWF6b25hd3MuY29tXFxcL3dwLWNvbnRlbnRcXFwvdXBsb2Fkc1xcXC8yMDE4XFxcLzA1XFxcLzMxMTE1NjA5XFxcL1VuaXZlcnNpdHktb2YtTGltZXJpY2suanBnXCIsXCJ3aWR0aFwiOjc2NyxcImhlaWdodFwiOjQzMSxcImRlZmF1bHRcIjpcImh0dHBzOlxcXC9cXFwvd3d3Lmhlci5pZVxcXC9hc3NldHNcXFwvaW1hZ2VzXFxcL2hlclxcXC9uby1pbWFnZS5wbmc_dj01XCJ9IiwiaGFzaCI6IjQxNWI5YmM4Nzk1YTczZGNjZDBiNDhlODg3MWY3M2ZjODAzNGY2OTUifQ==/university-of-limerick.jpg",
        "http://ulsites.ul.ie/mlal/sites/default/files/living%20bridge%20night.jpg",
        "https://img.maximummedia.ie/joe_ie/eyJkYXRhIjoie1widXJsXCI6XCJodHRwOlxcXC9cXFwvbWVkaWEtam9lLm1heGltdW1tZWRpYS5pZS5zMy5hbWF6b25hd3MuY29tXFxcL3dwLWNvbnRlbnRcXFwvdXBsb2Fkc1xcXC8yMDE3XFxcLzAyXFxcLzE3MTYzMTA5XFxcL2xpbWVyaWNrLTEwMjR4NjgxLmpwZWdcIixcIndpZHRoXCI6NzY3LFwiaGVpZ2h0XCI6NDMxLFwiZGVmYXVsdFwiOlwiaHR0cHM6XFxcL1xcXC93d3cuam9lLmllXFxcL2Fzc2V0c1xcXC9pbWFnZXNcXFwvam9lXFxcL25vLWltYWdlLnBuZz92PTVcIn0iLCJoYXNoIjoiZDkyNTViY2I2NWNmNDI1YWE5M2NmOGI2MTMwYzhjODJlODE5OGE3NCJ9/limerick-1024x681.jpeg",
        "http://tkdo.ul.ie/training/UL_map.jpg",
    };
    return urls[(int) (Math.random() * urls.length)];
  }
}
