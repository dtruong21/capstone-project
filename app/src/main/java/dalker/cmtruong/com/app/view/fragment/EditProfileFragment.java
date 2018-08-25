package dalker.cmtruong.com.app.view.fragment;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
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
    private FirebaseFirestoreSettings settings;


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
        connectFirebase();
        loadData();
        getAvatar();
        cancel();
        update();
        getInstallationIdentifier();
        return view;
    }

    private void connectFirebase() {
        fb = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fb.setFirestoreSettings(settings);
        mStorage = FirebaseStorage.getInstance();
        mRef = mStorage.getReference();
    }

    private void loadData() {
        data = PreferencesHelper.getDocumentReference(getContext());
        DocumentReference documentReference = fb.collection(getString(R.string.users)).document(data);
        documentReference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Gson gson = new Gson();
                            JsonElement jsonElement = gson.toJsonTree(documentSnapshot.getData());
                            user = gson.fromJson(jsonElement, User.class);
                            Timber.d("data%s", user.toString());


                            if (user.getPictureURL() != null) {
                                if (user.getPictureURL().getLarge() != null) {
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(user.getPictureURL().getLarge());
                                    GlideApp.with(getContext()).load(storageReference).error(R.drawable.ic_photo_camera_black_24dp).placeholder(R.drawable.ic_photo_camera_black_24dp).into(circleImageView);
                                }
                            }

                            if (user.getName() != null) {
                                if (user.getName().getTitle().equals("Mr"))
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

                            if (user.getLocation() != null) {
                                address.setText(user.getLocation().getStreet());
                                postCode.setText(user.getLocation().getPostCode());
                                city.setText(user.getLocation().getCity());
                                state.setText(user.getLocation().getState());
                            }

                            if (user.getDescription() != null) {
                                description.setText(user.getDescription());
                            }
                            price.setText(String.valueOf(user.getPrice()));
                        }
                    }
                });
    }

    private String convertToJson(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    private void cancel() {
        cancel.setOnClickListener(v -> backToProfile());
    }

    private void update() {
        update.setOnClickListener(v -> {
            Picture picture = new Picture(cloudPath, pictureFilePath, pictureFilePath);
            user.setPictureURL(picture);
            Name name = new Name(gender.getSelectedItem().toString(), firstName.getText().toString(), lastName.getText().toString());
            user.setName(name);
            user.setEmail(email.getText().toString());
            Dob ageValue = new Dob(0);
            if (!age.getText().toString().equals("")) {
                ageValue.setAge(Integer.parseInt(age.getText().toString()));
            }
            user.setDob(ageValue);
            Login account = new Login(login.getText().toString(), password.getText().toString());
            user.setLogin(account);
            user.setPhone(phone.getText().toString());
            if (!postCode.getText().toString().equals("")) {
                Location mLocation = new Location(address.getText().toString(), city.getText().toString(), state.getText().toString(), Integer.parseInt(postCode.getText().toString()));
                user.setLocation(mLocation);
            }
            user.setDescription(description.getText().toString());
            if (!price.getText().toString().equals("")) {

            }

            Timber.d("New user: %s", user.toString());
            fb.setFirestoreSettings(settings);

            String referenceID = PreferencesHelper.getDocumentReference(getContext());
            Timber.d("ref: %s", referenceID);
            DocumentReference documentReference = fb.collection(getString(R.string.users)).document(referenceID);

            String json = convertToJson(user);
            Map<String, Object> userFF = new Gson().fromJson(
                    json, new TypeToken<HashMap<String, Object>>() {
                    }.getType()
            );
            documentReference.update(userFF)
                    .addOnSuccessListener(aVoid -> Snackbar.make(getView(), "Update finished", Snackbar.LENGTH_LONG).show())
                    .addOnFailureListener(e -> Snackbar.make(getView(), "Update data KO", Snackbar.LENGTH_LONG).show());
            PreferencesHelper.saveUserSession(getContext(), user.toString());
            addToCloudStorage();
        });
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
                    Timber.e("Photo can't be create: %s", e.getMessage());
                    return;
                }

                if (pictureFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), "dalker.cmtruong.com.app.fileprovider", pictureFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
                }
                addToGallery();
                cloudPath = getCloudPath();
            }

        });


    }

    private void addToGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pictureFilePath);
        Uri pictureUri = Uri.fromFile(f);
        galleryIntent.setData(pictureUri);
        Timber.d("Path: %s", pictureUri.toString());
        Objects.requireNonNull(getActivity()).sendBroadcast(galleryIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pictureFilePath);
            if (imgFile.exists()) {
                GlideApp.with(getContext()).load(Uri.fromFile(imgFile)).error(R.drawable.ic_photo_camera_black_24dp).placeholder(R.drawable.ic_photo_camera_black_24dp).into(circleImageView);
            }

        }
    }

    private File getPicturePath() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "_" + timeStamp;
        File storage = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storage);
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
        File file = new File(pictureFilePath);
        Uri uri = Uri.fromFile(file);
        StorageReference uploadRef = mRef.child(cloudPath);
        uploadRef.putFile(uri).addOnFailureListener(e -> {
            Timber.d("Failed to upload picture to cloud storage %s", e.getMessage());
            Snackbar.make(getView(), "Failed to upload picture to cloud", Snackbar.LENGTH_LONG).show();
        }).addOnSuccessListener(taskSnapshot -> {
            Timber.d("Picture uploaded");
            Snackbar.make(getView(), "Picture uploaded to cloud", Snackbar.LENGTH_LONG).show();
        });
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
