package ru.mifi.practice.val5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.mifi.practice.val5.mapper.dto.VersionDto;
import ru.mifi.practice.val5.model.VersionModel;

@Mapper
public interface VersionMapper {
    VersionMapper DEFAULT = Mappers.getMapper(VersionMapper.class);

    @Mapping(source = "version", target = "version")
    @Mapping(source = "buildDateTime", target = "releaseDate")
    VersionDto toVersionDto(VersionModel v);
}
