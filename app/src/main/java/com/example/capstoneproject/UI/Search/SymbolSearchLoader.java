package com.example.capstoneproject.UI.Search;

import com.example.capstoneproject.Entity.Stock.StockSearchItem;

import java.util.List;

public interface SymbolSearchLoader {
    void onSymbolSearchLoaded(List<StockSearchItem> searchItem);
}
