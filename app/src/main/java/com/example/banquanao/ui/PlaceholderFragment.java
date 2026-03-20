package com.example.banquanao.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.banquanao.R;

/**
 * Base fragment that shows a centered title until the real screen is implemented.
 */
public abstract class PlaceholderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_placeholder, container, false);
        TextView title = root.findViewById(R.id.placeholderTitle);
        title.setText(getTitleRes());
        return root;
    }

    protected abstract int getTitleRes();
}
