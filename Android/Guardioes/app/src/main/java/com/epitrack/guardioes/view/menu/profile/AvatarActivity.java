package com.epitrack.guardioes.view.menu.profile;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.epitrack.guardioes.BuildConfig;
import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.ProfileImage;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.Extension;
import com.epitrack.guardioes.utility.Logger;
import com.epitrack.guardioes.utility.MediaUtility;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.google.android.gms.analytics.HitBuilders;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class AvatarActivity extends BaseAppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = AvatarActivity.class.getSimpleName();

    @Bind(R.id.grid_view)
    GridView gridView;

    private File photoFile;

    private final SelectHandler handler = new SelectHandler();

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ProfileImage profileImage = ProfileImage.getInstance();

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.avatar);

        profileImage.setAvatar("");
        profileImage.setUri(null);

        gridView.setAdapter(new AvatarAdapter(this, Avatar.values()));

        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        getTracker().setScreenName("Select Avatar Screen - " + this.getClass().getSimpleName());
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.avatar, menu);

        return true;
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
        handler.update(view, (Avatar) adapterView.getItemAtPosition(position));
    }

    public void onPhoto(final MenuItem menuItem) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            getTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Take Photo Button")
                    .build());

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File imagesFolder = new File(Environment.getExternalStorageDirectory(), "GDS_Images");
            imagesFolder.mkdirs();

            File image = new File(imagesFolder, "GDS_" + timeStamp + ".png");
            Uri uriSavedImage = Uri.fromFile(image);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            startActivityForResult(takePictureIntent, Constants.RequestCode.IMAGE);
            SingleUser.getInstance().setUri(uriSavedImage);
            profileImage.setUri(uriSavedImage);

            final Intent intent = new Intent();

            intent.putExtra(Constants.Bundle.URI, "");
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @OnClick(R.id.button_photo)
    public void onSave() {
        getTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Select Avatar Button")
                .build());

        if (handler.getAvatar() == null) {

            // Mostrar messagem
            profileImage.setAvatar("");

        } else {

            profileImage.setAvatar(String.valueOf(handler.getAvatar().getId()));

            final Intent intent = new Intent();

            intent.putExtra(Constants.Bundle.AVATAR, handler.getAvatar());

            setResult(RESULT_OK, intent);

            finish();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {

        if (requestCode == Constants.RequestCode.IMAGE && resultCode == RESULT_OK) {
            finish();
        }
    }

    private File getPhotoFile() {

        // TODO: we need to put the name of the user.
        if (photoFile == null) {
            photoFile = MediaUtility.createTempFile("teste1", Extension.PNG, Environment.DIRECTORY_PICTURES);
        }

        return photoFile;
    }

    private class SelectHandler {

        private static final int COLOR_DESELECT = android.R.color.transparent;
        private static final int COLOR_SELECT = R.drawable.round_blue;

        private View view;
        private Avatar avatar;

        private void deselect() {

            if (view == null) {

                if (BuildConfig.DEBUG) {
                    Logger.logDebug(TAG, "The view is null.");
                }

            } else {

                view.setBackgroundResource(COLOR_DESELECT);
            }
        }

        private void select() {

            if (view == null) {

                if (BuildConfig.DEBUG) {
                    Logger.logDebug(TAG, "The view is null.");
                }

            } else {

                view.setBackgroundResource(COLOR_SELECT);
            }
        }

        public final Avatar getAvatar() {
            return avatar;
        }

        public final void update(final View view, final Avatar avatar) {

            deselect();

            this.view = view;
            this.avatar = avatar;

            select();
        }
    }
}
