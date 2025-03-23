package es.webapp03.backend.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import es.webapp03.backend.model.Material;
import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

    // ✅ Asegurar que el ID se mapea correctamente
    @Mapping(target = "id", source = "id")
    MaterialBasicDTO toDTO(Material material);

    // ✅ MapStruct automáticamente usará `toDTO(Material material)` para mapear cada elemento
    List<MaterialBasicDTO> toDTOs(Collection<Material> materials);

    // ✅ Asegurar que la conversión a `Material` no pierda datos importantes
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "file", ignore = true)
    @Mapping(target = "id", source = "id") // 📌 Asegurar que el ID también se mapea al revés
    Material toDomain(MaterialBasicDTO materialDTO);
}
