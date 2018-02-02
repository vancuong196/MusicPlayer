package player.project.com.musicplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {
    RecyclerView mRecyclerView;
    RecyclerViewAdapter mRcvAdapter;
    List<String> data;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        data = new ArrayList<>();
        data.add("Nguyễn Minh Hưng");
        data.add("Hoàng Minh Lợi");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Ngọc Doanh");
        data.add("Nguyễn Phạm Thế Hà");
        data.add("Trần Anh Đức");
        data.add("Trần Minh Hải");
        mRcvAdapter = new RecyclerViewAdapter(view.getContext(), data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRcvAdapter);
        // Inflate the layout for this fragment
    }
}