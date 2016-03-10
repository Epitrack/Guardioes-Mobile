package com.epitrack.guardioes.view.base.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epitrack.guardioes.R;

/**
 * @author Igor Morais
 */
public abstract class BaseDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = BaseDialogFragment.class.getSimpleName();

    private static final String NEGATIVE = "negative";
    private static final String NEUTRAL = "neutral";
    private static final String POSITIVE = "positive";

    private View viewNegative;
    private View viewNeutral;
    private View viewPositive;

    private int requestCode;

    private DialogListener listener;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setStyle(STYLE_NORMAL, R.style.Theme_Base_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup viewGroup, final Bundle bundle) {

        final View view = inflater.inflate(getLayout(), viewGroup, false);

        findView(view);

        return view;
    }

    public void findView(final View view) {

        viewNegative = view.findViewWithTag(NEGATIVE);

        if (viewNegative == null) {

        } else {
            viewNegative.setOnClickListener(this);
        }

        viewNeutral = view.findViewWithTag(NEUTRAL);

        if (viewNeutral == null) {

        } else {
            viewNeutral.setOnClickListener(this);
        }

        viewPositive = view.findViewWithTag(POSITIVE);

        if (viewPositive == null) {

        } else {
            viewPositive.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(final View view) {

        if (POSITIVE.equals(view.getTag())) {
            listener.onPositive(this, requestCode);

        } else if (NEGATIVE.equals(view.getTag())) {
            listener.onNegative(this, requestCode);

        } else if (NEUTRAL.equals(view.getTag())) {
            listener.onNeutral(this, requestCode);
        }
    }

    public final View getViewNegative() {
        return viewNegative;
    }

    public final View getViewNeutral() {
        return viewNeutral;
    }

    public final View getViewPositive() {
        return viewPositive;
    }

    public final int getRequestCode() {
        return requestCode;
    }

    public final BaseDialogFragment setRequestCode(final int requestCode) {
        this.requestCode = requestCode;

        return this;
    }

    public final BaseDialogFragment setListener(final DialogListener listener) {
        this.listener = listener;

        return this;
    }

    public abstract int getLayout();
}
