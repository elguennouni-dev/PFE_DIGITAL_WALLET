package pfe.digitalWallet.shared.dto;


public record ApiResponse<T>(
     boolean success,
     String message,
     T data
    )
{
    public ApiResponse<T> withSuccess(boolean newSuccess) {
        return new ApiResponse<>(newSuccess, this.message, this.data);
    }

    public ApiResponse<T> withMessage(String newMessage) {
        return new ApiResponse<>(this.success, newMessage, this.data);
    }

    public ApiResponse<T> withData(T newData) {
        return new ApiResponse<>(this.success, this.message, newData);
    }
}
