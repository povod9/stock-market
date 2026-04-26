package com.stock.market.mapper;

import com.stock.market.dto.AuditLogDto;
import com.stock.market.entity.AuditLogEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    AuditLogDto auditEntityToDto(AuditLogEntity auditLogEntity);
}
