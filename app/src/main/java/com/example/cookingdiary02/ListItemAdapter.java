package com.example.cookingdiary02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

//ListView 아이템 표시를 위한 Adapter, 필터링 기능 지원
public class ListItemAdapter extends BaseAdapter implements Filterable {

    //Adapter에 추가된 데이터 저장을 위한 ArrayList
    private ArrayList<ListItem> listItems = new ArrayList<ListItem>();
    //필터링된 결과 데이터 저장을 위한 ArrayList
    private ArrayList<ListItem> filteredItems = listItems;

    Filter filter;

    public ListItemAdapter() {

    }

    @Override //Adapter에 사용되는 데이터의 개수 리턴
    public int getCount() {
        return filteredItems.size();
    }

    @Override //position에 있는 데이터 리턴
    public Object getItem(int position) {
        return filteredItems.get(position);
    }

    @Override //position에 있는 데이터와 관계된 아이템의 id를 리턴
    public long getItemId(int position) {
        return position;
    }

    @Override //position에 위차한 데이터를 화면에 출력하는데 사용될 View를 리턴
    public View getView(int position, View view, ViewGroup vg) {
        final Context context = vg.getContext();

        if(view == null) {
            LayoutInflater inflater
                    = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, vg, false);
        }

        TextView noText = (TextView)view.findViewById(R.id.no);
        TextView foodText = (TextView)view.findViewById(R.id.showFood);
        TextView writerText = (TextView)view.findViewById(R.id.showWriter);

        //position에 위치한 데이터 참조 획득
        ListItem listItem = filteredItems.get(position);

        //아이템 내에 각 데이터 반영
        noText.setText(String.valueOf(listItem.getTxt1()));
        foodText.setText(listItem.getTxt2());
        writerText.setText(listItem.getTxt3());

        return view;
    }

    //아이템 추가를 위한 함수
    public void addItem(int i, String str1, String str2) {
        ListItem item = new ListItem();

        item.setTxt1(i);
        item.setTxt2(str1);
        item.setTxt3(str2);

        listItems.add(item);
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ListFilter();
        }
        return filter;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence cs) {
            FilterResults results = new FilterResults();

            if(cs == null || cs.length() == 0) {
                results.values = listItems;
                results.count = listItems.size();
            } else {
                ArrayList<ListItem> itemList = new ArrayList<ListItem>();
                for(ListItem item : listItems) {
                    if (item.getTxt2().toUpperCase().contains(cs.toString().toUpperCase()) ||
                            item.getTxt3().toUpperCase().contains(cs.toString().toUpperCase())) {
                        itemList.add(item);
                    }
                }
                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence cs, FilterResults results) {

            //필터링 된 리스트를 갱신한다.
            filteredItems = (ArrayList<ListItem>) results.values;

            if(results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}