package com.example.restaurantserver.services;

import com.example.restaurantserver.configurations.GoogleAuthorizationConfig;
import com.example.restaurantserver.models.BookingData;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleSheetsServiceImpl implements GoogleSheetsService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleSheetsServiceImpl.class);

    @Value("${spreadsheet.id}")
    private String spreadsheetId;


    private final GoogleAuthorizationConfig googleAuthorizationConfig;

    public GoogleSheetsServiceImpl(GoogleAuthorizationConfig googleAuthorizationConfig) {
        this.googleAuthorizationConfig = googleAuthorizationConfig;
    }

    @Override
    public void insertRow(ValueRange body, String range) throws GeneralSecurityException, IOException {
        Sheets sheetsService = googleAuthorizationConfig.getSheetsService();
        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    @Override
    public void deleteRow(String range) throws GeneralSecurityException, IOException {
        ClearValuesRequest requestBody = new ClearValuesRequest();
        Sheets sheetsService =  googleAuthorizationConfig.getSheetsService();
        Sheets.Spreadsheets.Values.Clear request =
                sheetsService.spreadsheets().values().clear(spreadsheetId, range, requestBody);
        ClearValuesResponse response = request.execute();
    }


    public ValueRange requestBuilder(BookingData bookingData, String range) {
        return new ValueRange()
                .setValues(Arrays.asList(
                        List.of(bookingData.getAccountOwnerEmail()),
                        List.of(bookingData.getBookingId()),
                        List.of(bookingData.getDate()),
                        List.of(bookingData.getName()),
                        List.of(bookingData.getEmail()),
                        List.of(bookingData.getPhoneNumber()),
                        List.of(bookingData.getGuestNumber()),
                        List.of(bookingData.getAdultNumber()),
                        List.of(bookingData.getChildrenNumber()),
                        List.of(bookingData.getSession())
                ))
                .setMajorDimension("COLUMNS")
                .setRange(range);
    }

    @Override
    public void getSpreadsheetValues() throws IOException, GeneralSecurityException {
        Sheets sheetsService = googleAuthorizationConfig.getSheetsService();
        Sheets.Spreadsheets.Values.BatchGet request =
                sheetsService.spreadsheets().values().batchGet(spreadsheetId);
        request.setRanges(getSpreadSheetRange());
        request.setMajorDimension("ROWS");
        BatchGetValuesResponse response = request.execute();
        List<List<Object>> spreadSheetValues = response.getValueRanges().get(0).getValues();
        List<Object> headers = spreadSheetValues.remove(0);
        for ( List<Object> row : spreadSheetValues ) {
            logger.info("{}: {}, {}: {}, {}: {}, {}: {}",
                    headers.get(0),row.get(0), headers.get(1),row.get(1),
                    headers.get(2),row.get(2), headers.get(3),row.get(3));
        }
    }


    private List<String> getSpreadSheetRange() throws IOException, GeneralSecurityException {
        Sheets sheetsService = googleAuthorizationConfig.getSheetsService();
        Sheets.Spreadsheets.Get request = sheetsService.spreadsheets().get(spreadsheetId);
        Spreadsheet spreadsheet = request.execute();
        Sheet sheet = spreadsheet.getSheets().get(0);
        int row = sheet.getProperties().getGridProperties().getRowCount();
        int col = sheet.getProperties().getGridProperties().getColumnCount();
        return Collections.singletonList("R1C1:R".concat(String.valueOf(row))
                .concat("C").concat(String.valueOf(col)));
    }
}