package com.example.eventmate.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.eventmate.Category;
import com.example.eventmate.Event;

import java.util.List;

@Dao
public interface EMADAO {

    @Query("select * from categories")
    LiveData<List<Category>> getAllCategories();

    @Insert
    void addCategory(Category category);

    @Query("DELETE FROM categories")
    void deleteAllCategory();

    @Query("SELECT * FROM categories WHERE categoryId = :categoryId")
    LiveData<List<Category>> getCategoryById(String categoryId);

    @Query("UPDATE categories SET eventCount = eventCount + 1 WHERE categoryId = :categoryId")
    void incrementEventCount(String categoryId);

    @Query("UPDATE categories SET eventCount = eventCount - 1 WHERE categoryId = :categoryId")
    void decrementEventCount(String categoryId);

    @Query("UPDATE categories SET eventCount = 0")
    void resetAllEventCounts();

    @Query("select * from events")
    LiveData<List<Event>> getAllEvents();

    @Insert
    void addEvent(Event event);

    @Query("DELETE FROM events")
    void deleteAllEvent();

    @Query("DELETE FROM events WHERE id = (SELECT MAX(id) FROM events)")
    void deleteMostRecentEvent();
}
