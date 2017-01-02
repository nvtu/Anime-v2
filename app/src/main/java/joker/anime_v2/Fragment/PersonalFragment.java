package joker.anime_v2.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import joker.anime_v2.R;
import joker.anime_v2.SettingActivity;

/**
 * Created by Tu Van Ninh on 25/12/2016.
 */
public class PersonalFragment extends Fragment{

    public PersonalFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_personal_fragment, container, false);
        initLayouts(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }


    private void initLayouts(View rootView) {
        ImageView userCoverView = (ImageView) rootView.findViewById(R.id.user_cover);
        ImageView userProfilePicture = (ImageView) rootView.findViewById(R.id.avatar);
        Picasso.with(rootView.getContext()).load(R.drawable.avatars).into(userProfilePicture);
        Picasso.with(rootView.getContext()).load(R.drawable.night).into(userCoverView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.personal_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.setting:
                Intent intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
