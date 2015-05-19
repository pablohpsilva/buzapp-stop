package aloeio.buzapp.app.Utils.Constants;

/**
 * Created by pablohenrique on 3/18/15.
 */
public class SQLConstants {
    public final static int DATABASE_VERSION = 1;
    public final static String DATABASE = "BuzAppDatabase.db";
    public final static String ROUTE_TABLE = "route";
    public final static String ROUTE_LINE_TABLE = "route_lines";


    public final static String ROUTE_COLUMN_ID = "id";
    public final static String ROUTE_COLUMN_BUS = "cars";
    public final static String ROUTE_COLUMN_STOPS_QUANTITY = "stopsize";
    public final static String ROUTE_COLUMN_STOPS = "stops";
    public final static String[] ROUTE_COLUMN_LIST = {ROUTE_COLUMN_ID, ROUTE_COLUMN_BUS, ROUTE_COLUMN_STOPS_QUANTITY, ROUTE_COLUMN_STOPS};

    public final static String CREATE_ROUTE_TABLE = "CREATE TABLE IF NOT EXISTS " + ROUTE_TABLE + "( " + ROUTE_COLUMN_ID + " TEXT PRIMARY KEY, " + ROUTE_COLUMN_BUS + " INTEGER, " + ROUTE_COLUMN_STOPS_QUANTITY + " INTEGER, " + ROUTE_COLUMN_STOPS + " BLOB);";
    public final static String DROP_ROUTE_TABLE = "DROP TABLE IF EXISTS " + ROUTE_TABLE + ";";

    public final static String SELECT_ROUTE_BY_ID = "SELECT * FROM " + ROUTE_TABLE + " WHERE " + ROUTE_COLUMN_ID + " = '|||';";
    public final static String SELECT_ALL_ROUTES = "SELECT * FROM " + ROUTE_TABLE + ";";
    public final static String SELECT_ROUTE_LIKE_BY_ID = "SELECT * FROM " + ROUTE_TABLE + " WHERE " + ROUTE_COLUMN_ID + " LIKE '%|||%';";

    public final static String DELETE_ROUTE_BY_ID = "DELETE FROM " + ROUTE_TABLE + " WHERE " + ROUTE_COLUMN_ID + " = '|||';";


    //SCHEDULE TABLE STRINGS
    public final static String SCHEDULE_TABLE = "schedule";
    public final static String SCHEDULE_COLUMN_ID = "id";
    public final static String SCHEDULE_COLUMN_WAY = "way";
    public final static String SCHEDULE_COLUMN_TYPE = "type";
    public final static String SCHEDULE_COLUMN_CAR = "car";
    public final static String SCHEDULE_COLUMN_HOUR= "hour";
    public final static String SCHEDULE_COLUMN_MINUTE= "minute";
    public final static String SCHEDULE_COLUMN_DESCRIPTION= "description";

    public final static String CREATE_SCHEDULE_TABLE = "CREATE TABLE IF NOT EXISTS " + SCHEDULE_TABLE + "( " + SCHEDULE_COLUMN_ID+ " TEXT , " + SCHEDULE_COLUMN_WAY + " TEXT, " + SCHEDULE_COLUMN_TYPE + " TEXT, "+ SCHEDULE_COLUMN_CAR + " INTEGER,"+ SCHEDULE_COLUMN_HOUR + " INTEGER,"+ SCHEDULE_COLUMN_MINUTE + " INTEGER,"+SCHEDULE_COLUMN_DESCRIPTION+" TEXT );";

    public final static String DROP_SCHEDULE_TABLE = "DROP TABLE IF EXISTS " + SCHEDULE_TABLE + ";";


    public final static String SELECT_SCHEDULE_BY_ID = "SELECT * FROM " + SCHEDULE_TABLE + " WHERE " + SCHEDULE_COLUMN_ID + " = '|||' AND "+SCHEDULE_COLUMN_WAY+" = ':::' AND (("+SCHEDULE_COLUMN_HOUR+" >= HMIN AND "+SCHEDULE_COLUMN_HOUR+"<= CURHOUR )OR (" +SCHEDULE_COLUMN_HOUR+" <= HMAX AND "+SCHEDULE_COLUMN_HOUR+" > CURHOUR )) ORDER BY "+SCHEDULE_COLUMN_HOUR+" ASC;";


    public final static String SELECT_SCHEDULE_BY_DESCRIPTION = "SELECT * FROM " + SCHEDULE_TABLE + " WHERE " + SCHEDULE_COLUMN_DESCRIPTION + " LIKE '%|||%' AND "+SCHEDULE_COLUMN_WAY+" = ':::' AND (("+SCHEDULE_COLUMN_HOUR+" >= HMIN AND "+SCHEDULE_COLUMN_HOUR+"<= CURHOUR )OR (" +SCHEDULE_COLUMN_HOUR+" <= HMAX AND "+SCHEDULE_COLUMN_HOUR+" > CURHOUR )) ORDER BY "+SCHEDULE_COLUMN_HOUR+" ASC;";

    public final static int HOUR_DISTANCE = 4;

    public final static String GET_SCHEDULE_ROWS_QTT = "SELECT Count(*) FROM "+SCHEDULE_TABLE;


    public final static String ROUTE_IDS_TABLE="ROUTE_IDS";

    public final static String CREATE_ROUTE_IDS_TABLE = "CREATE TABLE IF NOT EXISTS " + ROUTE_IDS_TABLE + "( " + ROUTE_COLUMN_ID + " TEXT PRIMARY KEY);";

    public final static String DROP_ROUTE_IDS_TABLE = "DROP TABLE IF EXISTS " + ROUTE_IDS_TABLE + ";";

    public final static String SELECT_ALL_ROUTE_IDS = "SELECT * FROM "+ ROUTE_IDS_TABLE;

}
