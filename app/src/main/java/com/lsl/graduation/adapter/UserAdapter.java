package com.lsl.graduation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsl.graduation.R;
import com.lsl.graduation.bean.ChannelItem;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Forrest on 16/4/18.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    private Context context;
    private List<ChannelItem> data;

    public UserAdapter(Context context, List<ChannelItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View layout= LayoutInflater.from(context).inflate(R.layout.channel_item,viewGroup,false);
        return new MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int i) {

        holder.textView.setText(data.get(i).getName());
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
//            {
//                @Override
//                public boolean onLongClick(View v)
//                {
//                    int pos = holder.getPosition();
//                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
//                    return false;
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        MyViewHolder(View view){
            super(view);
            textView= (TextView) view.findViewById(R.id.text_item);

        }
    }
    public void addData(int position,ChannelItem item)
    {
        data.add(position, item);
        notifyItemInserted(position);
    }


    public void removeData(int position)
    {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void refresh(List<ChannelItem> items){
        data.clear();
        data.addAll(items);
        notifyDataSetChanged();
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
