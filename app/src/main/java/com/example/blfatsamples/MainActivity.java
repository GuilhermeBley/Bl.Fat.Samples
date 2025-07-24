package com.example.blfatsamples;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blfatsamples.adapter.ProductAdapter;
import com.example.blfatsamples.constants.Constant;
import com.example.blfatsamples.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        String email = Constant.getUserInfo() != null  ? Constant.getUserInfo().getEmail()  : null;

        if (email == null)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivities(new Intent[]{ intent });
            return;
        }

        findViewById(R.id.closeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });

        setFeaturedProducts();
    }

    private void setFeaturedProducts()
    {
        RecyclerView listagemProdutoDestaque = findViewById(R.id.recycler_view_featured_products);
        listagemProdutoDestaque.setLayoutManager(new LinearLayoutManager(this));

        List<ProductModel> produtos = new ArrayList<>();
        produtos.add(new ProductModel("Produto 2", "Descrição do Produto 2", "", 12.5));
        produtos.add(new ProductModel("Produto 3", "Descrição do Produto 3", "", 32.4));
        produtos.add(new ProductModel("Produto 1", "Descrição do Produto 1", "", 23.5));

        ProductAdapter adapter = new ProductAdapter(produtos);
        listagemProdutoDestaque.setAdapter(adapter);
    }
}