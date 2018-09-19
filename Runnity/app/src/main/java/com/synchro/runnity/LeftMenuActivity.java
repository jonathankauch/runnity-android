package com.synchro.runnity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Map;

public class LeftMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private TextView title;
    private NavigationView navigationView;

    String[] sortArrayWithDistance = {"Alphabétique", "Distance", "Évolution CA croissant", "Évolution CA décroissant", "CA croissant", "CA décroissant", "Visites Anciennes -> Récentes", "Visites Récentes -> Anciennes"};
    String[] sortArrayWithoutDistance = {"Alphabétique", "Évolution CA croissant", "Évolution CA décroissant", "CA croissant", "CA décroissant", "Visites Anciennes -> Récentes", "Visites Récentes -> Anciennes"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        setupDrawerContent(navigationView);
//        System.out.println(Singleton.getInstance().getToken());

        Fragment fragment = null;

        Class fragmentClass;
        fragmentClass = MapsFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        if (!menuItem.isChecked()) {
            Fragment fragment = null;

            Class fragmentClass;
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    fragmentClass = MapsFragment.class;
                    break;
                case R.id.nav_wall:
                    fragmentClass = PostsFragment.class;
                    break;
                case R.id.nav_friends:
                    fragmentClass = FriendsFragment.class;
                    break;
                case R.id.nav_runs:
                    fragmentClass = RunsFragment.class;
                    break;
                case R.id.nav_rankings:
                    fragmentClass = RankingsFragment.class;
                    break;
                case R.id.nav_spots:
                    fragmentClass = SpotsFragment.class;
                    break;
                case R.id.nav_events:
                    fragmentClass = EventsFragment.class;
                    break;
                case R.id.naw_groups:
                    fragmentClass = GroupsFragment.class;
                    break;
                case R.id.nav_profile:
                    fragmentClass = ProfileFragment.class;
                    break;
                case R.id.nav_stats:
                    fragmentClass = StatsFragment.class;
                    break;
                case R.id.nav_charts:
                    fragmentClass = ChartsFragment.class;
                    break;
                case R.id.nav_objectives:
                    fragmentClass = ObjectivesFragment.class;
                    break;
                case R.id.nav_disconnect:
                    super.onBackPressed();
                    return;
                default:
                    fragmentClass = MapsFragment.class;
                    break;
            }

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().show(fragment).commit();
            Fragment myFragment = (Fragment)fragmentManager.findFragmentByTag("" + menuItem.getItemId());
            if (myFragment != null)
                fragment = myFragment;
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment, "" + menuItem.getItemId()).commit();

            // Highlight the selected item, update the title, and close the drawer
            menuItem.setChecked(true);
            title.setText(menuItem.getTitle());
        }
        mDrawer.closeDrawers();
    }


    @Override
    public void onBackPressed() {
/*        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if (navigationView.getMenu().getItem(1).isChecked()) {
            Fragment fragment = null;

            Class fragmentClass;
            fragmentClass = MapsFragment.class;

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_top,
                    R.anim.exit_to_bot).replace(R.id.flContent, fragment).commit();
            navigationView.getMenu().getItem(0).setChecked(true);
        } else {
            moveTaskToBack(true);
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.left_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public DrawerLayout getmDrawer() {
        return mDrawer;
    }

    public void setmDrawer(DrawerLayout mDrawer) {
        this.mDrawer = mDrawer;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }

    public TextView getToolBarTitle() {
        return title;
    }

    public void setToolBarTitle(TextView title) {
        this.title = title;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public void setNavigationView(NavigationView navigationView) {
        this.navigationView = navigationView;
    }
}
