package com.example.ecolim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Google_Maps extends AppCompatActivity {

    private WebView webView;
    private FloatingActionButton fabCenter;

    // URL de Google Maps en versión mobile (sin API Key)
    private static final String URL_MAPA = "https://www.google.com/maps?q=EXPOMALL,+Trujillo,+Perú&hl=es";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        // Configurar Toolbar
        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        // Configurar WebView para mostrar Google Maps en mobile
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(URL_MAPA);

        // Configurar botón flotante para abrir en Google Maps App
        fabCenter = findViewById(R.id.fabCenter);
        fabCenter.setOnClickListener(v -> abrirEnGoogleMaps());
    }

    // Método para abrir Google Maps en la app nativa
    private void abrirEnGoogleMaps() {
        Uri gmmIntentUri = Uri.parse("geo:-8.1018092,-79.045216?q=EXPOMALL, Trujillo, Perú");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
