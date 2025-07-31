package com.example.blfatsamples;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.blfatsamples.adapter.ProductAdapter;
import com.example.blfatsamples.constants.ApiQueue;
import com.example.blfatsamples.constants.Constant;
import com.example.blfatsamples.constants.ConstantUrl;
import com.example.blfatsamples.model.ProductModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private ProductFilter filter = new ProductFilter();
    private List<ProductModel> allProducts = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_activity);

        Button drinkFilterBtn = findViewById(R.id.btn_drink_filter);
        Button foodFilterBtn = findViewById(R.id.btn_food_filter);
        Button dessertFilterBtn = findViewById(R.id.btn_dessert_filter);

        foodFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "Pratos";
                if (filter.getCategory() != category)
                    filter.setCategory(category);
                else
                    filter.setCategory("");
                applyCurrentFilter();
            }
        });

        dessertFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "Doces";
                if (filter.getCategory() != category)
                    filter.setCategory(category);
                else
                    filter.setCategory("");
                applyCurrentFilter();
            }
        });

        drinkFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "Bebidas";
                if (filter.getCategory() != category)
                    filter.setCategory(category);
                else
                    filter.setCategory("");
                applyCurrentFilter();
            }
        });

        String email = Constant.getUserInfo() != null  ? Constant.getUserInfo().getEmail()  : null;

        if (email == null)
        {
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivities(new Intent[]{ intent });
            return;
        }

        setUpNavigation();

        setAllProducts();
    }

    private void setAllProducts()
    {
        RecyclerView listagemProdutoDestaque = findViewById(R.id.recycler_view_all_products);
        listagemProdutoDestaque.setLayoutManager(new LinearLayoutManager(this));
        String url = ConstantUrl.GetProduct;

        // Create a StringRequest

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject productJson = response.getJSONObject(i);

                                ProductModel product = new ProductModel(
                                        productJson.getString("name"),
                                        productJson.getString("description"),
                                        productJson.isNull("imageUrl") ? null : productJson.getString("imageUrl"),
                                        productJson.getString("category"),
                                        productJson.getDouble("price")
                                );

                                allProducts.add(product);
                            }

                            ProductAdapter adapter = new ProductAdapter(allProducts);
                            listagemProdutoDestaque.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONError", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        // Add the request to the RequestQueue using the singleton
        ApiQueue.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void applyCurrentFilter()
    {
        RecyclerView listagemProdutoDestaque = findViewById(R.id.recycler_view_all_products);
        listagemProdutoDestaque.setLayoutManager(new LinearLayoutManager(this));
        List<ProductModel> products;
        if (!filter.hasAnyFilter()) {
            products = allProducts;
        }
        else {
            products = filter.FilterItems(allProducts);
        }

        ProductAdapter adapter = new ProductAdapter(products);
        listagemProdutoDestaque.setAdapter(adapter);
    }

    private  void setUpNavigation()
    {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home){
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
            if (itemId == R.id.menu_menu){
                // do nothing here, you're already in this screen
                return true;
            }
            if (itemId == R.id.menu_cart){
                return true;
            }
            if (itemId == R.id.menu_profile){
                startActivity(new Intent(this, UserInfoActivity.class));
                return true;
            }
            return false;
        });
    }

    private class ProductFilter{
        private String category = "";
        private String name= "";

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            if (category != null)
                this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            if (name != null)
                this.name = name;
        }

        public boolean hasAnyFilter(){
            return hasNameFilter() || hasCategoryFilter();
        }

        public boolean hasCategoryFilter(){
            return !category.isBlank();
        }

        public boolean hasNameFilter(){
            return !name.isBlank();
        }

        public List<ProductModel> FilterItems(Collection<ProductModel> other){
            if (!hasAnyFilter()) return new ArrayList<>(other);

            List<ProductModel> filteredList = new ArrayList<>();

            for (ProductModel product : other) {
                boolean matches = true;

                // Filter by category if not blank
                if (!category.isBlank()) {
                    matches = product.getCategory() != null &&
                            product.getCategory().equalsIgnoreCase(category);
                }

                // Filter by name if not blank and category already matches
                if (matches && !name.isBlank()) {
                    matches = product.getName() != null &&
                            product.getName().toLowerCase().contains(name.toLowerCase());
                }

                if (matches) {
                    filteredList.add(product);
                }
            }

            return filteredList;

        }
    }
}
