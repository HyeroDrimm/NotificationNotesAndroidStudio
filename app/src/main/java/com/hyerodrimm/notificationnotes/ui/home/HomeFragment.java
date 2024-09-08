package com.hyerodrimm.notificationnotes.ui.home;

import static androidx.core.content.ContextCompat.getSystemService;

import static com.hyerodrimm.notificationnotes.NotificationHelper.createNotification;
import static com.hyerodrimm.notificationnotes.NotificationHelper.saveNotification;
import static com.hyerodrimm.notificationnotes.NotificationHelper.sendNotification;

import android.app.NotificationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hyerodrimm.notificationnotes.R;
import com.hyerodrimm.notificationnotes.database.NoteSave;
import com.hyerodrimm.notificationnotes.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private NotificationManager notificationManager;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notificationManager = getSystemService(container.getContext(), NotificationManager.class);

        Button button = root.findViewById(R.id.send_notification_button);
        button.setOnClickListener(v -> {
            EditText messageInput = (EditText)root.findViewById(R.id.message_input);
            EditText titleInput = (EditText)root.findViewById(R.id.title_input);

            NoteSave noteSave = createNotification(messageInput.getText().toString(), titleInput.getText().toString());
            noteSave = saveNotification(noteSave);
            sendNotification(getContext(),getActivity(), notificationManager, noteSave);

            messageInput.setText("",  TextView.BufferType.EDITABLE);
            titleInput.setText("",  TextView.BufferType.EDITABLE);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}