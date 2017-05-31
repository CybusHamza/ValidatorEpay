package com.epay.validator.validator_epay.localDatabase;

/**
 * Created by Rizwan Butt on 13-Apr-17.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "SIGNUP";
    public static final String ROUTES = "ROUTES";
    public static final String FARE = "FARE";
    public static final String CUSTOMER_ACCOUNTS = "CUSTOMER_ACCOUNTS";
    public static final String HISTORY_TRAVEL = "HISTORY_TRAVEL";
    public static final String TRANSACTIONS_TABLE = "TRANSACTIONS";

    // Table columns SIGNUP.
    public static final String _ID = "customer_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String GENDER = "gender";
    public static final String CARDTYPE = "cardtype";

    // TABLE COLUMNS ROUTES//
    public static final String ID = "id";
    public static final String ROUTE_CODE = "route_code";
    public static final String ROUTE_NAME = "route_name";
    public static final String ROUTE_START = "route_start";
    public static final String ROUTE_DESTINATION = "route_destination";
    public static final String ROUTE_ADDED_DATE = "route_added_date";
    public static final String TIME = "time";
    public static final String ROUTE_ADDED_BY = "route_added_by";
    public static final String ROUTE_UPDATED_DATE = "route_updated_date";
    public static final String ROUTE_UPDATED_BY = "route_updated_by";

    // TABLE COLUMNS fare//
    public static final String FARE_ID = "Fare_ID";
    public static final String FARE_ROUTE = "Fare_route";
    public static final String FARE_PRICE = "Fare_price";
    public static final String FARE_TYPE = "Fare_type";
    public static final String ADDED_BY = "added_by";
    public static final String UPDATED_BY = "update_by";
    public static final String DATE_ADDED = "date_added";
    public static final String DATE_UPDATED = "date_updated";

    // TABLE COLUMNS Customer Accounts//
    public static final String C_ID = "id";
    public static final String C_CUSTOMER_ID = "customer_id";
    public static final String C_CUSTOMER_BALANCE = "customer_balance";

    //TABLE COLUMNS HISTORY TRAVEL//
    public static final String H_ID = "id";
    public static final String H_ROUTE_ID = "route_id";
    public static final String H_USER_ID = "user_id";
    public static final String H_TRANS_ID = "trans_id";
    public static final String H_PERSON_TRAVELING = "person_travling";
    public static final String H_DATE_ADDED = "date_added";
    public static final String H_DATE_MODIFIED = "date_modified";

    // TABLE COLUMNS TRANSACTIONS //
    public static final String T_ID = "id";
    public static final String T_CUSTOMER_ID = "customer_id";
    public static final String T_FARE_TYPE_ID = "fare_type_id";
    public static final String T_ROUTE_ID = "route_id";
    public static final String T_BUS_TYPE_ID = "bus_type_id";
    public static final String T_TERMINAL_ID = "terminal_id";
    public static final String T_OPERATOR_ID = "operator_id";
    public static final String T_AMOUNT_PAID = "amount_paid";
    public static final String T_CURRENCY = "currency";
    public static final String T_TRANS_STATUS_ID = "trans_status_id";
    public static final String T_TRANS_ID = "trans_id";
    public static final String T_PAID_DATE = "paid_date";
    public static final String T_TRANS_DATE = "trans_date";
    public static final String T_CANCEL_DATE = "cancel_date";




    // Database Information
    static final String DB_NAME = "VALIDATOR_EPAY.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY, " + FIRST_NAME + " TEXT, " + LAST_NAME + " TEXT," + PASSWORD + " TEXT," + PHONE_NUMBER + " TEXT," +GENDER + " TEXT,"+ EMAIL + " TEXT,"+ CARDTYPE + " TEXT);";

    private static final String CREATE_TABLE_ROUTES = "create table " + ROUTES + "(" + ID
            + " INTEGER PRIMARY KEY, " + ROUTE_CODE + " TEXT, " + ROUTE_NAME + " TEXT," + ROUTE_START + " TEXT," + ROUTE_DESTINATION + " TEXT,"+ ROUTE_ADDED_DATE + " TEXT," + TIME + " TEXT," + ROUTE_ADDED_BY + " TEXT," + ROUTE_UPDATED_DATE + " TEXT," + ROUTE_UPDATED_BY + " TEXT);";

    private static final String CREATE_TABLE_FARE = "create table " + FARE + "(" + FARE_ID
            + " INTEGER PRIMARY KEY, " + FARE_ROUTE + " TEXT, " + FARE_PRICE + " TEXT," + FARE_TYPE + " TEXT," + ADDED_BY + " TEXT,"+ UPDATED_BY + " TEXT," + DATE_ADDED + " TEXT," + DATE_UPDATED + " TEXT);";


    private static final String CREATE_TABLE_CUSTOMER_ACCOUNTS = "create table " + CUSTOMER_ACCOUNTS + "(" + C_ID
            + " INTEGER PRIMARY KEY, " + C_CUSTOMER_ID + " TEXT, " + C_CUSTOMER_BALANCE + " TEXT);";


    private static final String CREATE_TABLE_HISTORY_TRAVEL = "create table " + HISTORY_TRAVEL + "(" + H_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + H_ROUTE_ID + " TEXT, " + H_USER_ID + " TEXT," + H_TRANS_ID + " TEXT," + H_PERSON_TRAVELING + " TEXT," + H_DATE_ADDED + " TEXT,"+ H_DATE_MODIFIED + " TEXT);";


    private static final String CREATE_TABLE_TRANSACTIONS = "create table " + TRANSACTIONS_TABLE + "(" + T_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + T_CUSTOMER_ID + " TEXT, " + T_FARE_TYPE_ID + " TEXT," + T_ROUTE_ID + " TEXT," + T_BUS_TYPE_ID + " TEXT,"+  T_OPERATOR_ID + " TEXT,"+ T_TERMINAL_ID + " TEXT,"+T_AMOUNT_PAID + " TEXT,"+ T_CURRENCY + " TEXT,"
            + T_TRANS_STATUS_ID + " TEXT,"+ T_TRANS_ID + " TEXT,"+ T_PAID_DATE + " TEXT,"+ T_TRANS_DATE + " TEXT,"+ T_CANCEL_DATE + " TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_ROUTES);
        db.execSQL(CREATE_TABLE_FARE);
        db.execSQL(CREATE_TABLE_CUSTOMER_ACCOUNTS);
        db.execSQL(CREATE_TABLE_HISTORY_TRAVEL);
        db.execSQL(CREATE_TABLE_TRANSACTIONS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ROUTES);
        db.execSQL("DROP TABLE IF EXISTS " + FARE);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TRAVEL);
        db.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS_TABLE);
        onCreate(db);
    }
}
