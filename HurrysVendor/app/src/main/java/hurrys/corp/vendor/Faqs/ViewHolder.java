package hurrys.corp.vendor.Faqs;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import hurrys.corp.vendor.R;


public class ViewHolder extends RecyclerView.ViewHolder  {

    View mView;

    private ClickListener mClickListener;

    public ViewHolder(View itemView) {

        super(itemView);
        mView = itemView;
        //Item Click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        //Item Long Click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails(Context ctx, String Answer, String PushId, String Question) {

        TextView answer,question,pushid;
        answer=mView.findViewById(R.id.answer);
        question=mView.findViewById(R.id.question);
        pushid=mView.findViewById(R.id.pushid);

        answer.setText(Answer);
        question.setText(Question);
        pushid.setText(PushId);
    }





    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);

        void onItemLongClick(View v, int position);
    }
}

