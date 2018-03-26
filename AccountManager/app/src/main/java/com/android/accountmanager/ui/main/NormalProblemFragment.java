package com.android.accountmanager.ui.main;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.model.AppInfoTemplate;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.FeedBackUtils;

import java.util.List;

public class NormalProblemFragment extends Fragment {
    private static NormalProblemFragment mFragment;
    private GridView mGridView;
    private List<AppInfoTemplate> mList;
    private AppsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_normalproblem, container, false);
        mGridView = (GridView) parentView.findViewById(R.id.griditem);
        mList = FeedBackUtils.loadModules(getActivity());
        mAdapter = new AppsAdapter();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(mClick);
        return parentView;
    }

    public static NormalProblemFragment newInstance() {
        if (null == mFragment) {
            mFragment = new NormalProblemFragment();
        }
        return mFragment;
    }

    class AppsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return mList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (null == view) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.graidview_item, null);
            }
            ImageView imageView = FeedBackUtils.CommonViewHolder.get(view, R.id.item_imageview);
            TextView textView = FeedBackUtils.CommonViewHolder.get(view, R.id.item_textview);

            AppInfoTemplate info = mList.get(i);
            imageView.setImageDrawable(info.getAppIcon());
            textView.setText(info.getAppName());
            return view;
        }
    }

    AdapterView.OnItemClickListener mClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (!AppUtils.isLogined(getActivity())) {
                ((MainActivity) getActivity()).showAction(R.string.feedback_need_login);
                return;
            }
            Intent intent = new Intent(getActivity(), IssueCommit.class);
            String title = (String) mList.get(position).getAppName();
            String pkname = mList.get(position).getPackageName();
            intent.putExtra("title", title);
            intent.putExtra("package", pkname);
            startActivity(intent);
        }
    };

}
