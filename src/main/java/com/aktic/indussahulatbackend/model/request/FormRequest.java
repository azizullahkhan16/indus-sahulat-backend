package com.aktic.indussahulatbackend.model.request;

import com.aktic.indussahulatbackend.model.entity.Response;
import lombok.Data;
import java.util.List;

@Data
public class FormRequest
{
    private List<Response> responseList;
}
