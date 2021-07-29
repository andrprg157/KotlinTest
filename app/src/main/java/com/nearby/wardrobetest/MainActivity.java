package com.nearby.wardrobetest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nearby.wardrobetest.databinding.ActivityMainBinding;
import com.nearby.wardrobetest.db.Bottomwear;
import com.nearby.wardrobetest.db.Topwear;
import com.nearby.wardrobetest.db.Wishlist;
import com.nearby.wardrobetest.vm.ApparelViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ApparelViewModel viewModel;
    private static final int PERMISSION_REQUEST_CODE = 200;
    int flag;
    TopSwipeAdapter swipeAdapter;
    BottomSwipeAdapter swipeAdapter2;
    File mPhotoFile = null;
    private List<Topwear> topwearList;
    private List<Bottomwear> bottomwearList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        init();

        binding.shuffleImg.setOnClickListener(view -> {
            Collections.shuffle(topwearList);
            Collections.shuffle(bottomwearList);
            swipeAdapter = new TopSwipeAdapter(getApplicationContext(),topwearList);
            binding.topPager.setAdapter(swipeAdapter);
            swipeAdapter2 = new BottomSwipeAdapter(getApplicationContext(),bottomwearList);
            binding.bottomPager.setAdapter(swipeAdapter2);
        });

        binding.wishImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topwearList.size() != 0 && bottomwearList.size() != 0) {
                    int topPos = binding.topPager.getCurrentItem();
                    int bottomPos = binding.bottomPager.getCurrentItem();
                    if (viewModel.getWishlistData(topwearList.get(topPos).getId(),
                    bottomwearList.get(bottomPos).getId()) == null){
                        viewModel.insertWishlist(new Wishlist(0,
                                topwearList.get(topPos).getId(),
                                bottomwearList.get(bottomPos).getId()));
                        binding.wishImg.setImageDrawable(getResources().getDrawable(R.drawable.wish));
                        Toast.makeText(getApplicationContext(),"Item added to wishlist",Toast.LENGTH_SHORT).show();
                    }else {
                        viewModel.deleteWishlist(new Wishlist(0,topwearList.get(topPos).getId(),
                                bottomwearList.get(bottomPos).getId()));
                        binding.wishImg.setImageDrawable(getResources().getDrawable(R.drawable.notwish));
                        Toast.makeText(getApplicationContext(),"Item removed from wishlist",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.topPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("pageSelect",position+" ");
                if (bottomwearList.size() != 0){
                    int bottomPos = binding.bottomPager.getCurrentItem();
                    if (viewModel.getWishlistData(topwearList.get(position).getId(),
                            bottomwearList.get(bottomPos).getId()) == null){
                        binding.wishImg.setImageDrawable(getResources().getDrawable(R.drawable.notwish));
                    }else {
                        binding.wishImg.setImageDrawable(getResources().getDrawable(R.drawable.wish));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        binding.bottomPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("pageSelectb",position+" ");
                if (topwearList.size() != 0){
                    int topPos = binding.topPager.getCurrentItem();
                    if (viewModel.getWishlistData(topwearList.get(topPos).getId(),
                            bottomwearList.get(position).getId()) == null){
                        binding.wishImg.setImageDrawable(getResources().getDrawable(R.drawable.notwish));
                    }else {
                        binding.wishImg.setImageDrawable(getResources().getDrawable(R.drawable.wish));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        binding.topImgPager.setOnClickListener(view -> {
            ImageDialog();
            flag = 0;
        });

        binding.bottomImgPager.setOnClickListener(view -> {
            ImageDialog();
            flag = 1;
        });

    }

    private void init(){
        viewModel = new ViewModelProvider(this).get(ApparelViewModel.class);
        topwearList = new ArrayList<>();
        bottomwearList = new ArrayList<>();
        displayViewPagers();
    }

    private void displayViewPagers() {
        viewModel.getAllTopWear().observe(this, topwears -> {
            if (topwears.size() != 0){
                topwearList = topwears;
                swipeAdapter = new TopSwipeAdapter(getApplicationContext(),topwearList);
                binding.topPager.setAdapter(swipeAdapter);
            }
        });

        viewModel.getAllBottomWear().observe(this, bottomwears -> {
            if (bottomwears.size() != 0){
                bottomwearList = bottomwears;
                swipeAdapter2 = new BottomSwipeAdapter(getApplicationContext(),bottomwearList);
                binding.bottomPager.setAdapter(swipeAdapter2);
            }
        });
    }

    private void ImageDialog(){
        if (Utils.checkPermission(this)) {
            final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Select Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = Utils.createImageFile(getApplicationContext());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            if (photoFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                                        BuildConfig.APPLICATION_ID + ".provider",
                                        photoFile);
                                mPhotoFile = photoFile;
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, 0);
                            }
                        }
                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }else {
            Utils.requestPermission(this,PERMISSION_REQUEST_CODE);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = Uri.fromFile(mPhotoFile);
                    if (flag == 0){
                        viewModel.insertTopWear(new Topwear(0,selectedImage.toString()));
                    }else {
                        viewModel.insertBottomWear(new Bottomwear(0,selectedImage.toString()));
                    }
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    if (flag == 0){
                        viewModel.insertTopWear(new Topwear(0,selectedImage.toString()));
                    }else {
                        viewModel.insertBottomWear(new Bottomwear(0,selectedImage.toString()));
                    }
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    ImageDialog();
                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            Utils.showMessageOKCancel(this,"You need to allow access permissions",
                                    (dialog, which) -> {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            Utils.requestPermission(this,PERMISSION_REQUEST_CODE);
                                        }
                                    });
                        }else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            Utils.showMessageOKCancel(this,"You need to allow access permissions",
                                    (dialog, which) -> {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            Utils.requestPermission(this,PERMISSION_REQUEST_CODE);
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

}