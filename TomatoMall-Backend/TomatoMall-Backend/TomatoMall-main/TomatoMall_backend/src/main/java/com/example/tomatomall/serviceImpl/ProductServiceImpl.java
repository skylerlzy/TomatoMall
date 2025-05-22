package com.example.tomatomall.serviceImpl;


import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.repository.ProductRepository;
import com.example.tomatomall.repository.StockpileRepository;
import com.example.tomatomall.service.ProductService;
import com.example.tomatomall.vo.ProductVO;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.vo.StockpileVO;
import com.example.tomatomall.po.Stockpile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockpileRepository stockpileRepository;

    @Autowired
    private HttpServletRequest request;

    public Double addRate(Double rate, Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(TomatoMallException::productNotFound);

        List<Double> rateList = product.getRateList();
        if (rateList == null) {
            rateList = new ArrayList<>();
        }

        rateList.add(rate);
        product.setRateList(rateList);

        double average = rateList.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        productRepository.save(product);

        if (average == product.getRate()) {
            return average;
        } else {
            throw TomatoMallException.wrongRate();
        }
    }

    public List<ProductVO> getSalesList() {
        return productRepository.findAll()
                .stream()
                .sorted((product1, product2) -> Integer.compare(product2.getSales(), product1.getSales()))  // 按 rate 降序排序
                .limit(3)  // 只取前三个
                .map(product -> {
                    ProductVO productVO = product.toVO();
                    // Ensure specifications are loaded
                    productVO.setSpecifications(product.specificationsToVO());
                    return productVO;
                })
                .collect(Collectors.toList());
    }

    public List<ProductVO> getRateList() {
        return productRepository.findAll()
                .stream()
                .sorted((product1, product2) -> Double.compare(product2.getRate(), product1.getRate()))  // 按 rate 降序排序
                .limit(3)  // 只取前三个
                .map(product -> {
                    ProductVO productVO = product.toVO();
                    // Ensure specifications are loaded
                    productVO.setSpecifications(product.specificationsToVO());
                    return productVO;
                })
                .collect(Collectors.toList());
    }

   @Override
    public List<ProductVO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(product -> {
                    ProductVO productVO = product.toVO();
                    // Ensure specifications are loaded
                    productVO.setSpecifications(product.specificationsToVO());
                    return productVO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductVO findById(String id) {
        Product product = productRepository.findById(Integer.parseInt(id))
                .orElseThrow(TomatoMallException::productNotFound);
        return product.toVO();
    }

    @Override
    @Transactional
    public String update(ProductVO productVO) {
        return productRepository.findById(productVO.getId())
                .map(product -> {
                    BeanUtils.copyProperties(productVO, product);
                    productRepository.save(product);
                    return "更新成功";
                })
                .orElseThrow(TomatoMallException::productNotFound);
    }

    @Override
    @Transactional
    public ProductVO add(ProductVO productVO) {
        Product product = new Product();
        product.setDescription(productVO.getDescription());
        product.setDetail(productVO.getDetail());
        product.setPrice(productVO.getPrice());
        product.setCover(productVO.getCover());
        product.setRate(productVO.getRate());
        product.setTitle(productVO.getTitle());

        Product savedProduct = productRepository.save(product);
        Integer product_id = savedProduct.getId();
        product.setId(product_id);

        List<Product.SpecificationPO> specs = Optional.ofNullable(productVO.getSpecifications())
                .orElse(Collections.emptyList())  // 如果 getSpecifications() 返回 null，使用一个空的 List
                .stream()
                .map(specDTO -> {
                    Product.SpecificationPO spec = new Product.SpecificationPO();
                    spec.setItem(specDTO.getItem());
                    spec.setValue(specDTO.getValue());
                    spec.setProductId(product_id);
                    // spec.setProduct(product); // 关键：设置关联的 Product
                    return spec;
                })
                .collect(Collectors.toList());

        savedProduct.setSpecifications(specs);
        savedProduct = productRepository.save(savedProduct);

        ProductVO reVO = savedProduct.toVO();
        // Ensure specifications are loaded
        reVO.setSpecifications(savedProduct.specificationsToVO());
        return reVO;
    }

    @Override
    @Transactional
    public String delete(String id) {
        if (productRepository.existsById(Integer.parseInt(id))) {
            productRepository.deleteById(Integer.parseInt(id));
            stockpileRepository.deleteByProductId(Integer.parseInt(id));
            return "删除成功";
        } else {
            throw TomatoMallException.productNotFound();
        }
    }

    @Override
    @Transactional
    public String amount(String productId, Integer amount) {
        if (!productRepository.existsById(Integer.parseInt(productId))) {
            throw TomatoMallException.productNotFound();
        }
        Stockpile stockpile = stockpileRepository.findByProductId(Integer.parseInt(productId));
        if (stockpile == null) {
            Stockpile stock = new Stockpile();
            stock.setProductId(Integer.parseInt(productId));
            stock.setAmount(amount);
            stock.setFrozen(0);
            stockpileRepository.save(stock);
            return "调整库存成功";
        }
        stockpile.setAmount(amount);
        stockpile.setFrozen(0);
        stockpileRepository.save(stockpile);
        return "调整库存成功";
    }

    @Override
    public StockpileVO findAmount(String productId) {
        StockpileVO stockpileVO = new StockpileVO();
        if (!productRepository.existsById(Integer.parseInt(productId))) {
            throw TomatoMallException.productNotFound();
        } else {
            stockpileVO = stockpileRepository.findByProductId(Integer.parseInt(productId)).toVO();
            return stockpileVO;
        }
    }

    private ProductVO convertToVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        return vo;
    }
}
