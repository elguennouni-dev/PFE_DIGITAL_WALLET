package pfe.digitalWallet.core.loginhistory.dto;

import java.time.LocalDateTime;

public record LoginHistoryDto(
        Long id,
        String device,
        String ipAddress,
        String location,
        LocalDateTime loginTime,
        LocalDateTime logoutTime
) {
    public LoginHistoryDto withDevice(String newDevice) {
        return new LoginHistoryDto(id, newDevice, ipAddress, location, loginTime, logoutTime);
    }

    public LoginHistoryDto withIpAddress(String newIpAddress) {
        return new LoginHistoryDto(id, device, newIpAddress, location, loginTime, logoutTime);
    }

    public LoginHistoryDto withLocation(String newLocation) {
        return new LoginHistoryDto(id, device, ipAddress, newLocation, loginTime, logoutTime);
    }

    public LoginHistoryDto withLoginTime(LocalDateTime newLoginTime) {
        return new LoginHistoryDto(id, device, ipAddress, location, newLoginTime, logoutTime);
    }

    public LoginHistoryDto withLogoutTime(LocalDateTime newLogoutTime) {
        return new LoginHistoryDto(id, device, ipAddress, location, loginTime, newLogoutTime);
    }
}
