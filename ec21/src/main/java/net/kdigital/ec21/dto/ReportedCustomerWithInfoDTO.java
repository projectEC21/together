package net.kdigital.ec21.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.kdigital.ec21.dto.check.CustomerGubun;
import net.kdigital.ec21.dto.check.ReportCategory;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
// 신고 회원 목록 조회를 위한 DTO
public class ReportedCustomerWithInfoDTO {
    private String reportedId;
    private Long reportCustomerId;
    private int reportedCount;
    private CustomerGubun customerGubun;
    private String customerName;
    private String customerComp;
    private String customerDepartment;
    private String remoteIp;
    private ReportCategory reportCategory;
    private String reportReason;
}
