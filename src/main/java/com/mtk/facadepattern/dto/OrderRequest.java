package com.mtk.facadepattern.dto;

import java.util.List;

public record OrderRequest(Long clientId, List<Long> productIds)
{
}
