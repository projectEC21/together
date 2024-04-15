package net.kdigital.ec21.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.kdigital.ec21.dto.check.ReportCategory;
import net.kdigital.ec21.dto.check.YesOrNo;
import net.kdigital.ec21.entity.ReportCustomerEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReportCustomerDTO {
    private Long reportCustomerId;
    private String reportedId;
    private ReportCategory reportCategory;
    private String reportReason;
    private LocalDateTime reportDate;
    private YesOrNo managerCheck;

    public static ReportCustomerDTO toDTO(ReportCustomerEntity entity) {
        return ReportCustomerDTO.builder()
                .reportCustomerId(entity.getReportCustomerId())
<<<<<<< HEAD
                .reportedId(entity.getReportedId())
=======
                .reportedId(entity.getReported_id())
>>>>>>> 8f56eeeebe6110c2450d22e56b2a6b03836acdd6
                .reportCategory(entity.getReportCategory())
                .reportReason(entity.getReportReason())
                .reportDate(entity.getReportDate())
                .managerCheck(entity.getManagerCheck())
                .build();
    }

}
