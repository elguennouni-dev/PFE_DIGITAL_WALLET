package pfe.digitalWallet.core.loginattempt.dto;

import java.time.LocalDateTime;

public record LoginAttemptDto(
        Long id,
        String device,
        String ipAddress,
        String location,
        boolean success,
        LocalDateTime attemptTime
) {
    public LoginAttemptDto withDevice(String newDevice) {
        return new LoginAttemptDto(id, newDevice, ipAddress, location, success, attemptTime);
    }

    public LoginAttemptDto withIpAddress(String newIpAddress) {
        return new LoginAttemptDto(id, device, newIpAddress, location, success, attemptTime);
    }

    public LoginAttemptDto withLocation(String newLocation) {
        return new LoginAttemptDto(id, device, ipAddress, newLocation, success, attemptTime);
    }

    public LoginAttemptDto withSuccess(boolean newSuccess) {
        return new LoginAttemptDto(id, device, ipAddress, location, newSuccess, attemptTime);
    }

    public LoginAttemptDto withAttemptTime(LocalDateTime newAttemptTime) {
        return new LoginAttemptDto(id, device, ipAddress, location, success, newAttemptTime);
    }
}