package com.example.android.kidtrackerparent.AsyncTasks;

public interface AsyncResponse<T> {
    void onSuccess(T item);
    void onFailure();
}
