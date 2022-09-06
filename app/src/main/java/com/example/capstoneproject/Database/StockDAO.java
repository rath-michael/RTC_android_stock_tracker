package com.example.capstoneproject.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.capstoneproject.Entity.Stock.CompanyInfo;
import com.example.capstoneproject.Entity.Stock.FinancialInfo;
import com.example.capstoneproject.Entity.Stock.PriceQuickView;
import com.example.capstoneproject.Entity.Stock.Stock;

import java.util.List;

@Dao
public interface StockDAO {
    // return list of all stock
    @Query("SELECT * FROM StockMaster")
    List<Stock> getStockMasterList();

    // check if stock exists by symbol
    @Query("SELECT * FROM StockMaster WHERE symbol = :symbol")
    boolean stockExists(String symbol);

    // return stock by symbol
    @Query("SELECT * FROM StockMaster WHERE symbol = :symbol")
    Stock getStockBySymbol(String symbol);

    // return financial info by symbol
    @Query("SELECT * FROM FinancialInfo WHERE symbol = :symbol")
    FinancialInfo getFinancialInfoBySymbol(String symbol);

    // return company info by symbol
    @Query("SELECT * FROM CompanyInfo WHERE symbol = :symbol")
    CompanyInfo getCompanyInfoBySymbol(String symbol);

    // return list of all financial info
    @Query("SELECT symbol, price, changePercentage, change FROM FinancialInfo")
    List<PriceQuickView> getCurrentFinancialInfo();

    // return list of all current symbols
    @Query("SELECT symbol FROM StockMaster")
    List<String> getSymbolList();

    // insert basic new stock
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNewStock(Stock stock);

    // return id of most recently added stock
    @Query("SELECT id FROM StockMaster ORDER BY id DESC LIMIT 1")
    int getMostRecentId();

    // insert new stock company info
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCompanyInfo(CompanyInfo companyInfo);

    // insert new stock financial info
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFinancialInfo(FinancialInfo financialInfo);

    // update stock by symbol and price
    @Query("UPDATE FinancialInfo SET price = :price, changePercentage = :changePercentage, change = :change, " +
            "dayLow = :dayLow, dayHigh = :dayHigh, open = :open WHERE symbol LIKE :symbol AND price != :price")
    void updateFinancialInfo(String symbol, double price, double changePercentage, double change,
                             double dayLow, double dayHigh, double open);

    // monitor changes in financial info table for live data list update
    @Query("SELECT symbol, price, changePercentage, change FROM FinancialInfo")
    LiveData<List<PriceQuickView>> monitorChanges();

    // delete stock from database
    @Query("DELETE FROM StockMaster WHERE ID like :id")
    void deleteStock(int id);
}
