package ly.muasica.base.auth;

import org.springframework.data.repository.CrudRepository;

/**
 * Database Object for the Authorization.
 * @author Daniel Gabl
 *
 */
public interface AuthRepository extends CrudRepository<User, Long>  {
    
}