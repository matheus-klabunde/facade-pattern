package com.mtk.facadepattern.repository;

import com.mtk.facadepattern.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long>
{
}
