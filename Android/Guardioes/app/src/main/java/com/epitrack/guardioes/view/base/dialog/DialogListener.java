package com.epitrack.guardioes.view.base.dialog;

/**
 * @author Igor Morais
 */
public interface DialogListener {

    void onNegative(BaseDialogFragment dialog, int requestCode);

    void onNeutral(BaseDialogFragment dialog, int requestCode);

    void onPositive(BaseDialogFragment dialog, int requestCode);
}
