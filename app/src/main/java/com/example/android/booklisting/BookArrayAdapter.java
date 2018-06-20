package com.example.android.booklisting;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookArrayAdapter extends ArrayAdapter <Book> {

    public BookArrayAdapter(Activity context, List <Book> bookList) {
        super(context, 0, bookList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_itemlayout, parent, false);
        }

        Context context = getContext();
        Resources resources = context.getResources();

        Book currentBook = this.getItem(position);


        TextView bookTitleTextView = currentItemView.findViewById(R.id.book_title);
        String bookTitle = currentBook.getTitle();
        String subTitle = currentBook.getSubtitle();
        String combinedSubtitle = createCombinedSubtitle(bookTitle,subTitle);
        bookTitleTextView.setText(combinedSubtitle);

        TextView bookAuthorTextView = currentItemView.findViewById(R.id.author);
        List <String> authorList = currentBook.getAuthors();
        String authorString = resources.getString(R.string.by_authors_string) + TextUtils.join(",", authorList);
        bookAuthorTextView.setText(authorString);

        TextView publishedYear = currentItemView.findViewById(R.id.published_year);
        String currentYear = extractCurrentYearFromDateString(currentBook.getPublishedDate());
        String publicationYearString = resources.getString(R.string.publicationyear_substring) +" " + currentYear;
        publishedYear.setText(publicationYearString);

        return currentItemView;
    }

    private static String extractCurrentYearFromDateString(String publishedDate) {
        String year = "";
        if(publishedDate !=null && !publishedDate.isEmpty()) {
            year = publishedDate.substring(0, 4);
        }
        return year;
    }

    private static String createCombinedSubtitle(String bookTitle, String subTitle) {
        if (subTitle!=null && !subTitle.trim().isEmpty()){
            bookTitle = bookTitle + ": " + subTitle;
        }
        return bookTitle;
    }
}
