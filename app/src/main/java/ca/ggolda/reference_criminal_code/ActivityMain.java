package ca.ggolda.reference_criminal_code;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by gcgol on 01/18/2017.
 */

public class ActivityMain extends AppCompatActivity {

    private ListView mListViewSections;
    private AdapterSection mAdapterSection;

    private AdapterHeading mAdapterHeading;
    private ListView mListViewHeadings;

    private ImageView mBtnSearch;
    private ImageView mBtnSearchOpen;

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

        mBtnParts = (ImageView) findViewById(R.id.btn_parts);
        mParts = (LinearLayout) findViewById(R.id.parts);

        mAdapterSection = new AdapterSection(ActivityMain.this, R.layout.card_section, dbHelper.getAllUser());
        mListViewSections = (ListView) findViewById(R.id.listview_section);
        mListViewSections.setAdapter(mAdapterSection);

        mAdapterHeading = new AdapterHeading(ActivityMain.this, R.layout.card_heading, dbHelper.getAllHeading());
        mListViewHeadings = (ListView) findViewById(R.id.listview_heading);
        mListViewHeadings.setAdapter(mAdapterHeading);

        layoutSearchbar = (RelativeLayout) findViewById(R.id.lyt_search);
        mBtnSearchOpen = (ImageView) findViewById(R.id.btn_search_open);
        mBtnSearch = (ImageView) findViewById(R.id.btn_search);



        // bring parts up or down
        mBtnParts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                partsHideShow();
            }
        });

        // bring parts up or down
        mBtnSearchOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSearchbar.setVisibility(View.VISIBLE);
            }
        });

        // bring parts up or down
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSearchbar.setVisibility(View.GONE);
            }
        });




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
        Intent intent = new Intent(this, ActivityTestLaunch.class);
        startActivity(intent);
        finish();
    }

}