package com.example.blfatsamples.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blfatsamples.R;
import com.example.blfatsamples.model.OrderModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderModel> orders;

    public OrderAdapter(List<OrderModel> Orders) {
        this.orders = Orders;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView status;
        public TextView observation;

        public OrderViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.orderTitle);
            status = itemView.findViewById(R.id.orderStatus);
            observation = itemView.findViewById(R.id.orderObservation);
        }
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_card, parent, false);
        return new OrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");

        OrderModel order = orders.get(position);

        String status = order.getDeliveredAt() != null
                ? ("Pedido foi entregue em " + hourFormatter.format(order.getStartedDeliveringAt()) + ".")
                : order.getStartedDeliveringAt() != null
                ? ("Pedido saiu para entrega as " + hourFormatter.format(order.getStartedDeliveringAt()) + ".")
                : "Pedido criado com sucesso! Aguarde...";
        String title = "Feito em " + dayFormatter.format(order.getCreatedAt());

        holder.title.setText(title);
        holder.status.setText(status);

        NumberFormat precoBr = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String obsevation = "Valor de " + precoBr + ".";
        holder.observation.setText(obsevation);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
