package com.example.capstoneproject.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.capstoneproject.Entity.Stock.CompanyInfo;
import com.example.capstoneproject.Entity.Stock.FinancialInfo;
import com.example.capstoneproject.Entity.Stock.Stock;

@Database(entities = {Stock.class, CompanyInfo.class, FinancialInfo.class}, version = 2, exportSchema = false)
public abstract class StockDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "stockInfo.db";
    private static StockDatabase mStockDatabase;

    // Singleton
    public static StockDatabase getInstance(Context context) {
        if (mStockDatabase == null) {
            mStockDatabase = Room.databaseBuilder(context, StockDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
            mStockDatabase.addStarterData();
        }

        return mStockDatabase;
    }

    // Access to DAO to modify database
    public abstract StockDAO stockDAO();

    // Database starter data
    private void addStarterData(){
        if (stockDAO().getStockMasterList().size() == 0){
            runInTransaction(() -> {
                Stock stock = new Stock("AAPL");
                stockDAO().insertNewStock(stock);

                int id = stockDAO().getMostRecentId();

                CompanyInfo companyInfo = new CompanyInfo(id, "AAPL", "Apple Inc.", "Technology", 154000,
                        "14089961010", "1 Apple Park Way", "Cupertino", "CALIFORNIA", "95014",
                        "1980-12-12", "https://financialmodelingprep.com/image-stock/AAPL.png", "Nasdaq Global Select",
                        "Consumer Electronics", "http://www.apple.com");
                stockDAO().insertCompanyInfo(companyInfo);

                FinancialInfo financialInfo = new FinancialInfo(id, "AAPL", 173.07000000, 0.51106620,
                        0.88000490, 171.09000000, 173.78000000, 116.21000000, 182.94000000,
                        2826994712576L, 80324997, 171.34000000, 30.85026700);
                stockDAO().insertFinancialInfo(financialInfo);

            });
        }
    }
}
