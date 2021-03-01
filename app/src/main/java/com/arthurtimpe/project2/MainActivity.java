package com.arthurtimpe.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import android.content.DialogInterface;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AccountDatabase accountdb;
    private BookDatabase bookdb;
    private LogDatabase logdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button accountBtn = (Button) findViewById(R.id.createAccountBtn);
        Button holdBtn = (Button) findViewById(R.id.placeHoldBtn);
        Button manageBtn = (Button) findViewById(R.id.manageSystemBtn);

        accountdb = AccountDatabase.getInstance(this);
        accountdb.populateInitialData();

        bookdb = BookDatabase.getInstance(this);
        bookdb.populateInitialData();

        logdb = LogDatabase.getInstance(this);

        // CREATE ACCOUNT
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input1 = new EditText(MainActivity.this);
                EditText input2 = new EditText(MainActivity.this);
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(input1);
                layout.addView(input2);
                String message = "Please enter a username and password";
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Create Account");
                alert.setMessage(message);
                alert.setView(layout);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        createValidAccount(input1.getText().toString(), input2.getText().toString());
                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        // CREATE HOLD
        holdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner = new Spinner(MainActivity.this);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainActivity.this,
                        R.array.genre_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Make Hold");
                alert.setMessage("Please select a Genre");
                alert.setView(spinner);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        createHold(spinner.getSelectedItem().toString());
                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        // MANAGE SYSTEM
        manageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input1 = new EditText(MainActivity.this);
                EditText input2 = new EditText(MainActivity.this);
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(input1);
                layout.addView(input2);
                String message = "Please enter the admin username and password";
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Admin Login");
                alert.setMessage(message);
                alert.setView(layout);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        manageSystem(input1.getText().toString(), input2.getText().toString());
                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
    }

    public void createHold(String genre) {
        if (bookdb.isGenreEmpty(genre)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Out of Stock");
            alert.setMessage("Sorry but there are no books available under that genre");
            alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            return;
        }

        EditText input = new EditText(MainActivity.this);
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        String message = "Select one of these books to make a hold (enter the book ID)\n";
        for(int i = 0; i < bookdb.book().count(); i++) {
            if (bookdb.book().getAll().get(i).getGenre().equals(genre)) {
                message += i + 1 + ". " + bookdb.book().getAll().get(i).getTitle() + "\n"; // the book ID shown is always offset by 1
            }
        }
        alert.setTitle("Make Hold");
        alert.setMessage(message);
        alert.setView(input);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (bookdb.book().getAll().get(
                        Integer.parseInt(
                        input.getText().toString()) - 1)
                        .getGenre().equals(genre)) { // Checks if the selected number is of the genre
                    logInHold(Integer.parseInt(input.getText().toString()) - 1);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Book Id", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    public void logInHold(int bookId) {
        EditText input1 = new EditText(MainActivity.this);
        EditText input2 = new EditText(MainActivity.this);
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input1);
        layout.addView(input2);
        String message = "Please enter a username and password";
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Log In");
        alert.setMessage(message);
        alert.setView(layout);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (accountdb.validAccountLogin(input1.getText().toString(), input2.getText().toString())) {
                    String message = "User: " + input1.getText().toString() + "\nTitle: " + bookdb.book().getAll().get(bookId).getTitle() + "\nReservation Number: " + logdb.generateReservationNumber();
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Is this correct?");
                    alert.setMessage(message);
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            logdb.log().addLog(new Log("Place hold", input1.getText().toString(), logdb.generateReservationNumber()));
                            bookdb.book().delete(bookdb.book().getAll().get(bookId));
                            Toast toast = Toast.makeText(getApplicationContext(), "Book successfully reserved", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Login failed. No such user exists.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    public void manageSystem(String username, String password) {
        if (username.equals(accountdb.account().getAll().get(0).getUsername())
                && password.equals(accountdb.account().getAll().get(0).getPassword())) {
            Button logInfoBtn = new Button(MainActivity.this);
            Button addBookBtn = new Button(MainActivity.this);
            logInfoBtn.setText(R.string.log_btn);
            addBookBtn.setText(R.string.add_book_btn);
            LinearLayout layout = new LinearLayout(MainActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(logInfoBtn);
            layout.addView(addBookBtn);
            String message = "Welcome back, admin. Please select an option.";
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Manage System");
            alert.setMessage(message);
            alert.setView(layout);
            alert.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();

            logInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Logs");
                    alert.setMessage(logdb.toString());
                    alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            });

            addBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText input1 = new EditText(MainActivity.this);
                    EditText input2 = new EditText(MainActivity.this);
                    EditText input3 = new EditText(MainActivity.this);
                    LinearLayout layout = new LinearLayout(MainActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.addView(input1);
                    layout.addView(input2);
                    layout.addView(input3);
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Add Book");
                    alert.setMessage("Please enter the Title, Author and Genre of the new book");
                    alert.setView(layout);
                    alert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            addBook(input1.getText().toString(), input2.getText().toString(), input3.getText().toString());
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            });
        }
        else { // Wrong username and password
            Toast toast = Toast.makeText(getApplicationContext(), "Login failed. Information incorrect", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void createValidAccount(String username, String password) {
        if (username.equals("") || password.equals("")) { // Checking if fields were left blank
            Toast toast = Toast.makeText(getApplicationContext(), "Required field left blank, please try again", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if (accountdb.accountExists(username)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Username invalid. User already exists", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        Toast toast = Toast.makeText(getApplicationContext(), "User successfully created", Toast.LENGTH_LONG);
        toast.show();
        accountdb.account().addAccount(new Account(username, password));
        logdb.log().addLog(new Log("New account", username));
    }

    public void addBook(String title, String author, String genre) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Is this information correct?");
        alert.setMessage(title + ", " + author + ", " + genre);
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                bookdb.book().addBook(new Book(title, author, genre));
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
}