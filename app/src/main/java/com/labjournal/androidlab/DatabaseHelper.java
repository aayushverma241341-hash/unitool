package com.labjournal.androidlab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LabJournalDB";
    private static final int DATABASE_VERSION = 3; // Upgraded for E-Commerce

    // ==========================================
    // EXISTING TABLES (V2)
    // ==========================================
    private static final String TABLE_STUDENTS = "students";
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_GRADES = "grades";
    private static final String TABLE_CONTACTS = "contacts";

    private static final String KEY_ID = "id";

    // STUDENTS Table
    private static final String COL_STUDENT_NAME = "name";
    private static final String COL_STUDENT_ROLL = "roll_no";
    private static final String COL_STUDENT_MARKS = "marks";

    // TASKS Table
    private static final String COL_TASK_TITLE = "title";
    private static final String COL_TASK_COMPLETED = "is_completed"; 

    // GRADES Table
    private static final String COL_GRADE_COURSE = "course_name";
    private static final String COL_GRADE_CREDITS = "credit_hours";
    private static final String COL_GRADE_SCORE = "grade_score"; 

    // CONTACTS Table
    private static final String COL_CONTACT_NAME = "name";
    private static final String COL_CONTACT_PHONE = "phone";
    private static final String COL_CONTACT_EMAIL = "email";

    // ==========================================
    // NEW E-COMMERCE TABLES (V3)
    // ==========================================
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_CART = "cart_items";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDER_ITEMS = "order_items";

    // USERS Table
    private static final String COL_USER_NAME = "name";
    private static final String COL_USER_ADDRESS = "delivery_address";

    // PRODUCTS Table
    private static final String COL_PROD_NAME = "name";
    private static final String COL_PROD_DESC = "description";
    private static final String COL_PROD_PRICE = "price";
    private static final String COL_PROD_CAT = "category";
    private static final String COL_PROD_ICON = "icon"; // Emoji string for simplicity

    // CART Table
    private static final String COL_CART_PROD_ID = "product_id";
    private static final String COL_CART_QTY = "quantity";

    // ORDERS Table
    private static final String COL_ORDER_DATE = "order_date";
    private static final String COL_ORDER_TOTAL = "total_amount";
    private static final String COL_ORDER_STATUS = "status";

    // ORDER ITEMS Table
    private static final String COL_OI_ORDER_ID = "order_id";
    private static final String COL_OI_PROD_ID = "product_id";
    private static final String COL_OI_QTY = "quantity";


    // ==========================================
    // CREATE TABLE STATEMENTS
    // ==========================================
    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_STUDENT_NAME + " TEXT, "
            + COL_STUDENT_ROLL + " TEXT, "
            + COL_STUDENT_MARKS + " REAL)";

    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TASK_TITLE + " TEXT, "
            + COL_TASK_COMPLETED + " INTEGER)";

    private static final String CREATE_TABLE_GRADES = "CREATE TABLE " + TABLE_GRADES + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_GRADE_COURSE + " TEXT, "
            + COL_GRADE_CREDITS + " INTEGER, "
            + COL_GRADE_SCORE + " REAL)";

    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_CONTACT_NAME + " TEXT, "
            + COL_CONTACT_PHONE + " TEXT, "
            + COL_CONTACT_EMAIL + " TEXT)";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_USER_NAME + " TEXT, "
            + COL_USER_ADDRESS + " TEXT)";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_PROD_NAME + " TEXT, "
            + COL_PROD_DESC + " TEXT, "
            + COL_PROD_PRICE + " REAL, "
            + COL_PROD_CAT + " TEXT, "
            + COL_PROD_ICON + " TEXT)";

    private static final String CREATE_TABLE_FAVORITES = "CREATE TABLE " + TABLE_FAVORITES + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_CART_PROD_ID + " INTEGER)";

    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_CART_PROD_ID + " INTEGER, "
            + COL_CART_QTY + " INTEGER)";

    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ORDER_DATE + " TEXT, "
            + COL_ORDER_TOTAL + " REAL, "
            + COL_ORDER_STATUS + " TEXT)";

    private static final String CREATE_TABLE_ORDER_ITEMS = "CREATE TABLE " + TABLE_ORDER_ITEMS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_OI_ORDER_ID + " INTEGER, "
            + COL_OI_PROD_ID + " INTEGER, "
            + COL_OI_QTY + " INTEGER)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // V2 Tables
        db.execSQL(CREATE_TABLE_STUDENTS);
        db.execSQL(CREATE_TABLE_TASKS);
        db.execSQL(CREATE_TABLE_GRADES);
        db.execSQL(CREATE_TABLE_CONTACTS);

        // V3 Tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_FAVORITES);
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_ORDER_ITEMS);

        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        onCreate(db);
    }

    private void seedData(SQLiteDatabase db) {
        // Seed User
        db.execSQL("INSERT INTO users (name, delivery_address) VALUES ('Student', '123 University Ave, Room 402')");

        // Seed Food Products
        db.execSQL("INSERT INTO products (name, description, price, category, icon) VALUES ('Margherita Pizza', 'Classic cheese and tomato pizza.', 12.99, 'food', '🍕')");
        db.execSQL("INSERT INTO products (name, description, price, category, icon) VALUES ('Cheeseburger', 'Double beef patty with cheddar cheese.', 8.99, 'food', '🍔')");
        db.execSQL("INSERT INTO products (name, description, price, category, icon) VALUES ('Spicy Tuna Roll', 'Fresh tuna with spicy mayo.', 14.50, 'food', '🍣')");
        db.execSQL("INSERT INTO products (name, description, price, category, icon) VALUES ('French Fries', 'Crispy golden fries.', 4.99, 'food', '🍟')");
        db.execSQL("INSERT INTO products (name, description, price, category, icon) VALUES ('Caesar Salad', 'Crisp romaine, parmesan, croutons.', 7.99, 'food', '🥗')");
        
        // Seed Grocery Products
        db.execSQL("INSERT INTO products (name, description, price, category, icon) VALUES ('Whole Milk', '1 Gallon, fresh.', 3.99, 'grocery', '🥛')");
        db.execSQL("INSERT INTO products (name, description, price, category, icon) VALUES ('Loaf of Bread', 'Sliced whole wheat bread.', 2.49, 'grocery', '🍞')");
        db.execSQL("INSERT INTO products (name, description, price, category, icon) VALUES ('Dozen Eggs', 'Farm fresh large eggs.', 4.29, 'grocery', '🥚')");
        db.execSQL("INSERT INTO products (name, description, price, category, icon) VALUES ('Fresh Apples', '1 lb of Fuji apples.', 1.99, 'grocery', '🍎')");
        db.execSQL("INSERT INTO products (name, description, price, category, icon) VALUES ('Chicken Breast', '1 lb boneless skinless.', 5.99, 'grocery', '🍗')");
    }

    // ==========================================
    // V2 CRUD Methods (Existing)
    // ==========================================
    public long addStudent(String name, String rollNo, double marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NAME, name);
        values.put(COL_STUDENT_ROLL, rollNo);
        values.put(COL_STUDENT_MARKS, marks);
        return db.insert(TABLE_STUDENTS, null, values);
    }
    public Cursor getAllStudents() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + KEY_ID + " ASC", null);
    }
    public int updateStudent(int id, String name, String rollNo, double marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STUDENT_NAME, name);
        values.put(COL_STUDENT_ROLL, rollNo);
        values.put(COL_STUDENT_MARKS, marks);
        return db.update(TABLE_STUDENTS, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
    public void deleteStudent(int id) {
        this.getWritableDatabase().delete(TABLE_STUDENTS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
    public void deleteAllStudents() {
        this.getWritableDatabase().delete(TABLE_STUDENTS, null, null);
    }
    public Cursor getStudentByRollNo(String rollNo) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_STUDENTS + " WHERE " + COL_STUDENT_ROLL + " = ?", new String[]{rollNo});
    }
    public long addTask(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK_TITLE, title);
        values.put(COL_TASK_COMPLETED, 0); 
        return db.insert(TABLE_TASKS, null, values);
    }
    public Cursor getAllTasks() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_TASKS + " ORDER BY " + KEY_ID + " DESC", null);
    }
    public void toggleTaskStatus(int id, boolean isCompleted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK_COMPLETED, isCompleted ? 1 : 0);
        db.update(TABLE_TASKS, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id) {
        this.getWritableDatabase().delete(TABLE_TASKS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
    public long addGrade(String course, int credits, double score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_GRADE_COURSE, course);
        values.put(COL_GRADE_CREDITS, credits);
        values.put(COL_GRADE_SCORE, score);
        return db.insert(TABLE_GRADES, null, values);
    }
    public Cursor getAllGrades() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_GRADES + " ORDER BY " + KEY_ID + " ASC", null);
    }
    public void deleteGrade(int id) {
        this.getWritableDatabase().delete(TABLE_GRADES, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
    public long addContact(String name, String phone, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CONTACT_NAME, name);
        values.put(COL_CONTACT_PHONE, phone);
        values.put(COL_CONTACT_EMAIL, email);
        return db.insert(TABLE_CONTACTS, null, values);
    }
    public Cursor getAllContacts() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_CONTACTS + " ORDER BY " + COL_CONTACT_NAME + " ASC", null);
    }
    public void deleteContact(int id) {
        this.getWritableDatabase().delete(TABLE_CONTACTS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // ==========================================
    // E-COMMERCE CRUD Methods (V3)
    // ==========================================

    public Cursor getProductsByCategory(String category) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PROD_CAT + " = ?", new String[]{category});
    }

    public Cursor getProductById(int id) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void addToCart(int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Check if item already in cart
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COL_CART_PROD_ID + " = ?", new String[]{String.valueOf(productId)});
        if (c.moveToFirst()) {
            int currentQty = c.getInt(c.getColumnIndexOrThrow(COL_CART_QTY));
            ContentValues values = new ContentValues();
            values.put(COL_CART_QTY, currentQty + quantity);
            db.update(TABLE_CART, values, COL_CART_PROD_ID + " = ?", new String[]{String.valueOf(productId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(COL_CART_PROD_ID, productId);
            values.put(COL_CART_QTY, quantity);
            db.insert(TABLE_CART, null, values);
        }
        c.close();
    }

    public Cursor getCartItems() {
        String query = "SELECT c.id as cart_id, c.quantity, p.* FROM " + TABLE_CART + " c INNER JOIN " + TABLE_PRODUCTS + " p ON c." + COL_CART_PROD_ID + " = p.id";
        return this.getReadableDatabase().rawQuery(query, null);
    }

    public void removeCartItem(int cartId) {
        this.getWritableDatabase().delete(TABLE_CART, KEY_ID + " = ?", new String[]{String.valueOf(cartId)});
    }

    public void clearCart() {
        this.getWritableDatabase().delete(TABLE_CART, null, null);
    }

    public int getCartItemCount() {
        Cursor c = this.getReadableDatabase().rawQuery("SELECT SUM(" + COL_CART_QTY + ") FROM " + TABLE_CART, null);
        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }

    public long placeOrder(double total) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        long orderId = -1;
        try {
            ContentValues orderVals = new ContentValues();
            orderVals.put(COL_ORDER_DATE, String.valueOf(System.currentTimeMillis()));
            orderVals.put(COL_ORDER_TOTAL, total);
            orderVals.put(COL_ORDER_STATUS, "Received");
            orderId = db.insert(TABLE_ORDERS, null, orderVals);

            Cursor cartCursor = getCartItems();
            if (cartCursor.moveToFirst()) {
                do {
                    int prodId = cartCursor.getInt(cartCursor.getColumnIndexOrThrow("id")); // id from product
                    int qty = cartCursor.getInt(cartCursor.getColumnIndexOrThrow("quantity"));
                    ContentValues itemVals = new ContentValues();
                    itemVals.put(COL_OI_ORDER_ID, orderId);
                    itemVals.put(COL_OI_PROD_ID, prodId);
                    itemVals.put(COL_OI_QTY, qty);
                    db.insert(TABLE_ORDER_ITEMS, null, itemVals);
                } while (cartCursor.moveToNext());
            }
            cartCursor.close();

            // Clear cart
            db.delete(TABLE_CART, null, null);
            
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return orderId;
    }

    public Cursor getAllOrders() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_ORDERS + " ORDER BY " + KEY_ID + " DESC", null);
    }
    
    public void updateOrderStatus(int orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ORDER_STATUS, status);
        db.update(TABLE_ORDERS, values, KEY_ID + " = ?", new String[]{String.valueOf(orderId)});
    }
    
    public Cursor getOrderDetails(int orderId) {
        String query = "SELECT oi.quantity, p.* FROM " + TABLE_ORDER_ITEMS + " oi INNER JOIN " + TABLE_PRODUCTS + " p ON oi." + COL_OI_PROD_ID + " = p.id WHERE oi." + COL_OI_ORDER_ID + " = ?";
        return this.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(orderId)});
    }
    
    public String getUserAddress() {
        Cursor c = this.getReadableDatabase().rawQuery("SELECT " + COL_USER_ADDRESS + " FROM " + TABLE_USERS + " LIMIT 1", null);
        String address = "No Address";
        if (c.moveToFirst()) {
            address = c.getString(0);
        }
        c.close();
        return address;
    }
    
    public boolean toggleFavorite(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_FAVORITES + " WHERE " + COL_CART_PROD_ID + " = ?", new String[]{String.valueOf(productId)});
        boolean isNowFav = false;
        if (c.moveToFirst()) {
            db.delete(TABLE_FAVORITES, COL_CART_PROD_ID + " = ?", new String[]{String.valueOf(productId)});
        } else {
            ContentValues values = new ContentValues();
            values.put(COL_CART_PROD_ID, productId);
            db.insert(TABLE_FAVORITES, null, values);
            isNowFav = true;
        }
        c.close();
        return isNowFav;
    }
    
    public boolean isFavorite(int productId) {
        Cursor c = this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_FAVORITES + " WHERE " + COL_CART_PROD_ID + " = ?", new String[]{String.valueOf(productId)});
        boolean isFav = c.moveToFirst();
        c.close();
        return isFav;
    }
    
    public Cursor getFavorites() {
        String query = "SELECT f.id as fav_id, p.* FROM " + TABLE_FAVORITES + " f INNER JOIN " + TABLE_PRODUCTS + " p ON f." + COL_CART_PROD_ID + " = p.id";
        return this.getReadableDatabase().rawQuery(query, null);
    }
}
