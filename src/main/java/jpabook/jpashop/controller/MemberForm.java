package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {
    //비어 있으면 안된다는 메세지 표시!!!
    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;


    private String city;
    private String street;
    private String zipcode;
}
