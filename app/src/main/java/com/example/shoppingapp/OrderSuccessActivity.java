package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrderSuccessActivity extends AppCompatActivity {

    TextView tvOrderId, tvPayment, tvDelivery;
    Button btnContinue, btnDownload;
    ImageView ivSuccess;

    int orderId;
    String payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        tvOrderId = findViewById(R.id.tv_order_id);
        tvPayment = findViewById(R.id.tv_payment_method);
        tvDelivery = findViewById(R.id.tv_delivery_date);
        btnContinue = findViewById(R.id.btn_continue);
        btnDownload = findViewById(R.id.btn_download_invoice);
        ivSuccess = findViewById(R.id.iv_success);

        orderId = getIntent().getIntExtra("order_id", 0);
        payment = getIntent().getStringExtra("payment_method");

        tvOrderId.setText("Order ID: #" + orderId);
        tvPayment.setText("Payment: " + payment);

        // 🔥 Load GIF properly
        Glide.with(this)
                .asGif()
                .load(R.drawable.ordersuccess)
                .into(ivSuccess);

        // 🔥 Delivery Date (3 Days Later)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);

        String deliveryDate = new SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault())
                .format(calendar.getTime());

        tvDelivery.setText("Estimated Delivery: " + deliveryDate);

        // 🔥 Create Notification Channel
        createNotificationChannel();

        // 🔥 Show Notification
        showOrderNotification();

        // 🔥 Download Invoice
        btnDownload.setOnClickListener(v -> generateInvoice());

        btnContinue.setOnClickListener(v -> {
            startActivity(new Intent(
                    OrderSuccessActivity.this,
                    HomeActivity.class));
            finish();
        });
    }

    // 🔔 Notification Channel
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            "order_channel",
                            "Order Notifications",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );

            channel.setDescription("Notification for order updates");

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    // 🔔 Show Notification
    private void showOrderNotification() {

        Intent intent = new Intent(this, HomeActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT |
                                PendingIntent.FLAG_IMMUTABLE
                );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "order_channel")
                        .setSmallIcon(R.drawable.success)
                        .setContentTitle("🎉 Order Confirmed!")
                        .setContentText("Your order #" + orderId + " was placed successfully.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(1, builder.build());
    }

    // 📄 Generate Invoice
    private void generateInvoice() {

        try {

            File file = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS),
                    "Invoice_" + orderId + ".pdf"
            );

            Document document = new Document();
            PdfWriter.getInstance(document,
                    new FileOutputStream(file));

            document.open();

            document.add(new Paragraph("SMART SHOP"));
            document.add(new Paragraph("----------------------------"));
            document.add(new Paragraph("Order ID: #" + orderId));
            document.add(new Paragraph("Payment Method: " + payment));
            document.add(new Paragraph("Thank you for shopping!"));

            document.close();

            Toast.makeText(this,
                    "Invoice Saved in Downloads!",
                    Toast.LENGTH_SHORT).show();

            shareInvoice(file);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,
                    "Error creating PDF",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // 📤 Share Invoice
    private void shareInvoice(File file) {

        Uri uri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".provider",
                file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(
                intent,
                "Share Invoice via"));
    }
}
