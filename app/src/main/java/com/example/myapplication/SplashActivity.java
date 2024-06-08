package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private TextView splashText;
    private Handler handler = new Handler();
    private int delay = 100; // Retardo en ms entre cada cambio de letra
    private int currentIndex = 0;
    private String text = "TeamUP";
    private int[] colors = new int[text.length()];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashText = findViewById(R.id.splash_text);

        // Inicializa los colores a gris
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Color.DKGRAY;
        }

        // Inicia la animación
        animateText();
    }

    private void animateText() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentIndex < text.length()) {
                    colors[currentIndex] = Color.WHITE;
                    updateTextColor();
                    currentIndex++;
                    handler.postDelayed(this, delay);
                } else {
                    // Inicia la actividad principal después de la animación
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, delay);
    }

    private void updateTextColor() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            sb.append("<font color='").append(String.format("#%06X", (0xFFFFFF & colors[i]))).append("'>").append(text.charAt(i)).append("</font>");
        }
        splashText.setText(android.text.Html.fromHtml(sb.toString()));
    }

    private int blendColors(int color1, int color2, float ratio) {
        final float inverseRatio = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRatio);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRatio);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRatio);
        return Color.rgb((int) r, (int) g, (int) b);
    }
}