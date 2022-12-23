package com.content.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import org.json.JSONException;

import com.getcapacitor.JSObject;
import com.getcapacitor.JSArray;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "ContentProviderHandler")
public class ContentProviderHandlerPlugin extends Plugin {

  private ContentProviderHandler implementation = new ContentProviderHandler();

  Uri uri = Uri.parse("content://com.content.provider.DataProvider/users");

  @PluginMethod
    public void echo(PluginCall call) {
      String value = call.getString("value");

      JSObject ret = new JSObject();
      ret.put("value", implementation.echo(value));
      call.resolve(ret);
  }

  @PluginMethod()
  public void test(PluginCall call) {
    String value = call.getString("value");

    JSObject ret = new JSObject();
    ret.put("value", value);
    notifyListeners("changeTestFunction",ret);
    call.resolve(ret);
  }

  @PluginMethod()
  public void getValues(PluginCall call) {
    JSArray arr = new JSArray();
    JSObject arrContent = new JSObject();
    Cursor cursor = getContext().getContentResolver().query( uri, null, null, null, null );

    String[] columns = cursor.getColumnNames();

    if( cursor.moveToFirst() ){
       while ( !cursor.isAfterLast() ){
         JSObject obj = new JSObject();
         obj.put( columns[0], cursor.getString(0) );
         obj.put( columns[1], cursor.getString( 1 ) );
         arr.put( obj );
         cursor.moveToNext();
       }
    }

    arrContent.put( "result", arr );
    call.resolve( arrContent );
  }

  @PluginMethod()
  public void insertValue(@NonNull PluginCall call) throws JSONException {
    JSObject res = new JSObject();
    JSObject obj = call.getData();
    ContentValues contentValues = new ContentValues();

    if (obj.has("name")) {
      contentValues.put("name", obj.getString("name"));

      Uri _uri = getContext().getContentResolver().insert(uri, contentValues);

      res.put("result", _uri != null);
      call.resolve(res);
      // notifyListeners("onInsertValue", res );
    }
  }

  @PluginMethod()
  public void deleteValue(@NonNull PluginCall call) throws JSONException {
    JSObject res = new JSObject();
    String id = call.getString("id");
    String[] args = new String[]{ id };

    int rowID = getContext().getContentResolver().delete( uri, "id=?", args );

    res.put("result", rowID );
    call.resolve(res);
    notifyListeners("onDeleteValue", res );
  }

  @PluginMethod()
  public void updateValue(@NonNull PluginCall call) throws JSONException {
    JSObject res = new JSObject();
    ContentValues values = new ContentValues();
    String id = call.getString("id");
    JSObject data = call.getData();
    String[] args = new String[]{ id };

    if ( data.has("name") ){
      values.put( "name", data.getString("name") );
      int rowID = getContext().getContentResolver().update( uri, values, "id=?", args );
      res.put("result", rowID );
    }
    call.resolve(res);
    notifyListeners("onUpdateValue", res );
  }
}
