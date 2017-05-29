package localDatabase;

/**
 * Created by Rizwan Butt on 13-Apr-17.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String id,String first_name, String last_name,String password,String phone_number,String gender,String email,String cardType) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper._ID,id);
        contentValue.put(DatabaseHelper.FIRST_NAME, first_name);
        contentValue.put(DatabaseHelper.LAST_NAME, last_name);
        contentValue.put(DatabaseHelper.PASSWORD,password);
        contentValue.put(DatabaseHelper.PHONE_NUMBER, phone_number);
        contentValue.put(DatabaseHelper.GENDER,gender);
        contentValue.put(DatabaseHelper.EMAIL, email);
        contentValue.put(DatabaseHelper.CARDTYPE,cardType);
        long result = database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);

    }
    public void insert_into_routes(String id,String route_code, String route_name,String route_start,String route_destination,String route_added_date,String time,String route_added_by,String route_updated_date,String route_updated_by) {
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.ID, id);
        contentValue.put(DatabaseHelper.ROUTE_CODE, route_code);
        contentValue.put(DatabaseHelper.ROUTE_NAME, route_name);
        contentValue.put(DatabaseHelper.ROUTE_START, route_start);
        contentValue.put(DatabaseHelper.ROUTE_DESTINATION, route_destination);
        contentValue.put(DatabaseHelper.ROUTE_ADDED_DATE, route_added_date);
        contentValue.put(DatabaseHelper.TIME, time);
        contentValue.put(DatabaseHelper.ROUTE_ADDED_BY, route_added_by);
        contentValue.put(DatabaseHelper.ROUTE_UPDATED_DATE, route_updated_date);
        contentValue.put(DatabaseHelper.ROUTE_UPDATED_BY, route_updated_by);
        long result = database.insert(DatabaseHelper.ROUTES, null, contentValue);

    }

    public void insert_into_fare(String fare_id,String fare_route,String fare_price, String fare_type,String added_by,String updated_by,String date_added,String date_updated) {
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.FARE_ID, fare_id);
        contentValue.put(DatabaseHelper.FARE_ROUTE, fare_route);
        contentValue.put(DatabaseHelper.FARE_PRICE, fare_price);
        contentValue.put(DatabaseHelper.FARE_TYPE, fare_type);
        contentValue.put(DatabaseHelper.ADDED_BY, added_by);
        contentValue.put(DatabaseHelper.UPDATED_BY, updated_by);
        contentValue.put(DatabaseHelper.DATE_ADDED, date_added);
        contentValue.put(DatabaseHelper.DATE_UPDATED, date_updated);
        long result = database.insert(DatabaseHelper.FARE, null, contentValue);
    }

    public void insert_into_customer_accounts(String c_id,String c_customer_id,String c_customer_balance) {
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.C_ID, c_id);
        contentValue.put(DatabaseHelper.C_CUSTOMER_ID, c_customer_id);
        contentValue.put(DatabaseHelper.C_CUSTOMER_BALANCE, c_customer_balance);
        long result = database.insert(DatabaseHelper.CUSTOMER_ACCOUNTS, null, contentValue);
    }
    public void insert_into_history_travel(String h_route_id,String h_user_id,String h_person_traveling,String h_date_added,String h_date_modified) {
        ContentValues contentValue = new ContentValues();

       // contentValue.put(DatabaseHelper.H_ID, h_id);
        contentValue.put(DatabaseHelper.H_ROUTE_ID, h_route_id);
        contentValue.put(DatabaseHelper.H_USER_ID, h_user_id);
        contentValue.put(DatabaseHelper.H_PERSON_TRAVELING, h_person_traveling);
        contentValue.put(DatabaseHelper.H_DATE_ADDED, h_date_added);
        contentValue.put(DatabaseHelper.H_DATE_MODIFIED, h_date_modified);
        long result = database.insert(DatabaseHelper.HISTORY_TRAVEL, null, contentValue);
    }

    public void insert_into_transactions(String t_customer_id,String t_fare_type_id,String t_route_id,String t_bus_type_id,String t_amount_paid,String t_currency,String t_trans_status_id,String t_trans_id,String t_paid_date,String t_trans_date,String t_cancel_date) {
        ContentValues contentValue = new ContentValues();

        // contentValue.put(DatabaseHelper.H_ID, h_id);
        contentValue.put(DatabaseHelper.T_CUSTOMER_ID, t_customer_id);
        contentValue.put(DatabaseHelper.T_FARE_TYPE_ID, t_fare_type_id);
        contentValue.put(DatabaseHelper.T_ROUTE_ID, t_route_id);
        contentValue.put(DatabaseHelper.T_BUS_TYPE_ID, t_bus_type_id);
        contentValue.put(DatabaseHelper.T_AMOUNT_PAID, t_amount_paid);
        contentValue.put(DatabaseHelper.T_CURRENCY, t_currency);
        contentValue.put(DatabaseHelper.T_TRANS_STATUS_ID, t_trans_status_id);
        contentValue.put(DatabaseHelper.T_TRANS_ID, t_trans_id);
        contentValue.put(DatabaseHelper.T_PAID_DATE, t_paid_date);
        contentValue.put(DatabaseHelper.T_TRANS_DATE, t_trans_date);
        contentValue.put(DatabaseHelper.T_CANCEL_DATE, t_cancel_date);
        long result = database.insert(DatabaseHelper.TRANSACTIONS_TABLE, null, contentValue);
    }
    public String[] fetch_transaction_table() {
        String[] args={"1"};
        Cursor cursor=database.rawQuery("SELECT customer_id FROM TRANSACTIONS WHERE id = ?",args);
        String[] customer_id = null;
        int i=0;
        if(cursor.moveToFirst()){
            do
            {
                customer_id[i]=cursor.getString(1);
                i++;

            } while (cursor.moveToNext());
        }
        return customer_id;
    }
    public String[] fetch() {

        String[] columns = new String[] {DatabaseHelper.T_CUSTOMER_ID};
        Cursor cursor = database.query(DatabaseHelper.TRANSACTIONS_TABLE, columns, null, null, null, null, null);
        int i=0;
        String[] daata = new String[cursor.getCount()];

        while (cursor.moveToNext()) {
            daata[i]=cursor.getString(0);
            cursor.moveToNext();
            i++;
        }
        return daata;
    }

    public String[] fetch_trans_id() {
        String[] columns = new String[] {DatabaseHelper.T_TRANS_ID};
        Cursor cursor = database.query(DatabaseHelper.TRANSACTIONS_TABLE, columns, null, null, null, null, null);
        int i=0;
        String[] transid = new String[cursor.getCount()];
        while (cursor.moveToNext()) {
            transid[i]=cursor.getString(0);
            cursor.moveToNext();
            i++;
        }
        return transid;
    }

    public String[] fetch_fare() {
        String[] columns = new String[] {DatabaseHelper.T_AMOUNT_PAID};
        Cursor cursor = database.query(DatabaseHelper.TRANSACTIONS_TABLE, columns, null, null, null, null, null);
        int i=0;
        String[] amount = new String[cursor.getCount()];
        while (cursor.moveToNext()) {
            //cursor.moveToFirst();
            amount[i]=cursor.getString(0);
            cursor.moveToNext();
            i++;
        }
        return amount;
    }

    public int update(long _id, String first_name, String last_name,String email,String phone_number) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FIRST_NAME, first_name);
        contentValues.put(DatabaseHelper.LAST_NAME, last_name);
        contentValues.put(DatabaseHelper.EMAIL, email);
        contentValues.put(DatabaseHelper.PHONE_NUMBER, phone_number);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }
    public int update_customer_balance(String customer_id,String customer_remaining_balance) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.C_CUSTOMER_BALANCE, customer_remaining_balance);
        int i = database.update(DatabaseHelper.CUSTOMER_ACCOUNTS, contentValues, DatabaseHelper.C_CUSTOMER_ID + " = " + customer_id, null);
        return i;
    }
    public int update_history_travel(String route_id, String user_id,String person_traveling) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.H_ROUTE_ID, route_id);
        contentValues.put(DatabaseHelper.H_PERSON_TRAVELING, person_traveling);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.H_USER_ID + " = " + user_id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}