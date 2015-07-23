package com.epitrack.guardioes.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.survey.SelectParticipantActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {

    @Bind(R.id.home_fragment_text_view_name)
    TextView textViewName;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setDisplayTitle(false);
        setDisplayLogo(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {

        final View view = inflater.inflate(R.layout.home_fragment, viewGroup, false);

        ButterKnife.bind(this, view);

        String text = getString(R.string.main_fragment_name_message);
        text = text.replace("{0}", "Renatinder");

        textViewName.setText(text);

        return view;
    }

    @OnClick(R.id.home_fragment_text_view_notice)
    public void onNews() {
        navigateTo(NoticeActivity.class);
    }

    @OnClick(R.id.home_fragment_text_view_map)
    public void onMap() {
        navigateTo(MapActivity.class);
    }

    @OnClick(R.id.home_fragment_text_view_tip)
    public void onTip() {
        navigateTo(TipActivity.class);
    }

    @OnClick(R.id.home_fragment_text_view_diary)
    public void onDiary() {
        navigateTo(DiaryActivity.class);
    }

    @OnClick(R.id.home_fragment_text_view_join)
    public void onJoin() {
        navigateTo(SelectParticipantActivity.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }
}