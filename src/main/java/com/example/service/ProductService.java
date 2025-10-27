package com.example.service;

import com.example.dto.ProductDto;
import com.example.model.Products;
import com.example.repository.GoodsRepository;
import com.example.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductsRepository productsRepository;


    public List<Products> getAllByItemId(long Itemid) {

        return productsRepository.findByItemId(Itemid);}}
//    }

//    public GoodsDto getByName(String name) {
//        Goods goods = goodsRepository.findByName(name);
//
//        return new GoodsDto(goods.getName(), goods.getType(), goods.getSubtype(), goods.getDescr());
//    }
//
//    public List<GoodsDto> getAllGoodss() {
//        return goodsRepository.findAll().stream().
//                map(goods -> new GoodsDto(goods.getName(), goods.getType(), goods.getSubtype(), goods.getDescr())).toList();
//    }
//
////    public List<Goods> getAllProducts2() {
////        return goodsRepository.findAll();
////    }
//
////    public GoodsDto createGoods(GoodsDto dto) {
////        Goods goods = new Goods(dto.getName(),dto.getDesc(),dto.getType(),dto.getSubtype());
////        goodsRepository.save(new Goods(dto.getName(),dto.getDesc(),dto.getType(),dto.getSubtype()));
////        return new GoodsDto(goods);
////    }
//
//    public String createGoods(Goods goods) {
//        if (goodsRepository.existsByName(goods.getName()))
//            throw new IllegalArgumentException("Goods already exist");
//        else goodsRepository.save(goods);
//        return "The Goods: " + goods.getName() + " saved successfully";
//    }
//
//    @Transactional
//    public String deleteGoods(String name) {
//        goodsRepository.deleteByName(name);
//        return "The Goods: " + name + " deleted successfully";
//    }
//
//    public String createGoodsList(List<Goods> goodss) {
//        List<Goods> p = goodss.stream().filter(goods -> !goodsRepository.existsByName(goods.getName())).toList();
//        if (p.isEmpty())
//            return "All goodss already exist";
//        else {
//            goodsRepository.saveAll(p);
//            return "The Goodss: " + p + " saved successfully";
//        }
//
//    }