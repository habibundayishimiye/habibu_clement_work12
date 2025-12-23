package com.example.habibu_clement_work12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    private List<MenuOption> menuOptions;
    private OnMenuClickListener listener;

    public interface OnMenuClickListener {
        void onMenuClick(int position);
    }

    public MenuAdapter(List<MenuOption> menuOptions) {
        this.menuOptions = menuOptions;
    }

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuOption option = menuOptions.get(position);
        holder.txtMenuIcon.setText(option.getIcon());
        holder.txtMenuName.setText(option.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMenuClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuOptions != null ? menuOptions.size() : 0;
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView txtMenuIcon;
        TextView txtMenuName;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMenuIcon = itemView.findViewById(R.id.txtMenuIcon);
            txtMenuName = itemView.findViewById(R.id.txtMenuName);
        }
    }
}

