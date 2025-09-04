package com.example.theperegrinefund;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private List<HistoryItem> historyItems;
    private ImageView menuIcon;
    private ImageView newIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Initialisation du Drawer et du contenu principal
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        LinearLayout mainContent = findViewById(R.id.main_content); // LinearLayout contenant tout le contenu principal

        // Décalage du contenu principal quand le drawer s'ouvre
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mainContent.setTranslationX(slideOffset * drawerView.getWidth());
            }
        });

        // Initialisation des autres vues (menuIcon, RecyclerView, etc.)
        menuIcon = findViewById(R.id.menu_icon);
        historyRecyclerView = findViewById(R.id.history_recycler_view);

        // Clic sur le bouton menu
        menuIcon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(findViewById(R.id.history_drawer))) {
                drawerLayout.closeDrawer(findViewById(R.id.history_drawer));
            } else {
                drawerLayout.openDrawer(findViewById(R.id.history_drawer));
            }
        });

        newIcon = findViewById(R.id.new_icon);
        // Clic sur le bouton new
        newIcon.setOnClickListener(v -> {
            Intent intent = new Intent(BaseActivity.this, SendAlerteActivity.class);
            startActivity(intent);
        });

        // Reste du code : RecyclerView, chargement des données, etc.
        historyItems = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyItems, this::onHistoryItemClick);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyRecyclerView.setAdapter(historyAdapter);
        loadSampleData();
    }

    @Override
    public void setContentView( @LayoutRes int layoutResID)
    {
        FrameLayout container = findViewById(R.id.container);
        if( container != null)
        {
            LayoutInflater.from(this).inflate(layoutResID, container, true);
        }
        else
        {
            super.setContentView(layoutResID);
        }
    }
    // protected void setChildLayout( int layoutResID)
    // {
    //     FrameLayout container = findViewById( R.id.container);
    //     LayoutInflater.from(this).inflate(layoutResId, container, true)
    // }


    private void loadSampleData() {
        historyItems.clear();
        historyItems.add(new HistoryItem("Feux de brousse 26/08/25", true));
        historyItems.add(new HistoryItem("Feux de brousse 25/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 24/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 23/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 22/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));
        historyItems.add(new HistoryItem("Feux de brousse 21/08/25", false));

        historyAdapter.notifyDataSetChanged();

    }

    private void onHistoryItemClick(HistoryItem item, int position) {
        for (HistoryItem historyItem : historyItems) {
            historyItem.setSelected(false);
        }
        item.setSelected(true);
        historyAdapter.notifyDataSetChanged();
        // updateDisplayedInformation(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.action_logout) {
            logout();
            return true;
        } else if (id == R.id.action_refresh) {
            refreshData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        loadSampleData();
    }

    private void logout() {
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.history_drawer))) {
            drawerLayout.closeDrawer(findViewById(R.id.history_drawer));
        } else {
            super.onBackPressed();
        }
    }
}
