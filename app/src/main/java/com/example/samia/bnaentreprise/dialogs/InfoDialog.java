package com.example.samia.bnaentreprise.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.samia.bnaentreprise.R;

public class InfoDialog extends Dialog {

    private View.OnClickListener listener;

    public InfoDialog(@NonNull Context context, String errorStr) {
        super(context);
        setContentView(R.layout.dialog_info);

        ((TextView)findViewById(R.id.error_tv)).setText(errorStr);

        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(findViewById(R.id.ok_btn));
                dismiss();
            }
        });
    }

    public InfoDialog setCancellable(boolean b){
        setCancelable(b);
        return this;
    }

    public InfoDialog title(@Nullable String title){
        ((TextView)findViewById(R.id.title_tv)).setText(title == null ? getContext().getString(R.string.app_name) : title);
        return this;
    }

    public InfoDialog setOnButtonClickListener(View.OnClickListener listener){
        this.listener = listener;
        return this;
    }
}
