package com.hyerodrimm.notificationnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyerodrimm.notificationnotes.database.NoteSave;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<NoteSave>
//        implements View.OnClickListener
{

    private ArrayList<NoteSave> dataSet;
    Context mContext;

    public CustomAdapter(Context context, ArrayList<NoteSave> data) {
        super(context, R.layout.list_history_element_layout, data);
        this.dataSet = data;
        this.mContext = context;
    }

//    @Override
//    public void onClick(View v) {
//
//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        NoteSave dataModel=(NoteSave)object;
//
//        switch (v.getId())
//        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
//    }
//
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_history_element_layout, parent, false);
        }

        // Get the data item for this position
        NoteSave dataModel = getItem(position);

        TextView txtTitle = (TextView) currentItemView.findViewById(R.id.history_list_title);
        TextView txtMessage = (TextView) currentItemView.findViewById(R.id.history_list_message);
        TextView txtTimeSend = (TextView) currentItemView.findViewById(R.id.history_list_time_sent);

        if (dataModel != null){
            txtTitle.setText("Title: " + dataModel.title);
            txtMessage.setText("Message: " + dataModel.message);
            txtTimeSend.setText("Time send: " + "Placeholder");
        }

        return currentItemView;
    }
}