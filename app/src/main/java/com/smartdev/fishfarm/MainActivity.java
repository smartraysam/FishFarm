package com.smartdev.fishfarm;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Sampler;
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
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.smartdev.fishfarm.Model.LogModel;
import com.smartdev.fishfarm.util.Constants;
import com.smartdev.fishfarm.util.GsonUtil;
import com.smartdev.fishfarm.util.NetworkClient;
import com.smartdev.fishfarm.util.RequestInterface;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    boolean isExport, isReload = false;
    ArrayList<LogModel> logData = new ArrayList<>();
    String From, Till;
    LinearLayout content;
    private ProgressDialog progress;
    private static final int PERMISSION_REQUEST_CODE = 200;
    TextView txtTemp, txtPh, txtCond, txtOxy, txtTurb;
    Button showGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        content = findViewById(R.id.contentFrame);
        txtTemp = findViewById(R.id.textTemp);
        txtPh = findViewById(R.id.textPh);
        txtCond = findViewById(R.id.textCon);
        txtOxy = findViewById(R.id.textOxy);
        txtTurb = findViewById(R.id.textTur);
        showGraph = findViewById(R.id.buttonGraph);
        showGraph.setOnClickListener(this);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        From = formatter.format(date) + " 00:00:00";
        Till = formatter.format(date) + " 23:59:59";
        getDataLog(From, Till);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                isReload = true;
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                From = formatter.format(date) + " 00:00:00";
                Till = formatter.format(date) + " 23:59:59";
                getDataLog(From, Till);
                Log.d(Constants.TAG, "Reload");
            }
        }, 0, 10, TimeUnit.SECONDS);


    }

    @Override
    public void onClick(View v) {
        if(v == showGraph){
            Intent showgraph = new Intent(MainActivity.this, GraphActivity.class);
            startActivity(showgraph);
        }
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
            progress = new ProgressDialog(this);
            progress.setIndeterminate(true);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage("Exporting Data to Excel...");
            progress.setCancelable(true);
            progress.show();
            logData = new ArrayList<>();
            isExport = true;
            getDataLog("all", "all");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean saveExcelFile(Context context, String fileName, ArrayList<LogModel> FarmData) {
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
        c.setCellValue("Turbidity");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Conductivity");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (15 * 500));
        sheet1.setColumnWidth(2, (15 * 500));
        sheet1.setColumnWidth(3, (15 * 500));
        sheet1.setColumnWidth(4, (15 * 500));
        int rowNum = 1;
        for (LogModel data : FarmData) {
            Row rows = sheet1.createRow(rowNum++);
            rows.createCell(0).setCellValue(data.getEvent());
            rows.createCell(1).setCellValue(data.getPH());
            rows.createCell(2).setCellValue(data.getTemperature());
            rows.createCell(3).setCellValue(data.getDissolveOxy());
            rows.createCell(4).setCellValue(data.getSanility());
        }

        // Create a path where we will place our List of objects on external storage
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            wb.write(os);
            //  Toast.makeText(context, "File Saved in Phone storage Folder ", Toast.LENGTH_LONG).show();
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
                    saveExcelFile(this, "FarmDataExport.xls", logData);
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


    private void getDataLog(String from, String till) {
        if (!isReload) {
            progress = new ProgressDialog(this);
            progress.setIndeterminate(true);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage("Loading Data...");
            progress.setCancelable(true);
            progress.show();
        }
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call call;
        if (from.equals("all") && till.equals("all")) {
            call = requestInterface.getAllLog();
        } else {
            call = requestInterface.getLogWithDate(from, till);
        }
        call.enqueue(new Callback<ArrayList<LogModel>>() {
            @Override
            public void onResponse(Call<ArrayList<LogModel>> call, retrofit2.Response<ArrayList<LogModel>> response) {
                logData = response.body();
                if (logData.size() == 0) {
                    final Snackbar snackBar = Snackbar.make(content, "No data is available.", Snackbar.LENGTH_LONG);
                    snackBar.show();
                    if (progress != null) {
                        progress.dismiss();
                    }
                    return;
                }
                if (progress != null) {
                    progress.dismiss();
                }
                Log.d(Constants.TAG, String.valueOf(logData.size()));
                if (!isExport) {
                    LogModel dModel = logData.get(0);
                    txtTemp.setText(dModel.getTemperature()+"\u2103");
                    txtPh.setText(dModel.getPH());
                    txtOxy.setText(" ");
                    txtTurb.setText(dModel.getDissolveOxy());
                    txtCond.setText(dModel.getSanility());
                } else {
                    if (checkPermission()) {
                        requestPermissionAndContinue();
                    } else {
                        saveExcelFile(MainActivity.this, "FarmDataExport.xls", logData);
                    }
                    final Snackbar snackBar = Snackbar.make(content, "Exporting of Data Done.\n Check your SDCard for the Export file", Snackbar.LENGTH_LONG);
                    snackBar.show();
                    isExport = false;
                }


            }

            @Override
            public void onFailure(Call<ArrayList<LogModel>> call, Throwable t) {
                // handleSignInResult(null);
                if (progress != null) {
                    progress.dismiss();
                }
                Log.d(Constants.TAG, "failed");
                final Snackbar snackBar = Snackbar.make(content, "Error occur.", Snackbar.LENGTH_LONG);
                snackBar.show();

            }
        });
    }
}
