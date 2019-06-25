package com.smartdev.fishfarm;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.smartdev.fishfarm.Model.DataModel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    BarChart DataChart;
    TextView viewLabel;
    ArrayList<DataModel> Data;
    ArrayList<String> weekLabels;
    ArrayList<String> dayLabels;
    ArrayList<String> monthLabels;

    ArrayList<BarEntry> PHEntry;
    ArrayList<BarEntry> DOXEntry;
    ArrayList<BarEntry> TempEntry;
    ArrayList<BarEntry> SalinityEntry;
    BarDataSet PHBaraDataSet, OXBaraDataSet, TempBaraDataSet, SalinityBaraDataSet;
    BarData farmData;
    float barSpace = 0.02f;
    float groupSpace = 0.3f;
    int groupCount = 5;
    BarEntry barEntry;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewLabel = findViewById(R.id.ViewType);
        DataChart = findViewById(R.id.DataChart);
        DataChart.setTouchEnabled(true);
        DataChart.setPinchZoom(true);
        dayLabels = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if(i<10){
                dayLabels.add("0"+i + ":00hr");
            }else{
                dayLabels.add(i + ":00hr");
            }

        }
        weekLabels = new ArrayList<>();
        weekLabels.add("Mon");
        weekLabels.add("Tue");
        weekLabels.add("Wed");
        weekLabels.add("Thur");
        weekLabels.add("Fri");
        weekLabels.add("Sat");
        weekLabels.add("Sun");
        monthLabels = new ArrayList<>();
        monthLabels.add("Jan");
        monthLabels.add("Feb");
        monthLabels.add("March");
        monthLabels.add("April");
        monthLabels.add("May");
        monthLabels.add("June");
        monthLabels.add("July");
        monthLabels.add("Aug");
        monthLabels.add("Sep");
        monthLabels.add("Oct");
        monthLabels.add("Nov");
        monthLabels.add("Dec");
        Data = new ArrayList<>();
        DataModel senseData = new DataModel();
        senseData.setDate("2019/06/11, 05:30");
        senseData.setDissolvedOXdata("23.55");
        senseData.setPHData("21.55");
        senseData.setTempData("33.55");
        senseData.setSalinitydata("12.55");
        Data.add(senseData);
        senseData = new DataModel();
        senseData.setDate("2019/06/12, 05:30");
        senseData.setDissolvedOXdata("20.50");
        senseData.setPHData("21.05");
        senseData.setTempData("13.82");
        senseData.setSalinitydata("22.58");
        Data.add(senseData);
        senseData = new DataModel();
        senseData.setDate("2019/06/13, 01:30");
        senseData.setDissolvedOXdata("11.03");
        senseData.setPHData("14.2");
        senseData.setTempData("21.82");
        senseData.setSalinitydata("24.08");
        Data.add(senseData);
        senseData = new DataModel();
        senseData.setDate("2019/06/14, 12:30");
        senseData.setDissolvedOXdata("22.00");
        senseData.setPHData("19.21");
        senseData.setTempData("15.02");
        senseData.setSalinitydata("12.38");
        Data.add(senseData);
        senseData = new DataModel();
        senseData.setDate("2019/06/15, 07:30");
        senseData.setDissolvedOXdata("22.50");
        senseData.setPHData("31.07");
        senseData.setTempData("43.32");
        senseData.setSalinitydata("02.78");
        Data.add(senseData);
        ChartView(Data, dayLabels);

    }

    void ChartView(ArrayList<DataModel> ChartData,  ArrayList<String> labels) {
        int count=0;
        PHEntry = new ArrayList<>();
        TempEntry = new ArrayList<>();
        DOXEntry = new ArrayList<>();
        SalinityEntry = new ArrayList<>();
        for(DataModel dModel: ChartData){
            barEntry = new BarEntry(count, Float.valueOf(dModel.getPHData()));
            PHEntry.add(barEntry);
            barEntry = new BarEntry(count, Float.valueOf(dModel.getTempData())); // Mar
            TempEntry.add(barEntry);
            barEntry = new BarEntry(count, Float.valueOf(dModel.getDissolvedOXdata())); // Mar
            DOXEntry.add(barEntry);
            barEntry = new BarEntry(count, Float.valueOf(dModel.getSalinitydata())); // Mar
            SalinityEntry.add(barEntry);
            count++;
        }
        DataChart.getAxisRight().setEnabled(false);
        YAxis leftAxis = DataChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        PHBaraDataSet = new BarDataSet(PHEntry, "PH");
        PHBaraDataSet.setColor(Color.RED);
        OXBaraDataSet = new BarDataSet(DOXEntry, "Dissolve Oxy");
        OXBaraDataSet.setColor(Color.BLUE);
        TempBaraDataSet = new BarDataSet(TempEntry, "Temperature");
        TempBaraDataSet.setColor(Color.GREEN);
        SalinityBaraDataSet = new BarDataSet(SalinityEntry, "Salinity");
        SalinityBaraDataSet.setColor(Color.YELLOW);
        farmData = new BarData(PHBaraDataSet, OXBaraDataSet, TempBaraDataSet, SalinityBaraDataSet);
        farmData.setValueFormatter(new DefaultValueFormatter(2));
        XAxis xAxis = DataChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        DataChart.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        //IMPORTANT *****
        DataChart.setData(farmData);
        DataChart.setVisibleXRangeMaximum(20); // allow 20 values to be displayed at once on the x-axis, not more
        DataChart.moveViewToX(10);
        farmData.setBarWidth(0.15f);
        DataChart.getXAxis().setAxisMinimum(0);
        DataChart.getXAxis().setAxisMaximum(0 + DataChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        DataChart.groupBars(0, groupSpace, barSpace);
        DataChart.invalidate(); // refresh
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_export) {
            if (checkPermission()) {
                requestPermissionAndContinue();
            }
            else{
                saveExcelFile(this,"FarmDataExport.xls",Data);
            }

            return true;
        } else if (id == R.id.action_day) {
            viewLabel.setText("This day data");
            ChartView(Data,dayLabels);
            return true;
        } else if (id == R.id.action_week) {
            viewLabel.setText("This week data");
            ChartView(Data,weekLabels);
            return true;
        } else if (id == R.id.action_month) {
            viewLabel.setText("This month data");
            ChartView(Data,monthLabels);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean saveExcelFile(Context context, String fileName,ArrayList<DataModel>FarmData) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("FISHFARM", "Storage not available or read only");
            return false;
        }

        // check if available and not read only

        boolean success = false;
        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("IOT DATA");

        // Generate column headings
        Row row = sheet1.createRow(0);


        c = row.createCell(0);
        c.setCellValue("Date/Time");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("PH");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Temperature");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Dissolve Oxygen");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Salinity");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));
        sheet1.setColumnWidth(4, (15 * 500));
        int rowNum = 1;
        for (DataModel data : FarmData) {
            Row rows = sheet1.createRow(rowNum++);
            rows.createCell(0).setCellValue(data.getDate());
            rows.createCell(1).setCellValue(data.getPHData());
            rows.createCell(2).setCellValue(data.getTempData());
            rows.createCell(3).setCellValue(data.getDissolvedOXdata());
            rows.createCell(4).setCellValue(data.getSalinitydata());
        }

        // Create a path where we will place our List of objects on external storage
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Toast.makeText(context, "File Saved in Phone storage Folder ", Toast.LENGTH_LONG).show();
            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return success;
    }
    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(("Permission request"));
                alertBuilder.setMessage("Permission is required for storage ");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    saveExcelFile(this,"FarmDataExport.xls",Data);
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
