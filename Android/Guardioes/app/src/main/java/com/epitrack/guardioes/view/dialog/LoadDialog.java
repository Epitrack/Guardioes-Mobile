package com.epitrack.guardioes.view.dialog;

import android.os.Bundle;

import com.epitrack.guardioes.R;
import com.epitrack.guardioes.view.base.dialog.BaseDialogFragment;


/**
 * @author Igor Morais
 */
public class LoadDialog extends BaseDialogFragment {

    public static final String TAG = LoadDialog.class.getSimpleName();

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);

        setStyle(STYLE_NORMAL, R.style.LoadDialog);

        setCancelable(false);
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_load;
    }
}
