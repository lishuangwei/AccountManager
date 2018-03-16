package com.android.accountmanager.ui.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.accountmanager.R;


/**
 * Created by fantao on 18-1-18.
 */

public class DetailsFragment extends BaseInfoFragment {
    private ImageView mImgBack;
    private TextView mTextTile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_details, null);
        mImgBack = (ImageView) parentView.findViewById(R.id.image_back);
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return parentView;
    }

    public void setTile(String str) {
        mTextTile.setText(str);
    }
}
