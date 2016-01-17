package com.example.android.app.popularmovies.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.app.popularmovies.R;
import com.example.android.app.popularmovies.dto.MovieDetails;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by RUPESH on 1/6/2016.
 */
public class DatabaseHelper  extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "popularMovies";
    private static final int DATABASE_VERSION = 1;
    private Dao<MovieDetails, Integer> movieDetailsDAO;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION,
                /**
                 * R.raw.ormlite_config is a reference to the ormlite_config.txt file in the
                 * /res/raw/ directory of this project
                 * */
                R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, MovieDetails.class);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            /**
             * Recreates the database when onUpgrade is called by the framework
             */
            TableUtils.dropTable(connectionSource, MovieDetails.class, false);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
       
    }

    public Dao<MovieDetails, Integer> getMovieDetailsDAO() throws SQLException {
        if(movieDetailsDAO == null) {
            movieDetailsDAO = getDao(MovieDetails.class);
        }
        return movieDetailsDAO;
    }


}
