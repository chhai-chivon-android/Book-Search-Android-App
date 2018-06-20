package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader <List <Book>> {

    private String mSearchTerm;
    public static final int numberOfResults = 10;

    public BookLoader(Context context, String searchTerm) {
        super(context);
        mSearchTerm = searchTerm;
    }

    @Override
    public List <Book> loadInBackground() {
        if (mSearchTerm == null) {
            return null;
        }
        List <Book> bookList = QueryUtils.extractBooksFromSearchTerm(mSearchTerm, numberOfResults);
        return bookList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
