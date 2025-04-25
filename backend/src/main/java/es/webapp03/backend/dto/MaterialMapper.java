package es.webapp03.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import es.webapp03.backend.model.Material;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

    @Mapping(target = "id", source = "id")
    MaterialBasicDTO toDTO(Material material);

    List<MaterialBasicDTO> toDTOs(Collection<Material> materials);

    @Mapping(target = "course", ignore = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "file", ignore = true)
    Material toDomain(MaterialBasicDTO materialDTO);

    @Mapping(target = "id", source = "id")
    MaterialDTO toMaterialDTO(Material material);
}
