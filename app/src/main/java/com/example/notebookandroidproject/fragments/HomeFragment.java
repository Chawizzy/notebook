package com.example.notebookandroidproject.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notebookandroidproject.CreateItemActivity;
import com.example.notebookandroidproject.OpenItemActivity;
import com.example.notebookandroidproject.R;
import com.example.notebookandroidproject.adapters.ItemAdapter;
import com.example.notebookandroidproject.adapters.ItemAdapterInterface;
import com.example.notebookandroidproject.models.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements ItemAdapterInterface {

    /*
    Variables.
    */
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private List<Item> itemList;
    private ItemAdapter itemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*
        Initializing the floating action button.
        */
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setColorFilter(Color.WHITE);

        /*
        Initializing the recycler view.
        */
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /*
        Initializing the list array.
        */
        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(HomeFragment.this, getContext(), itemList);
        recyclerView.setAdapter(itemAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*
        Redirects to a method called loadItemList.
        */
        loadItemList();

        /*
        floating action button redirects to a method called openCreateItemActivity().
        */
        floatingActionButton.setOnClickListener(v -> openCreateItemActivity());
    }

    /*
    Transfers item objects to list array, then display at HomeFragment as CardView.
    */
    public void loadItemList() {
        FirebaseDatabase.getInstance().getReference("Item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();

                if(snapshot.exists()) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Item item = dataSnapshot.getValue(Item.class);

                        if(Objects.requireNonNull(item).getUserID().equals(Objects
                                .requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                            itemList.add(item);
                        }
                    }

                    itemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    Open OpenItemActivity after HomeFragment CardView is clicked, and transfer item data
    using intent.
    */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), OpenItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /*
        Creating item object.
        */
        Item item = new Item();
        item.setItemID(itemList.get(position).getItemID());
        item.setUserID(itemList.get(position).getUserID());
        item.setLocationID(itemList.get(position).getLocationID());
        item.setName(itemList.get(position).getName());
        item.setDescription(itemList.get(position).getDescription());
        item.setPrice(itemList.get(position).getPrice());
        item.setImageURL(itemList.get(position).getImageURL());
        item.setPurchased(itemList.get(position).isPurchased());

        /*
        Transferring item object to OpenItemActivity.class.
        */
        intent.putExtra("item", item);
        startActivity(intent);
    }

    /*
    Open CreateItemActivity.
    */
    private void openCreateItemActivity() {
        Intent intent = new Intent(getContext(), CreateItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}