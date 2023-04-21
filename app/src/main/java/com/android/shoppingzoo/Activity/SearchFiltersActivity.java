package com.android.shoppingzoo.Activity;

import static com.android.shoppingzoo.Fragment.HomeFragment.clearClicked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.shoppingzoo.Fragment.HomeFragment;
import com.android.shoppingzoo.Model.Utils;
import com.android.shoppingzoo.R;

public class SearchFiltersActivity extends AppCompatActivity implements View.OnClickListener {
    TextView clearFilters,sort_by_text;
    CardView search_name, search_manufacturer, search_category;
    CardView cat_mobile,cat_wearables,cat_laptops,cat_tv,cat_monitor,cat_computers,cat_appliances,cat_printers,cat_console,cat_other;
    CardView sort_ascending, sort_descending, sort_none;
    CardView sort_name, sort_price, sort_manufacturer, sort_starRating;
    HorizontalScrollView sort_by_btns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);
        Utils.statusBarColor(SearchFiltersActivity.this);

        initAll();

        clearFilters.setOnClickListener(this);

        //SearchBy
        search_name.setOnClickListener(this);
        search_manufacturer.setOnClickListener(this);
        search_category.setOnClickListener(this);
        //Sort
        sort_ascending.setOnClickListener(this);
        sort_descending.setOnClickListener(this);
        sort_none.setOnClickListener(this);
        //By
        sort_name.setOnClickListener(this);
        sort_price.setOnClickListener(this);
        sort_manufacturer.setOnClickListener(this);
        sort_starRating.setOnClickListener(this);
        //Categories
        cat_mobile.setOnClickListener(this);
        cat_wearables.setOnClickListener(this);
        cat_laptops.setOnClickListener(this);
        cat_tv.setOnClickListener(this);
        cat_monitor.setOnClickListener(this);
        cat_computers.setOnClickListener(this);
        cat_appliances.setOnClickListener(this);
        cat_printers.setOnClickListener(this);
        cat_console.setOnClickListener(this);
        cat_other.setOnClickListener(this);
    }

    private void initAll() {
        //SearchBy
        search_name = findViewById(R.id.search_name);
        search_manufacturer = findViewById(R.id.search_manufacturer);
        search_category = findViewById(R.id.search_category);
        //Sort
        sort_ascending = findViewById(R.id.sort_ascending);
        sort_descending = findViewById(R.id.sort_descending);
        sort_none = findViewById(R.id.sort_none);
        //By
        sort_name = findViewById(R.id.sort_name);
        sort_price = findViewById(R.id.sort_price);
        sort_manufacturer = findViewById(R.id.sort_manufacturer);
        sort_starRating = findViewById(R.id.sort_starRating);
        //Categories
        cat_mobile = findViewById(R.id.cat_mobile);
        cat_wearables = findViewById(R.id.cat_wearables);
        cat_laptops = findViewById(R.id.cat_laptops);
        cat_tv = findViewById(R.id.cat_tv);
        cat_monitor = findViewById(R.id.cat_monitor);
        cat_computers = findViewById(R.id.cat_computers);
        cat_appliances = findViewById(R.id.cat_appliances);
        cat_printers = findViewById(R.id.cat_printers);
        cat_console = findViewById(R.id.cat_console);
        cat_other = findViewById(R.id.cat_other);

        //
        clearFilters = findViewById(R.id.id_clear_btn);
        sort_by_text = findViewById(R.id.sort_by_text);
        sort_by_btns = findViewById(R.id.sort_by_btns);
    }

    public void goBack(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.search_name:
                HomeFragment.search = "Name";
                search_name.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                search_manufacturer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                search_category.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isSearchSelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.search_manufacturer:
                HomeFragment.search = "Manufacturer";
                search_manufacturer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                search_name.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                search_category.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isSearchSelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.search_category:
                HomeFragment.search = "Category";
                search_category.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                search_name.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                search_manufacturer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isSearchSelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.sort_ascending:
                HomeFragment.sort = "Ascending";
                sort_ascending.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                sort_descending.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_none.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_by_text.setVisibility(View.VISIBLE);
                sort_by_btns.setVisibility(View.VISIBLE);
                HomeFragment.isSortSelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.sort_descending:
                HomeFragment.sort = "Descending";
                sort_descending.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                sort_ascending.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_none.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_by_text.setVisibility(View.VISIBLE);
                sort_by_btns.setVisibility(View.VISIBLE);
                HomeFragment.isSortSelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.sort_none:
                HomeFragment.sort = "None";
                sort_none.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                sort_ascending.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_descending.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_by_text.setVisibility(View.GONE);
                sort_by_btns.setVisibility(View.GONE);
                HomeFragment.isSortSelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.sort_name:
                HomeFragment.sortBy = "Name";
                sort_name.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                sort_price.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_manufacturer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_starRating.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isSortBySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.sort_price:
                HomeFragment.sortBy = "Price";
                sort_name.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_price.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                sort_manufacturer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_starRating.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isSortBySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.sort_manufacturer:
                HomeFragment.sortBy = "Manufacturer";
                sort_name.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_price.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_manufacturer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                sort_starRating.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isSortBySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.sort_starRating:
                HomeFragment.sortBy = "Star Rating";
                sort_name.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_price.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_manufacturer.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                sort_starRating.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                HomeFragment.isSortBySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.cat_mobile:
                HomeFragment.category = "Mobile Devices";
                cat_mobile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                cat_wearables.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_laptops.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_tv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_monitor.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_computers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_appliances.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_printers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_console.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_other.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isCategorySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.cat_wearables:
                HomeFragment.category = "Wearables";
                cat_wearables.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                cat_mobile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_laptops.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_tv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_monitor.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_computers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_appliances.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_printers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_console.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_other.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isCategorySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.cat_laptops:
                HomeFragment.category = "Laptops";
                cat_laptops.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                cat_mobile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_wearables.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_tv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_monitor.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_computers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_appliances.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_printers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_console.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_other.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isCategorySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.cat_tv:
                HomeFragment.category = "Tv sets";
                cat_tv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                cat_mobile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_wearables.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_laptops.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_monitor.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_computers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_appliances.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_printers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_console.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_other.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isCategorySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.cat_monitor:
                HomeFragment.category = "Monitors";
                cat_monitor.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                cat_mobile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_wearables.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_laptops.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_tv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_computers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_appliances.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_printers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_console.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_other.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isCategorySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.cat_computers:
                HomeFragment.category = "Computers";
                cat_computers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                cat_mobile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_wearables.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_laptops.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_monitor.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_tv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_appliances.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_printers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_console.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_other.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isCategorySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.cat_appliances:
                HomeFragment.category = "Appliances";
                cat_appliances.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                cat_mobile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_wearables.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_laptops.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_monitor.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_computers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_tv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_printers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_console.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_other.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isCategorySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.cat_printers:
                HomeFragment.category = "Printers";
                cat_printers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                cat_mobile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_wearables.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_laptops.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_monitor.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_computers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_appliances.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_tv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_console.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_other.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isCategorySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.cat_console:
                HomeFragment.category = "Game Consoles";
                cat_console.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                cat_mobile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_wearables.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_laptops.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_monitor.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_computers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_appliances.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_printers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_tv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_other.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isCategorySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.cat_other:
                HomeFragment.category = "Other";
                cat_other.setCardBackgroundColor(ContextCompat.getColor(this, R.color.selectedFilter));
                cat_console.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_mobile.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_wearables.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_laptops.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_monitor.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_computers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_appliances.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_printers.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                cat_tv.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white));
                HomeFragment.isCategorySelected = true;
                HomeFragment.isFiltersApplied = true;
                break;
            case R.id.id_clear_btn:
                clearClicked();
                finish();
                break;
        }
    }
}