package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;


@Slf4j
@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    @Override
    public void createBillingAccount(
            BillingRequest billingRequest,
            StreamObserver<BillingResponse> responseObserver
    ) {
        log.info("Creating billing account request");
        // Business logic

        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("1234")
                .setStatus("ACTIVE")
                .build();

        log.info("Created billing account response");

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
