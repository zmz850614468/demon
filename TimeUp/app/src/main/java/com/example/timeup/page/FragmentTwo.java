package com.example.timeup.page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timeup.R;
import com.example.timeup.adapters.ExecuteAdapter;
import com.example.timeup.beans.TypeBean;
import com.example.timeup.controls.MediaControl;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTwo extends Fragment {

    @BindView(R.id.rv_type)
    RecyclerView rvType;
    private ExecuteAdapter executeAdapter;
    private List<TypeBean> typeBeanList;
    private static Timer timer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View chatView = inflater.inflate(R.layout.fragment_2, container, false);
        ButterKnife.bind(this, chatView);
        initAdapter();
        startTimer();
        return chatView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            private int count = 0;

            @Override
            public void run() {
                for (TypeBean bean : typeBeanList) {
                    bean.during--;
                    if (bean.during == 0) {
                        MediaControl.getInstance(getActivity()).playRandon();
                    }
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateUI();
                        }
                    });
                }
            }
        }, 1000, 1000);

    }


    public void updateUI() {
        executeAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        typeBeanList = SlipPageActivity.typeBeanList;

        executeAdapter = new ExecuteAdapter(getActivity(), typeBeanList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvType.setLayoutManager(manager);
        rvType.setAdapter(executeAdapter);

        executeAdapter.setListener(new ExecuteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TypeBean bean) {
                if (bean.during <= 0) {
                    SlipPageActivity.removerTimer(bean);
                    executeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onStartClick(TypeBean bean) {
            }

            @Override
            public void onDeleteClick(final TypeBean bean) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("注意")
                        .setMessage("确定删除计时任务：" + bean.name)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SlipPageActivity.removerTimer(bean);
                                executeAdapter.notifyDataSetChanged();
                            }
                        });
                builder.create().show();
            }
        });
    }
}
