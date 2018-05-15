package daniilsmirnov.game.clicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> myDataset = getDataSet();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<String> getDataSet() {

        ArrayList<String> mDataSet = new ArrayList();

        for (int i = 0; i < 100; i++) {
            mDataSet.add(i, "item" + i);
        }
        mDataSet.add(mDataSet.size(), "пункт" + mDataSet.size() + 1);
        return mDataSet;
    }

}
