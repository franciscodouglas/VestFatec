package br.com.vestfatec.candidate;

import br.com.vestfatec.candidate.dto.CandidateRequest;
import br.com.vestfatec.candidate.dto.CandidateResponse;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Caching(evict = {
            @CacheEvict(value = "candidates", key = "'all'"),
            @CacheEvict(value = "candidates", allEntries = true)
    })
    @Transactional
    public CandidateResponse create(CandidateRequest request) {
        if (candidateRepository.existsByCpf(request.cpf())) {
            throw new IllegalArgumentException("CPF ja cadastrado");
        }
        if (candidateRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }

        Candidate candidate = new Candidate(
                request.fullName(),
                request.cpf(),
                request.email(),
                request.phone()
        );

        return CandidateResponse.from(candidateRepository.save(candidate));
    }

    @Cacheable(value = "candidates", key = "'all'")
    @Transactional(readOnly = true)
    public List<CandidateResponse> findAll() {
        return candidateRepository.findAll().stream()
                .map(CandidateResponse::from)
                .toList();
    }

    @Cacheable(value = "candidates", key = "#id")
    @Transactional(readOnly = true)
    public CandidateResponse findById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidato nao encontrado"));
        return CandidateResponse.from(candidate);
    }

    @Caching(evict = {
            @CacheEvict(value = "candidates", key = "'all'"),
            @CacheEvict(value = "candidates", key = "#id")
    })
    @Transactional
    public CandidateResponse update(Long id, CandidateRequest request) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidato nao encontrado"));

        if (candidateRepository.existsByCpfAndIdNot(request.cpf(), id)) {
            throw new IllegalArgumentException("CPF ja cadastrado");
        }
        if (candidateRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new IllegalArgumentException("Email ja cadastrado");
        }

        candidate.update(request.fullName(), request.cpf(), request.email(), request.phone());
        return CandidateResponse.from(candidateRepository.save(candidate));
    }

    @Caching(evict = {
            @CacheEvict(value = "candidates", key = "'all'"),
            @CacheEvict(value = "candidates", key = "#id")
    })
    @Transactional
    public void delete(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new IllegalArgumentException("Candidato nao encontrado");
        }
        candidateRepository.deleteById(id);
    }
}
