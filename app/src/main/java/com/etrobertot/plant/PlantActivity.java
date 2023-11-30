package com.etrobertot.plant;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PlantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);

        FirebaseApp.initializeApp(PlantActivity.this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FloatingActionButton add = findViewById(R.id.addPlant);


        //////// ingresar los datos //////////////

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(PlantActivity.this).inflate(R.layout.add_note_dialog, null);

                /*//////////////////////////////////////////
                TextInputLayout textInputLayout = findViewById(R.id.inputLayout);
                MaterialAutoCompleteTextView autoCompleteTextView = findViewById(R.id.inputTV);
                /////////////////////////////////////////*/

                TextInputLayout nombreLayout, tipoLayout, especieLayout, cuidadoLayout, estadoLayout;
                nombreLayout = view1.findViewById(R.id.nombreLayout);
                tipoLayout = view1.findViewById(R.id.tipoLayout);
                especieLayout = view1.findViewById(R.id.especieLayout);
                cuidadoLayout = view1.findViewById(R.id.cuidadoLayout);
                estadoLayout = view1.findViewById(R.id.estadoLayout);
                TextInputEditText nombreET, tipoET, especieET, cuidadoET, estadoET;
                nombreET = view1.findViewById(R.id.nombreET);
                tipoET = view1.findViewById(R.id.tipoET);
                especieET = view1.findViewById(R.id.especieET);
                cuidadoET = view1.findViewById(R.id.cuidadoET);
                estadoET = view1.findViewById(R.id.estadoET);
                AlertDialog alertDialog = new AlertDialog.Builder(PlantActivity.this)
                        .setTitle("Add")
                        .setView(view1)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Objects.requireNonNull(nombreET.getText()).toString().isEmpty()) {
                                    nombreLayout.setError("This field is required!");
                                } else if (Objects.requireNonNull(tipoET.getText()).toString().isEmpty()) {
                                    tipoLayout.setError("This field is required!");
                                }else if (Objects.requireNonNull(especieET.getText()).toString().isEmpty()) {
                                    especieLayout.setError("This field is required!");
                                }else if (Objects.requireNonNull(cuidadoET.getText()).toString().isEmpty()) {
                                    cuidadoLayout.setError("This field is required!");
                                }else if (Objects.requireNonNull(estadoET.getText()).toString().isEmpty()) {
                                    estadoLayout.setError("This field is required!");
                                }
                                /*///////////////////////////////////////////////
                                else if (Objects.requireNonNull(autoCompleteTextView.getText()).toString().isEmpty()) {
                                    textInputLayout.setError("Select an option");
                                }*/
                                //////////////////////////////////////////////
                                else {
                                    ProgressDialog dialog = new ProgressDialog(PlantActivity.this);
                                    dialog.setMessage("Storing in Database...");
                                    dialog.show();
                                    Plant plant = new Plant();
                                    plant.setNombre(nombreET.getText().toString());
                                    plant.setTipo(tipoET.getText().toString());
                                    plant.setEspecie(especieET.getText().toString());
                                    plant.setCuidado(cuidadoET.getText().toString());
                                    plant.setEstado(estadoET.getText().toString());
                                    database.getReference().child("plants").push().setValue(plant).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialog.dismiss();
                                            dialogInterface.dismiss();
                                            Toast.makeText(PlantActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(PlantActivity.this, "There was an error while saving data", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });

        ///// cargar los datos ////
        TextView empty = findViewById(R.id.empty);

        RecyclerView recyclerView = findViewById(R.id.recycler);

        database.getReference().child("plants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Plant> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Plant plant = dataSnapshot.getValue(Plant.class);
                    Objects.requireNonNull(plant).setKey(dataSnapshot.getKey());
                    arrayList.add(plant);
                }

                if (arrayList.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                PlantAdapter adapter = new PlantAdapter(PlantActivity.this, arrayList);
                recyclerView.setAdapter(adapter);



                adapter.setOnItemClickListener(new PlantAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Plant plant) {
                        View view = LayoutInflater.from(PlantActivity.this).inflate(R.layout.add_note_dialog, null);
                        TextInputLayout nombreLayout, tipoLayout, especieLayout, cuidadoLayout, estadoLayout;
                        TextInputEditText nombreET, tipoET, especieET, cuidadoET, estadoET ;

                        /*//////////////////////////////////////////
                        TextInputLayout textInputLayout = findViewById(R.id.inputLayout);
                        MaterialAutoCompleteTextView autoCompleteTextView = findViewById(R.id.inputTV);
                        /////////////////////////////////////////*/

                        nombreET = view.findViewById(R.id.nombreET);
                        tipoET = view.findViewById(R.id.tipoET);
                        especieET = view.findViewById(R.id.especieET);
                        cuidadoET = view.findViewById(R.id.cuidadoET);
                        estadoET = view.findViewById(R.id.estadoET);
                        nombreLayout = view.findViewById(R.id.nombreLayout);
                        tipoLayout = view.findViewById(R.id.tipoLayout);
                        especieLayout = view.findViewById(R.id.especieLayout);
                        cuidadoLayout = view.findViewById(R.id.cuidadoLayout);
                        estadoLayout = view.findViewById(R.id.estadoLayout);


                        nombreET.setText(plant.getNombre());
                        tipoET.setText(plant.getTipo());
                        especieET.setText(plant.getEspecie());
                        cuidadoET.setText(plant.getCuidado());
                        estadoET.setText(plant.getEstado());

                        ProgressDialog progressDialog = new ProgressDialog(PlantActivity.this);

                        //////////////// Editar los datos  ////////////////////
                        AlertDialog alertDialog = new AlertDialog.Builder(PlantActivity.this)
                                .setTitle("Edit")
                                .setView(view)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Objects.requireNonNull(nombreET.getText()).toString().isEmpty()) {
                                            nombreLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(tipoET.getText()).toString().isEmpty()) {
                                            tipoLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(especieET.getText()).toString().isEmpty()) {
                                            especieLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(cuidadoET.getText()).toString().isEmpty()) {
                                            cuidadoLayout.setError("This field is required!");
                                        } else if (Objects.requireNonNull(estadoET.getText()).toString().isEmpty()) {
                                            estadoLayout.setError("This field is required!");
                                        }
                                        /*///////////////////////////////////////////////////////////
                                        else if (Objects.requireNonNull(autoCompleteTextView.getText()).toString().isEmpty()) {
                                            textInputLayout.setError("Select an option");
                                        }
                                        //////////////////////////////////////////////////////////*/
                                        else {
                                            progressDialog.setMessage("Saving...");
                                            progressDialog.show();
                                            Plant plant1 = new Plant();
                                            plant1.setNombre(nombreET.getText().toString());
                                            plant1.setTipo(tipoET.getText().toString());
                                            plant1.setEspecie(especieET.getText().toString());
                                            plant1.setCuidado(cuidadoET.getText().toString());
                                            plant1.setEstado(estadoET.getText().toString());
                                            database.getReference().child("plants").child(plant.getKey()).setValue(plant1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    progressDialog.dismiss();
                                                    dialogInterface.dismiss();
                                                    Toast.makeText(PlantActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(PlantActivity.this, "There was an error while saving data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                })
                                //////////// Cerrar Dialog /////////////////
                                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                ////////// Eliminar Registro //////////////
                                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        progressDialog.setTitle("Deleting...");
                                        progressDialog.show();
                                        database.getReference().child("plants").child(plant.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(PlantActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }).create();
                        alertDialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}