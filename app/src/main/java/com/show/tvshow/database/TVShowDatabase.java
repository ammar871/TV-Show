package com.show.tvshow.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.show.tvshow.dao.TVShowDao;
import com.show.tvshow.models.TVShow;

@Database(entities = TVShow.class, version =1, exportSchema = false)
public abstract class TVShowDatabase extends RoomDatabase{



        private static TVShowDatabase mInstance;

public abstract TVShowDao tvShowDao();


        public synchronized static TVShowDatabase getDatabaseInstance(Context context) {

            if (mInstance == null) {
                mInstance = Room.databaseBuilder(context.getApplicationContext(), TVShowDatabase.class, "tv_show_db")

                        .build();
            }
            return mInstance;
        }

        public static void destroyInstance() {
            mInstance = null;
        }

    }


