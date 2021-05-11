package xyz.fragbots.errors;

/*

* Custom error for when the api returns something invalid

 */

public class ApiReturnError extends Exception {
    public ApiReturnError(String errorMessage) {
        super(errorMessage);
    }
}
