package com.example.assignment1.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.assignment1.Category;
import com.example.assignment1.Event;

import java.util.List;

public class EMARepository {


    private EMADAO emaDAO;
    private LiveData<List<Category>> allCategoryLiveData;

    private LiveData<List<Event>> allEventLiveData;

    EMARepository(Application application) {

        EMADatabase db = EMADatabase.getDatabase(application);

        emaDAO = db.emaDAO();

        allCategoryLiveData = emaDAO.getAllCategories();
        allEventLiveData = emaDAO.getAllEvents();
    }


    LiveData<List<Category>> getAllCategories() {
        return allCategoryLiveData;
    }

    void insertCategory(Category category) {
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.addCategory(category));
    }

    void deleteAllCategory() {
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteAllCategory());
    }

    LiveData<List<Category>> getCategoryById(String categoryId){
        return emaDAO.getCategoryById(categoryId);
    }

    void incrementEventCount(String categoryId){
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.incrementEventCount(categoryId));
    }

    void decrementEventCount(String categoryId){
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.decrementEventCount(categoryId));
    }

    void resetAllEventCounts(){
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.resetAllEventCounts());
    }

    LiveData<List<Event>> getAllEvents() {
        return allEventLiveData;
    }

    void insertEvent(Event event) {
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.addEvent(event));
    }

    void deleteAllEvent() {
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteAllEvent());
    }

    void deleteMostRecentEvent(){
        EMADatabase.databaseWriteExecutor.execute(() -> emaDAO.deleteMostRecentEvent());
    }

}
