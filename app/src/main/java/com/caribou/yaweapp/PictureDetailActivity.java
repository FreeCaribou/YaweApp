package com.caribou.yaweapp;



import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.caribou.yaweapp.ApiUrl.ListOfApiUrl;
import com.caribou.yaweapp.adapter.CommentArrayAdapter;
import com.caribou.yaweapp.model.CommentPicture;
import com.caribou.yaweapp.task.DeleteAsyncTask;
import com.caribou.yaweapp.task.GetAsyncTask;
import com.caribou.yaweapp.task.PostCommentPictureAsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PictureDetailActivity extends AppCompatActivity implements GetAsyncTask.GetAsyncTaskCallback {

    TextView tvTitle;
    TextView tvAuthor;
    TextView tvDescription;
    ImageView ivPicture;
    String imgUrl;
    long idAuthor;
    long id;
    private Toolbar toolbar;
    boolean isAdmin;
    boolean isAuthor;
    EditText edComment;
    Button btComment;
    ListView lvComment;
    long idUser;
    ArrayList<CommentPicture> listComments;
    LinearLayout ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        setTitle("Picture");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        isAdmin = false;
        isAuthor = false;

        id = 0;
        imgUrl = "";
        tvTitle = (TextView) findViewById(R.id.tv_pictureDetail_title);
        tvAuthor = (TextView) findViewById(R.id.tv_pictureDetail_author);
        tvDescription = (TextView) findViewById(R.id.tv_pictureDetail_descritpion);
        ivPicture = (ImageView) findViewById(R.id.iv_pictureDetail_image);
        lvComment = (ListView) findViewById(R.id.lv_commentPicture);
        edComment = (EditText) findViewById(R.id.ed_pictureDetail_comment);
        btComment = (Button) findViewById(R.id.bt_pictureDetail_comment);
        ll = (LinearLayout) findViewById(R.id.ll_pictureDetail);

        final Bundle extra = this.getIntent().getExtras();
        if(extra != null){
            id = extra.getLong("id_picture");

            GetAsyncTask task = new GetAsyncTask(PictureDetailActivity.this);
            task.execute(ListOfApiUrl.getUrlAllCommentPictureByIdPicture(String.valueOf(id)));

            tvTitle.setText(extra.getString("title_picture"));
            setTitle(extra.getString("title_picture"));
            tvAuthor.setText(extra.getString("author_picture"));
            tvDescription.setText(extra.getString("description_picture"));

            listComments = new ArrayList<>();
            registerForContextMenu(lvComment);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PictureDetailActivity.this);

            idUser = prefs.getLong("id_user", 0);
            idAuthor = extra.getLong("id_author");
            isAuthor = (idUser == idAuthor);
            isAdmin = prefs.getBoolean("isAdmin", false);


            ImageLoader mImageLoader;
            NetworkImageView mNetworkImageView = (NetworkImageView) findViewById(R.id.iv_pictureDetail_image);
            mImageLoader = CustomVolleyRequestQueue.getInstance(PictureDetailActivity.this).getImageLoader();
            //Image URL - This can point to any image file supported by Android
            final String url = extra.getString("url_picture");
            imgUrl = extra.getString("url_picture");
            mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView,
                    R.drawable.yawe_logo, android.R.drawable
                            .ic_dialog_alert));
            mNetworkImageView.setImageUrl(url, mImageLoader);

            ivPicture.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(PictureDetailActivity.this);
                    alert.setTitle("Download this picture?");
                    alert.setMessage("You will be redirected to your browser");

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Uri webpage = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }
                        }
                    });

                    alert.setNegativeButton("No please", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog show = alert.create();
                    show.show();

                    return false;
                }
            });
        }

        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edComment.getText().toString().equals("")){
                    Toast.makeText(PictureDetailActivity.this, "Message is empty...", Toast.LENGTH_SHORT).show();
                } else {
                    CommentPicture cp = new CommentPicture();
                    cp.setId_picture(id);
                    cp.setId_user(idUser);
                    cp.setText(edComment.getText().toString());
                    cp.setPostDate(Calendar.getInstance().getTime());

                    SharedPreferences loginPref = PreferenceManager.getDefaultSharedPreferences(PictureDetailActivity.this);
                    cp.setAuthor_name(loginPref.getString("loginUsername", ""));

                    PostCommentPictureAsyncTask task = new PostCommentPictureAsyncTask();
                    task.execute(cp);
                    edComment.setText("");
                    GetAsyncTask taske = new GetAsyncTask(PictureDetailActivity.this);
                    taske.execute(ListOfApiUrl.getUrlAllCommentPictureByIdPicture(String.valueOf(id)));
                }
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_picture, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_deletePicture:

                if(isAuthor || isAdmin){
                    AlertDialog.Builder alert = new AlertDialog.Builder(PictureDetailActivity.this);
                    alert.setTitle(R.string.are_you_sur);
                    alert.setMessage("You are going to delete this beautiful picture!");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DeleteAsyncTask delete = new DeleteAsyncTask();
                            delete.execute(ListOfApiUrl.getUrlDeletePicture(String.valueOf(id)));
                            finish();
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(PictureDetailActivity.this, "Picture not deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog show = alert.create();
                    show.show();
                } else {
                    Toast.makeText(this, "You aren't admin or the author of this picture, you can't delete this", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_editPicture:
                if(isAuthor){
                    Intent goDetail = new Intent(PictureDetailActivity.this, EditPictureActivity.class);
                    goDetail.putExtra("id_picture", id);
                    goDetail.putExtra("title_picture", tvTitle.getText().toString());
                    goDetail.putExtra("description_picture", tvDescription.getText().toString());
                    goDetail.putExtra("url_picture", imgUrl);
                    goDetail.putExtra("id_author", idAuthor);
                    startActivity(goDetail);
                    finish();
                } else {
                    Toast.makeText(this, "You aren't the author of this picture, you can't edit this", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return true;
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     * src: http://blog.lovelyhq.com/setting-listview-height-depending-on-the-items/
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }
            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onPreGet() {

    }

    @Override
    public void onPostGet(String sJSON) {
        try {
            JSONArray jResponse = new JSONArray(sJSON);
            listComments.removeAll(listComments);

            for (int i=0;i<jResponse.length(); i++){
                JSONObject jEvent = jResponse.getJSONObject(i);
                String sDate = jEvent.getString("postDate");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date d = sdf.parse(sDate);
                long id = jEvent.getLong("id");
                String text = jEvent.getString("text");
                long id_user = jEvent.getLong("id_user");
                long id_picture = jEvent.getLong("id_picture");
                Date date = new Date(d.getTime());
                String author_name = jEvent.getString("name");
                CommentPicture ncp = new CommentPicture(id, text, date, id_user, id_picture, author_name);
                Log.i("toString cp: ", ncp.toString());

                listComments.add(ncp);
            }

            CommentArrayAdapter adapter = new CommentArrayAdapter(PictureDetailActivity.this, listComments);
            lvComment.setAdapter(adapter);
            setListViewHeightBasedOnItems(lvComment);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //    public final static String APP_PATH_SD_CARD = "/YaweApp/";
//
//    public boolean saveImageToExternalStorage(Bitmap image) {
//        Toast.makeText(this, "Begin downloading", Toast.LENGTH_SHORT).show();
//        Log.i("sbire: ", "debut download");
//        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD ;//+ APP_THUMBNAIL_PATH_SD_CARD;
//        Log.i("sbire: ", "chemin acces");
//        try {
//            File dir = new File(fullPath);
//            if (!dir.exists()) {
//                dir.mkdir();
//            }
//
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            String nameFile = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
//            Log.i("nom fichier: ", nameFile);
//
//            OutputStream fOut = null;
//            File file = new File(fullPath, nameFile);
//            file.createNewFile();
//            fOut = new FileOutputStream(file);
//
//            // 100 means no compression, the lower you go, the stronger the compression
//            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//            fOut.flush();
//            fOut.close();
//            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
//            Toast.makeText(PictureDetailActivity.this, "Picture download in YaweApp folder", Toast.LENGTH_SHORT).show();
//            return true;
//
//        } catch (Exception e) {
//            Log.e("saveToExternalStorage()", e.getMessage());
//            return false;
//        }
//    }
}
