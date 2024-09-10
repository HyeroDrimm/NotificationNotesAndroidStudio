package com.hyerodrimm.notificationnotes.ui.dashboard;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hyerodrimm.notificationnotes.CustomAdapter;
import com.hyerodrimm.notificationnotes.MyApp;
import com.hyerodrimm.notificationnotes.NotificationHelper;
import com.hyerodrimm.notificationnotes.R;
import com.hyerodrimm.notificationnotes.database.NoteSave;
import com.hyerodrimm.notificationnotes.database.NoteSaveDao;
import com.hyerodrimm.notificationnotes.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.Collections;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private NotificationManager notificationManager;
    private CustomAdapter adapter;
    ArrayList<NoteSave> list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView history_list = (ListView) root.findViewById(R.id.history_list);
        list = getNotesHistory();
        adapter = new CustomAdapter(getContext(), list);
        history_list.setAdapter(adapter);
        registerForContextMenu(history_list);

        notificationManager = getSystemService(container.getContext(), NotificationManager.class);

        return root;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.history_listview_item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NoteSave noteSave = (NoteSave) adapter.getItem(info.position);
        NoteSaveDao noteSaveDao = MyApp.historyDatabase.noteSaveDao();

//        if (item.getItemId() == R.id.add_to_favorites) {
//            return true;
//        } else
        if (item.getItemId() == R.id.send_again) {
            NotificationHelper.sendNotification(getContext(),getActivity(), notificationManager, noteSave);
            return true;
//        } else if (item.getItemId() == R.id.edit) {
//            return true;
        } else if (item.getItemId() == R.id.delete) {
            noteSaveDao.delete(noteSave);
            list.remove(info.position);
            adapter.notifyDataSetChanged();
            return true;
        }else{
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private ArrayList<NoteSave> getNotesHistory() {
        NoteSaveDao noteSaveDao = MyApp.historyDatabase.noteSaveDao();
        ArrayList<NoteSave> output = (ArrayList<NoteSave>) noteSaveDao.getAll();
        Collections.reverse(output);
        return output;
    }

}