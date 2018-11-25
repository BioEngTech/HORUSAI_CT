package vigi.patient.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vigi.patient.R;

public class HealthFragment extends Fragment {

    public HealthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_health, container, false);

        ViewPager mViewPager = view.findViewById(R.id.userHealthFragment_ViewPager);
        setupViewPager(mViewPager);

        TabLayout mTabLayout = view.findViewById(R.id.userHealthFragment_TabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }


    private void setupViewPager(ViewPager viewPager){

        SectionsPageAdapter adapter = new SectionsPageAdapter(getFragmentManager());
        adapter.addFragment(new MedicalFileFragment(),getString(R.string.medical_file_text));
        adapter.addFragment(new MedicalFileFragment(),getString(R.string.health_record_text));
        viewPager.setAdapter(adapter);

    }

}
