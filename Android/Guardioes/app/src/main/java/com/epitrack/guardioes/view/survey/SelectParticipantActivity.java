package com.epitrack.guardioes.view.survey;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.Constants;
import com.epitrack.guardioes.utility.DateFormat;
import com.epitrack.guardioes.view.base.BaseAppCompatActivity;
import com.epitrack.guardioes.view.menu.profile.Avatar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author Igor Morais
 */
public class SelectParticipantActivity extends BaseAppCompatActivity implements ParentListener {

    @Bind(R.id.text_view_name)
    TextView textViewName;

    @Bind(R.id.text_view_age)
    TextView textViewAge;

    @Bind(R.id.image_view_photo)
    ImageView imageViewAvatar;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.text_view_id)
    TextView textViewId;

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.select_participant);

        //Miquéias Lopes
        SingleUser singleUser = SingleUser.getInstance();
        int j = DateFormat.getDateDiff(singleUser.getDob());

        textViewName.setText(singleUser.getNick());
        textViewAge.setText(j + " Anos");
        textViewId.setText(singleUser.getId());

        if (Integer.parseInt(singleUser.getPicture()) == 0) {

            if (singleUser.getGender().equals("M")) {

                if (singleUser.getRace().equals("branco") || singleUser.getRace().equals("amarelo")) {
                    imageViewAvatar.setImageResource(R.drawable.image_avatar_6);
                } else {
                    imageViewAvatar.setImageResource(R.drawable.image_avatar_4);
                }
            } else {

                if (singleUser.getRace().equals("branco") || singleUser.getRace().equals("amarelo")) {
                    imageViewAvatar.setImageResource(R.drawable.image_avatar_8);
                } else {
                    imageViewAvatar.setImageResource(R.drawable.image_avatar_7);
                }
            }
        } else {
            imageViewAvatar.setImageResource(Avatar.getBy(Integer.parseInt(singleUser.getPicture())).getLarge());
        }

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        final List<User> parentList = new ArrayList<>();

        SimpleRequester simpleRequester = new SimpleRequester();
        simpleRequester.setUrl(Requester.API_URL + "user/household/" + singleUser.getId());
        simpleRequester.setJsonObject(null);
        simpleRequester.setMethod(Method.GET);

        try {
            String jsonStr = simpleRequester.execute(simpleRequester).get();

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.get("error").toString() == "false") {

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                if (jsonArray.length() > 0) {

                    JSONObject jsonObjectHousehold;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObjectHousehold = jsonArray.getJSONObject(i);
                        parentList.add(new User(R.drawable.image_avatar_small_8, jsonObjectHousehold.get("nick").toString(),
                                /*jsonObjectHousehold.get("email").toString()*/"", jsonObjectHousehold.get("id").toString(),
                                jsonObjectHousehold.get("dob").toString(), jsonObjectHousehold.get("race").toString(),
                                jsonObjectHousehold.get("gender").toString(), jsonObjectHousehold.get("picture").toString()));
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(new ParentAdapter(getApplicationContext(), this, parentList));
    }

    @OnClick(R.id.image_view_photo)
    public void onUserSelect() {
        final Bundle bundle = new Bundle();

        bundle.putBoolean(Constants.Bundle.MAIN_MEMBER, true);
        navigateTo(StateActivity.class, bundle);
    }


    @Override
    public void onParentSelect(String id) {
        final Bundle bundle = new Bundle();

        bundle.putString("id_user", id);
        bundle.putBoolean(Constants.Bundle.NEW_MEMBER, false);
        navigateTo(StateActivity.class, bundle);
    }
}
