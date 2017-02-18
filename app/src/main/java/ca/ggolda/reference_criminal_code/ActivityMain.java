package ca.ggolda.reference_criminal_code;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by gcgol on 01/18/2017.
 */

public class ActivityMain extends AppCompatActivity {

    private ListView mListViewSections;
    private AdapterSection mAdapterSection;

    private ListView mListViewHeadings;
    private AdapterHeading mAdapterHeading;

    private ImageView mBtnSearch;
    private ImageView mBtnSearchOpen;
    private EditText mEdtSearch;

    private LinearLayout loadCover;

    private ImageView mBtnParts;
    public static LinearLayout mParts;

    private RelativeLayout layoutSearchbar;

    DbHelper dbHelper;

    public static int partsVisible = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = DbHelper.getInstance(getApplicationContext());

        mAdapterSection = new AdapterSection(ActivityMain.this, R.layout.card_section, dbHelper.getAllSection());
        mListViewSections = (ListView) findViewById(R.id.listview_section);
        mListViewSections.setAdapter(mAdapterSection);

        mAdapterHeading = new AdapterHeading(ActivityMain.this, R.layout.card_heading, dbHelper.getAllHeading());
        mListViewHeadings = (ListView) findViewById(R.id.listview_heading);
        mListViewHeadings.setAdapter(mAdapterHeading);

        mBtnParts = (ImageView) findViewById(R.id.btn_parts);
        mParts = (LinearLayout) findViewById(R.id.parts);

        layoutSearchbar = (RelativeLayout) findViewById(R.id.lyt_search);
        mBtnSearchOpen = (ImageView) findViewById(R.id.btn_search_open);
        mBtnSearch = (ImageView) findViewById(R.id.btn_search);
        mEdtSearch = (EditText) findViewById(R.id.edt_search);
        loadCover = (LinearLayout) findViewById(R.id.load_cover);


        // bring parts up or down
        mBtnParts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                partsHideShow();
            }
        });

        // bring searchbar up
        mBtnSearchOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSearchbar.setVisibility(View.VISIBLE);

                // Kinda hackish, get keyboard and edittext focus onclick
                mEdtSearch.requestFocus();
                InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mEdtSearch,0);
            }
        });

        // bring searchbar down
        // TODO: also search
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSearchbar.setVisibility(View.GONE);

                String query = mEdtSearch.getText().toString();

                Log.e("EEEP", ""+query);

                if(!query.equals("")) {
                    mAdapterSection.clear();
                    mAdapterSection = new AdapterSection(ActivityMain.this, R.layout.card_section, dbHelper.getSearchResults(query));
                    mListViewSections.setAdapter(mAdapterSection);
                }
            }
        });


        // If the no sections in database, import via ActivityImport
        // TODO: figure out why activity main must be accessed before the import func in ActivityImport / ActivityDebug work
        if (dbHelper.getAllSection().size() < 1) {
            Intent intent = new Intent(ActivityMain.this, ActivityImport.class);
            startActivity(intent);
        } else {

            //TODO: fix, still loading via white screen
            // TODO: maybe remove loadCover, vis already set gone in XML as it doesn't seem to work
            loadCover.setVisibility(View.GONE);
        }

    }

    public static void partsHideShow() {
        if (partsVisible == 0) {
            mParts.setVisibility(View.VISIBLE);
            partsVisible = 1;
        } else if (partsVisible == 1) {
            mParts.setVisibility(View.GONE);
            partsVisible = 0;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

}