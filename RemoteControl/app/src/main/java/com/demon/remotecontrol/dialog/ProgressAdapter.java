package com.demon.remotecontrol.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.bean.ProgressBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.AccountHolder> {

    private Context context;
    private List<ProgressBean> beanList;
    private int selectedInde;

    public ProgressAdapter(Context context, List<ProgressBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
        selectedInde = -1;
    }

    public void selectedName(String bean) {
        selectedInde = -1;
        if (bean != null) {
            for (int i = 0; i < beanList.size(); i++) {
                if (beanList.get(i).equals(bean)) {
                    selectedInde = i;
                    break;
                }
            }
        }
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_progress, viewGroup, false);
        return new AccountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, final int i) {
        final ProgressBean bean = beanList.get(i);

        holder.tvFileName.setText((bean.isIssue ? "(下发)" : "(上传)") + bean.fileName);
        holder.tvIssueProgress.setText(bean.issueProgress);
        holder.tvReceiverProgress.setText(bean.receiverProgress);
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class AccountHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_file_name)
        TextView tvFileName;
        @BindView(R.id.tv_issue_progress)
        TextView tvIssueProgress;
        @BindView(R.id.tv_receiver_progress)
        TextView tvReceiverProgress;

        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setSelectedInde(int selectedInde) {
        this.selectedInde = selectedInde;
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(String bean);
    }
}
