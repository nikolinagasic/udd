package com.la;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

public class Credentials {
    static String clientId = "AUoKNW_Sjoz8cYq6N_P33gzaxwUscgYfvlVOvP_35MvZdX3JPsqeczYH96m3Lh1ZslUGs7gfBOISjZfs";
    static String secret = "EM_0ZBI_Bg--4EYY11aZnuFGcjfBp8dm4n2xV-1pJtsEA9pPw5wTTPSMfRMt2tCpwqrs2IVUxsNnDd4d";

    // Creating a sandbox environment
    private static PayPalEnvironment environment = new PayPalEnvironment.Sandbox(clientId, secret);

    // Creating a client for the environment
    public static PayPalHttpClient client = new PayPalHttpClient(environment);
}
