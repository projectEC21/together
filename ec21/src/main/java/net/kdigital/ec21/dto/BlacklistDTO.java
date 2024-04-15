package net.kdigital.ec21.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.kdigital.ec21.dto.check.ReportCategory;
import net.kdigital.ec21.entity.BlacklistEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BlacklistDTO {
    private Long blacklistId;
    private String customerId;
    private String compName;
    private String remoteIp;
    private String country;
    private ReportCategory blackType;
    private String blackReason;
    private LocalDateTime inputDate;

    public static BlacklistDTO toDTO(BlacklistEntity entity) {
        return BlacklistDTO.builder()
                .blacklistId(entity.getBlacklistId())
                .customerId(entity.getCustomerId())
                .compName(entity.getCompName())
                .remoteIp(entity.getRemoteIp())
                .country(entity.getCountry())
                .blackType(entity.getBlackType())
                .blackReason(entity.getBlackReason())
                .inputDate(entity.getInputDate())
                .build();
    }

<<<<<<< HEAD
=======
    public BlacklistDTO(String customerId, String compName, String remoteIp, String country, ReportCategory blackType,
            String blackReason) {
        this.customerId = customerId;
        this.compName = compName;
        this.remoteIp = remoteIp;
        this.country = country;
        this.blackType = blackType;
        this.blackReason = blackReason;
    }

>>>>>>> 8f56eeeebe6110c2450d22e56b2a6b03836acdd6
}
