package com.example.ozzy.user.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ServiceTermsRequest {

    private int userSeq;
    private String agreed1;
    private String agreed2;
    private String agreed3;
    private String agreed4;

    public ServiceTermsRequest() {}

    public ServiceTermsRequest(int userSeq, String agreed1, String agreed2, String agreed3, String agreed4) {
        this.userSeq = userSeq;
        this.agreed1 = agreed1;
        this.agreed2 = agreed2;
        this.agreed3 = agreed3;
        this.agreed4 = agreed4;
    }

    public static ServiceTermsRequest firstAdd(int userSeq) {
        return new ServiceTermsRequest(userSeq,"N","N","N","N");
    }
}
