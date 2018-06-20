package com.example.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /*Don't instantiate utils
     * class. Directly use the methods*/
    private QueryUtils() {
    }

    /*Sample complete url: https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10 */
    private static final String halfBakedUrl = "https://www.googleapis.com/books/v1/volumes?q=";

    private static String createFullUrlString(String halfBakedUrl, String searchParameter, int numberOfResults) {
        return halfBakedUrl + createSearchParameters(searchParameter) + "&maxResults=" + numberOfResults;
    }

    private static String createSearchParameters(String searchTerm) {
        String returnString = new String();
        String[] splitStr = searchTerm.split("\\s+");
        returnString = TextUtils.join("+", splitStr);
        return returnString;
    }

    public static List <Book> extractBooksFromSearchTerm(String searchTerm, int numberOfResults) {
        List <Book> resultsBookList = new ArrayList <>();
        String urlString = createFullUrlString(halfBakedUrl, searchTerm, numberOfResults);

        String stringJsonResponse = null;
        URL queryUrl = createUrl(urlString);
        try {
            stringJsonResponse = makeHttpRequest(queryUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        resultsBookList = extractBookListFromJson(stringJsonResponse);
        return resultsBookList;
    }

    private static String makeHttpRequest(URL queryUrl) throws IOException {

        if (queryUrl == null) {
            return null;
        }

        InputStream inputStream = null;
        String jsonResponseString = null;
        HttpURLConnection httpconnection = null;
        try {
            httpconnection = (HttpURLConnection) queryUrl.openConnection();
            httpconnection.setConnectTimeout(15000);
            httpconnection.setReadTimeout(10000);
            httpconnection.setRequestMethod("GET");
            httpconnection.connect();

            if (httpconnection.getResponseCode() == 200) {
                inputStream = httpconnection.getInputStream();
                jsonResponseString = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "response code is not 200. response code: " + httpconnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (httpconnection != null) {
                httpconnection.disconnect();
            }

        }
        return jsonResponseString;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null) {
                response.append(line);
                line = reader.readLine();
            }
        }
        return response.toString();
    }

    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static List <Book> extractBookListFromJson(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
            return null;
        }
        List <Book> bookList = new ArrayList <Book>();
        try {
            JSONObject rootObject = new JSONObject(jsonResponse);
            JSONArray itemsArray = rootObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject currentBookJsonObj = itemsArray.getJSONObject(i);
                JSONObject volumeInfoJsonObj = currentBookJsonObj.getJSONObject("volumeInfo");
                String bookTitle = volumeInfoJsonObj.getString("title");

                JSONArray authorJsonArray = volumeInfoJsonObj.getJSONArray("authors");
                String authors = authorJsonArray.join(", ");
                List <String> authorList = new ArrayList <>();
                authorList.add(authors);

                Book book = new Book(authorList,bookTitle);
                book.setCanonicalUrl(extractInfoLinkFromJsonObj(volumeInfoJsonObj));
                book.setSubtitle(extractSubTitleFromJsonObj(volumeInfoJsonObj));
                book.setPublishedDate(extractPublishedDate(volumeInfoJsonObj));

                bookList.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bookList;
    }

    private static String extractPublishedDate(JSONObject volumeInfoJsonObj) {
        String date = new String();
        try {
            date = volumeInfoJsonObj.getString("publishedDate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static String extractInfoLinkFromJsonObj(JSONObject volumeInfoJsonObj) {
        String infoLink = new String();
        try {
            infoLink = volumeInfoJsonObj.getString("infoLink");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return infoLink;
    }

    private static String extractSubTitleFromJsonObj(JSONObject volumeInfoJsonObj) {
        String subtitle=new String();
        try {
             subtitle = volumeInfoJsonObj.getString("subtitle");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subtitle;
    }
}
