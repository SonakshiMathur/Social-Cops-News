package com.example.sonakshi.socialcops.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.sonakshi.socialcops.data.dao.HeadlinesDao;
import com.example.sonakshi.socialcops.data.dao.SavedDao;
import com.example.sonakshi.socialcops.models.Article;
import com.example.sonakshi.socialcops.models.SavedArticle;

@Database(entities = {Article.class, SavedArticle.class},
        version = 1,
        exportSchema = false)
@TypeConverters(DatabaseConverters.class)
public abstract class NewsDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "news";
    private static NewsDatabase sInstance;

    public static NewsDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        NewsDatabase.class,
                        DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

    public abstract HeadlinesDao headlinesDao();

    public abstract SavedDao savedDao();
}
