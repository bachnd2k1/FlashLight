package com.flashlight.demo.adapter.myadapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Filter;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.flashlight.demo.R;
import com.flashlight.demo.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class ChangeLanguageAdapter extends RecyclerView.Adapter<ChangeLanguageAdapter.ViewHolder> implements Filterable {
    public int selectedLanguagePosition = 0;
    private ArrayList<String> itemClone = new ArrayList<>();
    private ArrayList<String> item = new ArrayList<>();
    public String languageSelected = MyUtils.getString(R.string.auto);

    public ArrayList<String> getDataSet() {
        return item;
    }

    public String getSelectedLanguage() {
        return languageSelected;
    }

    public interface Listener {
        void onItemClick(int position);
    }

    Listener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_select_language, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindView(i);
    }

    @Override
    public int getItemCount() {
        return itemClone.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<String> filteredResults;
                if (constraint.length() == 0) {
                    filteredResults = item;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemClone = (ArrayList<String>) results.values;
                ChangeLanguageAdapter.this.notifyDataSetChanged();
            }
        };
    }

    public List<String> getFilteredResults(String key) {
        String constraint = key.trim();
        List<String> results = new ArrayList<>();
        for (String item : item) {
            if (item.toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    public void updateData(ArrayList<String> listCountries) {
        item = listCountries;
        itemClone = listCountries;
        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private int oldLanguagePosition;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cbSelectLanguage;
        TextView tvLanguage;
        ConstraintLayout llItemChangeLanguage;
        private int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            ButterKnife.bind(ViewHolder.this, itemView);
            cbSelectLanguage = itemView.findViewById(R.id.cb_selected_language);
            tvLanguage = itemView.findViewById(R.id.tv_language);
            llItemChangeLanguage = itemView.findViewById(R.id.ll_item_change_language);
        }

        public void bindView(int p) {
            this.position = p;
            final String language = itemClone.get(p);
            tvLanguage.setText(language);
            boolean checked = TextUtils.equals(language, languageSelected);
            if (checked) {
                selectedLanguagePosition = position;
                cbSelectLanguage.setVisibility(View.VISIBLE);
            } else {
                cbSelectLanguage.setVisibility(View.GONE);
            }
            cbSelectLanguage.setEnabled(checked);
            llItemChangeLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    languageSelected = language;
                    oldLanguagePosition = selectedLanguagePosition;
                    selectedLanguagePosition = position;
                    notifyItemChanged(oldLanguagePosition);
                    cbSelectLanguage.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}


