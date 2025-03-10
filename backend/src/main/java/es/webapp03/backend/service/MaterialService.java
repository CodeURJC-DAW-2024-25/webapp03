package es.webapp03.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.webapp03.backend.model.Material;
import es.webapp03.backend.repository.MaterialRepository;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public void save(Material material) {
        materialRepository.save(material);
    }

    public Optional<Material> findById(Long materialId) {
        return materialRepository.findById(materialId);
    }

    public void deleteById(Long materialId) {
        materialRepository.deleteById(materialId);
    }

}
