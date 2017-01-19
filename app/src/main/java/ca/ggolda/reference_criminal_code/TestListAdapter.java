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

        View convertView = inflater.inflate(R.layout.test_card_section, parent, false);
        ListViewHolder viewHolder = new ListViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        holder.tv_fulltext.setText(dataList.get(position).fulltext);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_fulltext;

        public ListViewHolder(View itemView) {
            super(itemView);

            tv_fulltext = (TextView) itemView.findViewById(R.id.tv_fulltext);

        }
    }


}