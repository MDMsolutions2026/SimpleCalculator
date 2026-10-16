
package com.example.simplecalculator;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    TextView display;
    String first = "", op = "";
    boolean fresh = true;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(1);
        root.setPadding(16, 32, 16, 16);
        root.setBackgroundColor(Color.rgb(22, 28, 40));

        display = new TextView(this);
        display.setText("0");
        display.setTextSize(42);
        display.setTextColor(Color.WHITE);
        display.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        root.addView(display, new LinearLayout.LayoutParams(-1, 0, 1));

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(4);
        String[] keys = {"AC","±","%","÷","7","8","9","×",
                         "4","5","6","−","1","2","3","+","0",".","⌫","="};

        for (String k : keys) {
            Button btn = new Button(this);
            btn.setText(k);
            btn.setTextSize(20);
            btn.setTextColor(Color.WHITE);
            btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                "+−×÷=".contains(k) ? 0xff397bf5 : 0xff273041));
            GridLayout.LayoutParams p = new GridLayout.LayoutParams();
            p.width = 0;
            p.height = 64;
            p.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            p.setMargins(3, 3, 3, 3);
            grid.addView(btn, p);
            btn.setOnClickListener(v -> press(k));
        }
        root.addView(grid, new LinearLayout.LayoutParams(-1, 0, 3));
        setContentView(root);
    }

    void press(String k) {
        String s = display.getText().toString();
        try {
            if (k.matches("[0-9]")) {
                display.setText(fresh || s.equals("0") ? k : s + k);
                fresh = false;
            } else if (k.equals(".")) {
                if (fresh) { display.setText("0."); fresh = false; }
                else if (!s.contains(".")) display.setText(s + ".");
            } else if (k.equals("AC")) {
                display.setText("0"); first = ""; op = ""; fresh = true;
            } else if (k.equals("±")) {
                display.setText(s.startsWith("-") ? s.substring(1) : "-" + s);
            } else if (k.equals("%")) {
                display.setText("" + (Double.parseDouble(s) / 100)); fresh = true;
            } else if (k.equals("⌫")) {
                display.setText(s.length() > 1 ? s.substring(0, s.length()-1) : "0");
            } else if ("+−×÷".contains(k)) {
                first = s; op = k; fresh = true;
            } else if (k.equals("=") && !op.isEmpty()) {
                double a = Double.parseDouble(first), c = Double.parseDouble(s), r;
                switch (op) {
                    case "+": r = a+c; break;
                    case "−": r = a-c; break;
                    case "×": r = a*c; break;
                    case "÷": r = a/c; break;
                    default: return;
                }
                display.setText("" + r); op = ""; fresh = true;
            }
        } catch (Exception e) { display.setText("Error"); fresh = true; }
    }
}
