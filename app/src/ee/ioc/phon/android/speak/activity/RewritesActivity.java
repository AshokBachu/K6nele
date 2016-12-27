/*
 * Copyright 2015-2016, Institute of Cybernetics at Tallinn University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ee.ioc.phon.android.speak.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import ee.ioc.phon.android.speak.R;
import ee.ioc.phon.android.speak.fragment.K6neleListFragment;

// TODO: use CursorAdapter to be able to specify the filterting
public class RewritesActivity extends Activity {

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_STRING_ARRAY = "EXTRA_STRING_ARRAY";

    private RewritesFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragment = new RewritesFragment();
        getFragmentManager().beginTransaction().add(android.R.id.content, mFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rewrites, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(mFragment);

        return true;
    }

    public static class RewritesFragment extends K6neleListFragment implements SearchView.OnQueryTextListener {

        @Override
        public void onResume() {
            super.onResume();
            Intent intent = getActivity().getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String title = extras.getString(EXTRA_TITLE);
                if (title != null) {
                    ActionBar actionBar = getActivity().getActionBar();
                    if (actionBar != null) {
                        actionBar.setSubtitle(title);
                    }
                }
                String[] stringArray = extras.getStringArray(EXTRA_STRING_ARRAY);
                if (stringArray != null) {
                    setListAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_rewrite, stringArray));
                }
                getListView().setFastScrollEnabled(true);
            }
            setEmptyView(getString(R.string.emptylistRewriteRules));
        }

        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            ((ArrayAdapter) getListAdapter()).getFilter().filter(s);
            return true;
        }
    }
}