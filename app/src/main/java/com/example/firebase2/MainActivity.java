package com.example.firebase2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.OAuthRequirements;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.BreakIterator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private FirebaseFirestore objectFirebaseFirestore;
    private CollectionReference objectCollectionRefernece;
    private static String Collectioncites = "cities";
    private static final String city = "cityname";
    private Dialog objectDialog;
    private TextView ValuesTv;
    private String allData = "";
    private EditText documentET, cityNameET, cityDetailsET;
    private DocumentReference ObjectDocumentReference;   /// to get dta from firebase collection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Step 2: Initialize Firebase & firestore object

        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        /*   objectCollectionRefernece=objectFirebaseFirestore.collection(newcites);*/
        objectDialog = new Dialog(this);

        objectDialog.setContentView(R.layout.please_wait);
        /*
        objectDialog.setCancelable(false);   (Dismis )

         */
        documentET = findViewById(R.id.documentIDET);

        cityNameET = findViewById(R.id.cityNameET);
        cityDetailsET = findViewById(R.id.Details);
        ValuesTv = findViewById(R.id.ValuesTv);

        try {
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            objectCollectionRefernece = objectFirebaseFirestore.collection(Collectioncites);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();


            if (!documentET.getText().toString().isEmpty())


                ObjectDocumentReference = objectFirebaseFirestore.collection(Collectioncites).document(
                        documentET.getText().toString());


        }

    }

    public void addValues(View v) {
        try {
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            objectFirebaseFirestore.collection(Collectioncites).document(documentET.getText().toString()).get()
                    /*
            if (!documentET.getText().toString().isEmpty() && !cityNameET.getText().toString().isEmpty()
                    && !cityDetailsET.getText().toString().isEmpty()) {
                objectDialog.show();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("city_name", cityNameET.getText().toString());
                objectMap.put("city_details", cityDetailsET.getText().toString());
                objectFirebaseFirestore
                        .collection(Collectioncites)
                        .document(documentET.getText().toString()).set(objectMap)
                        */

                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.getResult().exists()) {
                                Toast.makeText(MainActivity.this, "You have already exist", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!documentET.getText().toString().isEmpty() && !cityNameET.getText().toString().isEmpty()
                                        && !cityDetailsET.getText().toString().isEmpty()) {
                                    objectDialog.show();

                                    final Map<String, Object> objectMap = new HashMap<>();
                                    objectMap.put("city_name", cityNameET.getText().toString());
                                    objectMap.put("city_details", cityDetailsET.getText().toString());
                                    objectFirebaseFirestore
                                            .collection(Collectioncites)
                                            .document(documentET.getText().toString()).set(objectMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    objectDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "Data succesfully", Toast.LENGTH_SHORT).show();
                                                }
                                            })

                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    objectDialog.dismiss();
                                                    Toast.makeText(MainActivity.this, "Fail data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(MainActivity.this, " enter valid details", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

        } catch (Exception e) {

            Toast.makeText(this, "Add Values" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }



    public void updatevaluefirebase(View v) {
        try {
            objectDialog.show();
            if (!documentET.getText().toString().isEmpty()) {

                ObjectDocumentReference = objectFirebaseFirestore.collection(Collectioncites).document(
                        documentET.getText().toString()
                );
                Map<String,Object> ObjectMap=new HashMap<>();
                ObjectMap.put("cityName",cityNameET.getText().toString());

                ObjectDocumentReference.update(ObjectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void avoid) {
                                objectDialog.dismiss();
                                                Toast.makeText(MainActivity.this, "Field is update", Toast.LENGTH_SHORT).show();
                                }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this," update faild"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

            } else {
                Toast.makeText(this, "please provide document", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            objectDialog.dismiss();
            Toast.makeText(this, "updateValues:" , Toast.LENGTH_SHORT).show();
        }
    }



    public void deldoc(View view) {
        try {
            objectDialog.show();
            if (!documentET.getText().toString().isEmpty()) {

                ObjectDocumentReference = objectFirebaseFirestore.collection(Collectioncites).document(
                        documentET.getText().toString()
                );
           /* Map<String, Object> ObjectMap = new HashMap<>();
            ObjectMap.put("cityName", FieldValue.delete());
*/
                ObjectDocumentReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void avoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Field is delete", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, " Fail to delete" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "please provide a document id to delete ", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
    public  void deldocument(View view)
    {
        try
        {
            objectDialog.show();
            if (!documentET.getText().toString().isEmpty()) {

                ObjectDocumentReference = objectFirebaseFirestore.collection(Collectioncites).document(
                        documentET.getText().toString()
                );
                ObjectDocumentReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this,"Document is deleted ",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Document Fail",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(this,"please provide a docuent id to delete",Toast.LENGTH_SHORT).show();
            }
            }
        catch (Exception e)
        {
            Toast.makeText(this,"Delete"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

     */
    public void showdata(View v) {

        try {
            objectDialog.show();
            objectCollectionRefernece.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            objectDialog.dismiss();
                            ValuesTv.setText("");
                            documentET.setText("");
                            for (DocumentSnapshot objectDocumentReference : queryDocumentSnapshots) {
                                String documentET = objectDocumentReference.getId();
                                String cityNameET = objectDocumentReference.getString("cityNameET");
                                String cityDetail = objectDocumentReference.getString("cityDetailsET");
                                allData += "documentET : " + documentET + '\n' + "cityNameEt : " + cityNameET + '\n' + "cityDetail : " + cityDetail;
                            }
                            ValuesTv.setText(allData);
                            Toast.makeText(MainActivity.this, "Retrieve Data Succcessf", Toast.LENGTH_LONG).show();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    objectDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Fails to retrieve data:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void delvalue(View view) {
        try {
            objectDialog.show();
            if (!documentET.getText().toString().isEmpty()) {

                ObjectDocumentReference = objectFirebaseFirestore.collection(Collectioncites).document(
                        documentET.getText().toString()
                );
                Map<String, Object> ObjectMap = new HashMap<>();
                ObjectMap.put("cityName",FieldValue.delete());

                ObjectDocumentReference.update(ObjectMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void avoid) {
                                objectDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Field is Delete", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, " Fails to delete field " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else
            {
                Toast.makeText(this,"please provide a documnent id is delete ",Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}


