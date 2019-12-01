package com.medialink.rxjavachap1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArrayAdapter<String> arrayAdapter;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView listView = findViewById(R.id.lv_result);
        listView.setAdapter(arrayAdapter);

        final EditText edSearch = findViewById(R.id.ed_search);
        RxTextView.textChanges(edSearch)
                .doOnNext(text -> this.clearSearchResult())
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) throws Exception {
                        return charSequence.length() >= 3;
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::updateSearchResult);
    }

    private void clearSearchResult() {
        arrayAdapter.clear();
    }

    private void updateSearchResult(CharSequence text) {
        Log.d(TAG, "updateSearchResult: test");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add(String.format("result: %s %.2f", text, Math.random()));
        }

        arrayAdapter.clear();
        arrayAdapter.addAll(list);
    }
}
