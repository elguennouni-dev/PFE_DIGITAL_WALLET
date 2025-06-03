package pfe.digitalWallet.core.appuser.dao;

public record LoginRequest (
        String username,
        String password,
        String device,
        String location,
        String ipAddress
){
    public LoginRequest withUsername(String newUsername) {
        return new LoginRequest(newUsername, this.password, this.device, this.location, this.ipAddress);
    }

    public LoginRequest withPassword(String newPassword) {
        return new LoginRequest(this.username, newPassword, this.device, this.location, this.ipAddress);
    }

    public LoginRequest withDevice(String newDevice) {
        return new LoginRequest(this.username, this.password, newDevice, this.location, this.ipAddress);
    }

    public LoginRequest withLocation(String newLocation) {
        return new LoginRequest(this.username, this.password, this.device, newLocation, this.ipAddress);
    }

}
