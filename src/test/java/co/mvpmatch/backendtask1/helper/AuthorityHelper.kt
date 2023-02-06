package co.mvpmatch.backendtask1.helper

import co.mvpmatch.backendtask1.config.ROLE_ADMIN
import co.mvpmatch.backendtask1.config.ROLE_BUYER
import co.mvpmatch.backendtask1.config.ROLE_SELLER
import co.mvpmatch.backendtask1.domain.Authority
import co.mvpmatch.backendtask1.repository.AuthoritiyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@SpringBootTest
class AuthorityHelper {
    @Autowired
    private lateinit var authorityRepository: AuthoritiyRepository

    @Transactional
    fun insertAuthoritiesIfNotExist() {
        if (authorityRepository.existsByName(ROLE_ADMIN)) addAuthority(ROLE_ADMIN)
        if (authorityRepository.existsByName(ROLE_BUYER)) addAuthority(ROLE_BUYER)
        if (authorityRepository.existsByName(ROLE_SELLER)) addAuthority(ROLE_SELLER)
    }

    @Transactional
    fun addAuthority(name: String) {
        authorityRepository.save(Authority(name))
    }
}
