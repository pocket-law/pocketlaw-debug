package ca.ggolda.reference_criminal_code;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gcgol on 01/18/2017.
 */

public class TestListAdapter extends RecyclerView.Adapter<TestListAdapter.ListViewHolder> {

    Context context;
    List<TestUserData> dataList = new ArrayList<>();
    LayoutInflater inflater;
    TestListener listener;

    public TestListAdapter(Context context, List<TestUserData> dataList1) {

        this.context = context;
        this.dataList = dataList1;
        this.listener= (TestListener) context;
        inflater = LayoutInflater.from(context);


    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = inflater.inflate(R.layout.test_card_user, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {


        holder.iv_delete.setTag(position);
        holder.tv_name.setText(dataList.get(position).name);

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.nameToChange(dataList.get((Integer) v.getTag()).name);


            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView iv_delete;

        public ListViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_delete= (TextView) itemView.findViewById(R.id.iv_delete);

        }
    }


}