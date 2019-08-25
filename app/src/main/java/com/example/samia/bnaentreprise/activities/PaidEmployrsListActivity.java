package com.example.samia.bnaentreprise.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samia.bnaentreprise.R;
import com.example.samia.bnaentreprise.adapters.PaidSalaryRecyclerAdapter;
import com.example.samia.bnaentreprise.commons.ImageUtil;
import com.example.samia.bnaentreprise.commons.entities.SalaryResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;



public class PaidEmployrsListActivity extends AppCompatActivity {

    private List<SalaryResponseEntity> data;
    private RecyclerView recyclerView;
    private PaidSalaryRecyclerAdapter adapter;
    private ImageView printIv;
    private String path;
    private Bitmap b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result_list);

        //init
        //hidding action bar
        getSupportActionBar().hide();

        //setting title and hidding loading shimmer
        findViewById(R.id.shimmer).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.list_title_tv)).setText("Résultats");
        printIv = findViewById(R.id.print_iv);
        printIv.setVisibility(View.VISIBLE);
        //getting data list
        data = (List<SalaryResponseEntity>) getIntent().getSerializableExtra("data");


        //init recycler view
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaidSalaryRecyclerAdapter(data);
        recyclerView.setAdapter(adapter);



        printIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(PaidEmployrsListActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(PaidEmployrsListActivity.this);
                }
                builder.setTitle(getString(R.string.print))
                        .setMessage(getString(R.string.ask_to_print))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, int which) {
                                dialog.dismiss();
                               print();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void print() {
        findViewById(R.id.title_bar).setVisibility(View.GONE);
        takeScreenShot();
        findViewById(R.id.title_bar).setVisibility(View.VISIBLE);

        Toast.makeText(this, "finished", Toast.LENGTH_SHORT).show();
        //progressDialog.show();
    }

    private void takeScreenShot() {

        findViewById(R.id.title_bar).setVisibility(View.GONE);
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Signature/");

        if (!folder.exists()) {
            boolean success = folder.mkdir();
        }


         path ="/sdcard/" +  System.currentTimeMillis() + ".bmp";// path where pdf will be stored

        View u = findViewById(R.id.scroll);
        NestedScrollView z = (NestedScrollView) findViewById(R.id.scroll); // parent view
        int totalHeight = z.getChildAt(0).getHeight();// parent view height
        int totalWidth = z.getChildAt(0).getWidth();// parent view width

        //Save bitmap to  below path
        String extr = Environment.getExternalStorageDirectory() + "/Signature/";
        File file = new File(extr);
        if (!file.exists())
            file.mkdir();
        String fileName = "test" + ".jpg";
        File myPath = new File(extr, fileName);
        String imagesUri = myPath.getPath();
        FileOutputStream fos = null;
        b = new ImageUtil().getBitmapFromView(u);

        try {
            fos = new FileOutputStream(path);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        findViewById(R.id.title_bar).setVisibility(View.VISIBLE);
        createPdf(b);// create pdf after creating bitmap and saving

    }


 private void createPdf(Bitmap b) {


        path = "/sdcard/" +  System.currentTimeMillis() +".pdf";

        int pageHeight = 4000;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(b.getWidth(), pageHeight, 1).create();

        int x = 0;
        int i = 0;

        while(i < b.getHeight()/pageHeight) {
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#ffffff"));
            canvas.drawPaint(paint);
            Bitmap bitmap = Bitmap.createBitmap(b, 0, x, b.getWidth(), pageHeight);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            document.finishPage(page);
            x+=pageHeight;
            i++;
        }

        if (x < b.getHeight()){
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#ffffff"));
            canvas.drawPaint(paint);
            Bitmap bitmap = Bitmap.createBitmap(b, 0, x, b.getWidth(), b.getHeight() - x);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            document.finishPage(page);
        }

        File filePath = new File(path);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

     PrintManager printManager=(PrintManager) getSystemService(Context.PRINT_SERVICE);
     try {
         PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(this,filePath.getAbsolutePath() );
         printManager.print("Document", printAdapter,new PrintAttributes.Builder().build());
     }
    catch (Exception e) {
    }

     // close the document
        document.close();

        //openPdf(path);// You can open pdf after complete
    }

    public void actionFinish(View v){
        finish();
    }
}


class PdfDocumentAdapter extends PrintDocumentAdapter {

    Context context = null;
    String pathName = "";
    public PdfDocumentAdapter(Context ctxt, String pathName) {
        context = ctxt;
        this.pathName = pathName;
    }
    @Override
    public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes1, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
        if (cancellationSignal.isCanceled()) {
            layoutResultCallback.onLayoutCancelled();
        }
        else {
            PrintDocumentInfo.Builder builder=
                    new PrintDocumentInfo.Builder(" file name");
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build();
            layoutResultCallback.onLayoutFinished(builder.build(),
                    !printAttributes1.equals(printAttributes));
        }
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
        InputStream in=null;
        OutputStream out=null;
        try {
            File file = new File(pathName);
            in = new FileInputStream(file);
            out=new FileOutputStream(parcelFileDescriptor.getFileDescriptor());

            byte[] buf=new byte[16384];
            int size;


            while ((size=in.read(buf)) >= 0
                    && !cancellationSignal.isCanceled()) {
                out.write(buf, 0, size);
            }

            if (cancellationSignal.isCanceled()) {
                writeResultCallback.onWriteCancelled();
            }
            else {
                writeResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
            }
        }
        catch (Exception e) {
            writeResultCallback.onWriteFailed(e.getMessage());
        }
        finally {
            try {
                in.close();
                out.close();
            }
            catch (IOException e) {
            }
        }
    }
}