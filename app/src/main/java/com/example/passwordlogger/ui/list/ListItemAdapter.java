package com.example.passwordlogger.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.passwordlogger.R;

import java.util.ArrayList;
import java.util.HashMap;


public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {
    private final ArrayList<HashMap<String, String>> data;
    private final LayoutInflater inflater;
    private ItemClickListener clickListener;

    ListItemAdapter(
            Context context,
            ArrayList<HashMap<String, String>> data
    ) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    static class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        EditText etPassword;
        TextView tvDate;

        ListItemViewHolder(View itemView) {
            super(itemView);

            etPassword = itemView.findViewById(R.id.etPasswordHolder);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_list_item, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        HashMap<String, String> dataFromDB = data.get(position);

        holder.etPassword.setText(dataFromDB.get("password"));
        holder.tvDate.setText(dataFromDB.get("currentDate"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
