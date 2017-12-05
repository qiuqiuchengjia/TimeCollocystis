package com.framework.net.db.controller;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.framework.log.DebugLog;
import com.framework.log.LogUtil;
import com.framework.net.db.dbutils.DBController;
import com.framework.net.db.dbutils.DBNotInitializeException;
import com.framework.net.db.domain.HttpCacheEntity;
import com.framework.net.db.utils.Lists;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;

/**
 * Created by 2015-4-10
 * 
 * 
 *
 */
public class HttpCacheController {
	private static final String LOGTAG = LogUtil
			.makeLogTag(HttpCacheController.class);
	private static final int EXPIRED_DAYS = 3;// Get three's date using Date
												// default

	private static final int EXPIRED_MINUTES = 15;

	private static Dao<HttpCacheEntity, String> getDao() throws SQLException,
			DBNotInitializeException {
		return DBController.getDB().getDao(HttpCacheEntity.class);
	}

	public static byte[] getCacheByUrl(String url) {
		try {
			List<HttpCacheEntity> result = getDao().queryForEq(
					HttpCacheEntity.TARGET_URL, url);
			if (Lists.isValidate(result)) {
				HttpCacheEntity entity = result.get(0);
				if (entity != null) {
					//entity.setUpdateTime(System.currentTimeMillis());
					//addOrUpdate(entity);
					return entity.getJson();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addOrUpdate(HttpCacheEntity entity) {
		try {
			entity.setUpdateTime(System.currentTimeMillis());
			getDao().createOrUpdate(entity);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}
	}

	public static void delete(List<HttpCacheEntity> cacheEntities) {
		try {
			DatabaseConnection connection = getDao().startThreadConnection();
			getDao().delete(cacheEntities);
			getDao().endThreadConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * clear local expired cache
	 *
	 * @param dur
	 *            dur days
	 */
	public static void clearExpired(int dur) {
		clearExpired(getExpiredTime(dur));
	}

	/**
	 * get expired time
	 *
	 * @return date
	 */
	private static Date getExpiredTime(int space) {
		Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.DATE, -space);
		cal.add(Calendar.MINUTE, -space);
		DebugLog.i(LOGTAG, "time:" + cal.getTime());
		return cal.getTime();
	}

	/**
	 * support delete expired row
	 *
	 * @param date
	 *            expired date
	 */
	public static void clearExpired(Date date) {
		try {
			List<HttpCacheEntity> resultList = Lists.newArrayList();
			QueryBuilder<HttpCacheEntity, String> queryBuilder = getDao()
					.queryBuilder();
			Where<HttpCacheEntity, String> wheres = queryBuilder.where();
			wheres.le(HttpCacheEntity.UPDATETIME, date.getTime());
			resultList = getDao().query(queryBuilder.prepare());
			if (Lists.isValidate(resultList)) {
				delete(resultList);
				DebugLog.i(LOGTAG, "clearLocalCache:" + resultList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * clear cache data
	 * 
	 * @return
	 */
	public static int clearExpired() {
		int row = 0;
		try {

			DeleteBuilder<HttpCacheEntity, String> deleteBuilder = getDao()
					.deleteBuilder();
			PreparedDelete<HttpCacheEntity> preparedDelete = deleteBuilder
					.prepare();
			row = getDao().delete(preparedDelete);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}
		return row;
	}

	/**
	 * delete request failure data cache
	 * 
	 * @return
	 */
	public static int deleteForFailure(String url) {
		int row = 0;
		try {

			DeleteBuilder<HttpCacheEntity, String> deleteBuilder = getDao()
					.deleteBuilder();
			Where<HttpCacheEntity, String> where = deleteBuilder.where();
			where.eq(HttpCacheEntity.TARGET_URL, url);
			PreparedDelete<HttpCacheEntity> preparedDelete = deleteBuilder
					.prepare();
			row = getDao().delete(preparedDelete);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DBNotInitializeException e) {
			e.printStackTrace();
		}
		return row;
	}

	/**
	 * add default clear cache method (3 days)
	 */
	public static void clearLocalCache() {
		try {
			// Clear out expired cookies
			// clearExpired(EXPIRED_DAYS);// clear before three days data
			clearExpired(EXPIRED_MINUTES);// clear before three days data
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * clear cache data
	 */
	public static int clearCache() {
		return clearExpired();
	}

	/**
	 * clear request failure data cache
	 * 
	 * @param url
	 * @return
	 */
	public static int deleteFailure(String url) {
		return deleteForFailure(url);
	}

}
