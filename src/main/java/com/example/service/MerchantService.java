package com.example.service;

import com.example.dto.MerchantDto;
import com.example.model.Merchant;
import com.example.repository.MerchantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantDto getByName2(String name) {
        Merchant merchant = merchantRepository.findByName(name);

        return new MerchantDto(merchant.getName(), merchant.getType(), merchant.getMobileNumber(), merchant.getAddress(), merchant.getRate(), merchant.getNotes());
    }

    public MerchantDto getByName(String name) {
        Merchant merchant = merchantRepository.findByName(name);

        return new MerchantDto(merchant.getName(), merchant.getType(), merchant.getMobileNumber(), merchant.getAddress(), merchant.getRate(), merchant.getNotes());
    }

    Merchant getMerchantByName(String name) {
        return merchantRepository.findByName(name);
    }

    public List<MerchantDto> getAllMerchants() {
        return merchantRepository.findAll().stream().
                map(merchant -> new MerchantDto(merchant.getName(), merchant.getType(), merchant.getMobileNumber(), merchant.getAddress(), merchant.getRate(), merchant.getNotes())).toList();
    }

    public String createMerchant(Merchant merchant) {
        if (merchantRepository.existsByName(merchant.getName()))
            throw new IllegalArgumentException("Merchant already exist");
        else merchantRepository.save(merchant);
        return "The Merchant: " + merchant.getName() + " saved successfully";
    }

    @Transactional
    public String deleteMerchant(String name) {
        merchantRepository.deleteByName(name);
        return "The Merchant: " + name + " deleted successfully";
    }

    public String createMerchantList(List<Merchant> merchants) {
        List<Merchant> p = merchants.stream().filter(merchant -> !merchantRepository.existsByName(merchant.getName())).toList();
        if (p.isEmpty())
            return "All merchants already exist";
        else {
            String S = p.stream().map(m -> m.getName() + ' ').toString();
            merchantRepository.saveAll(p);
            return "The Merchants: " + S + " saved successfully";
        }

    }
}