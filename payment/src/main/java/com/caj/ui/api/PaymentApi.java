package com.caj.ui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caj.domain.payment.PaymentService;
import com.caj.ui.dto.GenericResponseDTO;
import com.caj.ui.dto.PaymentRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.micrometer.tracing.Tracer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1")
public class PaymentApi {
	
	@Autowired
	PaymentService paymentService;

	@Autowired
	Tracer tracer;
	
	/**
	 * 訂位 API
	 * 
	 * @param reservationRequestDTO
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@Operation(summary = "付款", description = "付款")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "操作成功"),
			@ApiResponse(responseCode = "500", description = "操作失敗") })
	@PostMapping(value = "/payments", produces = "application/json")
	public ResponseEntity<GenericResponseDTO> reservation(@RequestBody PaymentRequestDTO paymentRequestDTO)
			throws JsonMappingException, JsonProcessingException {
		log.info("POST /payments");
		paymentService.pay(paymentRequestDTO.getPnr(), paymentRequestDTO.getPaymentMethod(), paymentRequestDTO.getPaymentAmt());
		return new ResponseEntity<>(
				GenericResponseDTO.builder()
				.code(String.valueOf(HttpStatus.OK.value()))
				.message(HttpStatus.OK.name())
				.traceId(tracer.currentSpan().context().traceId())
				.build(), HttpStatus.OK);
	}

}
