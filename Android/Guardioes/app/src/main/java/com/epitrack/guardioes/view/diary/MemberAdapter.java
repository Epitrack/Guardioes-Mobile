package com.epitrack.guardioes.view.diary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.utility.ViewUtility;
import com.epitrack.guardioes.view.survey.ParentListener;

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

    //private List<Parent> parentList = new ArrayList<>();
    private List<User> usertList = new ArrayList<>();

    private Context context;

    /*public MemberAdapter(final ParentListener listener, final List<Parent> parentList) {

        if (listener == null) {
            throw new IllegalArgumentException("The listener cannot be null.");
        }

        this.listener = listener;
        this.parentList = parentList;
    }*/

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
        ImageView imageViewPhoto;

        @Bind(R.id.view_select)
        View viewSelect;

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

                listener.onParentSelect("");
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
    }

    @Override
    public int getItemCount() {
        return usertList.size();
    }

}
