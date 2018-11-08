package technology.nine.doubleslitproject.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Switch;

import technology.nine.doubleslitproject.fragments.AudioFragment;
import technology.nine.doubleslitproject.fragments.ImageFragment;
import technology.nine.doubleslitproject.fragments.VideoFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new ImageFragment();
        }
        if (i == 1){
            return  new VideoFragment();
        }

            return null;
    }

    @Override
    public int getCount() {
        return 2 ;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return  "Images";
        }
        if (position == 1){
            return  "Videos";
        }
        return  null;
    }
}
