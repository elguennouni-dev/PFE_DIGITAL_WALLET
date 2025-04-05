package pfe.digitalWallet.shared.dto;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
