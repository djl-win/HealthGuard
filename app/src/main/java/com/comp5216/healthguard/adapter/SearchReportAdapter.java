package com.comp5216.healthguard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.comp5216.healthguard.R;
import com.comp5216.healthguard.entity.HealthInformation;
import com.comp5216.healthguard.entity.MedicalReport;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 搜索页面第二个的report的适配器
 * <p>
 * 渲染报告信息列表，并处理逻辑
 * </p>
 *
 * @author Jiale Dong
 * @version 1.0
 * @since 2023-10-09
 */
public class SearchReportAdapter extends RecyclerView.Adapter<SearchReportAdapter.ReportViewHolder> {

    // 回调接口，获取单机事件的回调
    public interface ItemClickListener {
        void onItemClick(MedicalReport medicalReport);
    }

    private SearchReportAdapter.ItemClickListener itemClickListener;

    /**
     * 为此recycle view设置单机事件
     *
     * @param itemClickListener 传进来当前视图
     */
    public void setItemClickListener(SearchReportAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    Context context;
    List<MedicalReport> medicalReports;

    // 创建一个SimpleDateFormat对象，指定所需的格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");



    public SearchReportAdapter(Context context, List<MedicalReport> medicalReports) {

        this.context = context;
        this.medicalReports = medicalReports;
    }


    @NonNull
    @Override
    public SearchReportAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.report_list_item, viewGroup, false);

        return new SearchReportAdapter.ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchReportAdapter.ReportViewHolder holder, int position) {
        initItemView(holder, position);
    }

    /**
     * 初始化每个item中的数据，
     *
     * @param holder   ChatViewHolder
     * @param position 列表中item的position
     */
    @SuppressLint("SetTextI18n")
    private void initItemView(@NonNull SearchReportAdapter.ReportViewHolder holder, int position) {

        // 将时间戳转换为Date对象
        Date date = new Date(medicalReports.get(position).getMedicalReportDate());

        // 格式化日期
        String formattedDate = sdf.format(date);

        holder.textViewTime.setText(formattedDate);

        holder.textViewNote.setText("note: " + medicalReports.get(position).getMedicalReportNote());

        // 给卡片设置单机事件
        holder.materialCardView.setOnClickListener(view -> {
            // 调用接口的回调方法，将点击的位置的用户信息传递给 MainActivity
            if (itemClickListener != null) {
                itemClickListener.onItemClick(medicalReports.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicalReports.size();
    }

    /**
     * 更新recycle view中的数据
     *
     * @param newMedicalReports 传入的新的列表
     */
    public void updateData(List<MedicalReport> newMedicalReports) {
        this.medicalReports.clear();
        this.medicalReports.addAll(newMedicalReports);
        notifyDataSetChanged();  // 这里通知适配器数据已经更改
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTime;
        TextView textViewNote;

        MaterialCardView materialCardView;
        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.report_list_time);
            textViewNote = itemView.findViewById(R.id.report_list_note);
            materialCardView = itemView.findViewById(R.id.report_list_cardview);
        }

    }
}

