package com.example.ecolim;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Google_Maps extends AppCompatActivity {

    private WebView webView;
    private FloatingActionButton fabCenter;

    // URL de Google Maps embebido con la ubicación de EXPOMALL
    private static final String URL_MAPA = "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d1560.317949315765!2d-79.045216!3d-8.1018092!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x91ad3dbc4a92a239%3A0x5569df5ba149089d!2sEXPOMALL!5e0!3m2!1ses-419!2spe!4v1648753294845!5m2!1ses-419!2spe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        // Configurar Toolbar
        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        // Configurar WebView
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(URL_MAPA);

        // Configurar botón flotante
        fabCenter = findViewById(R.id.fabCenter);
        fabCenter.setOnClickListener(v -> webView.loadUrl(URL_MAPA));
    }
}
