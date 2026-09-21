package ifpb.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public abstract class JPAUtil {
    public static EntityManagerFactory entityManagerFactory;

    public static EntityManager gerarEntityManager() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("sistema-gerenciador-teatro");
        }

        return entityManagerFactory.createEntityManager();
    }
}
