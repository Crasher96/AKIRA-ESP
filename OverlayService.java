package com.akira.esp;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OverlayService extends Service {
    private WindowManager wm;
    private View bubble, panel;
    private WindowManager.LayoutParams bubbleParams, panelParams;

    @Override public void onCreate() {
        super.onCreate();
        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        int type = Build.VERSION.SDK_INT >= 26 ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        bubble = new Button(this);
        ((Button)bubble).setText("A");
        ((Button)bubble).setTextColor(0xFFFFFFFF);
        bubble.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE00018));
        bubbleParams = new WindowManager.LayoutParams(58, 58, type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        bubbleParams.gravity = Gravity.TOP | Gravity.END;
        bubbleParams.x = 24; bubbleParams.y = 280;
        wm.addView(bubble, bubbleParams);

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(22, 18, 22, 18);
        box.setBackgroundColor(0xFF171717);
        TextView heading = new TextView(this);
        heading.setText("AKIRA ESP");
        heading.setTextColor(0xFFFF303B);
        heading.setTextSize(24);
        heading.setGravity(Gravity.CENTER);
        box.addView(heading);
        addToggle(box, "ESP ROLES (demo)");
        addToggle(box, "ESP NAMES (demo)");
        addToggle(box, "ESP COLORS (demo)");
        addToggle(box, "PLAYER INFO (demo)");
        TextView note = new TextView(this);
        note.setText("Demo grafica: non mostra ruoli reali e non interagisce con Among Us.");
        note.setTextColor(0xFFBBBBBB);
        note.setPadding(0, 12, 0, 8);
        box.addView(note);
        Button close = new Button(this);
        close.setText("CHIUDI");
        box.addView(close);
        panel = box;
        panelParams = new WindowManager.LayoutParams(
                (int)(getResources().getDisplayMetrics().density * 285),
                WindowManager.LayoutParams.WRAP_CONTENT, type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        panelParams.gravity = Gravity.TOP | Gravity.END;
        panelParams.x = 18; panelParams.y = 350;
        bubble.setOnClickListener(v -> {
            if (panel.getParent() == null) wm.addView(panel, panelParams);
            else wm.removeView(panel);
        });
        close.setOnClickListener(v -> { if (panel.getParent() != null) wm.removeView(panel); });
        bubble.setOnTouchListener(new View.OnTouchListener() {
            private int sx, sy, ox, oy;
            private boolean moved;
            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    sx=(int)e.getRawX(); sy=(int)e.getRawY();
                    ox=bubbleParams.x; oy=bubbleParams.y; moved=false; return false;
                }
                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    int dx=(int)e.getRawX()-sx, dy=(int)e.getRawY()-sy;
                    if (Math.abs(dx)+Math.abs(dy)>12) moved=true;
                    if (moved) { bubbleParams.x=ox-dx; bubbleParams.y=oy+dy; wm.updateViewLayout(bubble,bubbleParams); }
                    return moved;
                }
                return false;
            }
        });
    }
    private void addToggle(LinearLayout box, String label) {
        android.widget.Switch s = new android.widget.Switch(this);
        s.setText(label); s.setTextColor(0xFFFFFFFF);
        s.setPadding(0, 8, 0, 8);
        box.addView(s);
    }
    @Override public int onStartCommand(Intent i, int f, int id) { return START_NOT_STICKY; }
    @Override public void onDestroy() {
        if (bubble != null && bubble.getParent() != null) wm.removeView(bubble);
        if (panel != null && panel.getParent() != null) wm.removeView(panel);
        super.onDestroy();
    }
    @Override public IBinder onBind(Intent i) { return null; }
}
