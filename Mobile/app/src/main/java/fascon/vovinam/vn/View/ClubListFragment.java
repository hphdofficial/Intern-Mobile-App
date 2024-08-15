package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.ViewModel.ClubAdapter;
import fascon.vovinam.vn.Model.Club;

import java.util.ArrayList;
import java.util.List;

public class ClubListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClubAdapter adapter;
    private List<Club> clubList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_club_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_club);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ClubAdapter(getContext(), clubList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void setData(List<Club> clubs) {
        this.clubList = clubs;
        if (adapter != null) {
            adapter.setData(clubs);
            adapter.notifyDataSetChanged();
        }
    }
}