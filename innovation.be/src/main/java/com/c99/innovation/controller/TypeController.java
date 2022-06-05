package com.c99.innovation.controller;

import com.c99.innovation.dto.ResponseDTO;
import com.c99.innovation.entity.Type;
import com.c99.innovation.service.TypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.c99.innovation.common.constant.Constants.API_V1;
import static com.c99.innovation.common.constant.Constants.FETCHING_SUCCESSFULLY;

@Api(value = "Type Controller")
@RestController
@RequestMapping(value = API_V1 + "/type")
public class TypeController {

    private TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @ApiOperation(value = "Get all types")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = FETCHING_SUCCESSFULLY)})
    @GetMapping()
    public ResponseDTO<List<Type>> getAllTypes() {
        List<Type> allType = this.typeService.getAll();
        return new ResponseDTO<>(HttpStatus.OK, FETCHING_SUCCESSFULLY, allType);
    }
}
