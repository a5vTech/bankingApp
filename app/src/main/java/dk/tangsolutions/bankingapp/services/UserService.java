package dk.tangsolutions.bankingapp.services;
import com.google.firebase.auth.FirebaseAuth;


public class UserService {

private FirebaseAuth mAuth;
    private String TAG = "USERSERVICE";




    public UserService(){
        mAuth = FirebaseAuth.getInstance();
    }
}
