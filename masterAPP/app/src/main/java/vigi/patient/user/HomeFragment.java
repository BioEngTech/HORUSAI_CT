package vigi.patient.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import vigi.patient.R;
import vigi.patient.utils.proxy.NullProxy;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_help, container, false);

        Toolbar myToolbar = view.findViewById(R.id.userHomeFragment_Toolbar);

        myToolbar.setNavigationIcon(R.drawable.icon_drawer);

        AppCompatActivity activity = NullProxy.createProxy(((AppCompatActivity) getActivity()));
        activity.setSupportActionBar(myToolbar);

        ActionBar actionBar = NullProxy.createProxy(activity.getSupportActionBar());
        actionBar.setDisplayShowTitleEnabled(false);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_items, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_message:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
