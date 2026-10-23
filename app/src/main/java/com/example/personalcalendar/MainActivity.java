package com.example.personalcalendar; // Kendi paket adını değiştirmeyi unutma!

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int count = 0;
    private int maxCap = 2;
    private SharedPreferences hafiza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvCounter = findViewById(R.id.tvCounter);
        Button btnTap = findViewById(R.id.btnTap);
        TextView tvPlan = findViewById(R.id.tvPlan);
        Button btnNewTask = findViewById(R.id.btnNewTask);

        hafiza = getSharedPreferences("TakvimVerileri", Context.MODE_PRIVATE);
        //hafiza.edit().clear().apply();
        count = hafiza.getInt("kayitliSayac", 0);
        maxCap = hafiza.getInt("kayitliSinir", 2);
        long sonGorevSaati = hafiza.getLong("kayitliZaman", 0);

        // YENİ EKLEME: Kaydedilmiş görev yazılarını defterden okuyoruz.
        // Eğer defter boşsa, sağdaki orijinal "Ders ve Spor" yazısını varsayılan olarak kabul ediyoruz.
        String varsayilanPlan = "10:40 - 14:00 Ders\n16:00 - 18:00 Spor";
        String kayitliPlan = hafiza.getString("kayitliYazilar", varsayilanPlan);
        tvPlan.setText(kayitliPlan); // Okuduğumuz yazıyı ekrana basıyoruz.

        long suAnkiSaat = System.currentTimeMillis();

        if (sonGorevSaati != 0 && suAnkiSaat > (sonGorevSaati + 300000)) {
            count = 0;
            hafiza.edit().putInt("kayitliSayac", count).apply();
        }

        tvCounter.setText("Tamamlanan Görev: " + count + " / " + maxCap);

        btnTap.setOnClickListener(v -> {
            if (count < maxCap) {
                count++;
                tvCounter.setText("Tamamlanan Görev: " + count + " / " + maxCap);
                hafiza.edit().putInt("kayitliSayac", count).apply();
            }
        });

        btnNewTask.setOnClickListener(v -> {
            Dialog popUp = new Dialog(MainActivity.this);
            popUp.setContentView(R.layout.dialog_new_task);

            Window window = popUp.getWindow();
            if (window != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                    window.getAttributes().setBlurBehindRadius(30);
                } else {
                    window.setDimAmount(0.7f);
                }

                DisplayMetrics metrikler = getResources().getDisplayMetrics();
                int genislik = (int) (metrikler.widthPixels * 0.80);
                int yukseklik = (genislik / 3) * 4;
                window.setLayout(genislik, yukseklik);
            }

            EditText etIsim = popUp.findViewById(R.id.etTaskName);
            EditText etTarih = popUp.findViewById(R.id.etTaskDate);
            EditText etSaat = popUp.findViewById(R.id.etTaskTime);
            Button btnKaydet = popUp.findViewById(R.id.btnSaveTask);

            btnKaydet.setOnClickListener(gorevKaydetV -> {
                String yazilanIsim = etIsim.getText().toString();
                String yazilanTarih = etTarih.getText().toString();
                String yazilanSaat = etSaat.getText().toString();

                if (!yazilanIsim.isEmpty() && !yazilanSaat.isEmpty()) {

                    String eklenecekYazi = "\n" + yazilanSaat + " " + yazilanIsim + " (" + yazilanTarih + ")";
                    tvPlan.append(eklenecekYazi);

                    // YENİ EKLEME: Ekrandaki güncel yazının TAMAMINI alıp deftere kaydediyoruz.
                    // Böylece uygulama kapansa bile ekrandaki yazılar geri gelecek.
                    hafiza.edit().putString("kayitliYazilar", tvPlan.getText().toString()).apply();

                    maxCap++;
                    hafiza.edit().putInt("kayitliSinir", maxCap).apply();
                    tvCounter.setText("Tamamlanan Görev: " + count + " / " + maxCap);

                    long demoBitisSaati = System.currentTimeMillis() + 60000;
                    hafiza.edit().putLong("kayitliZaman", demoBitisSaati).apply();

                    popUp.dismiss();
                }
            });

            popUp.show();
        });
    }
}