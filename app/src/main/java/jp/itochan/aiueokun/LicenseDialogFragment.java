package jp.itochan.aiueokun;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;

public class LicenseDialogFragment extends DialogFragment {
    public static final String TAG = LicenseDialogFragment.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View root = View.inflate(getActivity(), R.layout.dialog_license, null);
        WebView webView = (WebView) root.findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/license.html");

        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.open_source_license))
                .setView(root)
                .create();
    }
}