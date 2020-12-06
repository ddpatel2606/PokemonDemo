package com.dixitpatel.pokemondemo.utils;

import android.text.TextUtils;
import android.widget.Filter;
import android.widget.Filterable;

import com.dixitpatel.pokemondemo.model.Pokemon;

import java.util.ArrayList;

/**
 *  Search adapter class for filterable interface.
 */
public abstract class SearchAdapter<T extends Pokemon> extends CommonAdapter<T> implements Filterable {

    private Filter filter;
    private ArrayList<T> mDataList;
    private ArrayList<T> mBackDataList;

    public SearchAdapter(ArrayList<T> data) {
        super(data);
        mDataList = data;
        mBackDataList = data;
        filter = new SearchFilter();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class SearchFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = new FilterResults();
            ArrayList<T> filterList;
            if (TextUtils.isEmpty(constraint)) {
                filterList = mBackDataList;
            } else {
                filterList = new ArrayList<>();
                for (T item : mBackDataList) {
                    try {
                        if (item.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filterList.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            result.values = filterList;
            result.count = filterList.size();
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                mDataList.clear();
                mDataList.addAll((ArrayList<T>)results.values);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
