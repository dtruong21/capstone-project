package dalker.cmtruong.com.app.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.helper.GlideApp;
import dalker.cmtruong.com.app.helper.PreferencesHelper;
import dalker.cmtruong.com.app.model.Dob;
import dalker.cmtruong.com.app.model.Location;
import dalker.cmtruong.com.app.model.Login;
import dalker.cmtruong.com.app.model.Name;
import dalker.cmtruong.com.app.model.Picture;
import dalker.cmtruong.com.app.model.User;
import dalker.cmtruong.com.app.view.activity.MainActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 22th
 */
public class EditProfileFragment extends Fragment implements OnItemSelectedListener {

    private static final int REQUEST_PICTURE_CAPTURE = 1;
    @BindView(R.id.avatar_iv)
    CircleImageView circleImageView;

    @BindView(R.id.title_sp)
    Spinner gender;

    @BindView(R.id.first_name_et)
    EditText firstName;

    @BindView(R.id.last_name_et)
    EditText lastName;

    @BindView(R.id.email_et)
    EditText email;

    @BindView(R.id.age_et)
    EditText age;

    @BindView(R.id.login_et)
    EditText login;

    @BindView(R.id.pass_et)
    EditText password;

    @BindView(R.id.phone_et)
    EditText phone;

    @BindView(R.id.address_et)
    EditText address;

    @BindView(R.id.cp_et)
    EditText postCode;

    @BindView(R.id.city_et)
    EditText city;

    @BindView(R.id.state_et)
    EditText state;

    @BindView(R.id.description_et)
    EditText description;

    @BindView(R.id.price_et)
    EditText price;

    @BindView(R.id.doggo_et)
    EditText doggo;

    @BindView(R.id.bt_cancel)
    Button cancel;

    @BindView(R.id.bt_update)
    Button update;

    private String data;
    private String genderValue;
    private User user;
    private String pictureFilePath;
    private FirebaseStorage mStorage;
    private StorageReference mRef;
    private String deviceIdentifier;
    private String cloudPath;
    private FirebaseFirestore fb;

    private DatabaseReference mDb;

    @BindView(R.id.edit_layout)
    LinearLayout mLayout;

    @BindView(R.id.edit_pb)
    ProgressBar mProgressBar;

    boolean imageChanged = false;

    public EditProfileFragment() {
    }

    public static EditProfileFragment getInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setRetainInstance(true);
        ButterKnife.bind(this, view);
        if (savedInstanceState == null) {
            connectFirebase();
            loadData();
            getAvatar();
            getInstallationIdentifier();
        } else {
            display();
            user = savedInstanceState.getParcelable(getString(R.string.user_key));
            populateUI(user);
        }
        cancel();
        update();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.user_key), user);
    }

    private void connectFirebase() {
        fb = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        mRef = mStorage.getReference();
        mDb = FirebaseDatabase.getInstance().getReference(getString(R.string.users));
    }

    private void loading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLayout.setVisibility(View.GONE);
    }

    private void display() {
        mProgressBar.setVisibility(View.GONE);
        mLayout.setVisibility(View.VISIBLE);
    }

    private void loadData() {
        data = PreferencesHelper.getDocumentReference(getContext());
        //    DocumentReference documentReference = fb.collection(getString(R.string.users)).document(data);
        loading();
//        documentReference.get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot documentSnapshot = task.getResult();
//                        if (documentSnapshot.exists()) {
//                            Gson gson = new Gson();
//                            JsonElement jsonElement = gson.toJsonTree(documentSnapshot.getData());
//                            user = gson.fromJson(jsonElement, User.class);
//                            populateUI(user);
//                        }
//                    }
//                });

        mDb.orderByChild("idUser").equalTo(data).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot mData : dataSnapshot.getChildren()) {
                        user = mData.getValue(User.class);
                        if (user != null)
                            populateUI(user);
                        else
                            Timber.d("Error by loading user data");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.d("Error by requesting user data");
            }
        });
        display();
    }

    private void populateUI(User user) {
        if (user.getPictureURL() != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(user.getPictureURL().getLarge());
            GlideApp.with(getContext())
                    .load(storageReference)
                    .error(R.drawable.ic_photo_camera_black_24dp)
                    .placeholder(R.drawable.ic_photo_camera_black_24dp)
                    .into(circleImageView);
        }

        if (user.getName() != null) {
            if (user.getName().getTitle().equals(getString(R.string.title_mr)))
                gender.setSelection(1);
            else
                gender.setSelection(2);
            firstName.setText(user.getName().getFirstName());
            lastName.setText(user.getName().getLastName());
        }
        email.setText(user.getEmail());
        login.setText(user.getLogin().getUsername());
        login.setEnabled(false);
        password.setText(user.getLogin().getPassword());
        if (user.getPhone() != null) {
            phone.setText(user.getPhone());
        }

        if (user.getDob() != null) {
            age.setText(String.valueOf(user.getDob().getAge()));
        }
        if (user.getLocation() != null) {
            address.setText(user.getLocation().getStreet());
            postCode.setText(String.valueOf(user.getLocation().getPostCode()));
            city.setText(user.getLocation().getCity());
            state.setText(user.getLocation().getState());
        }


        doggo.setText(String.valueOf(user.getDogNumber()));

        if (user.getDescription() != null) {
            description.setText(user.getDescription());
        }
        price.setText(String.valueOf(user.getPrice()));
    }

    private String convertToJson(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    private void cancel() {
        cancel.setOnClickListener(v -> backToProfile());
    }

    private void update() {
        update.setOnClickListener(v ->
                sendData(user)
        );
    }

    private void sendData(User user) {
        if (imageChanged) {
            Picture picture = new Picture(cloudPath, pictureFilePath, pictureFilePath);
            user.setPictureURL(picture);
        }

        Name name = new Name(gender.getSelectedItem().toString(), firstName.getText().toString(), lastName.getText().toString());
        user.setName(name);
        user.setEmail(email.getText().toString());
        Dob ageValue = new Dob(0);
        if (!age.getText().toString().equals("")) {
            ageValue.setAge(Integer.parseInt(age.getText().toString()));
        }
        user.setDob(ageValue);
        user.setPhone(phone.getText().toString());
        if (!postCode.getText().toString().equals("")) {
            Location mLocation = new Location(address.getText().toString(),
                    city.getText().toString(), state.getText().toString(), Integer.parseInt(postCode.getText().toString()));
            user.setLocation(mLocation);
        }
        if (!description.getText().toString().equals("")) {
            user.setDescription(description.getText().toString());
        }


        if (!price.getText().toString().equals("")) {
            user.setPrice(Float.parseFloat(price.getText().toString()));
        }
        if (!doggo.getText().toString().equals(""))
            user.setDogNumber(Integer.parseInt(doggo.getText().toString()));
        String referenceID = PreferencesHelper.getDocumentReference(getContext());
        DocumentReference documentReference = fb.collection(getString(R.string.users)).document(referenceID);

        String json = convertToJson(user);
        Map<String, Object> userFF = new Gson().fromJson(
                json, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        loading();

        mDb.child(referenceID)
                .setValue(user)
                .addOnCompleteListener(task -> Snackbar.make(getView(), R.string.update_finish, Snackbar.LENGTH_LONG).show())
                .addOnFailureListener(e -> Snackbar.make(getView(), R.string.update_ko, Snackbar.LENGTH_LONG).show());
//        documentReference.update(userFF)
//                .addOnSuccessListener(aVoid -> Snackbar.make(getView(), R.string.update_finish, Snackbar.LENGTH_LONG).show())
//                .addOnFailureListener(e -> Snackbar.make(getView(), R.string.update_ko, Snackbar.LENGTH_LONG).show());
        display();
        PreferencesHelper.saveUserSession(getContext(), user.toString());
        addToCloudStorage();
    }

    private void getAvatar() {
        circleImageView.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
                File pictureFile;
                try {
                    pictureFile = getPicturePath();
                } catch (IOException e) {
                    Timber.e(getString(R.string.photo_error), e.getMessage());
                    return;
                }

                if (pictureFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), getString(R.string.provider), pictureFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
                }
                addToGallery();
                cloudPath = getCloudPath();
                imageChanged = true;
            }
        });
    }

    private void addToGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pictureFilePath);
        Uri pictureUri = Uri.fromFile(f);
        galleryIntent.setData(pictureUri);
        Objects.requireNonNull(getActivity()).sendBroadcast(galleryIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pictureFilePath);
            if (imgFile.exists()) {
                GlideApp.with(getContext())
                        .load(Uri.fromFile(imgFile))
                        .error(R.drawable.ic_photo_camera_black_24dp)
                        .placeholder(R.drawable.ic_photo_camera_black_24dp)
                        .into(circleImageView);
            }

        }
    }

    private File getPicturePath() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "_" + timeStamp;
        File storage = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, getString(R.string.jpg_suffix), storage);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    private void backToProfile() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(getString(R.string.fragment), R.id.navigation_profile);
        startActivity(intent);
    }

    private String getCloudPath() {
        File file = new File(pictureFilePath);
        Uri uri = Uri.fromFile(file);
        return uri.getLastPathSegment();
    }


    private void addToCloudStorage() {
        if (pictureFilePath != null) {
            File file = new File(pictureFilePath);
            Uri uri = Uri.fromFile(file);
            StorageReference uploadRef = mRef.child(cloudPath);
            uploadRef.putFile(uri).addOnFailureListener(e -> {
                Timber.d("Failed to upload picture to cloud storage %s", e.getMessage());
            }).addOnSuccessListener(taskSnapshot -> {
                Timber.d("Picture uploaded");
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        genderValue = parent.getItemAtPosition(position).toString();
        Timber.d(genderValue);
    }

    private void getInstallationIdentifier() {
        if (deviceIdentifier == null) {
            deviceIdentifier = UUID.randomUUID().toString();
            Timber.d("device: %s", deviceIdentifier);
            PreferencesHelper.saveDevideID(getContext(), deviceIdentifier);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
