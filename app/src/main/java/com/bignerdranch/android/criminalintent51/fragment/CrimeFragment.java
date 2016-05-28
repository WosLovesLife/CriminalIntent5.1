package com.bignerdranch.android.criminalintent51.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bignerdranch.android.criminalintent51.R;
import com.bignerdranch.android.criminalintent51.bean.Crime;
import com.bignerdranch.android.criminalintent51.single.CrimeLab;
import com.project.myutilslibrary.pictureloader.PictureLoader;
import com.project.myutilslibrary.view.MarqueeButton;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by zhangH on 2016/5/24.
 */
public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    private static final String DIALOG_PICTURE = "DialogPicture";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_CONTACT_PHONE = 3;
    private static final int REQUEST_TACK_PICTURE = 4;

    private Crime mCrime;
    private EditText mTitleFiled;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mCrimeTimeButton;
    private Button mReportButton;
    private MarqueeButton mSuspectButton;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;
    private File mPhotoFile;


    private Callbacks mCallbacks;

    public interface Callbacks {

        /** 实时同步明细页和列表页的的Crime对象状态 */
        void onCrimeUpdated(Crime crime);

        /** 当页面需要被卸载时触发(例如用户点击了Toolbar上的删除menuItem) */
        void onCrimeRemove(Fragment fragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleFiled = (EditText) view.findViewById(R.id.crime_title);
        mTitleFiled.setText(mCrime.getTitle());
        mTitleFiled.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mDateButton = (Button) view.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });

        mCrimeTimeButton = (Button) view.findViewById(R.id.crime_time);
        mCrimeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                FragmentManager fm = getFragmentManager();
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timePickerFragment.show(fm, DIALOG_TIME);
            }
        });

        mReportButton = (Button) view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 使用一般方式启动一个ChooserIntent
                /*Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, R.string.crime_report_subject);
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);*/

                // 使用IntentBuilder启动一个ChooserIntent
                ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(getActivity());
                Intent chooserIntent = builder.setType("text/plain").
                        setChooserTitle(R.string.send_report).
                        setText(getCrimeReport()).
                        setSubject(getString(R.string.crime_report_subject)).
                        createChooserIntent();
                startActivity(chooserIntent);
            }
        });

        final Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (MarqueeButton) view.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT);
            }
        });
        mSuspectButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Button removeSuspectButton = new Button(getActivity());
                removeSuspectButton.setText("Remove the suspect");
                removeSuspectButton.setBackgroundResource(android.R.color.transparent);
                final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(removeSuspectButton).create();
                removeSuspectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCrime.setSuspect(null);
                        mSuspectButton.setText(R.string.crime_suspect_text);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
        updateSuspectButtonText();

        /** 如果系统中不包含任何能够启动联系人Intent的应用,则禁用掉启动联系人的Button,防止应用崩溃 */
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setText(R.string.no_contacts_text);
            mSuspectButton.setEnabled(false);
        }

        Button callSuspectButton = (Button) view.findViewById(R.id.crime_call_suspect);
        callSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(pickContactIntent, REQUEST_CONTACT_PHONE);
            }
        });

        mPhotoView = (ImageView) view.findViewById(R.id.crime_photo);
        updatePicture();
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureFragment.newInstance(mPhotoFile.getPath()).show(getFragmentManager(), DIALOG_PICTURE);
            }
        });

        mPhotoButton = (ImageButton) view.findViewById(R.id.crime_camera);
        final Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canCapture = mPhotoFile != null && takePicIntent.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canCapture);

        if (canCapture) {
            mPhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.fromFile(mPhotoFile);
                    takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(takePicIntent, REQUEST_TACK_PICTURE);
                }
            });
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_DATE && data != null) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_TIME && data != null) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri uri = data.getData();
            Log.w(TAG, "onActivityResult: SuspectUri:" + uri);
            String[] queryFiled = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(uri, queryFiled, null, null, null);

            if (cursor == null || cursor.getCount() == 0) {
                return;
            }
            try {
                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                mCrime.setSuspect(suspect);
                updateSuspectButtonText();
            } finally {
                cursor.close();
            }
        } else if (requestCode == REQUEST_CONTACT_PHONE && data != null) {

            String phoneNumber = getSuspectPhoneNumber(data);
            if (phoneNumber != null) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(i);
            } else {
                Toast.makeText(getActivity(), R.string.toast_no_number, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_TACK_PICTURE) {
            /** 如果拍了新的照片,删除内存中的缓存 */
            PictureLoader.newInstance(getActivity()).deleteSpecificCache(mPhotoFile.getPath());
            updatePicture();
        }
    }

    private void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

    @Nullable
    private String getSuspectPhoneNumber(Intent data) {
        String phoneNumber = null;
        ContentResolver resolver = getActivity().getContentResolver();

        Uri uri = data.getData();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        try {
            cursor.moveToFirst();
            int indexId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(indexId);

            Log.w(TAG, "onActivityResult: ContactID: " + contactId);

            Log.w(TAG, "getSuspectPhoneNumber: name:" + cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)));

            Cursor phoneCursor = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);

            if (phoneCursor == null || phoneCursor.getCount() == 0) {
                return null;
            }
            try {
                phoneCursor.moveToFirst();
                int indexNumber = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                phoneNumber = phoneCursor.getString(indexNumber);
                int type = phoneCursor.getType(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                Log.w(TAG, "getSuspectPhoneNumber: type: " + type);

                Log.w(TAG, "getSuspectPhoneNumber: phoneNumber: " + phoneNumber);
            } finally {
                phoneCursor.close();
            }
        } finally {
            cursor.close();
        }

        return phoneNumber;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
//        CrimeLab.get(getActivity()).updateCrime(mCrime);
        updateCrime();
    }

    private void updateUI() {
        mTitleFiled.setSelection(mTitleFiled.getText().length());
    }

    private void updateDate() {
        CharSequence format = "cccc,MMMd,yyyy";
        mDateButton.setText(DateFormat.format(format, mCrime.getDate()).toString());
        updateCrime();
    }

    private void updatePicture() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
            return;
        }

        PictureLoader.newInstance(getActivity()).
                setImageViewThumbnailPictureWithCache(mPhotoFile.getPath(),mPhotoView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                mCallbacks.onCrimeRemove(this);
//                getActivity().finish();
                updateCrime();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCrimeReport() {
        String solved;
        if (mCrime.isSolved()) {
            solved = getString(R.string.crime_report_solved);
        } else {
            solved = getString(R.string.crime_report_unsolved);
        }

        String suspect = mCrime.getSuspect();
        if (suspect != null) {
            suspect = getString(R.string.crime_report_suspect, suspect);
        } else {
            suspect = getString(R.string.crime_report_no_suspect);
        }

        String dateFormat = "EEE,MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        return getString(R.string.crime_report, mCrime.getTitle(), dateString, solved, suspect);
    }

    private void updateSuspectButtonText() {
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText("The suspect is: " + mCrime.getSuspect());
        }
    }
}
