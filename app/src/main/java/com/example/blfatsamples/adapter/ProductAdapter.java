package com.example.blfatsamples.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blfatsamples.R;
import com.example.blfatsamples.model.ProductModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ProductModel> products;

    public ProductAdapter(List<ProductModel> products) {
        this.products = products;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView name;
        public TextView description;
        public TextView price;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.foodImage);
            name = itemView.findViewById(R.id.foodTitle);
            description = itemView.findViewById(R.id.foodDescription);
            price = itemView.findViewById(R.id.foodPrice);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        ProductModel produto = products.get(position);

        holder.productImage.setImageURI(Uri.parse(produto.getImgUrl()));
        holder.name.setText(produto.getName());
        holder.description.setText(produto.getDescription());

        NumberFormat precoBr = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        holder.price.setText(precoBr.format(produto.getPrice()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
