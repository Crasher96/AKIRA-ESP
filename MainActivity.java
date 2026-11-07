package com.akira.esp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 48, 32, 32);
        layout.setBackgroundColor(0xFF101010);
        TextView title = new TextView(this);
        title.setText("AKIRA ESP");
        title.setTextColor(0xFFFF303B);
        title.setTextSize(28);
        title.setGravity(17);
        layout.addView(title);
        TextView info = new TextView(this);
        info.setText("Demo overlay indipendente. Non legge i ruoli nascosti di Among Us.");
        info.setTextColor(0xFFFFFFFF);
        info.setTextSize(15);
        info.setPadding(0, 24, 0, 24);
        layout.addView(info);
        Button start = new Button(this);
        start.setText("ATTIVA MENU FLOTTANTE");
        layout.addView(start);
        start.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
                startActivity(i);
            } else {
                startService(new Intent(this, OverlayService.class));
                finish();
            }
        });
        setContentView(layout);
    }
}
