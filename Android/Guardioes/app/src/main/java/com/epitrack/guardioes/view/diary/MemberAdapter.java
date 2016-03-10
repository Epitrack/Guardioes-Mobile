package com.epitrack.guardioes.view.diary;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.utility.ViewUtility;
import com.epitrack.guardioes.view.menu.profile.Avatar;
import com.epitrack.guardioes.view.survey.ParentListener;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private static final int SELECT = 0;

    private static final float MARGIN_LARGE = 16;
    private static final float MARGIN_SMALL = 8;

    private ViewHolder viewHolder;

    private final ParentListener listener;

    private List<User> usertList = new ArrayList<>();

    private Context context;
    private String userId;
    private SingleUser singleUser = SingleUser.getInstance();

    public MemberAdapter(final Context context, final ParentListener listener, final List<User> parentList) {

        if (listener == null) {
            throw new IllegalArgumentException("The listener cannot be null.");
        }

        this.listener = listener;
        this.usertList = parentList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.text_view_name)
        TextView textViewName;

        @Bind(R.id.image_view_photo)
        CircularImageView imageViewPhoto;

        @Bind(R.id.view_select)
        View viewSelect;

        @Bind(R.id.text_id_view)
        TextView textViewId;

        private boolean select;

        public ViewHolder(final View view) {
            super(view);

            ButterKnife.bind(this, view);

            imageViewPhoto.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {

            if (viewSelect.getVisibility() == View.INVISIBLE) {

                viewSelect.setVisibility(View.VISIBLE);
                viewHolder.viewSelect.setVisibility(View.INVISIBLE);

                viewHolder = this;

                listener.onParentSelect(viewHolder.textViewId.getText().toString());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {

        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.diary_member_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final User parent = usertList.get(position);

        if (position == SELECT) {

            ViewUtility.setMarginLeft(viewHolder.imageViewPhoto,
                                      ViewUtility.toPixel(viewHolder.itemView.getContext(), MARGIN_LARGE));

            viewHolder.viewSelect.setVisibility(View.VISIBLE);

            this.viewHolder = viewHolder;

        } else {

            ViewUtility.setMarginLeft(viewHolder.imageViewPhoto,
                    ViewUtility.toPixel(viewHolder.itemView.getContext(), MARGIN_SMALL));
        }

        if (position == getItemCount() - 1) {
            ViewUtility.setMarginRight(viewHolder.imageViewPhoto,
                    ViewUtility.toPixel(viewHolder.itemView.getContext(), MARGIN_LARGE));

        } else {

            ViewUtility.setMarginRight(viewHolder.imageViewPhoto,
                                       ViewUtility.toPixel(viewHolder.itemView.getContext(), MARGIN_SMALL));
        }

        viewHolder.textViewName.setText(parent.getNick());

        userId = parent.getId();
        viewHolder.textViewId.setText(userId);

        if (parent.getPicture().equals("")) {
            parent.setPicture("0");
        }

        if (parent.getPicture().length() > 2) {
            String url = parent.getPicture();
            if (url.substring(0, 4).toLowerCase().equals("http")) {
                parent.setPicture("0");
                if (parent.getGender().equals("M")) {

                    if (parent.getRace().equals("branco") || parent.getRace().equals("amarelo")) {
                        viewHolder.imageViewPhoto.setImageResource(R.drawable.image_avatar_6);
                    } else {
                        viewHolder.imageViewPhoto.setImageResource(R.drawable.image_avatar_4);
                    }
                } else {

                    if (parent.getRace().equals("branco") || parent.getRace().equals("amarelo")) {
                        viewHolder.imageViewPhoto.setImageResource(R.drawable.image_avatar_8);
                    } else {
                        viewHolder.imageViewPhoto.setImageResource(R.drawable.image_avatar_7);
                    }
                }
            } else {
                Uri uri = Uri.parse(parent.getPicture());
                viewHolder.imageViewPhoto.setImageURI(uri);
            }
        } else {
            if (Integer.parseInt(parent.getPicture()) == 0) {

                if (parent.getGender().equals("M")) {

                    if (parent.getRace().equals("branco") || parent.getRace().equals("amarelo")) {
                        viewHolder.imageViewPhoto.setImageResource(R.drawable.image_avatar_6);
                    } else {
                        viewHolder.imageViewPhoto.setImageResource(R.drawable.image_avatar_4);
                    }
                } else {

                    if (parent.getRace().equals("branco") || parent.getRace().equals("amarelo")) {
                        viewHolder.imageViewPhoto.setImageResource(R.drawable.image_avatar_8);
                    } else {
                        viewHolder.imageViewPhoto.setImageResource(R.drawable.image_avatar_7);
                    }
                }
            } else {
                viewHolder.imageViewPhoto.setImageResource(Avatar.getBy(Integer.parseInt(parent.getPicture())).getLarge());
            }
        }

    }

    @Override
    public int getItemCount() {
        return usertList.size();
    }

}
