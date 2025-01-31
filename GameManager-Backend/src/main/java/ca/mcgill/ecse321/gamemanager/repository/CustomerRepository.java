package ca.mcgill.ecse321.gamemanager.repository;

import ca.mcgill.ecse321.gamemanager.model.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    public Customer findCustomerByEmail(String Email);
    //public Customer deleteByEmail(String Email);
   // public Customer findCustomerByName(String name );

}