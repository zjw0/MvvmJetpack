package com.mvvm.libnetwork.cache;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mvvm.libcommon.AppGlobals;

public abstract class CacheDatabase extends RoomDatabase {

    private static final CacheDatabase database;

        static {
            //创建一个内存数据库
            //但是这种数据库的数据只存在于内存中，也就是进程被杀之后，数据随之丢失
            //Room.inMemoryDatabaseBuilder()
            database = Room.databaseBuilder(AppGlobals.getApplication(), CacheDatabase.class, "ppjoke_cache")
                    //是否允许在主线程进行查询
                    .allowMainThreadQueries()
                    //数据库创建和打开后的回调
                    //.addCallback()
                    //设置查询的线程池
                    //.setQueryExecutor()
                    //.openHelperFactory()
                    //room的日志模式
                    //.setJournalMode()
                    //数据库升级异常之后的回滚
                    //.fallbackToDestructiveMigration()
                    //数据库升级异常后根据指定版本进行回滚
                    //.fallbackToDestructiveMigrationFrom()
                    // .addMigrations(CacheDatabase.sMigration)
                    .build();

        }

    public abstract CacheDao getCache();

    public static CacheDatabase get() {
        return database;
    }

//    static Migration sMigration = new Migration(1, 3) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("alter table teacher rename to student");
//            database.execSQL("alter table teacher add column teacher_age INTEGER NOT NULL default 0");
//        }
//    };
}
