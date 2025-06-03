package pfe.digitalWallet.core.loginhistory.dto;

import java.time.LocalDateTime;

public record LoginHistoryDto(
        Long id,
        String device,
        String ipAddress,
        String location,
        String reason,
        LocalDateTime loginTime,
        LocalDateTime logoutTime
) {
    public LoginHistoryDto withDevice(String newDevice) {
        return new LoginHistoryDto(id, newDevice, ipAddress, location, reason, loginTime, logoutTime);
    }

    public LoginHistoryDto withIpAddress(String newIpAddress) {
        return new LoginHistoryDto(id, device, newIpAddress, location, reason, loginTime, logoutTime);
    }

    public LoginHistoryDto withLocation(String newLocation) {
        return new LoginHistoryDto(id, device, ipAddress, newLocation, reason, loginTime, logoutTime);
    }

    public LoginHistoryDto withLoginTime(LocalDateTime newLoginTime) {
        return new LoginHistoryDto(id, device, ipAddress, location, reason, newLoginTime, logoutTime);
    }

    public LoginHistoryDto withLogoutTime(LocalDateTime newLogoutTime) {
        return new LoginHistoryDto(id, device, ipAddress, location, reason, loginTime, newLogoutTime);
    }

    public LoginHistoryDto withReason(String newReason) {
        return new LoginHistoryDto(id, device, ipAddress, location, newReason, loginTime, logoutTime);
    }
}
