package com.example.sampletestsearchimage.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.sampletestsearchimage.R;
import com.example.sampletestsearchimage.Utilities.Utils;
import com.example.sampletestsearchimage.adapters.CommentListAdapter;
import com.example.sampletestsearchimage.database.SampleDatabase;
import com.example.sampletestsearchimage.database.entity.ImageComments;
import com.example.sampletestsearchimage.database.entity.ImageResponse;
import com.example.sampletestsearchimage.database.entity.ImagesData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ViewImageActivity extends AppCompatActivity implements View.OnClickListener {
    public static String TAG_IMAGE_DATA = "image_data";

    private ImageView imageView;
    private RecyclerView rvComments;
    private EditText editComment;
    private Button submitButton;
    private CommentListAdapter adapter;
    private ArrayList<ImageComments> commentsList = new ArrayList<>();
    private ImageResponse imageResponse;
    private String imageId;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        initUi();
        initAdapter();
        initListener();
        handleIntentData();
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(TAG_IMAGE_DATA))
            imageResponse = (ImageResponse) intent.getSerializableExtra(TAG_IMAGE_DATA);

        if (imageResponse != null) {
            setImageData();
            getCommentsFromLocal(imageId);

            ActionBar supportActionBar = getSupportActionBar();
            supportActionBar.setTitle(imageResponse.getTitle());
        }
    }

    private void initUi() {
        imageView = findViewById(R.id.imageView);
        rvComments = findViewById(R.id.rvComments);
        editComment = findViewById(R.id.commentEditText);
        submitButton = findViewById(R.id.submitButton);
    }

    private void initAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvComments.setLayoutManager(linearLayoutManager);

        adapter = new CommentListAdapter(this, commentsList);
        rvComments.setAdapter(adapter);
    }

    private void initListener() {
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                validateData();
                break;
        }
    }

    private void validateData() {
        String comment = editComment.getText().toString();
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        ImageComments imageComments = new ImageComments(timeInMillis, imageId, timeInMillis, comment);

        if (!Utils.isNullOrBlank(comment)) {
            saveCommentinLocal(imageComments);
            editComment.setText("");
        } else
            Toast.makeText(this, R.string.please_add_valid_comment, Toast.LENGTH_SHORT).show();

    }

    private void saveCommentinLocal(ImageComments imageComments) {
        compositeDisposable.add(
                Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        SampleDatabase.getDatabase(getApplicationContext()).sampleDao().addComment(imageComments);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe((new Action() {
                    public final void run() {
                        commentsList.add(imageComments);
                        adapter.notifyDataSetChanged();
                    }
                })));
    }

    private void getCommentsFromLocal(String imageId) {
        compositeDisposable.add(
                SampleDatabase.getDatabase(getApplicationContext()).sampleDao().getCommentsList(imageId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<ImageComments>>() {
                            @Override
                            public void onSuccess(List<ImageComments> imageComments) {
                                commentsList.addAll(imageComments);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(ViewImageActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        }));
    }

    private void setImageData() {
        if (imageResponse.getImages() != null && imageResponse.getImages().size() > 0) {
            ImagesData images = imageResponse.getImages().get(0);
            imageId = images.getId();
            Glide.with(this)
                    .load(images.getLink())
                    .centerCrop()
                    .override(400, 400)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        }
    }

}
