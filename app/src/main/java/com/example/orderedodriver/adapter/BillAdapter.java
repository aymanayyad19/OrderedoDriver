package com.example.orderedodriver.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.orderedodriver.R;
import com.example.orderedodriver.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {
    private Context context;
    //UserAcount
    List<Product> products;

    public BillAdapter(final List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_card, parent, false);

        return new MyViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Product product = products.get(position);
//        holder.count.setText(product.getCount());
        holder.productName.setText(product.getName());
        holder.price.setText("" + product.getTotalPrice());
//        holder.image.s""+product.getTotalPrice());
        if (!product.getImage().equals("")) {
            Picasso
                    .get()
                    .load(product.getImage())
                    .fit()
                    .centerCrop()
                    .into(holder.image);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView count;
        public TextView price;
        public TextView productName;

        public MyViewHolder(View view, int type) {
            super(view);
            image = (ImageView) view.findViewById(R.id.iv_order_item_image);
            count = (TextView) view.findViewById(R.id.tv_order_item_product_count);
            productName = (TextView) view.findViewById(R.id.tv_order_item_product_name);
            price = (TextView) view.findViewById(R.id.tv_order_item_product_price);
        }
    }
    public void addAllNew(List<Product> products){
        this.products.clear();
        this.products = products;
        notifyDataSetChanged();

    }

}