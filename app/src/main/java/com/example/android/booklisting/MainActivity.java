package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks <List <Book>> {

    private SearchView bookQueryView;
    private ListView bookItemsListView;
    private ProgressBar mProgressBar;
    private TextView mEmptyTextView;

    public static final int BOOK_LOADER = 1;
    private LoaderManager mLoaderManager;
    private ArrayList<Book> mBookList;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List <Book> bookArrayList;
    private BookArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoaderManager = getLoaderManager();
        // the list returned by onRetainCustomNonConfigurationInstance. contains items before rotation occured
        bookArrayList = (ArrayList<Book>) getLastCustomNonConfigurationInstance();
        initViewsAndData();
    }

    private void initViewsAndData() {
        bookQueryView = (SearchView) findViewById(R.id.querybar_book);
        bookQueryView.setSubmitButtonEnabled(true);
        mProgressBar = (ProgressBar) findViewById(R.id.booksloading_bar);
        mProgressBar.setVisibility(View.GONE);

        mEmptyTextView = (TextView) findViewById(R.id.empty_textview);

        bookItemsListView = (ListView) findViewById(R.id.book_listview);
        bookItemsListView.setEmptyView(mEmptyTextView);

        // if the device returns a list before rotation occured (not null), dont change the list. otherwise initialize a new list object
        if(bookArrayList == null) {
            bookArrayList = new ArrayList <Book>();
        }

        mAdapter = new BookArrayAdapter(MainActivity.this, bookArrayList);

        bookItemsListView.setAdapter(mAdapter);


        bookItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                Book currentBook = mAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentBook.getCanonicalUrl()));
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });

        bookQueryView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                //check if there is internet connection. else do not spin off a loader
                ConnectivityManager cm =
                        (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    /*spin off a loader*/
                    Bundle b = new Bundle();
                    b.putString("searchquery", s);
                    mLoaderManager.restartLoader(BOOK_LOADER, b, MainActivity.this);/*diff between restart loader and init loader: https://stackoverflow.com/q/14445070*/
                } else {
                    mEmptyTextView.setText(R.string.no_internet);
                    mAdapter.clear();
                    mAdapter.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
               /* Log.e("chirag","onquerytextchange");
                Bundle b = new Bundle();
                b.putString("searchquery",s);
                getLoaderManager().restartLoader(BOOK_LOADER,b,MainActivity.this);
               */
                return false;
            }
        });
    }



    @Override
    public Loader <List <Book>> onCreateLoader(int i, Bundle bundle) {
        mProgressBar.setVisibility(View.VISIBLE);
        return new BookLoader(MainActivity.this, bundle.getString("searchquery"));
    }

    @Override
    public void onLoadFinished(Loader <List <Book>> loader, List <Book> bookList) {
        mBookList = (ArrayList <Book>) bookList;
        mProgressBar.setVisibility(View.GONE);
        mEmptyTextView.setText(R.string.no_results_search);
        mAdapter.clear();
        mAdapter.addAll(mBookList);
        mAdapter.notifyDataSetChanged();
    }

    /*This function is needed because android team apparently decided to let devs handle screen rotation changes.
    Function called when the device is about to rotate to save custom objs.
    can be retained via getLastCustomNonConfigurationInstance in oncreate*/
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mBookList;
    }

    @Override
    public void onLoaderReset(Loader <List <Book>> loader) {
        mAdapter.clear();
    }
}
