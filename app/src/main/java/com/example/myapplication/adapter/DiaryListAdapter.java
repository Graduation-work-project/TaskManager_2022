package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Diary.Diary;
import com.example.myapplication.R;

import java.util.ArrayList;

public class DiaryListAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Diary> DiarysList;

    public DiaryListAdapter(Context context, int layout, ArrayList<Diary> DiarysList) {
        this.context = context;
        this.layout = layout;
        this.DiarysList = DiarysList;
    }

    @Override
    public int getCount() {
        return DiarysList.size();
    }

    @Override
    public Object getItem(int position) {
        return DiarysList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = (TextView) row.findViewById(R.id.txtTitle);
            holder.imageView = (ImageView) row.findViewById(R.id.imgDiary);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Diary diary = DiarysList.get(position);

        holder.txtName.setText(diary.getTitle());

        byte[] foodImage = diary.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName;
    }

}

