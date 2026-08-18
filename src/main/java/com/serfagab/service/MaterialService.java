package com.serfagab.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.serfagab.entities.Material;
import com.serfagab.repository.MaterialRepository;
import java.util.List;

@Service
public class MaterialService {
    private final MaterialRepository materialRepository;

    @PersistenceContext
    private EntityManager em;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @Transactional
    public void registrarLote(List<Material> materiales) {
        int i = 0;
        for (Material m : materiales) {
            em.persist(m);
            i++;
            if (i % 10 == 0) {
                em.flush();
                em.clear();
            }
        }
    }

    public List<Material> listarTodos() {
        return em.createQuery("SELECT m FROM Material m WHERE m.activo = true", Material.class)
                .setHint("org.hibernate.fetchSize", 5) // Trae todos, pero lee de 5 en 5
                .getResultList();
    }

    public List<Material> buscar(String nombre) {
        return materialRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }
}
