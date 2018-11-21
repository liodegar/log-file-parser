package com.wallet.parser.persistence;

import com.wallet.parser.model.WebServerRequest;
import org.springframework.data.repository.CrudRepository;

/**
 * This will be AUTO IMPLEMENTED by Spring into a Bean called WebServerRequestRepository to handle CRUD operations
 * Created by Liodegar on 11/20/2018.
 */
public interface WebServerRequestRepository extends CrudRepository<WebServerRequest, Long> {

}
