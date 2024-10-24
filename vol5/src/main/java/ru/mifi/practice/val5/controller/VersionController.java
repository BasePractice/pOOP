package ru.mifi.practice.val5.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mifi.practice.val5.model.VersionModel;

@Slf4j
@RestController
@RequestMapping("/version")
public class VersionController {

    @Operation(summary = "Версия сервиса")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Версия", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = VersionModel.class))
        })
    })
    @GetMapping
    public ResponseEntity<VersionModel> version() {
        return ResponseEntity.ok(VersionModel.current());
    }
}
