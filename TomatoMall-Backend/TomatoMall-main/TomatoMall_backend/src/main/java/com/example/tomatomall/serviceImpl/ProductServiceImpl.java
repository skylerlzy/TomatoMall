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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockpileRepository stockpileRepository;

    @Autowired
    private HttpServletRequest request;

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
        Product product = productRepository.findById(Integer.parseInt(id)).get();
        if (product == null) {
            throw TomatoMallException.productNotFound();
        }
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
        BeanUtils.copyProperties(productVO, product);
        //细节之处，通过这一步获取有效的id
        Product savedProduct = productRepository.save(product);


        //System.out.println(savedProduct.getSpecifications());
        //System.out.println("/n/n/n/n/n/n/n/n/n/n/n");

        // 初始化库存
        Stockpile stockpile = new Stockpile();
        stockpile.setProductId(savedProduct.getId());
        stockpile.setAmount(0);
        stockpile.setFrozen(0);

        stockpileRepository.save(stockpile);

        return convertToVO(savedProduct);
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
    public String amount(String productId, Integer amount, Integer frozen) {
        if (!productRepository.existsById(Integer.parseInt(productId))) {
            throw TomatoMallException.productNotFound();
        }
        Stockpile stockpile = stockpileRepository.findByProductId(Integer.parseInt(productId));
        stockpile.setAmount(amount);
        stockpile.setFrozen(frozen);
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
