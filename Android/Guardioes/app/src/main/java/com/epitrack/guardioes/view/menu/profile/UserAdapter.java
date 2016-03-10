package com.epitrack.guardioes.view.menu.profile;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Igor Morais
 */
public class UserAdapter extends ArrayAdapter<User> {

    private final UserListener listener;
    SingleUser singleUser = SingleUser.getInstance();
    private ArrayList<User> userArrayList;

    public UserAdapter(final Context context, final ArrayList<User> userList, final UserListener listener) {
        super(context, 0, userList);

        this.userArrayList = userList;

        //Miquéias Lopes

        /*SingleUser singleUser = SingleUser.getInstance();

        userList.add(new User(R.drawable.image_avatar_small_2, singleUser.getNick(), singleUser.getEmail(), singleUser.getId(),
                singleUser.getDob(), singleUser.getRace(), singleUser.getGender(), singleUser.getPicture()));

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

                        User user = new User(R.drawable.image_avatar_small_8, jsonObjectHousehold.get("nick").toString(),
                                        "", jsonObjectHousehold.get("id").toString(),
                                        jsonObjectHousehold.get("dob").toString(), jsonObjectHousehold.get("race").toString(),
                                        jsonObjectHousehold.get("gender").toString(), jsonObjectHousehold.get("picture").toString());
                        try {
                            user.setRelationship(jsonObjectHousehold.get("relationship").toString());
                        } catch (Exception e) {
                            user.setRelationship("");
                        }

                        try {
                            user.setEmail(jsonObjectHousehold.get("email").toString());
                        } catch (Exception e) {
                            user.setEmail("");
                        }

                        userList.add(user);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        this.listener = listener;
    }

    class ViewHolder {

        TextView textViewName;
        TextView textViewType;
        CircularImageView imageViewImage;
        ImageView imageTrash;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup viewGroup) {
        View view = convertView;

        ViewHolder viewHolder;

        if (view == null) {

            view = LayoutInflater.from(viewGroup.getContext())
                                 .inflate(R.layout.user_item, viewGroup, false);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = (TextView) view.findViewById(R.id.text_view_name);
            viewHolder.textViewType = (TextView) view.findViewById(R.id.text_view_type);
            viewHolder.imageViewImage = (CircularImageView) view.findViewById(R.id.image_view_image);

            view.findViewById(R.id.linear_layout).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View itemView) {
                    listener.onEdit(getItem(position));
                }
            });

            view.findViewById(R.id.image_view_trash).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View itemView) {
                    listener.onDelete(getItem(position));
                }
            });

            view.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        final User user = userArrayList.get(position); //getItem(position);

        if (user.getId() == singleUser.getId()) {
            viewHolder.imageTrash = (ImageView) view.findViewById(R.id.image_view_trash);
            viewHolder.imageTrash.setVisibility(View.INVISIBLE);
        }

        viewHolder.textViewName.setText(user.getNick());
        viewHolder.textViewType.setText(user.getType());

        if (user.getPicture() == null) {
            user.setPicture("0");
        }

        if (user.getPicture().equals("")) {
            user.setPicture("0");
        }

        if (user.getPicture().length() > 2) {
            try {
                String url = user.getPicture();

                if (url.substring(0, 4).toLowerCase().equals("http")) {
                    user.setPicture("0");
                    //viewHolder.imageViewImage.setImageResource(Avatar.getBy(Integer.parseInt(user.getPicture())).getSmall());
                    if (user.getGender().equals("M")) {

                        if (user.getRace().equals("branco") || user.getRace().equals("amarelo")) {
                            viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_6);
                        } else {
                            viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_4);
                        }
                    } else {

                        if (user.getRace().equals("branco") || user.getRace().equals("amarelo")) {
                            viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_8);
                        } else {
                            viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_7);
                        }
                    }
                } else {
                    Uri uri = Uri.parse(user.getPicture());
                    File file = new File(user.getPicture());

                    if (!file.exists()) {
                        viewHolder.imageViewImage.setImageURI(uri);
                        Drawable drawable = viewHolder.imageViewImage.getDrawable();
                        viewHolder.imageViewImage.setImageDrawable(drawable);

                        if (drawable == null) {
                            if (user.getGender().equals("M")) {

                                if (user.getRace().equals("branco") || user.getRace().equals("amarelo")) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_6);
                                } else {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_4);
                                }
                            } else {

                                if (user.getRace().equals("branco") || user.getRace().equals("amarelo")) {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_8);
                                } else {
                                    viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_7);
                                }
                            }
                        }
                    } else {
                        viewHolder.imageViewImage.setImageURI(uri);
                    }
                }
            } catch (Exception e) {
                user.setPicture("0");
                //viewHolder.imageViewImage.setImageResource(Avatar.getBy(Integer.parseInt(user.getPicture())).getSmall());
                if (user.getGender().equals("M")) {

                    if (user.getRace().equals("branco") || user.getRace().equals("amarelo")) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_6);
                    } else {
                        viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_4);
                    }
                } else {

                    if (user.getRace().equals("branco") || user.getRace().equals("amarelo")) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_8);
                    } else {
                        viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_7);
                    }
                }
            }
        } else {
            user.setPicture(user.getPicture());

            if (Integer.parseInt(user.getPicture()) == 0) {

                if (user.getGender().equals("M")) {

                    if (user.getRace().equals("branco") || user.getRace().equals("amarelo")) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_6);
                    } else {
                        viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_4);
                    }
                } else {

                    if (user.getRace().equals("branco") || user.getRace().equals("amarelo")) {
                        viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_8);
                    } else {
                        viewHolder.imageViewImage.setImageResource(R.drawable.image_avatar_small_7);
                    }
                }
            } else {
                viewHolder.imageViewImage.setImageResource(Avatar.getBy(Integer.parseInt(user.getPicture())).getSmall());
            }
        }

        return view;
    }
}