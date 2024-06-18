package com.example.eventmate.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.eventmate.Category;
import com.example.eventmate.Event;

import java.util.List;



public class EMAViewModel extends AndroidViewModel {
    // reference to CardRepository
    private EMARepository repository;
    // private class variable to temporary hold all the items retrieved and pass outside of this class
    private LiveData<List<Category>> allCategoryLiveData;
    private LiveData<List<Event>> allEventLiveData;
    public EMAViewModel(@NonNull Application application) {
        super(application);

        // get reference to the repository class
        repository = new EMARepository(application);

        // get all items by calling method defined in repository class
        allCategoryLiveData = repository.getAllCategories();
        allEventLiveData = repository.getAllEvents();
    }


    public LiveData<List<Category>> getAllCategories() {
        return allCategoryLiveData;
    }

    public void insertCategory(Category category) {
        repository.insertCategory(category);
    }

    public void deleteAllCategory() {
        repository.deleteAllCategory();
    }

    public LiveData<List<Category>> getCategoryById(String categoryId){
        return repository.getCategoryById(categoryId);
    }
    public void incrementEventCount(String categoryId){
        repository.incrementEventCount(categoryId);
    }

    public void decrementEventCount(String categoryId){
        repository.decrementEventCount(categoryId);
    }

    public void resetAllEventCounts(){
        repository.resetAllEventCounts();
    }

    public LiveData<List<Event>> getAllEvents() {
        return allEventLiveData;
    }

    public void insertEvent(Event event) {
        repository.insertEvent(event);
    }

    public void deleteAllEvent() {
        repository.deleteAllEvent();
    }

    public void deleteMostRecentEvent() {
        repository.deleteMostRecentEvent();
    }

}

