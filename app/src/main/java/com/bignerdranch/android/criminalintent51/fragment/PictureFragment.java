package com.bignerdranch.android.criminalintent51.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.bignerdranch.android.criminalintent51.R;
import com.project.myutilslibrary.pictureloader.PictureLoader;

/**
 * Created by zhangH on 2016/5/27.
 */
public class PictureFragment extends DialogFragment {

    private static final String ARG_PIC_PATH = "picPath";

    public static PictureFragment newInstance(String picPath){
        Bundle args = new Bundle();
        args.putString(ARG_PIC_PATH,picPath);

        PictureFragment pictureFragment = new PictureFragment();
        pictureFragment.setArguments(args);
        return pictureFragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getContext(),R.layout.dialog_scene_picture,null);
        ImageView picture = (ImageView) view.findViewById(R.id.dialog_picture_image_view);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        String picPath = getArguments().getString(ARG_PIC_PATH);
        PictureLoader.newInstance(getActivity()).setImageViewPictureWithCache(picPath,picture);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).create();
    }
}
