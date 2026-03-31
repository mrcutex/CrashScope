package com.cutex.crashscope.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.cutex.crashscope.data.model.CrashEvent;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CrashDao_Impl implements CrashDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CrashEvent> __insertionAdapterOfCrashEvent;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public CrashDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCrashEvent = new EntityInsertionAdapter<CrashEvent>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `CrashEvent` (`id`,`packageName`,`type`,`exceptionType`,`thread`,`timestamp`,`stackTrace`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CrashEvent entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPackageName());
        statement.bindString(3, entity.getType());
        statement.bindString(4, entity.getExceptionType());
        statement.bindString(5, entity.getThread());
        statement.bindLong(6, entity.getTimestamp());
        statement.bindString(7, entity.getStackTrace());
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM CrashEvent";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CrashEvent crash, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCrashEvent.insert(crash);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clear(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClear.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClear.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CrashEvent>> getAll() {
    final String _sql = "SELECT * FROM CrashEvent ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"CrashEvent"}, new Callable<List<CrashEvent>>() {
      @Override
      @NonNull
      public List<CrashEvent> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfExceptionType = CursorUtil.getColumnIndexOrThrow(_cursor, "exceptionType");
          final int _cursorIndexOfThread = CursorUtil.getColumnIndexOrThrow(_cursor, "thread");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfStackTrace = CursorUtil.getColumnIndexOrThrow(_cursor, "stackTrace");
          final List<CrashEvent> _result = new ArrayList<CrashEvent>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CrashEvent _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpExceptionType;
            _tmpExceptionType = _cursor.getString(_cursorIndexOfExceptionType);
            final String _tmpThread;
            _tmpThread = _cursor.getString(_cursorIndexOfThread);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpStackTrace;
            _tmpStackTrace = _cursor.getString(_cursorIndexOfStackTrace);
            _item = new CrashEvent(_tmpId,_tmpPackageName,_tmpType,_tmpExceptionType,_tmpThread,_tmpTimestamp,_tmpStackTrace);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
