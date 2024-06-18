package com.example.eventmate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventmate.provider.EMAViewModel;

import java.util.ArrayList;

import com.google.gson.Gson;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListEvent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentListEvent() {
        // Required empty public constructor
    }
    ArrayList<Event> listEvents = new ArrayList<>();
    EventRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    Gson gson = new Gson();

    //ViewModel
    private EMAViewModel emaViewModel;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListEvent newInstance(String param1, String param2) {
        FragmentListEvent fragment = new FragmentListEvent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_list_event, container, false);
        recyclerView = fragmentView.findViewById(R.id.recyclerviewEvent);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //Initialise ViewModel
        emaViewModel = new ViewModelProvider(this).get(EMAViewModel.class);

        recyclerAdapter = new EventRecyclerAdapter();
        recyclerAdapter.setData(listEvents);
        recyclerView.setAdapter(recyclerAdapter);

        // Subscribe to LiveData
        emaViewModel.getAllEvents().observe(getViewLifecycleOwner(), newData -> {
            recyclerAdapter.setData(new ArrayList<Event>(newData));
            recyclerAdapter.notifyDataSetChanged();
        });

        return fragmentView;
    }

}