package com.example.liftdo.Recycler;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.liftdo.R;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder>{

    Context context;
    String[] ans;
    @NonNull
    @Override
    public FAQAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_faq, parent, false);
        return new ViewHolder(view);
    }
    public FAQAdapter(Context context, String[] ans)
    {
        this.ans = ans;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.tv_faq.setText(ans[position]);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_faq;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_faq = itemView.findViewById(R.id.tv_faq);

        }
    }
}

