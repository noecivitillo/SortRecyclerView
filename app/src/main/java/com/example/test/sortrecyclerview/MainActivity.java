package com.example.test.sortrecyclerview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private DbHelper mDbHelper;
    private List<UserData> listUsers;
    private ListAdapter adapterUsers;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycler= (RecyclerView) findViewById(R.id.recycler);
        mDbHelper= new DbHelper(getApplicationContext());

        UserData user= new UserData();
        user.setId("1");
        user.setName("Hello");
        user.setDescription("This is hello");
        user.setQuantity("1");
        mDbHelper.insertUserDetail(user);

        UserData user2= new UserData();
        user2.setName("Good bye");
        user2.setId("2");
        user2.setDescription("This is good bye");
        user2.setQuantity("2");
        mDbHelper.insertUserDetail(user2);

        UserData user3= new UserData();
        user3.setName("Good morning");
        user3.setId("3");
        user3.setDescription("This is good morning");
        user3.setQuantity("3");
        mDbHelper.insertUserDetail(user3);


        UserData user4= new UserData();
        user4.setName("Good evening");
        user4.setId("4");
        user4.setDescription("This is good evening");
        user4.setQuantity("4");
        mDbHelper.insertUserDetail(user4);


        listUsers= mDbHelper.getAllUser();
        recycler.setHasFixedSize(true);
        adapterUsers= new ListAdapter(getApplicationContext(), listUsers);
        recycler.setAdapter(adapterUsers);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(), new LinearLayoutManager(getApplicationContext()).getOrientation());
        recycler.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapterUsers);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recycler);

    }
    public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
        private static final String TAG = "ListAdapter";
        DbHelper dbHelper;
        Context context;
        List<UserData> dataList = new ArrayList<>();
        LayoutInflater inflater;
        //Listener listener;

        /**
         *
         * This lines are hidden for test
         public interface Listener {
         void nameToChnge(String name);
         }
         */
        public ListAdapter(Context context, List<UserData> dataList1) {
            this.context = context;
            this.dataList = dataList1;
            //this.listener= (Listener) context;
            inflater = LayoutInflater.from(context);
        }


        @Override
        public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = inflater.inflate(R.layout.recyclerview_one, parent, false);
            ListViewHolder viewHolder = new ListViewHolder(convertView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ListViewHolder holder, final int position) {
            holder.tv_name.setText(dataList.get(position).name);
            holder.tv_quantity.setText(dataList.get(position).quantity);
            holder.tv_description.setText(dataList.get(position).description + "");



            holder.relLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String s = dataList.get(position).id;
                    Integer stringo = Integer.parseInt(s);

                    /**
                     * This lines are hidden for test
                     Intent intent = new Intent(context, ItemEditActivity.class);
                     intent.putExtra("ItemNumber", stringo);
                     context.startActivity(intent);
                     */

                }
            });

        }
        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class ListViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
            TextView tv_name, tv_quantity, tv_description;
            RelativeLayout relLayout;

            public ListViewHolder(View itemView) {
                super(itemView);
                tv_name = (TextView) itemView.findViewById(R.id.nameDisplay);
                tv_quantity = (TextView) itemView.findViewById(R.id.quantityDisplay);
                tv_description = (TextView) itemView.findViewById(R.id.descriptionDisplay);
                relLayout = (RelativeLayout) itemView.findViewById(R.id.relLayout);
            }
            @Override
            public void onItemSelected() {
                Log.d(TAG, "item selected");
            }

            @Override
            public void onItemClear() {
                Log.d(TAG, "item clear");
                for (int count = 0; count < dataList.size(); count++) {
                    UserData u = dataList.get(count);
                    u.setSort(count);
                    mDbHelper.updateUserData(u);
                }
                notifyDataSetChanged();
            }
        }
        public void onItemDismiss(int position) {
            dataList.remove(position);
            notifyItemRemoved(position);
        }
        public boolean onItemMove(int fromPosition, int toPosition) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(dataList, i, i + 1);
                    Log.d(TAG, toPosition + "");
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(dataList, i, i - 1);
                    Log.d(TAG, toPosition + "");
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }
    }
}
