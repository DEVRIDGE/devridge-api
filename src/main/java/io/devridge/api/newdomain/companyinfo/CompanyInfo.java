//package io.devridge.api.newdomain.companyinfo;
//
//import io.devridge.api.newdomain.BaseTimeEntity;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import javax.persistence.*;
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@EntityListeners(AuditingEntityListener.class)
//@Entity
//public class CompanyInfo extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "company_info_id")
//    private Long id;
//
//    @Column(name = "company_info_content")
//    private String content;
//
//    @JoinColumn(name = "job_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Job job;
//
//    @JoinColumn(name = "service_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Service service;
//
//
//    @JoinColumn(name = "company_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Company company;
//}
