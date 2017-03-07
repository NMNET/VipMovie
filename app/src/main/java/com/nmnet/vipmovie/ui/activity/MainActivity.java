package com.nmnet.vipmovie.ui.activity;

import android.support.v4.app.Fragment;

import com.nmnet.parentlib.ui.activity.ParentBaseMainActivity;
import com.nmnet.vipmovie.R;
import com.nmnet.vipmovie.ui.fragment.FirstFragment;
import com.nmnet.vipmovie.ui.fragment.ForthFragment;
import com.nmnet.vipmovie.ui.fragment.SecondFragment;
import com.nmnet.vipmovie.ui.fragment.ThirdFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ParentBaseMainActivity {

    @Override
    protected int[] setTabIcons() {
        return new int[]{R.mipmap.ic_bnb_part1, R.mipmap.ic_bnb_part2,
                R.mipmap.ic_bnb_part3, R.mipmap.ic_bnb_part4};
    }

    @Override
    protected String[] setTabStrings() {
        return new String[]{"PHONE", "MSM", "SYNC", "CONT"};
    }

    @Override
    protected List<Class<? extends Fragment>> setFragmentClasses() {
        List<Class<? extends Fragment>> fragmentClasses = new ArrayList<>();
        fragmentClasses.add(FirstFragment.class);
        fragmentClasses.add(SecondFragment.class);
        fragmentClasses.add(ThirdFragment.class);
        fragmentClasses.add(ForthFragment.class);
        return fragmentClasses;
    }
}
