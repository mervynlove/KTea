
import android.database.Cursor

/**
 * Create by MengWei at 2018/7/13
 */

 fun Cursor.getBlob(columnName: String): ByteArray =
        getBlob(getColumnIndexOrThrow(columnName))

 fun Cursor.getDouble(columnName: String): Double =
        getDouble(getColumnIndexOrThrow(columnName))

 fun Cursor.getFloat(columnName: String): Float = getFloat(getColumnIndexOrThrow(columnName))

 fun Cursor.getInt(columnName: String): Int = getInt(getColumnIndexOrThrow(columnName))

 fun Cursor.getLong(columnName: String): Long = getLong(getColumnIndexOrThrow(columnName))

 fun Cursor.getShort(columnName: String): Short = getShort(getColumnIndexOrThrow(columnName))

 fun Cursor.getString(columnName: String): String =
        getString(getColumnIndexOrThrow(columnName))

 fun Cursor.getBlobOrNull(index: Int) = if (isNull(index)) null else getBlob(index)

 fun Cursor.getDoubleOrNull(index: Int) = if (isNull(index)) null else getDouble(index)

 fun Cursor.getFloatOrNull(index: Int) = if (isNull(index)) null else getFloat(index)

 fun Cursor.getIntOrNull(index: Int) = if (isNull(index)) null else getInt(index)

 fun Cursor.getLongOrNull(index: Int) = if (isNull(index)) null else getLong(index)

 fun Cursor.getShortOrNull(index: Int) = if (isNull(index)) null else getShort(index)

 fun Cursor.getStringOrNull(index: Int) = if (isNull(index)) null else getString(index)

 fun Cursor.getBlobOrNull(columnName: String) =
        getColumnIndexOrThrow(columnName).let { if (isNull(it)) null else getBlob(it) }

 fun Cursor.getDoubleOrNull(columnName: String) =
        getColumnIndexOrThrow(columnName).let { if (isNull(it)) null else getDouble(it) }

 fun Cursor.getFloatOrNull(columnName: String) =
        getColumnIndexOrThrow(columnName).let { if (isNull(it)) null else getFloat(it) }

 fun Cursor.getIntOrNull(columnName: String) =
        getColumnIndexOrThrow(columnName).let { if (isNull(it)) null else getInt(it) }

 fun Cursor.getLongOrNull(columnName: String) =
        getColumnIndexOrThrow(columnName).let { if (isNull(it)) null else getLong(it) }

 fun Cursor.getShortOrNull(columnName: String) =
        getColumnIndexOrThrow(columnName).let { if (isNull(it)) null else getShort(it) }

 fun Cursor.getStringOrNull(columnName: String) =
        getColumnIndexOrThrow(columnName).let { if (isNull(it)) null else getString(it) }