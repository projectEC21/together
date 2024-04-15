package net.kdigital.ec21.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
<<<<<<< HEAD
=======
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
>>>>>>> 8f56eeeebe6110c2450d22e56b2a6b03836acdd6
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.kdigital.ec21.dto.ReportCustomerDTO;
import net.kdigital.ec21.dto.check.ReportCategory;
import net.kdigital.ec21.dto.check.YesOrNo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "report_customer")
public class ReportCustomerEntity {
    @SequenceGenerator(name = "report_customer_seq", sequenceName = "report_customer_seq", allocationSize = 1, initialValue = 1)
    @Id
    @Column(name = "report_customer_id")
    @GeneratedValue(generator = "report_customer_seq")
    private Long reportCustomerId;

<<<<<<< HEAD
    @Column(name = "reported_id", nullable = false)
    private String reportedId;

    @Column(name = "report_category", nullable = false)
=======
    // FK
    @Column(name = "reported_id", nullable = false)
    private String reported_id;

    @Column(name = "report_category", nullable = false)
    @Enumerated(EnumType.STRING)
>>>>>>> 8f56eeeebe6110c2450d22e56b2a6b03836acdd6
    private ReportCategory reportCategory;

    @Column(name = "report_reason")
    private String reportReason;

    @Column(name = "report_date")
    @CreationTimestamp
    private LocalDateTime reportDate;

    @Column(name = "manager_check")
<<<<<<< HEAD
=======
    @Enumerated(EnumType.STRING)
>>>>>>> 8f56eeeebe6110c2450d22e56b2a6b03836acdd6
    private YesOrNo managerCheck;

    public static ReportCustomerEntity toEntity(ReportCustomerDTO dto) {
        return ReportCustomerEntity.builder()
                .reportCustomerId(dto.getReportCustomerId())
<<<<<<< HEAD
                .reportedId(dto.getReportedId())
=======
                .reported_id(dto.getReportedId())
>>>>>>> 8f56eeeebe6110c2450d22e56b2a6b03836acdd6
                .reportCategory(dto.getReportCategory())
                .reportReason(dto.getReportReason())
                .reportDate(dto.getReportDate())
                .managerCheck(dto.getManagerCheck())
                .build();
    }

}
