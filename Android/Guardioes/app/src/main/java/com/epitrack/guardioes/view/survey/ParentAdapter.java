package com.epitrack.guardioes.view.survey;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.model.SingleUser;
import com.epitrack.guardioes.model.User;
import com.epitrack.guardioes.request.Method;
import com.epitrack.guardioes.request.Requester;
import com.epitrack.guardioes.request.SimpleRequester;
import com.epitrack.guardioes.utility.DateFormat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Igor Morais
 */
public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> implements View.OnClickListener {

    private final ParentListener listener;
    private Context context;

    private List<User> parentList = new ArrayList<>();

    public ParentAdapter(final ParentListener listener, final List<User> parentList) {

        if (listener == null) {
            throw new IllegalArgumentException("The listener cannot be null.");
        }

        this.listener = listener;
        this.parentList = parentList;
    }

    public ParentAdapter(final Context context, final ParentListener listener, final List<User> parentList) {

        if (listener == null) {
            throw new IllegalArgumentException("The listener cannot be null.");
        }

        this.listener = listener;
        this.parentList = parentList;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.text_view_name)
        TextView textViewName;

        @Bind(R.id.text_view_age)
        TextView textViewAge;

        @Bind(R.id.image_view_photo)
        ImageView imageViewPhoto;

        @Bind(R.id.text_view_id_parent)
        TextView textViewId;

        public ViewHolder(final View view) {
            super(view);

            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(context, textViewId.getText(), Toast.LENGTH_SHORT).show();
            listener.onParentSelect(textViewId.getText().toString());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int viewType) {

        final View view = LayoutInflater.from(viewGroup.getContext())
                                        .inflate(R.layout.parent_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        User parent = parentList.get(position);

        viewHolder.textViewName.setText(parent.getNick());
        viewHolder.textViewAge.setText(DateFormat.getDateDiff(parent.getDob()) + " Anos");
        viewHolder.textViewId.setText(parent.getId());

        if (parent.getGender().equals("F")) {
            viewHolder.imageViewPhoto.setImageResource(R.drawable.image_avatar_1);
        } else {
            viewHolder.imageViewPhoto.setImageResource(R.drawable.image_avatar_2);
         }
    }

    @Override
    public int getItemCount() {
        return parentList.size();
    }

}
