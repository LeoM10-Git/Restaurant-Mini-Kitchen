package com.example.restaurantserver.services;

import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleSheetsService {
    void getSpreadsheetValues() throws IOException, GeneralSecurityException;
    void insertRow(ValueRange body, String range) throws GeneralSecurityException, IOException;
    void deleteRow(String range) throws GeneralSecurityException, IOException;
}