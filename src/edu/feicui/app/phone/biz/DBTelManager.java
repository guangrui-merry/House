package edu.feicui.app.phone.biz;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import edu.feicui.app.phone.entity.ClassInfo;
import edu.feicui.app.phone.entity.TableInfo;

public class DBTelManager {
	private static final String FILE_NAME = "commonnum.db"; // db文件名称
	private static final String PACKAGE_NAME = "com.androide.app.phone"; // 当前应用程序包名
	// 文件路径
	private static final String FILE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;
	// private static final String DBFILE_PATH =
	// Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
	// PACKAGE_NAME;

	public static void readUpdateDB(InputStream path) throws IOException {
		File toFile = new File(FILE_PATH + "/" + FILE_NAME);
		if (toFile.exists()) {
			return;
		}
		// 输入流
		BufferedInputStream buffInputStream = new BufferedInputStream(path);
		// 输出流
		FileOutputStream outputStream = new FileOutputStream(toFile, false);
		BufferedOutputStream buffOutputStream = new BufferedOutputStream(outputStream);

		int len = 0;
		byte[] buff = new byte[5 * 1024];
		while ((len = buffInputStream.read(buff)) != -1) {
			buffOutputStream.write(buff, 0, len);
		}
		buffOutputStream.flush();
		buffOutputStream.close();
		buffInputStream.close();
	}

	public static ArrayList<ClassInfo> readClassListTable() {
		ArrayList<ClassInfo> classInfos = new ArrayList<ClassInfo>();

		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(FILE_PATH + "/" + FILE_NAME, null);
		String sql = "select * from classlist";
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(cursor.getColumnIndex("name")).trim();
				int idx = cursor.getInt(cursor.getColumnIndex("idx"));
				ClassInfo classInfo = new ClassInfo(name, idx);
				classInfos.add(classInfo);
			} while (cursor.moveToNext());
		}
		cursor.close();
		database.close();

		return classInfos;
	}

	public static ArrayList<TableInfo> readTable(String tableName) {
		ArrayList<TableInfo> tableInfos = new ArrayList<TableInfo>();

		SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(FILE_PATH + "/" + FILE_NAME, null);
		String sql = "select * from " + tableName;
		Cursor cursor = database.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			do {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				long number = cursor.getLong(cursor.getColumnIndex("number"));
				TableInfo tableInfo = new TableInfo(name, number);
				tableInfos.add(tableInfo);
			} while (cursor.moveToNext());
		}
		cursor.close();
		database.close();
		return tableInfos;
	}

}
