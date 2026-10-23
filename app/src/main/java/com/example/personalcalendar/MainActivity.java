package com.example.personalcalendar; // Paket adını kontrol et!

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private int count = 0;
    private int maxCap = 0;
    private SharedPreferences hafiza;
    private LinearLayout llIncompleteTasks;
    private LinearLayout llCompletedTasks;
    private TextView tvCounter;
    private ArrayList<String> gorevListesi;

    private final String[] aylar = {"Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran", "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCounter = findViewById(R.id.tvCounter);
        Button btnNewTask = findViewById(R.id.btnNewTask);
        llIncompleteTasks = findViewById(R.id.llIncompleteTasks);
        llCompletedTasks = findViewById(R.id.llCompletedTasks);

        hafiza = getSharedPreferences("TakvimVerileri", Context.MODE_PRIVATE);
        gorevListesi = new ArrayList<>();

        String varsayilanGorevler = "Ders:::Amfide mobil yazılım dersi:::23 Ekim 2026:::23 Ekim 2026:::10:40:::14:00:::0";
        String kayitliGorevler = hafiza.getString("gorevListesiPaket", varsayilanGorevler);

        String[] parcalanmisGorevler = kayitliGorevler.split("###");
        for (String gorev : parcalanmisGorevler) {
            if (!gorev.isEmpty()) {
                gorevListesi.add(gorev);
            }
        }

        ekranlariYenile();

        // "-1" parametresi bunun sıfırdan "Yeni Görev" olduğunu belirtir.
        btnNewTask.setOnClickListener(v -> gorevDialoguAc(-1));
    }

    // HEM EKLEME HEM DÜZENLEME İŞLEMİNİ YAPAN ORTAK METOT
    private void gorevDialoguAc(final int duzenlenecekIndex) {
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
            int genislik = (int) (metrikler.widthPixels * 0.85);
            window.setLayout(genislik, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        TextView tvTitle = popUp.findViewById(R.id.tvDialogTitle);
        EditText etIsim = popUp.findViewById(R.id.etTaskName);
        EditText etDesc = popUp.findViewById(R.id.etTaskDesc);
        EditText etStartDate = popUp.findViewById(R.id.etStartDate);
        EditText etEndDate = popUp.findViewById(R.id.etEndDate);
        EditText etStartTime = popUp.findViewById(R.id.etStartTime);
        EditText etEndTime = popUp.findViewById(R.id.etEndTime);
        Button btnKaydet = popUp.findViewById(R.id.btnSaveTask);

        etStartDate.setOnClickListener(v1 -> tarihSeciciAc(etStartDate));
        etEndDate.setOnClickListener(v1 -> tarihSeciciAc(etEndDate));
        etStartTime.setOnClickListener(v1 -> saatSeciciAc(etStartTime));
        etEndTime.setOnClickListener(v1 -> saatSeciciAc(etEndTime));

        String mevcutDurum = "0";

        // Eğer düzenleme modundaysak (-1 değilse), bilgileri kutulara doldur
        if (duzenlenecekIndex != -1) {
            if (tvTitle != null) tvTitle.setText("Görevi Düzenle");
            btnKaydet.setText("Güncelle");

            String[] parts = gorevListesi.get(duzenlenecekIndex).split(":::", -1);
            if (parts.length >= 7) {
                etIsim.setText(parts[0]);
                if (!parts[1].equals(" ")) etDesc.setText(parts[1]);
                etStartDate.setText(parts[2]);
                etEndDate.setText(parts[3]);
                etStartTime.setText(parts[4]);
                etEndTime.setText(parts[5]);
                mevcutDurum = parts[6]; // Görev tamamlanmışsa durumu koru
            }
        } else {
            if (tvTitle != null) tvTitle.setText("Yeni Görev Oluştur");
            btnKaydet.setText("Görevi Ekle");
        }

        final String sonDurum = mevcutDurum;

        btnKaydet.setOnClickListener(v -> {
            String isim = etIsim.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();
            String startDate = etStartDate.getText().toString().trim();
            String endDate = etEndDate.getText().toString().trim();
            String startTime = etStartTime.getText().toString().trim();
            String endTime = etEndTime.getText().toString().trim();

            if (desc.isEmpty()) desc = " ";
            if (!isim.isEmpty() && !startDate.isEmpty() && !startTime.isEmpty()) {
                if (endDate.isEmpty()) endDate = startDate;
                if (endTime.isEmpty()) endTime = startTime;

                String yeniPaket = isim + ":::" + desc + ":::" + startDate + ":::" + endDate + ":::" + startTime + ":::" + endTime + ":::" + sonDurum;

                // İndex -1 ise listeye yeni ekle, değilse mevcut indexi güncelle
                if (duzenlenecekIndex != -1) {
                    gorevListesi.set(duzenlenecekIndex, yeniPaket);
                } else {
                    gorevListesi.add(yeniPaket);
                }

                hafizayaKaydet();
                ekranlariYenile();
                popUp.dismiss();
            }
        });
        popUp.show();
    }

    private void tarihSeciciAc(EditText hedefInput) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(MainActivity.this, (view, year, month, dayOfMonth) -> {
            hedefInput.setText(dayOfMonth + " " + aylar[month] + " " + year);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dateDialog.show();
    }

    private void saatSeciciAc(EditText hedefInput) {
        Calendar c = Calendar.getInstance();
        TimePickerDialog timeDialog = new TimePickerDialog(MainActivity.this, (view, hourOfDay, minute) -> {
            String dk = (minute < 10) ? "0" + minute : String.valueOf(minute);
            String st = (hourOfDay < 10) ? "0" + hourOfDay : String.valueOf(hourOfDay);
            hedefInput.setText(st + ":" + dk);
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
        timeDialog.show();
    }

    private void ekranlariYenile() {
        llIncompleteTasks.removeAllViews();
        llCompletedTasks.removeAllViews();
        int tamamlananSayisi = 0;

        for (int i = 0; i < gorevListesi.size(); i++) {
            final int index = i;
            String[] parts = gorevListesi.get(index).split(":::", -1);

            if (parts.length >= 7) {
                View gorevKarti = LayoutInflater.from(this).inflate(R.layout.item_task, null);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 24);
                gorevKarti.setLayoutParams(params);

                TextView tvYazi = gorevKarti.findViewById(R.id.tvTaskText);
                ImageButton btnEdit = gorevKarti.findViewById(R.id.btnEditTask);
                Button btnComplete = gorevKarti.findViewById(R.id.btnCompleteTask);

                tvYazi.setText(parts[0] + " (" + parts[4] + " - " + parts[5] + ")\n" + parts[2] + "\n" + parts[1]);

                // Düzenle Butonuna Tıklanınca İlgili İndexi Gönder
                btnEdit.setOnClickListener(v -> gorevDialoguAc(index));

                if (parts[6].equals("1")) {
                    tamamlananSayisi++;
                    btnComplete.setVisibility(View.GONE);
                    llCompletedTasks.addView(gorevKarti);
                } else {
                    btnComplete.setVisibility(View.VISIBLE);
                    btnComplete.setOnClickListener(v -> {
                        String guncelGorev = parts[0] + ":::" + parts[1] + ":::" + parts[2] + ":::" + parts[3] + ":::" + parts[4] + ":::" + parts[5] + ":::1";
                        gorevListesi.set(index, guncelGorev);
                        hafizayaKaydet();
                        ekranlariYenile();
                    });
                    llIncompleteTasks.addView(gorevKarti);
                }
            }
        }
        maxCap = gorevListesi.size();
        count = tamamlananSayisi;
        tvCounter.setText("Tamamlanan Görev: " + count + " / " + maxCap);
        hafiza.edit().putInt("kayitliSayac", count).apply();
    }

    private void hafizayaKaydet() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gorevListesi.size(); i++) {
            sb.append(gorevListesi.get(i));
            if (i < gorevListesi.size() - 1) sb.append("###");
        }
        hafiza.edit().putString("gorevListesiPaket", sb.toString()).apply();
    }
}