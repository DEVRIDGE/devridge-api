//package io.devridge.api.newdomain.companyinfo;
//
//import io.devridge.api.newdomain.BaseTimeEntity;
//import lombok.AccessLevel;
//import lombok.Builder;
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
//public class Company extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "company_id")
//    private Long id;
//
//    @Column(name = "company_name")
//    private String name;
//
//
//    @Builder
//    public Company(Long id, String name, String logo) {
//        this.id = id;
//        this.name = name;
//    }
//}
